package com.booking.test

import com.booking.api.BookingApi
import com.booking.api.vo.BookingDates
import com.booking.api.vo.PartialBooking
import com.booking.api.vo.PartialBookingDates
import org.apache.http.HttpStatus
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.time.LocalDate
@DisplayName("PartialUpdateBooking")
class PartialUpdateTest : BookingApiTest() {

    @Nested
    @DisplayName("Update and check all fields")
    inner class checkAllFields {
        @Test
        fun `Lastname should be changed, other fields unchangeable`() {
            val id = BookingApi.create(
                booking(
                    lastName = "Bb",
                    firstName = "Joey",
                    totalPrice = 252,
                    bookingDates = BookingDates(
                        LocalDate.of(2011, 12, 30),
                        LocalDate.of(2011, 12, 31)
                    ),
                    depositPaid = true
                )
            )
            val rq = PartialBooking(lastName = "Brown")

            val response = bookingApi.partialUpdate(id, rq)

            assertEquals(HttpStatus.SC_OK, response.statusCode)
            val body = response.jsonPath()
            assertEquals("Brown", body.get("lastname"))
            assertEquals("Joey", body.get("firstname"))
            assertEquals(252, body.getLong("totalprice"))
            assertEquals(true, body.getBoolean("depositpaid"))
            assertEquals("2011-12-30", body.get("bookingdates.checkin"))
            assertEquals("2011-12-31", body.get("bookingdates.checkout"))
            assertNull(response.jsonPath().get("additionalneeds"))
        }

        @Test
        fun `Deposit and additional need should be changed, other fields unchangeable`() {
            val id = BookingApi.create(
                booking(
                    lastName = "Bb",
                    firstName = "Joey",
                    totalPrice = 252,
                    bookingDates = BookingDates(
                        LocalDate.of(2011, 12, 30),
                        LocalDate.of(2011, 12, 31)
                    ),
                    depositPaid = false
                )
            )
            val rq = PartialBooking(depositPaid = true, additionalNeeds = "Breakfast")

            val response = bookingApi.partialUpdate(id, rq)


            assertEquals(HttpStatus.SC_OK, response.statusCode)
            val body = response.jsonPath()
            assertEquals("Bb", body.get("lastname"))
            assertEquals("Joey", body.get("firstname"))
            assertEquals(252, body.getLong("totalprice"))
            assertEquals(true, body.getBoolean("depositpaid"))
            assertEquals("2011-12-30", body.get("bookingdates.checkin"))
            assertEquals("2011-12-31", body.get("bookingdates.checkout"))
            assertEquals("Breakfast", body.get("additionalneeds"))
        }

        @Test
        fun `Nothing should be changed after update with empty body`() {
            val id = BookingApi.create(
                booking(
                    lastName = "Bb",
                    firstName = "Joey",
                    totalPrice = 252,
                    bookingDates = BookingDates(
                        LocalDate.of(2011, 12, 30),
                        LocalDate.of(2011, 12, 31)
                    ),
                    depositPaid = false,
                    additionalNeeds = ""
                )
            )

            val response = bookingApi.partialUpdatePlain<Any>(id, null)

            assertEquals(HttpStatus.SC_OK, response.statusCode)
            val body = response.jsonPath()
            assertEquals("Bb", body.get("lastname"))
            assertEquals("Joey", body.get("firstname"))
            assertEquals(252, body.getLong("totalprice"))
            assertFalse(body.getBoolean("depositpaid"))
            assertEquals("2011-12-30", body.get("bookingdates.checkin"))
            assertEquals("2011-12-31", body.get("bookingdates.checkout"))
            assertEquals("", body.get("additionalneeds"))
        }

        @Test
        fun `Every field should be changed`() {
            val id = BookingApi.create(
                booking(
                    lastName = "Bb", firstName = "Joey", totalPrice = 252,
                    bookingDates = BookingDates(
                        LocalDate.of(2011, 12, 30),
                        LocalDate.of(2011, 12, 31)
                    ),
                    depositPaid = false
                )
            )
            val rq = PartialBooking(
                firstName = "Jo", lastName = "Brown", totalPrice = 300, depositPaid = true,
                bookingDates = PartialBookingDates(
                    LocalDate.of(2012, 1, 30),
                    LocalDate.of(2012, 1, 31)
                ),
                additionalNeeds = "Breakfast"
            )

            val response = bookingApi.partialUpdate(id, rq)


            assertEquals(HttpStatus.SC_OK, response.statusCode)
            val body = response.jsonPath()
            assertEquals("Brown", body.get("lastname"))
            assertEquals("Jo", body.get("firstname"))
            assertEquals(300, body.getLong("totalprice"))
            assertEquals(true, body.getBoolean("depositpaid"))
            assertEquals("2012-01-30", body.get("bookingdates.checkin"))
            assertEquals("2012-01-31", body.get("bookingdates.checkout"))
            assertEquals("Breakfast", body.get("additionalneeds"))
        }
    }

