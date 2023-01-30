package com.booking.api.vo

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDate

@JsonInclude(JsonInclude.Include.NON_NULL)
data class PartialBooking(
        @JsonProperty("firstname") val firstName: String? = null,
        @JsonProperty("lastname") val lastName: String? = null,
        @JsonProperty("totalprice") val totalPrice: Long? = null,
        @JsonProperty("depositpaid") val depositPaid: Boolean? = null,
        @JsonProperty("bookingdates") val bookingDates: PartialBookingDates? = null,
        @JsonProperty("additionalneeds") val additionalNeeds: String? = null
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class PartialBookingDates(
        @JsonProperty("checkin") val checkIn: LocalDate? = null,
        @JsonProperty("checkout") val checkOut: LocalDate? = null
)