create table biz_retry
(
    id           int auto_increment comment '主键'
        primary key,
    biz_no       varchar(64)                         not null comment '业务编号',
    biz_type     varchar(64)                         not null comment '业务类型',
    biz_params   text                                null comment '业务参数',
    retry_type   varchar(128)                        null comment '重试类型',
    retry_count  int       default -1                null comment '重试次数',
    retry_status tinyint   default 0                 null comment '重试状态(0:待重试; 1:重试成功; 2:重试失败)',
    comment      varchar(512)                        null comment '备注',
    created      timestamp default CURRENT_TIMESTAMP not null comment '创建时间',
    modified     timestamp default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '修改时间',
    flag         smallint  default 1                 null comment '数据是否有效'
)
    comment '服务重试表';

create index risk_decision_retry_biz_no_index
    on biz_retry (biz_no);

create index risk_decision_retry_created_index
    on biz_retry (created);

create index risk_decision_retry_event_type_index
    on biz_retry (retry_type);

create index risk_decision_retry_modified_index
    on biz_retry (modified);

create index risk_decision_retry_retry_status_index
    on biz_retry (retry_status);

