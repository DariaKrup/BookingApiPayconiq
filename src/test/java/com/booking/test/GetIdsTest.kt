package com.booking.test

import com.booking.api.BookingApi
import com.booking.api.vo.BookingDates
import com.booking.api.vo.BookingFilter
import com.booking.api.vo.PartialBooking
import org.apache.http.HttpStatus
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.time.LocalDate

class GetIdsTest : BookingApiTest() {

    @Test
    fun `Get booking ids and check non-empty`() {
        val id = BookingApi.create(booking())

        val response = bookingApi.getIds()

        assertEquals(HttpStatus.SC_OK, response.statusCode)
        assertFalse(response.body().jsonPath().getList<Any>("$").isEmpty())
    }

    @Test
    fun `Get booking ids and check existence`() {
        val id = BookingApi.create(booking())

        val response = bookingApi.getIds()

        assertEquals(HttpStatus.SC_OK, response.statusCode)
        assertTrue(response.body().jsonPath().getList<HashMap<String, Int>>("$")
            .contains(hashMapOf("bookingid" to id.toInt())))
    }

    @Test
    fun `Get booking ids and check existence after deletion`() {
        val id = BookingApi.create(booking())
        bookingApi.delete(id.toString())

        val response = bookingApi.getIds()

        assertEquals(HttpStatus.SC_OK, response.statusCode)
        assertFalse(response.body().jsonPath().getList<HashMap<String, Int>>("$")
            .contains(hashMapOf("bookingid" to id.toInt())))
    }

    @Test
    fun `Get booking ids and check existence with filter on firstname`() {
        val id = BookingApi.create(booking(firstName = "Mathew"))
        val filter = BookingFilter(firstName = "Mathew")

        val response = bookingApi.getIds(filter)

        assertEquals(HttpStatus.SC_OK, response.statusCode)
        assertTrue(response.body().jsonPath().getList<HashMap<String, Int>>("$")
            .contains(hashMapOf("bookingid" to id.toInt())))
    }

    @Test
    fun `Get booking ids and check existence with filter on lastname`() {
        val id = BookingApi.create(booking(lastName = "Bing"))
        val filter = BookingFilter(lastName = "Bing")

        val response = bookingApi.getIds(filter)

        assertEquals(HttpStatus.SC_OK, response.statusCode)
        assertTrue(response.body().jsonPath().getList<HashMap<String, Int>>("$")
            .contains(hashMapOf("bookingid" to id.toInt())))
    }

    @Test
    fun `Get booking ids and check existence with filter on firstname and lastname`() {
        val id = BookingApi.create(booking(firstName = "Chandler", lastName = "Bing"))
        val filter = BookingFilter(firstName = "Chandler", lastName = "Bing")

        val response = bookingApi.getIds(filter)

        assertEquals(HttpStatus.SC_OK, response.statusCode)
        assertTrue(response.body().jsonPath().getList<HashMap<String, Int>>("$")
            .contains(hashMapOf("bookingid" to id.toInt())))
    }

    @Test
    fun `Get booking with absorption of checkin-checkout period`() {
        val id = BookingApi.create(booking(
            bookingDates = BookingDates(
                LocalDate.of(2011, 12, 30),
                LocalDate.of(2011, 12, 31)
            )
        ))
        val filter = BookingFilter(checkIn = LocalDate.of(2011, 12, 28),
            checkOut = LocalDate.of(2012, 1, 2))

        val response = bookingApi.getIds(filter)

        assertEquals(HttpStatus.SC_OK, response.statusCode)
        assertTrue(response.body().jsonPath().getList<HashMap<String, Int>>("$")
            .contains(hashMapOf("bookingid" to id.toInt())))
    }

    @Test
    fun `Get booking with intersection of checkin-checkout period on the left border`() {
        val id = BookingApi.create(booking(
            bookingDates = BookingDates(
                LocalDate.of(2011, 12, 30),
                LocalDate.of(2011, 12, 31)
            )
        ))
        val filter = BookingFilter(checkIn = LocalDate.of(2011, 12, 30))

        val response = bookingApi.getIds(filter)

        assertEquals(HttpStatus.SC_OK, response.statusCode)
        assertTrue(response.body().jsonPath().getList<HashMap<String, Int>>("$")
            .contains(hashMapOf("bookingid" to id.toInt())))
    }

