package com.booking.api;

import com.booking.Mapper;
import com.booking.api.vo.Booking;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;

import static io.restassured.RestAssured.given;

public class BookingApi {
    private final String token;

    private static String URL = ApiConstants.BASE_URL + "/booking";

    public BookingApi(String token) {
        this.token = token;
    }

    public static Booking get(final long id) {
        final var rsp = RestAssured.get(URL + "/" + id);
        if (rsp.statusCode() == HttpStatus.SC_NOT_FOUND) {
            return null;
        }
        return rsp.as(Booking.class, Mapper.getJsonMapper());
    }

    public static long create(Booking rq) {
        return given()
                .body(rq, Mapper.getJsonMapper())
                .contentType(ContentType.JSON)
                .when()
                .post(URL)
                .jsonPath().getLong("bookingid");
    }
}
