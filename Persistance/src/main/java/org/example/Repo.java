package org.example;

public interface Repo<ID, E> {

    E findOneById(ID id);

    Iterable<E> findAll();

    void save(E entity);

    E delete(ID id);

    void update(E entity, ID id);

    ID findId(E entity);

}
