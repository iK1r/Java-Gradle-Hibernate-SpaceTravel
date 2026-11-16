package com.example.spacetravel.service;

import com.example.spacetravel.EntityNotFoundException;
import com.example.spacetravel.dao.PlanetDao;
import com.example.spacetravel.model.Planet;
import java.util.List;
import java.util.Objects;

public class PlanetCrudServiceImpl implements PlanetCrudService {
    private final PlanetDao dao;

    public PlanetCrudServiceImpl(PlanetDao dao) {
        this.dao = Objects.requireNonNull(dao);
    }

    @Override
    public String create(String id, String name) {
        validateId(id);
        validateName(name);
        return dao.create(new Planet(id, name));
    }

    @Override
    public Planet getById(String id) {
        validateId(id);
        Planet planet = dao.findById(id);
        if (planet == null) throw new EntityNotFoundException("Planet not found: " + id);
        return planet;
    }

    @Override
    public void updateName(String id, String name) {
        validateId(id);
        validateName(name);
        if (!dao.updateName(id, name)) {
            throw new EntityNotFoundException("Planet not found: " + id);
        }
    }

    @Override
    public void deleteById(String id) {
        validateId(id);
        if (!dao.deleteById(id)) {
            throw new EntityNotFoundException("Planet not found: " + id);
        }
    }

    @Override
    public List<Planet> listAll() {
        return dao.listAll();
    }

    private static void validateId(String id) {
        if (id == null || id.isEmpty() || id.length() > 255 || !id.matches("[A-Z0-9]+")) {
            throw new IllegalArgumentException("Planet ID must contain only uppercase Latin letters and digits");
        }
    }

    private static void validateName(String name) {
        if (name == null || name.isBlank() || name.length() < 1 || name.length() > 500) {
            throw new IllegalArgumentException("Planet name must contain 1 to 500 characters");
        }
    }
}
