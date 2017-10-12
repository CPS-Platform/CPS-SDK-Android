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

import ru.ctvt.cps.sdk.SDKManager;
import ru.ctvt.cps.sdk.dagger.ApiWrapper;
import ru.ctvt.cps.sdk.errorprocessing.BaseCpsException;
import ru.ctvt.cps.sdk.errorprocessing.CPSErrorParser;
import ru.ctvt.cps.sdk.network.Api;
import ru.ctvt.cps.sdk.network.BaseResponse;
import ru.ctvt.cps.sdk.network.TriggerResponse;
import ru.ctvt.cps.sdk.network.ValueT;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import retrofit2.Response;


/**
 * Последовательность данных устройства
 */
public class Sequence<KeyType, ValueType>{
    @Inject
    Api api;

    @Inject
    ApiWrapper apiWrapper;

    private String sequenceName;
    private String serviceID;
    private Device parentDevice;
    private Type type;

    /**
     * Тип ключа в последовательности
     */
    public enum Type {
        /**
         * Дата и время
         */
        datetime("datetime"),

        /**
         * Целые значения
         */
        integer("integer"),

        /**
         * Рациональные значения
         */
        real("real");

        private final String name;

        Type(String s) {
            name = s;
        }

        public boolean equalsName(String otherName) {
            return name.equals(otherName);
        }

        public String toString() {
            return this.name;
        }
    }

    Sequence(String sequenceName, Device parentDevice, Class<KeyType> keyType) {
        this.sequenceName = sequenceName;
        this.parentDevice = parentDevice;
        switch (keyType.getSimpleName()){
            case "Integer":
                type = Type.integer;
                break;
            case "Float":
                type = Type.real;
                break;
            case "Calendar":
                type = Type.datetime;
                break;
            default:
                break;
        }
        apiWrapper = new ApiWrapper();
        SDKManager.getInstance().getAppComponent().inject(apiWrapper);
    }

    /**
     * Возвращает имя последовательности
     *
     * @return значение типа String
     */
    public String getSequenceName(){
        return sequenceName;
    }

    /**
     * Возвращает родительское устройство
     *
     * @return объект типа Device
     */
    public Device getParentDevice(){
        return parentDevice;
    }

    /**
     * Возвращает тип последовательности
     *
     * @return значение из enum
     */
    public Type getType(){
        return type;
    }

    /**
     * Возвращает идентификатор сервиса
     * @return
     */
    public String getServiceID(){
        return serviceID;
    }

    /**
     * Возвращает целевое значение текущей последовательности для текущего контейнера
     *
     * @param dataItemID Идентификатор значения
     *
     * @return Объект типа DataItem
     *
     * @throws IOException
     * @throws BaseCpsException
     */
    @WorkerThread
    public Object fetchDataItem(String dataItemID) throws IOException, BaseCpsException {
        Response<BaseResponse<Object>> response = apiWrapper.api.getDataByKey(parentDevice.getDeviceID(), sequenceName, dataItemID).execute();
        if(response.isSuccessful())
            return response.body().data;
        else
            CPSErrorParser.throwCpsException(response.errorBody(), response.code());
        return null;
    }

    /**
     * Возвращает все значения текущей последовательности для текущего контейнера
     *
     * @return HashMap данных из последовательности
     *
     * @throws IOException
     * @throws BaseCpsException
     */
    @WorkerThread
    public HashMap<String, Object> fetchAllValues() throws IOException, BaseCpsException {
        Response<BaseResponse<HashMap<String, Object>>> response = apiWrapper.api.fetchAllValues(parentDevice.getDeviceID(), sequenceName).execute();
        if(response.isSuccessful())
            return response.body().data;
        else
            CPSErrorParser.throwCpsException(response.errorBody(), response.code());
        return null;
    }

    /**
     * Возвращает элементы последовательности в каком-либо диапазоне
     *
     * @param from левая граница диапазона
     * @param to правая граница диапазона
     * @param centre центр диапазона
     * @param radius радиус значений относительно центра
     * @param keys набор ключей
     *
     * @throws IOException
     * @throws BaseCpsException
     */
    @WorkerThread
    public HashMap<String, Object> fetchRangeValues(String from, String to, String centre, String radius, List<String> keys) throws IOException, BaseCpsException {
        Gson range = new Gson();
        FetchRangeValuesBodyParameter fetchRangeValuesBodyParameter = new FetchRangeValuesBodyParameter(from, to, centre, radius, keys);
        String bodyParameter = range.toJson(fetchRangeValuesBodyParameter);
        Response<BaseResponse<HashMap<String, Object>>> response =
                apiWrapper.api.fetchRangeValues("/v0/devices/" + parentDevice.getDeviceID() + "/sequences/" + sequenceName + "/items?" + bodyParameter).execute();
        if (response.isSuccessful())
            return response.body().data;
        else
            CPSErrorParser.throwCpsException(response.errorBody(), response.code());
        return null;
    }

