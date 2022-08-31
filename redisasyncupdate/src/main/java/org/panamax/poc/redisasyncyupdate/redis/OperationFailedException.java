package org.panamax.poc.redisasyncyupdate.redis;

public class OperationFailedException extends RuntimeException {
    public OperationFailedException(Exception exception) {
        super(exception);
    }
}
