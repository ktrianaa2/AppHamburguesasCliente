package com.example.apphamburguesascliente.Api;

import com.example.apphamburguesascliente.Interfaces.ApiService;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static final String BASE_URL = "https://9jpn4ctd-8000.use2.devtunnels.ms/";
    private static ApiService apiService;
    private static final Object LOCK = new Object();

    public static ApiService getInstance() {
        if (apiService == null) {
            synchronized (LOCK) {
                if (apiService == null) {
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(BASE_URL)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                    apiService = retrofit.create(ApiService.class);
                }
            }
        }
        return apiService;
    }
}
