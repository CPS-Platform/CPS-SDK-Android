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

import ru.ctvt.cps.sdk.errorprocessing.authentication.AuthenticationException;
import ru.ctvt.cps.sdk.errorprocessing.authorization.AuthorizationException;
import ru.ctvt.cps.sdk.errorprocessing.device_registration.DeviceRegistrationException;
import ru.ctvt.cps.sdk.errorprocessing.image_processing.ImageProcessingException;
import ru.ctvt.cps.sdk.errorprocessing.onetime_password.OnetimePasswordException;

/**
 * Базовый класс ошибок платформы
 * Created by Nokolya on 24.04.2017.
 */

public class BaseCpsException extends Exception {


    protected final static int EXCEPTION_CODE_BASE = 0;
    /**
     * префикс кода базовых ошибок
     */
    protected final static int EXCEPTION_PREFIX_BASE = 1000;
    /**
     * префикс кода общих ошибок аутентификации
     */
    protected final static int EXCEPTION_PREFIX_AUTHENTICATION = 1100;
    /**
     * префикс кода общих ошибок авторизации
     */
    protected final static int EXCEPTION_PREFIX_AUTHORIZATION = 1200;
    /**
     * префикс кода ошибок одноразовых паролей
     */
    protected final static int EXCEPTION_PREFIX_ONETIME_PASSWORD = 1300;
    /**
     * префикс кода ошибок аутентификации этого проекта
     */
    protected final static int EXCEPTION_PREFIX_AUTHENTICATION_2 = 2100;
    /**
     * префикс кода ошибок авторизации этого проекта
     */
    protected final static int EXCEPTION_PREFIX_AUTHORIZATION_2 = 2200;
    /**
     * префикс кода ошибок регистрации устройств
     */
    protected final static int EXCEPTION_PREFIX_DEVICE_REGISTRATION = 3000;
    /**
     * префикс кода ошибок обработки изображений
     */
    protected final static int EXCEPTION_PREFIX_IMAGE_PROCESSING = 3100;
    /**
     * код ошибки resource_or_page_not_found
     */
    protected final static int EXCEPTION_POSTFIX_RESOURCE_NOT_FOUND = 1;
    /**
     * код ошибки object_not_found
     */
    protected final static int EXCEPTION_POSTFIX_OBJECT_NOT_FOUND = 2;
    /**
     * код ошибки malformed_object
     */
    protected final static int EXCEPTION_POSTFIX_MALFORMED_OBJECT_ID= 3;
    /**
     * код ошибки object_already_exists
     */
    protected final static int EXCEPTION_POSTFIX_OBJECT_ALREADY_EXISTS = 4;
    /**
     * код ошибки bad_request
     */
    protected final static int EXCEPTION_POSTFIX_BAD_REQUEST = 5;
    /**
     * код ошибки internal_server_error
     */
    protected final static int EXCEPTION_POSTFIX_UNEXPECTED_INTERNAL_SERVER_ERROR = 6;
    /**
     * код ошибки unforseen_consequences
     */
    protected final static int EXCEPTION_POSTFIX_UNFORESEEN_CONSEQUENCES = 7;
    /**
     * код ошибки missing_argument
     */
    protected final static int EXCEPTION_POSTFIX_MISSING_ARGUMENT = 8;
    /**
     * код ошибки malformed_json
     */
    protected final static int EXCEPTION_POSTFIX_MALFORMED_JSON = 9;
    /**
     * код ошибки json_validation_failed
     */
    protected final static int EXCEPTION_POSTFIX_JSON_VALIDATION_FAILED = 10;
    /**
     * код ошибки service_downtime
     */
    protected final static int EXCEPTION_POSTFIX_SERVICE_DOWNTIME = 11;
    /**
     * код ошибки service_unavailable
     */
    protected final static int EXCEPTION_POSTFIX_SERVICE_UNAVAILABLE= 12;

    /**
     * код ошибки network errorprocessing
     */
    protected final static int EXCEPTION_POSTFIX_NETWORK_EXCEPTION = 70;

    /**
     * код ошибки "Ошибка клиента"
     */
    protected final static int EXCEPTION_POSTFIX_CLIENT_ERROR= 71;

