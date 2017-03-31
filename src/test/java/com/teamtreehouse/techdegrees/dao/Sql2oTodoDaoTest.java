package com.teamtreehouse.techdegrees.dao;

import com.teamtreehouse.techdegrees.model.Todo;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import static org.junit.Assert.*;

public class Sql2oTodoDaoTest {
    private Sql2oTodoDao dao;
    private Connection conn;
    private Todo todo;

    @Before
    public void setUp() throws Exception {
        String connectionString = "jdbc:h2:mem:testing;INIT=RUNSCRIPT from 'classpath:db/init.sql'";
        Sql2o sql2o = new Sql2o(connectionString, "", "");
        dao = new Sql2oTodoDao(sql2o);

        // Keep connection open through entire test so that it isn't wiped out
        conn = sql2o.open();
    }

    @After
    public void tearDown() throws Exception {
        conn.close();
    }

    @Test
    public void addingTodoSetsId() throws Exception {
        Todo todo = newTestTodo();
        int originalTodoId = todo.getId();

        dao.add(todo);

        assertNotEquals(originalTodoId, todo.getId());
    }

    @Test
    public void addedTodosAreReturnedFromFindAll() throws Exception {
        Todo todo = newTestTodo();

        dao.add(todo);

        assertEquals(1, dao.findAll().size());
    }

    @Test
    public void noTodosReturnsEmptyList() throws Exception {
        assertEquals(0, dao.findAll().size());
    }

    @Test
    public void existingTodosCanBeFoundById() throws Exception {
        Todo todo = newTestTodo();
        dao.add(todo);

        Todo foundTodo = dao.findById(todo.getId());

        assertEquals(todo, foundTodo);
    }

    private Todo newTestTodo() {
        return new Todo("Test");
    }
}