package com.storm.shardingsphere.vo;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 *
 */
@Data
@ToString
public class CourseVo implements Serializable {

    private long corderNo;

    private String cname;

    private int num;
}
