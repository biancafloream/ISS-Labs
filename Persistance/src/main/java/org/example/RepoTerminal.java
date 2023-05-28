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

public class RepoTerminal implements Repo<Integer, Terminal>{

    private SessionFactory sessionFactory;

    private static final Logger logger = LogManager.getLogger();

    public RepoTerminal() {
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
    public Terminal findOneById(Integer integer) {
        return null;
    }

    @Override
    public Iterable<Terminal> findAll() {
        try (Session session = sessionFactory.openSession()) {
            try {
                Transaction transaction = session.beginTransaction();
                Query<Terminal> query = session.createQuery("FROM Terminal", Terminal.class);
                List<Terminal> result = query.list();
                transaction.commit();
                return result;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public void save(Terminal entity) {

    }

    @Override
    public Terminal delete(Integer integer) {
        return null;
    }

    @Override
    public void update(Terminal entity, Integer integer) {

    }

    @Override
    public Integer findId(Terminal entity) {
        return null;
    }
}
