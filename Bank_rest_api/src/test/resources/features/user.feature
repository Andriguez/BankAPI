Feature: User

  Scenario: Successful getAllUsers
    Given I login as an Admin with email "admin@email.com" and password "password"
    When I request to read all users
    Then I receive users response with status code 200
    And I receive a list of users
    And list has user of type "GUEST"
    And list has user of type "CUSTOMER"

  Scenario: Successful getAllCustomers
    Given I login as an Admin with email "admin@email.com" and password "password"
    When I request to read users of type "CUSTOMER"
    Then I receive users response with status code 200
    And I receive a list of users
    And list has user of type "CUSTOMER"
