package com.todo;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

public class SimpleHttpServer {

    public static void main(String[] args) throws IOException {
        int port = 8000;
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);

        // Mapping of endpoint paths to their corresponding handlers
        Map<String, HttpHandler> endpoints = new HashMap<>();
        endpoints.put("/hello", new HelloHandler());
        endpoints.put("/todos", new GetAllHandler());
        endpoints.put("/todos/add", new AddHandler());
        endpoints.put("/todos/delete", new DeleteHandler());
        endpoints.put("/todos/update", new UpdateHandler());

        // Register handlers for each endpoint
        for (Map.Entry<String, HttpHandler> entry : endpoints.entrySet()) {
            server.createContext(entry.getKey(), entry.getValue());
        }

        // Start the server
        server.start();

        System.out.println("Server is listening on port " + port);
    }
}
