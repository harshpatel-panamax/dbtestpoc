package org.panamax.poc.useraccountcachemanager;

public class ExceptionLoggingHandler implements Handler{
        public <T> T handle(SupplierWithException<T> t, String name) {
            try {
                return t.get();
            } catch (Exception e) {
                System.out.println(String.format("Problem occurred while %s. %s", name, e.getMessage()));
                e.printStackTrace();
            }
            return null;
        }
}
