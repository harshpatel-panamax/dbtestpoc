package org.panamax.poc.useraccountcachemanager;


import org.panamax.poc.KryoObjectCodec;
import org.panamax.poc.Ttl;

import java.util.Optional;

public class RedisUserAccountCacheDriver {
    private final CacheOperation cacheOperation;
    private final Ttl ttl;

    private final KryoObjectCodec codec;

    public RedisUserAccountCacheDriver(CacheOperation cacheOperation, Ttl ttl, KryoObjectCodec codec) {
        this.cacheOperation = cacheOperation;
        this.ttl = ttl;
        this.codec = codec;
    }


    public UserAccountData getById(String id) {
        return Optional.ofNullable(cacheOperation.get(id))
                       .map(bytes -> codec.deserialize(bytes, UserAccountData.class))
                       .orElse(null);
    }

    public void set(UserAccountData userAccountData) {
        cacheOperation.set(userAccountData.getId(), codec.serialize(userAccountData), ttl.get());
    }
}
