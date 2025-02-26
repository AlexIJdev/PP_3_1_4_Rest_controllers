create table users
(
    id         int PRIMARY KEY AUTO_INCREMENT,
    first_name varchar(50)  NOT NULL,
    last_name  varchar(50)  NOT NULL,
    age        int          NOT NULL,
    email      varchar(100) UNIQUE,
    password   varchar(100) NOT NULL
);

insert into users (first_name, last_name, age, email, password)
values ('One', 'User', 11, 'oneuser@mail.ru', '$2a$12$1O6tCPPkYw3yTBXwemVWJ.J1P/OU5pfLqoLRbvEnQqSsoEjaumLcu');
insert into users (first_name, last_name, age, email, password)
values ('Two', 'User', 12, 'twouser@mail.ru', '$2a$12$5Ah8lErbI2QEtV.4r4rI0OsFPFxU.bGROt9oVdaMunhEBkxpu8SDy');
insert into users (first_name, last_name, age, email, password)
values ('Three', 'User', 13, 'threeuser@mail.ru', '$2a$12$cYD5Cc0kZaLG7ggAzJFb7OUAsNgZ3xlmfdUkbVE5QNbaJIRuvfn7S');
insert into users (first_name, last_name, age, email, password)
values ('Four', 'User', 14, 'fouruser@mail.ru', '$2a$12$Gtmxiup5XsVYvGsxx0XHYuwiIFPJhy9GmpFerOAEoJ3KR/Z4INT1e');

create table roles
(
    id   int primary key auto_increment,
    name varchar(100)
);

insert into roles (name)
values ('ROLE_ADMIN');
insert into roles (name)
values ('ROLE_USER');

create table user_role
(
    user_id int references users (id),
    role_id int references roles (id),
    PRIMARY KEY (user_id, role_id)
);

insert into user_role
values (1, 1);
insert into user_role
values (2, 2);
insert into user_role
values (3, 1);
insert into user_role
values (4, 2);

select *
from user_role;

select *
from users;

select *
from roles;

drop table roles;

drop table user_role;

drop table users;