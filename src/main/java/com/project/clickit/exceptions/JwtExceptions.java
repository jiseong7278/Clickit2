package com.project.clickit.exceptions;

public class JwtExceptions extends RuntimeException{
    public JwtExceptions(){
        super();
    }

    public JwtExceptions(String message){
        super(message);
    }
}
