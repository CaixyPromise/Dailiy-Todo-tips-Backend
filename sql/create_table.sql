# 数据库初始化

-- 创建库
create database if not exists todo_db;

-- 切换库
use todo_db;

create table task_directories
(
    id         bigint auto_increment comment 'id'
        primary key,
    user_id    bigint                             not null comment '用户id',
    tagName    varchar(32)                        not null comment '分类名称',
    createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    constraint tagName
        unique (tagName)
)
    comment '任务分类标签表' collate = utf8mb4_unicode_ci;

create index idx_userId
    on task_directories (user_id);

create table tasks
(
    id          bigint auto_increment comment 'id'
        primary key,
    title       varchar(255)                        not null comment '任务标题',
    dirId       bigint                              not null comment '任务所属目录',
    userID      bigint                              not null comment '任务所属用户',
    description text                                null comment '任务描述',
    date        timestamp                           null comment '任务日期',
    completed   tinyint   default 0                 not null comment '任务是否完成',
    important   tinyint   default 0                 not null comment '任务是否重要',
    alarm       tinyint   default 0                 not null comment '任务是否有提醒',
    created_at  timestamp default CURRENT_TIMESTAMP not null comment '创建时间',
    updated_at  timestamp default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间'
)
    collate = utf8mb4_unicode_ci;

create index tasks_userID_index
    on tasks (userID)
    comment '根据用户查任务';

create table user
(
    id           bigint auto_increment comment 'id'
        primary key,
    userAccount  varchar(256)                           not null comment '账号',
    userPassword varchar(512)                           not null comment '密码',
    unionId      varchar(256)                           null comment '微信开放平台id',
    mpOpenId     varchar(256)                           null comment '公众号openId',
    userName     varchar(256)                           null comment '用户昵称',
    userAvatar   varchar(1024)                          null comment '用户头像',
    userProfile  varchar(512)                           null comment '用户简介',
    userRole     varchar(256) default 'user'            not null comment '用户角色：user/admin/ban',
    createTime   datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime   datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete     tinyint      default 0                 not null comment '是否删除'
)
    comment '用户' collate = utf8mb4_unicode_ci;

create index idx_unionId
    on user (unionId);

