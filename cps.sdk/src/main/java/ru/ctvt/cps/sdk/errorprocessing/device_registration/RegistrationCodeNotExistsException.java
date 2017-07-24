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
 * 3001
 * registration_code_not_exists
 * Когда устройство прислало код регистрации, которого нет в базе
 * Created by Nokolya on 26.04.2017.
 */

public class RegistrationCodeNotExistsException extends DeviceRegistrationException {

    private void init(){
        error_code_postfix = EXCEPTION_POSTFIX_REGISTRATION_CODE_NOT_EXISTS;
    }

    public RegistrationCodeNotExistsException(){
        super(EXCEPTION_STRING_REGISTRATION_CODE_NOT_EXISTS);
        init();
    }

    public RegistrationCodeNotExistsException(String message){
        super(Strings.isNullOrEmpty(message)? EXCEPTION_STRING_REGISTRATION_CODE_NOT_EXISTS: message);
        init();
    }

    public RegistrationCodeNotExistsException(String message, Throwable cause){
        super(Strings.isNullOrEmpty(message)? EXCEPTION_STRING_REGISTRATION_CODE_NOT_EXISTS: message, cause);
        init();
    }

    public RegistrationCodeNotExistsException(Throwable cause){
        super(EXCEPTION_STRING_REGISTRATION_CODE_NOT_EXISTS, cause);
        init();
    }

    /**
     * Статический метод, позволяющий получить код ошибки, соответствующей данному классу исключения
     * @return - код ошибки
     */
    public static int getExpectedErrorCode(){
        return EXCEPTION_PREFIX_DEVICE_REGISTRATION+EXCEPTION_POSTFIX_REGISTRATION_CODE_NOT_EXISTS;
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
