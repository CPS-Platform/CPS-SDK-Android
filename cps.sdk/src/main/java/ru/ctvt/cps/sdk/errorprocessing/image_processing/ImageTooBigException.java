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

import com.google.common.base.Strings;

/**
 * 3101
 * img_too_big
 * Когда изображения слишком большое в байтах или в пикселях
 * Created by Nokolya on 26.04.2017.
 */

public class ImageTooBigException extends ImageProcessingException {

    private void init(){
        error_code_postfix = EXCEPTION_POSTFIX_IMG_TOO_BIG;
    }

    public ImageTooBigException(){
        super(EXCEPTION_STRING_IMG_TOO_BIG);
        init();
    }

    public ImageTooBigException(String message){
        super(Strings.isNullOrEmpty(message)? EXCEPTION_STRING_IMG_TOO_BIG: message);
        init();
    }

    public ImageTooBigException(String message, Throwable cause){
        super(Strings.isNullOrEmpty(message)? EXCEPTION_STRING_IMG_TOO_BIG: message, cause);
        init();
    }

    public ImageTooBigException(Throwable cause){
        super(EXCEPTION_STRING_IMG_TOO_BIG, cause);
        init();
    }

    /**
     * Статический метод, позволяющий получить код ошибки, соответствующей данному классу исключения
     * @return - код ошибки
     */
    public static int getExpectedErrorCode(){
        return EXCEPTION_PREFIX_IMAGE_PROCESSING+EXCEPTION_POSTFIX_IMG_TOO_BIG;
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
