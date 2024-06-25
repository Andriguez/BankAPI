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

# Andy's Code
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
