package org.panamax.poc.useraccountcachemanager;

public interface Handler {

    <T> T handle(SupplierWithException<T> t, String name);
}
