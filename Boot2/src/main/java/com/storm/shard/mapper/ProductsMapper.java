package com.storm.shard.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.storm.shard.entity.Products;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProductsMapper extends BaseMapper<Products> {
}