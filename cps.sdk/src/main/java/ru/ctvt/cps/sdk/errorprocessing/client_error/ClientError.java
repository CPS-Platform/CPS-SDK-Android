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

import ru.ctvt.cps.sdk.errorprocessing.BaseCpsException;
import com.google.common.base.Strings;

/**
 * 1071
 * unexpected_internal_server_error
 * ошибка клиента
 * Created by Nokolya on 19.05.2017.
 */

public class ClientError extends BaseCpsException {

    /**
     * не авторизован
     */
    protected final static int RESPONSE_CODE_UNAUTHORIZED = 401;
    /**
     * необходима оплата
     */
    protected final static int RESPONSE_CODE_PAYMENT_REQUIRED = 402;
    /**
     * метод не поддерживается
     */
    protected final static int RESPONSE_CODE_METHOD_NOT_ALLOWED = 405;
    /**
     * неприемлемо
     */
    protected final static int RESPONSE_CODE_NOT_ACCEPTABLE = 406;
    /**
     * необходима аутентификация прокси
     */
    protected final static int RESPONSE_CODE_PROXY_AUTHENTICATION_REQUIRED = 407;
    /**
     * истекло время ожидания
     */
    protected final static int RESPONSE_CODE_REQUEST_TIMEOUT = 408;
    /**
     * конфликт
     */
    protected final static int RESPONSE_CODE_CONFLICT = 409;
    /**
     * удалён
     */
    protected final static int RESPONSE_CODE_GONE = 410;
    /**
     * необходима длина
     */
    protected final static int RESPONSE_CODE_LENGTH_REQUIRED = 411;
    /**
     * условие ложно
     */
    protected final static int RESPONSE_CODE_PRECONDITION_FAILED = 412;
    /**
     * размер запроса слишком велик
     */
    protected final static int RESPONSE_CODE_REQUEST_ENTITY_TOO_LARGE = 413;
    /**
     * запрашиваемый URI слишком длинный
     */
    protected final static int RESPONSE_CODE_REQUEST_URL_TOO_LARGE = 414;
    /**
     * неподдерживаемый тип данных
     */
    protected final static int RESPONSE_CODE_UNSUPPORTED_MEDIA_TYPE = 415;
    /**
     * запрашиваемый диапазон не достижим
     */
    protected final static int RESPONSE_CODE_REQUESTED_RANGE_NOT_SATISFIABLE = 416;
    /**
     * ожидаемое неприемлемо
     */
    protected final static int RESPONSE_CODE_EXPECTATION_FAILED = 417;
    /**
     * необрабатываемый экземпляр
     */
    protected final static int RESPONSE_CODE_UNPROCESSABLE_ENTITY = 422;
    /**
     * заблокировано
     */
    protected final static int RESPONSE_CODE_LOCKED = 423;
    /**
     * невыполненная зависимость
     */
    protected final static int RESPONSE_CODE_FAILED_DEPENDENCY = 424;
    /**
     * неупорядоченный набор
     */
    protected final static int RESPONSE_CODE_UNORDERED_COLLECTION = 425;
    /**
     * необходимо обновление
     */
    protected final static int RESPONSE_CODE_UPGRADE_REQUIRED = 426;
    /**
     * необходимо предусловие
     */
    protected final static int RESPONSE_CODE_PRECONDITION_REQUIRED = 428;
    /**
     * слишком много запросов
     */
    protected final static int RESPONSE_CODE_TOO_MANY_REQUESTS = 429;
    /**
     * поля заголовка запроса слишком большие
     */
    protected final static int RESPONSE_CODE_REQUEST_HEADER_FIELDS_TOO_LARGE = 431;
    /**
     * Закрывает соединение без передачи заголовка ответа. Нестандартный код
     */
    protected final static int RESPONSE_CODE_NO_RESPONSE = 444;
    /**
     * повторить с
     */
    protected final static int RESPONSE_CODE_RETRY_WITH = 449;
    /**
     * недоступно по юридическим причинам
     */
    protected final static int RESPONSE_CODE_UNAVAILABLE_FOR_LEGAL_REASONS = 451;


