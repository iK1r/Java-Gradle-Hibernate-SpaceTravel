package com.example.spacetravel.service;

import com.example.spacetravel.model.Client;
import java.util.List;

public interface ClientCrudService {
    long create(String name);
    Client getById(long id);
    void updateName(long id, String name);
    void deleteById(long id);
    List<Client> listAll();
}
