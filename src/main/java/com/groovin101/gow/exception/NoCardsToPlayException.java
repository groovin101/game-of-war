package com.groovin101.gow.exception;

/**
 */
public class NoCardsToPlayException extends Exception {

    public NoCardsToPlayException(String msg) {
        super(msg);
    }

    public NoCardsToPlayException(String msg, Throwable e) {
        super(msg, e);
    }
}
