DROP DATABASE IF EXISTS banko_db;

CREATE DATABASE banko_db;

USE banko_db;

CREATE TABLE accounts (
  id INT(15) PRIMARY KEY AUTO_INCREMENT,
  serialized_object BLOB
);


CREATE TABLE transactions (
    id INT(15) PRIMARY KEY AUTO_INCREMENT,
    from_id INT(15),
    to_id INT(15),
    amount DOUBLE,
    FOREIGN KEY (from_id) REFERENCES accounts(id) ON DELETE SET NULL,
    FOREIGN KEY (to_id) REFERENCES accounts(id) ON DELETE SET NULL
);