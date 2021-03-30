CREATE DATABASE todo;
CREATE TABLE tasks (
                      id SERIAL PRIMARY KEY,
                      date TIMESTAMP,
                      description TEXT
);