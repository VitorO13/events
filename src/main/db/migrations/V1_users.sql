CREATE table users{
    
    id TEXT primary key unique not null,
    email TEXT not null unique,
    password TEXT not null,
    role TEXT not null
    
}