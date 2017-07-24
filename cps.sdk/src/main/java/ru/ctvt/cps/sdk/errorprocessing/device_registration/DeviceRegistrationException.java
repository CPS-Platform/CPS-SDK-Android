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

import ru.ctvt.cps.sdk.errorprocessing.BaseCpsException;
import com.google.common.base.Strings;

/**
 * 30xx
 * Ошибки регистрации устройств
 * Created by Nokolya on 26.04.2017.
 */

public class DeviceRegistrationException extends BaseCpsException {

    /**
     * сообщение по умолчанию для DeviceRegistrationException
     */
    protected final static String EXCEPTION_STRING_DEVICE_REGISTRATION = "device registration failed";

    /**
     * код ошибки registration_code_not_exists
     */
    protected final static int EXCEPTION_POSTFIX_REGISTRATION_CODE_NOT_EXISTS = 1;
    /**
     * код ошибки bad_registration_extra_secret
     */
    protected final static int EXCEPTION_POSTFIX_BAD_REGISTRATION_EXTRA_SECRET = 2;
    /**
     * код ошибки code_not_yet_assigned
     */
    protected final static int EXCEPTION_POSTFIX_CODE_NOT_YET_ASSIGNED = 3;
    /**
     * код ошибки code_already_taken
     */
    protected final static int EXCEPTION_POSTFIX_CODE_ALREADY_TAKEN = 4;


    /**
     * сообщение по умолчанию для ошибки registration_code_not_exists
     */
    protected final static String EXCEPTION_STRING_REGISTRATION_CODE_NOT_EXISTS = "This registration code does not exists";
    /**
     * сообщение по умолчанию для ошибки bad_registration_extra_secret
     */
    protected final static String EXCEPTION_STRING_BAD_REGISTRATION_EXTRA_SECRET = "Supplied secret field does not match with real secret";
    /**
     * сообщение по умолчанию для ошибки code_not_yet_assigned
     */
    protected final static String EXCEPTION_STRING_CODE_NOT_YET_ASSIGNED = "Device not yet assigned to this registration code";
    /**
     * сообщение по умолчанию для ошибки code_already_taken
     */
    protected final static String EXCEPTION_STRING_CODE_ALREADY_TAKEN = "This code already taken by other device";


    public DeviceRegistrationException(){
        super(EXCEPTION_STRING_DEVICE_REGISTRATION);
        error_code_prefix = EXCEPTION_PREFIX_DEVICE_REGISTRATION;
        error_code_postfix = EXCEPTION_PREFIX_BASE;
    }

    public DeviceRegistrationException(String message){
        super(Strings.isNullOrEmpty(message)? EXCEPTION_STRING_DEVICE_REGISTRATION : message);
        error_code_prefix = EXCEPTION_PREFIX_DEVICE_REGISTRATION;
        error_code_postfix = EXCEPTION_PREFIX_BASE;
    }

    public DeviceRegistrationException(String message, Throwable cause){
        super(Strings.isNullOrEmpty(message)? EXCEPTION_STRING_DEVICE_REGISTRATION : message, cause);
        error_code_prefix = EXCEPTION_PREFIX_DEVICE_REGISTRATION;
        error_code_postfix = EXCEPTION_PREFIX_BASE;
    }

    public DeviceRegistrationException(Throwable cause){
        super(EXCEPTION_STRING_DEVICE_REGISTRATION, cause);
        error_code_prefix = EXCEPTION_PREFIX_DEVICE_REGISTRATION;
        error_code_postfix = EXCEPTION_PREFIX_BASE;
    }


    public DeviceRegistrationException(String message, int errorCode, Throwable cause){
        super(Strings.isNullOrEmpty(message)? EXCEPTION_STRING_DEVICE_REGISTRATION : message, errorCode, cause);
        int prefix = errorCode/100;
        prefix *= 100;
        int postfix = errorCode - prefix;
        //для того, чтобы входить в указанное множество ошибок, у кода должен быть соответствующий префикс (первые два разряда)
        error_code_prefix = EXCEPTION_PREFIX_DEVICE_REGISTRATION;
        error_code_postfix = postfix;
    }


    public DeviceRegistrationException(String message, int errorCode){
        super(Strings.isNullOrEmpty(message)? EXCEPTION_STRING_DEVICE_REGISTRATION : message, errorCode);
        int prefix = errorCode/100;
        prefix *= 100;
        int postfix = errorCode - prefix;
        //для того, чтобы входить в указанное множество ошибок, у кода должен быть соответствующий префикс (первые два разряда)
        error_code_prefix = EXCEPTION_PREFIX_DEVICE_REGISTRATION;
        error_code_postfix = postfix;
    }

    /**
     * Возвращает объект исключения, соответствующий по классу коду ошибки в иерархии DeviceRegistrationException
     * @param message - сообщение об ошибке
     * @param errorCode - код ошибки
     * @return
     */
    public static DeviceRegistrationException createDeviceRegistrationException(String message, int errorCode){
        return createDeviceRegistrationException(message, errorCode, null);
    }

    /**
     * Возвращает объект исключения, соответствующий по классу коду ошибки в иерархии DeviceRegistrationException
     * @param message - сообщение об ошибке
     * @param errorCode - код ошибки
     * @param cause
     * @return
     */
    public static DeviceRegistrationException createDeviceRegistrationException(String message, int errorCode, Throwable cause){

        int prefix = errorCode/100;
        prefix *= 100;
        int postfix = errorCode - prefix;

        switch (postfix){
            case EXCEPTION_POSTFIX_REGISTRATION_CODE_NOT_EXISTS:
                return cause == null? new RegistrationCodeNotExistsException(message): new RegistrationCodeNotExistsException(message, cause);
            case EXCEPTION_POSTFIX_BAD_REGISTRATION_EXTRA_SECRET:
                return cause == null? new BadRegistrationExtraSecretException(message): new BadRegistrationExtraSecretException(message, cause);
            case EXCEPTION_POSTFIX_CODE_NOT_YET_ASSIGNED:
                return cause == null? new CodeNotYetAssignedException(message): new CodeNotYetAssignedException(message, cause);
            case EXCEPTION_POSTFIX_CODE_ALREADY_TAKEN:
                return cause == null? new CodeAlreadyTakenException(message): new CodeAlreadyTakenException(message, cause);
        }

        return  cause == null? new DeviceRegistrationException(message, errorCode) : new DeviceRegistrationException(message, errorCode, cause);
    }

    /**
     * Статический метод, позволяющий получить код ошибки, соответствующей данному классу исключения
     * @return - код ошибки
     */
    public static int getExpectedErrorCode(){
        return EXCEPTION_PREFIX_DEVICE_REGISTRATION;
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

        return prefix == EXCEPTION_PREFIX_DEVICE_REGISTRATION;
    }


}
