package com.earthquake.exception;
//custom exception thrown when USGS API is unavailable
public class ApiUnavailableException extends RuntimeException {

    public ApiUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }

    public ApiUnavailableException(String message) {
        super(message);
    }
}