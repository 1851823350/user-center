-- auto-generated definition
create table user
(
    id           bigint auto_increment
        primary key,
    username     varchar(256)                       null,
    userAccount  varchar(256)                       null,
    gender       tinyint                            null,
    userPassword varchar(256)                       not null,
    phone        varchar(256)                       null,
    avatarUrl    varchar(1024)                      null,
    email        varchar(256)                       null,
    userStatus   tinyint  default 0                 null,
    createTime   datetime default CURRENT_TIMESTAMP null,
    updateTime   datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP,
    isDelete     tinyint  default 0                 null,
    userRole     int      default 0                 not null comment '用户角色 0-》普通用户  1-》管理员',
    planetCode   varchar(512)                       null comment '星球编号'
);