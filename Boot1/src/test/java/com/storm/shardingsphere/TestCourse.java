package com.storm.shardingsphere;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.storm.shardingsphere.entity.Course;
import com.storm.shardingsphere.entity.CourseSection;
import com.storm.shardingsphere.entity.District;
import com.storm.shardingsphere.mapper.CourseMapper;
import com.storm.shardingsphere.mapper.CourseSectionMapper;
import com.storm.shardingsphere.mapper.DistrictMapper;
import com.storm.shardingsphere.vo.CourseVo;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

/**
 *
 */
@SpringBootTest(classes = ShardApplication.class)
@Slf4j
public class TestCourse {

    @Autowired
    private CourseMapper courseMapper;

    @Resource
    private CourseSectionMapper courseSectionMapper;
    @Resource
    private DistrictMapper districtMapper;

    @Test
    public void testInsertCourseAndCourseSection() {

        //userID为奇数  -->  msb_course_db1数据库
        for (int i = 11; i <= 20; i++) {
            Course course = new Course();
            course.setUserId((long) i);
            //CorderNo为偶数 --> t_course_0, 为奇数t_course_1
            course.setCorderNo(100 + (long) i + 1);
            course.setPrice(100.0);
            course.setCname("ShardingSphere实战");
            course.setBrief("ShardingSphere实战-直播课");
            course.setStatus(1);
            courseMapper.insert(course);

            Long cid = course.getCid();
            for (int j = 6; j <= 10; j++) {  //每个课程 设置三个章节
                CourseSection section = new CourseSection();
                section.setUserId((long) i);
                //CorderNo为偶数 --> t_course_0, 为奇数t_course_1
                section.setCorderNo(100 + (long) i + 1);
                section.setCid(cid);
                section.setSectionName("ShardingSphere实战_" + i);
                section.setStatus(1);
                courseSectionMapper.insert(section);
            }
        }
    }


    @Test
    public void testSelectCourseDetail() {
        List<CourseVo> courseVo = courseMapper.getCourseNameAndSectionName();
        for (CourseVo vo : courseVo) {
            System.err.println(vo);
        }
    }

    //广播表: 插入数据
    @Test
    public void testBroadcast(){
        District district = new District();
        district.setDistrictName("昌平区");
        district.setLevel(1);

        districtMapper.insert(district);
    }


    //查询操作，只从一个节点获取数据, 随机负载均衡规则
    @Test
    public void testSelectBroadcast(){

        List<District> districtList = districtMapper.selectList(null);
        districtList.forEach(System.out::println);
    }


}
