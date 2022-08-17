package com.panamax.dbtestpoc;

import org.h2.tools.RunScript;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

public class H2DB {
    private final String url;
    private List<Connection> connections = new CopyOnWriteArrayList<>();

    public H2DB(String url) {
        this.url = url;
    }

    public static H2DB inMemory() throws RuntimeException {
        try {
            Class.forName ("org.h2.Driver");
            return new H2DB(String.format("jdbc:h2:mem:%s", UUID.randomUUID()));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public H2DB schema(String s) {
        try {
            URL resource = H2DB.class.getClassLoader().getResource(s);
            URI uri = resource.toURI();
            RunScript.execute(connection(), Files.newBufferedReader(Paths.get(uri)));
        } catch (URISyntaxException | IOException | SQLException e) {
            throw new RuntimeException(e);
        }
        return this;
    }



    public Connection connection() {
        try {
            Connection connection = DriverManager.getConnection(url, "", "");
            connections.add(connection);
            return connection;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void shutdown() {
        connections.stream().filter(connection -> {
            try {
                return !connection.isClosed();
            } catch (SQLException e) {
                return true;
            }
        }).forEach(connection -> closeQuietly(connection));
    }

    private void closeQuietly(Connection connection) {
        try {
            connection.close();
        } catch (SQLException e) {
        }
    }
}
