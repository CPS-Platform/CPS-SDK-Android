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

package ru.ctvt.cps.sdk.errorprocessing.device_registration;

import com.google.common.base.Strings;

/**
 * 3004
 * registration_code_already_taken
 * Когда другое устройство уже воспользовалось данным кодом
 * Created by Nokolya on 26.04.2017.
 */

public class CodeAlreadyTakenException extends DeviceRegistrationException {

    private void init(){
        error_code_postfix = EXCEPTION_POSTFIX_CODE_ALREADY_TAKEN;
    }

    public CodeAlreadyTakenException(){
        super(EXCEPTION_STRING_CODE_ALREADY_TAKEN);
        init();
    }

    public CodeAlreadyTakenException(String message){
        super(Strings.isNullOrEmpty(message)? EXCEPTION_STRING_CODE_ALREADY_TAKEN: message);
        init();
    }

    public CodeAlreadyTakenException(String message, Throwable cause){
        super(Strings.isNullOrEmpty(message)? EXCEPTION_STRING_CODE_ALREADY_TAKEN: message, cause);
        init();
    }

    public CodeAlreadyTakenException(Throwable cause){
        super(EXCEPTION_STRING_CODE_ALREADY_TAKEN, cause);
        init();
    }

    /**
     * Статический метод, позволяющий получить код ошибки, соответствующей данному классу исключения
     * @return - код ошибки
     */
    public static int getExpectedErrorCode(){
        return EXCEPTION_PREFIX_DEVICE_REGISTRATION+EXCEPTION_POSTFIX_CODE_ALREADY_TAKEN;
    }

    /**
     * Статический метод, позволяющий понять, может ли указанный код ошибки соответствовать данному классу исключений
     * @param errorCode - код ошибки с сервера
     * @return
     */
    public static boolean isErrorCodeAllowed(int errorCode){
        return errorCode == getExpectedErrorCode();
    }

}
