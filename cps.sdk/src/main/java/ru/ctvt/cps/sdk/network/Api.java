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

package ru.ctvt.cps.sdk.network;


import android.support.annotation.Nullable;

import com.google.gson.JsonElement;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;


/**
 * Интерфейс описывает набор запросов, используемых в платформе
 */
public interface Api {
    String PARAM_GROUP_NAME = "group_name";
    String PARAM_KEY_NAME = "key_name";
    String PARAM_ID = "id";
    String PARAM_KVS_VISIBILITY = "kvs_visibility";
    String PARAM_KVS_CONTAINER = "kvs_container";
    String PARAM_DEVICE_ID = "device_id";
    String PARAM_QUEUE_NAME = "queue_name";
    String PARAM_COMMAND_ID = "command_id";
    String PARAM_SERVICE_ID = "service";
    String PARAM_DEVICE_CODE = "code";
    String PARAM_DEVICE_SECRET = "secret";
    String PARAM_SEQUENCE_NAME = "name";
    String PARAM_SEQUENCE_TYPE = "type";
    String PARAM_DATA_ITEM_ID = "data_item_id";
    String PARAM_CREATION_MODE = "creation_mode";
    String PARAM_BODY_FROM_JSON = "body";

    /* Запросы для работы с аккаунтом */
    //region Auth

    /**
     * Нативная авторизация пользователя
     *
     * @param email      емайл (логин) пользователя в системе
     * @param password   пароль
     * @param set_cookie флаг сохранения долгой сессии
     * @param service_id ид сервиса
     * @return BaseResponse, в котором передаётся Header для дальнейшего использования его как ключ сессии
     */
    @FormUrlEncoded
    @POST("/v0/auth/native/sign-in")
    Call<BaseResponse<AuthResponse>> login(@Field("email") String email, @Field("password") String password, @Field("set_cookie") boolean set_cookie, @Field("service") String service_id);

    /**
     * Нативная регистрация пользователя в системе
     *
     * @param email      емайл (логин) пользователя в системе
     * @param password   пароль
     * @param service_id ид сервиса
     * @return BaseResponse, в котором передаётся Header для дальнейшего использования его как ключ сессии
     */
    @FormUrlEncoded
    @POST("/v0/auth/native/registration")
    Call<BaseResponse<AuthResponse>> register(@Field("email") String email, @Field("password") String password, @Field("service") String service_id);

    /**
     * Удаление сессии пользователя со стороны сервера
     *
     * @return BaseResponse
     */
    @POST("/v0/auth/logout")
    Call<BaseResponse<String>> logout();

    /**
     * Восстановление пароля
     *
     * @param email      емайл (логин) пользователя в системе
     * @param service_id ид сервиса
     * @return BaseResponse, в котором передаётся Header для дальнейшего использования его как ключ сессии
     */
    @FormUrlEncoded
    @PUT("/v0/auth/recovery")
    Call<BaseResponse<AuthResponse>> recoverPassword(@Field("email") String email, @Field("service") String service_id);

    //endregion Auth

    /** Запросы для работы с пользователем */
    //region User

    /**
     * Получение списка устройств пользователя, возвращает список данных устройства (идентификаторы)
     *
     * @return BaseResponse
     */
    @GET("/v0/users/current/devices")
    Call<BaseResponse<List<DeviceResponse>>> getDevices();

    //endregion User

    /** Запросы для работы с устройством */
    //region Device

    /**
     * генерация одноразового кода для регистрации устройства
     *
     * @param serviceId id сервиса
     * @return строка с кодом
     */
    @GET("v0/devices/registration/code")
    Call<BaseResponse<DeviceCodeResponse>> generateCode(@Query(PARAM_SERVICE_ID) String serviceId);


