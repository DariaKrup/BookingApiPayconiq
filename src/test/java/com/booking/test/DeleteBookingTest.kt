package com.booking.test

import com.booking.api.BookingApi
import org.apache.http.HttpStatus
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@DisplayName("Delete booking")
class DeleteBookingTest : BookingApiTest() {

    @Test
    fun `By id`() {
        val id = BookingApi.create(booking())

        val response = bookingApi.delete(id)

        assertEquals(HttpStatus.SC_CREATED, response.statusCode)
        assertNull(BookingApi.get(id))
    }

    @Test
    fun `Double delete by id`() {
        val id = BookingApi.create(booking())
        bookingApi.delete(id.toString())

        val response = bookingApi.delete(id)

        assertEquals(HttpStatus.SC_METHOD_NOT_ALLOWED, response.statusCode)
    }

    @Nested
    @DisplayName("Incorrect format")
    inner class IncorrectFormat {
        @Test
        fun `String instead of id`() {
            val response = bookingApi.delete("acc")

            assertEquals(HttpStatus.SC_METHOD_NOT_ALLOWED, response.statusCode)
        }

        @Test
        fun `Negative id`() {
            val id = BookingApi.create(booking())

            val response = bookingApi.delete("-$id")

            assertEquals(HttpStatus.SC_METHOD_NOT_ALLOWED, response.statusCode)
        }
    }
}