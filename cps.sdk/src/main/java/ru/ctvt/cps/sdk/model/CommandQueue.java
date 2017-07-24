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

import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;

import ru.ctvt.cps.sdk.SDKManager;
import ru.ctvt.cps.sdk.errorprocessing.BaseCpsException;
import ru.ctvt.cps.sdk.errorprocessing.CPSErrorParser;
import ru.ctvt.cps.sdk.network.Api;
import ru.ctvt.cps.sdk.network.BaseResponse;
import ru.ctvt.cps.sdk.network.CommandArguments;
import ru.ctvt.cps.sdk.network.CommandResponse;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Response;

/**
 * Очередь команд
 */
public class CommandQueue {
    @Inject
    Api api;

    private String name;
    private String firstKey;
    private String lastKey;
    private Device parentDevice;
    private String serviceID;
    private String ownerEntityID;
    private String scope;

    CommandQueue(String name, String firstKey, String lastKey, Device parentDevice, String serviceID, String ownerEntityID, String scope) {
        this.name = name;
        this.firstKey = firstKey;
        this.lastKey = lastKey;
        this.parentDevice = parentDevice;
        this.serviceID = serviceID;
        this.ownerEntityID = ownerEntityID;
        this.scope = scope;

        SDKManager.getInstance().getAppComponent().inject(this);
    }

    /**
     * Получить устройство, к которому относится очередь
     *
     * @return Устройство, к которому относится очередь
     */
    public Device getParentDevice() {
        return parentDevice;
    }

    /**
     * Возвращает имя очереди команд
     *
     * @return объект типа String
     */
    public String getName(){
        return name;
    }

    /**
     * Возвращает первичный ключ
     *
     * @return объект типа String
     */
    public String getFirstKey(){
        return firstKey;
    }

    /**
     * Возвращает вторичный ключ
     *
     * @return объект типа String
     */
    public String getLastKey(){
        return lastKey;
    }

    /**
     * Возвращает идентификатор сервиса, в рамках которого находится эта очередь
     *
     * @return объект типа String
     */
    public String getServiceID(){
        return serviceID;
    }

    /**
     * Возвращает идентификатор сущности, кому принадлежит эта очередь (тип сущности зависит от типа области данных)
     *
     * @return объект типа String
     */
    public String getOwnerEntityID(){
        return ownerEntityID;
    }

    /**
     * Возвращает область данных, кому относится эта запись (device_data, user_data, user_public_data, service_data, service_public_data)
     *
     * @return объект типа String
     */
    public String getScope(){
        return scope;
    }

    /**
     * Получить команду из очереди команд
     *
     * @param commandID       идентификатор команды
     *
     * @return Команда из очереди команд
     */
    @WorkerThread
    public Command fetchCommand(String commandID) throws IOException, BaseCpsException {
        Response<BaseResponse<CommandResponse>> response = api.fetchCommand(parentDevice.getDeviceID(), name, commandID).execute();
        if (response.isSuccessful()) {
            CommandResponse resp = response.body().data;
            return new Command(resp.command, resp.id, resp.state, resp.stateChangedAt, this, resp.payload);
        } else
            CPSErrorParser.throwCpsException(response.errorBody(), response.code());
        return null;
    }

    /**
     * Добавить новую команду в очередь команд
     *
     * @param action          действие команды для устройства
     *
     * @return Созданная команда
     */
    @WorkerThread
    public Command createCommand(String action, Object payload) throws IOException, BaseCpsException {
        CommandArguments commandArgs = new CommandArguments(action, payload);
        Response<BaseResponse<CommandResponse>> response = api.addCommand(parentDevice.getDeviceID(), name, commandArgs).execute();
        if (response.isSuccessful()) {
            CommandResponse resp = response.body().data;
            return new Command(resp.id, resp.command, resp.state, resp.stateChangedAt, this, resp.payload);
        } else
            CPSErrorParser.throwCpsException(response.errorBody(), response.code());
        return null;
    }