    /**
     * обмен кода регистрации на токен и информацию об устройстве
     *
     * @param serviceId    id сервиса
     * @param deviceCode   код регистрации
     * @param deviceSecret дополнительный секрет
     * @return
     */
    @GET("/v0/devices/registration/credentials")
    Call<BaseResponse<DeviceAccessTokenResponse>> recieveTokenForDevice(@Query(PARAM_SERVICE_ID) String serviceId,
                                                                        @Query(PARAM_DEVICE_CODE) String deviceCode,
                                                                        @Query(PARAM_DEVICE_SECRET) String deviceSecret);


    /**
     * Привязка реального устройства к сущности
     *
     * @param deviceId идентификатор устройства
     * @param code     код, полученный с реального устройства
     * @return
     */
    @FormUrlEncoded
    @PUT("/v0/devices/{" + PARAM_DEVICE_ID + "}/registration-code")
    Call<BaseResponse<CreatedDeviceResponse>> setDeviceCode(@Path(PARAM_DEVICE_ID) String deviceId, @Field("code") String code);

    /**
     * Возвращает информацию об устройстве. Устройство должно существовать.
     *
     * @param deviceId id устройства
     * @return
     */
    @GET("/v0/devices/{" + PARAM_DEVICE_ID + "}")
    Call<BaseResponse<DeviceResponse>> getDeviceInfo(@Path(PARAM_DEVICE_ID) String deviceId);

    /**
     * отзыв текущего токена сущности
     *
     * @param deviceId id устройства
     * @return
     */
    @DELETE("/v0/devices/{" + PARAM_DEVICE_ID + "}/access-token")
    Call<BaseResponse<String>> deleteDeviceToken(@Path(PARAM_DEVICE_ID) String deviceId);

    /**
     * возвращает информацию о текущем токене доступа сущности
     *
     * @param deviceId id устройства
     * @return
     */
    @GET("/v0/devices/{" + PARAM_DEVICE_ID + "}/access-token")
    Call<BaseResponse<DeviceAccessTokenResponse>> getDeviceToken(@Path(PARAM_DEVICE_ID) String deviceId);

    /**
     * Перевыпуск токена доступа сущности. Старый токен отзывается, создается и возвращается новый токен.
     *
     * @param deviceId id устройства
     * @return
     */
    @PUT("/v0/devices/{" + PARAM_DEVICE_ID + "}/access-token")
    Call<BaseResponse<DeviceAccessTokenResponse>> updateDeviceToken(@Path(PARAM_DEVICE_ID) String deviceId);

    /**
     * Добавление устройства с привязкой, используя полученный код
     *
     * @param userId     id пользователя
     * @param deviceCode одноразовый пароль для привязки. Если null - устройство будет добавлено без привязки
     * @return
     */
    @POST("/v0/users/{" + PARAM_ID + "}/devices")
    Call<BaseResponse<DeviceResponse>> addNewDevice(@Path(PARAM_ID) String userId,
                                                    @Query(PARAM_DEVICE_CODE) String deviceCode);


    //endregion Device

    //region Key Value Storage

    /**
     * Получение всего Key Value Storage
     *
     * @param kvs_container  может быть передано: user, service и device в зависимости от того, чей Key Value Storage необходимо получить
     * @param id             идентификатор пользователя, девайса или сервиса в зависимости от того, какой kvs_container был выбран
     * @param kvs_visibility может быть передано: local - для получения Local KVS и public - для получения Public KVS
     * @return BaseResponse Набор ключей и значений KVS
     */
    @GET("/v0/{" + PARAM_KVS_CONTAINER + "}/{" + PARAM_ID + "}/kv/{" + PARAM_KVS_VISIBILITY + "}")
    Call<BaseResponse<HashMap<String, HashMap<String, Object>>>> getKVStorage(@Path(PARAM_KVS_CONTAINER) String kvs_container,
                                                                              @Path(PARAM_ID) String id,
                                                                              @Path(PARAM_KVS_VISIBILITY) String kvs_visibility);


