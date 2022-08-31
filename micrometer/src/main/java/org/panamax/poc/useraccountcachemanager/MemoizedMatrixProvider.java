package org.panamax.poc.useraccountcachemanager;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MemoizedMatrixProvider implements MatrixProvider{
    private Map<MatrixId, Matrix> idToMatrix = new ConcurrentHashMap<>();

    private final MeterRegistry registry;

    public MemoizedMatrixProvider(MeterRegistry registry) {
        this.registry = registry;
    }


    @Override
    public Matrix get(MatrixId matrixId) {

        return idToMatrix.computeIfAbsent(matrixId, id -> {
            Timer.Builder timerBuilder = Timer.builder("timer." + matrixId.id());
            Counter.Builder counterBuilder = Counter.builder("counter." + matrixId.id());

            matrixId.tags().forEach(entry -> {
                timerBuilder.tag(entry.getKey(), entry.getValue());
                counterBuilder.tag(entry.getKey(), entry.getValue());
            });

            return new CompositeMatrix(
                    new MicrometerTimerMatrix(timerBuilder.register(registry)),
                    new MicrometerCounterMatrix(counterBuilder.register(registry)));
        });


    }
}
