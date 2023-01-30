package com.booking.test

import com.booking.api.BookingApi
import com.booking.api.vo.BookingDates
import com.booking.api.vo.PartialBooking
import io.restassured.response.Response
import org.apache.http.HttpStatus
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.time.LocalDate

@DisplayName("GetBookingIds")
class GetIdsTest : BookingApiTest() {

    @Test
    fun `Should include when no filters`() {
        val id = BookingApi.create(booking())

        val response = bookingApi.getIds()

        assertEquals(HttpStatus.SC_OK, response.statusCode)
        assertTrue(response.contains(id))
    }

    @Test
    fun `Should not include when non-existing id`() {
        val id = BookingApi.create(booking())
        bookingApi.delete(id)

        val response = bookingApi.getIds()

        assertEquals(HttpStatus.SC_OK, response.statusCode)
        assertFalse(response.contains(id))
    }

    @Nested
    @DisplayName("Filters by name")
    inner class NameFilterGroup {
        @Test
        fun `Should include when filtered by firstname`() {
            val idPassing = BookingApi.create(booking(firstName = "Mathew"))
            val idNotPassing = BookingApi.create(booking(firstName = "Andrew"))
            val filter = mapOf("firstname" to "Mathew")

            val response = bookingApi.getIds(filter)

            assertEquals(HttpStatus.SC_OK, response.statusCode)
            assertTrue(response.contains(idPassing))
            assertFalse(response.contains(idNotPassing))
        }

        @Test
        fun `Should include when filtered by lastname`() {
            val idPassing = BookingApi.create(booking(lastName = "Bing"))
            val idNotPassing = BookingApi.create(booking(lastName = "Andrew"))
            val filter = mapOf("lastname" to "Bing")

            val response = bookingApi.getIds(filter)

            assertEquals(HttpStatus.SC_OK, response.statusCode)
            assertTrue(response.contains(idPassing))
            assertFalse(response.contains(idNotPassing))
        }

        @Test
        fun `Should combine predicates when filtered by firstname and lastname`() {
            val idPassing = BookingApi.create(booking(firstName = "Chandler", lastName = "Bing"))
            val idNotPassing = BookingApi.create(booking(firstName = "Andrew", lastName = "Bing"))
            val filter = mapOf("firstname" to "Chandler", "lastname" to "Bing")

            val response = bookingApi.getIds(filter)

            assertEquals(HttpStatus.SC_OK, response.statusCode)
            assertTrue(response.contains(idPassing))
            assertFalse(response.contains(idNotPassing))
        }
    }

