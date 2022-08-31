package org.panamax.poc.useraccountcachemanager;

public class FunctionalInterfaceUtils {

    public static SupplierWithException<Void> from(RunnableWithThrow runnable) {
        return () -> {
            runnable.run();
            return null;
        };
    }
}
