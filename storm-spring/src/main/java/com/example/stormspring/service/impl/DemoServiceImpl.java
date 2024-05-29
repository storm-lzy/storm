package com.example.stormspring.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.stormspring.entity.DemoEntity;
import com.example.stormspring.mapper.DemoMapper;
import com.example.stormspring.service.IDemoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 *
 */
@Service
public class DemoServiceImpl extends ServiceImpl<DemoMapper,DemoEntity> implements IDemoService {

    @Resource
    private DemoMapper demoMapper;

    @Transactional
    public void insert(DemoEntity demo) {
        demoMapper.insert(demo);
    }

    public DemoEntity getUserById(Long userId) {
        return baseMapper.selectById(userId);
    }
}
