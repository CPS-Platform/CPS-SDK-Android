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

import ru.ctvt.cps.sdk.errorprocessing.BaseCpsException;
import com.google.common.base.Strings;

/**
 * 11xx
 * 21xx
 * когда не прошла аутентификация
 * Created by Nokolya on 25.04.2017.
 */

public class AuthenticationException extends BaseCpsException {

    /**
     * сообщение по умолчанию для AutentificationException
     */
    protected final static String EXCEPTION_STRING_AUTHENTICATION = "authentication failed";

    /**
     * код ошибки invalid_emeil
     */
    protected final static int EXCEPTION_POSTFIX_INVALID_EMAIL = 1;
    /**
     * код ошибки week_password
     */
    protected final static int EXCEPTION_POSTFIX_WEAK_PASSWORD = 2;
    /**
     * код ошибки email_already_taken
     */
    protected final static int EXCEPTION_POSTFIX_EMAIL_ALREADY_TAKEN = 3;
    /**
     * код ошибки invalid_emeil_or_password
     */
    protected final static int EXCEPTION_POSTFIX_INVALID_EMAIL_OR_PASSWORD = 4;
    /**
     * код ошибки password_mismatch
     */
    protected final static int EXCEPTION_POSTFIX_PASSWORD_MISMATCH = 6;
    /**
     * код ошибки email_not_found
     */
    protected final static int EXCEPTION_POSTFIX_EMAIL_NOT_FOUND = 7;
    /**
     * код ошибки email_not_confirmed
     */
    protected final static int EXCEPTION_POSTFIX_EMAIL_NOT_CONFIRMED = 8;
    /**
     * код ошибки oauth_access_denied_by_user
     */
    protected final static int EXCEPTION_POSTFIX_OAUTH_ACCESS_DENIED_BY_USER = 31;
    /**
     * код ошибки oauth_token_not_verified
     */
    protected final static int EXCEPTION_POSTFIX_OAUTH_TOKEN_NOT_VERIFIED = 32;
    /**
     * код ошибки oauth_retrieve_data_error
     */
    protected final static int EXCEPTION_POSTFIX_OAUTH_RETRIEVE_DATA_ERROR = 33;

    /**
     * код ошибки no_such_authentication_method
     */
    protected final static int EXCEPTION_POSTFIX_NO_SUCH_AUTHENTICATION_METHOD = 1;
    /**
     * код ошибки authentication_method_already_exists
     */
    protected final static int EXCEPTION_POSTFIX_AUTHENTICATION_METHOD_ALREADY_EXISTS = 2;
    /**
     * код ошибки authentication_already_taken
     */
    protected final static int EXCEPTION_POSTFIX_AUTHENTICATION_ALREADY_TAKEN = 3;

    /**
     * код ошибки wrong role
     */
    protected final static int EXCEPTION_POSTFIX_WRONG_ROLE = 51;

    /**
     * сообщение по умолчанию для ошибки invalid_email
     */
    protected final static String EXCEPTION_STRING_INVALID_EMAIL = "Invalid format of email {}";
    /**
     * сообщение по умолчанию для ошибки weak_password
     */
    protected final static String EXCEPTION_STRING_WEAK_PASSWORD = "Password is not strong enough, length 6 or more required";
    /**
     * сообщение по умолчанию для ошибки email_already_taken
     */
    protected final static String EXCEPTION_STRING_EMAIL_ALREADY_TAKEN = "Email {} already taken";
    /**
     * сообщение по умолчанию для ошибки invalid_email_or_password
     */
    protected final static String EXCEPTION_STRING_INVALID_EMAIL_OR_PASSWORD = "Invalid email or password";
    /**
     * сообщение по умолчанию для ошибки password_mismatch
     */
    protected final static String EXCEPTION_STRING_PASSWORD_MISMATCH = "Password mismatch";
    /**
     * сообщение по умолчанию для ошибки email_not_found
     */
    protected final static String EXCEPTION_STRING_EMAIL_NOT_FOUND = "Email {} is not registered in service";
    /**
     * сообщение по умолчанию для ошибки email_not_confirmed
     */
    protected final static String EXCEPTION_STRING_EMAIL_NOT_CONFIRMED = "User should confirm his {} email to perform operation";
    /**
     * сообщение по умолчанию для ошибки oauth_access_denied_by_user
     */
    protected final static String EXCEPTION_STRING_OAUTH_ACCESS_DENIED_BY_USER = "OAuth2 access denied by user";
    /**
     * сообщение по умолчанию для ошибки oauth_token_not_verified
     */
    protected final static String EXCEPTION_STRING_OAUTH_TOKEN_NOT_VERIFIED = "OAuth2 token is not verified: {}";
    /**
     * сообщение по умолчанию для ошибки oauth_retrieve_data_error
     */
    protected final static String EXCEPTION_STRING_OAUTH_RETRIEVE_DATA_ERROR = "Cannot retrieve {} via OAuth2: {}";

