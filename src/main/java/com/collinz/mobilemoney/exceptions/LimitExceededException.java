package com.collinz.mobilemoney.exceptions;

public class LimitExceededException extends Exception{
    public LimitExceededException(String message){
        super(message);
    }
}
