package org.panamax.poc;

import org.panamax.poc.exceptionhandling.LoggingExceptionHandler;
import org.panamax.poc.exceptionhandling.RMICacheManager;
import org.panamax.poc.exceptionhandling.RMIDriver;
import org.panamax.poc.exceptionhandling.RemoteCacheManager;
import org.panamax.poc.exceptionhandling.RemoteDriver;
import org.panamax.poc.exceptionhandling.RetryExceptionHandler;

public class Main {
    public static void main(String[] args) {
        RemoteDriver remoteDriver = new RemoteDriver();

        LoggingExceptionHandler loggingExceptionHandler = new LoggingExceptionHandler();
        RemoteCacheManager remoteCacheManager = new RemoteCacheManager(remoteDriver, loggingExceptionHandler);
        remoteCacheManager.set("test");
        remoteCacheManager.get();

        RMICacheManager rmiCacheManager = new RMICacheManager(new RMIDriver(),
                                                              new RetryExceptionHandler(new LoggingExceptionHandler()));

        rmiCacheManager.get();
    }
}