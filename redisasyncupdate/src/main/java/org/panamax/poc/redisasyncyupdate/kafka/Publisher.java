package org.panamax.poc.redisasyncyupdate.kafka;

public interface Publisher<T> {
    void publish(Publisher<T> message);
}