    /**
     * сообщение по умолчанию для ошибки no_such_authentication_method
     */
    protected final static String EXCEPTION_STRING_NO_SUCH_AUTHENTICATION_METHOD = "User does not set up {} authentication method";
    /**
     * сообщение по умолчанию для ошибки authentication_method_already_exists
     */
    protected final static String EXCEPTION_STRING_AUTHENTICATION_METHOD_ALREADY_EXISTS = "User already set up {} authentication method";
    /**
     * сообщение по умолчанию для ошибки authentication_already_taken
     */
    protected final static String EXCEPTION_STRING_AUTHENTICATION_ALREADY_TAKEN = "Authentication via {} already bound to different user";
    /**
     * сообщение по умолчанию для ошибки wrong role
     */
    protected final static String EXCEPTION_STRING_WRONG_ROLE = "wrong role";

    public AuthenticationException(){
        super(EXCEPTION_STRING_AUTHENTICATION);
        error_code_prefix = EXCEPTION_PREFIX_AUTHENTICATION;
        error_code_postfix = EXCEPTION_PREFIX_BASE;
    }

    public AuthenticationException(String message){
        super(Strings.isNullOrEmpty(message)? EXCEPTION_STRING_AUTHENTICATION: message);
        error_code_prefix = EXCEPTION_PREFIX_AUTHENTICATION;
        error_code_postfix = EXCEPTION_PREFIX_BASE;
    }

    public AuthenticationException(String message, Throwable cause){
        super(Strings.isNullOrEmpty(message)? EXCEPTION_STRING_AUTHENTICATION: message, cause);
        error_code_prefix = EXCEPTION_PREFIX_AUTHENTICATION;
        error_code_postfix = EXCEPTION_PREFIX_BASE;
    }

    public AuthenticationException(Throwable cause){
        super(EXCEPTION_STRING_AUTHENTICATION, cause);
        error_code_prefix = EXCEPTION_PREFIX_AUTHENTICATION;
        error_code_postfix = EXCEPTION_PREFIX_BASE;
    }

    public AuthenticationException(String message, int errorCode, Throwable cause){
        super(Strings.isNullOrEmpty(message)? EXCEPTION_STRING_AUTHENTICATION: message, errorCode, cause);
        int prefix = errorCode/100;
        prefix *= 100;
        int postfix = errorCode - prefix;
        //для того, чтобы входить в указанное множество ошибок, у кода должен быть соответствующий префикс (первые два разряда)
        error_code_prefix = EXCEPTION_PREFIX_AUTHENTICATION;
        error_code_postfix = postfix;
    }

    public AuthenticationException(String message, int errorCode){
        super(Strings.isNullOrEmpty(message)? EXCEPTION_STRING_AUTHENTICATION: message, errorCode);
        int prefix = errorCode/100;
        prefix *= 100;
        int postfix = errorCode - prefix;
        //для того, чтобы входить в указанное множество ошибок, у кода должен быть соответствующий префикс (первые два разряда)
        error_code_prefix = EXCEPTION_PREFIX_AUTHENTICATION;
        error_code_postfix = postfix;
    }

