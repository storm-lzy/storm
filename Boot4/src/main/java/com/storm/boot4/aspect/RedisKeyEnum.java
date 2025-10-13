package com.storm.boot4.aspect;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author zsc
 * @create 2022/11/23 17:38
 * @ClassName RedisKeyEnum
 * @Description redis锁枚举
 */
@Getter
@AllArgsConstructor
public enum RedisKeyEnum implements EnumInterface {
    /**
     * 业务单(订单/服务单/售后服务单)状态锁
     */
    BIZ_STATUS_LOCK("ORDER_STATUS_LOCK_", "订单状态锁"),

    /**
     * 订单包状态锁
     */
    BIZ_STATUS_ORDER_PACKAGE_LOCK("BIZ_STATUS_ORDER_PACKAGE_LOCK_", "订单包状态锁"),

    /**
     * 订单推送push的锁
     */
    BIZ_ORDER_PUSH_LOCK("BIZ_ORDER_PUSH_LOCK_", "订单推送push的锁"),

    /**
     * 订单包抢包锁
     */
    BIZ_ROB_ORDER_PACKAGE_LOCK("ROB_ORDER_PACKAGE_LOCK", "订单包抢包锁"),
    /**
     * 师傅id锁
     */
    WORKER_ID("WORKER_ID_", "师傅id锁"),
    /**
     * 更换服务商订单锁
     */
    CHANGE_PROVIDER("CHANGE_PROVIDER_", "更换服务商订单锁"),
    /**
     * 发送订单核销验证码锁
     */
    ORDER_SEND_VERIFY_LOCK("ORDER_SEND_VERIFY_LOCK_", "发送订单核销验证码锁"),
    /**
     * 师傅每日接单数量key(包含接单+智能派单+人工转派+被雇佣的订单数量)
     */
    WORKER_RECEIVE_ORDER_COUNT_KEY("worker_receive_order_count_key_", "师傅每日接单数量key"),

    WORKER_SEND_ORDER_COUNT_LOCK_KEY("worker_send_order_count_lock_key_", "师傅每日派单数量锁key"),

    DS_WORKER_RECEIVE_ORDER_COUNT_KEY("ds_worker_receive_order_count_key_", "电商师傅每日接单数量key"),

    FOUR_DAY_WORKER_ORDER_STATISTICS_KEY("four_day_worker_order_statistics_key_", "融合/电商四日单量统计key"),

    RH_FOUR_DAY_ORDER_COUNT("rh_four_day_order_count", "融合师傅四日单量"),

    RH_FOUR_DAY_ORDER_AMOUNT("rh_four_day_order_amount", "融合师傅四日金额"),

    DS_FOUR_DAY_ORDER_COUNT("ds_four_day_order_count", "电商师傅四日单量"),

    DS_FOUR_DAY_ORDER_AMOUNT("ds_four_day_order_amount", "电商师傅四日金额"),

    RH_UPDATE_TIME("rh_update_time", "融合师傅四日金额/单量更新时间"),

    DS_UPDATE_TIME("ds_update_time", "电商师傅四日金额/单量更新时间"),

    TASK_SERVICE_NAME_LOCK_KEY("task_service_name_lock_key_", "导出任务taskName"),
    /**
     * 订单核销key
     */
    ORDER_VERIFY_KEY("order_verify_key_", "订单核销key"),
    /**
     * 预约次数key
     */
    APPOINTMENT_NUM_KEY("appointment_num_key_", "预约次数key"),
    /**
     * 订单支付
     */
    CREATE_PAY_KEY("pay_key_", "支付锁"),
    /**
     * 订单合单
     */
    MERGE_ORDER_KEY("merge_order_key_", "订单合单"),
    /**
     * 报价单-师傅报价
     */
    WORKER_OFFER_KEY("worker_offer_order_key_", "报价单师傅报价"),
    /**
     * 数据看板-订单最新数量
     */
    LASTED_ORDER_NUM_KEY("lasted_order_num_key", "订单最新数量"),
    /**
     * 订单今日之前总量
     */
    BEFORE_TODAY_ORDER_NUM_KEY("before_today_order_num_key", "订单今日之前总量"),
    /**
     * 服务商街道缓存
     */
    PROVIDER_STREET_LIST_KEY("AUTO_DISTRIBUTE_PROVIDER_LIST_", "服务商街道缓存"),
    /**
     * 服务商街道缓存锁
     */
    PROVIDER_STREET_LIST_LOCK_KEY("AUTO_DISTRIBUTE_PROVIDER_LIST_LOCK_", "服务商街道缓存锁"),
    /**
     * 师傅街道缓存
     */
    WORKER_STREET_LIST_KEY("AUTO_DISTRIBUTE_WORKER_LIST_", "师傅街道缓存"),
    /**
     * 师傅街道缓存锁
     */
    WORKER_STREET_LIST_LOCK_KEY("AUTO_DISTRIBUTE_WORKER_LIST_LOCK_", "师傅街道缓存锁"),

