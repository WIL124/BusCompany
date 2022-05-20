DROP DATABASE IF EXISTS buscompany;
CREATE DATABASE buscompany;
USE buscompany;

CREATE TABLE IF NOT EXISTS users
(
    id         INT AUTO_INCREMENT,
    firstname  VARCHAR(50) NOT NULL,
    lastname   VARCHAR(50) NOT NULL,
    patronymic VARCHAR(50),
    login      VARCHAR(50) NOT NULL,
    password   VARCHAR(50) NOT NULL,
    userType   VARCHAR(10) NOT NULL,
    active     boolean     NOT NULL DEFAULT TRUE,
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
create table if not exists sessions
(
    user_id            INT         NOT NULL,
    session_id         VARCHAR(36) NOT NULL,
    last_activity_time long        NOT NULL,
    user_type           VARCHAR(10) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (id),
    UNIQUE KEY (session_id),
    PRIMARY KEY (user_id)
) ENGINE = INNODB
  DEFAULT CHARSET = utf8;

