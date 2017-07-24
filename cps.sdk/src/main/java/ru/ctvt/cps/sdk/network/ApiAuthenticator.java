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

package ru.ctvt.cps.sdk.network;

import android.content.Context;

import java.io.IOException;

import okhttp3.Authenticator;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;

public class ApiAuthenticator implements Authenticator {

    private Api mApi;
    private Context mContext;

    public void setApi(Api api, Context context) {
        this.mApi = api;
        this.mContext = context;
    }

    @Override
    public Request authenticate(Route route, Response response) throws IOException {
        if (response.code() == 401) {
            //Toast.makeText(mContext, "Действие токена истекло", Toast.LENGTH_SHORT);
        }
        return null;
    }
}
