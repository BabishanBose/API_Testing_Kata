@booking
Feature: Create a hotel booking

  @regression @smoke @positive
  Scenario Outline: Successfully create a booking with valid details
    Given I have a booking request with
      | roomId      | <roomId>      |
      | firstName   | <firstName>   |
      | lastName    | <lastName>    |
      | depositPaid | <depositPaid> |
      | checkIn     | <checkIn>     |
      | checkOut    | <checkOut>    |
      | email       | <email>       |
      | phone       | <phone>       |
    When I create the booking
    Then the booking request should be "created" with status code 200
    Examples:
      | roomId | firstName          | lastName           | depositPaid | checkIn    | checkOut   | email                  | phone                 |
      # Standard valid booking - depositPaid = true
      | 1      | Babishan           | Bose               | true        | 2026-09-20 | 2026-09-21 | Babishan.Bose@test.com | +919234567890         |
      # Standard valid booking - depositPaid = false
      | 2      | Babishan           | Bose               | false       | 2026-09-20 | 2026-09-21 | Babishan.Bose@test.com | +919234567891         |
      # First name minimum length (3) + phone minimum length (11)
      | 3      | Bab                | Bose               | true        | 2030-08-20 | 2030-08-21 | tom.doe@test.com       | 12345678901           |
      # First name maximum length (18)
      | 4      | Babishan1234567890 | Bose               | true        | 2030-08-20 | 2030-08-21 | test18@test.com        | 12345678901           |
      # Last name minimum length (3)
      | 5      | Babishan           | Doe                | true        | 2030-08-20 | 2030-08-21 | bab.doe@test.com       | 12345678901           |
      # Last name maximum length (18) + phone maximum length (21)
      | 6      | Babishan           | Smith1234567890123 | false       | 2030-08-20 | 2030-08-21 | john.smith@test.com    | 123456789012345678901 |


  @regression @negative @data-validation
  Scenario Outline: Reject booking with invalid field data
    Given I have a booking request with
      | roomId      | <roomId>      |
      | firstName   | <firstName>   |
      | lastName    | <lastName>    |
      | depositPaid | <depositPaid> |
      | checkIn     | <checkIn>     |
      | checkOut    | <checkOut>    |
      | email       | <email>       |
      | phone       | <phone>       |
    When I create the booking
    Then the booking request should be "rejected" with status code <statusCode>
    And the response should contain error "<expectedError>"
    Examples:
      # First name below minimum length
      | roomId | firstName           | lastName                           | depositPaid | checkIn    | checkOut   | email                     | phone                  | statusCode | expectedError                       |
      | 1      | Ba                  | Bose                               | true        | 2030-08-20 | 2030-08-21 | Babishan.Bose@example.com | +919234567890          | 400        | size must be between 3 and 18       |
      # First name above maximum length
      | 2      | Babishan12345678901 | Bose                               | true        | 2030-08-20 | 2030-08-21 | Babishan.Bose@example.com | +919234567890          | 400        | size must be between 3 and 18       |
      # Last name below minimum length
      | 3      | Babishan            | Bo                                 | true        | 2030-08-20 | 2030-08-21 | Babishan.Bose@example.com | +919234567890          | 400        | size must be between 3 and 18       |
      # Last name above maximum length
      | 4      | Babishan            | ThisLastnameIsTooLongValueToCreate | true        | 2030-08-20 | 2030-08-21 | Babishan.Bose@example.com | +919234567890          | 400        | size must be between 3 and 18       |
      # Invalid email format
      | 5      | Babishan            | Bose                               | true        | 2030-08-20 | 2030-08-21 | invalid-email             | +919234567890          | 400        | must be a well-formed email address |
      # Phone below minimum length
      | 6      | Babishan            | Bose                               | true        | 2030-08-20 | 2030-08-21 | Babishan.Bose@example.com | 1234567890             | 400        | size must be between 11 and 21      |
      # Phone above maximum length
      | 7      | Babishan            | Bose                               | true        | 2030-08-20 | 2030-08-21 | Babishan.Bose@example.com | 1234567890123456789012 | 400        | size must be between 11 and 21      |

  @regression @negative @date-validation
  Scenario: Reject booking when checkout date is before check-in date
    Given I have a booking request with
      | roomId      | 1                      |
      | firstName   | Babishan               |
      | lastName    | Bose                   |
      | depositPaid | true                   |
      | checkIn     | 2026-09-25             |
      | checkOut    | 2026-09-20             |
      | email       | Babishan.Bose@test.com |
      | phone       | +919234567890          |
    When I create the booking
    Then the booking request should be "rejected" with status code 400

  @regression @negative @required-fields-validation
  Scenario Outline: Reject booking when a required field is missing
    Given I have a booking request with
      | roomId      | 1                      |
      | firstName   | Babishan               |
      | lastName    | Bose                   |
      | depositPaid | true                   |
      | checkIn     | 2030-08-20             |
      | checkOut    | 2030-08-21             |
      | email       | Babishan.Bose@test.com |
      | phone       | +919234567890          |
    When I create the booking without the "<field>" field
    Then the booking request should be "rejected" with status code <statusCode>
    Examples:
      | field        | statusCode |
      | roomid       | 400        |
      | firstname    | 400        |
      | lastname     | 400        |
      | depositpaid  | 400        |
      | bookingdates | 400        |
      | email        | 400        |
      | phone        | 400        |