package com.booking.test

import com.booking.api.BookingApi
import com.booking.api.vo.BookingDates
import com.booking.api.vo.PartialBooking
import org.apache.http.HttpStatus
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.time.LocalDate

@DisplayName("Get ids")
class GetIdsTest : BookingApiTest() {

    @Test
    fun `Check existence`() {
        val id = BookingApi.create(booking())

        val response = bookingApi.getIds()

        assertEquals(HttpStatus.SC_OK, response.statusCode)
        assertTrue(response.body().jsonPath().getList<HashMap<String, Int>>("$")
            .contains(hashMapOf("bookingid" to id.toInt())))
    }

    @Test
    fun `Check existence after deletion`() {
        val id = BookingApi.create(booking())
        bookingApi.delete(id.toString())

        val response = bookingApi.getIds()

        assertEquals(HttpStatus.SC_OK, response.statusCode)
        assertFalse(response.body().jsonPath().getList<HashMap<String, Int>>("$")
            .contains(hashMapOf("bookingid" to id.toInt())))
    }

    @Nested
    @DisplayName("Request with filter")
    inner class filter {
        @Test
        fun `By firstname`() {
            val id = BookingApi.create(booking(firstName = "Mathew"))
            val filter = hashMapOf("firstname" to "Mathew")

            val response = bookingApi.getIds(filter)

            assertEquals(HttpStatus.SC_OK, response.statusCode)
            assertTrue(
                response.body().jsonPath().getList<HashMap<String, Int>>("$")
                    .contains(hashMapOf("bookingid" to id.toInt()))
            )
        }

        @Test
        fun `By lastname`() {
            val id = BookingApi.create(booking(lastName = "Bing"))
            val filter = hashMapOf("lastname" to "Bing")

            val response = bookingApi.getIds(filter)

            assertEquals(HttpStatus.SC_OK, response.statusCode)
            assertTrue(
                response.body().jsonPath().getList<HashMap<String, Int>>("$")
                    .contains(hashMapOf("bookingid" to id.toInt()))
            )
        }

        @Test
        fun `By firstname and lastname`() {
            val id = BookingApi.create(booking(firstName = "Chandler", lastName = "Bing"))
            val filter = hashMapOf("firstname" to "Chandler", "lastname" to "Bing")

            val response = bookingApi.getIds(filter)

            assertEquals(HttpStatus.SC_OK, response.statusCode)
            assertTrue(
                response.body().jsonPath().getList<HashMap<String, Int>>("$")
                    .contains(hashMapOf("bookingid" to id.toInt()))
            )
        }
    }

