package org.example;

public interface IRepoBorrowing <ID, E> extends Repo<ID, E>{

    Iterable<E> findAllForReader(Integer id);
}
