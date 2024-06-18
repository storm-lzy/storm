package com.storm.shardingsphere.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

@TableName("t_course_section")
@Data
@ToString
public class CourseSection {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private Long cid;  //课程id

    private Long corderNo;

    private Long userId;

    private String sectionName;

    private int status;
}