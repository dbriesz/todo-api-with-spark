package com.teamtreehouse.testing;

import spark.utils.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ApiClient {
    private String server;

    // Pass in the server
    public ApiClient(String server) {
        this.server = server;
    }

    public ApiResponse request(String method, String uri) {
        return request(method, uri, null);
    }

    //
    public ApiResponse request(String method, String uri, String requestBody) {
        try {
            // Build URL that comes from the Java.io package by concatenating the known server and the URI that is passed in
            URL url = new URL(server + uri);

            // Open a new connection
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Set the connection as a post or a get
            connection.setRequestMethod(method);

            // Set the connection header
            connection.setRequestProperty("Content-Type", "application/json");

            // If a request body is passed in, tell the connection that it needs to do output
            if (requestBody != null) {
                connection.setDoOutput(true);

                // Open a connection stream and write out all of the bytes (in case there are any Unicode characters)
                try (OutputStream os = connection.getOutputStream()) {
                    os.write(requestBody.getBytes("UTF-8"));
                }
            }

            // Makes the connection happen
            connection.connect();

            // Get back either an input stream (status code < 400) or an error stream (status code > 400) with this ternary operator
            InputStream inputStream = connection.getResponseCode() < 400 ?
                    connection.getInputStream() :
                    connection.getErrorStream();

            // Takes stream and converts it to a String
            String body = IOUtils.toString(inputStream);

            return new ApiResponse(connection.getResponseCode(), body);
        } catch (IOException e) {
            e.printStackTrace();

            // If anything goes wrong, return a RuntimeException so the test can catch it
            throw new RuntimeException("Whoops!  Connection error");
        }
    }
}