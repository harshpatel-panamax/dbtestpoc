package com.panamax.poc.dbtest;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;


public class DbTest {
    private static final H2DB h2DB = H2DB.inMemory().schema("schema.sql");

    @Test
    public void dbTest() throws SQLException {
        Connection conn = h2DB.connection();
        Statement st = conn.createStatement();
        st.executeQuery("select * from user1");
        conn.close();
    }

    @AfterEach
    void tearDown() {
        h2DB.shutdown();
    }
}
