package com.storm.shardingsphere.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("t_district")
@Data
public class District {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private String districtName;

    private int level;
}