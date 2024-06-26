Feature: User

  Scenario: Successful getAllUsers
    Given I login with email "admin@email.com" and password "password"
    When I request to read all users
    Then I receive users response with status code 200
    And I receive a list of users
    And list has user of type "GUEST"
    And list has user of type "CUSTOMER"

  Scenario: Successful getAllCustomers
    Given I login with email "admin@email.com" and password "password"
    When I request to read users of type "CUSTOMER"
    Then I receive users response with status code 200
    And I receive a list of usersDTO
    And list has user with name "customer"


  Scenario: Successful get User By Id
    Given I login with email "admin@email.com" and password "password"
    When I request to read user with Id 152
    Then I receive users response with status code 200
    And I receive a single UserDTO
    And User has Id 152

  Scenario: Successful customer gets their own information
    Given I login with email "customer2@email.com" and password "password"
    When I request to read user with Id 0
    Then I receive users response with status code 200
    And I receive a single UserDTO
    And User has email "customer2@email.com"

  Scenario: failure User By Id Not found
    Given I login with email "admin@email.com" and password "password"
    When I request to read user with Id 600
    Then I receive users response with status code 404
    And The user Response has message "User not found."


  Scenario: failure No permission to get User By Id
    Given I login with email "customer@email.com" and password "password"
    When I request to read user with Id 152
    Then I receive users response with status code 403
    And The user Response has message "user has no access to this data!"

  Scenario: failure get user by wrong type
    Given I login with email "admin@email.com" and password "password"
    When I request to read users of type "WRONGTYPE"
    Then I receive users response with status code 404


