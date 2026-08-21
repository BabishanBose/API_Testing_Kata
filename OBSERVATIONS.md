# API Test Observations

This document captures observations and defects identified while testing the Booking API against the supplied OpenAPI specification.

The observations are based on the documented API contract, automated test execution, and API behavior observed during testing.

---

## 1. Successful Booking Returns HTTP 201 Instead of Documented HTTP 200

### Scenario

Create a booking with valid booking details.

### Expected

According to the supplied OpenAPI specification:

```text
HTTP 200
```
## 2. Last Name Validation
The API specification documents the following validation message:
```text
size must be between 3 and 18
```
However, testing through Postman showed that the API returns:
```text
size must be between 3 and 30
```
### for last-name validation.
Expected based on specification:
```text
3–18 characters
```
Observed from API:
```text
3–30 characters
```
There is a discrepancy between the documented API contract and the actual API validation behavior for lastname.

## 3. Invalid Checkout Date

The API specification documents invalid booking dates under HTTP 400.

When the checkout date is earlier than the check-in date, the API currently returns:
```text
409 Conflict
```
instead of:
```text
400 Bad Request
```
The response status differs from the documented API specification for invalid booking date combinations.

## 4. Missing Required Fields

The API specification defines the following fields as required:

- roomid
- firstname
- lastname
- depositpaid
- bookingdates
- email
- phone

The test suite contains individual scenarios to verify that each required field is handled when omitted.
```text
The API does not consistently return the documented validation response for all required fields.
```
## 4. Request Schema Validation Failures

JSON schema validation was added to validate the structure of the booking request against the supplied OpenAPI contract.

During test execution, response schema validation produced failures for some scenarios.
