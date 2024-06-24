package nl.inholland.BankAPI.CucumberTests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import nl.inholland.BankAPI.Model.DTO.CustomerTransactionsDTO;
import nl.inholland.BankAPI.Model.DTO.LoginRequestDTO;
import nl.inholland.BankAPI.Model.DTO.LoginResponseDTO;
import nl.inholland.BankAPI.Model.Transaction;
import nl.inholland.BankAPI.Model.TransactionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class TransactionControllerStepDefinitions extends CucumberSpringConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(TransactionControllerStepDefinitions.class);

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    private LoginResponseDTO loginResponse;
    private CustomerTransactionsDTO customerTransactionsResponse;
    @Autowired
    private final ObjectMapper objectMapper = new ObjectMapper();

    protected ResponseEntity<LoginResponseDTO> loginResponseEntity;
    protected ResponseEntity<String> customerTransactionResponseEntity;
    protected HttpHeaders httpHeaders = new HttpHeaders();

    public TransactionControllerStepDefinitions() {
        
    }
    private LoginRequestDTO loginDTO;

    // Sara's Code
    @Given("To see my transactions, I login as as Customer with email {string} and password {string}")
    public void toSeeMyTransactionsILoginAsAsCustomerWithEmailAndPassword(String email, String password) {
        logger.info(email + " " + password);
        loginDTO = new LoginRequestDTO(email, password);
        String url = "http://localhost:" + port + "/login";
        loginResponseEntity = restTemplate.postForEntity(url, loginDTO, LoginResponseDTO.class);
        // Store login response
        loginResponse = loginResponseEntity.getBody();
        // Set JWT token in headers for future requests
        httpHeaders.setBearerAuth(loginResponse.getToken());
        // httpHeaders.add("Authorization", "Bearer " + loginResponse.getToken());
    }

    // Sara's Code
    @When("I request to read transactions without account type")
    public void iRequestToReadTransactionsWithoutAccountType() {
        HttpEntity<String> entity = new HttpEntity<>(httpHeaders);
        customerTransactionResponseEntity = restTemplate.exchange("/transactions", HttpMethod.GET, entity,
                String.class);
        logger.info(customerTransactionResponseEntity.toString());
    }

    // Sara's Code
    @And("I receive error message {string}")
    public void iReceiveErrorMessage(String message) {
        assertEquals(message, customerTransactionResponseEntity.getBody());
    }

    // Sara's Code
    @When("I request to read transactions of {string} account")
    public void iRequestToReadTransactionsOfAccount(String accountType) {
        HttpEntity<String> entity = new HttpEntity<>(httpHeaders);
        customerTransactionResponseEntity = restTemplate.exchange("/transactions?accountType="+accountType,
                HttpMethod.GET,
                entity,
                String.class);
    }

    // Sara's Code
    @Then("I receive transaction response with status code {int}")
    public void iReceiveCustomerTransactionResponseWithStatusCode(int statusCode) throws JsonProcessingException {
        assertEquals(HttpStatus.valueOf(statusCode), customerTransactionResponseEntity.getStatusCode());
        if (statusCode == 200) {
            // if the response is successful, convert the response body (which is String) to proper format of
            // CustomerTransactionsDTO)
            customerTransactionsResponse = objectMapper.readValue(customerTransactionResponseEntity.getBody(), CustomerTransactionsDTO.class);
        }
    }

    // Sara's Code
    @And("I receive transaction array of length {int}")
    public void iReceiveTransactionArrayOfLength(int numberOfTransactions) {
        assertNotNull(customerTransactionsResponse);
        assertEquals(numberOfTransactions, customerTransactionsResponse.transactions().size());
    }

    // Sara's Code
    @And("I receive account of transaction of type {string}")
    public void iReceiveAccountOfTransactionOfType(String accountType) {
        String readAccount = customerTransactionsResponse.account().getType().toString();
        assertTrue(accountType.equals(readAccount));
    }

    private List<Transaction> allTransactions;
    private String filter;
    private String accountType;
    private List<Transaction> filteredTransactions;

    // Sara's Code
    @Then("I receive transaction array and save it")
    public void iReceiveTransactionArrayAndSaveIt() {
        allTransactions = customerTransactionsResponse.transactions();
    }

    // Sara's Code
    @Then("I request to read transactions with filter {string} for account of type {string}")
    public void iRequestToReadTransactionsWithFilterForAccountOfType(String userFilter, String accountType) throws JsonProcessingException {
        filter = userFilter;
        this.accountType = accountType;
        HttpEntity<String> entity = new HttpEntity<>(httpHeaders);
        customerTransactionResponseEntity = restTemplate.exchange("/transactions?accountType="+accountType+"&"+userFilter,
                HttpMethod.GET,
                entity,
                String.class);
        logger.info("customerTransactionResponseEntity");
        logger.info(customerTransactionResponseEntity.toString());
        assertEquals(HttpStatus.valueOf(200), customerTransactionResponseEntity.getStatusCode());
        customerTransactionsResponse = objectMapper.readValue(customerTransactionResponseEntity.getBody(), CustomerTransactionsDTO.class);
        filteredTransactions = customerTransactionsResponse.transactions();
    }

    // Sara's Code
    // with help from ChatGPT
    private Map<String, String> getMap(String filter) {
        Map<String, String> params = new HashMap<>();
        for (String param : filter.split("&")) {
            String[] keyValue = param.split("=");
            if (keyValue.length == 2) {
                params.put(keyValue[0], keyValue[1]);
            }
        }
        return params;
    }
    private TransactionType getTransactionTypeFromMap(Map<String, String> params) {
        // Retrieve transactionType parameter
        String transactionTypeStr = params.get("transactionType");

        // Convert transaction type string to TransactionType enum
        TransactionType transactionType = null;
        if (transactionTypeStr != null) {
            try {
                transactionType = TransactionType.valueOf(transactionTypeStr.toUpperCase());
            } catch (IllegalArgumentException e) {
                System.err.println("Invalid transaction type: " + transactionTypeStr);
                // Handle the error or return a default value if necessary
            }
        }
        return transactionType;
    }
    private LocalDate getStartDateFromMap(Map<String, String> params) {
        // Retrieve transactionType parameter
        String startDateStr = params.get("startDate");
        LocalDate startDate = null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        if (startDateStr != null) {
            try {
                startDate = LocalDate.parse(startDateStr, formatter);
            } catch (IllegalArgumentException e) {
                System.err.println("Invalid start date: " + startDateStr);
                // Handle the error or return a default value if necessary
            }
        }
        return startDate;
    }
    private LocalDate getEndDateFromMap(Map<String, String> params) {
        // Retrieve transactionType parameter
        String endDateStr = params.get("endDate");
        LocalDate endDate = null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        if (endDateStr != null) {
            try {
                endDate = LocalDate.parse(endDateStr, formatter);
            } catch (IllegalArgumentException e) {
                System.err.println("Invalid end date: " + endDateStr);
                // Handle the error or return a default value if necessary
            }
        }
        return endDate;
    }
    private Float getMinAmountFromMap(Map<String, String> params) {
        // Retrieve transactionType parameter
        String minAmountStr = params.get("minAmount");
        Float minAmount = null;
        if (minAmountStr != null) {
            try {
                minAmount =  Float.parseFloat(minAmountStr);
            } catch (IllegalArgumentException e) {
                System.err.println("Invalid minAmount: " + minAmountStr);
                // Handle the error or return a default value if necessary
            }
        }
        return minAmount;
    }
    private Float getMaxAmountFromMap(Map<String, String> params) {
        String maxAmountStr = params.get("maxAmount");
        Float maxAmount = null;
        if (maxAmountStr != null) {
            try {
                maxAmount =  Float.parseFloat(maxAmountStr);
            } catch (IllegalArgumentException e) {
                System.err.println("Invalid maxAmount: " + maxAmountStr);
                // Handle the error or return a default value if necessary
            }
        }
        return maxAmount;
    }
    private Float getExactAmountFromMap(Map<String, String> params) {
        String exactAmountStr = params.get("exactAmount");
        Float exactAmount = null;
        if (exactAmountStr != null) {
            try {
                exactAmount =  Float.parseFloat(exactAmountStr);
            } catch (IllegalArgumentException e) {
                System.err.println("Invalid exact amount: " + exactAmountStr);
                // Handle the error or return a default value if necessary
            }
        }
        return exactAmount;
    }
    private Integer getSkipFromMap(Map<String, String> params) {
        String skipStr = params.get("skip");
        Integer skip = null;
        if (skipStr != null) {
            try {
                skip =  Integer.parseInt(skipStr);
            } catch (IllegalArgumentException e) {
                System.err.println("Invalid skip: " + skipStr);
                // Handle the error or return a default value if necessary
            }
        }
        return skip;
    }
    private Integer getLimitFromMap(Map<String, String> params) {
        String limitStr = params.get("limit");
        Integer limit = null;
        if (limitStr != null) {
            try {
                limit =  Integer.parseInt(limitStr);
            } catch (IllegalArgumentException e) {
                System.err.println("Invalid limit: " + limitStr);
                // Handle the error or return a default value if necessary
            }
        }
        return limit;
    }

    // Sara's Code
    @Then("I check to see if filter is correctly applied")
    public void iCheckToSeeIfFilterIsCorrectlyApplied() {
        Map<String, String> params = getMap(filter);
        TransactionType transactionType = getTransactionTypeFromMap(params);
        LocalDate startDate = getStartDateFromMap(params);
        LocalDate endDate = getEndDateFromMap(params);
        Float minAmount = getMinAmountFromMap(params);
        Float maxAmount = getMaxAmountFromMap(params);
        Float exactAmount = getExactAmountFromMap(params);
        String iban = params.get("iban");
        Integer skip = getSkipFromMap(params);
        Integer limit = getLimitFromMap(params);
        List<Transaction> actualFiltered = allTransactions.stream()
                .filter(transaction -> transactionType == null || transaction.getTransactionType() == transactionType)
                .filter(transaction -> startDate == null || transaction.getDateTime().isAfter(startDate.atStartOfDay()))
                .filter(transaction -> endDate == null || transaction.getDateTime().isBefore(endDate.atStartOfDay()))
                .filter(transaction -> minAmount == null || transaction.getAmount() >= minAmount)
                .filter(transaction -> maxAmount == null || transaction.getAmount() <= maxAmount)
                .filter(transaction -> exactAmount == null || transaction.getAmount() == exactAmount)
                .filter(transaction -> iban == null ||
                        transaction.getSenderAccount().getIban().contains(iban) ||
                        transaction.getReceiverAccount().getIban().contains(iban))
                .skip(skip != null ? skip : 0)
                .limit(limit != null ? limit : Integer.MAX_VALUE).toList();
        logger.info("number of transactions: " + filteredTransactions.size()+ " with filter " + filter);
        assertEquals(filteredTransactions.size(), actualFiltered.size());
    }

    protected ResponseEntity<String> filteredTransactionsResponseEntity;
    @Given("I am logged in as Admin with email {string} and password {string}")
    public void iLogInAsAdminWithEmailAndPassword(String email, String password){
        loginDTO = new LoginRequestDTO(email,password);
        String url = "http://localhost:" + port + "/login";
        loginResponseEntity = restTemplate.postForEntity(url, loginDTO, LoginResponseDTO.class);
        // Store login response
        loginResponse = loginResponseEntity.getBody();
        // Set JWT token in headers for future requests
        httpHeaders.setBearerAuth(loginResponse.token());
    }
    @When("I filter transactions with condition {string} and userid {long} and skip {int} and limit {int}")
    public void iFilterTransactionsWithConditionID(String condition, long userId, int skip, int limit) {
        String url = "http://localhost:" + port +  "/transactions/history?condition=" + condition + "&userId=" + userId + "&limit=" + limit +"&skip=" + skip;
        HttpEntity<String> entity = new HttpEntity<>(httpHeaders);
        filteredTransactionsResponseEntity = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
    }
    @When("I filter transactions with condition {string} and skip {int} and limit {int}")
    public void iFilterTransactionsWithCondition(String condition,int skip, int limit) {
        String url = "http://localhost:" + port +  "/transactions/history?condition=" + condition + "&limit=" + limit +"&skip=" + skip;
        HttpEntity<String> entity = new HttpEntity<>(httpHeaders);
        filteredTransactionsResponseEntity = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

    }
    @Then("the response status for transaction should be {int}")
    public void theResponseStatusShouldBe(int expectedStatus) {
        HttpStatus expectedHttpStatus = HttpStatus.valueOf(expectedStatus);
        assertEquals(expectedHttpStatus, filteredTransactionsResponseEntity.getStatusCode(), "Unexpected status code");
    }


    @Then("I should receive the filtered transactions based on condition")
     public void IReceiveFilteredTransactionsBasedOnCondition(){
        assertNotNull(filteredTransactionsResponseEntity.getBody(), "Response body should not be null");

    }

}
