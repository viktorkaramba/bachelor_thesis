package com.unicyb.minitaxi.database.dao;

import java.util.List;

public interface DAO <Entity>{
    boolean add(Entity entity);
    List<Entity> getAll();
    Entity getOne(int ID);
    boolean update(Entity entity);
    boolean delete(int ID);
}
