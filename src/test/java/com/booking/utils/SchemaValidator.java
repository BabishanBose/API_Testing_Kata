package com.booking.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.MatcherAssert.assertThat;

public class SchemaValidator {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private SchemaValidator() {
    }
    public static void validate(Object request, String schemaPath) {
        try {
            String requestJson = objectMapper.writeValueAsString(request);
            assertThat(requestJson, matchesJsonSchemaInClasspath(schemaPath));
        } catch (Exception e) {
            throw new RuntimeException("Failed to validate request schema", e);
        }
    }
}