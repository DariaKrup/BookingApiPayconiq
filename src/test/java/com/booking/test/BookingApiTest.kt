package com.booking.test

import com.booking.api.AuthApi
import com.booking.api.BookingApi
import com.booking.api.vo.Booking
import com.booking.api.vo.BookingDates
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance
import java.time.LocalDate

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
open class BookingApiTest {

    protected val token: String? = AuthApi.createToken()
    protected lateinit var bookingApi: BookingApi

    @BeforeAll
    fun setUp() {
        bookingApi = BookingApi(token)
    }

    @JvmName("getAuthToken")
    fun getToken(): String? {
        return token
    }

    protected fun booking(
        firstName: String = "John",
        lastName: String = "BB",
        totalPrice: Long = 232,
        bookingDates: BookingDates = BookingDates(
            LocalDate.of(2011, 12, 30),
            LocalDate.of(2011, 12, 31)
        ),
        depositPaid: Boolean = false,
        additionalNeeds: String? = null
    ) = Booking(firstName, lastName, totalPrice, depositPaid, bookingDates, additionalNeeds)
}