Feature: Register

  Scenario: Successful registration
    Given I have registration request with details "John" "Doe" "john.doe@email.com" 1234567890 987654321 "password"
    When I send registration request
    Then I receive registration response with status code 200
    Then I receive valid registration response