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

package ru.ctvt.cps.sdk.errorprocessing.authorization;

import com.google.common.base.Strings;

/**
 * 2201
 * not_a_service_member
 * Когда пользователь проходит аутентификацию, но не является членом указанного сервиса
 * Created by Nokolya on 25.04.2017.
 */

public class NotAServiceNumberException extends AuthorizationException {

    private void init(){
        error_code_prefix = EXCEPTION_PREFIX_AUTHORIZATION_2;
        error_code_postfix = EXCEPTION_POSTFIX_NOT_A_SERVICE_NUMBER;
    }

    public NotAServiceNumberException(){
        super(EXCEPTION_STRING_NOT_A_SERVICE_NUMBER);
        init();
    }

    public NotAServiceNumberException(String message){
        super(Strings.isNullOrEmpty(message)? EXCEPTION_STRING_NOT_A_SERVICE_NUMBER: message);
        init();
    }

    public NotAServiceNumberException(String message, Throwable cause){
        super(Strings.isNullOrEmpty(message)? EXCEPTION_STRING_NOT_A_SERVICE_NUMBER: message, cause);
        init();
    }

    public NotAServiceNumberException(Throwable cause){
        super(EXCEPTION_STRING_NOT_A_SERVICE_NUMBER, cause);
        init();
    }

    /**
     * Статический метод, позволяющий получить код ошибки, соответствующей данному классу исключения
     * @return - код ошибки
     */
    public static int getExpectedErrorCode(){
        return EXCEPTION_PREFIX_AUTHORIZATION_2+EXCEPTION_POSTFIX_NOT_A_SERVICE_NUMBER;
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
