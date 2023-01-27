package com.booking.test

import com.booking.api.BookingApi
import com.booking.api.GetBookingIdsApi
import org.apache.http.HttpStatus
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

open class GetIdsTest : BookingApiTest() {

    private val getBookingIdsApi : GetBookingIdsApi = GetBookingIdsApi(getToken())

    @Test
    fun `Get booking ids and check non-empty`() {
        val id = BookingApi.create(booking())
        val booking = BookingApi.get(id)
        print(booking)

        val response = getBookingIdsApi.getIds()

        assertEquals(HttpStatus.SC_OK, response.statusCode)
        assertFalse(response.body().jsonPath().getList<Any>("$").isEmpty())
    }
}