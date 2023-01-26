package com.bookingApi.main;

import retrofit2.Retrofit;
import com.bookingApi.utility.utils.RequestExecutor;
import com.bookingApi.main.BookingApiRestService;
import retrofit2.converter.gson.GsonConverterFactory;


public class App 
{
    private Retrofit retrofit;
    private RequestExecutor requestExecutor;
    private BookingApiRestService service;
    public static void main( String[] args ) {

    }

    protected void launchUrl() {
        retrofit = new Retrofit.Builder()
                .baseUrl(requestExecutor.getBASE_URL())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(BookingApiRestService.class);
    }
}
