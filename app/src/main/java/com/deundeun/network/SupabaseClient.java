package com.deundeun.network;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import java.io.IOException;

public class SupabaseClient {
    private static final String BASE_URL = "https://wwklcdmksmdgwofubrdk.supabase.co/";
    private static final String ANON_KEY = "sb_publishable_TLysd1E477vo3RCp1TwZdw_yOTCduKn";
    
    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit == null) {
            OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request original = chain.request();
                        Request.Builder requestBuilder = original.newBuilder()
                            .header("apikey", ANON_KEY)
                            .header("Authorization", "Bearer " + ANON_KEY);
                        Request request = requestBuilder.build();
                        return chain.proceed(request);
                    }
                })
                .build();

            retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient)
                .build();
        }
        return retrofit;
    }
}
