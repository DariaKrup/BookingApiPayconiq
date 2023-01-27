package com.booking.api.vo


class PartialBooking (firstName: String?, lastName: String?, totalPrice: Long?, depositPaid: Boolean?, bookingDates: BookingDates?, additionalNeeds: String?) {
    public var values: HashMap<String, Any> = HashMap()

    init {
        if (firstName != null)
            values["firstname"] = firstName
        if (lastName != null)
            values["lastname"] = lastName
        if (totalPrice != null)
            values["totalprice"] = totalPrice
        if (depositPaid != null)
            values["depositpaid"] = depositPaid
        if (bookingDates != null)
            values["bookingdates"] = bookingDates
        if (additionalNeeds != null)
            values["additionalneeds"] = additionalNeeds
    }
}