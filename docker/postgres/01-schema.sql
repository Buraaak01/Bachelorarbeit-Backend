CREATE TABLE IF NOT EXISTS student
(
    id SERIAL PRIMARY KEY,
    name varchar(255) not null,
    timestamp      timestamp with time zone not null
);