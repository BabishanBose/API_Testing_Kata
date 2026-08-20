# API Testing Kata

API automation framework for testing the booking API using Java, Rest-Assured and Cucumber BDD.

The framework focuses on the `POST /booking` endpoint and covers positive, negative and validation scenarios using data-driven Cucumber tests.

## Tech Stack

- Java 17
- Maven
- Rest-Assured
- Cucumber BDD
- JUnit 5
- Jackson
- Lombok
		
## Package Responsibilities
config – API configuration and endpoints
constants – Reusable response JSON paths
dto – Request data models
service – API interaction using Rest-Assured
stepdefinitions – Cucumber steps and response assertions
features – Cucumber scenarios and test data
spec – API specification used as the reference for test scenarios

## Setup
### Prerequisites
Make sure the following are installed:
Java 17 or higher
Maven 3.9 or higher
Git
IntelliJ IDEA (recommended)

Verify Java:
java -version

Verify Maven:
mvn -version

## Clone the Repository
git clone <repository-url>
Navigate to the project:
cd API_Testing_Kata

## Install Dependencies
Maven will download all project dependencies defined in pom.xml.
mvn clean install

## Running Tests
### Run All Tests
mvn clean test
### Run Smoke Tests
mvn clean test "-Dcucumber.filter.tags=@smoke"
### Run Regression Tests
mvn clean test "-Dcucumber.filter.tags=@regression"
### Run Negative Tests
mvn clean test "-Dcucumber.filter.tags=@negative"
### Run Required Field Validation Tests
mvn clean test "-Dcucumber.filter.tags=@required-fields"
### Run Data Validation Tests
mvn clean test "-Dcucumber.filter.tags=@data-validation"
### Run Date Validation Tests
mvn clean test "-Dcucumber.filter.tags=@date-validation"

## Test Reports
After test execution, reports are generated under:
target/

Cucumber reports can be found under:
target/cucumber-reports/

Maven test reports can be found under:
target/surefire-reports/
API Specification

The API specification used for designing the tests is available at:
src/test/resources/spec/booking.yaml

The tests use the booking API hosted at:
https://automationintesting.online/

## Framework Structure

```text
src/test
├── java/com/booking
│   ├── config
│   │   └── ApiConfig.java
│   ├── constants
│   │   └── ApiResponsePaths.java
│   ├── dto
│   │   └── request
│   │       ├── BookingDates.java
│   │       └── BookingRequest.java
│   ├── service
│   │   └── BookingService.java
│   ├── stepdefinitions
│   │   ├── BookingSteps.java
│   │   └── GetMessages.java
│   └── TestRunner.java
│
└── resources
    ├── features
    │   ├── booking.feature
    │   └── messages.feature
    └── spec
        └── booking.yaml