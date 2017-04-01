package com.teamtreehouse.techdegrees.dao;

import com.teamtreehouse.techdegrees.exc.DaoException;
import com.teamtreehouse.techdegrees.model.Todo;

import java.util.List;

public interface TodoDao {
    void add(Todo todo) throws DaoException;
    void save(Todo todo) throws DaoException;
/*
    void delete(Todo todo) throws DaoException;
*/
    List<Todo> findAll();
    Todo findById(int id);
}
