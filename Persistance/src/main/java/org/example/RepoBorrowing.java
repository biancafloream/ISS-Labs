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

public class RepoBorrowing implements IRepoBorrowing<Integer, Borrowing>{

    private SessionFactory sessionFactory;

    private static final Logger logger = LogManager.getLogger();

    public RepoBorrowing() {
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
    public Borrowing findOneById(Integer integer) {
        return null;
    }

    @Override
    public Iterable<Borrowing> findAll() {
        try (Session session = sessionFactory.openSession()) {
            try {
                Transaction transaction = session.beginTransaction();
                Query<Borrowing> query = session.createQuery("FROM Borrowing", Borrowing.class);
                List<Borrowing> result = query.list();
                transaction.commit();
                return result;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public void save(Borrowing entity) {
        try (Session session = sessionFactory.openSession()) {
            try {
                Transaction transaction = session.beginTransaction();
                session.save(entity);
                transaction.commit();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public Borrowing delete(Integer integer) {
        return null;
    }

    @Override
    public void update(Borrowing entity, Integer integer) {

    }

    @Override
    public Integer findId(Borrowing entity) {
        return null;
    }

    @Override
    public Iterable<Borrowing> findAllForReader(Integer id) {
        try (Session session = sessionFactory.openSession()) {
            try {
                Transaction transaction = session.beginTransaction();
                Query<Borrowing> query = session.createQuery("SELECT b FROM Borrowing b WHERE b.reader.id=:id", Borrowing.class);
                query.setParameter("id", id);
                List<Borrowing> result = query.list();
                transaction.commit();
                return result;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public Iterable<Borrowing> findAllFromTerminal(Integer id) {
        try (Session session = sessionFactory.openSession()) {
            try {
                Transaction transaction = session.beginTransaction();
                Query<Borrowing> query = session.createQuery("SELECT b FROM Borrowing b WHERE b.book.terminal.id=:id", Borrowing.class);
                query.setParameter("id", id);
                List<Borrowing> result = query.list();
                transaction.commit();
                return result;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public void update(Borrowing entity, Integer idBook, Integer idReader) {
        try (Session session = sessionFactory.openSession()) {
            try {
                Transaction transaction = session.beginTransaction();
                Query<Borrowing> query = session.createQuery("SELECT b FROM Borrowing b WHERE b.book.id=:id1 and b.reader.id=:id2", Borrowing.class);
                query.setParameter("id1", idBook);
                query.setParameter("id2", idReader);
                List<Borrowing> result = query.list();
                if (!result.isEmpty()) {
                    Borrowing existingBorrowing = result.get(0);
                    existingBorrowing.setStatus(entity.getStatus());
                    session.update(existingBorrowing);
                }
                transaction.commit();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}