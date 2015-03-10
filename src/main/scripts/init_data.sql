INSERT INTO User (username, name, password)
VALUES ('anddor', 'Andreas Dørum', 'passord');

INSERT INTO User (username, name, password)
VALUES ('user1', 'navn navnesen', 'passord1');

INSERT INTO User(username, name, password)
    VALUES ('a','navn','p');

INSERT INTO Event (name, date, start, end, creator)
VALUES ('Kaffe med Andreas', '2015-03-03', '14:00:00', '14:30:00', 'user1');

INSERT INTO UserEvent (username, event_id, status)
VALUES ('anddor', 1,'maybe');
INSERT INTO UserEvent (username, event_id,status)
VALUES ('user1', 1,'maybe');