    @Nested
    @DisplayName("Filters by date")
    inner class DateFilterGroup {
        @Test
        fun `Should not include when checkin greater than filter, checkout less than filter`() {
            val id = BookingApi.create(
                booking(
                    bookingDates = BookingDates(
                        LocalDate.of(2011, 12, 30),
                        LocalDate.of(2011, 12, 31)
                    )
                )
            )
            val filter = mapOf(
                "checkin" to "2011-12-28",
                "checkout" to "2012-01-02"
            )

            val response = bookingApi.getIds(filter)

            assertEquals(HttpStatus.SC_OK, response.statusCode)
            assertFalse(response.contains(id))
        }

        @Test
        fun `Should not include when checkin less than filter, checkout greater than filter`() {
            val id = BookingApi.create(
                booking(
                    bookingDates = BookingDates(
                        LocalDate.of(2011, 12, 27),
                        LocalDate.of(2011, 12, 31)
                    )
                )
            )
            val filter = mapOf(
                "checkin" to "2011-12-28",
                "checkout" to "2011-12-30"
            )

            val response = bookingApi.getIds(filter)

            assertEquals(HttpStatus.SC_OK, response.statusCode)
            assertFalse(response.contains(id))
        }

        @Test
        fun `Should include when checkin == filter`() {
            val id = BookingApi.create(
                booking(
                    bookingDates = BookingDates(
                        LocalDate.of(2011, 12, 30),
                        LocalDate.of(2011, 12, 31)
                    )
                )
            )
            val filter = mapOf("checkin" to "2011-12-30")

            val response = bookingApi.getIds(filter)

            assertEquals(HttpStatus.SC_OK, response.statusCode)
            assertTrue(response.contains(id))
        }

        @Test
        fun `Should include when checkout == filter`() {
            val id = BookingApi.create(
                booking(
                    bookingDates = BookingDates(
                        LocalDate.of(2011, 12, 30),
                        LocalDate.of(2011, 12, 31)
                    )
                )
            )
            val filter = mapOf("checkout" to "2011-12-31")

            val response = bookingApi.getIds(filter)

            assertEquals(HttpStatus.SC_OK, response.statusCode)
            assertTrue(response.contains(id))
        }

        @Test
        fun `Should not include when checkin less than filter, checkout less than filter`() {
            val id = BookingApi.create(
                booking(
                    bookingDates = BookingDates(
                        LocalDate.of(2011, 12, 30),
                        LocalDate.of(2011, 12, 31)
                    )
                )
            )
            val filter = mapOf(
                "checkin" to "2014-12-30",
                "checkout" to "2014-12-31"
            )

            val response = bookingApi.getIds(filter)

            assertEquals(HttpStatus.SC_OK, response.statusCode)
            assertFalse(response.contains(id))
        }

        @Test
        fun `Should include when checkin greater than filter, checkout greater than filter`() {
            val id = BookingApi.create(
                booking(
                    bookingDates = BookingDates(
                        LocalDate.of(2011, 12, 30),
                        LocalDate.of(2011, 12, 31)
                    )
                )
            )
            val filter = mapOf(
                "checkin" to "2008-12-30",
                "checkout" to "2009-12-31"
            )

            val response = bookingApi.getIds(filter)

            assertEquals(HttpStatus.SC_OK, response.statusCode)
            assertTrue(response.contains(id))
        }

        @Test
        fun `Should include when checkin == filter date, checkout greater than filter`() {
            val id = BookingApi.create(
                booking(
                    bookingDates = BookingDates(
                        LocalDate.of(2011, 12, 30),
                        LocalDate.of(2012, 1, 14)
                    )
                )
            )
            val filter = mapOf(
                "checkin" to "2011-12-30",
                "checkout" to "2012-01-03"
            )

            val response = bookingApi.getIds(filter)

            assertEquals(HttpStatus.SC_OK, response.statusCode)
            assertTrue(response.contains(id))
        }

        @Test
        fun `Should include when checkin greater than filter, checkout == filter`() {
            val id = BookingApi.create(
                booking(
                    bookingDates = BookingDates(
                        LocalDate.of(2011, 12, 30),
                        LocalDate.of(2012, 1, 5)
                    )
                )
            )
            val filter = mapOf(
                "checkin" to "2011-12-25",
                "checkout" to "2012-01-05"
            )

            val response = bookingApi.getIds(filter)

            assertEquals(HttpStatus.SC_OK, response.statusCode)
            assertTrue(response.contains(id))
        }
    }

    @Test
    fun `Should include after PartialUpdateBooking on firstname`() {
        val id = BookingApi.create(booking(firstName = "Ivan"))
        bookingApi.partialUpdate(id, PartialBooking(firstName = "Mathew"))

        val response = bookingApi.getIds(mapOf("firstname" to "Mathew"))

        assertEquals(HttpStatus.SC_OK, response.statusCode)
        assertTrue(response.contains(id))
    }

    @Nested
    @DisplayName("Incorrect format")
    inner class IncorrectFormat {
        @Test
        fun `Should pe positive on non-existing filter param`() {
            BookingApi.create(booking())

            val response = bookingApi.getIds(mapOf("additionalneeds" to "Breakfast"))

            assertEquals(HttpStatus.SC_OK, response.statusCode)
        }

        @Test
        fun `Should be error when month and date have incorrect value`() {
            BookingApi.create(booking())

            val response = bookingApi.getIds(mapOf("checkin" to "2012-33-77"))

            assertEquals(HttpStatus.SC_INTERNAL_SERVER_ERROR, response.statusCode)
        }

        @Test
        fun `Should be error on incorrect date format`() {
            BookingApi.create(booking())

            val response = bookingApi.getIds(mapOf("checkin" to "31/12/3014"))

            assertEquals(HttpStatus.SC_INTERNAL_SERVER_ERROR, response.statusCode)
        }
    }

    private fun Response.contains(id: String) = body().jsonPath().getList<HashMap<String, Int>>("$")
        .contains(mapOf("bookingid" to id.toInt()))
}