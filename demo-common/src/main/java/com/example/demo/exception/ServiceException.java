package com.example.demo.exception;

import com.example.demo.api.IErrorCode;

/**
 * 自定义服务异常
 * Created by YuanJW on 2022/12/9.
 */
public class ServiceException extends RuntimeException{
    private IErrorCode errorCode;

    public ServiceException(IErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(Throwable cause) {
        super(cause);
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public IErrorCode getErrorCode() {
        return errorCode;
    }
}
