package org.panamax.poc.redisasyncyupdate.kafka;

import org.panamax.poc.redisasyncyupdate.redis.OperationFailedException;
import org.panamax.poc.redisasyncyupdate.redis.UpdateOperation;

import java.time.Duration;
import java.util.List;
import java.util.Map;

public class AsyncCacheOperation implements UpdateOperation {

    private final Publisher<Message> messagePublisher;

    public AsyncCacheOperation(Publisher<Message> messagePublisher) {
        this.messagePublisher = messagePublisher;
    }

    @Override
    public boolean set(String key, byte[] val) throws OperationFailedException {
        messagePublisher.publish(messagePublisher);
        return false;
    }

    @Override
    public boolean add(String key, byte[] val) throws OperationFailedException {
        return false;
    }

    @Override
    public boolean set(String key, String field, byte[] bytes) throws OperationFailedException {
        return false;
    }

    @Override
    public void remove(String key) throws OperationFailedException {

    }

    @Override
    public void setFields(String key, Map<String, byte[]> fieldValue) throws OperationFailedException {

    }

    @Override
    public void removeField(String key, String field) throws OperationFailedException {

    }

    @Override
    public void removeFields(String key, List<String> fields) throws OperationFailedException {

    }

    @Override
    public void removeAsync(String key, byte[] value) throws OperationFailedException {

    }

    @Override
    public void remove(String key, byte[] value) throws OperationFailedException {

    }

    @Override
    public void remove(String key, List<byte[]> value) throws OperationFailedException {

    }

    @Override
    public void removeFromSortedSet(String key, byte[] data) throws OperationFailedException {

    }

    @Override
    public boolean setIfNotExist(String key, byte[] value) throws OperationFailedException {
        return false;
    }

    @Override
    public void addToSortedSet(String key, double weight, byte[] value) throws OperationFailedException {

    }

    @Override
    public void removeAll(List<String> keys) throws OperationFailedException {

    }

    @Override
    public void set(String key, byte[] serialize, Duration ttl) throws OperationFailedException {

    }

    @Override
    public void geoAdd(String key, double longitude, double latitude, byte[] value) throws OperationFailedException {

    }

    @Override
    public void geoRemove(String key, byte[] value) throws OperationFailedException {

    }

    @Override
    public long incrementFieldBy(String key, String field, long val) {
        return 0;
    }
}
