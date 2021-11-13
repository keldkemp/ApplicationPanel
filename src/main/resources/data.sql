--PASS: adminka
INSERT INTO users (id, login, name, password)
VALUES (0, 'admin', 'admin', '$2a$10$BcqbIMZH.1Bj1rxA1TF.1OtvLojlgZwqcChscpbyylCei5L.cP6TC')
    ON CONFLICT DO NOTHING;