    /**
     * Получить все принятые к исполнению команды из данной очереди
     *
     * @return Список команд
     */
    @WorkerThread
    public List<Command> fetchQueuedCommands() throws IOException, BaseCpsException {
        Response<BaseResponse<List<CommandResponse>>> response = api.fetchQueuedCommands(parentDevice.getDeviceID(), name).execute();
        if (response.isSuccessful()) {
            List<Command> tmpCommand = new ArrayList<>();
            List<CommandResponse> resp = response.body().data;
            for (int i = 0; i < response.body().data.size(); i++)
                tmpCommand.add(new Command(resp.get(i).id, resp.get(i).command, resp.get(i).state, resp.get(i).stateChangedAt, this, resp.get(i).payload));
            return tmpCommand;
        } else
            CPSErrorParser.throwCpsException(response.errorBody(), response.code());
        return null;
    }

    /**
     * Получить следующую принятую к исполнению команду из данной очереди
     *
     * @return Команда
     */
    @WorkerThread
    public Command fetchNearestQueuedCommand() throws IOException, BaseCpsException {
        Response<BaseResponse<CommandResponse>> response = api.fetchNearestQueuedCommand(parentDevice.getDeviceID(), name).execute();
        if (response.isSuccessful()) {
            CommandResponse resp = response.body().data;
            return new Command(resp.id, resp.command, resp.state, resp.stateChangedAt, this, resp.payload);
        } else
            CPSErrorParser.throwCpsException(response.errorBody(), response.code());
        return null;
    }

    /**
     * Получить все команды очереди команд
     *
     * @return Список команд
     */
    @WorkerThread
    public List<Command> fetchCommands() throws IOException, BaseCpsException {
        Response<BaseResponse<List<CommandResponse>>> response = api.fetchAllCommands(parentDevice.getDeviceID(), name).execute();
        if (response.isSuccessful()) {
            List<Command> tmpCommand = new ArrayList<>();
            List<CommandResponse> resp = response.body().data;
            for (int i = 0; i < response.body().data.size(); i++)
                tmpCommand.add(new Command(resp.get(i).id, resp.get(i).command, resp.get(i).state, resp.get(i).stateChangedAt, this, resp.get(i).payload));
            return tmpCommand;
        } else
            CPSErrorParser.throwCpsException(response.errorBody(), response.code());
        return null;
    }

    /**
     * Вернуть все команды очереди команд в заданном диапазоне - отрезок или луч (параметры не могут быть одновременно null)
     *
     * @param idFrom идентификатор начала диапазона
     * @param idTo   идентификатор конца диапазона
     * @return список команд
     */
    @WorkerThread
    public List<Command> fetchCommands(@Nullable String idFrom, @Nullable String idTo) throws IOException, BaseCpsException {
        if (idFrom == null && idTo == null)
            throw new IllegalArgumentException();

        Gson range = new Gson();
        CommandsRangeBodyParameter queueRange = new CommandsRangeBodyParameter(idFrom, idTo);
        String bodyParameter = range.toJson(queueRange);
        Response<BaseResponse<List<CommandResponse>>> response = api.fetchRangeCommands("/v0/devices/" + parentDevice.getDeviceID() + "/command-queues/" + name + "/commands?" + bodyParameter).execute();

        if (response.isSuccessful()) {
            List<Command> tmpCommand = null;
            List<CommandResponse> resp = response.body().data;
            for (int i = 0; i < response.body().data.size(); i++) {
                tmpCommand.add(new Command(resp.get(i).id, resp.get(i).command, resp.get(i).state, resp.get(i).stateChangedAt, this, resp.get(i).payload));
            }
            return tmpCommand;
        } else
            CPSErrorParser.throwCpsException(response.errorBody(), response.code());
        return null;
    }

    /**
     * Класс для параметра body запроса
     */
    public class CommandsRangeBodyParameter {

        @SerializedName("from")
        private String from;

        @SerializedName("to")
        private String to;

        CommandsRangeBodyParameter(@Nullable String from, @Nullable String to) {
            this.from = from;
            this.to = to;
        }

        CommandsRangeBodyParameter() {
        }
    }

}