    @Test
    fun `Checkin only should be changed`() {
        val id = BookingApi.create(
            booking(
                bookingDates = BookingDates(
                    LocalDate.of(2011, 12, 30),
                    LocalDate.of(2011, 12, 31)
                ),
                depositPaid = false
            )
        )
        val rq = PartialBooking(
            bookingDates = PartialBookingDates(
                checkIn = LocalDate.of(2011, 12, 27)
            )
        )

        val response = bookingApi.partialUpdate(id, rq)

        assertEquals(HttpStatus.SC_OK, response.statusCode)
        val body = response.jsonPath()
        assertEquals("2011-12-27", body.get("bookingdates.checkin"))
        assertEquals("2011-12-31", body.get("bookingdates.checkout"))
    }

    @Test
    fun `Should be changed with checkin bigger than checkout`() {
        val id = BookingApi.create(
            booking(
                bookingDates = BookingDates(
                    LocalDate.of(2011, 12, 30),
                    LocalDate.of(2011, 12, 31)
                )
            )
        )
        val rq = PartialBooking(
            bookingDates = PartialBookingDates(
                LocalDate.of(2012, 1, 30),
                LocalDate.of(2012, 1, 15)
            )
        )

        val response = bookingApi.partialUpdate(id, rq)

        assertEquals(HttpStatus.SC_OK, response.statusCode)
        val body = response.jsonPath()
        assertEquals("2012-01-30", body.get("bookingdates.checkin"))
        assertEquals("2012-01-15", body.get("bookingdates.checkout"))
    }

