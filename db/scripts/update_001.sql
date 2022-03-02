CREATE TABLE Cars
(
    id       SERIAL PRIMARY KEY,
    model VARCHAR NOT NULL,
    description VARCHAR NOT NULL,
    bodyType VARCHAR NOT NULL,
    photo VARCHAR NOT NULL
);

CREATE TABLE Announcements
(
    id       SERIAL PRIMARY KEY,
    description VARCHAR NOT NULL,
    car_id integer NOT NULL UNIQUE REFERENCES Cars(id),
    sold boolean NOT NULL
);

CREATE TABLE Authors
(
    id       SERIAL PRIMARY KEY,
    name VARCHAR NOT NULL,
    email VARCHAR NOT NULL UNIQUE,
    announcement_id integer REFERENCES Announcements(id)
);

CREATE TABLE Authors_Announcements
(
    author_id integer NOT NULL REFERENCES Authors(id),
    announcement_id integer NOT NULL REFERENCES Announcements(id)
);

CREATE TABLE Buyers
(
    id       SERIAL PRIMARY KEY,
    name VARCHAR NOT NULL,
    email VARCHAR NOT NULL UNIQUE,
    car_id integer REFERENCES Cars(id)
);

CREATE TABLE Buyers_Cars
(
    buyer_id integer NOT NULL REFERENCES Buyers(id),
    car_id integer NOT NULL REFERENCES Cars(id)
);