Feature: Login
  
  Scenario: Successful login
    Given I have login request with email "customer2@email.com" and password "password"
    When I send login request
    Then I set login response and token
    Then I receive login response with status code 200
    Then I receive valid login response

  Scenario: login fail wrong email
    Given I have login request with email "wrongemail@email.com" and password "password"
    When I send login request
    Then I receive login response with status code 401
    And The login Response has message "No user found with this email"


  Scenario: login fail wrong password
    Given I have login request with email "customer2@email.com" and password "wrongpassword"
    When I send login request
    Then I receive login response with status code 401
    And The login Response has message "password is incorrect"
