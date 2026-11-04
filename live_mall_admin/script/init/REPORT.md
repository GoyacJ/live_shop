# 初始化脚本生成报告

由 `script/init/tools/gen_schema.py` 生成，改了实体后重跑即可覆盖。

## 统计

| 项 | 数量 |
| --- | --- |
| 实体表总数 | 79 |
| 沿用 `script/snowy.sql` 原始列定义 | 32 |
| 按实体推导建表 | 47 |
| 总列数 | 1157 |

## 表清单

| 表 | 列数 | 主键 | 定义来源 |
| --- | --- | --- | --- |
| `auth_third_user` | 14 | ID | 导出 |
| `biz_notice` | 16 | ID | 导出 |
| `client_relation` | 5 | ID | 导出 |
| `client_user` | 45 | ID | 导出 |
| `dev_config` | 12 | ID | 导出 |
| `dev_dict` | 12 | ID | 导出 |
| `dev_email` | 17 | ID | 导出 |
| `dev_file` | 17 | ID | 导出 |
| `dev_job` | 14 | ID | 导出 |
| `dev_log` | 22 | ID | 导出 |
| `dev_message` | 10 | ID | 导出 |
| `dev_relation` | 5 | ID | 导出 |
| `dev_slideshow` | 13 | ID | 导出 |
| `dev_sms` | 13 | ID | 导出 |
| `gen_basic` | 23 | ID | 导出 |
| `gen_config` | 21 | ID | 导出 |
| `mall_goods` | 22 | id | 导出 |
| `mall_goods_slide` | 11 | id | 导出 |
| `mobile_resource` | 19 | ID | 导出 |
| `sys_group` | 10 | ID | 导出 |
| `sys_org` | 13 | ID | 导出 |
| `sys_position` | 12 | ID | 导出 |
| `sys_relation` | 5 | ID | 导出 |
| `sys_resource` | 20 | ID | 导出 |
| `sys_role` | 12 | ID | 导出 |
| `sys_user` | 52 | ID | 导出 |
| `user_address` | 16 | id | 导出 |
| `user_balance_log` | 16 | id | 导出 |
| `user_info` | 23 | id | 导出 |
| `user_integral` | 10 | id | 导出 |
| `user_level` | 14 | id | 导出 |
| `user_withdrawal_log` | 9 | id | 导出 |
| `live_marketing_campaigns_log` | 7 | ID | 实体推导 |
| `live_spectator_grade` | 7 | ID | 实体推导 |
| `live_spectator_watching` | 6 | ID | 实体推导 |
| `live_task_sheet` | 6 | ID | 实体推导 |
| `live_shopping_car` | 9 | ID | 实体推导 |
| `live_gift` | 5 | ID | 实体推导 |
| `live_streaming_type` | 10 | ID | 实体推导 |
| `live_focus_relationships` | 5 | ID | 实体推导 |
| `live_anchor` | 7 | ID | 实体推导 |
| `live_marketing_campaigns` | 9 | ID | 实体推导 |
| `live_biz` | 21 | ID | 实体推导 |
| `task_record` | 25 | ID | 实体推导 |
| `task_info` | 25 | ID | 实体推导 |
| `integral_record` | 22 | ID | 实体推导 |
| `integral_withdrawal_record` | 13 | ID | 实体推导 |
| `integral_info` | 13 | ID | 实体推导 |
| `integral_rules` | 17 | ID | 实体推导 |
| `dev_app_version` | 13 | ID | 实体推导 |
| `distribution_record` | 23 | ID | 实体推导 |
| `distribution_settlement` | 23 | ID | 实体推导 |
| `distribution_info` | 18 | ID | 实体推导 |
| `user_question_answer` | 8 | ID | 实体推导 |
| `mobile_device_info` | 14 | ID | 实体推导 |
| `user_feedback` | 12 | ID | 实体推导 |
| `user_account` | 10 | ID | 实体推导 |
| `live_message_log` | 8 | ID | 实体推导 |
| `user_amount_available` | 8 | ID | 实体推导 |
| `content_video` | 22 | ID | 实体推导 |
| `content_watch_progress` | 15 | ID | 实体推导 |
| `content_watch_record` | 14 | ID | 实体推导 |
| `content_stat_counter` | 14 | ID | 实体推导 |
| `content_user_action` | 11 | ID | 实体推导 |
| `content_shop_play_follow` | 10 | ID | 实体推导 |
| `content_short_play_episode` | 19 | ID | 实体推导 |
| `content_short_play` | 20 | ID | 实体推导 |
| `mall_order` | 24 | ID | 实体推导 |
| `mall_goods_change` | 10 | ID | 实体推导 |
| `mall_goods_evaluate` | 13 | ID | 实体推导 |
| `mall_payment_config` | 22 | ID | 实体推导 |
| `mall_order_goods` | 13 | ID | 实体推导 |
| `mall_after_sales` | 11 | ID | 实体推导 |
| `mall_goods_special_topic_detail` | 10 | ID | 实体推导 |
| `mall_goods_special_topic` | 11 | ID | 实体推导 |
| `mall_third_payment` | 13 | ID | 实体推导 |
| `mall_order_logistics` | 16 | ID | 实体推导 |
| `mall_integral_goods` | 14 | ID | 实体推导 |
| `mall_sales_type` | 8 | ID | 实体推导 |

