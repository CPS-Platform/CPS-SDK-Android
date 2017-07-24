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

import ru.ctvt.cps.sdk.errorprocessing.authorization.ForbiddenException;
import ru.ctvt.cps.sdk.errorprocessing.client_error.ClientError;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Iterator;

import okhttp3.ResponseBody;

/**
 * Статический класс для обработки ответов сервера с ошибками
 * Created by Nokolya on 03.05.2017.
 */

public class CPSErrorParser  {

    /**
     * плохой, неверный запрос
     */
    private final static int RESPONSE_CODE_BAD_REQUEST = 400;
    /**
     * внутренняя ошибка сервера
     */
    private final static int RESPONSE_CODE_SERVER_INTERNAL_ERROR = 500;
    /**
     * не найдено
     */
    private final static int RESPONSE_CODE_PAGE_NOT_FOUND = 404;
    /**
     * запрещено
     */
    private final static int RESPONSE_CODE_FORBIDDEN = 403;

    /**
     * не авторизован
     */
    private final static int RESPONSE_CODE_UNAUTHORIZED = 401;
    /**
     * необходима оплата
     */
    private final static int RESPONSE_CODE_PAYMENT_REQUIRED = 402;
    /**
     * метод не поддерживается
     */
    private final static int RESPONSE_CODE_METHOD_NOT_ALLOWED = 405;
    /**
     * неприемлемо
     */
    private final static int RESPONSE_CODE_NOT_ACCEPTABLE = 406;
    /**
     * необходима аутентификация прокси
     */
    private final static int RESPONSE_CODE_PROXY_AUTHENTICATION_REQUIRED = 407;
    /**
     * истекло время ожидания
     */
    private final static int RESPONSE_CODE_REQUEST_TIMEOUT = 408;
    /**
     * конфликт
     */
    private final static int RESPONSE_CODE_CONFLICT = 409;
    /**
     * удалён
     */
    private final static int RESPONSE_CODE_GONE = 410;
    /**
     * необходима длина
     */
    private final static int RESPONSE_CODE_LENGTH_REQUIRED = 411;
    /**
     * условие ложно
     */
    private final static int RESPONSE_CODE_PRECONDITION_FAILED = 412;
    /**
     * размер запроса слишком велик
     */
    private final static int RESPONSE_CODE_REQUEST_ENTITY_TOO_LARGE = 413;
    /**
     * запрашиваемый URI слишком длинный
     */
    private final static int RESPONSE_CODE_REQUEST_URL_TOO_LARGE = 414;
    /**
     * еподдерживаемый тип данных
     */
    private final static int RESPONSE_CODE_UNSUPPORTED_MEDIA_TYPE = 415;
    /**
     * запрашиваемый диапазон не достижим
     */
    private final static int RESPONSE_CODE_REQUESTED_RANGE_NOT_SATISFIABLE = 416;
    /**
     * ожидаемое неприемлемо
     */
    private final static int RESPONSE_CODE_EXPECTATION_FAILED = 417;
    /**
     * необрабатываемый экземпляр
     */
    private final static int RESPONSE_CODE_UNPROCESSABLE_ENTITY = 422;
    /**
     * заблокировано
     */
    private final static int RESPONSE_CODE_LOCKED = 423;
    /**
     * невыполненная зависимость
     */
    private final static int RESPONSE_CODE_FAILED_DEPENDENCY = 424;
    /**
     * неупорядоченный набор
     */
    private final static int RESPONSE_CODE_UNORDERED_COLLECTION = 425;
    /**
     * необходимо обновление
     */
    private final static int RESPONSE_CODE_UPGRADE_REQUIRED = 426;
    /**
     * необходимо предусловие
     */
    private final static int RESPONSE_CODE_PRECONDITION_REQUIRED = 428;
    /**
     * слишком много запросов
     */
    private final static int RESPONSE_CODE_TOO_MANY_REQUESTS = 429;
    /**
     * поля заголовка запроса слишком большие
     */
    private final static int RESPONSE_CODE_REQUEST_HEADER_FIELDS_TOO_LARGE = 431;
    /**
     * Закрывает соединение без передачи заголовка ответа. Нестандартный код
     */
    private final static int RESPONSE_CODE_NO_RESPONSE = 444;
    /**
     * повторить с
     */
    private final static int RESPONSE_CODE_RETRY_WITH = 449;
    /**
     * недоступно по юридическим причинам
     */
    private final static int RESPONSE_CODE_UNAVAILABLE_FOR_LEGAL_REASONS = 451;


