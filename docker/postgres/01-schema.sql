CREATE TABLE IF NOT EXISTS test_entity
(
    id SERIAL PRIMARY KEY,
    nummer varchar(255) not null,
    aktiv boolean default false
);