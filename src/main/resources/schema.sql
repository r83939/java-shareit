


CREATE TABLE IF NOT EXISTS users (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY NOT NULL,
    email varchar(512) NOT NULL,
    name varchar(255) NOT NULL,
    request_id BIGINT,
    UNIQUE (email));

CREATE TABLE IF NOT EXISTS items (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY NOT NULL,
    name varchar(100) NOT NULL,
    description varchar(1000),
    available boolean,
    user_id BIGINT NOT null REFERENCES users(id) ON DELETE CASCADE,
    url VARCHAR(1000),
    UNIQUE(url));

CREATE TABLE IF NOT EXISTS bookings (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY NOT NULL,
    start_date TIMESTAMP,
    end_date TIMESTAMP,
    item_id BIGINT NOT null references items(id) ON DELETE CASCADE,
    booker_id BIGINT NOT null references users(id) ON DELETE CASCADE,
    status VARCHAR(20));

CREATE TABLE IF NOT EXISTS comments (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY NOT NULL,
    message varchar(2000) NOT NULL,
    user_id BIGINT NOT null references users(id) ON DELETE CASCADE,
    item_id BIGINT NOT null references items(id) ON DELETE CASCADE);

