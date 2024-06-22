Feature: Login
  
Scenario: Successful login
  Given I have login request with email "customer2@email.com" and password "password"
  When I send login request
  Then I receive login response with status code 200
  Then I receive valid login response
