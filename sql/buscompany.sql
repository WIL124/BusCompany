DROP DATABASE IF EXISTS buscompany;
CREATE DATABASE IF NOT EXISTS buscompany;
USE buscompany;

CREATE TABLE IF NOT EXISTS users
(
    id         INT AUTO_INCREMENT,
    firstname  VARCHAR(50) NOT NULL,
    lastname   VARCHAR(50) NOT NULL,
    patronymic VARCHAR(50),
    login      VARCHAR(50) NOT NULL,
    PASSWORD   VARCHAR(50) NOT NULL,
    user_type  VARCHAR(10) NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY (login)
) ENGINE = INNODB
  DEFAULT CHARSET = utf8;

create table if not exists clients
(
    id    INT         NOT NULL,
    email VARCHAR(50) NOT NULL,
    phone VARCHAR(12) NOT NULL,
    FOREIGN KEY (id) REFERENCES users (id)
) ENGINE = INNODB
  DEFAULT CHARSET = utf8;

create table if not exists admins
(
    id       INT         NOT NULL,
    position VARCHAR(50) NOT NULL,
    FOREIGN KEY (id) REFERENCES users (id)
) ENGINE = INNODB
  DEFAULT CHARSET = utf8;
