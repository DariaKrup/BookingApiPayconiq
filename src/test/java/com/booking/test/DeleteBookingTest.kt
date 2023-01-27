package com.booking.test

import com.booking.api.BookingApi
import com.booking.api.DeleteBookingApi
import org.apache.http.HttpStatus
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test


open class DeleteBookingTest : BookingApiTest() {

    private val deleteBookingApi: DeleteBookingApi = DeleteBookingApi(getToken())

    @Test
    fun `Test delete by id`() {
        val id = BookingApi.create(booking())
        val booking = BookingApi.get(id)
        print(booking)

        val response = deleteBookingApi.delete(id)
        assertEquals(HttpStatus.SC_CREATED, response.statusCode)
    }
}