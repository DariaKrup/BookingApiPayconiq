package com.booking.api;

import io.restassured.http.ContentType;
import static io.restassured.RestAssured.given;

public class AuthApi {

    private static String URL = ApiConstants.BASE_URL + "/auth";
    public static String createToken() {
        return given()
                .contentType(ContentType.JSON)
                .body("{\"username\": \"admin\",\"password\":\"password123\"}")
                .when()
                .post(URL)
                .path("token");
    }
}
