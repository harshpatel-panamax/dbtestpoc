package com.panamax.dbtestpoc;


import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

@Testcontainers
public class TestContainerPocTest {

    @Container
    private static final JdbcDatabaseContainer MY_SQL_CONTAINER = new MySQLContainer("mysql:8.0.30")
            .withInitScript("schema.sql");
    private static String url;

    @BeforeAll
    static void beforeAll() {
        MY_SQL_CONTAINER.start();
        url = MY_SQL_CONTAINER.getJdbcUrl();
    }

    @Test
    public void dbTest() throws SQLException {
        Connection conn = DriverManager.getConnection(url);
        Statement st = conn.createStatement();
        st.executeQuery("select * from user1");
        conn.close();
    }

    @AfterAll
    static void afterAll() {
        MY_SQL_CONTAINER.stop();
    }
}
