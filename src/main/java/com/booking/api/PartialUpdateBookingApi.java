package com.booking.api;

import com.booking.Mapper;
import io.restassured.http.ContentType;
import io.restassured.http.Cookie;
import io.restassured.response.Response;

import java.util.HashMap;

import static io.restassured.RestAssured.given;

public class PartialUpdateBookingApi {
    private final String token;

    private static String URL = ApiConstants.BASE_URL + "/booking";

    public PartialUpdateBookingApi(String token) {
        this.token = token;
    }

    public Response partialUpdate(HashMap rq, final long id) {
        return given()
                .body(rq, Mapper.getJsonMapper())
                .contentType(ContentType.JSON)
                .header("Accept", "application/json")
                .cookie(new Cookie.Builder("token", token).build())
                .when()
                .patch(URL + "/" + id);
    }


}
