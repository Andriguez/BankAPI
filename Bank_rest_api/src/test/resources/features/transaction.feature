Feature: Transaction

Scenario: Customer with no specific account reading transactions
  Given To see my transactions, I login as as Customer with email "customernotransaction@email.com" and password "password"
  When I request to read transactions without account type
  Then I receive transaction response with status code 400
  And I receive error message "accountType should be present"

Scenario: Customer with no Current transactions
  Given To see my transactions, I login as as Customer with email "customernotransaction@email.com" and password "password"
  When I request to read transactions of "CURRENT" account
  Then I receive transaction response with status code 200
  And I receive transaction array of length 0
  And I receive account of transaction of type "CURRENT"

Scenario: Customer with no Saving transactions
  Given To see my transactions, I login as as Customer with email "customernotransaction@email.com" and password "password"
  When I request to read transactions of "SAVINGS" account
  Then I receive transaction response with status code 200
  And I receive transaction array of length 0
  And I receive account of transaction of type "SAVINGS"


Scenario: Filter transactions based on transactionType=Deposit
  Given To see my transactions, I login as as Customer with email "customer2@email.com" and password "password"
  When I request to read transactions of "CURRENT" account
  Then I receive transaction response with status code 200
  Then I receive transaction array and save it
  Then I request to read transactions with filter "transactionType=DEPOSIT" for account of type "CURRENT"
  Then I check to see if filter is correctly applied

Scenario: Filter transactions based on transactionType=TRANSFER
  Given To see my transactions, I login as as Customer with email "customer2@email.com" and password "password"
  When I request to read transactions of "CURRENT" account
  Then I receive transaction response with status code 200
  Then I receive transaction array and save it
  Then I request to read transactions with filter "transactionType=TRANSFER" for account of type "CURRENT"
  Then I check to see if filter is correctly applied

Scenario: Filter transactions based on transactionType=WITHDRAWAL
  Given To see my transactions, I login as as Customer with email "customer2@email.com" and password "password"
  When I request to read transactions of "CURRENT" account
  Then I receive transaction response with status code 200
  Then I receive transaction array and save it
  Then I request to read transactions with filter "transactionType=WITHDRAWAL" for account of type "CURRENT"
  Then I check to see if filter is correctly applied

Scenario: Filter transactions based on startDate=2024-06-10
  Given To see my transactions, I login as as Customer with email "customer2@email.com" and password "password"
  When I request to read transactions of "CURRENT" account
  Then I receive transaction response with status code 200
  Then I receive transaction array and save it
  Then I request to read transactions with filter "startDate=2024-06-10" for account of type "CURRENT"
  Then I check to see if filter is correctly applied

Scenario: Filter transactions based on startDate=2024-06-10 and endDate=2024-08-20
  Given To see my transactions, I login as as Customer with email "customer2@email.com" and password "password"
  When I request to read transactions of "CURRENT" account
  Then I receive transaction response with status code 200
  Then I receive transaction array and save it
  Then I request to read transactions with filter "startDate=2024-06-10&endDate=2024-08-20" for account of type "CURRENT"
  Then I check to see if filter is correctly applied

Scenario: Filter transactions based on minAmount=10.5
  Given To see my transactions, I login as as Customer with email "customer2@email.com" and password "password"
  When I request to read transactions of "CURRENT" account
  Then I receive transaction response with status code 200
  Then I receive transaction array and save it
  Then I request to read transactions with filter "minAmount=10.5" for account of type "CURRENT"
  Then I check to see if filter is correctly applied

Scenario: Filter transactions based on minAmount=10.5 and maxAmount=111.5
  Given To see my transactions, I login as as Customer with email "customer2@email.com" and password "password"
  When I request to read transactions of "CURRENT" account
  Then I receive transaction response with status code 200
  Then I receive transaction array and save it
  Then I request to read transactions with filter "minAmount=10.5&maxAmount=111.5" for account of type "CURRENT"
  Then I check to see if filter is correctly applied

Scenario: Filter transactions based on maxAmount=111.5 and limit=5 and skip=1
  Given To see my transactions, I login as as Customer with email "customer2@email.com" and password "password"
  When I request to read transactions of "CURRENT" account
  Then I receive transaction response with status code 200
  Then I receive transaction array and save it
  Then I request to read transactions with filter "maxAmount=111.5&limit=5&skip=1" for account of type "CURRENT"
  Then I check to see if filter is correctly applied