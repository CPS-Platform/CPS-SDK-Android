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
import android.os.Parcel;
import android.support.annotation.WorkerThread;

import ru.ctvt.cps.sdk.SDKManager;
import ru.ctvt.cps.sdk.errorprocessing.BaseCpsException;
import ru.ctvt.cps.sdk.errorprocessing.CPSErrorParser;
import ru.ctvt.cps.sdk.network.Api;
import ru.ctvt.cps.sdk.network.BaseResponse;
import ru.ctvt.cps.sdk.network.CommandQueueResponse;
import ru.ctvt.cps.sdk.network.DeviceResponse;
import ru.ctvt.cps.sdk.network.SequenceResponse;

import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.Calendar;
import java.util.HashMap;

import javax.inject.Inject;

import retrofit2.Response;

/**
 * Класс для работы с устройством
 */
public class Device {
    @Inject
    Api api;

    @Inject
    Context context;

    private String deviceID;
    private String serviceID;
    private String deviceName;

    protected Device(Parcel in) {
        deviceID = in.readString();
        serviceID = in.readString();

        SDKManager.getInstance().getAppComponent().inject(this);
    }


    /**
     * Режим создания
     */
    public enum CreationMode {
        /**
         * Перезаписать если существует
         */
        overwrite_if_exist("overwrite_if_exists"),

        /**
         * Пропустить если существует
         */
        skip_if_exactly_exist("skip_if_exactly_exists"),

        /**
         * Получить ошибку если существует
         */
        error_if_exist("error_if_exists");

        private final String name;

        private CreationMode(String s) {
            name = s;
        }

        public boolean equalsName(String otherName) {
            return name.equals(otherName);
        }

        public String toString() {
            return this.name;
        }
    }

    /**
     * Создать устройство
     *
     * @param deviceID   идентификатор устройства
     * @param serviceID идентификатор сервиса
     */
    Device(String deviceID, String serviceID, String name) {
        this.deviceID = deviceID;
        this.serviceID = serviceID;
        this.deviceName = name;

        SDKManager.getInstance().getAppComponent().inject(this);
    }

    public String getServiceID(){ return serviceID; }

    /**
     * Получить идентификатор устройства
     *
     * @return Идентификатор устройства
     */
    public String getDeviceID() {
        return deviceID;
    }

    public String getName(){
        return deviceName;
    }



    @WorkerThread
    public void editDeviceName(String newDeviceName) throws IOException, BaseCpsException {
        Response<BaseResponse<DeviceResponse>> response = api.editDevice(this.deviceID, newDeviceName).execute();
        if(response.isSuccessful()){
            this.deviceName = newDeviceName;
        }
        else CPSErrorParser.throwCpsException(response.errorBody(), response.code());
    }

    /**
     * Получить локальное (приватное) хранилище текущего устройства
     * @return Локальное (приватное) хранилище текущего устройства
     */
    public KeyValueStorage getLocalKVStorage() throws IOException, BaseCpsException {
        return new KeyValueStorage(KeyValueStorage.ContainerTypeEnum.devices, deviceID, KeyValueStorage.VisibilityEnum.local);
    }


    /**
     * Получить очередь команд
     *
     * @param queueName имя очереди команд
     * @return Очередь команд
     */
    @WorkerThread
    public CommandQueue fetchCommandQueue(String queueName) throws IOException, BaseCpsException {
        Response<BaseResponse<CommandQueueResponse>> response = api.fetchCommandQueueInfo(deviceID, queueName).execute();
        if (response.isSuccessful())
            return new CommandQueue(
                    response.body().data.name,
                    response.body().data.firstKey,
                    response.body().data.lastKey,
                    this,
                    response.body().data.serviceID,
                    response.body().data.ownerEntityID,
                    response.body().data.scope);
        else
            CPSErrorParser.throwCpsException(response.errorBody(), response.code());
        return null;
    }

    /**
     * Получить все очереди команд для текущего устройства
     *
     * @return Ассоциативный массив "имя"-"очередь команд"
     */
    @WorkerThread
    public HashMap<String, CommandQueue> fetchAllCommandQueues() throws IOException, BaseCpsException {
        Response<BaseResponse<HashMap<String, CommandQueueResponse>>> response = api.fetchAllQueues(deviceID).execute();
        if (response.isSuccessful()) {
            HashMap<String, CommandQueueResponse> tmpResponse = response.body().data;
            HashMap<String, CommandQueue> tmpQueue = new HashMap<>();
            for (CommandQueueResponse resp : tmpResponse.values())
                tmpQueue.put(resp.name, new CommandQueue(resp.name, resp.firstKey, resp.lastKey, this, resp.serviceID, resp.ownerEntityID, resp.scope));
            return tmpQueue;
        } else
            CPSErrorParser.throwCpsException(response.errorBody(), response.code());
        return null;
    }

