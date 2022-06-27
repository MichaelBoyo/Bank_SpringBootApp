package com.example.bank.exceptions;

public class BankException extends RuntimeException{
    public BankException(){
        super();
    }
    public BankException(String message){
        super(message);
    }
    public BankException(String message,Throwable cause){
        super(message,cause);
    }
    public BankException(Throwable cause){
        super(cause);
    }
}
