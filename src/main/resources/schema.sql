


CREATE TABLE IF NOT EXISTS users (
    --id bigint generated by default as identity not null PRIMARY KEY,
    id BIGINT PRIMARY KEY,
    email varchar(512) NOT NULL,
    name varchar(255) NOT NULL,
    UNIQUE (email));

CREATE TABLE IF NOT EXISTS requests (
    --id bigint generated by default as identity not null PRIMARY KEY,
    id BIGINT PRIMARY KEY,
    description varchar(1000) NOT NULL,
    requester_id BIGINT NOT NULL references users(id) ON DELETE CASCADE,
    created TIMESTAMP);


CREATE TABLE IF NOT EXISTS items (
    --id bigint generated by default as identity not null PRIMARY KEY,
    id BIGINT PRIMARY KEY,
    name varchar(100) NOT NULL,
    description varchar(1000),
    available boolean,
    user_id BIGINT NOT null REFERENCES users(id) ON DELETE CASCADE,
    request_id BIGINT null references requests(id) ON DELETE CASCADE);


CREATE TABLE IF NOT EXISTS bookings (
    --id bigint generated by default as identity not null PRIMARY KEY,
    id BIGINT PRIMARY KEY,
    start_date TIMESTAMP,
    end_date TIMESTAMP,
    item_id BIGINT NOT null references items(id) ON DELETE CASCADE,
    booker_id BIGINT NOT null references users(id) ON DELETE CASCADE,
    status VARCHAR(20));


CREATE TABLE IF NOT EXISTS comments (
    --id bigint generated by default as identity not null PRIMARY KEY,
    id BIGINT PRIMARY KEY,
    text varchar(2000) NOT NULL,
    user_id BIGINT NOT null references users(id) ON DELETE CASCADE,
    item_id BIGINT NOT null references items(id) ON DELETE CASCADE,
    created TIMESTAMP);

