create table if not exists `note` (
    `id` bigint auto_increment primary key,
    `uuid` binary(16) not null unique,
    `content` longtext not null
);

create table if not exists `user` (
    `id` bigint auto_increment primary key,
    `uuid` binary(16) not null unique,
    `username` varchar(32) not null unique,
    `normalized_username` varchar(32) not null unique
);
