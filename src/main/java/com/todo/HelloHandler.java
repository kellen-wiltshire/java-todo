package com.todo;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;

public class HelloHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        // Set the response headers
        exchange.getResponseHeaders().set("Content-Type", "text/plain");
        exchange.sendResponseHeaders(200, 0);

        // Write the response body
        OutputStream outputStream = exchange.getResponseBody();
        String response = "Hello, World!";
        outputStream.write(response.getBytes());
        outputStream.close();
    }
}