## 待复核 1：导出与实体推导的类型不一致（53 处）

这些表在 `script/snowy.sql` 里有真实定义，脚本保留导出类型，只是记录下来。

- 表 sys_user 列 AVATAR 导出为 longtext，实体推导 varchar(1000)（保留导出定义）
- 表 sys_user 列 SIGNATURE 导出为 longtext，实体推导 varchar(255)（保留导出定义）
- 表 sys_user 列 HOME_ADDRESS 导出为 text，实体推导 varchar(255)（保留导出定义）
- 表 sys_user 列 MAILING_ADDRESS 导出为 text，实体推导 varchar(255)（保留导出定义）
- 表 sys_user 列 EMERGENCY_ADDRESS 导出为 text，实体推导 varchar(255)（保留导出定义）
- 表 sys_user 列 POSITION_JSON 导出为 longtext，实体推导 varchar(255)（保留导出定义）
- 表 sys_user 列 LAST_LOGIN_DEVICE 导出为 text，实体推导 varchar(255)（保留导出定义）
- 表 sys_user 列 LATEST_LOGIN_DEVICE 导出为 text，实体推导 varchar(255)（保留导出定义）
- 表 dev_file 列 NAME 导出为 text，实体推导 varchar(255)（保留导出定义）
- 表 dev_file 列 SIZE_KB 导出为 bigint(20)，实体推导 varchar(255)（保留导出定义）
- 表 dev_file 列 OBJ_NAME 导出为 text，实体推导 varchar(255)（保留导出定义）
- 表 dev_file 列 STORAGE_PATH 导出为 text，实体推导 varchar(255)（保留导出定义）
- 表 dev_file 列 DOWNLOAD_PATH 导出为 text，实体推导 varchar(255)（保留导出定义）
- 表 dev_file 列 THUMBNAIL 导出为 longtext，实体推导 varchar(255)（保留导出定义）
- 表 dev_sms 列 PHONE_NUMBERS 导出为 text，实体推导 varchar(255)（保留导出定义）
- 表 dev_sms 列 SIGN_NAME 导出为 text，实体推导 varchar(255)（保留导出定义）
- 表 dev_sms 列 TEMPLATE_CODE 导出为 text，实体推导 varchar(255)（保留导出定义）
- 表 dev_sms 列 TEMPLATE_PARAM 导出为 text，实体推导 varchar(255)（保留导出定义）
- 表 dev_sms 列 RECEIPT_INFO 导出为 longtext，实体推导 varchar(255)（保留导出定义）
- 表 dev_config 列 CONFIG_VALUE 导出为 longtext，实体推导 varchar(255)（保留导出定义）
- 表 dev_message 列 SUBJECT 导出为 text，实体推导 varchar(255)（保留导出定义）
- 表 dev_message 列 CONTENT 导出为 longtext，实体推导 text（保留导出定义）
- 表 dev_log 列 EXE_MESSAGE 导出为 longtext，实体推导 varchar(255)（保留导出定义）
- 表 dev_log 列 REQ_URL 导出为 longtext，实体推导 varchar(255)（保留导出定义）
- 表 dev_log 列 PARAM_JSON 导出为 longtext，实体推导 varchar(255)（保留导出定义）
- 表 dev_log 列 RESULT_JSON 导出为 longtext，实体推导 varchar(255)（保留导出定义）
- 表 dev_log 列 SIGN_DATA 导出为 longtext，实体推导 varchar(255)（保留导出定义）
- 表 dev_email 列 RECEIVE_ACCOUNTS 导出为 text，实体推导 varchar(255)（保留导出定义）
- 表 dev_email 列 SUBJECT 导出为 text，实体推导 varchar(255)（保留导出定义）
- 表 dev_email 列 CONTENT 导出为 longtext，实体推导 text（保留导出定义）
- 表 dev_email 列 TEMPLATE_NAME 导出为 text，实体推导 varchar(255)（保留导出定义）
- 表 dev_email 列 TEMPLATE_PARAM 导出为 text，实体推导 varchar(255)（保留导出定义）
- 表 dev_email 列 RECEIPT_INFO 导出为 longtext，实体推导 varchar(255)（保留导出定义）
- 表 user_integral 列 AMOUNT 导出为 decimal(12, 2) UNSIGNED，实体推导 double（保留导出定义）
- 表 user_integral 列 INTEGRAL 导出为 decimal(12, 2) UNSIGNED，实体推导 double（保留导出定义）
- 表 user_level 列 SORT 导出为 tinyint(3) UNSIGNED，实体推导 varchar(255)（保留导出定义）
- 表 user_info 列 AVATAR 导出为 longtext，实体推导 varchar(1000)（保留导出定义）
- 表 user_info 列 LAST_LOGIN_DEVICE 导出为 text，实体推导 varchar(255)（保留导出定义）
- 表 user_withdrawal_log 列 AMOUNT 导出为 decimal(12, 2) UNSIGNED，实体推导 varchar(255)（保留导出定义）
- 表 sys_resource 列 PATH 导出为 text，实体推导 varchar(1000)（保留导出定义）
- 表 mall_goods_slide 列 SORT 导出为 tinyint(1) UNSIGNED，实体推导 varchar(255)（保留导出定义）
- 表 mall_goods 列 COST_PRICE 导出为 decimal(12, 2) UNSIGNED，实体推导 double（保留导出定义）
- 表 mall_goods 列 PRICE 导出为 decimal(12, 2) UNSIGNED，实体推导 double（保留导出定义）
- 表 mall_goods 列 SORT 导出为 tinyint(1) UNSIGNED，实体推导 varchar(255)（保留导出定义）
- 表 mall_goods 列 DESCRIPTION 导出为 varchar(255)，实体推导 text（保留导出定义）
- 表 client_user 列 AVATAR 导出为 longtext，实体推导 varchar(1000)（保留导出定义）
- 表 client_user 列 SIGNATURE 导出为 longtext，实体推导 varchar(255)（保留导出定义）
- 表 client_user 列 HOME_ADDRESS 导出为 text，实体推导 varchar(255)（保留导出定义）
- 表 client_user 列 MAILING_ADDRESS 导出为 text，实体推导 varchar(255)（保留导出定义）
- 表 client_user 列 EMERGENCY_ADDRESS 导出为 text，实体推导 varchar(255)（保留导出定义）
- 表 client_user 列 LAST_LOGIN_DEVICE 导出为 text，实体推导 varchar(255)（保留导出定义）
- 表 client_user 列 LATEST_LOGIN_DEVICE 导出为 text，实体推导 varchar(255)（保留导出定义）
- 表 auth_third_user 列 AVATAR 导出为 longtext，实体推导 varchar(1000)（保留导出定义）

