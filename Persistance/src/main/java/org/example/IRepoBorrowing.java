package org.example;

public interface IRepoBorrowing <ID, E> extends Repo<ID, E>{

    Iterable<E> findAllForReader(Integer id);

    Iterable<E> findAllFromTerminal(Integer id);

    void update(E entity, Integer id, Integer id2);
}
