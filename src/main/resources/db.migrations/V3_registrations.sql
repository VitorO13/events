create table registrations {
    id integer primary key auto_increment,
    id_user foreign key references users(id) not null,
    id_event foreign key references event(id) not nul,
    regis_date datetime not null, 
    is_present boolean not null

};