create schema if not exists contacts_schema;
create table if not exists contacts_schema.contacts
(
    id
    BIGINT PRIMARY KEY,
    first_name varchar(255) not null,
    last_name varchar(255) not null,
    email varchar(255) not null,
    phone varchar(255) not null
);