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
import ru.ctvt.cps.sdk.errorprocessing.BaseCpsException;
import ru.ctvt.cps.sdk.errorprocessing.CPSErrorParser;
import ru.ctvt.cps.sdk.network.Api;
import ru.ctvt.cps.sdk.network.BaseResponse;
import ru.ctvt.cps.sdk.network.ValueT;
import ru.ctvt.cps.sdk.network.ValuesT;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import retrofit2.Response;

/**
 * Класс для управления хранилищем иерархической (группы, ключи, значения) структуры данных
 * Может быть получен для любого контейнера.
 * Может быть локальным или публичным.
 * Каждый объект хранилища соответствует создавшему его объекту-контейнеру.
 * Этот объект позволяет выполнять запросы получения, записи и удаления данных.
 */
public class KeyValueStorage {

    Gson gson = new Gson();
    /**
     * Видимость хранилища данных
     */
    public enum VisibilityEnum {

        /**
         * Видимость в рамках контейнера
         */
        local("local"),

        /**
         * Видимость за пределами контейнера
         */
        global("public");

        private final String name;

        VisibilityEnum(String s) {
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
     * Тип контейнера хранилища
     */
    enum ContainerTypeEnum {
        /**
         * контейнер "Пользователи"
         */
        users("services/current/users"),

        /**
         * контейнер "Устройства"
         */
        devices("devices"),

        /**
         * контейнер "Сервисы" (Пока не реализовано)
         */
        services("services");

        private final String name;

        ContainerTypeEnum(String s) {
            name = s;
        }

        public boolean equalsName(String otherName) {
            return name.equals(otherName);
        }

        public String toString() {
            return this.name;
        }
    }

    private ContainerTypeEnum container;
    private String paramId;
    private VisibilityEnum visibility;

    @Inject
    Api api;

    /**
     * универсальный конструктор, используется для получения хранилища любого контейнера
     *
     * @param сontainerType то, к чему относится хранилище структуры данных (users / devices / services)
     * @param containerId   идентификатор владельца хранилища (user_id / device_id / service_id)
     * @param visibility    видимость хранилища (local / public) @see VisibilityValues
     */
    KeyValueStorage(ContainerTypeEnum сontainerType, String containerId, VisibilityEnum visibility) {
        this.container = сontainerType;
        this.paramId = containerId;
        this.visibility = visibility;
        SDKManager.getInstance().getAppComponent().inject(this);
    }

    /**
     * Получить все значения
     *
     * @return Все значения в виде словаря словарей значений
     */
    @WorkerThread
    public HashMap<String, HashMap<String, Object>> fetchKVStorageData() throws IOException, BaseCpsException {
        Response<BaseResponse<HashMap<String, HashMap<String, Object>>>> response = api.getKVStorage(container.toString(), paramId, visibility.toString()).execute();
        if (response.isSuccessful())
            return response.body().data;
        else
            CPSErrorParser.throwCpsException(response.errorBody(), response.code());
        return null;
    }

    /**
     * Получить группу по ключу (С указанием нескольких типов объектов)
     * Обратите внимание!
     * POJO НЕ ДОЛЖНЫ быть одинаковыми
     * один POJO не должен содержать все поля, идентичные другому
     * иначе возможна некорректная десериализация
     * @param groupKey ключ
     * @param listOfTypes список типов, которые ожидаются к получению
     * @return коллекция ключ - объекты указанных типов
     * @throws IOException
     * @throws BaseCpsException
     */
    @WorkerThread
    public HashMap<String, Object> fetchGroup(String groupKey, List<Class<?>> listOfTypes) throws IOException, BaseCpsException {
        Response<BaseResponse<HashMap<String, JsonElement>>> response = api.getKVGroupAsObject(container.toString(), paramId, visibility.toString(), groupKey).execute();
        HashMap<String, Object> map = new HashMap<>();
        if (response.isSuccessful()){
            for (Map.Entry entry : response.body().data.entrySet()){
                for (int i = 0; i< listOfTypes.size(); i++){
                    try {
                        map.put(entry.getKey().toString(), gson.fromJson(gson.toJson(entry.getValue()), listOfTypes.get(i)));
                        break;
                    } catch (Exception e){}
                }
            }
            return map;
        }

        else {
            CPSErrorParser.throwCpsException(response.errorBody(), response.code());
            return null;
        }
    }

    /**
     * Получить группу по ключу
     * @param groupKey ключ группы
     * @return коллекция типа ключ - LinkedTreeMap
     * @throws IOException
     * @throws BaseCpsException
     */
    @WorkerThread
    public HashMap<String, Object> fetchGroup(String groupKey) throws IOException, BaseCpsException {
        Response<BaseResponse<HashMap<String, Object>>> response = api.getKVGroup(container.toString(), paramId, visibility.toString(), groupKey).execute();
        if (response.isSuccessful())
            return response.body().data;
        else {
            CPSErrorParser.throwCpsException(response.errorBody(), response.code());
            return null;
        }
    }

    /**
     * Получить все значения по ключу
     * Предназначен ТОЛЬКО для случая, когда в одной группе лежат объекты одного типа
     * @param groupKey ключ группы
     * @param type тип принимаемых значений
     * @param <T> необязательный дженерик-параметр
     * @return коллекция ключ - указанный тип.
     * @throws IOException
     * @throws BaseCpsException
     */
    @WorkerThread
    public <T> HashMap<String, T> fetchGroup(String groupKey, Class<T> type) throws IOException, BaseCpsException {
        Response<BaseResponse<HashMap<String, JsonElement>>> response = api.getKVGroupAsObject(container.toString(), paramId, visibility.toString(), groupKey).execute();
        if (response.isSuccessful()){
            HashMap<String, T> map = new HashMap<>();
            for (Map.Entry entry : response.body().data.entrySet()){
                map.put(entry.getKey().toString(), gson.fromJson(gson.toJson(entry.getValue()), type));
            }
            return map;
        }
        else{
            CPSErrorParser.throwCpsException(response.errorBody(), response.code());
            return null;
        }
    }


    /**
     * Получить значение по имени группы и ключу (Дженерик для десериализации значения в объект)
     * @param groupName имя группы
     * @param key ключ
     * @param type тип возвращаемого значения. должен быть передан *.class
     *             Можно описывать собственные POJO-объекты и передавать их как тип.
     * @param <T> - необязательный параметр - тип
     * @return объект произвольного типа
     * @throws IOException
     * @throws BaseCpsException
     */
    @WorkerThread
    public <T> T fetchValue(String groupName, String key, Class<T> type) throws IOException, BaseCpsException {
        Response<BaseResponse<JsonElement>> response = api.getValueAsObject(container.toString(), paramId, visibility.toString(), groupName, key).execute();
        if (response.isSuccessful())
            return gson.fromJson(gson.toJson(response.body().data), type);
        else
            CPSErrorParser.throwCpsException(response.errorBody(), response.code());
        return null;
    }

    /**
     * Получить значение по имени группы и ключу (Метод возвращает объект типа LinkedTreeMap)
     * @param groupName имя группы
     * @param key ключ
     * @return Объект значение - LinkedTreeMap
     * @throws IOException
     * @throws BaseCpsException
     */
    @WorkerThread
    public Object fetchValue(String groupName, String key) throws IOException, BaseCpsException {
        Response<BaseResponse<Object>> response = api.getValue(container.toString(), paramId, visibility.toString(), groupName, key).execute();
        if (response.isSuccessful())
            return response.body().data;
        else
            CPSErrorParser.throwCpsException(response.errorBody(), response.code());
        return null;
    }

    /**
     * Удалить группу по имени группы
     *
     * @param groupName имя группы
     */
    @WorkerThread
    public void deleteGroup(String groupName) throws IOException, BaseCpsException {
        Response<BaseResponse> response = api.deleteGroup(container.toString(), paramId, visibility.toString(), groupName).execute();
        if (!response.isSuccessful())
            CPSErrorParser.throwCpsException(response.errorBody(), response.code());
    }

    /**
     * Удалить значение по имени группы и ключу
     *
     * @param groupName имя группы
     * @param key       ключ значения
     */
    @WorkerThread
    public void deleteValue(String groupName, String key) throws IOException, BaseCpsException {
        Response<BaseResponse> response = api.deleteValue(container.toString(), paramId, visibility.toString(), groupName, key).execute();
        if (!response.isSuccessful())
            CPSErrorParser.throwCpsException(response.errorBody(), response.code());
    }

    /**
     * Добавление новой группы в хранилище
     *
     * @param groupName  имя группы
     * @param groupValue словарь ключей и их значений
     */
    @WorkerThread
    public <T> void putGroup(String groupName, HashMap<String, T> groupValue) throws IOException, BaseCpsException {
        ValuesT<HashMap<String, T>> sendMap = new ValuesT<>();
        sendMap.values = groupValue;
        Response<BaseResponse<String>> response = api.putGroup(container.toString(), paramId, visibility.toString(), groupName, sendMap).execute();
        if (!response.isSuccessful())
            CPSErrorParser.throwCpsException(response.errorBody(), response.code());
    }

    /**
     * Записать значение
     *
     * @param groupName имя группы
     * @param key       ключ значения
     * @param value     записываемое значение
     */
    @WorkerThread
    public <T> void putValue(String groupName, String key, T value) throws IOException, BaseCpsException {
        ValueT<T> valueT =  new ValueT<>();
        valueT.value = value;
        Response<BaseResponse> response = api.putValue(container.toString(), paramId, visibility.toString(), groupName, key, valueT).execute();
        if (!response.isSuccessful())
            CPSErrorParser.throwCpsException(response.errorBody(), response.code());
    }
}