    /**
     * Удаляет множество данных в данной последовательности для текущего контейнера
     *
     * @param keys список ключей
     *
     * @throws IOException
     * @throws BaseCpsException
     */
    @WorkerThread
    public void deleteManyData(List<String> keys) throws IOException, BaseCpsException {
        Response<BaseResponse> response =
                apiWrapper.api.deleteManyData(parentDevice.getDeviceID(), sequenceName, new DeleteManyItemsBodyParameter(keys)).execute();
        if(!response.isSuccessful())
            CPSErrorParser.throwCpsException(response.errorBody(), response.code());
    }

    /**
     * Удаляет множество данных в данной последовательности для текущего контейнера
     *
     * @param keys ключи
     * @param whenValues значения для проверки
     *
     * @throws IOException
     * @throws BaseCpsException
     */
    @WorkerThread
    public void deleteManyData(List<String> keys, HashMap<String, Object> whenValues) throws IOException, BaseCpsException{
        Response<BaseResponse> response =
                apiWrapper.api.deleteManyData(parentDevice.getDeviceID(), sequenceName, new DeleteManyItemsBodyParameter(keys, whenValues)).execute();
        if(!response.isSuccessful())
            CPSErrorParser.throwCpsException(response.errorBody(), response.code());
    }

    /**
     * Обновляет или добавляет множество значений в данной последовательности для текущего контейнера
     *
     * @param values старые значения
     *
     * @throws IOException
     * @throws BaseCpsException
     */
    @WorkerThread
    public void patchManyData(HashMap<String, Object> values) throws IOException, BaseCpsException {
        Response<BaseResponse> response = apiWrapper.api.patchManyData(parentDevice.getDeviceID(), sequenceName, new PatchManyDataBodyParameter(values)).execute();
        if(!response.isSuccessful())
            CPSErrorParser.throwCpsException(response.errorBody(), response.code());
    }

    /**
     * Обновляет или добавляет множество значений в данной последовательности для текущего контейнера
     *
     * @param values старые значения
     * @param whenValues новые значения
     *
     * @throws IOException
     * @throws BaseCpsException
     */
    @WorkerThread
    public void patchManyData(HashMap<String, Object> values, HashMap<String, Object> whenValues) throws IOException, BaseCpsException {
        Response<BaseResponse> response = apiWrapper.api.patchManyData(parentDevice.getDeviceID(), sequenceName, new PatchManyDataBodyParameter(values, whenValues)).execute();
        if(!response.isSuccessful())
            CPSErrorParser.throwCpsException(response.errorBody(), response.code());
    }

    /**
     * Добавляет множество данных в текущую последовательность для текущего контейнера
     *
     * @param values значения
     *
     * @throws IOException
     * @throws BaseCpsException
     */
    @WorkerThread
    public void addManyData(HashMap<String, Object> values) throws IOException, BaseCpsException {
        Response<BaseResponse> response = apiWrapper.api.addManyData(parentDevice.getDeviceID(), sequenceName, new AddManyDataBodyParameter(values)).execute();
        if(!response.isSuccessful())
            CPSErrorParser.throwCpsException(response.errorBody(), response.code());
    }

    /**
     * Добавить данный элемент последовательности в текущую последовательность без ключа
     *
     * @param value значение
     *
     * @throws IOException
     * @throws BaseCpsException
     */
    @WorkerThread
    public void addDataKeyless(Object value) throws IOException, BaseCpsException{
        if (!this.type.toString().equals("datetime"))
            throw new IOException();
        ValueT<Object> valueT = new ValueT<>();
        valueT.value = value;
        Response<BaseResponse> response = apiWrapper.api.addDataKeyless(parentDevice.getDeviceID(), sequenceName, valueT).execute();
        if(!response.isSuccessful())
            CPSErrorParser.throwCpsException(response.errorBody(), response.code());
    }


    /**
     * Удалить значение по ключу в текущей последовательности для текущего контейнера
     *
     * @param key идентификатор элемента последовательности
     *
     * @throws IOException
     * @throws BaseCpsException
     */
    @WorkerThread
    public void deleteOneItem(String key) throws IOException, BaseCpsException {
        Response<BaseResponse> response = apiWrapper.api.deleteOneItem(parentDevice.getDeviceID(), sequenceName, key, new DeleteOneItemBodyParameter()).execute();
        if(!response.isSuccessful())
            CPSErrorParser.throwCpsException(response.errorBody(), response.code());
    }

