INSERT INTO User (username, name, password)
VALUES ('anddor', 'Andreas Dørum', 'passord');

INSERT INTO User (username, name, password)
VALUES ('user1', 'navn navnesen', 'passord1');

INSERT INTO User(username, name, password)
VALUES ('a','navn','p');

INSERT INTO Event (name, date, start, end, creator)
VALUES ('Kaffe med Andreas', '2015-03-03', '14:00:00', '14:30:00', 'user1');

INSERT INTO Room(name, capacity) VALUES ('rom 203','10');
INSERT INTO Room(name, capacity) VALUES ('rom 204','15');
INSERT INTO Room(name, capacity) VALUES ('rom 205','5');
INSERT INTO Room(name, capacity) VALUES ('rom 206','20');

INSERT INTO UserEvent (username, event_id) VALUES ('anddor', 1);
INSERT INTO UserEvent (username, event_id) VALUES ('user1', 1);

INSERT INTO Notification (event_id, user_username, message) VALUES (1, 'user1', 'You have been invited to an event');
