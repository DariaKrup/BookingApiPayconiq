package com.booking.api;

import com.booking.Mapper;
import com.booking.api.vo.Booking;
import com.booking.api.vo.PartialBooking;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Cookie;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class BookingApi {
    private final String token;

    private static String URL = ApiConstants.BASE_URL + "/booking";

    public BookingApi(String token) {
        this.token = token;
    }

    public static Booking get(final String id) {
        final var rsp = RestAssured.get(URL + "/" + id);
        if (rsp.statusCode() == HttpStatus.SC_NOT_FOUND) {
            return null;
        }
        return rsp.as(Booking.class, Mapper.getJsonMapper());
    }

    public static String create(Booking rq) {
        return given()
                .body(rq, Mapper.getJsonMapper())
                .contentType(ContentType.JSON)
                .when()
                .post(URL)
                .jsonPath().getString("bookingid");
    }

    public Response partialUpdate(final String id, final PartialBooking rq) {
        return partialUpdate(id, rq, token);
    }

    public Response partialUpdate(final String id, @NotNull final PartialBooking rq, final String token) {
        var spec = given()
                .body(rq, Mapper.getJsonMapper())
                .contentType(ContentType.JSON)
                .header("Accept", "application/json");
        if (token != null) {
            spec = spec.cookie(new Cookie.Builder("token", token).build());
        }
        return spec.when().patch(URL + "/" + id);
    }

    public <V> Response partialUpdatePlain(final String id, final Map<String, V> rq) {
        var spec = given()
                .contentType(ContentType.JSON)
                .header("Accept", "application/json")
                .cookie(new Cookie.Builder("token", token).build());
        if (rq != null) {
            spec = spec.body(rq);

        }
        return spec.when().patch(URL + "/" + id);
    }

    public Response delete(final String id) {
        return given()
                .contentType(ContentType.JSON)
                .cookie(new Cookie.Builder("token", token).build())
                .when()
                .delete(URL + "/" + id);
    }

    public Response getIds() {
        return getIds(new HashMap<>());
    }


    public<V> Response getIds(HashMap<String, V> pathParams) {
        return given()
                .contentType(ContentType.JSON)
                .params(pathParams)
                .when()
                .get(URL);
    }
}
