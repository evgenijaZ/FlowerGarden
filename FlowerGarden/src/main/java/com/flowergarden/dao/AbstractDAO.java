package com.flowergarden.dao;

import java.util.List;

/**
 * @author Yevheniia Zubrych on 07.03.2018.
 */
public abstract class AbstractDAO<E, K> {

    public abstract List <E> getAll();

    public abstract boolean update(E entity);

    public abstract E getById(K id);

    public abstract boolean delete(E entity);

    public abstract boolean create(E entity);

}