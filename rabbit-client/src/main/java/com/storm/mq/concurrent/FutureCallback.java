package com.storm.mq.concurrent;

public interface FutureCallback <T> {
    default void complete(T result, Throwable ex, int status) {
    }

    default void success(T result) {
    }

    default void failed(Throwable ex) {
    }

    default void cancelled() {
    }
}
