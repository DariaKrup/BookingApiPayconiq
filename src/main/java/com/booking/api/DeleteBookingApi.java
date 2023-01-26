package com.booking.api;

import io.restassured.http.ContentType;
import io.restassured.http.Cookie;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class DeleteBookingApi {
    private final String token;
    private static String URL = ApiConstants.BASE_URL + "/booking";
    public DeleteBookingApi(String token) {
        this.token = token;
    }

    public Response delete(final long id) {
        return given()
                .contentType(ContentType.JSON)
                .cookie(new Cookie.Builder("token", token).build())
                .when()
                .delete(URL + "/" + id);
    }
}
