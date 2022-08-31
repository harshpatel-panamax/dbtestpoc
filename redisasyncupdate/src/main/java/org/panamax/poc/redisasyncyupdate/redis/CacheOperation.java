package org.panamax.poc.redisasyncyupdate.redis;

import io.lettuce.core.GeoArgs;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

public interface CacheOperation extends UpdateOperation {

    byte[] get(String key) throws OperationFailedException;
    byte[] get(String key, Duration ttl) throws OperationFailedException;

    Set<byte[]> getAll(String key) throws OperationFailedException;

    Map<String, byte[]> getAllFieldValue(String key)throws OperationFailedException;

    byte[] getFieldValue(String key, String field) throws OperationFailedException;


    List<String> getFields(String key) throws OperationFailedException;

    boolean exist(String key) throws OperationFailedException;

    Map<String,byte[]> getFieldsValue(String key, List<String> fields) throws OperationFailedException;

    long size(String batchId) throws OperationFailedException;

    List<byte[]> range(String key, int start, int end) throws OperationFailedException;

    Stream<byte[]> searchByPrefix(String key, String prefix) throws OperationFailedException;

    Set<byte[]> searchWithInRadius(String key, double latitude, double longitude, double radius, GeoArgs.Unit unit) throws OperationFailedException;

}
