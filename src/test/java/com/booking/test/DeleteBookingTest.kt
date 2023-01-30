package com.booking.test

import com.booking.api.BookingApi
import org.apache.http.HttpStatus
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@DisplayName("DeleteBooking")
class DeleteBookingTest : BookingApiTest() {

    @Test
    fun `Should delete`() {
        val id = BookingApi.create(booking())

        val response = bookingApi.delete(id)

        assertEquals(HttpStatus.SC_CREATED, response.statusCode)
        assertNull(BookingApi.get(id))
    }

    @Test
    fun `Should be negative when non-existing booking is removed`() {
        val id = BookingApi.create(booking())
        bookingApi.delete(id)

        val response = bookingApi.delete(id)

        assertEquals(HttpStatus.SC_METHOD_NOT_ALLOWED, response.statusCode)
    }

    @Nested
    @DisplayName("Incorrect format")
    inner class IncorrectFormat {
        @Test
        fun `Should not be allowed on random string instead of id`() {
            val response = bookingApi.delete("acc")

            assertEquals(HttpStatus.SC_METHOD_NOT_ALLOWED, response.statusCode)
        }

        @Test
        fun `Should not be allowed when id is negative`() {
            val id = BookingApi.create(booking())

            val response = bookingApi.delete("-$id")

            assertEquals(HttpStatus.SC_METHOD_NOT_ALLOWED, response.statusCode)
        }
    }
}