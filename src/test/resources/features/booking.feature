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
    Then the booking should be created successfully
    Examples:
      | roomId | firstName | lastName | depositPaid | checkIn    | checkOut   | email                  | phone         |
      | 1      | Babishan  | Bose     | true        | 2026-09-20 | 2026-09-21 | Babishan.Bose@test.com | +919234567890 |
      | 2      | Durga     | Prasad   | false       | 2026-09-20 | 2026-09-21 | Durga.Prasad@test.com  | +919234567891 |

  @regression @negative
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
    Then the booking request should be rejected with status code <statusCode>
    Examples:
      | roomId | firstName | lastName                           | depositPaid | checkIn    | checkOut   | email                     | phone         | statusCode |
      | 1      | Ba        | Bose                               | true        | 2026-09-20 | 2026-09-21 | Babishan.Bose@example.com | +919234567890 | 400        |
      | 2      | Babishan  | ThisLastnameIsTooLongValueToCreate | true        | 2026-09-20 | 2026-09-21 | Babishan.Bose@example.com | +919234567890 | 400        |
      | 3      | Babishan  | Bose                               | true        | 2026-09-20 | 2026-09-21 | invalid-email             | +919234567890 | 400        |

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
    Then the booking request should be rejected with status code 400

  @regression @negative @required-fields-validation
  Scenario Outline: Reject booking when a required field is missing
    Given I have a booking request with
      | roomId      | 1                    |
      | firstName   | Babishan             |
      | lastName    | Bose                 |
      | depositPaid | true                 |
      | checkIn     | 2030-08-20           |
      | checkOut    | 2030-08-21           |
      | email       | Babishan.Bose@test.com |
      | phone       | +919234567890        |
    When I create the booking without the "<field>" field
    Then the booking request should be rejected with status code <statusCode>
    Examples:
      | field       | statusCode |
      | roomid      | 400        |
      | firstname   | 400        |
      | lastname    | 400        |
      | depositpaid | 400        |
      | bookingdates| 400        |
      | email       | 400        |
      | phone       | 400        |