## 待复核 2：枚举 / 自定义类型（62 处）

代码里是 Java 枚举，库里按 `varchar(50)` 存。MyBatis-Flex 默认存枚举的 `name()`；如果某个枚举用了 `@EnumValue` 落了别的值，报告里这几列要改成对应类型。

- 表 task_record 字段 taskType 类型 TaskType 未识别，按 varchar(50) 处理
- 表 task_record 字段 taskCategory 类型 TaskCategory 未识别，按 varchar(50) 处理
- 表 task_record 字段 taskBusinessCategory 类型 TaskBusinessCategory 未识别，按 varchar(50) 处理
- 表 task_record 字段 triggerCondition 类型 TaskTriggerCondition 未识别，按 varchar(50) 处理
- 表 task_record 字段 refreshTime 类型 RefreshTimeType 未识别，按 varchar(50) 处理
- 表 task_record 字段 isRefresh 类型 SimpleStatus 未识别，按 varchar(50) 处理
- 表 task_record 字段 isLimitTime 类型 SimpleStatus 未识别，按 varchar(50) 处理
- 表 task_record 字段 executeStatus 类型 SimpleStatus 未识别，按 varchar(50) 处理
- 表 task_info 字段 taskType 类型 TaskType 未识别，按 varchar(50) 处理
- 表 task_info 字段 taskCategory 类型 TaskCategory 未识别，按 varchar(50) 处理
- 表 task_info 字段 taskBusinessCategory 类型 TaskBusinessCategory 未识别，按 varchar(50) 处理
- 表 task_info 字段 triggerCondition 类型 TaskTriggerCondition 未识别，按 varchar(50) 处理
- 表 task_info 字段 refreshTime 类型 RefreshTimeType 未识别，按 varchar(50) 处理
- 表 task_info 字段 isRefresh 类型 SimpleStatus 未识别，按 varchar(50) 处理
- 表 task_info 字段 isLimitTime 类型 SimpleStatus 未识别，按 varchar(50) 处理
- 表 task_info 字段 status 类型 SimpleStatus 未识别，按 varchar(50) 处理
- 表 integral_record 字段 integralSource 类型 IntegralSource 未识别，按 varchar(50) 处理
- 表 integral_record 字段 changeType 类型 IntegralChangeType 未识别，按 varchar(50) 处理
- 表 integral_record 字段 integralCategory 类型 IntegralCategory 未识别，按 varchar(50) 处理
- 表 integral_record 字段 operationSource 类型 IntegralOperationSource 未识别，按 varchar(50) 处理
- 表 integral_record 字段 clearExpireStatus 类型 SimpleStatus 未识别，按 varchar(50) 处理
- 表 integral_withdrawal_record 字段 withdrawalStatus 类型 IntegralWithdrawalStatus 未识别，按 varchar(50) 处理
- 表 integral_info 字段 status 类型 SimpleStatus 未识别，按 varchar(50) 处理
- 表 integral_rules 字段 userLimitType 类型 UserLimitType 未识别，按 varchar(50) 处理
- 表 integral_rules 字段 limitTimeType 类型 RefreshTimeType 未识别，按 varchar(50) 处理
- 表 integral_rules 字段 withdrawalTimeType 类型 RefreshTimeType 未识别，按 varchar(50) 处理
- 表 integral_rules 字段 status 类型 SimpleStatus 未识别，按 varchar(50) 处理
- 表 dev_app_version 字段 appType 类型 AppType 未识别，按 varchar(50) 处理
- 表 distribution_record 字段 type 类型 DistributionType 未识别，按 varchar(50) 处理
- 表 distribution_record 字段 settlementStatus 类型 SettlementStatus 未识别，按 varchar(50) 处理
- 表 distribution_record 字段 recommendCommission 类型 RecommendCommission 未识别，按 varchar(50) 处理
- 表 distribution_record 字段 settlementType 类型 SettlementType 未识别，按 varchar(50) 处理
- 表 distribution_record 字段 settlementTimeType 类型 SettlementTimeType 未识别，按 varchar(50) 处理
- 表 distribution_record 字段 commissionTimeType 类型 CommissionTimeType 未识别，按 varchar(50) 处理
- 表 distribution_record 字段 commissionTimeStatus 类型 CommissionTimeStatus 未识别，按 varchar(50) 处理
- 表 distribution_settlement 字段 type 类型 DistributionType 未识别，按 varchar(50) 处理
- 表 distribution_settlement 字段 recommendCommission 类型 RecommendCommission 未识别，按 varchar(50) 处理
- 表 distribution_settlement 字段 settlementType 类型 SettlementType 未识别，按 varchar(50) 处理
- 表 distribution_settlement 字段 settlementStatus 类型 SettlementStatus 未识别，按 varchar(50) 处理
- 表 distribution_settlement 字段 settlementTimeType 类型 SettlementTimeType 未识别，按 varchar(50) 处理
- 表 distribution_settlement 字段 commissionTimeType 类型 CommissionTimeType 未识别，按 varchar(50) 处理
- 表 distribution_settlement 字段 commissionTimeStatus 类型 CommissionTimeStatus 未识别，按 varchar(50) 处理
- 表 distribution_info 字段 type 类型 DistributionType 未识别，按 varchar(50) 处理
- 表 distribution_info 字段 status 类型 SimpleStatus 未识别，按 varchar(50) 处理
- 表 distribution_info 字段 recommendCommission 类型 RecommendCommission 未识别，按 varchar(50) 处理
- 表 distribution_info 字段 settlementType 类型 SettlementType 未识别，按 varchar(50) 处理
- 表 distribution_info 字段 settlementTimeType 类型 SettlementTimeType 未识别，按 varchar(50) 处理
- 表 distribution_info 字段 commissionTimeType 类型 CommissionTimeType 未识别，按 varchar(50) 处理
- 表 user_balance_log 字段 type 类型 UserBalanceChangeType 未识别，按 varchar(50) 处理
- 表 user_balance_log 字段 balanceType 类型 UserBalanceType 未识别，按 varchar(50) 处理
- 表 user_balance_log 字段 settleTimeType 类型 IntegralSettleTimeType 未识别，按 varchar(50) 处理
- 表 user_info 字段 registerUrlStatus 类型 RegisterUrlStatus 未识别，按 varchar(50) 处理
- 表 content_watch_progress 字段 contentType 类型 ContentType 未识别，按 varchar(50) 处理
- 表 content_watch_record 字段 contentType 类型 ContentType 未识别，按 varchar(50) 处理
- 表 content_stat_counter 字段 contentType 类型 ContentType 未识别，按 varchar(50) 处理
- 表 content_user_action 字段 contentActionType 类型 ContentActionType 未识别，按 varchar(50) 处理
- 表 content_user_action 字段 isActive 类型 SimpleStatus 未识别，按 varchar(50) 处理
- 表 content_shop_play_follow 字段 isActive 类型 SimpleStatus 未识别，按 varchar(50) 处理
- 表 mall_order 字段 settlementStatus 类型 SettlementStatus 未识别，按 varchar(50) 处理
- 表 mall_order_logistics 字段 shipper 类型 Shipper 未识别，按 varchar(50) 处理
- 表 mall_order_logistics 字段 state 类型 KdniaoLogisticsState 未识别，按 varchar(50) 处理
- 表 mall_order_logistics 字段 stateEx 类型 KdniaoLogisticsStateEx 未识别，按 varchar(50) 处理

## 其他告警（4 处）

- 表 mall_goods 的 INSERT 值(20)少于列(22)，缺的补 NULL
- 表 user_address 的 INSERT 值(12)少于列(16)，缺的补 NULL
- 表 user_balance_log 的 INSERT 值(13)少于列(16)，缺的补 NULL
- 表 user_info 的 INSERT 值(19)少于列(23)，缺的补 NULL
