package org.panamax.poc.useraccountcachemanager;

public interface MatrixProvider {
    Matrix get(MatrixId id);
}
