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

public class CommandQueueResponse
{
    @SerializedName("first_key")
    public String firstKey;

    @SerializedName("last_key")
    public String lastKey;

    @SerializedName("name")
    public String name;

    @SerializedName("service_id")
    public String serviceID;

    @SerializedName("owner_entity_id")
    public String ownerEntityID;

    @SerializedName("scope")
    public String scope;
}
