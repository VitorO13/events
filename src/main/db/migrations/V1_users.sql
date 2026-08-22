CREATE table users{
    
    id TEXT primary key unique not null,
    email  not null unique,
    password  not null,
    role  not null
    
}