    /**
     * Получение группы в Key Value Storage
     *
     * @param kvs_container  может быть передано: user, service и device в зависимости от того, чью группу необходимо получить
     * @param id             идентификатор пользователя, девайса или сервиса в зависимости от того, какой kvs_container был выбран
     * @param kvs_visibility может быть передано: local - для получения Local группы и public - для получения Public группы
     * @param group_name     имя группы
     * @return BaseResponse Набор ключей и значений группы
     */
    @GET("/v0/{" + PARAM_KVS_CONTAINER + "}/{" + PARAM_ID + "}/kv/{" + PARAM_KVS_VISIBILITY + "}/{" + PARAM_GROUP_NAME + "}")
    Call<BaseResponse<HashMap<String, Object>>> getKVGroup(@Path(PARAM_KVS_CONTAINER) String kvs_container,
                                                                        @Path(PARAM_ID) String id,
                                                                        @Path(PARAM_KVS_VISIBILITY) String kvs_visibility,
                                                                        @Path(PARAM_GROUP_NAME) String group_name);

    @GET("/v0/{" + PARAM_KVS_CONTAINER + "}/{" + PARAM_ID + "}/kv/{" + PARAM_KVS_VISIBILITY + "}/{" + PARAM_GROUP_NAME + "}")
    Call<BaseResponse<HashMap<String, JsonElement>>> getKVGroupAsObject(@Path(PARAM_KVS_CONTAINER) String kvs_container,
                                                                        @Path(PARAM_ID) String id,
                                                                        @Path(PARAM_KVS_VISIBILITY) String kvs_visibility,
                                                                        @Path(PARAM_GROUP_NAME) String group_name);

    /**
     * Получение определенного значения по ключу и группе
     *
     * @param kvs_container  может быть передано: user, service и device в зависимости от того, чье значение необходимо получить
     * @param id             идентификатор пользователя, девайса или сервиса в зависимости от того, какой kvs_container был выбран
     * @param kvs_visibility может быть передано: local - для получения Local значения и public - для получения Public значения
     * @param group_name     имя группы
     * @param key_name       имя ключа
     * @return BaseResponse Объект - значение
     */
    @GET("/v0/{" + PARAM_KVS_CONTAINER + "}/{" + PARAM_ID + "}/kv/{" + PARAM_KVS_VISIBILITY + "}/{" + PARAM_GROUP_NAME + "}/{" + PARAM_KEY_NAME + "}")
    Call<BaseResponse<Object>> getValue(@Path(PARAM_KVS_CONTAINER) String kvs_container,
                                               @Path(PARAM_ID) String id,
                                               @Path(PARAM_KVS_VISIBILITY) String kvs_visibility,
                                               @Path(PARAM_GROUP_NAME) String group_name,
                                               @Path(PARAM_KEY_NAME) String key_name);

    @GET("/v0/{" + PARAM_KVS_CONTAINER + "}/{" + PARAM_ID + "}/kv/{" + PARAM_KVS_VISIBILITY + "}/{" + PARAM_GROUP_NAME + "}/{" + PARAM_KEY_NAME + "}")
    Call<BaseResponse<JsonElement>> getValueAsObject(@Path(PARAM_KVS_CONTAINER) String kvs_container,
                                                     @Path(PARAM_ID) String id,
                                                     @Path(PARAM_KVS_VISIBILITY) String kvs_visibility,
                                                     @Path(PARAM_GROUP_NAME) String group_name,
                                                     @Path(PARAM_KEY_NAME) String key_name);


    /**
     * Запись группы в KVS
     *
     * @param kvs_container  может быть передано: user, service и device в зависимости от того, куда необходимо записать группу
     * @param id             идентификатор пользователя, девайса или сервиса в зависимости от того, какой kvs_container был выбран
     * @param kvs_visibility может быть передано: local - для записи Local группы и public - для записи Public группы
     * @param group_name     имя группы
     * @param keyValueGroup  объект - группа, хранящий в себе ключи и значения
     * @return BaseResponse Основная информация о запросе ( статус, код, сообщение, данные)
     */
    @PUT("/v0/{" + PARAM_KVS_CONTAINER + "}/{" + PARAM_ID + "}/kv/{" + PARAM_KVS_VISIBILITY + "}/{" + PARAM_GROUP_NAME + "}")
    Call<BaseResponse<String>> putGroup(@Path(PARAM_KVS_CONTAINER) String kvs_container,
                                        @Path(PARAM_ID) String id,
                                        @Path(PARAM_KVS_VISIBILITY) String kvs_visibility,
                                        @Path(PARAM_GROUP_NAME) String group_name,
                                        @Body ValuesT keyValueGroup);

