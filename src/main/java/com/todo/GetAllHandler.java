package com.todo;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.todo.Database.DatabaseConnection;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GetAllHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        List<TodoItem> todoItems = getTodoItems();

        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, 0);

        OutputStream outputStream = exchange.getResponseBody();
        String response = JsonConverter.convertToJson(todoItems);
        outputStream.write(response.getBytes());
        outputStream.close();
    }

    private static List<TodoItem> getTodoItems() {
        List<TodoItem> todoItems = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getConnection()) {
            System.out.println("Connection established");
            String sql = "SELECT * FROM todo";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
                ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    TodoItem todoItem = new TodoItem();
                    todoItem.setId(resultSet.getInt("id"));
                    todoItem.setTitle(resultSet.getString("title"));
                    todoItem.setCompleted(resultSet.getBoolean("isCompleted"));
                    todoItems.add(todoItem);
                }
            }
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return todoItems;
        
    }
}
