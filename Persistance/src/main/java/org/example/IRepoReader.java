package org.example;

public interface IRepoReader<ID, E> extends Repo<ID, E>{

    E findOneByEmail(String email);
}
