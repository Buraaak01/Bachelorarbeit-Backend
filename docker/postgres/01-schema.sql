CREATE TABLE IF NOT EXISTS recipe
(
    id                      SERIAL PRIMARY KEY,
    title                   text not null,
    preparation             text not null,
    image                   bytea not null,
    "portions"              double precision not null,
    favorite                boolean not null
    );

CREATE TABLE IF NOT EXISTS ingredient
(
    id              varchar(255) PRIMARY KEY,
    name            varchar(255) not null,
    calories        double precision not null,
    proteins        double precision not null,
    fats            double precision not null,
    carbohydrates   double precision not null
    );


CREATE TABLE IF NOT EXISTS ingredient_unit
(
    id                  SERIAL PRIMARY KEY,
    ingredient_id       varchar(255) not null,
    label                varchar(255) NOT NULL,
    value          double precision NOT NULL,
    FOREIGN KEY (ingredient_id) REFERENCES ingredient (id)
    );

CREATE TABLE IF NOT EXISTS recipe_ingredient
(
    recipe_id               bigint not null,
    ingredient_id           varchar(255) not null,
    quantity                double precision not null,
    title                   varchar(255) not null,
    PRIMARY KEY (recipe_id, ingredient_id),
    FOREIGN KEY (recipe_id) REFERENCES recipe (id),
    FOREIGN KEY (ingredient_id) REFERENCES ingredient (id)
    );