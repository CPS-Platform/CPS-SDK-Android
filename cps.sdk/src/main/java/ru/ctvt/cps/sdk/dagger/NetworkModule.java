/*
 * Copyright (c) Connectivity,  2017.
 *  This program is a free software: you can redistribute it and/or modify
 *   it under the terms of the Apache License, Version 2.0 (the "License");
 *
 *   You may obtain a copy of the Apache 2 License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   Apache 2 License for more details.
 */

package ru.ctvt.cps.sdk.dagger;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import ru.ctvt.cps.sdk.SDKManager;
import ru.ctvt.cps.sdk.model.AccountControl;
import ru.ctvt.cps.sdk.network.Api;
import ru.ctvt.cps.sdk.network.ApiAuthenticator;
import com.google.common.base.Strings;

import java.io.IOException;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;


@Module
public class NetworkModule {

    private String baseURL;

    public NetworkModule(@NonNull String baseURL) {
        this.baseURL = baseURL;
    }

    @Provides
    @Singleton
    Api getApi(Retrofit retrofit) {
        return retrofit.create(Api.class);
    }

    @Provides
    @Singleton
    Retrofit getRetrofit(OkHttpClient client) {
        return new Retrofit.Builder()
                .baseUrl(baseURL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
    }

    @Provides
    @NonNull
    @Singleton
    OkHttpClient okHttpClient(ApiAuthenticator authenticator, final Context context) {

        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        clientBuilder.addInterceptor(logging);

        clientBuilder.addInterceptor(new Interceptor(){

            @Override public Response intercept(Chain chain) throws IOException {

            Request request = chain.request();
            HttpUrl url = request.url().newBuilder().build();
            Request.Builder requestBuilder = request.newBuilder().url(url);

            SharedPreferences mSharedPreferences = context.getSharedPreferences(SDKManager.PreferencesNameConsts.FILE_NAME, Context.MODE_PRIVATE);
            String userToken = "";
            if (mSharedPreferences != null)
                userToken = mSharedPreferences.getString(SDKManager.PreferencesNameConsts.AUTH_TOKEN, "");
            else
                userToken = AccountControl.getInstance().getAuthToken();

            if (!Strings.isNullOrEmpty(userToken)) {
                requestBuilder.addHeader("Authorization", userToken);
                requestBuilder.addHeader("Content-Type", "application/json");
            }

            Response response = null;
                response = chain.proceed(requestBuilder.build());

            return response;
        }});
        clientBuilder.authenticator(authenticator);
        return clientBuilder.build();
    }

    @Provides
    @NonNull
    @Singleton
    ApiAuthenticator authenticator() {
        return new ApiAuthenticator();
    }
}
