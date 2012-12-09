package com.groovin101.gow.exception;

import java.util.NoSuchElementException;

/**
 */
public class NoCardsLeftException extends NoSuchElementException {

    public NoCardsLeftException(String msg) {
        super(msg);
    }
}