    /**
     * Запись значения по ключу и группе
     *
     * @param kvs_container  может быть передано: user, service и device в зависимости от того, куда необходимо записать значение
     * @param id             идентификатор пользователя, девайса или сервиса в зависимости от того, какой kvs_container был выбран
     * @param kvs_visibility может быть передано: local - для записи Local значения и public - для записи Public значения
     * @param group_name     имя группы
     * @param key            ключ
     * @param value          объект - значение
     * @return BaseResponse Основная информация о запросе ( статус, код, сообщение, данные)
     */
    @PUT("/v0/{" + PARAM_KVS_CONTAINER + "}/{" + PARAM_ID + "}/kv/{" + PARAM_KVS_VISIBILITY + "}/{" + PARAM_GROUP_NAME + "}/{" + PARAM_KEY_NAME + "}")
    Call<BaseResponse> putValue(@Path(PARAM_KVS_CONTAINER) String kvs_container,
                                @Path(PARAM_ID) String id,
                                @Path(PARAM_KVS_VISIBILITY) String kvs_visibility,
                                @Path(PARAM_GROUP_NAME) String group_name,
                                @Path(PARAM_KEY_NAME) String key,
                                @Body ValueT value);

    /**
     * Удаление группы из KVS
     *
     * @param kvs_container  может быть передано: user, service и device в зависимости от того, чью группу необходимо удалить
     * @param id             идентификатор пользователя, девайса или сервиса в зависимости от того, какой kvs_container был выбран
     * @param kvs_visibility может быть передано: local - для удаления Local группы и public - для удаления Public группы
     * @param group_name     имя группы
     * @return BaseResponse Основная информация о запросе ( статус, код, сообщение, данные)
     */
    @DELETE("/v0/{" + PARAM_KVS_CONTAINER + "}/{" + PARAM_ID + "}/kv/{" + PARAM_KVS_VISIBILITY + "}/{" + PARAM_GROUP_NAME + "}")
    Call<BaseResponse> deleteGroup(@Path(PARAM_KVS_CONTAINER) String kvs_container,
                                   @Path(PARAM_ID) String id,
                                   @Path(PARAM_KVS_VISIBILITY) String kvs_visibility,
                                   @Path(PARAM_GROUP_NAME) String group_name);

    /**
     * Удаление значения по ключу и группе
     *
     * @param kvs_container  может быть передано: user, service и device в зависимости от того, чье значение необходимо удалить
     * @param user_id        идентификатор пользователя, девайса или сервиса в зависимости от того, какой kvs_container был выбран
     * @param kvs_visibility может быть передано: local - для удаления Local значения и public - для удаления Public значения
     * @param group_name     имя группы
     * @param key            ключ
     * @return BaseResponse Основная информация о запросе ( статус, код, сообщение, данные)
     */
    @DELETE("/v0/{" + PARAM_KVS_CONTAINER + "}/{" + PARAM_ID + "}/kv/{" + PARAM_KVS_VISIBILITY + "}/{" + PARAM_GROUP_NAME + "}/{" + PARAM_KEY_NAME + "}")
    Call<BaseResponse> deleteValue(@Path(PARAM_KVS_CONTAINER) String kvs_container,
                                   @Path(PARAM_ID) String user_id,
                                   @Path(PARAM_KVS_VISIBILITY) String kvs_visibility,
                                   @Path(PARAM_GROUP_NAME) String group_name,
                                   @Path(PARAM_KEY_NAME) String key);

    //endregion Key Value Storage

    //region Commands

