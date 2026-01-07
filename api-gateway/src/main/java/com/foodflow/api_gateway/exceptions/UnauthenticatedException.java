package com.foodflow.api_gateway.exceptions;

public class UnauthenticatedException extends RuntimeException{

    public UnauthenticatedException(String message){
        super(message);
    }
}
