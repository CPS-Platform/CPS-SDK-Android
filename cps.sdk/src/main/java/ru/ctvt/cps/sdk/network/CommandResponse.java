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

import ru.ctvt.cps.sdk.model.Command;
import com.google.gson.annotations.SerializedName;

public class CommandResponse
{
    @SerializedName("command")
    public String command;

    @SerializedName("id")
    public String id;

    @SerializedName("state")
    public Command.State state;

    @SerializedName("payload")
    public Object payload;

    @SerializedName("command_result")
    public Object result;

    @SerializedName("state_changed_at")
    public String stateChangedAt;
}
