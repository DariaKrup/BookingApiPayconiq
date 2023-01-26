package com.booking.api.vo

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDate

@JsonInclude(JsonInclude.Include.NON_NULL)
data class Booking(
        @JsonProperty("firstname") val firstName: String,
        @JsonProperty("lastname") val lastName: String,
        @JsonProperty("totalprice") val totalPrice: Long,
        @JsonProperty("depositpaid") val depositPaid: Boolean,
        @JsonProperty("bookingdates") val bookingDates: BookingDates,
        @JsonProperty("additionalneeds") val additionalNeeds: String? = null
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class BookingDates(
        @JsonProperty("checkin") val checkIn: LocalDate,
        @JsonProperty("checkout") val checkOut: LocalDate
)