    /**
     * Возвращает объект исключения, соответствующий по классу коду ошибки в иерархии AuthenticationException
     * @param message - сообщение об ошибке
     * @param errorCode - код ошибки
     * @return
     */
    public static AuthenticationException createAuthenticationException(String message, int errorCode){
        return createAuthenticationException(message, errorCode, null);
    }

    /**
     * Возвращает объект исключения, соответствующий по классу коду ошибки в иерархии AuthenticationException
     * @param message - сообщение об ошибке
     * @param errorCode - код ошибки
     * @param cause
     * @return
     */
    public static AuthenticationException createAuthenticationException(String message, int errorCode, Throwable cause){

        int prefix = errorCode/100;
        prefix *= 100;
        int postfix = errorCode - prefix;

        switch (prefix){
            case EXCEPTION_PREFIX_AUTHENTICATION:
                switch (postfix){
                    case EXCEPTION_POSTFIX_INVALID_EMAIL:
                        return cause == null? new InvalidEmailException(message): new InvalidEmailException(message, cause);
                    case EXCEPTION_POSTFIX_WEAK_PASSWORD:
                        return cause == null? new WeakPasswordException(message): new WeakPasswordException(message, cause);
                    case EXCEPTION_POSTFIX_EMAIL_ALREADY_TAKEN:
                        return cause == null? new EmailAlreadyTakenException(message): new EmailAlreadyTakenException(message, cause);
                    case EXCEPTION_POSTFIX_INVALID_EMAIL_OR_PASSWORD:
                        return cause == null? new InvalidEmailOrPasswordException(message): new InvalidEmailOrPasswordException(message, cause);
                    case EXCEPTION_POSTFIX_PASSWORD_MISMATCH:
                        return cause == null? new PasswordMismatchException(message): new PasswordMismatchException(message, cause);
                    case EXCEPTION_POSTFIX_EMAIL_NOT_FOUND:
                        return cause == null? new EmailNotFoundException(message): new EmailNotFoundException(message, cause);
                    case EXCEPTION_POSTFIX_EMAIL_NOT_CONFIRMED:
                        return cause == null? new EmailNotConfirmedException(message): new EmailNotConfirmedException(message, cause);
                    case EXCEPTION_POSTFIX_OAUTH_ACCESS_DENIED_BY_USER:
                        return cause == null? new AccessDeniedByUserException(message): new AccessDeniedByUserException(message, cause);
                    case EXCEPTION_POSTFIX_OAUTH_TOKEN_NOT_VERIFIED:
                        return cause == null? new TokenNotVerifiedException(message): new TokenNotVerifiedException(message, cause);
                    case EXCEPTION_POSTFIX_OAUTH_RETRIEVE_DATA_ERROR:
                        return cause == null? new RetrieveDataError(message): new RetrieveDataError(message, cause);
                }
                break;
            case EXCEPTION_PREFIX_AUTHENTICATION_2:
                switch (postfix){
                    case EXCEPTION_POSTFIX_NO_SUCH_AUTHENTICATION_METHOD:
                        return cause == null? new NoSuchAuthenticationMethodException(message): new NoSuchAuthenticationMethodException(message, cause);
                    case EXCEPTION_POSTFIX_AUTHENTICATION_METHOD_ALREADY_EXISTS:
                        return cause == null? new AuthenticationMethodAlreadyExistsException(message): new AuthenticationMethodAlreadyExistsException(message, cause);
                    case EXCEPTION_POSTFIX_AUTHENTICATION_ALREADY_TAKEN:
                        return cause == null? new AuthenticationAlreadyTakenException(message): new AuthenticationAlreadyTakenException(message, cause);
                }
                break;
        }

        return cause == null?  new AuthenticationException(message, errorCode) : new AuthenticationException(message, errorCode, cause);
    }


    /**
     * Статический метод, позволяющий получить код ошибки, соответствующей данному классу исключения
     * @return - код ошибки
     */
    public static int getExpectedErrorCode(){
        return EXCEPTION_PREFIX_AUTHENTICATION;
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

        //у аутентификации два возможных префикса
        return prefix == EXCEPTION_PREFIX_AUTHENTICATION || prefix == EXCEPTION_PREFIX_AUTHENTICATION_2;
    }


}