    /**
     * Возвращает все очереди команд для устройства
     *
     * @param deviceId идентификатор устройства
     *
     * @return BaseResponse Основная информация о запросе (статус, код, сообщение, данные)
     */
    @GET("/v0/devices/{" + PARAM_DEVICE_ID + "}/command-queues")
    Call<BaseResponse<HashMap<String, CommandQueueResponse>>> fetchAllQueues(@Path(PARAM_DEVICE_ID) String deviceId);

    /**
     * Удаляет очередь команд для устройтсва
     *
     * @param deviceId идентификатор устройства
     * @param queue_id идентификатор очереди команд
     *
     * @return BaseResponse Основная информация о запросе (статус, код, сообщение, данные)
     */
    @DELETE("/v0/devices/{" + PARAM_DEVICE_ID + "}/command-queues/{" + PARAM_QUEUE_NAME + "}")
    Call<BaseResponse> deleteQueue(@Path(PARAM_DEVICE_ID) String deviceId,
                                   @Path(PARAM_QUEUE_NAME) String queue_id);

    /**
     * Возвращает очередь команд для устройтсва
     *
     * @param deviceId идентификатор устройства
     * @param queue_id идентификатор очереди команд
     *
     * @return Объект типа CommandQueueResponse
     */
    @GET("/v0/devices/{" + PARAM_DEVICE_ID + "}/command-queues/{" + PARAM_QUEUE_NAME + "}")
    Call<BaseResponse<CommandQueueResponse>> fetchCommandQueueInfo(@Path(PARAM_DEVICE_ID) String deviceId,
                                                                   @Path(PARAM_QUEUE_NAME) String queue_id);

    /**
     * Добавляет новую очередь команд для устройтсва
     *
     * @param deviceId     идентификатор устройства
     * @param queue_id     идентификатор очереди команд
     * @param creationMode режим создания
     *
     * @return BaseResponse Основная информация о запросе (статус, код, сообщение, данные)
     */
    @PUT("/v0/devices/{" + PARAM_DEVICE_ID + "}/command-queues/{" + PARAM_QUEUE_NAME + "}")
    Call<BaseResponse<CommandQueueResponse>> addQueue(@Path(PARAM_DEVICE_ID) String deviceId,
                                                      @Path(PARAM_QUEUE_NAME) String queue_id,
                                                      @Query(PARAM_CREATION_MODE) String creationMode);

    /**
     * Возвращает команды из очереди команд для устройтсва
     *
     * @param deviceId идентификатор устройства
     * @param queue_id идентификатор очереди команд
     *
     * @return List объектов типа CommandResponse
     */
    @GET("/v0/devices/{" + PARAM_DEVICE_ID + "}/command-queues/{" + PARAM_QUEUE_NAME + "}/commands")
    Call<BaseResponse<List<CommandResponse>>> fetchAllCommands(@Path(PARAM_DEVICE_ID) String deviceId,
                                                               @Path(PARAM_QUEUE_NAME) String queue_id);

    /**
     * Возвращает команды в определенном диапазоне
     *
     * @return List объектов типа CommandResponse
     */
    @GET
    Call<BaseResponse<List<CommandResponse>>> fetchRangeCommands(@Url String URL);

    /**
     * Добавляет новую команду в очередь команд для устройтсва
     *
     * @param deviceId идентификатор устройства
     * @param queue_id идентификатор очереди команд
     * @param argument содержимое команды
     *
     * @return BaseResponse Основная информация о запросе (статус, код, сообщение, данные)
     */
    @POST("/v0/devices/{" + PARAM_DEVICE_ID + "}/command-queues/{" + PARAM_QUEUE_NAME + "}/commands")
    Call<BaseResponse<CommandResponse>> addCommand(@Path(PARAM_DEVICE_ID) String deviceId,
                                                   @Path(PARAM_QUEUE_NAME) String queue_id,
                                                   @Body @Nullable CommandArguments argument);

