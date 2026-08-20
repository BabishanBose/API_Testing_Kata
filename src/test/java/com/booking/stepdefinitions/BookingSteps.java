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
import java.util.Map;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class BookingSteps {
    private final BookingService bookingService = new BookingService();
    private BookingRequest bookingRequest;
    private Response response;

    @Given("I have a valid booking request with:")
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

    @Then("the booking should be created successfully")
    public void verifyBookingCreated() {
        assertEquals(200, response.getStatusCode());
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
}