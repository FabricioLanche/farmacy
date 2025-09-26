package com.example.farmacy.usuario.exception;

public class InvalidCredentialsUser extends RuntimeException {
    public InvalidCredentialsUser(String message) {
        super(message);
    }
}
