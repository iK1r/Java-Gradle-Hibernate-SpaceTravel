package com.example.spacetravel;

import com.example.spacetravel.dao.ClientDaoImpl;
import com.example.spacetravel.dao.PlanetDaoImpl;
import com.example.spacetravel.model.Ticket;
import com.example.spacetravel.service.ClientCrudService;
import com.example.spacetravel.service.ClientCrudServiceImpl;
import com.example.spacetravel.service.PlanetCrudService;
import com.example.spacetravel.service.PlanetCrudServiceImpl;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SpaceTravelSelfTest {
    private static final String TEST_URL = "jdbc:h2:mem:spacetravel_test;DB_CLOSE_DELAY=-1";

    public static void main(String[] args) throws Exception {
        check(DatabaseMigrationService.migrate(TEST_URL) == 2, "Exactly two migrations must run");
        check(DatabaseMigrationService.migrate(TEST_URL) == 0, "Migration must not repeat");
        System.out.println("Flyway migrations V1 and V2: OK");

        try (SessionFactory sessionFactory = HibernateUtil.createSessionFactory(TEST_URL)) {
            ClientCrudService clients = new ClientCrudServiceImpl(new ClientDaoImpl(sessionFactory));
            PlanetCrudService planets = new PlanetCrudServiceImpl(new PlanetDaoImpl(sessionFactory));
            check(clients.listAll().size() == 10, "Expected ten clients");
            check(planets.listAll().size() == 5, "Expected five planets");
            try (Session session = sessionFactory.openSession()) {
                long ticketCount = session.createQuery("select count(t) from Ticket t", Long.class)
                        .getSingleResult();
                check(ticketCount == 10, "Expected ten tickets");
                Ticket ticket = session.get(Ticket.class, 1L);
                check(ticket.getCreatedAt() != null, "Ticket must have created_at");
                check(ticket.getClient().getId() == 1L, "Ticket client mapping is wrong");
                check("EARTH".equals(ticket.getFromPlanet().getId()), "From-planet mapping is wrong");
                check("MARS".equals(ticket.getToPlanet().getId()), "To-planet mapping is wrong");
            }
            System.out.println("Seed: 10 clients, 5 planets, 10 tickets and relationships: OK");

            long id = clients.create("Sample Passenger");
            check(id >= 11L, "Identity should continue after seeded clients");
            check("Sample Passenger".equals(clients.getById(id).getName()), "Client CREATE/READ failed");
            clients.updateName(id, "Updated Passenger");
            check("Updated Passenger".equals(clients.getById(id).getName()), "Client UPDATE failed");
            clients.deleteById(id);
            expectError(EntityNotFoundException.class, () -> clients.getById(id));
            expectError(IllegalArgumentException.class, () -> clients.create("Jo"));
            expectError(IllegalArgumentException.class, () -> clients.create("A".repeat(201)));
            expectError(IllegalArgumentException.class, () -> clients.deleteById(-1));
            System.out.println("Client CRUD and validation: OK");

            String planetId = planets.create("NEW9", "New Planet");
            check("NEW9".equals(planetId), "Planet CREATE failed");
            check("New Planet".equals(planets.getById(planetId).getName()), "Planet READ failed");
            planets.updateName(planetId, "Updated Planet");
            check("Updated Planet".equals(planets.getById(planetId).getName()), "Planet UPDATE failed");
            planets.deleteById(planetId);
            expectError(EntityNotFoundException.class, () -> planets.getById(planetId));
            expectError(IllegalArgumentException.class, () -> planets.create("mars", "Invalid"));
            expectError(IllegalArgumentException.class, () -> planets.create("BAD-1", "Invalid"));
            expectError(IllegalArgumentException.class, () -> planets.create("NEW1", ""));
            expectError(IllegalArgumentException.class, () -> planets.create("NEW1", "A".repeat(501)));
            System.out.println("Planet CRUD and validation: OK");
        }

        checkDatabaseConstraints();
        System.out.println("H2 CHECK constraints and ticket foreign keys: OK");
        System.out.println("All SpaceTravel tests passed!");
    }

    private static void checkDatabaseConstraints() throws SQLException {
        try (Connection connection = DriverManager.getConnection(TEST_URL, "sa", "")) {
            expectSqlError(connection,
                    "INSERT INTO client(name) VALUES (?)", "Jo");
            expectSqlError(connection,
                    "INSERT INTO planet(id, name) VALUES (?, 'Wrong ID')", "bad");
            expectSqlError(connection,
                    "INSERT INTO ticket(created_at, client_id, from_planet_id, to_planet_id) " +
                    "VALUES (TIMESTAMP '2026-11-16 12:00:00', 999999, 'EARTH', 'MARS')", null);
        }
    }

    private static void expectSqlError(Connection connection, String sql, String input) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            if (input != null) statement.setString(1, input);
            try {
                statement.executeUpdate();
                throw new AssertionError("Database accepted invalid data: " + sql);
            } catch (SQLException expected) {
                // The database itself must reject invalid rows.
            }
        }
    }

    private static void expectError(Class<? extends Throwable> type, Runnable action) {
        try {
            action.run();
        } catch (Throwable error) {
            if (type.isInstance(error)) return;
            throw new AssertionError("Expected " + type.getSimpleName() + " but got " + error, error);
        }
        throw new AssertionError("Expected " + type.getSimpleName());
    }

    private static void check(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }
}
