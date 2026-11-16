package com.example.spacetravel.dao;

import com.example.spacetravel.model.Planet;
import java.util.List;

public interface PlanetDao {
    String create(Planet planet);
    Planet findById(String id);
    boolean updateName(String id, String name);
    boolean deleteById(String id);
    List<Planet> listAll();
}
