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
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;

import ru.ctvt.cps.sdk.SDKManager;
import ru.ctvt.cps.sdk.errorprocessing.BaseCpsException;
import ru.ctvt.cps.sdk.errorprocessing.CPSErrorParser;
import ru.ctvt.cps.sdk.network.Api;
import ru.ctvt.cps.sdk.network.BaseResponse;
import ru.ctvt.cps.sdk.network.TriggerResponse;
import com.google.gson.annotations.SerializedName;

import java.io.IOException;
import java.util.HashMap;

import javax.inject.Inject;

import retrofit2.Response;

/**
 * Класс для работы с триггером
 */
public class Trigger {
    @Inject
    Api api;

    @Inject
    Context context;

    private String serviceID;
    private String deviceID;
    private String parentName;
    private String header;
    private String name;
    private HashMap<String, Object> parameterValues;
    private boolean enabled;
    private TriggerContainer type;

    protected Trigger(Parcel in) {
        name = in.readString();
        serviceID = in.readString();
        header = in.readString();
        //parameterValues = in.readHashMap();

        SDKManager.getInstance().getAppComponent().inject(this);
    }

    /**
     * Создать триггер
     *
     * @param name            имя-идентификатор триггера
     * @param serviceID       идентификатор сервиса
     * @param deviceID        идентификатор устройства, к которому привязан триггер
     * @param parentName      имя очереди команд или последовательности, к которой привязан триггер
     * @param header          заголовок триггера
     * @param parameterValues список параметров триггера и их значений
     * @param enabled         состояние триггера
     */
    Trigger(String name, String serviceID, String deviceID, String parentName, TriggerContainer type, String header, HashMap<String, Object> parameterValues, boolean enabled) {
        this.name = name;
        this.serviceID = serviceID;
        this.deviceID = deviceID;
        this.parentName = parentName;
        this.header = header;
        this.parameterValues = parameterValues;
        this.enabled = enabled;
        this.type = type;

        SDKManager.getInstance().getAppComponent().inject(this);
    }

    /**
     * Получить имя триггера
     *
     * @return Имя триггера
     */
    public String getName(){
        return name;
    }

    /**
     * Получить заголовок триггера
     *
     * @return Заголовок триггера
     */
    public String getHeader(){
        return header;
    }

    /**
     * Получить список параметров триггера и их значений
     *
     * @return Список параметров триггера и их значений
     */
    public HashMap<String, Object> getParameterValues(){
        return parameterValues;
    }

    /**
     * Получить состояние триггера (может быть вклюяен и отключен)
     *
     * @return Состояние триггера
     */
    public boolean isEnabled(){
        return enabled;
    }

    /**
     * Получить тип триггера
     *
     * @return Тип триггера
     */
    public TriggerContainer getType(){
        return type;
    }

    /**
     * Задать состояние триггера (true - включен, false - отключен)
     *
     * @param enabled состояние триггера
     *
     * @throws IOException
     * @throws BaseCpsException
     */
    @WorkerThread
    public void setEnabled(TriggerContainer container, boolean enabled) throws IOException, BaseCpsException{
                Response<BaseResponse> resp = api.patchTrigger(deviceID, container.toString(), parentName, name, new EditTriggerBodyParameter(enabled)).execute();
                if (resp.isSuccessful())
                    this.enabled = enabled;
                else
                    CPSErrorParser.throwCpsException(resp.errorBody(), resp.code());
    }

    /**
     * Меняет статус множества триггеров для очередей команд и последовательностей данных
     *
     * @param forCommandQueues значения для очередей команд
     * @param forSequences значения для последовательностей данных
     *
     * @throws IOException
     * @throws BaseCpsException
     */
    //TODO: попробовать упростить параметры
    @WorkerThread
    public void setManyStatus(@Nullable HashMap<String, HashMap<String, Boolean>> forCommandQueues, @Nullable HashMap<String, HashMap<String, Boolean>> forSequences) throws IOException,BaseCpsException {
        Response<BaseResponse> response = api.manyTriggerControl(deviceID, new SetManyTriggersStatusBodyParameter(forCommandQueues, forSequences)).execute();
        if (!response.isSuccessful())
            CPSErrorParser.throwCpsException(response.errorBody(), response.code());
    }

    /**
     * Обновить данные о триггере с сервера
     *
     * @throws IOException
     * @throws BaseCpsException
     */
    @WorkerThread
    public void fetchData(TriggerContainer container) throws IOException, BaseCpsException {
                Response<BaseResponse<TriggerResponse>> resp = api.fetchTrigger(deviceID, container.toString(), parentName, name).execute();
                if (resp.isSuccessful()){
                    parameterValues = resp.body().data.parameterValues;
                    enabled = resp.body().data.enabled;
                }
                else
                    CPSErrorParser.throwCpsException(resp.errorBody(), resp.code());
    }

    /**
     * Тип отношения триггера
     */
    //TODO: доделать перечисление для типа триггера
    public enum TriggerContainer {

        /**
         * Триггер на последовательтность
         */
        sequenceTrigger("sequences"),

        /**
         * Триггер на очередь команд
         */
        commandQueueTrigger("command-queues");

        private final String name;

        TriggerContainer(String s) {
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
     * Класс для параметра body запросов PATCH
     */
    private class EditTriggerBodyParameter {
        @SerializedName("enabled")
        public boolean enabled;

        public EditTriggerBodyParameter(boolean enabled){
            this.enabled = enabled;
        }

        public EditTriggerBodyParameter(){}
    }

    /**
     * Класс для параметра body запроса изменения состояния множества триггеров
     */
    private class SetManyTriggersStatusBodyParameter {
        @SerializedName("command_queues")
        HashMap<String, HashMap<String, Boolean>> commandQueues;

        @SerializedName("sequences")
        HashMap<String, HashMap<String, Boolean>> sequences;

        public SetManyTriggersStatusBodyParameter(@Nullable HashMap<String, HashMap<String, Boolean>> forCommandQueues, @Nullable HashMap<String, HashMap<String, Boolean>> forSequences){
            this.commandQueues = forCommandQueues;
            this.sequences = forSequences;
        }

        public SetManyTriggersStatusBodyParameter(){}
    }
}
