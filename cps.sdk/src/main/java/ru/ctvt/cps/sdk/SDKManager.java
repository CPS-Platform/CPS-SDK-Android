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

package ru.ctvt.cps.sdk;

import android.content.Context;

import ru.ctvt.cps.sdk.dagger.AppComponent;
import ru.ctvt.cps.sdk.dagger.ContextModule;
import ru.ctvt.cps.sdk.dagger.DaggerAppComponent;
import ru.ctvt.cps.sdk.dagger.NetworkModule;

/**
 * Класс для инициализации SDK
 */
public class SDKManager {
    private static final String CPS_BASE_URL = "http://cps.fvds.ru/";
    //private static final String CPS_BASE_URL = "http://192.168.1.163:8080/"; тестовый сервер

    public interface PreferencesNameConsts {
        String FILE_NAME = "preferences.xml";
        String AUTH_TOKEN = "access_token";
        String LOGIN = "login";
        String PASSWORD = "password";
        String USER_ID = "user_id";
        String SERVICE_ID = "service_id";
        String ROLE = "role";
    }

    private static SDKManager instance = null;

    private AppComponent mAppComponent;

    public static SDKManager getInstance() {
        if (instance == null)
            instance = new SDKManager();
        return instance;
    }

    /**
     * Инициализация SDK
     * @param context контекст
     */
    public void init(Context context) {
        mAppComponent = DaggerAppComponent
                .builder()
                .contextModule(new ContextModule(context))
                .networkModule(new NetworkModule(CPS_BASE_URL))
                .build();
    }

    public AppComponent getAppComponent() {
        return mAppComponent;
    }
}
