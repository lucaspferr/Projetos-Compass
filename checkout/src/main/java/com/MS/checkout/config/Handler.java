package com.MS.checkout.config;

import feign.FeignException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;

@RestControllerAdvice
public class Handler {

    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<?> customHandler(Exception e){
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(FeignException.class)
    public ResponseEntity<?> feignHandler(Exception e){
        String[] simplifiedMessage = e.getMessage().split("]: \\[");
        return ResponseEntity.badRequest().body(simplifiedMessage[1].substring(0,(simplifiedMessage[1].length()-1)));
    }

    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ExceptionHandler({ConstraintViolationException.class, MethodArgumentNotValidException.class})
    public String nullHandler(Exception e){
        String error = e.getMessage();
        System.out.println(error);
        return ("The field(s) bellow must be filled:"+nameGetter(error));
    }

    public String nameGetter(String e){
        String posError="";
        if(e.contains("[type]")) posError = posError + "\n* Type";
        if(e.contains("[discount]")) posError = posError + "\n* Discount";
        if(e.contains("[status]")) posError = posError + "\n* Status";
        if(e.contains("[user_id]")) posError = posError + "\n* User ID";
        if(e.contains("[payment_id]")) posError = posError + "\n* Payment method ID";
        if(e.contains("[cart]")) posError = posError + "\n* Cart";
        if(e.contains("[variant_id]")) posError = posError + "\n* Variant ID";
        if(e.contains("[quantity]")) posError = posError + "\n* Variant quantity";
        return posError;
    }

}
