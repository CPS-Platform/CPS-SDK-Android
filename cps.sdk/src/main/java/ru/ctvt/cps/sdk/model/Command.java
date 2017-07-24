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
import ru.ctvt.cps.sdk.network.CommandResponse;

import java.io.IOException;

import javax.inject.Inject;

import retrofit2.Response;

/**
 * Команда управления устройством
 */
public class Command
{
    @Inject
    Api api;

    private String name;
    private String commandId;
    private State state;
    private String stateChangedAt;
    private CommandQueue parentCommandQueue;
    private Object payLoad;
    private Object result;

    /**
     * Состояние комманды
     */
    public enum State {
        /**
         * Добавлена в очередь
         */
        queued("queued"),

        /**
         * Взята к исполнению
         */
        acquired("acquired"),

        /**
         * Успешно исполнена
         */
        executed("executed"),

        /**
         * Отменена до исполнения
         */
        rejected("rejected"),

        /**
         * Взята к исполнению и не исполнена за определенное время
         */
        expired("expired"),

        /**
         * Ошибка при исполнении
         */
        failed("failed");

        private final String name;

        State(String s) {
            name = s;
        }

        public String toString() {
            return this.name;
        }
    }

    Command(String commandId, String name, State state, String stateChangedAt, CommandQueue parentCommandQueue, Object payLoad) {
        this.commandId = commandId;
        this.name = name;
        this.state = state;
        this.stateChangedAt = stateChangedAt;
        this.parentCommandQueue = parentCommandQueue;
        this.payLoad = payLoad;
        this.result = null;

        SDKManager.getInstance().getAppComponent().inject(this);
    }

    /**
     * Возвращает имя команды
     *
     * @return объект типа String
     */
    public String getName(){
        return name;
    }

    /**
     * Возвращает идентификатор команды
     *
     * @return объект типа String
     */
    public String getCommandId(){
        return this.commandId;
    }

    /**
     * Возвращает объект-аргумент.
     *
     * @return объект типа String
     */
    public Object getPayLoad() {
        return payLoad;
    }

    /**
     * Возвращает результат выполнения команды <br>
     * Для получения актуального значения результата с сервера не забудьте вызвать {@link #fetchData()}
     *
     * @return объект типа Object
     */
    public Object getResult() {
        return result;
    }

    /**
     * Получить состояние команды <br>
     * Для получения актуального значения состояния с сервера не забудьте вызвать {@link #fetchData()}
     *
     * @return объект типа enum
     */
    public State getState() {
        return state;
    }

    /**
     * Возвращает время изменения состояния команды
     *
     * @return объект типа String
     */
    public String getStateChangedAt(){
        return stateChangedAt;
    }

    /**
     * Возвращает родительскую очередь команд для текущей команды
     *
     * @return объект типа CommandQueue
     */
    public CommandQueue getParentCommandQueue(){
        return parentCommandQueue;
    }

    /**
     * Обновить следующую информацию о команде с сервера:<br>
     * - результат ({@link #getResult()})<br>
     * - состояние ({@link #getState()})
     */
    @WorkerThread
    public Command fetchData() throws IOException, BaseCpsException {
        Response<BaseResponse<CommandResponse>> response = api.fetchCommand(parentCommandQueue.getParentDevice().getDeviceID(), parentCommandQueue.getName(), commandId).execute();
        if (response.isSuccessful()) {
            CommandResponse resp = response.body().data;
            this.commandId = resp.id;
            this.payLoad = resp.payload;
            this.name = resp.command;
            this.state = resp.state;
            this.result = resp.result;
            return this;
        } else
            CPSErrorParser.throwCpsException(response.errorBody(), response.code());
        return null;
    }

    /**
     * Пометить команду как выполняемую (изменяется состояние)
     */
    @WorkerThread
    public void markExecuting() throws IOException, BaseCpsException {
        Response<BaseResponse> response = api.markExecuting(parentCommandQueue.getParentDevice().getDeviceID(), parentCommandQueue.getName(), commandId).execute();
        if (!response.isSuccessful())
            CPSErrorParser.throwCpsException(response.errorBody(), response.code());
    }

    /**
     * Пометить команду как выполненную (изменяется состояние)
     *
     * @param result результат выполнения команды
     */
    @WorkerThread
    public void markExecuted(Object result) throws IOException, BaseCpsException {
        Response<BaseResponse> response = api.markExecuted(parentCommandQueue.getParentDevice().getDeviceID(), parentCommandQueue.getName(), commandId, result).execute();
        if (!response.isSuccessful())
            CPSErrorParser.throwCpsException(response.errorBody(), response.code());
    }
}
