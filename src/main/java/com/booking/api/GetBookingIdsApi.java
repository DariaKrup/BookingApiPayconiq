package com.booking.api;

import com.booking.Mapper;
import io.restassured.http.ContentType;
import io.restassured.http.Cookie;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;

import java.util.List;

import static io.restassured.RestAssured.given;

public class GetBookingIdsApi {
    private final String token;

    private static String URL = ApiConstants.BASE_URL + "/booking";

    public GetBookingIdsApi(String token) {
        this.token = token;
    }

    public Response getIds() {
        return given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .cookie(new Cookie.Builder("token", token).build())
                .when()
                .get(URL);
    }
}
