drop table if exists user;

create table user
(
    id int AUTO_INCREMENT,
    login_id varchar(25) not null,
    password varchar(50) not null,
    user_name  varchar(25) not null,
    user_role  varchar(25) not null,

    primary key (ID)
);
