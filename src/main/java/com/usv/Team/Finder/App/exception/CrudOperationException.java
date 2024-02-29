package com.usv.Team.Finder.App.exception;

public class CrudOperationException extends RuntimeException{
    public CrudOperationException(String mesaj){
        super(mesaj);
    }
}