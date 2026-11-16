INSERT INTO client (id, name) VALUES
    (1, 'Alice Carter'),
    (2, 'Daniel Reed'),
    (3, 'Sofia Martinez'),
    (4, 'Oliver Price'),
    (5, 'Emma Wilson'),
    (6, 'Lucas Green'),
    (7, 'Mia Johnson'),
    (8, 'Noah Brown'),
    (9, 'Lily Taylor'),
    (10, 'Ethan Adams');

-- Identity values must not collide with the ten inserted clients.
ALTER TABLE client ALTER COLUMN id RESTART WITH 11;

INSERT INTO planet (id, name) VALUES
    ('EARTH', 'Earth'),
    ('MARS', 'Mars'),
    ('VEN', 'Venus'),
    ('JUPITER', 'Jupiter'),
    ('SATURN', 'Saturn');

-- Times below are written explicitly as UTC timestamps (without time zone).
INSERT INTO ticket (id, created_at, client_id, from_planet_id, to_planet_id) VALUES
    (1, TIMESTAMP '2026-11-01 08:00:00', 1, 'EARTH', 'MARS'),
    (2, TIMESTAMP '2026-11-01 09:30:00', 2, 'MARS', 'VEN'),
    (3, TIMESTAMP '2026-11-02 10:15:00', 3, 'VEN', 'EARTH'),
    (4, TIMESTAMP '2026-11-03 07:45:00', 4, 'EARTH', 'JUPITER'),
    (5, TIMESTAMP '2026-11-03 12:00:00', 5, 'JUPITER', 'SATURN'),
    (6, TIMESTAMP '2026-11-04 11:05:00', 6, 'SATURN', 'MARS'),
    (7, TIMESTAMP '2026-11-04 14:20:00', 7, 'VEN', 'JUPITER'),
    (8, TIMESTAMP '2026-11-05 05:10:00', 8, 'MARS', 'EARTH'),
    (9, TIMESTAMP '2026-11-05 16:35:00', 9, 'SATURN', 'VEN'),
    (10, TIMESTAMP '2026-11-06 18:55:00', 10, 'EARTH', 'SATURN');

ALTER TABLE ticket ALTER COLUMN id RESTART WITH 11;
