package org.example;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;

import java.util.List;

public class RepoReader implements IRepoReader<Integer, Reader> {

    private SessionFactory sessionFactory;

    private static final Logger logger = LogManager.getLogger();

    public RepoReader() {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure() // configures settings from hibernate.cfg.xml
                .build();
        try {
            sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        } catch (Exception e) {
            System.err.println("Exception " + e);
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }


    @Override
    public Reader findOneById(Integer integer) {
        return null;
    }

    @Override
    public Iterable<Reader> findAll() {
        try (Session session = sessionFactory.openSession()) {
            try {
                Transaction transaction = session.beginTransaction();
                Query<Reader> query = session.createQuery("FROM Reader", Reader.class);
                List<Reader> result = query.list();
                transaction.commit();
                return result;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public void save(Reader entity) {

    }

    @Override
    public Reader delete(Integer integer) {
        return null;
    }

    @Override
    public void update(Reader entity, Integer integer) {

    }

    @Override
    public Integer findId(Reader entity) {
        return null;
    }

    @Override
    public Reader findOneByEmail(String email) {
        try (Session session = sessionFactory.openSession()) {
            try {
                Transaction transaction = session.beginTransaction();
                Query<Reader> query = session.createQuery("SELECT e FROM Reader e WHERE e.email=:email", Reader.class);
                query.setParameter("email", email);
                List<Reader> result = query.list();
                transaction.commit();
                if (!result.isEmpty()) {
                    return result.get(0);
                }
                else return null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
