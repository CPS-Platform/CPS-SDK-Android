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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.WorkerThread;
import android.util.Base64;

import ru.ctvt.cps.sdk.SDKManager;
import ru.ctvt.cps.sdk.errorprocessing.BaseCpsException;
import ru.ctvt.cps.sdk.errorprocessing.CPSErrorParser;
import ru.ctvt.cps.sdk.network.Api;
import ru.ctvt.cps.sdk.network.BaseResponse;
import ru.ctvt.cps.sdk.network.DeviceResponse;
import ru.ctvt.cps.sdk.network.UserResponse;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import okhttp3.ResponseBody;
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
    private String firstName;
    private String lastName;
    private Bitmap avatar;

    /**
     * Создание объекта
     *
     * @param id    идентификатор пользователя
     * @param email электронная почта пользователя
     */
    User(String id, String email) {
        this.id = id;
        this.email = email;
        this.firstName = null;
        this.lastName = null;
        SDKManager.getInstance().getAppComponent().inject(this);
    }

    User(String id, String email, String firstName, String lastName) {
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
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
        this.firstName = mPrefs.getString(SDKManager.PreferencesNameConsts.FIRST_NAME, "");
        this.lastName = mPrefs.getString(SDKManager.PreferencesNameConsts.LAST_NAME, "");
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


    public String getName() {
        return firstName + " " + lastName;
    }

    public Bitmap getAvatar() {
        return this.avatar;
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
                devices.add(new UserDevice(r.id, r.ownerServiceId, r.gatewayId, r.createdAt,r.name, this));
            return devices;
        } else
            CPSErrorParser.throwCpsException(response.errorBody(), response.code());
        return null;

    }

    @WorkerThread
    public void fetchUser() throws IOException {
        Response<BaseResponse<UserResponse>> response = api.getUserInfo().execute();
        if (response.isSuccessful()) {
            this.firstName = response.body().data.first_name;
            this.lastName = response.body().data.last_name;
            SharedPreferences mPrefs = context.getSharedPreferences(SDKManager.PreferencesNameConsts.FILE_NAME, Context.MODE_PRIVATE);
            if (mPrefs != null){
            mPrefs.edit().putString(SDKManager.PreferencesNameConsts.FIRST_NAME, response.body().data.first_name).apply();
            mPrefs.edit().putString(SDKManager.PreferencesNameConsts.LAST_NAME, response.body().data.last_name).apply();
        }}
    }


    @WorkerThread
    public void editName(String newFirstName, String newLastName) throws IOException {
        Response<BaseResponse<UserResponse>> response = api.putUserInfo(newFirstName, newLastName).execute();
        if (response.isSuccessful()) {
            this.firstName = newFirstName;
            this.lastName = newLastName;
            SharedPreferences mPrefs = context.getSharedPreferences(SDKManager.PreferencesNameConsts.FILE_NAME, Context.MODE_PRIVATE);
            mPrefs.edit().putString(SDKManager.PreferencesNameConsts.FIRST_NAME, newFirstName).apply();
            mPrefs.edit().putString(SDKManager.PreferencesNameConsts.LAST_NAME, newLastName).apply();

        }
    }

    /**
     * Добавить устройство с привязкой
     *
     * @param device_code код, выданный устройству. Если null - устройство будет добавлено без привязки
     * @return Добавленное устройство
     */
    @WorkerThread
    public UserDevice addDevice(String device_code, @NonNull String deviceName) throws IOException, BaseCpsException {
        Response<BaseResponse<DeviceResponse>> response = api.addNewDevice(this.id, device_code, deviceName.isEmpty()? "device" : deviceName).execute();
        if (response.isSuccessful())
            return new UserDevice(response.body().data.id, response.body().data.ownerServiceId,
                    response.body().data.gatewayId, response.body().data.createdAt,response.body().data.name, this);
        else
            CPSErrorParser.throwCpsException(response.errorBody(), response.code());
        return null;
    }

    /**
     * Добавить устройство без привязки
     *
     * @return
     * @throws IOException
     * @throws BaseCpsException
     */
    @WorkerThread
    public UserDevice addDevice(@NonNull String deviceName) throws IOException, BaseCpsException {
        Response<BaseResponse<DeviceResponse>> response = api.addNewDevice(this.id, null, deviceName.isEmpty()? "device": deviceName).execute();
        if (response.isSuccessful())
            return new UserDevice(response.body().data.id, response.body().data.ownerServiceId,
                    response.body().data.gatewayId, response.body().data.createdAt,response.body().data.name, this);
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

    /**
     * Получить аватар пользователя
     */
    @WorkerThread
    public void fetchAvatar() throws BaseCpsException, IOException {
        Response<ResponseBody> response = api.getUserAvatar().execute();
        if (!response.isSuccessful())
            CPSErrorParser.throwCpsException(response.errorBody(), response.code());
        else if (response.body() != null) {
            Bitmap bmpAvatar = BitmapFactory.decodeStream(response.body().byteStream());
            this.avatar = bmpAvatar;
        } else {
            this.avatar = null;
        }
    }

    /**
     * Загрузка аватара
     *
     * @param bmpAvatar изображение
     */
    @WorkerThread
    public void setAvatar(Bitmap bmpAvatar) throws IOException, BaseCpsException {
        String base64 = convertToBase64(bmpAvatar);

        Response<BaseResponse> response = api.setUserAvatar(base64).execute();
        if (response.isSuccessful()) {
            this.avatar = bmpAvatar;
        } else
            CPSErrorParser.throwCpsException(response.errorBody(), response.code());
    }

    /**
     * Загрузка аватара
     *
     * @param bmpPath путь до изображения в файловой системе
     */
    @WorkerThread
    public void setAvatar(Uri bmpPath, Context context) throws IOException, BaseCpsException {
        //File imgFile = new File(bmpPath);
        //if (imgFile.exists()) {
            //Bitmap bmpAvatar = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(bmpPath));

            Bitmap bmpAvatar = decodeFile(bmpPath, context);
            String base64 = convertToBase64(bmpAvatar);

            Response<BaseResponse> response = api.setUserAvatar(base64).execute();
            if (response.isSuccessful()) {
                this.avatar = bmpAvatar;
            } else
                CPSErrorParser.throwCpsException(response.errorBody(), response.code());
        //} else throw new FileNotFoundException();
    }

    /**
     * конвертация изображения в строку base64
     *
     * @param bitmap
     * @return
     */
    private static String convertToBase64(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        return Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT);
    }

    /**
     * Сжатие файла до приемлимого размера аватара
     *
     * @param bmpPath путь до изображения в файловой системе
     * @param context контекст
     */
    private Bitmap decodeFile(Uri bmpPath, Context context) throws FileNotFoundException {
        try {
            // Декодирование исходного файла
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(context.getContentResolver().openInputStream(bmpPath), null, o);

            // Установка макс поддерживаемого размера
            final int REQUIRED_SIZE = 200;

            // поиск корректного параметра масштабирования
            int scale = 1;
            while (o.outWidth / scale >= REQUIRED_SIZE &&
                    o.outHeight / scale >= REQUIRED_SIZE) {
                scale *= 2;
            }

            // Декодирование с текущим параметром масштабирования
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            o2.inJustDecodeBounds = false;
            return BitmapFactory.decodeStream(context.getContentResolver().openInputStream(bmpPath), null, o2);
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException();
        }
    }
}