    /**
     * 师傅当天已经报价次数
     */
    WORKER_HAD_OFFER_TIMES_LOCK("WORKER_HAD_OFFER_TIMES_LOCK_", "师傅已经报价次数"),

    /**
     * 创建订单号序列---redis生成，自增
     */
    CREATE_ORDER_NO_SEQ("CREATE_ORDER_NO_SEQ", "创建订单号序列"),

    /**
     * 批量创建订单号序列---redis生成，自增
     */
    BATCH_CREATE_ORDER_NO_SEQ("BATCH_CREATE_ORDER_NO_SEQ", "批量创建订单号序列"),

    /**
     * 创建服务单号序列---redis生成，自增
     */
    CREATE_SERVICE_ORDER_NO_SEQ("CREATE_SERVICE_ORDER_NO_SEQ", "创建服务单号序列"),

    /**
     * 创建单据好序列---redis生成，自增
     */
    TRADE_NO_SEQ("TRADE_NO_SEQ", "创建单据号序列"),

    CREATE_RECEIPT_NO_SEQ("CREATE_ORDER_NO_SEQ", "创建单据号序列"),
    /**
     * 创建单据好序列---redis生成，自增
     */
    OLD_ORDER_CACHE("OLD_ORDER_CACHE_", "老平台查询订单缓存"),

    INTELLIGENT_ORDER_UPDATE_STATISTICS("INTELLIGENT_ORDER_UPDATE_STATISTICS", "智能派单每日定时更新统计信息标识"),

    /**
     * 创建退款单号序列---redis生成，自增
     */
    CREATE_REFUND_NO_SEQ("CREATE_REFUND_NO_SEQ", "创建退款单号序列"),

    /**
     * 创建质保卡序列---redis生成，自增
     */
    CREATE_WARRANTY_CARD_NO_SEQ("CREATE_WARRANTY_CARD_NO_SEQ", "创建质保卡序列"),
    /**
     * 完结退款提交申请
     */
    FINISH_REFUND_SUBMIT_LOCK("FINISH_REFUND_SUBMIT_LOCK_", "完结退款提交申请"),

    /**
     * 完结退款同意退款
     */
    FINISH_REFUND_AGREE_LOCK("FINISH_REFUND_AGREE_LOCK_", "完结退款同意退款"),

    /**
     * 完结退款拒绝退款
     */
    FINISH_REFUND_REFUSE_LOCK("FINISH_REFUND_REFUSE_LOCK_", "完结退款拒绝退款"),

    /**
     * 完结退款平台仲裁
     */
    FINISH_REFUND_ARBITRAMENT_LOCK("FINISH_REFUND_ARBITRAMENT_LOCK_", "完结退款平台仲裁"),
    /**
     * 当平台有2位师傅报价(可比价后)，触发短信通知雇佣师傅
     */
    SEND_PRICE_COMPARISON_NOTIFICATION_LOCK("SEND_PRICE_COMPARISON_NOTIFICATION_LOCK_", "当平台有2位师傅报价(可比价后)，触发短信通知雇佣师傅 锁"),

    UNIT_PAYMENT_BiZ_STR_KEY("UNIT_PAYMENT_BIZ_STR_KEY_", "异步支付透传参数key"),
    AUTO_OFFER_TIME_KEY("AUTO_OFFER_TIME_KEY", "自动报价的时间key"),

    ORDER_CONFIG_LOCK("ORDER_CONFIG_LOCK","订单统一导入全局锁"),

    AUTO_OFFER_CONFIG_LOCK("AUTO_OFFER_CONFIG_LOCK_", "自动报价的配置锁"),

    WORKER_ORDER_SETTLE_LOCK("ORDER_SETTLE_BATCH_LOCK","师傅单结批量导入锁"),

