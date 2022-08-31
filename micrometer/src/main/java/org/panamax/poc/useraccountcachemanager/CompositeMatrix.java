package org.panamax.poc.useraccountcachemanager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class CompositeMatrix implements Matrix{

    private final List<Matrix> matrices;

    public CompositeMatrix(Matrix... matrices) {
        this.matrices = new ArrayList<>();
        for (Matrix matrix : matrices) {
            this.matrices.add(matrix);
        }
    }


    @Override
    public void record(long duration, TimeUnit timeUnit) {
        matrices.forEach(matrix -> matrix.record(duration, timeUnit));
    }
}
