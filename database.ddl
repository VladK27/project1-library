create table person
(
    id            int auto_increment
        primary key,
    name          varchar(30) not null,
    surname       varchar(30) not null,
    year_of_birth int         not null
);

create table book
(
    id            int auto_increment
        primary key,
    title         varchar(100) not null,
    author        varchar(300) not null,
    year          int          not null,
    person_id     int          null,
    date_of_issue date         null,
    constraint title
        unique (title),
    constraint book_fk
        foreign key (person_id) references person (id)
            on delete set null
);

create table employee_info
(
    id            int auto_increment
        primary key,
    name          varchar(100) not null,
    surname       varchar(100) not null,
    year_of_birth int          not null,
    phone_number  varchar(20)  not null,
    email         varchar(100) not null,
    constraint email
        unique (email),
    constraint phone_number
        unique (phone_number),
    check (`year_of_birth` > 1900)
);


create table employee_credentials
(
    id               int auto_increment
        primary key,
    username         varchar(100) not null,
    password         varchar(60)  not null,
    personal_info_id int          null,
    role_id          int          null,
    constraint personal_info_id
        unique (personal_info_id),
    constraint username
        unique (username),
    constraint info_fk
        foreign key (personal_info_id) references employee_info (id)
);