    TRANSFER_ORDER_MERCHANT_CONFIG_LOCK("TRANSFER_ORDER_MERCHANT_CONFIG_LOCK_", "转单商家配置锁"),
    TMALL_TRANSFER_ORDER_MERCHANT_CONFIG_LOCK("TMALL_TRANSFER_ORDER_MERCHANT_CONFIG_LOCK_", "天猫转单商家配置锁"),
    /**
     * 商家工作台自动雇佣失败异常推送
     */
    AUTO_HIRE_EXCEPTION_PUSH_INTERVAL_KEY("AUTO_HIRE_EXCEPTION_PUSH_INTERVAL_KEY_", "自动雇佣站内信异常推送间隔超时"),

    /**
     * 刷新订单表的师傅分组和商家分组
     */
    ORDER_REFRESH_GROUP_KEY("ORDER_REFRESH_GROUP_KEY", "刷新订单表的师傅分组和商家分组"),

    /**
     * 刷新订单表的师傅分组和商家分组 刷新间隔
     */
    ORDER_REFRESH_GROUP_TIME_KEY("ORDER_REFRESH_GROUP_TIME_KEY","刷新订单表的师傅分组和商家分组时间"),

    /**
     * 分组幂等处理
     */
    ORDER_REFRESH_GROUP_IDEMPOTENT_KEY("ORDER_REFRESH_GROUP_IDEMPOTENT_KEY","刷新订单表的师傅分组和商家分组幂等key"),

    /**
     * 天猫订单合单/派单/转类型锁
     */
    TMALL_ORDER_MERGE_DISTRIBUTE_KEY("TMALL_ORDER_MERGE_KEY_","天猫订单合单/派单/转类型锁"),

    /**
     * 1.手动订单/服务单师傅单结
     */
    MANUAL_WORKER_SINGLE_SETTLE_LOCK("MANUAL_WORKER_SINGLE_SETTLE_LOCK_", "手动订单/服务单师傅单结"),

    /**
     * 2.手动撤销订单/服务单师傅单结
     */
    MANUAL_CANCEL_WORKER_SINGLE_SETTLE_LOCK("MANUAL_CANCEL_WORKER_SINGLE_SETTLE_LOCK_", "手动撤销订单/服务单师傅单结"),

    /**
     * 3.手动订单/服务单商家单结
     */
    MANUAL_MERCHANT_SINGLE_SETTLE_LOCK("MANUAL_MERCHANT_SINGLE_SETTLE_LOCK_", "手动订单/服务单商家单结"),

    /**
     * 4.手动取消订单/服务单商家单结
     */
    MANUAL_CANCEL_MERCHANT_SINGLE_SETTLE_LOCK("MANUAL_CANCEL_MERCHANT_SINGLE_SETTLE_LOCK_", "手动取消订单/服务单商家单结"),

    /**
     * 5.1订单验收完成状态锁
     */
    ORDER_VALID_COMPLETED_LOCK("ORDER_VALID_COMPLETED_LOCK_", "订单验收完成状态锁"),

    /**
     * 自动验收重试key
     */
    AUTO_VERIFY_RETRY_KEY("AUTO_VERIFY_RETRY_KEY_", "自动验收重试key"),

    /**
     * 5.2服务单验收完成状态锁
     */
    SERVICE_VALID_COMPLETED_LOCK("SERVICE_VALID_COMPLETED_LOCK_", "服务单验收完成状态锁"),

    /**
     * 订单标记锁
     */
    ORDER_SIGN_LOCK("ORDER_SIGN_LOCK_", "订单标记锁"),

    ROB_HALL_WORKER_LIST("rob_hall_worker_list_", "师傅的抢单大厅列表缓存数据"),

    ORDER_COUPON_USE("order_coupon_use", "订单优惠券使用"),

    COUPON_STOCK("COUPON_STOCK_", "优惠券库存key"),
    COUPON_SEND_INTERCEPT("COUPON_SEND_INTERCEPT_", "优惠券发放拦截器key"),

    COUPON_RECEIVE_NUM_KEY("COUPON_RECEIVE_NUM_KEY_", "优惠券领取次数key"),
    COUPON_RECEIVE_MAX_NUM_KEY("COUPON_RECEIVE_MAX_NUM_KEY_", "优惠券领取最大次数key"),

    /**
     * 好评相关
     */
    ORDER_GOOD_REVIEW_AUDIT_KEY("order_good_review_audit_key","订单好评审核锁"),
    GOOD_REVIEW_GIVE_UP_WORKER_ID("good_review_give_up_worker_id_","放弃好评师傅id"),

    /**
     * 天猫创建调整单锁
     */
    TMALL_CREATE_ADJUSTMENT_LOCK("tmall_create_adjustment_lock_", "天猫创建调整单锁"),

