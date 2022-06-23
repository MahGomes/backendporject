CREATE TABLE IF NOT EXISTS `Client` (
     `id`         INTEGER  PRIMARY KEY AUTO_INCREMENT,
     `name` VARCHAR(50) NOT NULL,
     `age`        INTEGER  NOT NULL,
     `email` VARCHAR(100) NOT NULL
);