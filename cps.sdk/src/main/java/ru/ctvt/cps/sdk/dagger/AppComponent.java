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

package ru.ctvt.cps.sdk.dagger;

import ru.ctvt.cps.sdk.model.AccountControl;
import ru.ctvt.cps.sdk.model.Command;
import ru.ctvt.cps.sdk.model.CommandQueue;
import ru.ctvt.cps.sdk.model.Device;
import ru.ctvt.cps.sdk.model.KeyValueStorage;
import ru.ctvt.cps.sdk.model.Trigger;
import ru.ctvt.cps.sdk.model.User;

import javax.inject.Singleton;

import dagger.Component;

@Component(modules = {ContextModule.class, NetworkModule.class})
@Singleton
public interface AppComponent {
    void inject(User x);
    void inject(KeyValueStorage x);
    void inject(AccountControl x);
    void inject(Device x);
    void inject(CommandQueue x);
    void inject(Command x);
    //void inject(Sequence x);
    void inject(ApiWrapper x);
    void inject(Trigger x);
}
