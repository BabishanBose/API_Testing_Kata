package com.booking.service;
import com.booking.config.ApiConfig;
import com.booking.dto.request.BookingRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;

public class BookingService {
    public Response createBooking(BookingRequest bookingRequest) {
        String username = System.getProperty("username");
        String password = System.getProperty("password");
        return given()
                .baseUri(ApiConfig.BASE_URL)
                .auth()
                .basic(username, password)
                .contentType("application/json")
                .body(bookingRequest)
                .when()
                .post(ApiConfig.BOOKING_ENDPOINT);
    }

    public Response createBookingWithoutField(BookingRequest bookingRequest,String fieldName) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode requestBody = objectMapper.valueToTree(bookingRequest);
        requestBody.remove(fieldName);
        return given()
                .baseUri(ApiConfig.BASE_URL)
                .contentType("application/json")
                .body(requestBody.toString())
                .when()
                .post(ApiConfig.BOOKING_ENDPOINT);
    }
}