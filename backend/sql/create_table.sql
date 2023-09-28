# 数据库初始化
# @author <a href="https://github.com/liyupi">程序员鱼皮</a>
# @from <a href="https://yupi.icu">编程导航知识星球</a>

-- 创建库
create database if not exists huang_oj;

-- 切换库
use huang_oj;

-- 用户表
create table if not exists user
(
    id           bigint auto_increment comment 'id' primary key,
    userAccount  varchar(256)                           not null comment '账号',
    userSalt     varchar(512)                           not null comment '盐',
    userPassword varchar(512)                           not null comment '密码',
    unionId      varchar(256)                           null comment '微信开放平台id',
    mpOpenId     varchar(256)                           null comment '公众号openId',
    userName     varchar(256)                           null comment '用户昵称',
    userAvatar   varchar(1024)                          null comment '用户头像',
    userProfile  varchar(512)                           null comment '用户简介',
    userEmail    varchar(256)                           null comment '用户邮箱',
    userRole     varchar(256) default 'user'            not null comment '用户角色：user/admin/ban',
    createTime   datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime   datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete     tinyint      default 0                 not null comment '是否删除',
    index idx_unionId (unionId)
) comment '用户' collate = utf8mb4_unicode_ci;

-- 题目表
create table if not exists problem
(
    id         bigint auto_increment comment 'id' primary key,
    title      varchar(512)                       null comment '题目标题',
    solution    text                               null comment '官解',
    content    text                               null comment '内容',
    tags       JSON                      null comment '题目标签列表',
    judgeConfig       JSON                      null comment '判题标签列表',
    judgeCase       JSON                      null comment '检测样例',
    thumbNum   int      default 0                 not null comment '点赞数',
    disLikeNum  int      default 0                 not null comment '点踩数',
    userId     bigint                             not null comment '创建用户 id',
    createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isVip      tinyint  default 0                 not null comment 'vip尊享?',
    isDelete   tinyint  default 0                 not null comment '是否删除',
    index idx_userId (userId)
) comment '题' collate = utf8mb4_unicode_ci;

-- 提交表
create table if not exists submission
(
    id         bigint auto_increment comment 'id' primary key,
    problemId     bigint                             not null comment '题目 id',
    userId     bigint                             not null comment '提交用户 id',
    `language`     varchar(128)                             not null comment '语言',
    code    text                               null comment '提交的代码',
    judgeStatus    tinyint                               null comment '当前状态 0-编译 1-运行 2-失败 3-成功',
    judgeInfo    JSON                               null comment '结果 ',
    submitTime datetime default CURRENT_TIMESTAMP not null comment '提交时间',
    isDelete   tinyint  default 0                 not null comment '是否删除',
    index idx_postId (problemId),
    index idx_userId (userId)
) comment '提交';

-- 帖子收藏表（硬删除）
create table if not exists post_favour
(
    id         bigint auto_increment comment 'id' primary key,
    postId     bigint                             not null comment '帖子 id',
    userId     bigint                             not null comment '创建用户 id',
    createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    index idx_postId (postId),
    index idx_userId (userId)
) comment '帖子收藏';
