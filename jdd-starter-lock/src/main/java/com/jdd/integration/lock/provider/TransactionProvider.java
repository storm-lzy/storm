package com.jdd.integration.lock.provider;

import com.jdd.integration.lock.enums.UnLockOpportunityEnum;
import com.jdd.integration.lock.lock.Lock;
import org.springframework.transaction.support.TransactionSynchronization;

/**
 * @author 李治毅
 */
public class TransactionProvider {
    public static TransactionSynchronization buildTransactionSynchronization(UnLockOpportunityEnum unLockOpportunityEnum, Lock lock) {
        switch (unLockOpportunityEnum) {
            case BEFORE_COMPLETION:
                return createBeforeCompletion(lock);
            case AFTER_COMPLETION:
                return createAfterCompletion(lock);
            default:
                return createBeforeCommit(lock);
        }
    }

    private static TransactionSynchronization createAfterCompletion(Lock lock) {
        return new TransactionSynchronization() {
            @Override
            public void afterCompletion(int status) {
                lock.release();
            }
        };
    }

    private static TransactionSynchronization createBeforeCompletion(Lock lock) {
        return new TransactionSynchronization() {
            @Override
            public void beforeCompletion() {
                lock.release();
            }
        };
    }

    private static TransactionSynchronization createBeforeCommit(Lock lock) {
        return new TransactionSynchronization() {
            @Override
            public void beforeCompletion() {
                lock.release();
            }
        };
    }
}