    /**
     * Возвращает все исполняемые команды очереди команд для устройтсва
     *
     * @param deviceId идентификатор устройства
     * @param queue_id идентификатор очереди команд
     *
     * @return List объектов типа CommandResponse
     */
    @GET("/v0/devices/{" + PARAM_DEVICE_ID + "}/command-queues/{" + PARAM_QUEUE_NAME + "}/commands/next")
    Call<BaseResponse<List<CommandResponse>>> fetchQueuedCommands(@Path(PARAM_DEVICE_ID) String deviceId,
                                                                  @Path(PARAM_QUEUE_NAME) String queue_id);

    /**
     * Возвращает следующую исполняемую команду
     *
     * @param deviceId идентификатор устройства
     * @param queue_id идентификатор очереди команд
     *
     * @return Объект типа CommandResponse
     */
    @GET("/v0/devices/{" + PARAM_DEVICE_ID + "}/command-queues/{" + PARAM_QUEUE_NAME + "}/commands/next/nearest")
    Call<BaseResponse<CommandResponse>> fetchNearestQueuedCommand(@Path(PARAM_DEVICE_ID) String deviceId,
                                                                  @Path(PARAM_QUEUE_NAME) String queue_id);

    /**
     * Возвращает команду из очереди команд для устройтсва
     *
     * @param deviceId   идентификатор устройства
     * @param queue_id   идентификатор очереди команд
     * @param command_id идентификатор команды
     *
     * @return Объект типа CommandResponse
     */
    @GET("/v0/devices/{" + PARAM_DEVICE_ID + "}/command-queues/{" + PARAM_QUEUE_NAME + "}/commands/{" + PARAM_COMMAND_ID + "}")
    Call<BaseResponse<CommandResponse>> fetchCommand(@Path(PARAM_DEVICE_ID) String deviceId,
                                                     @Path(PARAM_QUEUE_NAME) String queue_id,
                                                     @Path(PARAM_COMMAND_ID) String command_id);

    /**
     * Пометить команду как принятую к исполнению
     *
     * @param deviceId   идентификатор устройства
     * @param queue_id   идентификатор очереди команд
     * @param command_id идентификатор команды
     *
     * @return BaseResponse Основная информация о запросе (статус, код, сообщение, данные)
     */
    @POST("/v0/devices/{" + PARAM_DEVICE_ID + "}/command-queues/{" + PARAM_QUEUE_NAME + "}/commands/{" + PARAM_COMMAND_ID + "}/acquire")
    Call<BaseResponse> markExecuting(@Path(PARAM_DEVICE_ID) String deviceId,
                                     @Path(PARAM_QUEUE_NAME) String queue_id,
                                     @Path(PARAM_COMMAND_ID) String command_id);

    /**
     * Пометить команду как исполненную
     *
     * @param deviceId   идентификатор устройства
     * @param queue_id   идентификатор очереди команд
     * @param command_id идентификатор команды
     * @param value       результат исполнения команды
     *
     * @return BaseResponse Основная информация о запросе (статус, код, сообщение, данные)
     */
    @POST("/v0/devices/{" + PARAM_DEVICE_ID + "}/command-queues/{" + PARAM_QUEUE_NAME + "}/commands/{" + PARAM_COMMAND_ID + "}/execute")
    Call<BaseResponse> markExecuted(@Path(PARAM_DEVICE_ID) String deviceId,
                                    @Path(PARAM_QUEUE_NAME) String queue_id,
                                    @Path(PARAM_COMMAND_ID) String command_id,
                                    @Body Object value);
    //endregion Commands

    //region Sequences

    /**
     * Возвращает все последовательности
     *
     * @param deviceId идентификатор устройства
     *
     * @return BaseResponse Основная информация о запросе (статус, код, сообщение, данные)
     */
    @GET("/v0/devices/{" + PARAM_DEVICE_ID + "}/sequences")
    Call<BaseResponse<HashMap<String, SequenceResponse>>> fetchAllSequences(@Path(PARAM_DEVICE_ID) String deviceId);

