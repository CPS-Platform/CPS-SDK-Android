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
import android.support.annotation.WorkerThread;

import ru.ctvt.cps.sdk.SDKManager;
import ru.ctvt.cps.sdk.errorprocessing.BaseCpsException;
import ru.ctvt.cps.sdk.errorprocessing.CPSErrorParser;
import ru.ctvt.cps.sdk.errorprocessing.authentication.WrongRoleException;
import ru.ctvt.cps.sdk.network.Api;
import ru.ctvt.cps.sdk.network.AuthResponse;
import ru.ctvt.cps.sdk.network.BaseResponse;
import ru.ctvt.cps.sdk.network.SystemResponse;
import com.google.common.base.Strings;

import java.io.IOException;

import javax.inject.Inject;

import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * Управление аутентификацией аккаунта
 */
public class AccountControl {

    @Inject
    Api api;

    @Inject
    Context context;

    private static AccountControl instance;

    static String authToken;

    public String getAuthToken(){
        return authToken;
    }

    private Role role = Role.user;

    /**
     * Роль
     */
    public enum Role {

        /**
         * Роль "пользователь"
         */
        user("user"),

        /**
         * Роль "устройство"
         */
        device("device");

        private final String name;

        Role(String s) {
            name = s;
        }

        /*public boolean equalsName(String otherName) {
            return name.equals(otherName);
        }

        public String toString() {
            return this.name;
        }*/
    }

    private AccountControl() {
        if(SDKManager.PreferencesNameConsts.ROLE.equals("device"))
            this.role = Role.device;
        SDKManager.getInstance().getAppComponent().inject(this);
    }

    /**
     * Получить экземпляр класса (паттерн Singleton)
     *
     * @return
     */
    public static AccountControl getInstance() {
        if (instance == null)
            instance = new AccountControl();
        return instance;
    }

    /**
     * Установка роли
     *
     * @param role роль
     */
    public AccountControl withRole(Role role){
        this.role = role;
        return instance;
    }

    /**
     * Получить установленную для аккаунта роль
     * @return роль
     */
    public Role getRole(){
        return role;
    }

    /**
     * Авторизоваться с использованием пары логин-пароль <br>
     * Параметры сессии записываются в SharedPreferences
     *
     * @param login     логин
     * @param password  пароль
     * @param serviceId идентификатор сервиса
     * @return Возвращает объект класса User
     */
    @WorkerThread
    public User login(final String login, final String password, final String serviceId) throws BaseCpsException, IOException {
        if(getRole() == Role.user){
        Response<BaseResponse<AuthResponse>> response = api.login(login, password, true, serviceId).execute();
        User user = null;
        if (response.isSuccessful()) {
            String userToken = response.body().data.header;
            authToken = userToken;


            SharedPreferences mSharedPreferences = context.getSharedPreferences(SDKManager.PreferencesNameConsts.FILE_NAME, Context.MODE_PRIVATE);
            if (mSharedPreferences != null) {
                mSharedPreferences.edit().putString(SDKManager.PreferencesNameConsts.AUTH_TOKEN, authToken).apply();
                mSharedPreferences.edit().putString(SDKManager.PreferencesNameConsts.LOGIN, login).apply();
                mSharedPreferences.edit().putString(SDKManager.PreferencesNameConsts.PASSWORD, password).apply();
                mSharedPreferences.edit().putString(SDKManager.PreferencesNameConsts.USER_ID, response.body().data.ownerEntityId).apply();
                mSharedPreferences.edit().putString(SDKManager.PreferencesNameConsts.SERVICE_ID, serviceId).apply();
                mSharedPreferences.edit().putString(SDKManager.PreferencesNameConsts.ROLE, "user").apply();
            }
            user = new User(response.body().data.ownerEntityId, login);
            user.fetchUser();
        } else
            CPSErrorParser.throwCpsException(response.errorBody(), response.code());
        return user;}
        else
            throw new WrongRoleException("expected: "+Role.user.name+", actual: "+getRole().name);
    }

    /**
     * Завершить сессию
     */
    @WorkerThread
    public void logout() throws BaseCpsException, IOException {
        if(getRole() == Role.user)
            if (isAuthorized()) {
                Response<BaseResponse<String>> response = api.logout().execute();
                if (response.isSuccessful())
                    try {
                        SharedPreferences mSharedPreferences = context.getSharedPreferences(SDKManager.PreferencesNameConsts.FILE_NAME, Context.MODE_PRIVATE);
                        mSharedPreferences.edit().putString(SDKManager.PreferencesNameConsts.AUTH_TOKEN, "").apply();
                    } catch (NullPointerException e) {
                        //NullPointerException словится под юнит тестами.
                    }
                else
                    CPSErrorParser.throwCpsException(response.errorBody(), response.code());
            } else if (authToken != null)
                authToken = null;
        else
            throw new WrongRoleException("expected: "+Role.user.name+", actual: "+getRole().name);
    }