    /**
     * 审核天猫调整单锁
     */
    TMALL_ADJUSTMENT_AUDIT_LOCK("tmall_adjustment_audit_lock_", "审核天猫调整单锁"),
    TMALL_PRICE_FACTOR_CACHE("tmall_price_factor_cache_","天猫订单计价因子缓存"),
    DOU_YIN_PRICE_FACTOR_CACHE("dou_yin_price_factor_cache_","抖音订单计价因子缓存"),
    /**
     * 处理费用调整单锁
     */
    TMALL_ADD_FEE_REVISION_LOCK("TMALL_ADD_FEE_REVISION_LOCK", "天猫订单添加费用调整单锁"),
    TMALl_RETRY_PERFORMANCE_LOCK("TMALl_RETRY_PERFORMANCE_LOCK", "天猫履约重试锁"),

    TMALL_ORDER_REFUND_LOCK("TMALL_ORDER_REFUND_LOCK_", "天猫订单取消"),
    /**
     * 修改客户手机号
     */
    UPDATECUSTOMMOBILELOCK("updateCustomMobile_","修改客户手机号"),
    /**
     * 订单列表角标数量key
     */
    ORDER_LIST_CORNER_NUM_KEY_("order_list_corner_num_key_", "订单列表角标数量key"),

    /**
     * 订单状态数量key
     */
    ORDER_STATUS_NUM_KEY_("order_status_num_key_", "订单状态数量key"),
    /**
     * 订单今日任务角标数量key
     **/
    ORDER_TODAY_TASK_CORNER_NUM_KEY_("order_today_task_corner_num_key_", "订单今日任务角标数量key"),
    /**
     * 订单发送钉钉机器人id
     **/
    ORDER_SEND_DING_DING_ROBOT_UUID("order_send_ding_ding_robot_uuid", "订单发送钉钉机器人id"),
    /**
     * ERP导入订单的特殊关单缓存(转单失败、无人接单)
     */
    ERP_IMPORT_ORDER_SPECIAL_CLOSE("ERP_IMPORT_ORDER_SPECIAL_CLOSE_", "ERP导入订单的特殊关单缓存(转单失败、无人接单)"),

    /**
     * 批量转派订单
     **/
    MANUAL_DISTRIBUTE_BATCH("manual_distribute_batch", "批量转派订单"),


    /**
     * 极速装相关redis key
     */
    FAST_INSTALL_SIGN_LOCK("FAST_INSTALL_SIGN_LOCK_", "极速装标记变更锁"),

    /**
     * 天猫订单同步履约记录锁
     */
    TMALL_ORDER_SYNC_PERFONACE_RECORD_LOCK("tmall_order_sync_perfonace_record_lock", "天猫订单同步履约记录锁"),

    /**
     * 天猫工人申请免异常 添加配置锁
     */
    TMALL_ORDER_WORKER_APPLY_NOT_ABNORMAL_CONFIG_ADD("tmall_order_worker_apply_config_add", "天猫工人申请免异常 添加配置锁"),

    /**
     * 天猫工人申请免异常 app发起申请锁
     */
    TMALL_ORDER_WORKER_APPLY_NOT_ABNORMAL_ADD("tmall_order_worker_apply_not_abnormal_add", "天猫工人申请免异常 app发起申请锁"),
    /**
     * 天猫工人申请免异常 管理端审核
     */
    TMALL_ORDER_WORKER_APPLY_AUDIT("tmall_order_worker_apply_audit", "天猫工人申请免异常 管理端审核"),

    ORDER_SETTLE_LOCK("ORDER_SETTLE_LOCK","刷新订单结算"),


    MAIN_CREATE_ORDER_NUM("main_create_order_num","主店铺下单量统计"),

    /**
     * 智能派单最终的派单记录id
     */
    ORDER_DISTRIBUTE_RECORD_ID("order_distribute_record_id", "智能派单最终的派单记录id"),

    TMALL_ORDER_PERFORMANCE_MIGRATE_LOCK("TMALL_ORDER_PERFORMANCE_MIGRATE_LOCK_", "天猫订单履约数据迁移锁"),

    DIGEST_DO_COMMAND_KEY("digest_do_command_key_", "消单后续动作命令缓存key"),

    ORDER_PROFIT_RATE_UPDATE("order_profit_rate_update_", "天猫订单利润率变更"),

    THE_CUSTOMER_NAME_KEY("the_customer_name_key","当日下单用户"),

    HIRE_WORKER_SERVICE_SCORE("hire_worker_service_score_%s", "雇佣师傅服务分展示缓存：1天"),

