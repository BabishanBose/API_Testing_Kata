package com.booking.stepdefinitions;

import com.booking.constants.ApiResponsePaths;
import com.booking.dto.request.BookingDates;
import com.booking.dto.request.BookingRequest;
import com.booking.service.BookingService;
import com.booking.utils.SchemaValidator;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.restassured.response.Response;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class BookingSteps {
    private final BookingService bookingService = new BookingService();
    private BookingRequest bookingRequest;
    private Response response;

    @Given("I have a booking request with")
    public void createBookingRequest(DataTable dataTable) {
        Map<String, String> bookingData = dataTable.asMap(String.class, String.class);

        BookingDates bookingDates = new BookingDates(
                bookingData.get("checkIn"),
                bookingData.get("checkOut")
        );
        bookingRequest = new BookingRequest(
                Integer.parseInt(bookingData.get("roomId")),
                bookingData.get("firstName"),
                bookingData.get("lastName"),
                Boolean.parseBoolean(bookingData.get("depositPaid")),
                bookingDates,
                bookingData.get("email"),
                bookingData.get("phone")
        );
        SchemaValidator.validate(bookingRequest,"schemas/booking-request-schema.json");
    }

    @When("I create the booking")
    public void createBooking() {
        response = bookingService.createBooking(bookingRequest);
    }

    @Then("the booking request should be {string} with status code {int}")
    public void verifyBookingResponse(String expectedResult, int expectedStatusCode) {
        assertEquals(expectedStatusCode, response.getStatusCode());
        if ("rejected".equalsIgnoreCase(expectedResult)) {
            assertNotNull(response.jsonPath().get("errors"),
                    "Rejected booking response should contain an 'errors' field");
        }
    }

    @Then("the response should contain error {string}")
    public void verifyResponseError(String expectedError) {
        List<String> errors = response.jsonPath().getList("errors");
        assertNotNull(errors,
                "Booking API error response should contain an 'errors' field");
        assertTrue(errors.contains(expectedError),
                "Unexpected API error message. "
                        + "Expected: '" + expectedError
                        + "', Actual: '" + errors + "'");
    }

    @Then("the booking details should match the request")
    public void verifyBookingDetails() {
        assertNotNull(response.jsonPath().get(ApiResponsePaths.BOOKING_ID),
                "Booking ID is missing from the API response");
        assertNotNull(response.jsonPath().get(ApiResponsePaths.BOOKING),
                "Booking object is missing from the API response");
        assertEquals(bookingRequest.getRoomid(),
                response.jsonPath().getInt(ApiResponsePaths.ROOM_ID),
                "Room ID in response does not match the booking request");
        assertEquals(bookingRequest.getFirstname(),
                response.jsonPath().getString(ApiResponsePaths.FIRST_NAME),
                "First name in response does not match the booking request");
        assertEquals(bookingRequest.getLastname(),
                response.jsonPath().getString(ApiResponsePaths.LAST_NAME),
                "Last name in response does not match the booking request");
        assertEquals(bookingRequest.isDepositpaid(),
                response.jsonPath().getBoolean(ApiResponsePaths.DEPOSIT_PAID),
                "Deposit paid status in response does not match the booking request");
        assertEquals(bookingRequest.getEmail(),
                response.jsonPath().getString(ApiResponsePaths.EMAIL),
                "Email in response does not match the booking request");
        assertEquals(bookingRequest.getPhone(),
                response.jsonPath().getString(ApiResponsePaths.PHONE),
                "Phone number in response does not match the booking request");
        assertEquals(bookingRequest.getBookingdates().getCheckin(),
                response.jsonPath().getString(ApiResponsePaths.CHECK_IN),
                "Check-in date in response does not match the booking request");
        assertEquals(bookingRequest.getBookingdates().getCheckout(),
                response.jsonPath().getString(ApiResponsePaths.CHECK_OUT),
                "Check-out date in response does not match the booking request");
    }

    @When("I create the booking without the {string} field")
    public void createBookingWithoutField(String fieldName) {
        response = bookingService.createBookingWithoutField(bookingRequest,fieldName);
    }

    @Then("the response should match the booking schema")
    public void verifyResponseSchema() {
        response.then().assertThat()
                .body(matchesJsonSchemaInClasspath("schemas/booking-response-schema.json"));
    }
}