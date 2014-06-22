package org.mongo.viewer.dao;

public interface Dao<T> {

    T create(T s);

    T read(String key);

    int update(T s);

    int delete(String key);
}
