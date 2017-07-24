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

package ru.ctvt.cps.sdk.errorprocessing.authentication;

import com.google.common.base.Strings;

/**
 * 1131
 * oauth_access_denied_by_user
 * Когда при запросе разрешений через OAuth2 пользователь не дает запрошенных разрешений
 * Created by Nokolya on 25.04.2017.
 */

public class AccessDeniedByUserException extends AuthenticationException {

    private void init(){
        error_code_postfix = EXCEPTION_POSTFIX_OAUTH_ACCESS_DENIED_BY_USER;
    }

    public AccessDeniedByUserException(){
        super(EXCEPTION_STRING_OAUTH_ACCESS_DENIED_BY_USER);
        init();
    }

    public AccessDeniedByUserException(String message){
        super(Strings.isNullOrEmpty(message)? EXCEPTION_STRING_OAUTH_ACCESS_DENIED_BY_USER: message);
        init();
    }

    public AccessDeniedByUserException(String message, Throwable cause){
        super(Strings.isNullOrEmpty(message)? EXCEPTION_STRING_OAUTH_ACCESS_DENIED_BY_USER: message, cause);
        init();
    }

    public AccessDeniedByUserException(Throwable cause){
        super(EXCEPTION_STRING_OAUTH_ACCESS_DENIED_BY_USER, cause);
        init();
    }


    /**
     * Статический метод, позволяющий получить код ошибки, соответствующей данному классу исключения
     * @return - код ошибки
     */
    public static int getExpectedErrorCode(){
        return EXCEPTION_PREFIX_AUTHENTICATION+EXCEPTION_POSTFIX_OAUTH_ACCESS_DENIED_BY_USER;
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
