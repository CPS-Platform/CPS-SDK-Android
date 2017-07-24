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

package ru.ctvt.cps.sdk.errorprocessing.client_error;

import com.google.common.base.Strings;

/**
 * 405
 * метод не поддерживается
 * Created by Nokolya on 19.05.2017.
 */

public class MethodNotAllowedException extends ClientError {

    public MethodNotAllowedException(){
        super(EXCEPTION_STRING_CLIENT_ERROR);
    }

    public MethodNotAllowedException(String message){
        super(Strings.isNullOrEmpty(message)? EXCEPTION_STRING_CLIENT_ERROR: message);
    }

    public MethodNotAllowedException(String message, Throwable cause){
        super(Strings.isNullOrEmpty(message)? EXCEPTION_STRING_CLIENT_ERROR: message, cause);
    }

    public MethodNotAllowedException(Throwable cause){
        super(EXCEPTION_STRING_CLIENT_ERROR, cause);
    }

    public int getResponseCode(){
        return RESPONSE_CODE_METHOD_NOT_ALLOWED;
    }


}
