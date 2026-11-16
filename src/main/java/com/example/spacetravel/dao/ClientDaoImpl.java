package com.example.spacetravel.dao;

import com.example.spacetravel.model.Client;
import org.hibernate.SessionFactory;
import java.util.List;

public class ClientDaoImpl implements ClientDao {
    private final HibernateExecutor database;

    public ClientDaoImpl(SessionFactory sessionFactory) {
        this.database = new HibernateExecutor(sessionFactory);
    }

    @Override
    public long create(Client client) {
        return database.write(session -> {
            session.persist(client);
            return client.getId();
        });
    }

    @Override
    public Client findById(long id) {
        return database.read(session -> session.get(Client.class, id));
    }

    @Override
    public boolean updateName(long id, String name) {
        return database.write(session -> {
            Client client = session.get(Client.class, id);
            if (client == null) return false;
            client.setName(name);
            return true;
        });
    }

    @Override
    public boolean deleteById(long id) {
        return database.write(session -> {
            Client client = session.get(Client.class, id);
            if (client == null) return false;
            session.remove(client);
            return true;
        });
    }

    @Override
    public List<Client> listAll() {
        return database.read(session ->
                session.createQuery("from Client c order by c.id", Client.class).getResultList());
    }
}
