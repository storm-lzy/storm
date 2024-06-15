package com.storm.mq.concurrent;

import com.storm.mq.utils.LockUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 */
@Slf4j
public class StateFuture<T> implements CallbackFuture<T> {

    private static final byte INIT = 0;
    private static final byte COMPLETE = 1;
    private static final byte CANCEL = 2;
    private static final byte RUNNING = 3;
    private final Lock lock = new ReentrantLock();
    private final Condition doneCondition;
    private final List<FutureCallback<T>> callbacks;
    private volatile T obj;
    private volatile Throwable throwable;
    private volatile byte status = 0;

    public StateFuture() {
        this.doneCondition = this.lock.newCondition();
        this.callbacks = new CopyOnWriteArrayList<>();
    }

    @Override
    public void addCallback(FutureCallback<T> callback) {
        if (!this.callback(callback)) {
            LockUtil.runWithLock(this.lock, () -> {
                if (!this.callback(callback)) {
                    this.callbacks.add(callback);
                }

            });
        }
    }

    private boolean callback(FutureCallback<T> callback) {
        if (this.status == 1) {
            byte status;
            if (this.throwable != null) {
                status = 1;
                callback.failed(this.throwable);
            } else {
                status = 0;
                callback.success(this.obj);
            }

            callback.complete(this.obj, this.throwable, status);
            return true;
        } else if (this.status == 2) {
            callback.cancelled();
            callback.complete(this.obj, this.throwable, 2);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void removeCallback(FutureCallback<T> callback) {
        this.callbacks.remove(callback);
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        if (this.status != 1 && this.status != 2) {
            this.status = 2;
            this.doneCondition.signalAll();

            for (FutureCallback<T> callback : this.callbacks) {
                try {
                    this.callback(callback);
                } catch (Throwable var4) {
                    log.error(String.format("%s Cancel [%s] callback error.", var4, callback));
                }
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean isCancelled() {
        return this.status == 2;
    }

    @Override
    public boolean isDone() {
        return this.status == 1 || this.status == 2;
    }

    @Override
    public T get() throws InterruptedException, ExecutionException {
        this.lock.lock();

        T var1;
        try {
            if (!this.isDone()) {
                this.doneCondition.await();
            }

            var1 = this.getNotWait();
        } finally {
            this.lock.unlock();
        }

        return var1;
    }

    private T getNotWait() throws ExecutionException {
        if (this.throwable != null) {
            throw new ExecutionException(this.throwable);
        } else if (this.isCancelled()) {
            throw new CancellationException();
        } else {
            return this.obj;
        }
    }

    @Override
    public T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        this.lock.lock();

        T var4;
        try {
            if (!this.isDone() && this.doneCondition.await(timeout, unit)) {
                throw new TimeoutException();
            }

            var4 = this.getNotWait();
        } finally {
            this.lock.unlock();
        }

        return var4;
    }

    public boolean done(T obj) {
        return LockUtil.runWithLock(this.lock, () -> {
            if (this.status != 3) {
                return false;
            } else {
                this.obj = obj;
                this.status = 1;
                this.doneCondition.signalAll();

                for (FutureCallback<T> tFutureCallback : this.callbacks) {
                    try {
                        this.callback(tFutureCallback);
                    } catch (Throwable var5) {
                        log.error("{} Normal callback [{}] An exception occurred during execution", var5, tFutureCallback);
                    }
                }
                return true;
            }
        });
    }

    public boolean exception(Throwable throwable) {
            if (this.status != 3 && this.status != 0) {
                return false;
            } else {
                this.throwable = throwable;
                this.status = 1;
                this.doneCondition.signalAll();

                for (FutureCallback<T> callback : this.callbacks) {
                    try {
                        this.callback(callback);
                    } catch (Throwable var5) {
                        log.error(String.format("%s Exception callback [%s] An exception occurred during execution", var5, callback));
                    }
                }

                return true;
            }
    }

    public boolean startRun() {
        return LockUtil.runWithLock(this.lock, () -> {
            if (this.status != 0) {
                return false;
            } else {
                this.status = 3;
                return true;
            }
        });
    }
}
