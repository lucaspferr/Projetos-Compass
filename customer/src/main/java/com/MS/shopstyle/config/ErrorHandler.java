package com.MS.shopstyle.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;

@RestControllerAdvice
public class ErrorHandler {

    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<?> illegalHandler(Exception e){
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    //ConstraintViolationException
//    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ExceptionHandler({ConstraintViolationException.class, MethodArgumentNotValidException.class})
    public String nullHandler(Exception e){
        String error = e.getMessage();
        System.out.println(error);
        return ("The field(s) bellow must be filled:"+nameGetter(error));
    }

    public String nameGetter(String preError){
        String posError = "";
        if(preError.contains("firstName")) {posError = posError + "\n* First name";if(preError.contains("[firstName],2147483647,3")) posError = posError + " : must be at least 3 characters";}
        if(preError.contains("lastName")) {posError = posError + "\n* Last name";if(preError.contains("[lastName],2147483647,3")) posError = posError + " : must be at least 3 characters";}
        if(preError.contains("sex")) posError = posError + "\n* Sex";
        if(preError.contains("cpf")) posError = posError + "\n* CPF";
        if(preError.contains("birthdate")) posError = posError + "\n* Birthdate (data de nascimento)";
        if(preError.contains("email")) posError = posError + "\n* Email";
        if(preError.contains("password")) posError = posError + "\n* Password (senha)";
        if(preError.contains("active")) posError = posError + "\n* Active (ativo)";
        return posError;
    }

//    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
//    @ExceptionHandler(Exception.class)
//    public String badRequestHandler(Exception e){
//        String error = e.getMessage();
//        return error;
//    }
}