    /**
     * не реализовано
     */
    private final static int RESPONSE_CODE_NOT_IMPLEMENTED = 501;
    /**
     * плохой, ошибочный шлюз
     */
    private final static int RESPONSE_CODE_BAD_GATEWAY = 502;
    /**
     * сервис недоступен
     */
    private final static int RESPONSE_CODE_SERVICE_UNAVAILABLE = 503;
    /**
     * шлюз не отвечает
     */
    private final static int RESPONSE_CODE_GATEWAY_TIMEOUT = 504;
    /**
     * версия HTTP не поддерживается
     */
    private final static int RESPONSE_CODE_HTTP_VERSION_NOT_SUPPORTED = 505;
    /**
     * вариант тоже проводит согласование
     */
    private final static int RESPONSE_CODE_VARIANT_ALSO_NEGOTIATES = 506;
    /**
     * переполнение хранилища
     */
    private final static int RESPONSE_CODE_INSUFFICIENT_STORAGE = 507;
    /**
     * обнаружено бесконечное перенаправление
     */
    private final static int RESPONSE_CODE_LOOP_DETECTED = 508;
    /**
     * исчерпана пропускная ширина канала
     */
    private final static int RESPONSE_CODE_BANDWIDTH_LIMIT_EXCEEDED = 509;
    /**
     * не расширено
     */
    private final static int RESPONSE_CODE_NOT_EXTENDED = 510;
    /**
     * требуется сетевая аутентификация
     */
    private final static int RESPONSE_CODE_NETWORK_AUTHENTICATION_REQUIRED = 511;
    /**
     * возникает когда сервер CDN не смог обработать ошибку веб-сервера; нестандартный код CloudFlare
     */
    private final static int RESPONSE_CODE_WEB_SERVER_IS_RETURNING_UNKNOWN_ERROR = 520;
    /**
     *  возникает когда подключения CDN отклоняются веб-сервером; нестандартный код CloudFlare
     */
    private final static int RESPONSE_CODE_WEB_SERVER_IS_DOWN = 521;
    /**
     * возникает когда CDN не удалось подключиться к веб-серверу; нестандартный код CloudFlare
     */
    private final static int RESPONSE_CODE_CONNECTION_TIMED_OUT = 522;
    /**
     * возникает когда веб-сервер недостижим; нестандартный код CloudFlare
     */
    private final static int RESPONSE_CODE_ORIGIN_IS_UNREACHABLE = 523;
    /**
     * возникает при истечении таймаута подключения между сервером CDN и веб-сервером; нестандартный код CloudFlare
     */
    private final static int RESPONSE_CODE_A_TIMEOUT_OCCURRED = 524;
    /**
     * возникает при ошибке рукопожатия SSL между сервером CDN и веб-сервером; нестандартный код CloudFlare
     */
    private final static int RESPONSE_CODE_SSL_HANDSHAKE_FAILED = 525;
    /**
     * возникает когда не удаётся подтвердить сертификат шифрования веб-сервера; нестандартный код CloudFlare
     */
    private final static int RESPONSE_CODE_INVALID_SSL_CERTIFICATE = 526;


