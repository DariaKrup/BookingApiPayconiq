package com.booking.api.vo

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDate

@JsonInclude(JsonInclude.Include.NON_NULL)
data class BookingFilter(
    @JsonProperty("firstname") val firstName: String? = null,
    @JsonProperty("lastname") val lastName: String? = null,
    @JsonProperty("checkin") val checkIn: LocalDate? = null,
    @JsonProperty("checkout") val checkOut: LocalDate? = null
)