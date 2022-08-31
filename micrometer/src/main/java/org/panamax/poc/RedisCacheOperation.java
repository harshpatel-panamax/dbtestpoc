package org.panamax.poc;

import io.lettuce.core.GeoArgs;
import io.lettuce.core.GetExArgs;
import io.lettuce.core.KeyValue;
import io.lettuce.core.MapScanCursor;
import io.lettuce.core.ScanArgs;
import io.lettuce.core.Value;
import io.lettuce.core.cluster.api.StatefulRedisClusterConnection;
import io.lettuce.core.cluster.api.async.RedisAdvancedClusterAsyncCommands;
import io.lettuce.core.cluster.api.sync.RedisAdvancedClusterCommands;
import org.panamax.poc.useraccountcachemanager.CacheOperation;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.Spliterator;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class RedisCacheOperation implements CacheOperation {

    private static final long ZERO = 0L;
    private final RedisAdvancedClusterCommands<String, byte[]> redisCommand;
    private final RedisAdvancedClusterAsyncCommands<String, byte[]> asyncRedisCommand;

    public RedisCacheOperation(StatefulRedisClusterConnection<String, byte[]> connection) {
        this.redisCommand = connection.sync();
        this.asyncRedisCommand = connection.async();
    }

    @Override
    public boolean set(String key, byte[] value) {
        try {
            redisCommand.set(key, value);
            waitForReplication();
        }catch (Exception e){
            throw new OperationFailedException(e);
        }

        return true;
    }

    @Override
    public byte[] get(String key) {
        try {
            return redisCommand.get(key);
        } catch (Exception e) {
            throw new OperationFailedException(e);
        }
    }

    @Override
    public byte[] get(String key, Duration ttl) throws OperationFailedException {
        try {
            return redisCommand.getex(key, new GetExArgs().px(ttl));
        } catch (Exception e) {
            throw new OperationFailedException(e);
        }
    }

    @Override
    public boolean add(String key, byte[] value) {
        try {
            boolean success = redisCommand.sadd(key, value) > 0;
            waitForReplication();
            return success;
        } catch (Exception e) {
            throw new OperationFailedException(e);
        }
    }

    @Override
    public Set<byte[]> getAll(String key) {
        try {
            return redisCommand.smembers(key);
        } catch (Exception e) {
            throw new OperationFailedException(e);
        }
    }

    @Override
    public boolean set(String key, String field, byte[] value) {
        try {
            boolean success = redisCommand.hset(key, field, value);
            waitForReplication();
            return success;
        } catch (Exception e) {
            throw new OperationFailedException(e);
        }
    }

    @Override
    public Map<String, byte[]> getAllFieldValue(String key) {
        try {
            return redisCommand.hgetall(key);
        } catch (Exception e) {
            throw new OperationFailedException(e);
        }
    }

    @Override
    public void remove(String key) {
        try {
            redisCommand.unlink(key);
            waitForReplication();
        } catch (Exception e) {
            throw new OperationFailedException(e);
        }
    }

    private void waitForReplication() {
        try {
            redisCommand.waitForReplication(1, TimeUnit.SECONDS.toMillis(2));
        } catch (Exception e) {
            // FIXME: 8/16/2022 user logger
        }
    }

    @Override
    public void setFields(String key, Map<String, byte[]> fieldValue) {
        try {
            for (Map.Entry<String, byte[]> entry : fieldValue.entrySet()) {
                set(key, entry.getKey(), entry.getValue());
            }
        } catch (Exception e) {
            throw new OperationFailedException(e);
        }
    }

    @Override
    public void removeField(String key, String field) {
        try {
            redisCommand.hdel(key, field);
            waitForReplication();
        } catch (Exception e) {
            throw new OperationFailedException(e);
        }
    }

    @Override
    public void removeFields(String key, List<String> fields) {
        try {
            redisCommand.hdel(key, fields.toArray(new String[0]));
            waitForReplication();
        } catch (Exception e) {
            throw new OperationFailedException(e);
        }
    }

    @Override
    public byte[] getFieldValue(String key, String field) {
        try {
            return redisCommand.hget(key, field);
        } catch (Exception e) {
            throw new OperationFailedException(e);
        }
    }

    @Override
    public List<String> getFields(String key) {
        try {
            return redisCommand.hkeys(key);
        } catch (Exception e) {
            throw new OperationFailedException(e);
        }
    }

    @Override
    public void removeAsync(String key, byte[] value) {
        try {
            asyncRedisCommand.srem(key, value);
            waitForReplication();
        } catch (Exception e) {
            throw new OperationFailedException(e);
        }
    }

    @Override
    public void remove(String key, byte[] value) {
        try {
            redisCommand.srem(key, value);
            waitForReplication();
        } catch (Exception e) {
            throw new OperationFailedException(e);
        }
    }

    @Override
    public boolean exist(String key) {
        try {
            return redisCommand.exists(key) > 0;
        } catch (Exception e) {
            throw new OperationFailedException(e);
        }
    }

    @Override
    public Map<String, byte[]> getFieldsValue(String key, List<String> fields) {
        try {
            return redisCommand.hmget(key, fields.toArray(new String[0]))
                               .stream()
                               .filter(Value::hasValue)
                               .collect(Collectors.toMap(KeyValue::getKey, KeyValue::getValue));
        } catch (Exception e) {
            throw new OperationFailedException(e);
        }
    }

    @Override
    public void remove(String key, List<byte[]> value) {
        try {
            asyncRedisCommand.srem(key, value.toArray(new byte[0][]));
            waitForReplication();
        } catch (Exception e) {
            throw new OperationFailedException(e);
        }
    }

    @Override
    public long size(String batchId) {
        try {
            return Optional.ofNullable(redisCommand.scard(batchId)).orElse(ZERO);
        } catch (Exception e) {
            throw new OperationFailedException(e);
        }

    }

    @Override
    public List<byte[]> range(String key, int start, int end) {
        try {
            return redisCommand.zrange(key, start, end);
        } catch (Exception e) {
            throw new OperationFailedException(e);
        }
    }

    @Override
    public void removeFromSortedSet(String key, byte[] data) {
        try {
            redisCommand.zrem(key, data);
        } catch (Exception exception) {
            throw new OperationFailedException(exception);
        }
    }

    @Override
    public Stream<byte[]> searchByPrefix(String key, String prefix) {
        ScanArgs args = ScanArgs.Builder.limit(Integer.MAX_VALUE).match(prefix + "*");
        MapScanCursor<String, byte[]> hscan;
        try {
            hscan = redisCommand.hscan(key, args);
        } catch (Exception e) {
            throw new OperationFailedException(e);
        }

        Map<String, byte[]> map = hscan.getMap();

        Spliterator<byte[]> spliterator = new Spliterator<byte[]>() {

            Spliterator<byte[]> stringIterator = map.values().stream().spliterator();
            MapScanCursor<String, byte[]> cursor = hscan;

            @Override
            public boolean tryAdvance(Consumer<? super byte[]> action) {
                boolean success = stringIterator.tryAdvance(action);
                if (success) {
                    return true;
                }
                if (!cursor.isFinished()) {
                    try {
                        cursor = redisCommand.hscan(key, cursor, args);
                    } catch (Exception e) {
                        throw new OperationFailedException(e);
                    }
                    stringIterator = cursor.getMap().values().stream().spliterator();
                    return stringIterator.tryAdvance(action);
                }
                return false;
            }

            @Override
            public Spliterator<byte[]> trySplit() {
                return null;
            }

            @Override
            public long estimateSize() {
                return 0;
            }

            @Override
            public int characteristics() {
                return 0;
            }
        };

        return StreamSupport.stream(spliterator, false);
    }

    @Override
    public boolean setIfNotExist(String key, byte[] value) {
        try {
            boolean success = redisCommand.setnx(key, value);
            waitForReplication();
            return success;
        } catch (Exception e) {
            throw new OperationFailedException(e);
        }
    }

    @Override
    public void addToSortedSet(String key, double weight, byte[] value) {
        try {
            redisCommand.zadd(key, weight, value);
            waitForReplication();
        } catch (Exception e) {
            throw new OperationFailedException(e);
        }
    }

    @Override
    public void removeAll(List<String> keys) {
        try {
            redisCommand.unlink(keys.toArray(new String[0]));
            waitForReplication();
        } catch (Exception e) {
            throw new OperationFailedException(e);
        }
    }

    @Override
    public void set(String key, byte[] serialize, Duration ttl) {
        try {
            if (ttl != Duration.ZERO) {
                redisCommand.psetex(key, ttl.toMillis(), serialize);
            } else {
                redisCommand.set(key, serialize);
            }
            waitForReplication();
        }catch (Exception e){
            throw new OperationFailedException(e);
        }
    }

    @Override
    public void geoAdd(String key, double longitude, double latitude, byte[] value) {
        try {
            redisCommand.geoadd(key, longitude, latitude, value);
            waitForReplication();
        } catch (Exception e) {
            throw new OperationFailedException(e);
        }
    }

    @Override
    public void geoRemove(String key, byte[] value) {
        removeFromSortedSet(key, value);
    }

    @Override
    public Set<byte[]> searchWithInRadius(String key, double latitude, double longitude, double radius,
            GeoArgs.Unit unit) {
        try {
            return redisCommand.georadius(key, latitude, longitude, radius, unit);
        } catch (Exception e) {
            throw new OperationFailedException(e);
        }
    }

    @Override
    public long incrementFieldBy(String key, String field, long val) {
        try {
            return redisCommand.hincrby(key, field, val);
        } catch (Exception e) {
            throw new OperationFailedException(e);
        }
    }
}
