Create table events{
    
    id UUID primary key unique not null,
    name TEXT not null,
    hour TEXT not null,
    duration TEXT not null,
    date TEXT not null,
    description TEXT not null,
    capacity INTEGER not null
    
}