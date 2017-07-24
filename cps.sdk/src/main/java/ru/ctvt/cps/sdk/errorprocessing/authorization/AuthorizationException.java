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

import ru.ctvt.cps.sdk.errorprocessing.BaseCpsException;
import com.google.common.base.Strings;

/**
 * 12xx
 * 22xx
 * когда не прошла авторизация
 * Created by Nokolya on 25.04.2017.
 */

public class AuthorizationException extends BaseCpsException {

    /**
     * сообщение по умолчанию для AuthorizationException
     */
    protected final static String EXCEPTION_STRING_AUTHORIZATION = "authorization failed";

    /**
     * код ошибки authentication_required
     */
    protected final static int EXCEPTION_POSTFIX_AUTHENTICATION_REQUIRED = 1;
    /**
     * код ошибки authentication_failed
     */
    protected final static int EXCEPTION_POSTFIX_AUTHENTICATION_FAILED = 2;
    /**
     * код ошибки forbidden
     */
    protected final static int EXCEPTION_POSTFIX_FORBIDDEN = 3;


    /**
     * код ошибки not_a_service_number
     */
    protected final static int EXCEPTION_POSTFIX_NOT_A_SERVICE_NUMBER = 1;

    /**
     * сообщение по умолчанию для ошибки authentication_required
     */
    protected final static String EXCEPTION_STRING_AUTHENTICATION_REQUIRED = "Authentication required to perform request";
    /**
     * сообщение по умолчанию для ошибки authentication_failed
     */
    protected final static String EXCEPTION_STRING_AUTHENTICATION_FAILED = "Authentication failed: {}";
    /**
     * сообщение по умолчанию для ошибки forbidden
     */
    protected final static String EXCEPTION_STRING_FORBIDDEN = "Not authorized to perform request";
    /**
     * сообщение по умолчанию для ошибки not_a_service_number
     */
    protected final static String EXCEPTION_STRING_NOT_A_SERVICE_NUMBER = "User has to be a member of specified service to complete authentication";


    public AuthorizationException(){
        super(EXCEPTION_STRING_AUTHORIZATION);
        error_code_prefix = EXCEPTION_PREFIX_AUTHORIZATION;
        error_code_postfix = EXCEPTION_PREFIX_BASE;
    }

    public AuthorizationException(String message){
        super(Strings.isNullOrEmpty(message)? EXCEPTION_STRING_AUTHORIZATION : message);
        error_code_prefix = EXCEPTION_PREFIX_AUTHORIZATION;
        error_code_postfix = EXCEPTION_PREFIX_BASE;
    }

    public AuthorizationException(String message, Throwable cause){
        super(Strings.isNullOrEmpty(message)? EXCEPTION_STRING_AUTHORIZATION : message, cause);
        error_code_prefix = EXCEPTION_PREFIX_AUTHORIZATION;
        error_code_postfix = EXCEPTION_PREFIX_BASE;
    }

    public AuthorizationException(Throwable cause){
        super(EXCEPTION_STRING_AUTHORIZATION, cause);
        error_code_prefix = EXCEPTION_PREFIX_AUTHORIZATION;
        error_code_postfix = EXCEPTION_PREFIX_BASE;
    }

    public AuthorizationException(String message, int errorCode, Throwable cause){
        super(Strings.isNullOrEmpty(message)? EXCEPTION_STRING_AUTHORIZATION : message, errorCode, cause);
        int prefix = errorCode/100;
        prefix *= 100;
        int postfix = errorCode - prefix;
        //для того, чтобы входить в указанное множество ошибок, у кода должен быть соответствующий префикс (первые два разряда)
        error_code_prefix = EXCEPTION_PREFIX_AUTHORIZATION;
        error_code_postfix = postfix;
    }

    public AuthorizationException(String message, int errorCode){
        super(Strings.isNullOrEmpty(message)? EXCEPTION_STRING_AUTHORIZATION : message, errorCode);
        int prefix = errorCode/100;
        prefix *= 100;
        int postfix = errorCode - prefix;
        //для того, чтобы входить в указанное множество ошибок, у кода должен быть соответствующий префикс (первые два разряда)
        error_code_prefix = EXCEPTION_PREFIX_AUTHORIZATION;
        error_code_postfix = postfix;
    }

    /**
     * Возвращает объект исключения, соответствующий по классу коду ошибки в иерархии AuthorizationException
     * @param message - сообщение об ошибке
     * @param errorCode - код ошибки
     * @return
     */
    public static AuthorizationException createAuthorizationException(String message, int errorCode){
        return createAuthorizationException(message, errorCode, null);
    }

    /**
     * Возвращает объект исключения, соответствующий по классу коду ошибки в иерархии AuthorizationException
     * @param message - сообщение об ошибке
     * @param errorCode - код ошибки
     * @param cause
     * @return
     */
    public static AuthorizationException createAuthorizationException(String message, int errorCode, Throwable cause){

        int prefix = errorCode/100;
        prefix *= 100;
        int postfix = errorCode - prefix;

        switch (prefix){
            case EXCEPTION_PREFIX_AUTHORIZATION:
                switch (postfix){
                    case EXCEPTION_POSTFIX_AUTHENTICATION_REQUIRED:
                        return cause == null? new AuthenticationRequiredException(message): new AuthenticationRequiredException(message, cause);
                    case EXCEPTION_POSTFIX_AUTHENTICATION_FAILED:
                        return cause == null? new AuthenticationFailedException(message): new AuthenticationFailedException(message, cause);
                    case EXCEPTION_POSTFIX_FORBIDDEN:
                        return cause == null? new ForbiddenException(message): new ForbiddenException(message, cause);
                }
                break;
            case EXCEPTION_PREFIX_AUTHORIZATION_2:
                switch (postfix){
                    case EXCEPTION_POSTFIX_NOT_A_SERVICE_NUMBER:
                        return cause == null? new NotAServiceNumberException(message): new NotAServiceNumberException(message, cause);
                }
                break;
        }

        return cause == null? new AuthorizationException(message, errorCode) : new AuthorizationException(message, errorCode, cause);
    }

    /**
     * Статический метод, позволяющий получить код ошибки, соответствующей данному классу исключения
     * @return - код ошибки
     */
    public static int getExpectedErrorCode(){
        return EXCEPTION_PREFIX_AUTHORIZATION;
    }

    /**
     * Статический метод, позволяющий понять, может ли указанный код ошибки соответствовать данному классу исключений
     * @param errorCode - код ошибки с сервера
     * @return
     */
    public static boolean isErrorCodeAllowed(int errorCode){

        //для того, чтобы входить в указанное множество ошибок, у кода должен быть соответствующий префикс (первые два разряда)
        int prefix = errorCode/100;
        prefix *= 100;

        //у авторизации два возможных префикса
        return prefix == EXCEPTION_PREFIX_AUTHORIZATION || prefix == EXCEPTION_PREFIX_AUTHORIZATION_2;
    }


}