    /**
     * Запросить регистрацию аккаунта (по паре логин-пароль)
     *
     * @param login     логин
     * @param password  пароль
     * @param serviceId индентификатор сервиса
     */
    @WorkerThread
    public void register(final String login, final String password, final String serviceId) throws BaseCpsException, IOException {
        if(getRole() == Role.user) {
            Response<BaseResponse<AuthResponse>> response = api.register(login, password, serviceId).execute();
            if (!response.isSuccessful())
                CPSErrorParser.throwCpsException(response.errorBody(), response.code());
        } else
            throw new WrongRoleException("expected: "+Role.user.name+", actual: "+getRole().name);
    }

    /**
     * Сбросить пароль аккаунта
     *
     * @param email     логин
     * @param serviceId идентификатор сервиса
     */
    @WorkerThread
    public void recoverPassword(final String email, final String serviceId) throws BaseCpsException, IOException {
        if(getRole() == Role.user) {
            Response<BaseResponse<AuthResponse>> response = api.recoverPassword(email, serviceId).execute();
            if (!response.isSuccessful())
                CPSErrorParser.throwCpsException(response.errorBody(), response.code());
        } else
            throw new WrongRoleException("expected: "+Role.user.name+", actual: "+getRole().name);
    }

    /**
     * Выполнить проверку наличия незаверенной авторизационной сессии
     *
     * @return True, если авторизационная сессия не завершена
     */
    public boolean isAuthorized() {
        try {
            SharedPreferences mSharedPreferences = context.getSharedPreferences(SDKManager.PreferencesNameConsts.FILE_NAME, Context.MODE_PRIVATE);
            String userToken = mSharedPreferences.getString(SDKManager.PreferencesNameConsts.AUTH_TOKEN, "");
            return !Strings.isNullOrEmpty(userToken);
        } catch (NullPointerException e){ return true;}
    }

    /**
     * Восстановить пользователя при наличи незавшеренной сессии авторизации
     *
     * @return пользователь
     */

    public User restoreUser() throws BaseCpsException {
        if(getRole() == Role.user)
            if (isAuthorized())
                return new User();
        else
            throw new WrongRoleException("expected: "+Role.user.name+", actual: "+getRole().name);
        return null;
    }

    /**
     * Восстановить объект, представляющий реальное устройство при наличии незавершенной сессии авторизации
     * @return
     */
    public RecorderDevice restoreDevice() throws BaseCpsException {
        if(getRole() == Role.device) {
            SharedPreferences mSharedPreferences = context.getSharedPreferences(SDKManager.PreferencesNameConsts.FILE_NAME, Context.MODE_PRIVATE);
            if (isAuthorized() && mSharedPreferences.getString(SDKManager.PreferencesNameConsts.ROLE, "user").equals("device"))
                return new RecorderDevice(mSharedPreferences.getString(SDKManager.PreferencesNameConsts.SERVICE_ID, ""), "recorder");
            else
                return null;
        } else
            throw new WrongRoleException("expected: "+Role.device.name+", actual: "+getRole().name);
    }

    /**
     * Начать работу в режиме устройства
     */
    public RecorderDevice instantiateDeviceRecorder(final String serviceId) throws BaseCpsException {
        if (getRole() == Role.device)
            return new RecorderDevice(serviceId, "recorder");
        else
            throw new WrongRoleException("expected: "+Role.device.name+", actual: "+getRole().name);
    }

    /**
     * Получить лого сервиса в формате картинки Bitmap
     *
     * @param serviceId идентификатор сервиса
     */
    @WorkerThread
    public Bitmap getServiceLogo(final String serviceId) throws IOException, BaseCpsException {
        SharedPreferences mSharedPreferences = context.getSharedPreferences(SDKManager.PreferencesNameConsts.FILE_NAME, Context.MODE_PRIVATE);
        if (mSharedPreferences.getString(SDKManager.PreferencesNameConsts.SERVICE_ID, "") != "" ){
            Response<ResponseBody> response = api.getServiceLogo(serviceId).execute();
            if (!response.isSuccessful())
                CPSErrorParser.throwCpsException(response.errorBody(), response.code());
            else
            if (response.body() != null) {
                Bitmap bmp = BitmapFactory.decodeStream(response.body().byteStream());
                return bmp;
            } else {
                // TODO может ли быть нул? когда не загружена картинка приходит 404
            }
        }
        //TODO бросить ошибку о том, что нет авторизационной сессии
        return null;
    }

    /**
     * Получить url на лого сервиса
     *
     * @param serviceId идентификатор сервиса
     */
    public String getServiceLogoURL(final String serviceId) throws IOException, BaseCpsException {
        SharedPreferences mSharedPreferences = context.getSharedPreferences(SDKManager.PreferencesNameConsts.FILE_NAME, Context.MODE_PRIVATE);
        if (mSharedPreferences.getString(SDKManager.PreferencesNameConsts.SERVICE_ID, "") != "" ) {
            return "http://test.cps.fvds.ru/v0/services/" + serviceId + "/logo";
        }
        //TODO бросить ошибку о том, что нет авторизационной сессии
        return null;
    }

    @WorkerThread
    public String checkSystemStatus() throws IOException {
        Response<BaseResponse<SystemResponse>> response = api.checkSystemStatus().execute();
        if(response.isSuccessful()){
            return response.body().data.status + " until: " + response.body().data.until;
        }

        else return "Back-end is down";
    }
}