    /**
     * Удаляет последовательность
     *
     * @param deviceId идентификатор устройства
     * @param sequenceName идентификатор последовательности
     *
     * @return BaseResponse Основная информация о запросе (статус, код, сообщение, данные)
     */
    @DELETE("/v0/devices/{" + PARAM_DEVICE_ID + "}/sequences/{" + PARAM_SEQUENCE_NAME + "}")
    Call<BaseResponse> deleteSequence(@Path(PARAM_DEVICE_ID) String deviceId,
                                      @Path(PARAM_SEQUENCE_NAME) String sequenceName);

    /**
     * Возвращает информацию о последовательности
     *
     * @param deviceId идентификатор устройства
     * @param sequenceName идентификатор последовательности
     *
     * @return Объект типа SequenceResponse
     */
    @GET("/v0/devices/{" + PARAM_DEVICE_ID + "}/sequences/{" + PARAM_SEQUENCE_NAME + "}")
    Call<BaseResponse<SequenceResponse>> fetchSequenceInfo(@Path(PARAM_DEVICE_ID) String deviceId,
                                                           @Path(PARAM_SEQUENCE_NAME) String sequenceName);

    /**
     * Создает новую последовательность
     *
     * @param deviceId     идентификатор устройства
     * @param sequenceName имя последовательности
     * @param sequenceType тип ключа в последовательности
     * @param creationMode режим создания
     *
     * @return BaseResponse Основная информация о запросе (статус, код, сообщение, данные)
     */
    @PUT("/v0/devices/{" + PARAM_DEVICE_ID + "}/sequences/{" + PARAM_SEQUENCE_NAME + "}")
    Call<BaseResponse> createSequence(@Path(PARAM_DEVICE_ID) String deviceId,
                                      @Path(PARAM_SEQUENCE_NAME) String sequenceName,
                                      @Query(PARAM_SEQUENCE_TYPE) String sequenceType,
                                      @Query(PARAM_CREATION_MODE) String creationMode);

    /**
     * Удаляет множество значений последовательности
     *
     * @param deviceId     идентификатор устройства
     * @param sequenceName имя последовательности
     * @param qualification уточнение области удаления (ключи и значения)
     *
     * @return BaseResponse Основная информация о запросе (статус, код, сообщение, данные)
     */
    @HTTP(method = "DELETE", path = "/v0/devices/{" + PARAM_DEVICE_ID + "}/sequences/{" + PARAM_SEQUENCE_NAME + "}/items", hasBody = true)
    Call<BaseResponse> deleteManyData(@Path(PARAM_DEVICE_ID) String deviceId,
                                      @Path(PARAM_SEQUENCE_NAME) String sequenceName,
                                      @Body Object qualification);

    /**
     * Возвращает все значения поледовательности
     *
     * @param deviceId     идентификатор устройства
     * @param sequenceName имя последовательности
     *
     * @return BaseResponse Основная информация о запросе (статус, код, сообщение, данные)
     */
    @GET("/v0/devices/{" + PARAM_DEVICE_ID + "}/sequences/{" + PARAM_SEQUENCE_NAME + "}/items")
    Call<BaseResponse<HashMap<String, Object>>> fetchAllValues(@Path(PARAM_DEVICE_ID) String deviceId,
                                                               @Path(PARAM_SEQUENCE_NAME) String sequenceName);

    /**
     * Возвращает множество значений последовательности в заданном диапазоне
     *
     * @return BaseResponse Основная информация о запросе (статус, код, сообщение, данные)
     */
    @GET
    Call<BaseResponse<HashMap<String, Object>>> fetchRangeValues(@Url String URL);

    /**
     * Записывает или обновляет множетсво значений последовательности
     *
     * @param deviceId     идентификатор устройства
     * @param sequenceName имя последовательности
     * @param changedValue информация для обновления (старые и новые значения)
     * @return BaseResponse Основная информация о запросе (статус, код, сообщение, данные)
     */
    @PATCH("/v0/devices/{" + PARAM_DEVICE_ID + "}/sequences/{" + PARAM_SEQUENCE_NAME + "}/items")
    Call<BaseResponse> patchManyData(@Path(PARAM_DEVICE_ID) String deviceId,
                                     @Path(PARAM_SEQUENCE_NAME) String sequenceName,
                                     @Body Object changedValue);

