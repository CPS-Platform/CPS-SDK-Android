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

package ru.ctvt.cps.sdk.errorprocessing.image_processing;

import ru.ctvt.cps.sdk.errorprocessing.BaseCpsException;
import com.google.common.base.Strings;

/**
 * 31xx
 * ошибки обработки изображений
 * Created by Nokolya on 26.04.2017.
 */

public class ImageProcessingException extends BaseCpsException {

    /**
     * сообщение по умолчанию для ImageProcessingException
     */
    protected final static String EXCEPTION_STRING_IMAGE_PROCESSING = "image processing failed";

    /**
     * код ошибки img_not_found
     */
    protected final static int EXCEPTION_POSTFIX_IMG_NOT_FOUND = 0;
    /**
     * код ошибки img_too_big
     */
    protected final static int EXCEPTION_POSTFIX_IMG_TOO_BIG = 1;

    /**
     * сообщение по умолчанию для ошибки img_not_found
     */
    protected final static String EXCEPTION_STRING_IMG_NOT_FOUND = "Image not found";
    /**
     * сообщение по умолчанию для ошибки img_too_big
     */
    protected final static String EXCEPTION_STRING_IMG_TOO_BIG = "Image too big";



    public ImageProcessingException(){
        super(EXCEPTION_STRING_IMAGE_PROCESSING);
        error_code_prefix = EXCEPTION_PREFIX_IMAGE_PROCESSING;
        error_code_postfix = EXCEPTION_PREFIX_BASE;
    }

    public ImageProcessingException(String message){
        super(Strings.isNullOrEmpty(message)? EXCEPTION_STRING_IMAGE_PROCESSING : message);
        error_code_prefix = EXCEPTION_PREFIX_IMAGE_PROCESSING;
        error_code_postfix = EXCEPTION_PREFIX_BASE;
    }

    public ImageProcessingException(String message, Throwable cause){
        super(Strings.isNullOrEmpty(message)? EXCEPTION_STRING_IMAGE_PROCESSING : message, cause);
        error_code_prefix = EXCEPTION_PREFIX_IMAGE_PROCESSING;
        error_code_postfix = EXCEPTION_PREFIX_BASE;
    }

    public ImageProcessingException(Throwable cause){
        super(EXCEPTION_STRING_IMAGE_PROCESSING, cause);
        error_code_prefix = EXCEPTION_PREFIX_IMAGE_PROCESSING;
        error_code_postfix = EXCEPTION_PREFIX_BASE;
    }


    public ImageProcessingException(String message, int errorCode, Throwable cause){
        super(Strings.isNullOrEmpty(message)? EXCEPTION_STRING_IMAGE_PROCESSING : message, errorCode, cause);
        int prefix = errorCode/100;
        prefix *= 100;
        int postfix = errorCode - prefix;
        //для того, чтобы входить в указанное множество ошибок, у кода должен быть соответствующий префикс (первые два разряда)
        error_code_prefix = EXCEPTION_PREFIX_IMAGE_PROCESSING;
        error_code_postfix = postfix;
    }

    public ImageProcessingException(String message, int errorCode){
        super (Strings.isNullOrEmpty(message)? EXCEPTION_STRING_IMAGE_PROCESSING : message, errorCode);
        int prefix = errorCode/100;
        prefix *= 100;
        int postfix = errorCode - prefix;
        //для того, чтобы входить в указанное множество ошибок, у кода должен быть соответствующий префикс (первые два разряда)
        error_code_prefix = EXCEPTION_PREFIX_IMAGE_PROCESSING;
        error_code_postfix = postfix;
    }


    /**
     * Возвращает объект исключения, соответствующий по классу коду ошибки в иерархии ImageProcessingException
     * @param message - сообщение об ошибке
     * @param errorCode - код ошибки
     * @return
     */
    public static ImageProcessingException createImageProcessingException(String message, int errorCode){
        return createImageProcessingException(message, errorCode, null);
    }

    /**
     * Возвращает объект исключения, соответствующий по классу коду ошибки в иерархии ImageProcessingException
     * @param message - сообщение об ошибке
     * @param errorCode - код ошибки
     * @param cause
     * @return
     */
    public static ImageProcessingException createImageProcessingException(String message, int errorCode, Throwable cause){

        int prefix = errorCode/100;
        prefix *= 100;
        int postfix = errorCode - prefix;

        switch (postfix){
            case EXCEPTION_POSTFIX_IMG_NOT_FOUND:
                return cause == null? new ImageNotFoundException(message): new ImageNotFoundException(message, cause);
            case EXCEPTION_POSTFIX_IMG_TOO_BIG:
                return cause == null? new ImageTooBigException(message): new ImageTooBigException(message, cause);
        }

        return  cause == null? new ImageProcessingException(message, errorCode): new ImageProcessingException(message, errorCode, cause);
    }

    /**
     * Статический метод, позволяющий получить код ошибки, соответствующей данному классу исключения
     * @return - код ошибки
     */
    public static int getExpectedErrorCode(){
        return EXCEPTION_PREFIX_IMAGE_PROCESSING;
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

        return prefix == EXCEPTION_PREFIX_IMAGE_PROCESSING;
    }

}
