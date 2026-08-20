Feature: Create a hotel booking

  Scenario Outline: Successfully create a booking with valid details
    Given I have a valid booking request with:
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
      | roomId | firstName    | lastName  | depositPaid | checkIn    | checkOut   | email                  | phone         |
      | 1      | Babishan     | Bose      | true        | 2026-08-20 | 2026-08-21 | Babishan.Bose@test.com | +919234567890 |
      | 2      | Durga        | Prasad    | false       | 2026-08-20 | 2026-08-21 | Durga.Prasad@test.com  | +919234567891 |