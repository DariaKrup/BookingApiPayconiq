package com.booking.test

import com.booking.api.BookingApi
import com.booking.api.PartialUpdateBookingApi
import com.booking.api.vo.PartialBooking
import org.apache.http.HttpStatus
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

open class PartialUpdateTest : BookingApiTest() {
    private val partialUpdateBookingApi : PartialUpdateBookingApi = PartialUpdateBookingApi(getToken())

    @Test
    fun `Partial update of lastname by id`() {
        val id = BookingApi.create(booking())
        val booking = BookingApi.get(id)
        print(booking)

        val lastName = "Brown"
        val partialBooking = PartialBooking(firstName = null, lastName = lastName,
            totalPrice = null, depositPaid = null, bookingDates = null, additionalNeeds = null)

        val response = partialUpdateBookingApi.partialUpdate(partialBooking.values, id)

        assertEquals(HttpStatus.SC_OK, response.statusCode)
        assertEquals(lastName, response.jsonPath().get("lastname"))
    }

}