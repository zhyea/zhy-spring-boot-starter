package org.chobit.spring.dlock.interceptor;

import java.util.concurrent.TimeUnit;

/**
 * Base class for redLock operations
 *
 * @author rui.zhang
 */
public class RLockOperation {


    /**
     * RedLock Key
     */
    private String key;


    /**
     * 等待时间
     */
    private long waitTime;


    /**
     * 持有锁的时长
     */
    private long leaseTime;


    /**
     * 时间单元
     */
    private TimeUnit timeUnit;

    /**
     * 是否需要释放锁
     */
    private boolean finallyRelease;


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public long getWaitTime() {
        return waitTime;
    }

    public void setWaitTime(long waitTime) {
        this.waitTime = waitTime;
    }

    public long getLeaseTime() {
        return leaseTime;
    }

    public void setLeaseTime(long leaseTime) {
        this.leaseTime = leaseTime;
    }

    public TimeUnit getTimeUnit() {
        return timeUnit;
    }

    public void setTimeUnit(TimeUnit timeUnit) {
        this.timeUnit = timeUnit;
    }

    public boolean isFinallyRelease() {
        return finallyRelease;
    }

    public void setFinallyRelease(boolean finallyRelease) {
        this.finallyRelease = finallyRelease;
    }
}
