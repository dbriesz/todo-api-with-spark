package com.teamtreehouse.techdegrees;

import com.google.gson.Gson;
import com.teamtreehouse.testing.ApiClient;
import com.teamtreehouse.testing.ApiResponse;
import org.junit.*;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import spark.Spark;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class ApiTest {

    private static final String PORT = "4568";
    private static final String TEST_DATASOURCE = "jdbc:h2:mem:testing";
    private Connection conn;
    private ApiClient client;
    private Gson gson;

    @BeforeClass
    public static void startServer() {
        String[] args = {PORT, TEST_DATASOURCE};
        Api.main(args);
    }

    @AfterClass
    public static void stopServer() {
        Spark.stop();
    }

    @Before
    public void setUp() throws Exception {
        Sql2o sql2o = new Sql2o(TEST_DATASOURCE + ";INIT=RUNSCRIPT from 'classpath:db/init.sql'", "", "");
        conn = sql2o.open();
        client = new ApiClient("http://localhost:" + PORT);
        gson = new Gson();
    }

    @After
    public void tearDown() throws Exception {
        conn.close();
    }

    @Test
    public void addingTodosReturnsCreatedStatus() throws Exception {
        Map<String, String> values = new HashMap<>();
        values.put("name", "Test");

        ApiResponse res = client.request("POST", "/api/v1/todos", gson.toJson(values));

        assertEquals(201, res.getStatus());
    }
}