    /**
     * сообщение по умолчанию для ошибки resource_or_page_not_found
     */
    protected final static String EXCEPTION_STRING_RESOURCE_NOT_FOUND = "Resource or page not found";
    /**
     * сообщение по умолчанию для ошибки object_not_found
     */
    protected final static String EXCEPTION_STRING_OBJECT_NOT_FOUND = "Unknown {} {}";
    /**
     * сообщение по умолчанию для ошибки malformed_object_id
     */
    protected final static String EXCEPTION_STRING_MALFORMED_OBJECT_ID= "Malformed {} identifier {}";
    /**
     * сообщение по умолчанию для ошибки object_already_exists
     */
    protected final static String EXCEPTION_STRING_OBJECT_ALREADY_EXISTS = "{} {} already exists";
    /**
     * сообщение по умолчанию для ошибки bad_request
     */
    protected final static String EXCEPTION_STRING_BAD_REQUEST = "Bad request";
    /**
     * сообщение по умолчанию для ошибки unexpected_internal_server_error
     */
    protected final static String EXCEPTION_STRING_UNEXPECTED_INTERNAL_SERVER_ERROR = "Unexpected internal server error";
    /**
     * сообщение по умолчанию для ошибки unforeseen_consequences
     */
    protected final static String EXCEPTION_STRING_UNFORESEEN_CONSEQUENCES = "Server cannot perform request because of unforeseen consequences";
    /**
     * сообщение по умолчанию для ошибки missing_argument
     */
    protected final static String EXCEPTION_STRING_MISSING_ARGUMENT = "Missing required argument";
    /**
     * сообщение по умолчанию для ошибки malformed_json
     */
    protected final static String EXCEPTION_STRING_MALFORMED_JSON = "Malformed json: {}";
    /**
     * сообщение по умолчанию для ошибки json_validation_failed
     */
    protected final static String EXCEPTION_STRING_JSON_VALIDATION_FAILED = "Json content validation failed: {}";
    /**
     * сообщение по умолчанию для ошибки service_downtime
     */
    protected final static String EXCEPTION_STRING_SERVICE_DOWNTIME = "Service unavailable, downtime until {}";
    /**
     * сообщение по умолчанию для ошибки service_unavailable
     */
    protected final static String EXCEPTION_STRING_SERVICE_UNAVAILABLE= "Service unavailable";

    /**
     * сообщение по умолчанию для ошибки network errorprocessing
     */
    protected final static String EXCEPTION_STRING_NETWORK_EXCEPTION= "no network";

    /**
     * сообщение по умолчанию для ошибки "Ошибка клиента"
     */
    protected final static String EXCEPTION_STRING_CLIENT_ERROR= "client error";

    /**
     * старшие разряды кода ошибок (определяет верхний уровень иерархии)
     */
    protected int error_code_prefix;
    /**
     * младшие разряды кода ошибки (детализируют исключение)
     */
    protected int error_code_postfix;

    /**
     * код ответа сервера
     */
    protected int response_code;

    /**
     * код ошибки, соответствующий коду ошибки на сервере, вызвавшей исключение
     * @return код ошибки
     */
    public int getErrorCode(){
        //код ошибки состоит из префикса и постфикса (так надо для генерации)
        return error_code_prefix+error_code_postfix;
    }

    /**
     * получить код заголовка ответа сервера
     * @return код ответа
     */
    public int getResponseCode(){
        return response_code;
    }

    /**
     * устагновить код заголовка ответа сервера
     * @param code - код ответа
     */
    public void setResponseCode(int code){
        if(     (code >= 100 && code <= 102) ||
                (code >= 200 && code <= 207) ||
                (code == 226)||
                (code >= 300 && code <= 307) ||
                (code >= 400 && code <= 417) ||
                (code >= 422 && code <= 426) ||
                (code >= 428 && code <= 429) ||
                (code == 431)||
                (code == 444)||
                (code == 449)||
                (code == 451)||
                (code >= 500 && code <= 511)||
                (code >= 520 && code <= 526)
                ){
            this.response_code = code;
        }
    }

    public BaseCpsException(){
        super();
        response_code = 400;
        error_code_prefix = EXCEPTION_CODE_BASE;
        error_code_postfix = EXCEPTION_CODE_BASE;
    }

    public BaseCpsException(String message){
        super(message);
        response_code = 400;
        error_code_prefix = EXCEPTION_CODE_BASE;
        error_code_postfix = EXCEPTION_CODE_BASE;
    }

    public BaseCpsException(String message,  Throwable cause){
        super(message, cause);
        response_code = 400;
        error_code_prefix = EXCEPTION_CODE_BASE;
        error_code_postfix = EXCEPTION_CODE_BASE;
    }

    public BaseCpsException(Throwable cause){
        super(cause);
        response_code = 400;
        error_code_prefix = EXCEPTION_CODE_BASE;
        error_code_postfix = EXCEPTION_CODE_BASE;
    }

    public BaseCpsException(String message, int errorCode){
        super(message);
        response_code = 400;
        int prefix = errorCode/100;
        prefix *= 100;
        int postfix = errorCode - prefix;
        error_code_prefix = prefix;
        error_code_postfix = postfix;
    }

    public BaseCpsException(String message, int errorCode, Throwable cause){
        super(message, cause);
        response_code = 400;
        int prefix = errorCode/100;
        prefix *= 100;
        int postfix = errorCode - prefix;
        error_code_prefix = prefix;
        error_code_postfix = postfix;
    }

