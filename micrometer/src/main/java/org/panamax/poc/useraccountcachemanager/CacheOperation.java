package org.panamax.poc.useraccountcachemanager;

import io.lettuce.core.GeoArgs;
import org.panamax.poc.OperationFailedException;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

public interface CacheOperation {
    boolean set(String key, byte[] val) throws OperationFailedException;

    byte[] get(String key) throws OperationFailedException;
    byte[] get(String key, Duration ttl) throws OperationFailedException;

    boolean add(String key, byte[] val) throws OperationFailedException;

    Set<byte[]> getAll(String key) throws OperationFailedException;

    boolean set(String key, String field, byte[] bytes) throws OperationFailedException;

    Map<String, byte[]> getAllFieldValue(String key)throws OperationFailedException;

    void remove(String key) throws OperationFailedException;

    void setFields(String key, Map<String, byte[]> fieldValue) throws OperationFailedException;

    void removeField(String key, String field) throws OperationFailedException;

    void removeFields(String key, List<String> fields) throws OperationFailedException;

    byte[] getFieldValue(String key, String field) throws OperationFailedException;


    List<String> getFields(String key) throws OperationFailedException;

    void removeAsync(String key, byte[] value) throws OperationFailedException;

    void remove(String key, byte[] value) throws OperationFailedException;
    void remove(String key, List<byte[]> value) throws OperationFailedException;

    boolean exist(String key) throws OperationFailedException;

    Map<String,byte[]> getFieldsValue(String key, List<String> fields) throws OperationFailedException;

    long size(String batchId) throws OperationFailedException;

    List<byte[]> range(String key, int start, int end) throws OperationFailedException;

    void removeFromSortedSet(String key, byte[] data) throws OperationFailedException;

    Stream<byte[]> searchByPrefix(String key, String prefix) throws OperationFailedException;

    boolean setIfNotExist(String key, byte[] value) throws OperationFailedException;

    void addToSortedSet(String key, double weight, byte[] value) throws OperationFailedException;

    void removeAll(List<String> keys) throws OperationFailedException;

    void set(String key, byte[] serialize, Duration ttl) throws OperationFailedException;

    void geoAdd(String key, double longitude, double latitude, byte[] value) throws OperationFailedException;

    void geoRemove(String key, byte[] value) throws OperationFailedException;

    Set<byte[]> searchWithInRadius(String key, double latitude, double longitude, double radius, GeoArgs.Unit unit) throws OperationFailedException;
    long incrementFieldBy(String key, String field, long val);

}
