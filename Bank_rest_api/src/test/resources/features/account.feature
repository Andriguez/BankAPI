Feature: Account

  Scenario: Guest has no accounts
    Given I login as as Guest with email "newguest@email.com" and password "password"
    When I request to read accounts
    Then I receive accounts response with status code 200
    And I receive accounts array of length 0