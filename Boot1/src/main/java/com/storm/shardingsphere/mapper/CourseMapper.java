package com.storm.shardingsphere.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.storm.shardingsphere.entity.Course;
import com.storm.shardingsphere.vo.CourseVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CourseMapper extends BaseMapper<Course> {

    @Select("SELECT \n" +
            "  c.corder_no,\n" +
            "  c.cname,\n" +
            "  COUNT(cs.id) num\n" +
            "FROM t_course c INNER JOIN t_course_section cs ON c.corder_no = cs.corder_no\n" +
            "GROUP BY c.corder_no,c.cname")
    List<CourseVo> getCourseNameAndSectionName();


}