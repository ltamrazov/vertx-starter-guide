package com.ltamrazov.vertxstarterguide.exceptions;

/**
 * Created by levontamrazov on 2017-01-28.
 */
public class CustomException extends RuntimeException{
    public CustomException(String msg, int status){
        super(msg);
    }
}
