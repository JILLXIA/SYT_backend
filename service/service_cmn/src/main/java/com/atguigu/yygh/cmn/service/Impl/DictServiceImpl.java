package com.atguigu.yygh.cmn.service.Impl;

import com.atguigu.yygh.cmn.mapper.DictMapper;
import com.atguigu.yygh.cmn.service.DictService;
import com.atguigu.yygh.model.cmn.Dict;
import com.atguigu.yygh.model.hosp.HospitalSet;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements DictService {
	@Override
	public List<Dict> findChildData(Long id) {
		QueryWrapper<Dict> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("parent_id",id);
		List<Dict> list = baseMapper.selectList(queryWrapper);
		// list中每个Dict设置hasChild
		for(Dict dict:list){
			Long dictId = dict.getId();
			dict.setHasChildren(this.isChild(dictId));
		}
		return list;
	}

	// 判断id下面是否有子节点
	private boolean isChild(Long id) {
		QueryWrapper<Dict> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("parent_id",id);
		Integer count = baseMapper.selectCount(queryWrapper);
		return count > 0;
	}
	// @Autowired
	// 原本需要注入Mapper，但是框架里面帮我们注入了，所以可以不用写
}
