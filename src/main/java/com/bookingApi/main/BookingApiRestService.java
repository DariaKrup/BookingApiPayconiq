package com.bookingApi.main;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import com.bookingApi.responses.CreateTokenResponse;

public interface BookingApiRestService {
    @POST("/auth")
    @Headers({
            "Content-Type: application/json"
    })
    Call<CreateTokenResponse> createToken(@Field("username") String username, @Field("password") String password);
}
