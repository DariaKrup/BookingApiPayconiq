package com.booking.api;

import com.booking.Mapper;
import com.booking.api.vo.Booking;
import com.booking.api.vo.BookingFilter;
import com.booking.api.vo.PartialBooking;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Cookie;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;

import java.util.HashMap;

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

    public Response partialUpdate(final long id, final PartialBooking rq) {
        return partialUpdate(id, rq, new Cookie.Builder("token", token).build());
    }

    public Response partialUpdate(final long id, final PartialBooking rq, Cookie cookie) {
        if (cookie != null) {
            return given()
                    .body(rq, Mapper.getJsonMapper())
                    .contentType(ContentType.JSON)
                    .header("Accept", "application/json")
                    .cookie(cookie)
                    .when()
                    .patch(URL + "/" + id);
        } else {
            return given()
                    .body(rq, Mapper.getJsonMapper())
                    .contentType(ContentType.JSON)
                    .header("Accept", "application/json")
                    .when()
                    .patch(URL + "/" + id);
        }
    }

    public Response partialUpdateMap(final String id, final HashMap rq) {
        return given()
                .body(rq)
                .contentType(ContentType.JSON)
                .header("Accept", "application/json")
                .cookie(new Cookie.Builder("token", token).build())
                .when()
                .patch(URL + "/" + id);
    }

    public Response delete(final String id) {
        return given()
                .contentType(ContentType.JSON)
                .cookie(new Cookie.Builder("token", token).build())
                .when()
                .delete(URL + "/" + id);
    }

    public Response getIds() {
        return getIds(new BookingFilter());
    }

    public Response getIds(BookingFilter filter) {
        if (filter != null) {
            return given()
                    .contentType(ContentType.JSON)
                    .accept(ContentType.JSON)
                    .body(filter)
                    .when()
                    .get(URL);
        } else {
            return given()
                    .contentType(ContentType.JSON)
                    .accept(ContentType.JSON)
                    .when()
                    .get(URL);
        }
    }

    public Response getIds(HashMap filter) {
        return given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(filter)
                .when()
                .get(URL);
    }
}
