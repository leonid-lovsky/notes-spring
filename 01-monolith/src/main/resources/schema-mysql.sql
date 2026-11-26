create table if not exists note (
    id bigint auto_increment primary key,
    uuid binary(16) not null unique,
    content longtext not null
);
