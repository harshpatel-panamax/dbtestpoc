package org.panamax.poc;

public interface ObjectCodec {
    byte[] serialize(Object obj) ;

    <T> T deserialize(byte[] bytes, Class<? extends T> claszz) throws DeserializationException;


}
