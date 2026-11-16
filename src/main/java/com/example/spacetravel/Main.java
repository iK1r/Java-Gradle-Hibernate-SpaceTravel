package com.example.spacetravel;

import com.example.spacetravel.dao.ClientDao;
import com.example.spacetravel.dao.ClientDaoImpl;
import com.example.spacetravel.dao.PlanetDao;
import com.example.spacetravel.dao.PlanetDaoImpl;
import com.example.spacetravel.service.ClientCrudService;
import com.example.spacetravel.service.ClientCrudServiceImpl;
import com.example.spacetravel.service.PlanetCrudService;
import com.example.spacetravel.service.PlanetCrudServiceImpl;
import org.hibernate.SessionFactory;

public class Main {
    public static void main(String[] args) {
        DatabaseConfig.prepareLocalDirectory();
        DatabaseMigrationService.migrate(DatabaseConfig.JDBC_URL);

        try (SessionFactory sessionFactory = HibernateUtil.createSessionFactory(DatabaseConfig.JDBC_URL)) {
            ClientDao clientDao = new ClientDaoImpl(sessionFactory);
            PlanetDao planetDao = new PlanetDaoImpl(sessionFactory);
            ClientCrudService clients = new ClientCrudServiceImpl(clientDao);
            PlanetCrudService planets = new PlanetCrudServiceImpl(planetDao);

            System.out.println("Clients in database: " + clients.listAll().size());
            System.out.println("Planets in database: " + planets.listAll().size());
            System.out.println("Planet MARS: " + planets.getById("MARS").getName());

            // Demo CRUD without modifying or deleting the seeded data.
            long temporaryClientId = clients.create("Test Explorer");
            System.out.println("Created client ID: " + temporaryClientId);
            System.out.println("Read client: " + clients.getById(temporaryClientId).getName());
            clients.updateName(temporaryClientId, "Updated Explorer");
            System.out.println("Updated client: " + clients.getById(temporaryClientId).getName());
            clients.deleteById(temporaryClientId);
            System.out.println("Deleted client ID: " + temporaryClientId);

            String temporaryPlanetId = planets.create("NEW9", "New Planet");
            System.out.println("Created planet: " + temporaryPlanetId);
            System.out.println("Read planet: " + planets.getById(temporaryPlanetId).getName());
            planets.updateName(temporaryPlanetId, "Updated Planet");
            System.out.println("Updated planet: " + planets.getById(temporaryPlanetId).getName());
            planets.deleteById(temporaryPlanetId);
            System.out.println("Deleted planet: " + temporaryPlanetId);
            System.out.println("All CRUD demo operations completed successfully!");
        }
    }
}
