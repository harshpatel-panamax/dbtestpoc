package org.panamax.poc;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.SerializerFactory;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.CompatibleFieldSerializer;
import com.esotericsoftware.kryo.util.DefaultInstantiatorStrategy;
import org.objenesis.strategy.StdInstantiatorStrategy;
import org.panamax.poc.useraccountcachemanager.MatrixId;
import org.panamax.poc.useraccountcachemanager.MatrixRecorder;
import org.panamax.poc.useraccountcachemanager.UserAccountData;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class KryoObjectCodec implements ObjectCodec {

    private Map<String, MatrixId> ids = new ConcurrentHashMap<>();
    private final ThreadLocal<Kryo> kryo = ThreadLocal.withInitial(KryoObjectCodec::createKyro);
    private final MatrixRecorder recorder;

    public KryoObjectCodec(MatrixRecorder recorder) {
        this.recorder = recorder;
    }

    private static Kryo createKyro() {
        Kryo kryo = new Kryo();
        kryo.setDefaultSerializer(CompatibleFieldSerializer.class);
        kryo.register(UserAccountData.class, 200);
        kryo.setInstantiatorStrategy(new DefaultInstantiatorStrategy(new StdInstantiatorStrategy()));
        kryo.setRegistrationRequired(false);
        kryo.setReferences(true);
        /*CompatibleFieldSerializer.CompatibleFieldSerializerConfig config =
                new CompatibleFieldSerializer.CompatibleFieldSerializerConfig();
        config.setReadUnknownFieldData(true);
        config.setChunkedEncoding(true);
        kryo.setDefaultSerializer(new SerializerFactory.CompatibleFieldSerializerFactory(config));*/
        kryo.setWarnUnregisteredClasses(true);
        return kryo;
    }

    @Override
    public byte[] serialize(Object obj) {
        MatrixId matrixId = ids.computeIfAbsent(obj.getClass().getName(), name -> {
            HashMap<String, String> tags = new HashMap<>();
            tags.put("class", name);
            return new MatrixId("serialize", tags);
        });
        return recorder.record(matrixId , () -> {
            Output output = new Output(1024, 1024*5);
            kryo.get().writeObject(output, obj);
            return output.toBytes();
        });
    }


    @Override
    public <T> T deserialize(byte[] bytes, Class<? extends T> claszz) {
        MatrixId matrixId = ids.computeIfAbsent(claszz.getName(), name -> {
            HashMap<String, String> tags = new HashMap<>();
            tags.put("class", name);
            return new MatrixId("deserialize", tags);
        });
        return recorder.record(matrixId , () -> kryo.get().readObject(new Input(bytes), claszz));
    }
}