    /**
     * Записывает значение без указания ключа (работает только для последовательностей типа DATETIME)
     *
     * @param deviceId     идентификатор устройства
     * @param sequenceName имя последовательности
     * @param value значение для записи
     * @return BaseResponse Основная информация о запросе (статус, код, сообщение, данные)
     */
    @POST("/v0/devices/{" + PARAM_DEVICE_ID + "}/sequences/{" + PARAM_SEQUENCE_NAME + "}/items")
    Call<BaseResponse> addDataKeyless(@Path(PARAM_DEVICE_ID) String deviceId,
                                      @Path(PARAM_SEQUENCE_NAME) String sequenceName,
                                      @Body Object value);

    /**
     * Записывает множество значений в последовательность
     *
     * @param deviceId     идентификатор устройства
     * @param sequenceName имя последовательности
     * @param values значения для записи
     * @return BaseResponse Основная информация о запросе (статус, код, сообщение, данные)
     */
    @PUT("/v0/devices/{" + PARAM_DEVICE_ID + "}/sequences/{" + PARAM_SEQUENCE_NAME + "}/items")
    Call<BaseResponse> addManyData(@Path(PARAM_DEVICE_ID) String deviceId,
                                   @Path(PARAM_SEQUENCE_NAME) String sequenceName,
                                   @Body Object values);

    /**
     * Удаляет значение из последовательности
     *
     * @param deviceId     идентификатор устройства
     * @param sequenceName имя последовательности
     * @param dataItemID   идентификатор значения
     * @return BaseResponse Основная информация о запросе (статус, код, сообщение, данные)
     */
    @HTTP(method = "DELETE", path = "/v0/devices/{" + PARAM_DEVICE_ID + "}/sequences/{" + PARAM_SEQUENCE_NAME + "}/items/{" + PARAM_DATA_ITEM_ID + "}", hasBody = true)
    Call<BaseResponse> deleteOneItem(@Path(PARAM_DEVICE_ID) String deviceId,
                                     @Path(PARAM_SEQUENCE_NAME) String sequenceName,
                                     @Path(PARAM_DATA_ITEM_ID) String dataItemID,
                                     @Body Object whenValue);




    /**
     * Возвращает значение из последовательности по ключу
     *
     * @param deviceId     идентификатор устройства
     * @param sequenceName имя последовательности
     * @param dataItemID   идентификатор значения
     * @return Объект типа DataItem
     */
    @GET("/v0/devices/{" + PARAM_DEVICE_ID + "}/sequences/{" + PARAM_SEQUENCE_NAME + "}/items/{" + PARAM_DATA_ITEM_ID + "}")
    Call<BaseResponse<Object>> getDataByKey(@Path(PARAM_DEVICE_ID) String deviceId,
                                            @Path(PARAM_SEQUENCE_NAME) String sequenceName,
                                            @Path(PARAM_DATA_ITEM_ID) String dataItemID);

    /**
     * Записывает значение в последовательность по ключу
     *
     * @param deviceId     идентификатор устройства
     * @param sequenceName имя последовательности
     * @param dataItemID   идентификатор значения
     * @return BaseResponse Основная информация о запросе (статус, код, сообщение, данные)
     */
    @PUT("/v0/devices/{" + PARAM_DEVICE_ID + "}/sequences/{" + PARAM_SEQUENCE_NAME + "}/items/{" + PARAM_DATA_ITEM_ID + "}")
    Call<BaseResponse> addDataByKey(@Path(PARAM_DEVICE_ID) String deviceId,
                                    @Path(PARAM_SEQUENCE_NAME) String sequenceName,
                                    @Path(PARAM_DATA_ITEM_ID) String dataItemID,
                                    @Body ValueT value);

    //endregion Sequences
}
