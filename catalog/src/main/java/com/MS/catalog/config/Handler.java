package com.MS.catalog.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;

@RestControllerAdvice
public class Handler {

    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ExceptionHandler({ConstraintViolationException.class, MethodArgumentNotValidException.class})
    public String nullHandler(Exception e){
        String error = e.getMessage();
        System.out.println(error);
        return ("The field(s) bellow must be filled:"+nameGetter(error));
    }

    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<?> customHandler(Exception e){
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    public String nameGetter(String e){
        String posError="";
        if(e.contains("[name]")) posError = posError + "\n* Name";
        if(e.contains("[active]")) posError = posError + "\n* Valid";
        if(e.contains("[category_ids]")) posError = posError + "\n* Category IDs";
        if(e.contains("[description]")) posError = posError + "\n* Description";
        if(e.contains("[color]")) posError = posError + "\n* Color";
        if(e.contains("[size]")) posError = posError + "\n* Size";
        if(e.contains("[price]")) posError = posError + "\n* Price";
        if(e.contains("[quantity]")) posError = posError + "\n* Quantity";
        return posError;
    }
}
