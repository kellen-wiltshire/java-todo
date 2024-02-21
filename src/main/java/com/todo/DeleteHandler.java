package com.todo;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.todo.Database.DatabaseConnection;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class DeleteHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if("DELETE".equals(exchange.getRequestMethod())){
            String query = exchange.getRequestURI().getQuery();
            String[] queryParams = query.split("&");
            int id = -1;
            for (String param : queryParams) {
                if (param.startsWith("id=")) {
                    id = Integer.parseInt(param.substring(3));
                    break;
                }
            }

            if (id != -1) {
                deleteTodoItem(id);
                exchange.sendResponseHeaders(200, 0);
                OutputStream os = exchange.getResponseBody();
                os.write(("Todo item with id " + id + " deleted successfully").getBytes());
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
    
    private static synchronized void deleteTodoItem(int id) {
        try (Connection connection = DatabaseConnection.getConnection()){
            String sql = "DELETE FROM todo WHERE id=?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, id); 
            preparedStatement.executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }
}