    /**
     * Создать новую очередь команд для текущего устройства
     *
     * @param queueName    идентификатор новой команды (задается пользователем)
     * @param creationMode режим создания
     * @return Объект типа CommandQueue
     */
    @WorkerThread
    public CommandQueue createCommandQueue(String queueName, CreationMode creationMode) throws IOException, BaseCpsException {
        Response<BaseResponse<CommandQueueResponse>> response = api.addQueue(deviceID, queueName, creationMode.toString()).execute();
        if (response.isSuccessful())
            return new CommandQueue(
                    response.body().data.name,
                    response.body().data.firstKey,
                    response.body().data.lastKey,
                    this,
                    response.body().data.serviceID,
                    response.body().data.ownerEntityID,
                    response.body().data.scope);
        else
            CPSErrorParser.throwCpsException(response.errorBody(), response.code());
        return null;
    }

    /**
     * Получить последовательность данных
     *
     * @param sequenceName имя последовательности
     * @return Последовательность данных
     */
    @WorkerThread
    public<ValueType> Sequence fetchSequence(String sequenceName) throws IOException, BaseCpsException {
        Response<BaseResponse<SequenceResponse>> response = api.fetchSequenceInfo(deviceID, sequenceName).execute();
        if (response.isSuccessful())
        {
            switch (response.body().data.type.toString())
            {
                case "datetime":
                    return new Sequence<Calendar, ValueType>(response.body().data.name, this, Calendar.class);
                case "integer":
                    return new Sequence<Integer, ValueType>(response.body().data.name, this, Integer.class);
                case "real":
                    return new Sequence<Float, ValueType>(response.body().data.name, this, Float.class);
                default:
                    throw new InvalidParameterException();
            }
        }
        else
            CPSErrorParser.throwCpsException(response.errorBody(), response.code());
        return null;
    }

    /**
     * Получить все последовательности данных
     *
     * @return Ассоциативный список последовательностей "имя"-"поледовательность"
     */
    @WorkerThread
    public<ValueType> HashMap<String, Sequence> fetchAllSequences() throws IOException, BaseCpsException {
        Response<BaseResponse<HashMap<String, SequenceResponse>>> response = api.fetchAllSequences(deviceID).execute();
        if (response.isSuccessful()) {
            HashMap<String, SequenceResponse> tmpResponse = response.body().data;
            HashMap<String, Sequence> tmpSequence = new HashMap<>();
            for (SequenceResponse resp : tmpResponse.values())
            {
                switch (resp.type.toString())
                {
                    case "datetime":
                        tmpSequence.put(resp.name, new Sequence<Calendar, ValueType>(resp.name, this, Calendar.class));
                        break;
                    case "integer":
                        tmpSequence.put(resp.name, new Sequence<Integer, ValueType>(resp.name, this, Integer.class));
                        break;
                    case "real":
                        tmpSequence.put(resp.name, new Sequence<Float, ValueType>(resp.name, this, Float.class));
                }
            }
            return tmpSequence;
        } else
            CPSErrorParser.throwCpsException(response.errorBody(), response.code());
        return null;
    }

    /**
     * Удаляет текущую последовательность данных для текущего контейнера
     *
     * @param sequenceName идентификатор последовательности
     * @throws IOException
     * @throws BaseCpsException
     */
    @WorkerThread
    public void deleteSequence(String sequenceName) throws IOException, BaseCpsException {
        Response<BaseResponse> response = api.deleteSequence(deviceID, sequenceName).execute();
        if (!response.isSuccessful())
            CPSErrorParser.throwCpsException(response.errorBody(), response.code());
    }

    /**
     * Создать новую последовательность данных
     *
     * @param sequenceName имя последовательности
     * @param type         тип данных, которые хранит последовательность
     * @param creationMode режим создания
     */
    @WorkerThread
    public void createSequence(String sequenceName, Sequence.Type type, CreationMode creationMode) throws IOException, BaseCpsException {
        Response<BaseResponse> response = api.createSequence(deviceID, sequenceName, type.toString(), creationMode.toString()).execute();
        if (!response.isSuccessful())
            CPSErrorParser.throwCpsException(response.errorBody(), response.code());
    }

    /**
     * Удалить очередь
     *
     * @param queueName имя очереди
     */
    @WorkerThread
    public void deleteCommandQueue(String queueName) throws IOException, BaseCpsException {
        Response<BaseResponse> response = api.deleteQueue(deviceID, queueName).execute();
        if (!response.isSuccessful())
            CPSErrorParser.throwCpsException(response.errorBody(), response.code());
    }

}
