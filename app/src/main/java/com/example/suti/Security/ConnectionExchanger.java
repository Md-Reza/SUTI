package com.example.suti.Security;

import java.util.Objects;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ConnectionExchanger {
    public static Retrofit retrofit = null;

    private static OkHttpClient getOkHttpClient() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.NONE);
        OkHttpClient okClient = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();
        return okClient;
    }

    public static Retrofit getRetrofit() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    //.baseUrl("http://192.168.65.43:500/")
                    .baseUrl(Objects.requireNonNull(GetConnection()))
                    .client(getOkHttpClient())
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static String GetConnection() {
        try {
            String prodServer = SharedPrefServer.read("serverURL", "");
            if (prodServer != null) {
                return prodServer;
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return null;
    }
}
