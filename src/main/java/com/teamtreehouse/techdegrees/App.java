package com.teamtreehouse.techdegrees;


import com.google.gson.Gson;
import com.teamtreehouse.techdegrees.dao.Sql2oTodoDao;
import com.teamtreehouse.techdegrees.dao.TodoDao;
import com.teamtreehouse.techdegrees.exc.ApiError;
import com.teamtreehouse.techdegrees.model.Todo;
import org.sql2o.Sql2o;

import static spark.Spark.*;

public class App {

    public static void main(String[] args) {
//        staticFileLocation("/public");
//        get("/api/v1/", (req, res) -> "Hello!");

        Sql2o sql2o = new Sql2o("jdbc:h2:~/todos.db;INIT=RUNSCRIPT from 'classpath:db/init.sql'", "", "");
        TodoDao todoDao = new Sql2oTodoDao(sql2o);
        Gson gson = new Gson();

        post("/api/v1/todos", "application/json", (req, res) -> {
            Todo todo = gson.fromJson(req.body(), Todo.class);
            todoDao.add(todo);
            res.status(201);
            return todo;
        }, gson::toJson);

        get("/api/v1/todos", "application/json",
                (req, res) -> todoDao.findAll(), gson::toJson);

        get("/api/v1/todos/:id", "application/json", (req, res) -> {
            int id = Integer.parseInt(req.params("id"));
            Todo todo = todoDao.findById(id);
            if (todo == null) {
                throw new ApiError(404, "Could not find todo with id " + id);
            }
            return todo;
        }, gson::toJson);

        after((req, res) -> {
            res.type("application/json");
        });
    }
}
