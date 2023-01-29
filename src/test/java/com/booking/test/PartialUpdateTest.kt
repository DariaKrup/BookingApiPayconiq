package com.booking.test

import com.booking.api.BookingApi
import com.booking.api.vo.BookingDates
import com.booking.api.vo.PartialBooking
import org.apache.http.HttpStatus
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import java.time.LocalDate

class PartialUpdateTest : BookingApiTest() {

    @Test
    fun `Partial update of lastname by id`() {
        val id = BookingApi.create(booking(lastName = "Bb", firstName = "Joey", totalPrice = 252,
            bookingDates = BookingDates(
                LocalDate.of(2011, 12, 30),
                LocalDate.of(2011, 12, 31)
            ),
            depositPaid = true))
        val rq = PartialBooking(lastName = "Brown")

        val response = bookingApi.partialUpdate(id, rq)

        assertEquals(HttpStatus.SC_OK, response.statusCode)
        assertEquals("Brown", response.jsonPath().get("lastname"))
        assertEquals("Joey", response.jsonPath().get("firstname"))
        assertEquals(252, response.jsonPath().getLong("totalprice"))
        assertEquals(true, response.jsonPath().getBoolean("depositpaid"))
        assertEquals("2011-12-30", response.jsonPath().get("bookingdates.checkin"))
        assertEquals("2011-12-31", response.jsonPath().get("bookingdates.checkout"))
        assertNull(response.jsonPath().get("additionalneeds"))
    }

    @Test
    fun `Partial update of deposit and additional need by id`() {
        val id = BookingApi.create(booking(lastName = "Bb", firstName = "Joey", totalPrice = 252,
            bookingDates = BookingDates(
                LocalDate.of(2011, 12, 30),
                LocalDate.of(2011, 12, 31)
            ),
            depositPaid = false))
        val rq = PartialBooking(depositPaid = true, additionalNeeds = "Breakfast")

        val response = bookingApi.partialUpdate(id, rq)

        assertEquals(HttpStatus.SC_OK, response.statusCode)
        assertEquals("Bb", response.jsonPath().get("lastname"))
        assertEquals("Joey", response.jsonPath().get("firstname"))
        assertEquals(252, response.jsonPath().getLong("totalprice"))
        assertEquals(true, response.jsonPath().getBoolean("depositpaid"))
        assertEquals("2011-12-30", response.jsonPath().get("bookingdates.checkin"))
        assertEquals("2011-12-31", response.jsonPath().get("bookingdates.checkout"))
        assertEquals("Breakfast", response.jsonPath().get("additionalneeds"))
    }

    @Test
    fun `Partial update of all fields by id`() {
        val id = BookingApi.create(booking(lastName = "Bb", firstName = "Joey", totalPrice = 252,
            bookingDates = BookingDates(
                LocalDate.of(2011, 12, 30),
                LocalDate.of(2011, 12, 31)
            ),
            depositPaid = false))
        val rq = PartialBooking(firstName = "Jo", lastName = "Brown", totalPrice = 300, depositPaid = true,
            bookingDates = BookingDates(
                LocalDate.of(2012, 1, 30),
                LocalDate.of(2012, 1, 31)
            ),
            additionalNeeds = "Breakfast")

        val response = bookingApi.partialUpdate(id, rq)

        assertEquals(HttpStatus.SC_OK, response.statusCode)
        assertEquals("Brown", response.jsonPath().get("lastname"))
        assertEquals("Jo", response.jsonPath().get("firstname"))
        assertEquals(300, response.jsonPath().getLong("totalprice"))
        assertEquals(true, response.jsonPath().getBoolean("depositpaid"))
        assertEquals("2012-01-30", response.jsonPath().get("bookingdates.checkin"))
        assertEquals("2012-01-31", response.jsonPath().get("bookingdates.checkout"))
        assertEquals("Breakfast", response.jsonPath().get("additionalneeds"))
    }

    @Test
    fun `Partial update with checkin bigger than checkout`() {
        val id = BookingApi.create(booking(lastName = "Bb", firstName = "Joey", totalPrice = 252,
            bookingDates = BookingDates(
                LocalDate.of(2011, 12, 30),
                LocalDate.of(2011, 12, 31)
            ),
            depositPaid = true))
        val rq = PartialBooking(
            bookingDates = BookingDates(
                LocalDate.of(2012, 1, 30),
                LocalDate.of(2012, 1, 15)
            ))

        val response = bookingApi.partialUpdate(id, rq)

        assertEquals(HttpStatus.SC_OK, response.statusCode)
        assertEquals("Bb", response.jsonPath().get("lastname"))
        assertEquals("Joey", response.jsonPath().get("firstname"))
        assertEquals(252, response.jsonPath().getLong("totalprice"))
        assertEquals(true, response.jsonPath().getBoolean("depositpaid"))
        assertEquals("2012-01-30", response.jsonPath().get("bookingdates.checkin"))
        assertEquals("2012-01-15", response.jsonPath().get("bookingdates.checkout"))
    }

