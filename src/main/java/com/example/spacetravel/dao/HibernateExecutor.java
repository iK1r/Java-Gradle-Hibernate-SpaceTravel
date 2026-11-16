package com.example.spacetravel.dao;

import com.example.spacetravel.DatabaseException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import java.util.Objects;
import java.util.function.Function;

/** All Hibernate sessions and write transactions are managed in the DAO layer. */
public final class HibernateExecutor {
    private final SessionFactory sessionFactory;

    public HibernateExecutor(SessionFactory sessionFactory) {
        this.sessionFactory = Objects.requireNonNull(sessionFactory);
    }

    public <T> T read(Function<Session, T> action) {
        try (Session session = sessionFactory.openSession()) {
            return action.apply(session);
        } catch (RuntimeException e) {
            throw new DatabaseException("Database read failed", e);
        }
    }

    public <T> T write(Function<Session, T> action) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                T result = action.apply(session);
                transaction.commit();
                return result;
            } catch (RuntimeException e) {
                if (transaction.isActive()) {
                    transaction.rollback();
                }
                throw new DatabaseException("Database write failed", e);
            }
        } catch (DatabaseException e) {
            throw e;
        } catch (RuntimeException e) {
            throw new DatabaseException("Cannot open database session", e);
        }
    }
}
