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
        val body = response.jsonPath()

        assertEquals(HttpStatus.SC_OK, response.statusCode)
        assertEquals("Brown", body.get("lastname"))
        assertEquals("Joey", body.get("firstname"))
        assertEquals(252, body.getLong("totalprice"))
        assertEquals(true, body.getBoolean("depositpaid"))
        assertEquals("2011-12-30", body.get("bookingdates.checkin"))
        assertEquals("2011-12-31", body.get("bookingdates.checkout"))
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
        val body = response.jsonPath()

        assertEquals(HttpStatus.SC_OK, response.statusCode)
        assertEquals("Bb", body.get("lastname"))
        assertEquals("Joey", body.get("firstname"))
        assertEquals(252, body.getLong("totalprice"))
        assertEquals(true, body.getBoolean("depositpaid"))
        assertEquals("2011-12-30", body.get("bookingdates.checkin"))
        assertEquals("2011-12-31", body.get("bookingdates.checkout"))
        assertEquals("Breakfast", body.get("additionalneeds"))
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
        val body = response.jsonPath()

        assertEquals(HttpStatus.SC_OK, response.statusCode)
        assertEquals("Brown", body.get("lastname"))
        assertEquals("Jo", body.get("firstname"))
        assertEquals(300, body.getLong("totalprice"))
        assertEquals(true, body.getBoolean("depositpaid"))
        assertEquals("2012-01-30", body.get("bookingdates.checkin"))
        assertEquals("2012-01-31", body.get("bookingdates.checkout"))
        assertEquals("Breakfast", body.get("additionalneeds"))
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
        val body = response.jsonPath()

        assertEquals(HttpStatus.SC_OK, response.statusCode)
        assertEquals("Bb", body.get("lastname"))
        assertEquals("Joey", body.get("firstname"))
        assertEquals(252, body.getLong("totalprice"))
        assertEquals(true, body.getBoolean("depositpaid"))
        assertEquals("2012-01-30", body.get("bookingdates.checkin"))
        assertEquals("2012-01-15", body.get("bookingdates.checkout"))
    }

    @Test
    fun `Partial update with non-existing field`() {
        val id = BookingApi.create(booking())
        val rq = hashMapOf("depositamount" to 200)

        val response = bookingApi.partialUpdatePlain(id, rq)

        assertEquals(HttpStatus.SC_OK, response.statusCode)
        assertNull(response.jsonPath().get("depositamount"))
    }

    @Test
    fun `Partial update with Double in amount of totalPrice`() {
        val id = BookingApi.create(booking(totalPrice = 250))
        val rq = hashMapOf("totalprice" to 200.50)

        val response = bookingApi.partialUpdatePlain(id, rq)

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
        val rq = hashMapOf<String, Any>("depositpaid" to "true")

        val response = bookingApi.partialUpdatePlain(id, rq)

        assertEquals(HttpStatus.SC_OK, response.statusCode)
        assertEquals(true, response.jsonPath().get("depositpaid"))
    }

    @Test
    fun `Partial update with incorrect month and day values`() {
        val id = BookingApi.create(booking())
        val rqInternal = hashMapOf("checkin" to "2012-33-93", "checkout" to "2012-44-83")
        val rq = hashMapOf("bookingdates" to rqInternal)

        val response = bookingApi.partialUpdatePlain(id, rq)

        assertEquals(HttpStatus.SC_OK, response.statusCode)
        assertEquals("0NaN-aN-aN", response.jsonPath().get("bookingdates.checkin"))
        assertEquals("0NaN-aN-aN", response.jsonPath().get("bookingdates.checkout"))
    }

    @Test
    fun `Partial update with duplicate field`() {
        val id = BookingApi.create(booking())
        val rq = hashMapOf("lastname" to "Brown", "lastname" to "Tribiani")

        val response = bookingApi.partialUpdatePlain(id, rq)

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

        val response = bookingApi.partialUpdate("-$id", rq)

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
        val rq = hashMapOf("additionalNeeds" to "Breakfast")

        val response = bookingApi.partialUpdatePlain("acc", rq)

        assertEquals(HttpStatus.SC_METHOD_NOT_ALLOWED, response.statusCode)
    }

}