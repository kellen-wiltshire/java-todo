package com.todo;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.todo.Database.DatabaseConnection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class UpdateHandler implements HttpHandler {

    private Gson gson = new Gson();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if("PUT".equals(exchange.getRequestMethod())){
            String query = exchange.getRequestURI().getQuery();
            String[] queryParams = query.split("&");
            int id = -1;
            for (String param : queryParams) {
                if (param.startsWith("id=")) {
                    id = Integer.parseInt(param.substring(3));
                    break;
                }
            }
            InputStreamReader isr = new InputStreamReader(exchange.getRequestBody(), "utf-8");
            BufferedReader br = new BufferedReader(isr);
            StringBuilder requestBody = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                requestBody.append(line);
            }
            br.close();
            TodoItem todoItem = gson.fromJson(requestBody.toString(), TodoItem.class);
    
            if (id != -1) {
                updateTodoItem(id, todoItem);
                exchange.sendResponseHeaders(200, 0);
                OutputStream os = exchange.getResponseBody();
                os.write(("Todo item with id " + id + " updated successfully").getBytes());
                os.close();
            } else {
                exchange.sendResponseHeaders(400, 0); // Bad Request
                OutputStream os = exchange.getResponseBody();
                os.write("Invalid id parameter".getBytes());
                os.close();
            }
        } else {
            exchange.sendResponseHeaders(405, -1);
        }
    }

    private static synchronized void updateTodoItem(int id, TodoItem updatedItem) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String sql = "UPDATE todo SET title=?, isCompleted=? WHERE id=?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, updatedItem.getTitle());
            preparedStatement.setBoolean(2, updatedItem.isCompleted());
            preparedStatement.setInt(3, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
}
