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
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;

import ru.ctvt.cps.sdk.SDKManager;
import ru.ctvt.cps.sdk.errorprocessing.BaseCpsException;
import ru.ctvt.cps.sdk.errorprocessing.CPSErrorParser;
import ru.ctvt.cps.sdk.network.Api;
import ru.ctvt.cps.sdk.network.BaseResponse;
import ru.ctvt.cps.sdk.network.DeviceResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Response;

/**
 * Класс для работы с пользователем
 */
public class User {
    @Inject
    Api api;

    @Inject
    Context context;

    private String id;
    private String email;

    /**
     * Создание объекта
     *
     * @param id    идентификатор пользователя
     * @param email электронная почта пользователя
     */
    User(String id, String email) {
        this.id = id;
        this.email = email;
        SDKManager.getInstance().getAppComponent().inject(this);
    }

    /**
     * Конструктор для восстановления авторизованного пользователя
     */
    User() {
        SDKManager.getInstance().getAppComponent().inject(this);
        SharedPreferences mPrefs = context.getSharedPreferences(SDKManager.PreferencesNameConsts.FILE_NAME, Context.MODE_PRIVATE);
        this.id = mPrefs.getString(SDKManager.PreferencesNameConsts.USER_ID, "");
        this.email = mPrefs.getString(SDKManager.PreferencesNameConsts.LOGIN, "");
    }


    /**
     * Получить идентификатор пользователя
     *
     * @return Идентификатор пользователя
     */
    public String getId() {
        return id;
    }

    /**
     * Получить адрес электронной почты пользователя
     *
     * @return Электронная почта пользователя
     */
    public String getEmail() {
        return email;
    }


    /**
     * Получить список устройств, зарегистрированных у данного пользователя
     *
     * @return Список типа устройств
     */
    @WorkerThread
    public List<UserDevice> fetchDevices() throws IOException, BaseCpsException {
        List<UserDevice> devices = new ArrayList<>();
        Response<BaseResponse<List<DeviceResponse>>> response = api.getDevices().execute();
        if (response.isSuccessful()) {
            for (DeviceResponse r : response.body().data)
                devices.add(new UserDevice(r.id, r.ownerServiceId, r.gatewayId, r.createdAt, this));
            return devices;
        } else
            CPSErrorParser.throwCpsException(response.errorBody(), response.code());
        return null;

    }

    /**
     * Добавить устройство с привязкой
     *
     * @param device_code код, выданный устройству. Если null - устройство будет добавлено без привязки
     * @return Добавленное устройство
     */
    @WorkerThread
    public UserDevice addDevice(@Nullable String device_code) throws IOException, BaseCpsException {
        Response<BaseResponse<DeviceResponse>> response = api.addNewDevice(this.id, device_code).execute();
        if (response.isSuccessful())
            return new UserDevice(response.body().data.id, response.body().data.ownerServiceId,
                    response.body().data.gatewayId, response.body().data.createdAt, this);
        else
            CPSErrorParser.throwCpsException(response.errorBody(), response.code());
        return null;
    }


    /**
     * Получить локальное (приватное) хранилище пользователя
     *
     * @return Локальное (приватное) хранилище пользователя
     */
    public KeyValueStorage getLocalKVStorage() {
        return new KeyValueStorage(KeyValueStorage.ContainerTypeEnum.users, id, KeyValueStorage.VisibilityEnum.local);
    }

    /**
     * Получить глобальное (приватное) хранилище пользователя
     *
     * @return глобальное (приватное) хранилище пользователя
     */
    public KeyValueStorage getPublicKVStorage() {
        return new KeyValueStorage(KeyValueStorage.ContainerTypeEnum.users, id, KeyValueStorage.VisibilityEnum.global);
    }
}
