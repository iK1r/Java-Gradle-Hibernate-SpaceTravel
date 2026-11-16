package com.example.spacetravel.dao;

import com.example.spacetravel.model.Planet;
import org.hibernate.SessionFactory;
import java.util.List;

public class PlanetDaoImpl implements PlanetDao {
    private final HibernateExecutor database;

    public PlanetDaoImpl(SessionFactory sessionFactory) {
        this.database = new HibernateExecutor(sessionFactory);
    }

    @Override
    public String create(Planet planet) {
        return database.write(session -> {
            session.persist(planet);
            return planet.getId();
        });
    }

    @Override
    public Planet findById(String id) {
        return database.read(session -> session.get(Planet.class, id));
    }

    @Override
    public boolean updateName(String id, String name) {
        return database.write(session -> {
            Planet planet = session.get(Planet.class, id);
            if (planet == null) return false;
            planet.setName(name);
            return true;
        });
    }

    @Override
    public boolean deleteById(String id) {
        return database.write(session -> {
            Planet planet = session.get(Planet.class, id);
            if (planet == null) return false;
            session.remove(planet);
            return true;
        });
    }

    @Override
    public List<Planet> listAll() {
        return database.read(session ->
                session.createQuery("from Planet p order by p.id", Planet.class).getResultList());
    }
}
