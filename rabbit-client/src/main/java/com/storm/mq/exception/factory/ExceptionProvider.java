package com.storm.mq.exception.factory;

public interface ExceptionProvider {

    default RuntimeException newRuntimeException(String message){
        return this.newRuntimeException((Throwable)null,message);
    };

    RuntimeException newRuntimeException(Throwable throwable,String message);
}