    @Nested
    @DisplayName("Filters with checkin-checkout")
    inner class TestDates {
        @Test
        fun `Checkin greater than filter date, checkout less than filter date`() {
            val id = BookingApi.create(
                booking(
                    bookingDates = BookingDates(
                        LocalDate.of(2011, 12, 30),
                        LocalDate.of(2011, 12, 31)
                    )
                )
            )
            val filter = hashMapOf(
                "checkin" to "2011-12-28",
                "checkout" to "2012-01-02"
            )

            val response = bookingApi.getIds(filter)

            assertEquals(HttpStatus.SC_OK, response.statusCode)
            assertFalse(
                response.body().jsonPath().getList<HashMap<String, Int>>("$")
                    .contains(hashMapOf("bookingid" to id.toInt()))
            )
        }

        @Test
        fun `Checkin less than filter date, checkout greater than filter date`() {
            val id = BookingApi.create(
                booking(
                    bookingDates = BookingDates(
                        LocalDate.of(2011, 12, 27),
                        LocalDate.of(2011, 12, 31)
                    )
                )
            )
            val filter = hashMapOf(
                "checkin" to "2011-12-28",
                "checkout" to "2011-12-30"
            )

            val response = bookingApi.getIds(filter)

            assertEquals(HttpStatus.SC_OK, response.statusCode)
            assertFalse(
                response.body().jsonPath().getList<HashMap<String, Int>>("$")
                    .contains(hashMapOf("bookingid" to id.toInt()))
            )
        }

        @Test
        fun `Checkin equal to filter date`() {
            val id = BookingApi.create(
                booking(
                    bookingDates = BookingDates(
                        LocalDate.of(2011, 12, 30),
                        LocalDate.of(2011, 12, 31)
                    )
                )
            )
            val filter = hashMapOf("checkin" to "2011-12-30")

            val response = bookingApi.getIds(filter)

            assertEquals(HttpStatus.SC_OK, response.statusCode)
            assertTrue(
                response.body().jsonPath().getList<HashMap<String, Int>>("$")
                    .contains(hashMapOf("bookingid" to id.toInt()))
            )
        }

        @Test
        fun `Checkout equal to filter date`() {
            val id = BookingApi.create(
                booking(
                    bookingDates = BookingDates(
                        LocalDate.of(2011, 12, 30),
                        LocalDate.of(2011, 12, 31)
                    )
                )
            )
            val filter = hashMapOf("checkout" to "2011-12-31")

            val response = bookingApi.getIds(filter)

            assertEquals(HttpStatus.SC_OK, response.statusCode)
            assertTrue(
                response.body().jsonPath().getList<HashMap<String, Int>>("$")
                    .contains(hashMapOf("bookingid" to id.toInt()))
            )
        }

        @Test
        fun `Checkin less than filter date, checkout less than filter date`() {
            val id = BookingApi.create(
                booking(
                    bookingDates = BookingDates(
                        LocalDate.of(2011, 12, 30),
                        LocalDate.of(2011, 12, 31)
                    )
                )
            )
            val filter = hashMapOf(
                "checkin" to "2014-12-30",
                "checkout" to "2014-12-31"
            )

            val response = bookingApi.getIds(filter)

            assertEquals(HttpStatus.SC_OK, response.statusCode)
            assertFalse(
                response.body().jsonPath().getList<HashMap<String, Int>>("$")
                    .contains(hashMapOf("bookingid" to id.toInt()))
            )
        }

        @Test
        fun `Checkin greater than filter date, checkout greater than filter date`() {
            val id = BookingApi.create(
                booking(
                    bookingDates = BookingDates(
                        LocalDate.of(2011, 12, 30),
                        LocalDate.of(2011, 12, 31)
                    )
                )
            )
            val filter = hashMapOf(
                "checkin" to "2008-12-30",
                "checkout" to "2009-12-31"
            )

            val response = bookingApi.getIds(filter)

            assertEquals(HttpStatus.SC_OK, response.statusCode)
            assertTrue(
                response.body().jsonPath().getList<HashMap<String, Int>>("$")
                    .contains(hashMapOf("bookingid" to id.toInt()))
            )
        }

        @Test
        fun `Checkin equal to filter date, checkout greater than filter date`() {
            val id = BookingApi.create(
                booking(
                    bookingDates = BookingDates(
                        LocalDate.of(2011, 12, 30),
                        LocalDate.of(2012, 1, 14)
                    )
                )
            )
            val filter = hashMapOf(
                "checkin" to "2011-12-30",
                "checkout" to "2012-01-03"
            )

            val response = bookingApi.getIds(filter)

            assertEquals(HttpStatus.SC_OK, response.statusCode)
            assertTrue(
                response.body().jsonPath().getList<HashMap<String, Int>>("$")
                    .contains(hashMapOf("bookingid" to id.toInt()))
            )
        }

        @Test
        fun `Checkin greater than filter date, checkout equal to filter date`() {
            val id = BookingApi.create(
                booking(
                    bookingDates = BookingDates(
                        LocalDate.of(2011, 12, 30),
                        LocalDate.of(2012, 1, 5)
                    )
                )
            )
            val filter = hashMapOf(
                "checkin" to "2011-12-25",
                "checkout" to "2012-01-05"
            )

            val response = bookingApi.getIds(filter)

            assertEquals(HttpStatus.SC_OK, response.statusCode)
            assertTrue(
                response.body().jsonPath().getList<HashMap<String, Int>>("$")
                    .contains(hashMapOf("bookingid" to id.toInt()))
            )
        }
    }

    @Test
    fun `Get booking ids check after partialUpdate on firstName`() {
        val id = BookingApi.create(booking(firstName = "Ivan"))
        bookingApi.partialUpdate(id, PartialBooking(firstName = "Mathew"))
        val filter = hashMapOf("firstname" to "Mathew")

        val response = bookingApi.getIds(filter)

        assertEquals(HttpStatus.SC_OK, response.statusCode)
        assertTrue(response.body().jsonPath().getList<HashMap<String, Int>>("$")
            .contains(hashMapOf("bookingid" to id.toInt())))
    }

    @Nested
    @DisplayName("Incorrect format")
    inner class IncorrectFormat {
        @Test
        fun `Non-existing filter`() {
            BookingApi.create(booking())

            val response = bookingApi.getIds(hashMapOf("additionalneeds" to "Breakfast"))

            assertEquals(HttpStatus.SC_OK, response.statusCode)
        }

        @Test
        fun `Incorrect month and date value`() {
            BookingApi.create(booking())

            val response = bookingApi.getIds(hashMapOf("checkin" to "2012-33-77"))

            assertEquals(HttpStatus.SC_INTERNAL_SERVER_ERROR, response.statusCode)
        }

        @Test
        fun `Incorrect date format`() {
            BookingApi.create(booking())

            val response = bookingApi.getIds(hashMapOf("checkin" to "31/12/3014"))

            assertEquals(HttpStatus.SC_INTERNAL_SERVER_ERROR, response.statusCode)
        }
    }
}