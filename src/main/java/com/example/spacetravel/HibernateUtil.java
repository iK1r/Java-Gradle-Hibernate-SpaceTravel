package com.example.spacetravel;

import com.example.spacetravel.model.Client;
import com.example.spacetravel.model.Planet;
import com.example.spacetravel.model.Ticket;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public final class HibernateUtil {
    private HibernateUtil() {
    }

    // Create one SessionFactory after Flyway has migrated the database.
    // Hibernate settings are loaded from src/main/resources/hibernate.properties.
    public static SessionFactory createSessionFactory(String jdbcUrl) {
        return new Configuration()
                .setProperty("hibernate.connection.url", jdbcUrl)
                .addAnnotatedClass(Client.class)
                .addAnnotatedClass(Planet.class)
                .addAnnotatedClass(Ticket.class)
                .buildSessionFactory();
    }
}
