package org.panamax.poc.useraccountcachemanager;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class MatrixId {
    private final String id;
    private final Map<String, String> tags;

    public MatrixId(String id, Map<String, String> tags) {
        this.id = id;
        this.tags = tags;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MatrixId matrixId = (MatrixId) o;
        return id.equals(matrixId.id) && tags.equals(matrixId.tags);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tags);
    }

    public String id() {
        return id;
    }

    public Set<Map.Entry<String, String>> tags() {
        return tags.entrySet();
    }
}
