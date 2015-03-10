DROP DATABASE andreahd_cal;
CREATE DATABASE andreahd_cal;
USE andreahd_cal;

CREATE TABLE User (
  username VARCHAR(32) NOT NULL,
  name     VARCHAR(64) NOT NULL,
  password VARCHAR(32) NOT NULL,
  PRIMARY KEY (username)
);

CREATE TABLE Team (
  number INT         NOT NULL AUTO_INCREMENT,
  name   VARCHAR(32) NOT NULL,
  PRIMARY KEY (number)
);

CREATE TABLE TeamMember (
  username    VARCHAR(32) NOT NULL,
  team_number INT         NOT NULL,
  PRIMARY KEY (username, team_number),
  FOREIGN KEY (username) REFERENCES User (username),
  FOREIGN KEY (team_number) REFERENCES Team (number)
);

CREATE TABLE Room (
  name     VARCHAR(64) NOT NULL,
  capacity INT         NOT NULL,
  PRIMARY KEY (name)
);

CREATE TABLE Event (
  id      INT         NOT NULL AUTO_INCREMENT,
  name    VARCHAR(32) NOT NULL,
  date    DATE        NOT NULL,
  start   TIME        NOT NULL,
  end     TIME        NOT NULL,
  creator VARCHAR(32) NOT NULL,
  room    VARCHAR(32),
  PRIMARY KEY (id),
  FOREIGN KEY (creator) REFERENCES User (username),
  FOREIGN KEY (room) REFERENCES Room (name)
);

CREATE TABLE TeamEvent (
  team_number INT NOT NULL,
  event_id    INT NOT NULL,
  PRIMARY KEY (team_number, event_id),
  FOREIGN KEY (team_number) REFERENCES Team (number),
  FOREIGN KEY (event_id) REFERENCES Event (id)
);

CREATE TABLE UserEvent (
  username VARCHAR(32) NOT NULL,
  event_id INT NOT NULL,
  status ENUM('ATTENDING','MAYBE','NOT_ATTENDING') NOT NULL DEFAULT 'MAYBE',
  PRIMARY KEY (username, event_id),
  FOREIGN KEY (username) REFERENCES User (username),
  FOREIGN KEY (event_id) REFERENCES Event (id)
);


