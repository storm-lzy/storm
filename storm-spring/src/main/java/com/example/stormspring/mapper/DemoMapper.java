package com.example.stormspring.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.stormspring.entity.DemoEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DemoMapper extends BaseMapper<DemoEntity> {

    @Insert("INSERT INTO `demo` (`id`,`name`,`age`,`email`) VALUES (#{id},#{name},#{age},#{email})")
    int insert(DemoEntity demo);
}
