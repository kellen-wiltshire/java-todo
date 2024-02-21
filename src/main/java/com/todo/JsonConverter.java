package com.todo;

import com.google.gson.Gson;

public class JsonConverter {

    private static final Gson gson = new Gson();

    public static String convertToJson(Object object) {
        return gson.toJson(object);
    }

    public static TodoItem convertToTodoItem(String json) {
        return gson.fromJson(json, TodoItem.class);
    }
}
