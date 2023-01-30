package com.booking.test

import com.booking.api.BookingApi
import org.apache.http.HttpStatus
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test


class DeleteBookingTest : BookingApiTest() {

    @Test
    fun `Test delete by id`() {
        val id = BookingApi.create(booking())

        val response = bookingApi.delete(id.toString())

        assertEquals(HttpStatus.SC_CREATED, response.statusCode)
        assertNull(BookingApi.get(id))
    }

    @Test
    fun `Double delete by id`() {
        val id = BookingApi.create(booking())
        bookingApi.delete(id.toString())

        val response = bookingApi.delete(id.toString())

        assertEquals(HttpStatus.SC_METHOD_NOT_ALLOWED, response.statusCode)
    }

    @Test
    fun `Delete by String instead of id`() {
        BookingApi.create(booking())

        val response = bookingApi.delete("acc")

        assertEquals(HttpStatus.SC_METHOD_NOT_ALLOWED, response.statusCode)
    }

    @Test
    fun `Delete by negative id`() {
        val id = BookingApi.create(booking())

        val response = bookingApi.delete(("-$id").toString())

        assertEquals(HttpStatus.SC_METHOD_NOT_ALLOWED, response.statusCode)
    }
}