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

package ru.ctvt.cps.sdk.errorprocessing;

import com.google.common.base.Strings;

/**
 * 1003
 * malformed_object_id
 * Когда идентификатор объекта или ресурса имеет неверный формат
 * Created by Nokolya on 24.04.2017.
 */

public class MalformedObjectIdException extends BaseCpsException {

    public MalformedObjectIdException(){
        super(EXCEPTION_STRING_MALFORMED_OBJECT_ID);
        error_code_prefix = EXCEPTION_PREFIX_BASE;
        error_code_postfix = EXCEPTION_POSTFIX_MALFORMED_OBJECT_ID;
    }

    public MalformedObjectIdException(String message){
        super(Strings.isNullOrEmpty(message)? EXCEPTION_STRING_MALFORMED_OBJECT_ID: message);
        error_code_prefix = EXCEPTION_PREFIX_BASE;
        error_code_postfix = EXCEPTION_POSTFIX_MALFORMED_OBJECT_ID;
    }

    public MalformedObjectIdException(String message, Throwable cause){
        super(Strings.isNullOrEmpty(message)? EXCEPTION_STRING_MALFORMED_OBJECT_ID: message, cause);
        error_code_prefix = EXCEPTION_PREFIX_BASE;
        error_code_postfix = EXCEPTION_POSTFIX_MALFORMED_OBJECT_ID;
    }

    public MalformedObjectIdException(Throwable cause){
        super(EXCEPTION_STRING_MALFORMED_OBJECT_ID, cause);
        error_code_prefix = EXCEPTION_PREFIX_BASE;
        error_code_postfix = EXCEPTION_POSTFIX_MALFORMED_OBJECT_ID;
    }

    /**
     * Статический метод, позволяющий получить код ошибки, соответствующей данному классу исключения
     * @return - код ошибки
     */
    public static int getExpectedErrorCode(){
        return EXCEPTION_PREFIX_BASE+EXCEPTION_POSTFIX_MALFORMED_OBJECT_ID;
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
