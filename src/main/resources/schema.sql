create table if not exists user
(
    id       varchar(36) primary key,
    username varchar(255) not null,
    password varchar(255) not null
);
create table if not exists chatroom
(
    id         varchar(36) PRIMARY KEY,
    createTime TIMESTAMP    NOT NULL,
    name       VARCHAR(255) NOT NULL
);
create table if not exists message
(
    id         varchar(36) PRIMARY KEY,
    content    TEXT      NOT NULL,
    createTime TIMESTAMP NOT NULL,
    chatRoomId varchar(36),
    userId     varchar(36),
    FOREIGN KEY (chatRoomId) REFERENCES chatroom (id),
    FOREIGN KEY (userId) REFERENCES user (id)
);
create table if not exists admin
(
    id       BIGINT PRIMARY KEY,
    password VARCHAR(255) NOT NULL,
    username VARCHAR(255) NOT NULL
)
