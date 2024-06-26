Feature: Transaction

# Sara's Code
Scenario: Customer with no specific account reading transactions
  Given To see my transactions, I login as as Customer with email "customernotransaction@email.com" and password "password"
  When I request to read transactions without account type
  Then I receive transaction response with status code 400
  And I receive error message "accountType should be present"

# Sara's Code
Scenario: Customer with no Current transactions
  Given To see my transactions, I login as as Customer with email "customernotransaction@email.com" and password "password"
  When I request to read transactions of "CURRENT" account
  Then I receive transaction response with status code 200
  And I receive transaction array of length 0
  And I receive account of transaction of type "CURRENT"

# Sara's Code
Scenario: Customer with no Saving transactions
  Given To see my transactions, I login as as Customer with email "customernotransaction@email.com" and password "password"
  When I request to read transactions of "SAVINGS" account
  Then I receive transaction response with status code 200
  And I receive transaction array of length 0
  And I receive account of transaction of type "SAVINGS"

# Sara's Code
Scenario: Filter transactions based on transactionType=Deposit
  Given To see my transactions, I login as as Customer with email "customer2@email.com" and password "password"
  When I request to read transactions of "CURRENT" account
  Then I receive transaction response with status code 200
  Then I receive transaction array and save it
  Then I request to read transactions with filter "transactionType=DEPOSIT" for account of type "CURRENT"
  Then I check to see if filter is correctly applied

# Sara's Code
Scenario: Filter transactions based on transactionType=TRANSFER
  Given To see my transactions, I login as as Customer with email "customer2@email.com" and password "password"
  When I request to read transactions of "CURRENT" account
  Then I receive transaction response with status code 200
  Then I receive transaction array and save it
  Then I request to read transactions with filter "transactionType=TRANSFER" for account of type "CURRENT"
  Then I check to see if filter is correctly applied

# Sara's Code
Scenario: Filter transactions based on transactionType=WITHDRAWAL
  Given To see my transactions, I login as as Customer with email "customer2@email.com" and password "password"
  When I request to read transactions of "CURRENT" account
  Then I receive transaction response with status code 200
  Then I receive transaction array and save it
  Then I request to read transactions with filter "transactionType=WITHDRAWAL" for account of type "CURRENT"
  Then I check to see if filter is correctly applied

# Sara's Code
Scenario: Filter transactions based on startDate=2024-06-10
  Given To see my transactions, I login as as Customer with email "customer2@email.com" and password "password"
  When I request to read transactions of "CURRENT" account
  Then I receive transaction response with status code 200
  Then I receive transaction array and save it
  Then I request to read transactions with filter "startDate=2024-06-10" for account of type "CURRENT"
  Then I check to see if filter is correctly applied

# Sara's Code
Scenario: Filter transactions based on startDate=2024-06-10 and endDate=2024-08-20
  Given To see my transactions, I login as as Customer with email "customer2@email.com" and password "password"
  When I request to read transactions of "CURRENT" account
  Then I receive transaction response with status code 200
  Then I receive transaction array and save it
  Then I request to read transactions with filter "startDate=2024-06-10&endDate=2024-08-20" for account of type "CURRENT"
  Then I check to see if filter is correctly applied

# Sara's Code
Scenario: Filter transactions based on minAmount=10.5
  Given To see my transactions, I login as as Customer with email "customer2@email.com" and password "password"
  When I request to read transactions of "CURRENT" account
  Then I receive transaction response with status code 200
  Then I receive transaction array and save it
  Then I request to read transactions with filter "minAmount=10.5" for account of type "CURRENT"
  Then I check to see if filter is correctly applied

# Sara's Code
Scenario: Filter transactions based on minAmount=10.5 and maxAmount=111.5
  Given To see my transactions, I login as as Customer with email "customer2@email.com" and password "password"
  When I request to read transactions of "CURRENT" account
  Then I receive transaction response with status code 200
  Then I receive transaction array and save it
  Then I request to read transactions with filter "minAmount=10.5&maxAmount=111.5" for account of type "CURRENT"
  Then I check to see if filter is correctly applied

# Aleks's Code
  Scenario: Filter transactions by condition ID
    Given I am logged in as Admin with email "admin@email.com" and password "password"
    When I filter transactions with condition "ID" and userid 152 and skip 0 and limit 10
    Then the response status for transaction should be 200
    Then I should receive the filtered transactions based on condition

  Scenario: Filter transactions by condition ALL
    Given I am logged in as Admin with email "admin@email.com" and password "password"
    When I filter transactions with condition "ALL" and skip 0 and limit 10
    Then the response status for transaction should be 200
    Then I should receive the filtered transactions based on condition

  Scenario: Filter transactions by condition ATM
    Given I am logged in as Admin with email "admin@email.com" and password "password"
    When I filter transactions with condition "ATM" and skip 0 and limit 10
    Then the response status for transaction should be 200
    Then I should receive the filtered transactions based on condition

  Scenario: Filter transactions by condition ADMIN
    Given I am logged in as Admin with email "admin@email.com" and password "password"
    When I filter transactions with condition "ADMIN" and skip 0 and limit 10
    Then the response status for transaction should be 200
    Then I should receive the filtered transactions based on condition

  Scenario: Filter transactions by condition CUSTOMER
    Given I am logged in as Admin with email "admin@email.com" and password "password"
    When I filter transactions with condition "CUSTOMER" and skip 0 and limit 10
    Then the response status for transaction should be 200
    Then I should receive the filtered transactions based on condition

    #Andy's
  Scenario: Create Transaction as Customer
    Given I am logged in with email "customer2@email.com" and "password"
    When I have a transaction of type "TRANSFER" with amount 5 from sender "NL12INHO0123456789" to receiver "NL76INHO0087598006"
    Then I send the transaction request
    And I receive a transaction response with status code 200
    Then I set the transaction response
    And The transaction response has sender "NL12INHO0123456789", receiver "NL76INHO0087598006", amount 5

  Scenario: Create Transaction as ADMIN
    Given I am logged in with email "admin@email.com" and "password"
    When I have a transaction of type "TRANSFER" with amount 5 from sender "NL12INHO0123456789" to receiver "NL76INHO0087598006"
    Then I send the transaction request
    And I receive a transaction response with status code 200
    Then I set the transaction response
    And The transaction response has sender "NL12INHO0123456789", receiver "NL76INHO0087598006", amount 5

  Scenario: Create Transaction as Customer fails no access to account
    Given I am logged in with email "customer2@email.com" and "password"
    When I have a transaction of type "TRANSFER" with amount 5 from sender "NL76INHO0087598006" to receiver "NL42INHO0558188352"
    Then I send the transaction request
    And I receive a transaction response with status code 403
    And transaction response has error message "User is not allowed to make this transaction"

  Scenario: Create Transaction as Customer fails daily limit violated
    Given I am logged in with email "customer2@email.com" and "password"
    When I have a transaction of type "TRANSFER" with amount 500 from sender "NL12INHO0123456789" to receiver "NL76INHO0087598006"
    Then I send the transaction request
    And I receive a transaction response with status code 400
    And transaction response has error message "Transaction limits are being violated"

  Scenario: Create Transaction as Customer fails amount is negative
    Given I am logged in with email "customer2@email.com" and "password"
    When I have a transaction of type "TRANSFER" with amount -20 from sender "NL12INHO0123456789" to receiver "NL76INHO0087598006"
    Then I send the transaction request
    And I receive a transaction response with status code 400
    And transaction response has error message "Transaction amount cannot be negative"