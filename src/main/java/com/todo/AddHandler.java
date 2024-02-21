package com.todo;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.todo.Database.DatabaseConnection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AddHandler implements HttpHandler {

    private Gson gson = new Gson();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if("POST".equals(exchange.getRequestMethod())){
            InputStreamReader isr = new InputStreamReader(exchange.getRequestBody(), "utf-8");
            BufferedReader br = new BufferedReader(isr);
            StringBuilder requestBody = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                requestBody.append(line);
            }
            br.close();
            try {
                TodoItem todoItem = gson.fromJson(requestBody.toString(), TodoItem.class);
                
                // No need to use JsonConverter.convertToTodoItem, as todoItem is already parsed
                addTodoItem(todoItem);

    
                exchange.sendResponseHeaders(200, 0);
                OutputStream os = exchange.getResponseBody();
                os.write("Todo item added successfully".getBytes());
                os.close();
    
            } catch (JsonSyntaxException e) {
                e.printStackTrace(); // Handle or log the exception
            }
        } else {
            exchange.sendResponseHeaders(405, -1);
        }
    }

     private static synchronized void addTodoItem(TodoItem item) {
        try (Connection connection = DatabaseConnection.getConnection()){
            String sql = "INSERT INTO todo (title, isCompleted) VALUES (?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, item.getTitle());
            preparedStatement.setBoolean(2, false);
            preparedStatement.executeUpdate();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
