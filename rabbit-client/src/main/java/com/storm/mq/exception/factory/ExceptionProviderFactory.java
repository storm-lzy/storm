package com.storm.mq.exception.factory;

/**
 *
 */
public class ExceptionProviderFactory {

    public static final ExceptionProvider ILLEGAL_ARGUMENT_EXCEPTION = ((throwable, message) ->
            new IllegalArgumentException(message,throwable));

    public static final ExceptionProvider ILLEGAL_STATE_EXCEPTION = ((throwable, message) ->
            new IllegalStateException(message,throwable));

    public static final ExceptionProvider UNSUPPORTED_OPERATION_EXCEPTION = ((throwable, message) ->
            new UnsupportedOperationException(message,throwable));

}
