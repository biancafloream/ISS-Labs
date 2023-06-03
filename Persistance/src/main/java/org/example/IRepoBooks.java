package org.example;

public interface IRepoBooks <ID, E> extends Repo<ID, E>{
    Iterable<E> findAllBySearch(String searched);

    Iterable<E> findAllByTerminal(Integer id);
}