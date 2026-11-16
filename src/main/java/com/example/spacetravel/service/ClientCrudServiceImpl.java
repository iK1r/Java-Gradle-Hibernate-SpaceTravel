package com.example.spacetravel.service;

import com.example.spacetravel.EntityNotFoundException;
import com.example.spacetravel.dao.ClientDao;
import com.example.spacetravel.model.Client;
import java.util.List;
import java.util.Objects;

/** Only validation and business-facing methods live in this service. */
public class ClientCrudServiceImpl implements ClientCrudService {
    private final ClientDao dao;

    public ClientCrudServiceImpl(ClientDao dao) {
        this.dao = Objects.requireNonNull(dao);
    }

    @Override
    public long create(String name) {
        validateName(name);
        return dao.create(new Client(name));
    }

    @Override
    public Client getById(long id) {
        validateId(id);
        Client client = dao.findById(id);
        if (client == null) throw new EntityNotFoundException("Client not found: " + id);
        return client;
    }

    @Override
    public void updateName(long id, String name) {
        validateId(id);
        validateName(name);
        if (!dao.updateName(id, name)) {
            throw new EntityNotFoundException("Client not found: " + id);
        }
    }

    @Override
    public void deleteById(long id) {
        validateId(id);
        if (!dao.deleteById(id)) {
            throw new EntityNotFoundException("Client not found: " + id);
        }
    }

    @Override
    public List<Client> listAll() {
        return dao.listAll();
    }

    private static void validateId(long id) {
        if (id <= 0) throw new IllegalArgumentException("Client ID must be positive");
    }

    private static void validateName(String name) {
        if (name == null || name.isBlank() || name.length() < 3 || name.length() > 200) {
            throw new IllegalArgumentException("Client name must contain 3 to 200 characters");
        }
    }
}
