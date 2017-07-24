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

package ru.ctvt.cps.sdk.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.WorkerThread;

import ru.ctvt.cps.sdk.SDKManager;
import ru.ctvt.cps.sdk.errorprocessing.BaseCpsException;
import ru.ctvt.cps.sdk.errorprocessing.CPSErrorParser;
import ru.ctvt.cps.sdk.network.BaseResponse;
import ru.ctvt.cps.sdk.network.DeviceAccessTokenResponse;
import ru.ctvt.cps.sdk.network.DeviceCodeResponse;

import java.io.IOException;

import retrofit2.Response;

import static ru.ctvt.cps.sdk.model.AccountControl.authToken;

/**
 * Класс для работы в режиме устройства
 */
public class RecorderDevice extends Device {

    private String deviceCode;
    private String deviceSecret;

    /**
     * Создать устройство

     * @param service_id идентификатор сервиса
     */
    RecorderDevice(String service_id) {
        super("current", service_id);

        SharedPreferences mSharedPreferences = context.getSharedPreferences(SDKManager.PreferencesNameConsts.FILE_NAME, Context.MODE_PRIVATE);
        if(mSharedPreferences != null)
            mSharedPreferences.edit().putString(SDKManager.PreferencesNameConsts.SERVICE_ID, service_id).apply();
    }

    public String getRegistrationCode(){
        return this.deviceCode;
    }

    /**
     * Удалить токена доступа
     */
    @WorkerThread
    public void deleteAccessToken() throws IOException, BaseCpsException {
        Response<BaseResponse<String>> response = api.deleteDeviceToken(this.getDeviceID()).execute();
        if (response.isSuccessful()) {
            SharedPreferences mSharedPreferences = context.getSharedPreferences(SDKManager.PreferencesNameConsts.FILE_NAME, Context.MODE_PRIVATE);
            mSharedPreferences.edit().putString(SDKManager.PreferencesNameConsts.AUTH_TOKEN, "").apply();
        } else
            CPSErrorParser.throwCpsException(response.errorBody(), response.code());
    }

    /**
     * Обновить токен устройства
     */
    @WorkerThread
    public void updateAccessToken() throws IOException, BaseCpsException {
        Response<BaseResponse<DeviceAccessTokenResponse>> response = api.updateDeviceToken(this.getDeviceID()).execute();
        if (response.isSuccessful()) {
            SharedPreferences mSharedPreferences = context.getSharedPreferences(SDKManager.PreferencesNameConsts.FILE_NAME, Context.MODE_PRIVATE);
            mSharedPreferences.edit().putString(SDKManager.PreferencesNameConsts.AUTH_TOKEN, response.body().data.token.header).apply();
        } else
            CPSErrorParser.throwCpsException(response.errorBody(), response.code());
    }

    /**
     * Получить код регистрации
     */
    @WorkerThread
    public void fetchRegistrationCode() throws IOException, BaseCpsException {
        Response<BaseResponse<DeviceCodeResponse>> response = api.generateCode(this.getServiceID()).execute();
        if (response.isSuccessful()) {
            this.deviceCode = response.body().data.code;
            this.deviceSecret = response.body().data.secret;
        } else
            CPSErrorParser.throwCpsException(response.errorBody(), response.code());
    }

    /**
     * Обменять код регистрации на токен доступа.
     * Если устройство, которому выдан код регистрации, привязано к сущности, тогда ему выдаётся токен доступа для записи данных.
     */
    @WorkerThread
    public void fetchAccessToken() throws IOException, BaseCpsException {
        Response<BaseResponse<DeviceAccessTokenResponse>> response = api.recieveTokenForDevice(this.getServiceID(), this.deviceCode, this.deviceSecret).execute();
        if (response.isSuccessful()) {
            SharedPreferences mSharedPreferences = context.getSharedPreferences(SDKManager.PreferencesNameConsts.FILE_NAME, Context.MODE_PRIVATE);
            authToken = response.body().data.token.header;
            mSharedPreferences.edit().putString(SDKManager.PreferencesNameConsts.AUTH_TOKEN, authToken).apply();
            mSharedPreferences.edit().putString(SDKManager.PreferencesNameConsts.ROLE, "device").apply();
        } else
            CPSErrorParser.throwCpsException(response.errorBody(), response.code());
    }

    /**
     * Получить публичное хранилище хозяина
     * @return экземпляр публичного хранилища пользователя
     * @throws IOException
     * @throws BaseCpsException
     */
    public KeyValueStorage getUserPublicKVStorage() throws IOException, BaseCpsException {
        return new KeyValueStorage(KeyValueStorage.ContainerTypeEnum.users, "current", KeyValueStorage.VisibilityEnum.global);
    }
}
