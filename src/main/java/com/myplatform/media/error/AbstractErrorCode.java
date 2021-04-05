package com.myplatform.media.error;

import lombok.Getter;

@Getter
public abstract class AbstractErrorCode {
    protected String code;
    protected String message;
    public void setMessage(final String message) {
        this.message = message;
    }
}
