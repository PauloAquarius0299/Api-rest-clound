package com.paulotech.apirestzero.service.exceptions;

public class EmailAlreadyExistsException extends RuntimeException{
    public EmailAlreadyExistsException(){
        super("Email already exists: ");
    }
}
