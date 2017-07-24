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

package ru.ctvt.cps.sdk.network;

import com.google.gson.annotations.SerializedName;

/**
 * Шаблон ответа сервера
 *
 * @param <T> тип данных в теле ответа
 */
public class BaseResponse<T> {
    @SerializedName("status")
    public String status;

    @SerializedName("code")
    public int errorCode;

    @SerializedName("message")
    public String errorMessage;

    @SerializedName("data")
    public T data;
}