package com.storm.boot3.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * @author wanxiaotao
 * @create 2022/11/26 9:53
 * @ClassName OrderStatusEnum
 * @Description
 */
@Getter
@AllArgsConstructor
public enum OrderStatusEnum {
    /**
     * 订单状态 1待支付；2支付中；3待派单；4待接单；5待报价；6待雇佣；7待预约；8待上门；9待核销；10待结算；11已完成；12已取消
     */

    STATUS_DZF(1, "待支付"),

    STATUS_ZFZ(2, "支付中"),

    STATUS_DPD(3, "待派单"),

    STATUS_DJD(4, "待接单"),

    STATUS_DBJ(5, "待报价"),

    STATUS_DGY(6, "待雇佣"),

    STATUS_DYY(7, "待预约"),

    STATUS_DSM(8, "待上门"),

    STATUS_DHX(9, "待核销"),

    STATUS_FWWC(10, "服务完成"),

    STATUS_YWC(11, "已完成"),

    STATUS_YQX(12, "已取消"),

    STATUS_YSWC(13, "验收完成"),
    ;

    private final Integer value;
    
    private final String desc;

    public static OrderStatusEnum getByValue(Integer value) {
        if (value == null) {
            throw new IllegalArgumentException("value is null");
        }
        for (OrderStatusEnum item : OrderStatusEnum.values()) {
            if (item.getValue().equals(value)) {
                return item;
            }
        }
        throw new IllegalArgumentException("not find OrderStatusEnum , value:" + value);
    }

    public static String getDesc(Integer value){
        return Arrays.stream(OrderStatusEnum.values()).filter(x -> x.getValue().equals(value)).map(OrderStatusEnum::getDesc).findFirst().orElse("");
    }
}
