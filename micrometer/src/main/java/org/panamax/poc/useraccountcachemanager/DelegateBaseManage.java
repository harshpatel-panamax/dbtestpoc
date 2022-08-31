package org.panamax.poc.useraccountcachemanager;


public abstract  class DelegateBaseManage<E> extends BaseManager implements ICacheManager<E> {

    private final ICacheManager<E> cacheManager;
    private final Handler handler;

    private final MatrixRecorder recorder;
    private final MatrixIdGenerator idGenerator;

    protected DelegateBaseManage(ICacheManager<E> cacheManager, Handler handler, MatrixRecorder recorder,
            MatrixIdGenerator idGenerator) {
        this.cacheManager = cacheManager;
        this.handler = handler;
        this.recorder = recorder;
        this.idGenerator = idGenerator;
    }

    protected  <T> T handle(String operationName, SupplierWithException<T> supplier) {
        return recorder.record(idGenerator.generate(operationName), () -> handler.handle(supplier, operationName));

    }

    protected void handle(String operationName, RunnableWithThrow runnable){
        recorder.record(idGenerator.generate(operationName), () -> handler.handle(FunctionalInterfaceUtils.from(runnable), operationName));
    }
}
