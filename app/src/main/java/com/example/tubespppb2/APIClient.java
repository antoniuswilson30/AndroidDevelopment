package com.example.tubespppb2;

import okhttp3.Dispatcher;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClient {
    private static Retrofit retrofit = null;

    public static Retrofit getClient(){
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.NONE);
        Dispatcher dispatcher = new Dispatcher();
        dispatcher.setMaxRequests(1);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).dispatcher(dispatcher).build();


        if(retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl("https://ifportal.labftis.net/api/v1/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        }
        return  retrofit;
    }
}
