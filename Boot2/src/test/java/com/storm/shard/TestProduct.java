package com.storm.shard;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.storm.shard.entity.Products;
import com.storm.shard.mapper.ProductsMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 *
 */
@SpringBootTest(classes = PoolApplication.class)
@Slf4j
public class TestProduct {

    @Resource
    private ProductsMapper productsMapper;

    //插入测试
    @Test
    public void testInsert(){

        Products products = new Products();
        products.setPname("电视机");
        products.setPrice(100);
        products.setFlag("0");

        productsMapper.insert(products);
    }


    @Test
    public void testSelect(){

        QueryWrapper<Products> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("pname","电视机");
        List<Products> products = productsMapper.selectList(queryWrapper);

        products.forEach(System.err::println);
    }

    //事务测试
    @Transactional  //开启事务
    @Test
    public void testTrans(){

        Products products = new Products();
        products.setPname("洗碗机");
        products.setPrice(2000);
        products.setFlag("1");
        productsMapper.insert(products);

        QueryWrapper<Products> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("pname","洗碗机");
        List<Products> list = productsMapper.selectList(queryWrapper);
        list.forEach(System.out::println);
    }
}
