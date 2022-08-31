package org.panamax.poc.redisasyncyupdate.redis;

import java.time.Duration;
import java.util.List;
import java.util.Map;

public interface UpdateOperation {
    boolean set(String key, byte[] val) throws OperationFailedException;

    boolean add(String key, byte[] val) throws OperationFailedException;

    boolean set(String key, String field, byte[] bytes) throws OperationFailedException;

    void remove(String key) throws OperationFailedException;

    void setFields(String key, Map<String, byte[]> fieldValue) throws OperationFailedException;

    void removeField(String key, String field) throws OperationFailedException;

    void removeFields(String key, List<String> fields) throws OperationFailedException;

    void removeAsync(String key, byte[] value) throws OperationFailedException;

    void remove(String key, byte[] value) throws OperationFailedException;

    void remove(String key, List<byte[]> value) throws OperationFailedException;

    void removeFromSortedSet(String key, byte[] data) throws OperationFailedException;

    boolean setIfNotExist(String key, byte[] value) throws OperationFailedException;

    void addToSortedSet(String key, double weight, byte[] value) throws OperationFailedException;

    void removeAll(List<String> keys) throws OperationFailedException;

    void set(String key, byte[] serialize, Duration ttl) throws OperationFailedException;

    void geoAdd(String key, double longitude, double latitude, byte[] value) throws OperationFailedException;

    void geoRemove(String key, byte[] value) throws OperationFailedException;

    long incrementFieldBy(String key, String field, long val);
}
