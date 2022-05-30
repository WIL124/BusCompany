DROP DATABASE IF EXISTS buscompany;
CREATE DATABASE buscompany;
USE buscompany;

CREATE TABLE users
(
    id         INT AUTO_INCREMENT,
    firstname  VARCHAR(50)              NOT NULL,
    lastname   VARCHAR(50)              NOT NULL,
    patronymic VARCHAR(50),
    login      VARCHAR(50)              NOT NULL,
    password   VARCHAR(50)              NOT NULL,
    userType   ENUM ('ADMIN', 'CLIENT') NOT NULL,
    active     boolean                  NOT NULL DEFAULT TRUE,
    PRIMARY KEY (id),
    UNIQUE KEY (login)
) ENGINE = INNODB
  DEFAULT CHARSET = utf8;

CREATE TABLE clients
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
CREATE TABLE sessions
(
    user_id            INT         NOT NULL,
    session_id         VARCHAR(36) NOT NULL,
    last_activity_time long        NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (id),
    UNIQUE KEY (session_id),
    PRIMARY KEY (user_id)
) ENGINE = INNODB
  DEFAULT CHARSET = utf8;
CREATE TABLE buses
(
    busName    VARCHAR(50) NOT NULL,
    placeCount int         NOT NULL,
    PRIMARY KEY (busName)
) ENGINE = INNODB
  DEFAULT CHARSET = utf8;
CREATE TABLE trips
(
    id          INT AUTO_INCREMENT,
    busName     VARCHAR(50) NOT NULL,
    fromStation VARCHAR(50) NOT NULL,
    toStation   VARCHAR(50) NOT NULL,
    start       TIME        NOT NULL,
    duration    TIME        NOT NULL,
    price       DECIMAL(10, 2) UNSIGNED,
    FOREIGN KEY (busName) REFERENCES buses (busName),
    PRIMARY KEY (id)
) ENGINE = INNODB
  DEFAULT CHARSET = utf8;

CREATE TABLE trips_dates
(
    trip_id     INT,
    date        DATE,
    place_count INT,
    FOREIGN KEY (trip_id) REFERENCES trips (id)
) ENGINE = INNODB
  DEFAULT CHARSET = utf8;