    /**
     * Разбирает ответ сервера и выбрасывает исключение, если произошла ошибка платформы
     * @param errorBody - тело ответа с описанием ошибки
     * @param responseCode - код заголовка ответа
     * @throws BaseCpsException
     */
    public static void throwCpsException(ResponseBody errorBody, int responseCode) throws BaseCpsException{


        switch(responseCode){

            /*
               Исключения, к которым привели различные ошибки внутри сервера
            */

            case RESPONSE_CODE_SERVER_INTERNAL_ERROR:
            case RESPONSE_CODE_SERVICE_UNAVAILABLE:
            case RESPONSE_CODE_NOT_IMPLEMENTED:
            case RESPONSE_CODE_HTTP_VERSION_NOT_SUPPORTED:
            case RESPONSE_CODE_VARIANT_ALSO_NEGOTIATES:
            case RESPONSE_CODE_INSUFFICIENT_STORAGE:
            case RESPONSE_CODE_LOOP_DETECTED:
            case RESPONSE_CODE_NOT_EXTENDED:
            case RESPONSE_CODE_NETWORK_AUTHENTICATION_REQUIRED:
            case RESPONSE_CODE_WEB_SERVER_IS_RETURNING_UNKNOWN_ERROR:

                if(errorBody == null){
                    UnexpectedInternalServerError unexpectedInternalServerError = new UnexpectedInternalServerError("Error body is null");
                    unexpectedInternalServerError.setResponseCode(responseCode);
                    throw unexpectedInternalServerError;
                }
                String errorString = null;
                try {
                    errorString = errorBody.string();
                } catch (IOException e) {
                    UnexpectedInternalServerError unexpectedInternalServerError = new UnexpectedInternalServerError("Failed to read error ResponseBody as String: "+ e.getMessage());
                    unexpectedInternalServerError.setResponseCode(responseCode);
                    throw unexpectedInternalServerError;
                }

                UnexpectedInternalServerError unexpectedInternalServerError = new UnexpectedInternalServerError(errorString);
                unexpectedInternalServerError.setResponseCode(responseCode);
                throw unexpectedInternalServerError;

             /*
               Исключения, обучловленные скорее плохой связью с сервером, чем с ошибками на его
               стороне
            */

            case RESPONSE_CODE_BAD_GATEWAY:
            case RESPONSE_CODE_GATEWAY_TIMEOUT:
            case RESPONSE_CODE_BANDWIDTH_LIMIT_EXCEEDED:
            case RESPONSE_CODE_A_TIMEOUT_OCCURRED:

                if(errorBody == null){
                    NetworkException networkException = new NetworkException("Error body is null");
                    networkException.setResponseCode(responseCode);
                    throw networkException;
                }
                String errorString5 = null;
                try {
                    errorString5 = errorBody.string();
                } catch (IOException e) {
                    NetworkException networkException = new NetworkException("Failed to read error ResponseBody as String: "+ e.getMessage());
                    networkException.setResponseCode(responseCode);
                    throw networkException;
                }

                NetworkException networkException = new NetworkException(errorString5);
                networkException.setResponseCode(responseCode);
                throw networkException;

            /*
               Исключения, к которым привели ошибки в запросах
            */

            case RESPONSE_CODE_UNAUTHORIZED:
            case RESPONSE_CODE_PAYMENT_REQUIRED:
            case RESPONSE_CODE_METHOD_NOT_ALLOWED:
            case RESPONSE_CODE_NOT_ACCEPTABLE:
            case RESPONSE_CODE_PROXY_AUTHENTICATION_REQUIRED:
            case RESPONSE_CODE_REQUEST_TIMEOUT:
            case RESPONSE_CODE_CONFLICT:
            case RESPONSE_CODE_GONE:
            case RESPONSE_CODE_LENGTH_REQUIRED:
            case RESPONSE_CODE_PRECONDITION_FAILED:
            case RESPONSE_CODE_REQUEST_ENTITY_TOO_LARGE:
            case RESPONSE_CODE_REQUEST_URL_TOO_LARGE:
            case RESPONSE_CODE_UNSUPPORTED_MEDIA_TYPE:
            case RESPONSE_CODE_REQUESTED_RANGE_NOT_SATISFIABLE:
            case RESPONSE_CODE_EXPECTATION_FAILED:
            case RESPONSE_CODE_UNPROCESSABLE_ENTITY:
            case RESPONSE_CODE_LOCKED:
            case RESPONSE_CODE_FAILED_DEPENDENCY:
            case RESPONSE_CODE_UNORDERED_COLLECTION:
            case RESPONSE_CODE_UPGRADE_REQUIRED:
            case RESPONSE_CODE_PRECONDITION_REQUIRED:
            case RESPONSE_CODE_TOO_MANY_REQUESTS:
            case RESPONSE_CODE_REQUEST_HEADER_FIELDS_TOO_LARGE:
            case RESPONSE_CODE_NO_RESPONSE:
            case RESPONSE_CODE_RETRY_WITH:
            case RESPONSE_CODE_UNAVAILABLE_FOR_LEGAL_REASONS:

                if(errorBody == null){
                    BadRequestException badRequestException2 = new BadRequestException("Error body is null");
                    badRequestException2.setResponseCode(responseCode);
                    throw badRequestException2;
                }
                String errorString6 = null;
                try {
                    errorString6 = errorBody.string();
                } catch (IOException e) {
                    BadRequestException badRequestException2 = new BadRequestException("Failed to read error ResponseBody as String: "+ e.getMessage());
                    badRequestException2.setResponseCode(responseCode);
                    throw badRequestException2;
                }

                ClientError clientError = ClientError.createClientError(errorString6, responseCode);
                throw clientError;

             /*
                Страница не найдена
              */

            case RESPONSE_CODE_PAGE_NOT_FOUND:

                if(errorBody == null){
                    ResourceNotFoundException resourceNotFoundException = new ResourceNotFoundException("Error body is null");
                    resourceNotFoundException.setResponseCode(responseCode);
                    throw resourceNotFoundException;
                }
                String errorString3 = null;
                try {
                    errorString3 = errorBody.string();
                } catch (IOException e) {
                    ResourceNotFoundException resourceNotFoundException = new ResourceNotFoundException("Failed to read error ResponseBody as String: "+ e.getMessage());
                    resourceNotFoundException.setResponseCode(responseCode);
                    throw resourceNotFoundException;
                }

                ResourceNotFoundException resourceNotFoundException = new ResourceNotFoundException(errorString3);
                resourceNotFoundException.setResponseCode(responseCode);
                throw resourceNotFoundException;

             /*
                Ошибка доступа
             */
            case RESPONSE_CODE_FORBIDDEN:

                if(errorBody == null){
                    ForbiddenException forbiddenException = new ForbiddenException("Error body is null");
                    forbiddenException.setResponseCode(responseCode);
                    throw forbiddenException;
                }
                String errorString4 = null;
                try {
                    errorString4 = errorBody.string();
                } catch (IOException e) {
                    ForbiddenException forbiddenException = new ForbiddenException("Failed to read error ResponseBody as String: "+ e.getMessage());
                    forbiddenException.setResponseCode(responseCode);
                    throw forbiddenException;
                }

                ForbiddenException forbiddenException = new ForbiddenException(errorString4);
                forbiddenException.setResponseCode(responseCode);
                throw forbiddenException;

              /*
                Ошибки, генерируемые платформой
              */

            case RESPONSE_CODE_BAD_REQUEST:

                /*
                При ошибке 400 от сервера, пытаемся найти в ответе
                код ошибки и вызывать конструктор исключения по коду
                 */

                if(errorBody == null){
                    throw new BadRequestException("Error body is null");
                }
                String errorString2 = null;
                try {
                    //берем тело ошибки как строку
                    errorString2 = errorBody.string();
                } catch (IOException e) {
                    //это может не получиться
                    throw new BadRequestException("Failed to read error ResponseBody as String: "+ e.getMessage());
                }
                JSONObject errorData = null;
                try {
                    //создаем объект с полями
                    errorData = new JSONObject(errorString2);
                    int errorCode = errorData.getInt("code"); //код ошибки, сгенерированный сервером
                    JSONObject data = errorData.getJSONObject("data"); //данные описания ошибки, содержащие {<key><value>}

                    /*
                    в данных содержится одна пара <key, value>, причем key может быть
                    разным, а нам нужно value, которое всегда строка - это сообщение
                    для нашего исключения
                     */

                    Iterator<String> keys = data.keys(); //создаем итератор для ключей (ключ будет всего один)
                    String message = ""; //строка сообщения для исключения
                    if(keys.hasNext()){
                        //если список ключей не пуст, полусаем значение строки по ключу
                        message = data.getString(keys.next());
                    }

                    try {
                        //выбрасываем нужное нам исключение с нужным текстом
                        throw BaseCpsException.createCpsException(message, errorCode);
                    }

                    //если, что-то не вышло, выбрасываем настолько информативное, насколько можем

                    catch (NullPointerException e){
                        throw new BadRequestException("json parser error: "+e.getMessage()+" Response data: "+errorString2);
                    }

                } catch (JSONException e) {
                    throw new BadRequestException("json parser error: "+e.getMessage());
                }
                catch (NullPointerException e){
                    throw new BadRequestException("json parser error: "+e.getMessage()+" Response data: "+errorString2);
                }

        }


    }

}
