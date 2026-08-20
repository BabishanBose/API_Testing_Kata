package com.booking.stepdefinitions;

import com.booking.constants.ApiResponsePaths;
import com.booking.dto.request.BookingDates;
import com.booking.dto.request.BookingRequest;
import com.booking.service.BookingService;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.restassured.response.Response;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
    }

    @When("I create the booking")
    public void createBooking() {
        response = bookingService.createBooking(bookingRequest);
    }

    @Then("the booking request should be {string} with status code {int}")
    public void verifyBookingResponse(String expectedResult, int expectedStatusCode) {
        assertEquals(expectedStatusCode, response.getStatusCode());
        if (expectedStatusCode == 400) {
            assertNotNull(response.jsonPath().get("errors"));
        }
    }

    @Then("the response should contain error {string}")
    public void verifyResponseError(String expectedError) {
        List<String> errors = response.jsonPath().getList("errors");
        assertNotNull(errors);
        assertTrue(errors.contains(expectedError));
    }

    @Then("the booking details should match the request")
    public void verifyBookingDetails() {
        assertNotNull(response.jsonPath().get(ApiResponsePaths.BOOKING_ID));
        assertNotNull(response.jsonPath().get(ApiResponsePaths.BOOKING));
        assertEquals(bookingRequest.getRoomid(),
                response.jsonPath().getInt(ApiResponsePaths.ROOM_ID));
        assertEquals(bookingRequest.getFirstname(),
                response.jsonPath().getString(ApiResponsePaths.FIRST_NAME));
        assertEquals(bookingRequest.getLastname(),
                response.jsonPath().getString(ApiResponsePaths.LAST_NAME) );
        assertEquals(bookingRequest.isDepositpaid(),
                response.jsonPath().getBoolean(ApiResponsePaths.DEPOSIT_PAID));
        assertEquals(bookingRequest.getEmail(),
                response.jsonPath().getString(ApiResponsePaths.EMAIL));
        assertEquals(bookingRequest.getPhone(),
                response.jsonPath().getString(ApiResponsePaths.PHONE));
        assertEquals(bookingRequest.getBookingdates().getCheckin(),
                response.jsonPath().getString(ApiResponsePaths.CHECK_IN));
        assertEquals(bookingRequest.getBookingdates().getCheckout(),
                response.jsonPath().getString(ApiResponsePaths.CHECK_OUT));
    }

    @When("I create the booking without the {string} field")
    public void createBookingWithoutField(String fieldName) {
        response = bookingService.createBookingWithoutField(bookingRequest,fieldName);
    }
}