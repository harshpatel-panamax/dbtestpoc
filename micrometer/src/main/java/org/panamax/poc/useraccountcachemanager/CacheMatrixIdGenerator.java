package org.panamax.poc.useraccountcachemanager;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CacheMatrixIdGenerator implements MatrixIdGenerator{
    public static final String CACHE = "cache.";
    public static final String OPERATION = "operation";
    private final String cacheName;
    public Map<String, MatrixId> operationToId = new ConcurrentHashMap<>();
    public CacheMatrixIdGenerator(String cacheName) {
        this.cacheName = cacheName;
    }

    @Override
    public MatrixId generate(String operationName) {
        return operationToId.computeIfAbsent(operationName, name -> {
            HashMap<String, String> tags = new HashMap<>();
            tags.put(OPERATION, operationName);
            return new MatrixId(CACHE + cacheName, tags);
        });
    }
}
