package com.ocean.whale.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class WhaleServiceException extends RuntimeException {
    WhaleException exceptionType;
    String detailMessage;
    Exception exception;

    public WhaleServiceException(WhaleException exceptionType, String detailMessage) {
        this.exceptionType = exceptionType;
        this.detailMessage = detailMessage;
    }
}
