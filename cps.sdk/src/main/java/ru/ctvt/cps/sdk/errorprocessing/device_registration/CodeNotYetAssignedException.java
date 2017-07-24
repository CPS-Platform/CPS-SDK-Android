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
 * 3003
 * registration_code_not_yet_assigned
 * Когда регистрационному коду ещё не присвоено устройство
 * Created by Nokolya on 26.04.2017.
 */

public class CodeNotYetAssignedException extends DeviceRegistrationException {

    private void init(){
        error_code_postfix = EXCEPTION_POSTFIX_CODE_NOT_YET_ASSIGNED;
    }

    public CodeNotYetAssignedException(){
        super(EXCEPTION_STRING_CODE_NOT_YET_ASSIGNED);
        init();
    }

    public CodeNotYetAssignedException(String message){
        super(Strings.isNullOrEmpty(message)? EXCEPTION_STRING_CODE_NOT_YET_ASSIGNED: message);
        init();
    }

    public CodeNotYetAssignedException(String message, Throwable cause){
        super(Strings.isNullOrEmpty(message) ? EXCEPTION_STRING_CODE_NOT_YET_ASSIGNED: message, cause);
        init();
    }

    public CodeNotYetAssignedException(Throwable cause){
        super(EXCEPTION_STRING_CODE_NOT_YET_ASSIGNED, cause);
        init();
    }

    /**
     * Статический метод, позволяющий получить код ошибки, соответствующей данному классу исключения
     * @return - код ошибки
     */
    public static int getExpectedErrorCode(){
        return EXCEPTION_PREFIX_DEVICE_REGISTRATION+EXCEPTION_POSTFIX_CODE_NOT_YET_ASSIGNED;
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