    public ClientError(){
        super(EXCEPTION_STRING_CLIENT_ERROR);
        error_code_prefix = EXCEPTION_PREFIX_BASE;
        error_code_postfix = EXCEPTION_POSTFIX_CLIENT_ERROR;
    }

    public ClientError(String message){
        super(Strings.isNullOrEmpty(message)? EXCEPTION_STRING_CLIENT_ERROR: message);
        error_code_prefix = EXCEPTION_PREFIX_BASE;
        error_code_postfix = EXCEPTION_POSTFIX_CLIENT_ERROR;
    }

    public ClientError(String message, Throwable cause){
        super(Strings.isNullOrEmpty(message)? EXCEPTION_STRING_CLIENT_ERROR: message, cause);
        error_code_prefix = EXCEPTION_PREFIX_BASE;
        error_code_postfix = EXCEPTION_POSTFIX_CLIENT_ERROR;
    }

    public ClientError(Throwable cause){
        super(EXCEPTION_STRING_CLIENT_ERROR, cause);
        error_code_prefix = EXCEPTION_PREFIX_BASE;
        error_code_postfix = EXCEPTION_POSTFIX_CLIENT_ERROR;
    }


    public ClientError(String message, int responseCode, Throwable cause){
        super(Strings.isNullOrEmpty(message)? EXCEPTION_STRING_CLIENT_ERROR: message, responseCode, cause);

        error_code_prefix = EXCEPTION_PREFIX_BASE;
        error_code_postfix = EXCEPTION_POSTFIX_CLIENT_ERROR;

        this.response_code = responseCode;
    }

    public ClientError(String message, int responseCode){
        super(Strings.isNullOrEmpty(message)? EXCEPTION_STRING_CLIENT_ERROR: message, responseCode);

        error_code_prefix = EXCEPTION_PREFIX_BASE;
        error_code_postfix = EXCEPTION_POSTFIX_CLIENT_ERROR;

        this.response_code = responseCode;
    }




    /**
     * Статический метод, позволяющий получить код ошибки, соответствующей данному классу исключения
     * @return - код ошибки
     */
    public static int getExpectedErrorCode(){
        return EXCEPTION_PREFIX_BASE;
    }

    /**
     * соответствует ли тип исключения коду ответа
     * @param responseCode - код
     */
    public static boolean isResponseCodeAllowed(int responseCode){
        return
                (responseCode >= 400 && responseCode <= 417) ||
                (responseCode >= 422 && responseCode <= 426) ||
                (responseCode >= 428 && responseCode <= 429) ||
                (responseCode == 431)||
                (responseCode == 444)||
                (responseCode == 449)||
                (responseCode == 451);
    }


    /**
     * Возвращает объект исключения, соответствующий по классу коду ошибки в иерархии ClientError
     * @param message - сообщение об ошибке
     * @param responseCode - код ответа сервера
     */
    public static ClientError createClientError(String message, int responseCode){
        return createClientError(message, responseCode, null);
    }