    @Test
    fun `Get booking with intersection of checkin-checkout period on the right border`() {
        val id = BookingApi.create(booking(
            bookingDates = BookingDates(
                LocalDate.of(2011, 12, 30),
                LocalDate.of(2012, 12, 31)
            )
        ))
        val filter = BookingFilter(checkOut = LocalDate.of(2011, 12, 31))

        val response = bookingApi.getIds(filter)

        assertEquals(HttpStatus.SC_OK, response.statusCode)
        assertTrue(response.body().jsonPath().getList<HashMap<String, Int>>("$")
            .contains(hashMapOf("bookingid" to id.toInt())))
    }


    @Test
    fun `Get booking from right interval without intersection of checkin-checkout period`() {
        val id = BookingApi.create(booking(
            bookingDates = BookingDates(
                LocalDate.of(2011, 12, 30),
                LocalDate.of(2011, 12, 31)
            )
        ))
        val filter = BookingFilter(checkIn = LocalDate.of(2014, 12, 30),
            checkOut = LocalDate.of(2014, 12, 31))

        val response = bookingApi.getIds(filter)

        assertEquals(HttpStatus.SC_OK, response.statusCode)
        assertFalse(response.body().jsonPath().getList<HashMap<String, Int>>("$")
            .contains(hashMapOf("bookingid" to id.toInt())))
    }

    @Test
    fun `Get booking from left interval without intersection of checkin-checkout period`() {
        val id = BookingApi.create(booking(
            bookingDates = BookingDates(
                LocalDate.of(2011, 12, 30),
                LocalDate.of(2011, 12, 31)
            )
        ))
        val filter = BookingFilter(checkIn = LocalDate.of(2008, 12, 30),
            checkOut = LocalDate.of(2009, 12, 31))

        val response = bookingApi.getIds(filter)

        assertEquals(HttpStatus.SC_OK, response.statusCode)
        assertTrue(response.body().jsonPath().getList<HashMap<String, Int>>("$")
            .contains(hashMapOf("bookingid" to id.toInt())))
    }

    @Test
    fun `Get booking from left interval with intersection of checkin-checkout period`() {
        val id = BookingApi.create(booking(
            bookingDates = BookingDates(
                LocalDate.of(2011, 12, 30),
                LocalDate.of(2012, 1, 5)
            )
        ))
        val filter = BookingFilter(checkIn = LocalDate.of(2011, 12, 30),
            checkOut = LocalDate.of(2012, 1, 3))

        val response = bookingApi.getIds(filter)

        assertEquals(HttpStatus.SC_OK, response.statusCode)
        assertTrue(response.body().jsonPath().getList<HashMap<String, Int>>("$")
            .contains(hashMapOf("bookingid" to id.toInt())))
    }

    @Test
    fun `Get booking with non-existing filter`() {
        BookingApi.create(booking())

        val response = bookingApi.getIds(hashMapOf("additionalneeds" to "Breakfast"))

        assertEquals(HttpStatus.SC_OK, response.statusCode)
        assertFalse(response.body().jsonPath().getList<Any>("$").isEmpty())
    }

    @Test
    fun `Get booking with incorrect month and date value`() {
        BookingApi.create(booking())

        val response = bookingApi.getIds(hashMapOf("checkin" to "2012-33-77"))

        assertEquals(HttpStatus.SC_OK, response.statusCode)
        assertFalse(response.body().jsonPath().getList<Any>("$").isEmpty())
    }

    @Test
    fun `Get booking ids check after partialUpdate on firstName`() {
        val id = BookingApi.create(booking(firstName = "Ivan"))
        bookingApi.partialUpdate(id, PartialBooking(firstName = "Mathew"))
        val filter = BookingFilter(firstName = "Mathew")

        val response = bookingApi.getIds(filter)

        assertEquals(HttpStatus.SC_OK, response.statusCode)
        assertTrue(response.body().jsonPath().getList<HashMap<String, Int>>("$")
            .contains(hashMapOf("bookingid" to id.toInt())))
    }
}