    @Nested
    @DisplayName("Incorrect format")
    inner class IncorrectFormat {
        @Test
        fun `Should be successful with non-existing field, not applied`() {
            val id = BookingApi.create(booking())
            val rq = mapOf("depositamount" to 200)

            val response = bookingApi.partialUpdatePlain(id, rq)

            assertEquals(HttpStatus.SC_OK, response.statusCode)
            assertNull(response.jsonPath().get("depositamount"))
        }

        @Test
        fun `Should be successful with Double in amount of totalPrice`() {
            val id = BookingApi.create(booking(totalPrice = 250))
            val rq = mapOf("totalprice" to "220.50")

            val response = bookingApi.partialUpdatePlain(id, rq)

            assertEquals(HttpStatus.SC_OK, response.statusCode)
            assertEquals(220, response.jsonPath().getLong("totalprice"))
        }

        @Test
        fun `Should be successful with negative amount of totalPrice`() {
            val id = BookingApi.create(booking(totalPrice = 250))
            val rq = PartialBooking(totalPrice = -100)

            val response = bookingApi.partialUpdate(id, rq)

            assertEquals(HttpStatus.SC_OK, response.statusCode)
            assertEquals(-100, response.jsonPath().getLong("totalprice"))
        }

        @Test
        fun `Should be successful with String in depositPaid`() {
            val id = BookingApi.create(booking(depositPaid = false))
            val rq = mapOf("depositpaid" to "true")

            val response = bookingApi.partialUpdatePlain(id, rq)

            assertEquals(HttpStatus.SC_OK, response.statusCode)
            assertTrue(response.jsonPath().getBoolean("depositpaid"))
        }

        @Test
        fun `Should be successful with incorrect month and day values`() {
            val id = BookingApi.create(booking())
            val rq = mapOf(
                "bookingdates" to mapOf(
                    "checkin" to "2012-33-93",
                    "checkout" to "2012-44-83"
                )
            )

            val response = bookingApi.partialUpdatePlain(id, rq)

            assertEquals(HttpStatus.SC_OK, response.statusCode)
            assertEquals("0NaN-aN-aN", response.jsonPath().get("bookingdates.checkin"))
            assertEquals("0NaN-aN-aN", response.jsonPath().get("bookingdates.checkout"))
        }

        @Test
        fun `Should be successful with incorrect order in date format`() {
            val id = BookingApi.create(booking())
            val rq = mapOf(
                "bookingdates" to mapOf(
                    "checkin" to "12-30-2015",
                    "checkout" to "12-31-2015"
                )
            )

            val response = bookingApi.partialUpdatePlain(id, rq)

            assertEquals(HttpStatus.SC_OK, response.statusCode)
            assertEquals("2015-12-30", response.jsonPath().get("bookingdates.checkin"))
            assertEquals("2015-12-31", response.jsonPath().get("bookingdates.checkout"))
        }

        @Test
        fun `Should be successful with incorrect date format`() {
            val id = BookingApi.create(booking())
            val rq = mapOf(
                "bookingdates" to mapOf(
                    "checkin" to "30/12/2015",
                    "checkout" to "31/12/2015"
                )
            )

            val response = bookingApi.partialUpdatePlain(id, rq)

            assertEquals(HttpStatus.SC_OK, response.statusCode)
            assertEquals("0NaN-aN-aN", response.jsonPath().get("bookingdates.checkin"))
            assertEquals("0NaN-aN-aN", response.jsonPath().get("bookingdates.checkout"))
        }

        @Test
        fun `Should be successful with duplicate field, last applied`() {
            val id = BookingApi.create(booking(lastName = "Aaa"))
            val rq = mapOf("lastname" to "Brown", "lastname" to "Tribiani")

            val response = bookingApi.partialUpdatePlain(id, rq)

            assertEquals(HttpStatus.SC_OK, response.statusCode)
            assertEquals("Tribiani", response.jsonPath().get("lastname"))
        }

        @Test
        fun `Should be forbidden without cookie`() {
            val id = BookingApi.create(booking())

            val response = bookingApi.partialUpdate(id, simplePartialRq(), null)

            assertEquals(HttpStatus.SC_FORBIDDEN, response.statusCode)
        }

        @Test
        fun `Should be not allowed by negative id`() {
            val id = BookingApi.create(booking())

            val response = bookingApi.partialUpdate("-$id", simplePartialRq())

            assertEquals(HttpStatus.SC_METHOD_NOT_ALLOWED, response.statusCode)
        }

        @Test
        fun `Should be not allowed by non-existing id`() {
            val id = BookingApi.create(booking())

            bookingApi.delete(id)

            val response = bookingApi.partialUpdate(id, simplePartialRq())

            assertEquals(HttpStatus.SC_METHOD_NOT_ALLOWED, response.statusCode)
        }

        @Test
        fun `Should be not allowed by String instead of id`() {
            val response = bookingApi.partialUpdatePlain("acc", mapOf("additionalNeeds" to ""))

            assertEquals(HttpStatus.SC_METHOD_NOT_ALLOWED, response.statusCode)
        }
    }

    private fun simplePartialRq() = PartialBooking(additionalNeeds = "Breakfast")
}