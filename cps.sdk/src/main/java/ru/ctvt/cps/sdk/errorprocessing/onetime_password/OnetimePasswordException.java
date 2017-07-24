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

package ru.ctvt.cps.sdk.errorprocessing.onetime_password;

import ru.ctvt.cps.sdk.errorprocessing.BaseCpsException;
import com.google.common.base.Strings;

/**
 * 13xx
 * ошибки одноразовых паролей
 * Created by Nokolya on 25.04.2017.
 */

public class OnetimePasswordException extends BaseCpsException {

    /**
     * сообщение по умолчанию для OnetimePasswordException
     */
    protected final static String EXCEPTION_STRING_ONETIME_PASSWORD = "onetime password error";

    /**
     * код ошибки invalid_onetime_password
     */
    protected final static int EXCEPTION_POSTFIX_INVALID_ONETIME_PASSWORD = 1;
    /**
     * код ошибки invalid_onetime_token
     */
    protected final static int EXCEPTION_POSTFIX_INVALID_ONETIME_TOKEN = 2;

    /**
     * сообщение по умолчанию для ошибки invalid_onetime_password
     */
    protected final static String EXCEPTION_STRING_INVALID_ONETIME_PASSWORD = "Invalid, expired or already redeemed onetime password";
    /**
     * сообщение по умолчанию для ошибки invalid_onetime_token
     */
    protected final static String EXCEPTION_STRING_INVALID_ONETIME_TOKEN = "{} token expired, invalid or already redeemed";


    public OnetimePasswordException(){
        super(EXCEPTION_STRING_ONETIME_PASSWORD);
        error_code_prefix = EXCEPTION_PREFIX_ONETIME_PASSWORD;
        error_code_postfix = EXCEPTION_PREFIX_BASE;
    }

    public OnetimePasswordException(String message){
        super(Strings.isNullOrEmpty(message)? EXCEPTION_STRING_ONETIME_PASSWORD : message);
        error_code_prefix = EXCEPTION_PREFIX_ONETIME_PASSWORD;
        error_code_postfix = EXCEPTION_PREFIX_BASE;
    }

    public OnetimePasswordException(String message, Throwable cause){
        super(Strings.isNullOrEmpty(message)? EXCEPTION_STRING_ONETIME_PASSWORD : message, cause);
        error_code_prefix = EXCEPTION_PREFIX_ONETIME_PASSWORD;
        error_code_postfix = EXCEPTION_PREFIX_BASE;
    }

    public OnetimePasswordException(Throwable cause){
        super(EXCEPTION_STRING_ONETIME_PASSWORD, cause);
        error_code_prefix = EXCEPTION_PREFIX_ONETIME_PASSWORD;
        error_code_postfix = EXCEPTION_PREFIX_BASE;
    }


    public OnetimePasswordException(String message, int errorCode, Throwable cause){
        super(Strings.isNullOrEmpty(message)? EXCEPTION_STRING_ONETIME_PASSWORD : message, errorCode, cause);
        int prefix = errorCode/100;
        prefix *= 100;
        int postfix = errorCode - prefix;
        //для того, чтобы входить в указанное множество ошибок, у кода должен быть соответствующий префикс (первые два разряда)
        error_code_prefix = EXCEPTION_PREFIX_ONETIME_PASSWORD;
        error_code_postfix = postfix;
    }

    public OnetimePasswordException(String message, int errorCode){
        super(Strings.isNullOrEmpty(message)? EXCEPTION_STRING_ONETIME_PASSWORD : message, errorCode);
        int prefix = errorCode/100;
        prefix *= 100;
        int postfix = errorCode - prefix;
        //для того, чтобы входить в указанное множество ошибок, у кода должен быть соответствующий префикс (первые два разряда)
        error_code_prefix = EXCEPTION_PREFIX_ONETIME_PASSWORD;
        error_code_postfix = postfix;
    }


    /**
     * Возвращает объект исключения, соответствующий по классу коду ошибки в иерархии OnetimePasswordnException
     * @param message - сообщение об ошибке
     * @param errorCode - код ошибки
     * @return
     */
    public static OnetimePasswordException createOnetimePasswordnException(String message, int errorCode){
        return  createOnetimePasswordnException(message, errorCode, null);
    }

    /**
     * Возвращает объект исключения, соответствующий по классу коду ошибки в иерархии OnetimePasswordnException
     * @param message - сообщение об ошибке
     * @param errorCode - код ошибки
     * @param cause
     * @return
     */
    public static OnetimePasswordException createOnetimePasswordnException(String message, int errorCode, Throwable cause){

        int prefix = errorCode/100;
        prefix *= 100;
        int postfix = errorCode - prefix;

        switch (postfix){

            case EXCEPTION_POSTFIX_INVALID_ONETIME_PASSWORD:
                return cause == null? new InvalidOnetimePasswordException(message): new InvalidOnetimePasswordException(message, cause);
            case EXCEPTION_POSTFIX_INVALID_ONETIME_TOKEN:
                return cause == null? new InvalidOnetimeTokenException(message): new InvalidOnetimeTokenException(message, cause);

        }

        return  cause == null? new OnetimePasswordException(message, errorCode) : new OnetimePasswordException(message, errorCode, cause);
    }


    /**
     * Статический метод, позволяющий получить код ошибки, соответствующей данному классу исключения
     * @return - код ошибки
     */
    public static int getExpectedErrorCode(){
        return EXCEPTION_PREFIX_ONETIME_PASSWORD;
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

        return prefix == EXCEPTION_PREFIX_ONETIME_PASSWORD;
    }


}