    public BaseCpsException(int errorCode, Throwable cause){
        this("", errorCode, cause);
    }

    public BaseCpsException(int errorCode){
        this("", errorCode);
    }


    /**
     * Возвращает объект исключения, соответствующий по классу коду ошибки
     * @param message - сообщение об ошибке
     * @param errorCode - код ошибки
     * @return
     */
    public static BaseCpsException createCpsException(String message, int errorCode){
        return createCpsException(message, errorCode, null);
    }

    /**
     * Возвращает объект исключения, соответствующий по классу коду ошибки
     * @param message - сообщение об ошибке
     * @param errorCode - код ошибки
     * @param cause
     * @return
     */
    public static BaseCpsException createCpsException(String message, int errorCode, Throwable cause){

        int prefix = errorCode/100;
        prefix *= 100;
        int postfix = errorCode - prefix;

        switch (prefix){
            case EXCEPTION_PREFIX_BASE:
                switch (postfix){
                    case EXCEPTION_POSTFIX_RESOURCE_NOT_FOUND:
                        return cause == null? new ResourceNotFoundException(message): new ResourceNotFoundException(message, cause);
                    case EXCEPTION_POSTFIX_OBJECT_NOT_FOUND:
                        return cause == null? new ObjectNotFoundException(message): new ObjectNotFoundException(message, cause);
                    case EXCEPTION_POSTFIX_MALFORMED_OBJECT_ID:
                        return cause == null? new MalformedObjectIdException(message): new MalformedObjectIdException(message, cause);
                    case EXCEPTION_POSTFIX_OBJECT_ALREADY_EXISTS:
                        return cause == null? new ObjectAlreadyExistsException(message): new ObjectAlreadyExistsException(message, cause);
                    case EXCEPTION_POSTFIX_BAD_REQUEST:
                        return cause == null? new BadRequestException(message): new BadRequestException(message, cause);
                    case EXCEPTION_POSTFIX_UNEXPECTED_INTERNAL_SERVER_ERROR:
                        return cause == null? new UnexpectedInternalServerError(message): new UnexpectedInternalServerError(message, cause);
                    case EXCEPTION_POSTFIX_UNFORESEEN_CONSEQUENCES:
                        return cause == null? new UnforeseenConsequencesException(message): new UnforeseenConsequencesException(message, cause);
                    case EXCEPTION_POSTFIX_MISSING_ARGUMENT:
                        return cause == null? new MissingArgumentException(message): new MissingArgumentException(message, cause);
                    case EXCEPTION_POSTFIX_MALFORMED_JSON:
                        return cause == null? new MalformedJsonException(message): new MalformedJsonException(message, cause);
                    case EXCEPTION_POSTFIX_JSON_VALIDATION_FAILED:
                        return cause == null? new JsonValidationFailedException(message): new JsonValidationFailedException(message, cause);
                    case EXCEPTION_POSTFIX_SERVICE_DOWNTIME:
                        return cause == null? new ServiceDowntimeException(message): new ServiceDowntimeException(message, cause);
                    case EXCEPTION_POSTFIX_SERVICE_UNAVAILABLE:
                        return cause == null? new ServiceUnavailableException(message): new ServiceUnavailableException(message, cause);
                }
                break;
            case EXCEPTION_PREFIX_AUTHENTICATION:
            case EXCEPTION_PREFIX_AUTHENTICATION_2:
                return AuthenticationException.createAuthenticationException(message, errorCode, cause);
            case EXCEPTION_PREFIX_AUTHORIZATION:
            case  EXCEPTION_PREFIX_AUTHORIZATION_2:
                return AuthorizationException.createAuthorizationException(message, errorCode, cause);
            case EXCEPTION_PREFIX_ONETIME_PASSWORD:
                return OnetimePasswordException.createOnetimePasswordnException(message, errorCode, cause);
            case EXCEPTION_PREFIX_DEVICE_REGISTRATION:
                return DeviceRegistrationException.createDeviceRegistrationException(message, errorCode, cause);
            case EXCEPTION_PREFIX_IMAGE_PROCESSING:
                return ImageProcessingException.createImageProcessingException(message, errorCode, cause);
        }


        return cause == null? new BaseCpsException(message, errorCode) : new BaseCpsException(message, errorCode, cause);
    }


    /**
     * Статический метод, позволяющий получить код ошибки, соответствующей данному классу исключения
     * @return - код ошибки
     */
    public static int getExpectedErrorCode(){
        return EXCEPTION_PREFIX_BASE;
    }

    /**
     * Статический метод, позволяющий понять, может ли указанный код ошибки соответствовать данному классу исключений
     * @param errorCode - код ошибки с сервера
     * @return
     */
    public static boolean isErrorCodeAllowed(int errorCode){

        //коды ошибок - четырехзначные числа
        return errorCode >= 1000 && errorCode < 10000;

    }




}
