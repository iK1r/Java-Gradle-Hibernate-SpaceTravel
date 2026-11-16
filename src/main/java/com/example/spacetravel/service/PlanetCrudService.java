package com.example.spacetravel.service;

import com.example.spacetravel.model.Planet;
import java.util.List;

public interface PlanetCrudService {
    String create(String id, String name);
    Planet getById(String id);
    void updateName(String id, String name);
    void deleteById(String id);
    List<Planet> listAll();
}
