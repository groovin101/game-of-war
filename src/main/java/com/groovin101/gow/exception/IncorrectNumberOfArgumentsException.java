package com.groovin101.gow.exception;

/**
 * Indicates that there were not enough arguments provided
 */
public class IncorrectNumberOfArgumentsException extends WarInitializationException {

    public IncorrectNumberOfArgumentsException(String msg) {
        super(msg);
    }
}
