package com.booking.service;
import com.booking.config.ApiConfig;
import com.booking.dto.request.BookingRequest;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;

public class BookingService {
    public Response createBooking(BookingRequest bookingRequest) {
        return given()
                .baseUri(ApiConfig.BASE_URL)
                .contentType("application/json")
                .body(bookingRequest)
                .when()
                .post(ApiConfig.BOOKING_ENDPOINT);
    }
}