    BASE_CHECK_ORDER_CAN_DIGEST_LOCK("baseCheckOrderCanDigestLock", "基础校验订单是否可以消单lock"),
    BASE_CHECK_ORDER_CAN_DIGEST("baseCheckOrderCanDigest", "基础校验订单是否可以消单"),

    /**商家工作台我的订单(相关数量)缓存，默认1分钟，超时时间可配置*/
    PC_MERCHANT_HOME_ORDER_DATA_KEY("pc_merchant_home_order_data_key_%s", "商家工作台我的订单(相关数量)缓存，默认1分钟，超时时间可配置"),
    /***商家工作台我的数据-今日数据，默认1分钟，超时时间可配置*/
    PC_MERCHANT_HOME_TODAY_ORDER_DATA_KEY("pc_merchant_home_today_order_data_key_%s", "商家工作台我的数据-今日数据缓存，默认1分钟，超时时间可配置"),
    /***app首页相关数量缓存，默认1分钟，超时时间可配置*/
    APP_WORKER_HOME_ORDER_DATA_KEY("app_worker_home_order_data_key_%s_%s_%s", "app首页相关数量缓存，默认1分钟，超时时间可配置"),
    /***天猫工人申请待审核的记录数量(app端)等于0不展示缓存，默认1分钟，超时时间可配置*/
    ORDER_WORKER_TM_EXEMPT_FEE_APPLY_NUM_KEY("order_worker_tm_exempt_fee_apply_num_key_%s", "天猫工人申请待审核的记录数量(app端)等于0不展示缓存，默认1分钟，超时时间可配置"),

    USER_QUERY_ORDER_LIST_CACHE_KEY("USER_QUERY_ORDER_LIST_CACHE_KEY:","限制一个用户最多查询5个订单/每年"),

    H5_HOME_PAGE_GOODS_COUPON_PRICE("H5_HOME_PAGE_GOODS_COUPON_PRICE:{}","首页商品券后价格缓存,过期时间10秒,用户id维度"),

    OP_MERCHANT_ID_LIST_KEY("OP_MERCHANT_ID_LIST_KEY", "欧普商家idkey"),

    ORDER_CURRENT_DIGEST_MODEL("order_current_digest_model", "订单当前的消单模式，默认存7天，匠铭消单结束会删除"),

    EVALUATION_CONFIGURATION_CACHE("EVALUATION_CONFIGURATION_CACHE","评价配置信息缓存key"),

    EVALUATION_MODERATION_CACHE("EVALUATION_MODERATION_CACHE:","评价文本、图片审核唯一id和订单号映射key"),

    EVALUATION_MODERATION_PAY_LOCK("EVALUATION_MODERATION_PAY_LOCK_","评价打款唯一key，订单维度锁"),
    WORKER_ABNORMAL_FEE_APPLY_LOCK("WORKER_ABNORMAL_FEE_APPLY_LOCK_","师傅申请异常费用锁"),

    WORK_ORDER_CREATE_CHANGE_LOCK_KEY("WORK_ORDER_CREATE_CHANGE_LOCK_", "工单创建或者状态改变业务锁"),
    SCAN_CODE_REWARDS_CHECK_KEY("SCAN_CODE_REWARDS_CHECK_KEY_","防作弊逻辑校验-一个客户（公众号id）最多扫N个订单"),

    SCAN_CODE_REWARDS_CAL_KEY("SCAN_CODE_REWARDS_CAL_KEY_","师傅邀请用户扫码核销单量缓存"),
    ORDER_TIME_STATS_LISTENER_KEY("ORDER_TIME_STATS_LISTENER_KEY_","订单时间统计监听锁"),

    ONLINE_PAY_KEY("online_pay_key_", "在线支付锁"),

    ORDER_LOGISTICS_CHANGE_LOCK("ORDER_LOGISTICS_CHANGE_LOCK_","订单物流变更锁"),

    /**
     * 期待上门时间修改次数
     */
    EXPECT_VISIT_TIME_MODIFY_COUNT("expect_visit_time_modify_count_%s", "期待上门时间修改次数，订单维度"),

    LOCATION_VERIFY_AUDIT_LOCK("location_verify_audit_lock_", "定位核销审核锁"),

    LOCATION_VERIFY_APPLY_LOCK("location_verify_apply_lock_", "定位核销人工申请接入锁"),

    TMALL_ORDER_GOOD_REVIEW_KEY("TMALL_ORDER_GOOD_REVIEW_KEY", "天猫好评配置缓存")

    ;

    private String key;
    private String desc;

    @Override
    public String lockKey() {
        return this.key;
    }
}
