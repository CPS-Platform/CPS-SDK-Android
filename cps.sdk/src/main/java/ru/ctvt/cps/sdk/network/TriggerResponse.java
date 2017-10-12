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

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Данные об одном триггере
 */
public class TriggerResponse {
    @SerializedName("parameter_values")
    public HashMap<String, Object> parameterValues;
    @SerializedName("trigger")
    public Trigger trigger;
    @SerializedName("name")
    public String name;
    @SerializedName("enabled")
    public boolean enabled;

    public class Trigger{
        @SerializedName("name")
        public String name;
        @SerializedName("service_id")
        public String serviceId;
    }
}
