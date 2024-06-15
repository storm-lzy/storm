package com.storm.mq.concurrent;

import java.util.concurrent.Future;

public interface CallbackFuture<T> extends Future<T> {

    void addCallback(FutureCallback<T> var1);

    void removeCallback(FutureCallback<T> var1);
}
