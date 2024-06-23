Feature: Account

Scenario: Guest has no accounts
  Given I login as as Guest with email "newguest@email.com" and password "password"
  When I request to read accounts as guest
  Then I receive error response with status code 400
  And I receive an error message "this user has no accounts"

Scenario: Customer has saving and current accounts
  Given To see my accounts, I login as as Customer with email "customer2@email.com" and password "password"
  When I request to read accounts as customer
  Then I receive accounts response with status code 200
  And I receive accounts array of length 2
  And I receive account of type "SAVINGS"
  And I receive account of type "CURRENT"