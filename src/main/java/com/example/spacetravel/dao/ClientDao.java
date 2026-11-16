package com.example.spacetravel.dao;

import com.example.spacetravel.model.Client;
import java.util.List;

public interface ClientDao {
    long create(Client client);
    Client findById(long id);
    boolean updateName(long id, String name);
    boolean deleteById(long id);
    List<Client> listAll();
}
