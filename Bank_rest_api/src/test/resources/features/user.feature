Feature: User

  Scenario: Successful getAllUsers
    Given I get all users, I login as an Admin with email "admin@email.com" and password "password"
    When I request to read all users
    Then I receive a list of users