    /**
     * Возвращает объект исключения, соответствующий по классу коду ошибки в иерархии ClientError
     * @param message сообщение об ошибке
     * @param responseCode код ответа сервера
     * @param cause объект исключения
     */
    public static ClientError createClientError(String message, int responseCode, Throwable cause){

        switch (responseCode){
            case RESPONSE_CODE_UNAUTHORIZED:
                return cause == null?  new UnauthorizedException(message) : new UnauthorizedException(message, cause);
            case RESPONSE_CODE_PAYMENT_REQUIRED:
                return cause == null?  new PaymentRequiredException(message) : new PaymentRequiredException(message, cause);
            case RESPONSE_CODE_METHOD_NOT_ALLOWED:
                return cause == null?  new MethodNotAllowedException(message) : new MethodNotAllowedException(message, cause);
            case RESPONSE_CODE_NOT_ACCEPTABLE:
                return cause == null?  new NotAcceptableException(message) : new NotAcceptableException(message, cause);
            case RESPONSE_CODE_PROXY_AUTHENTICATION_REQUIRED:
                return cause == null?  new ProxyAuthenticationRequiredException(message) : new ProxyAuthenticationRequiredException(message, cause);
            case RESPONSE_CODE_REQUEST_TIMEOUT:
                return cause == null?  new RequestTimeoutException(message) : new RequestTimeoutException(message, cause);
            case RESPONSE_CODE_CONFLICT:
                return cause == null?  new ConflictException(message) : new ConflictException(message, cause);
            case RESPONSE_CODE_GONE:
                return cause == null?  new GoneException(message) : new GoneException(message, cause);
            case RESPONSE_CODE_LENGTH_REQUIRED:
                return cause == null?  new LengthRequiredException(message) : new LengthRequiredException(message, cause);
            case RESPONSE_CODE_PRECONDITION_FAILED:
                return cause == null?  new PreconditionFailedException(message) : new PreconditionFailedException(message, cause);
            case RESPONSE_CODE_REQUEST_ENTITY_TOO_LARGE:
                return cause == null?  new RequestEntityTooLargeException(message) : new RequestEntityTooLargeException(message, cause);
            case RESPONSE_CODE_REQUEST_URL_TOO_LARGE:
                return cause == null?  new RequestURLTooLargeException(message) : new RequestURLTooLargeException(message, cause);
            case RESPONSE_CODE_UNSUPPORTED_MEDIA_TYPE:
                return cause == null?  new UnsupportedMediaTypeException(message) : new UnsupportedMediaTypeException(message, cause);
            case RESPONSE_CODE_REQUESTED_RANGE_NOT_SATISFIABLE:
                return cause == null?  new RequestedRangeNotSatisfiableException(message) : new RequestedRangeNotSatisfiableException(message, cause);
            case RESPONSE_CODE_EXPECTATION_FAILED:
                return cause == null?  new ExpectationFailedException(message) : new ExpectationFailedException(message, cause);
            case RESPONSE_CODE_UNPROCESSABLE_ENTITY:
                return cause == null?  new UnprocessableEntityException(message) : new UnprocessableEntityException(message, cause);
            case RESPONSE_CODE_LOCKED:
                return cause == null?  new LockedException(message) : new LockedException(message, cause);
            case RESPONSE_CODE_FAILED_DEPENDENCY:
                return cause == null?  new FailedDependencyException(message) : new FailedDependencyException(message, cause);
            case RESPONSE_CODE_UNORDERED_COLLECTION:
                return cause == null?  new UnorderedCollectionException(message) : new UnorderedCollectionException(message, cause);
            case RESPONSE_CODE_UPGRADE_REQUIRED:
                return cause == null?  new UpgradeRequiredException(message) : new UpgradeRequiredException(message, cause);
            case RESPONSE_CODE_PRECONDITION_REQUIRED:
                return cause == null?  new PreconditionRequiredException(message) : new PreconditionRequiredException(message, cause);
            case RESPONSE_CODE_TOO_MANY_REQUESTS:
                return cause == null?  new TooManyRequestsException(message) : new TooManyRequestsException(message, cause);
            case RESPONSE_CODE_REQUEST_HEADER_FIELDS_TOO_LARGE:
                return cause == null?  new RequestHeaderFieldsTooLargeException(message) : new RequestHeaderFieldsTooLargeException(message, cause);
            case RESPONSE_CODE_NO_RESPONSE:
                return cause == null?  new NoResponseException(message) : new NoResponseException(message, cause);
            case RESPONSE_CODE_RETRY_WITH:
                return cause == null?  new RetryWithException(message) : new RetryWithException(message, cause);
            case RESPONSE_CODE_UNAVAILABLE_FOR_LEGAL_REASONS:
                return cause == null?  new UnavailableForLegalReasonsException(message) : new UnavailableForLegalReasonsException(message, cause);
        }

        return cause == null?  new ClientError(message, responseCode) : new ClientError(message, responseCode, cause);
    }


}
