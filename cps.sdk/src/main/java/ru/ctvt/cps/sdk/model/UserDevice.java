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

import android.support.annotation.WorkerThread;

import ru.ctvt.cps.sdk.errorprocessing.BaseCpsException;
import ru.ctvt.cps.sdk.errorprocessing.CPSErrorParser;
import ru.ctvt.cps.sdk.network.BaseResponse;
import ru.ctvt.cps.sdk.network.CreatedDeviceResponse;
import ru.ctvt.cps.sdk.network.DeviceResponse;

import java.io.IOException;

import retrofit2.Response;

/**
 * Класс для работы с устройством пользователя
 */
public class UserDevice extends Device {

    private User parentUser;
    private String gateway_id;
    private String created_at;

    /**
     * Создать устройство
     *
     * @param deviceID   идентификатор устройства
     * @param service_id идентификатор сервиса
     * @param gateway_id идентификатор гейтвея
     * @param created_at дата создания
     * @param parentUser ссылка на объекта-родителя (владельца устройства)
     */
    UserDevice(String deviceID, String service_id, String gateway_id, String created_at, String name, User parentUser) {
        super(deviceID, service_id, name);

        this.gateway_id = gateway_id;
        this.created_at = created_at;
        this.parentUser = parentUser;
    }

    /**
     * Получить владельца устройства
     *
     * @return Владелец устройства
     */
    public User getParentUser() {
        return parentUser;
    }

    /**
     * Получить идентификатор владельца устройства
     *
     * @return Идентификатор владельца
     */
    public String getOwnerID() {
        return getParentUser().getId();
    }

    /**
     * Получить идентификатор шлюза
     * @return
     */
    public String getGateway_id(){
        return gateway_id;
    }

    /**
     * Получить дату создания устройства
     *
     * @return Дата создания устройства
     */
    public String getCreateDate() {
        return created_at;
    }


    /**
     * Привязать реальное устройство к сущности в платформе
     *
     * @param code код, выданный устройству
     */
    @WorkerThread
    public void setDeviceCode(String code) throws IOException, BaseCpsException {
        Response<BaseResponse<CreatedDeviceResponse>> response = api.setDeviceCode(this.getDeviceID(), code).execute();
        if (!response.isSuccessful())
            CPSErrorParser.throwCpsException(response.errorBody(), response.code());
    }

    /**
     * Удаление устройства
     */
    @WorkerThread
    public void deleteDevice() throws IOException, BaseCpsException {
        Response<BaseResponse> response = api.deleteDevice(this.getDeviceID()).execute();
        if (!response.isSuccessful())
            CPSErrorParser.throwCpsException(response.errorBody(), response.code());
    }
}
