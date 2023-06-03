package org.example;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;

import java.util.List;

public class RepoBooks implements IRepoBooks<Integer, Book>{

    private SessionFactory sessionFactory;

    public RepoBooks() {
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
    public Book findOneById(Integer integer) {
        return null;
    }

    @Override
    public Iterable<Book> findAll() {
        try (Session session = sessionFactory.openSession()) {
            try {
                Transaction transaction = session.beginTransaction();
                Query<Book> query = session.createQuery("FROM Book", Book.class);
                List<Book> result = query.list();
                transaction.commit();
                return result;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public void save(Book entity) {
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
    public Book delete(Integer integer) {
        try (Session session = sessionFactory.openSession()) {
            try {
                Transaction transaction = session.beginTransaction();
                Book existingBook = session.get(Book.class, integer);
                if (existingBook != null) {
                    session.delete(existingBook);
                }
                transaction.commit();
                return existingBook;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public void update(Book entity, Integer integer) {
        try (Session session = sessionFactory.openSession()) {
            try {
                Transaction transaction = session.beginTransaction();
                Book existingBook = session.get(Book.class, integer);
                if (existingBook != null) {
                    existingBook.setNoOfCopies(entity.getNoOfCopies());
                    existingBook.setAuthor(entity.getAuthor());
                    existingBook.setName(entity.getName());
                    session.update(existingBook);
                }
                transaction.commit();
                transaction.commit();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public Integer findId(Book entity) {
        return null;
    }

    @Override
    public Iterable<Book> findAllBySearch(String searched) {
        try (Session session = sessionFactory.openSession()) {
            try {
                Transaction transaction = session.beginTransaction();
                Query<Book> query = session.createQuery("FROM Book WHERE lower(name) like lower(:searched) OR lower(author) like lower(:searched)", Book.class);
                query.setParameter("searched", "%" + searched + "%");
                List<Book> result = query.list();
                transaction.commit();
                return result;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public Iterable<Book> findAllByTerminal(Integer id) {
        try (Session session = sessionFactory.openSession()) {
            try {
                Transaction transaction = session.beginTransaction();
                Query<Book> query = session.createQuery("SELECT b FROM Book b WHERE b.terminal.id=:id", Book.class);
                query.setParameter("id", id);
                List<Book> result = query.list();
                transaction.commit();
                return result;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}