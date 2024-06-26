Feature: Register

  Scenario: Successful registration
    Given I have registration request with details name "John", lastname "Doe", email "john.doe1@email.com", phone 1234567890, bsn 987654321, password "password"
    When I send registration request
    Then I receive registration response with status code 200
    Then I receive valid registration response

  Scenario: registration failure email already exists
    Given I have registration request with details name "John", lastname "Doe", email "admin@email.com", phone 1234567890, bsn 987624321, password "password"
    When I send registration request
    Then I receive registration response with status code 400
    And The registration Response has message "Email is already registered with another user"

    Scenario: registration failure missing fields
      Given I have a failing registration request with details name "John", lastname "Doe", email "newuser@email.com", phone 1234567890, bsn 983654321
      When I send registration request
      Then I receive registration response with status code 400
      And The registration Response has message "All fields are required"