    @Test
    fun `Partial update with non-existing field`() {
        val id = BookingApi.create(booking())
        val rq = hashMapOf<String, Any>("depositamount" to 200)

        val response = bookingApi.partialUpdateMap(id.toString(), rq)

        assertEquals(HttpStatus.SC_OK, response.statusCode)
        assertNull(response.jsonPath().get("depositamount"))
    }

    @Test
    fun `Partial update with Double in amount of totalPrice`() {
        val id = BookingApi.create(booking(totalPrice = 250))
        val rq = hashMapOf<String, Any>("totalprice" to 200.50)

        val response = bookingApi.partialUpdateMap(id.toString(), rq)

        assertEquals(HttpStatus.SC_OK, response.statusCode)
        assertEquals(200, response.jsonPath().getLong("totalprice"))
    }

    @Test
    fun `Partial update with negative amount of totalPrice`() {
        val id = BookingApi.create(booking(totalPrice = 250))
        val rq = PartialBooking(totalPrice = -100)

        val response = bookingApi.partialUpdate(id, rq)

        assertEquals(HttpStatus.SC_OK, response.statusCode)
        assertEquals(-100, response.jsonPath().getLong("totalprice"))
    }

    @Test
    fun `Partial update with String in depositPaid`() {
        val id = BookingApi.create(booking(depositPaid = false))
        val rq = hashMapOf<String, Any>("depositpaid" to "PAID")

        val response = bookingApi.partialUpdateMap(id.toString(), rq)

        assertEquals(HttpStatus.SC_OK, response.statusCode)
        assertEquals(true, response.jsonPath().get("depositpaid"))
    }

    @Test
    fun `Partial update with Int in depositPaid`() {
        val id = BookingApi.create(booking(depositPaid = false))
        val rq = hashMapOf<String, Any>("depositpaid" to 10)

        val response = bookingApi.partialUpdateMap(id.toString(), rq)

        assertEquals(HttpStatus.SC_OK, response.statusCode)
        assertEquals(true, response.jsonPath().get("depositpaid"))
    }

    @Test
    fun `Partial update with incorrect date format`() {
        val id = BookingApi.create(booking())
        val rqInternal = hashMapOf<String, Any>("checkin" to "2012-33-93", "checkout" to "2012-44-83")
        val rq = hashMapOf<String, Any>("bookingdates" to rqInternal)

        val response = bookingApi.partialUpdateMap(id.toString(), rq)

        assertEquals(HttpStatus.SC_OK, response.statusCode)
        assertEquals("0NaN-aN-aN", response.jsonPath().get("bookingdates.checkin"))
        assertEquals("0NaN-aN-aN", response.jsonPath().get("bookingdates.checkout"))
    }

    @Test
    fun `Partial update with duplicate field`() {
        val id = BookingApi.create(booking())
        val rq = hashMapOf<String, Any>("lastname" to "Brown", "lastname" to "Tribiani")

        val response = bookingApi.partialUpdateMap(id.toString(), rq)

        assertEquals(HttpStatus.SC_OK, response.statusCode)
        assertEquals("Tribiani", response.jsonPath().get("lastname"))
    }

    @Test
    fun `Partial update without token (cookie)`() {
        val id = BookingApi.create(booking())
        val rq = PartialBooking(additionalNeeds = "Breakfast")


        val response = bookingApi.partialUpdate(id, rq, null)

        assertEquals(HttpStatus.SC_FORBIDDEN, response.statusCode)
    }

    @Test
    fun `Partial update with negative id`() {
        val id = BookingApi.create(booking())
        val rq = PartialBooking(additionalNeeds = "Breakfast")

        val response = bookingApi.partialUpdate(-id, rq)

        assertEquals(HttpStatus.SC_METHOD_NOT_ALLOWED, response.statusCode)
    }

    @Test
    fun `Partial update with non-existing id`() {
        val id = BookingApi.create(booking())
        bookingApi.delete(id.toString())
        val rq = PartialBooking(additionalNeeds = "Breakfast")

        val response = bookingApi.partialUpdate(id, rq)

        assertEquals(HttpStatus.SC_METHOD_NOT_ALLOWED, response.statusCode)
    }

    @Test
    fun `Partial update by String instead of id`() {
        val id = BookingApi.create(booking())
        val rq = hashMapOf<String, Any>("additionalNeeds" to "Breakfast")

        val response = bookingApi.partialUpdateMap("acc", rq)

        assertEquals(HttpStatus.SC_METHOD_NOT_ALLOWED, response.statusCode)
    }

}