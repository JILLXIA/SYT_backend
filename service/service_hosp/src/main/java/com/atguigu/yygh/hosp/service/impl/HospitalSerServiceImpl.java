package com.atguigu.yygh.hosp.service.impl;

import com.atguigu.yygh.hosp.mapper.HospitalSetMapper;
import com.atguigu.yygh.hosp.service.HospitalSetService;
import com.atguigu.yygh.model.hosp.HospitalSet;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HospitalSerServiceImpl extends ServiceImpl<HospitalSetMapper, HospitalSet> implements HospitalSetService {
	@Override
	public String getSignKey(String hoscode) {
		QueryWrapper<HospitalSet> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("hoscode",hoscode);
		HospitalSet hospitalSet = baseMapper.selectOne(queryWrapper);
		return hospitalSet.getSignKey();
	}
	// @Autowired
	// 原本需要注入Mapper，但是框架里面帮我们注入了，所以可以不用写
}
