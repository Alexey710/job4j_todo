CREATE DATABASE todo;
CREATE TABLE tasks (
                      id SERIAL PRIMARY KEY,
                      date TIMESTAMP,
                      description TEXT,
                      user_id int not null references users(id)
);

create table users (
    id SERIAL PRIMARY KEY,
    name varchar(2000)
    email TEXT,
    password TEXT,
    CONSTRAINT email_unique UNIQUE (email)
);