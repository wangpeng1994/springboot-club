create table user
(
    id                 int primary key auto_increment unique not null,
    username           varchar(100) unique,
    encrypted_password varchar(100),
    avatar             varchar(100),
    created_at         datetime,
    updated_at         datetime
)