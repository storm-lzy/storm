package com.example.stormspring.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 *
 */
@TableName("demo")
@Data
public class DemoEntity implements Serializable {

    @TableId(
            value = "id",
            type = IdType.ASSIGN_ID
    )
    private Long id;

    private String name;

    private String age;

    private String email;


}
