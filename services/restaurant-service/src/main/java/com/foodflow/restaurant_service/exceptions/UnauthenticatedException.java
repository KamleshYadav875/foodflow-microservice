package com.foodflow.restaurant_service.exceptions;

public class UnauthenticatedException extends RuntimeException{

    public UnauthenticatedException(String message){
        super(message);
    }
}