    /**
     * Удаляет одно значение по ключу в текущей последовательности для текущего контейнера при условии совпадения значения
     *
     * @param key идентификатор элемента последовательности
     * @param whenValue значение для проверки
     *
     * @throws IOException
     * @throws BaseCpsException
     */
    @WorkerThread
    public void deleteOneItem(String key, Object whenValue) throws IOException, BaseCpsException{
        Response<BaseResponse> response = apiWrapper.api.deleteOneItem(parentDevice.getDeviceID(), sequenceName, key, new DeleteOneItemBodyParameter(whenValue)).execute();
        if(!response.isSuccessful())
            CPSErrorParser.throwCpsException(response.errorBody(), response.code());
    }

    /**
     * Добавить значение по ключу в текущей последовательности
     *
     * @param value значение
     * @param dataItemID идентификатор элемента последовательности
     *
     * @throws IOException
     * @throws BaseCpsException
     */
    @WorkerThread
    public void addDataByKey(Object value, String dataItemID) throws IOException, BaseCpsException {
        ValueT<Object> valueT = new ValueT<>();
        valueT.value = value;
        Response<BaseResponse> response = apiWrapper.api.addDataByKey(parentDevice.getDeviceID(), sequenceName, dataItemID, valueT).execute();
        if(!response.isSuccessful())
            CPSErrorParser.throwCpsException(response.errorBody(), response.code());
    }

    public ArrayList<Trigger> fetchTriggers() throws IOException, BaseCpsException{
        Response<BaseResponse<HashMap<String, TriggerResponse>>> response = apiWrapper.api.fetchTriggers(parentDevice.getDeviceID(), Trigger.TriggerContainer.sequenceTrigger.toString(), sequenceName).execute();
        if(response.isSuccessful()) {
            ArrayList<Trigger> triggers = new ArrayList<>();
            for (Map.Entry<String, TriggerResponse> entry : response.body().data.entrySet()) {
                Trigger trigger = new Trigger(entry.getValue().name, entry.getValue().trigger.serviceId, parentDevice.getDeviceID(), this.sequenceName, Trigger.TriggerContainer.sequenceTrigger, entry.getValue().trigger.name, entry.getValue().parameterValues, entry.getValue().enabled);
                triggers.add(trigger);
            }
            return triggers;
        }
        else
            CPSErrorParser.throwCpsException(response.errorBody(), response.code());
        return null;
    }

    private class AddManyDataBodyParameter{
        @SerializedName("values")
        HashMap<String, Object> values;

        AddManyDataBodyParameter(HashMap<String, Object> values){
            this.values = values;
        }
    }
    /**
     * Класс для параметра body запроса удаления одного элемента последовательности
     */
    private class DeleteOneItemBodyParameter {
        @SerializedName("when_value")
        Object whenValue;

        DeleteOneItemBodyParameter(Object whenValue){
            this.whenValue = whenValue;
        }

        DeleteOneItemBodyParameter(){
            this.whenValue = null;
        }
    }

    /**
     * Класс для параметра body запроса удаления множества элементов последовательности
     */
    private class DeleteManyItemsBodyParameter {
        @SerializedName("keys")
        public List<String> keys;

        @SerializedName("when_values")
        public HashMap<String, Object> whenValues;

        DeleteManyItemsBodyParameter(List<String> keys){
            this.keys = keys;
        }

        DeleteManyItemsBodyParameter(List<String> keys, HashMap<String, Object> whenValues){
            this.keys = keys;
            this.whenValues = whenValues;
        }
    }

    /**
     * Класс для параметра body запроса получения значений в определенном диапазоне
     */
    private class FetchRangeValuesBodyParameter {
        @SerializedName("centre")
        String centre;

        @SerializedName("from")
        String from;

        @SerializedName("radius")
        String radius;

        @SerializedName("to")
        String to;

        @SerializedName("keys")
        List<String> keys;

        FetchRangeValuesBodyParameter(String from, String to, String centre, String radius, List<String> keys){
            this.from = from;
            this.to = to;
            this.centre = centre;
            this.radius = radius;
            this.keys = keys;
        }
    }

    /**
     * Класс для параметра body запроса изменения или записи множества элементов последовательности
     */
    private class PatchManyDataBodyParameter{
        @SerializedName("values")
        HashMap<String, Object> values;

        @SerializedName("when_values")
        HashMap<String, Object> whenValues;

        PatchManyDataBodyParameter(HashMap<String, Object> values){
            this.values = values;
        }

        PatchManyDataBodyParameter(HashMap<String, Object> values, HashMap<String, Object> whenValues){
            this.values = values;
            this.whenValues = whenValues;
        }
    }
}
