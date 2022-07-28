package com.atguigu.yygh.cmn.service.Impl;

import com.alibaba.excel.EasyExcel;
import com.atguigu.yygh.cmn.listener.DictListener;
import com.atguigu.yygh.cmn.mapper.DictMapper;
import com.atguigu.yygh.cmn.service.DictService;
import com.atguigu.yygh.model.cmn.Dict;
import com.atguigu.yygh.model.hosp.HospitalSet;
import com.atguigu.yygh.vo.cmn.DictEeVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements DictService {
	@Override
	@Cacheable(value = "dict",keyGenerator = "keyGenerator")
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

	@Override
	public void exportDictData(HttpServletResponse response) {
		response.setContentType("application/vnd.ms-excel"); // content类型
		response.setCharacterEncoding("utf-8");
		String fileName = "dict";
		// 让我的方式以下载打开
		response.setHeader("Content-disposition", "attachment;filename="+ fileName + ".xlsx");

		// 查询数据库
		List<Dict> dictList = baseMapper.selectList(null);
		List<DictEeVo> dictVo = new ArrayList<>();
		for(Dict dict:dictList){
			DictEeVo dictEeVo = new DictEeVo();
			BeanUtils.copyProperties(dict,dictEeVo);
			dictVo.add(dictEeVo);
		}
		// Dict -> DictEeVo
		// 调用easyExcel
		try {
			EasyExcel.write(response.getOutputStream(), DictEeVo.class).sheet("dict").doWrite(dictVo);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	@CacheEvict(value = "dict", allEntries=true)
	public void importDictData(MultipartFile multipartFile) {
		// 清空dict缓存下的所有内容
		try {
			//System.out.println("multipartFile"+multipartFile);
			//System.out.println("multipartFile.getInputStream()"+multipartFile.getInputStream());
			EasyExcel.read(multipartFile.getInputStream(),DictEeVo.class,new DictListener(baseMapper)).sheet().doRead();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getDictName(String dictCode, String value) {
		if(StringUtils.isEmpty(dictCode)){
			QueryWrapper<Dict> queryWrapper = new QueryWrapper<>();
			queryWrapper.eq("value",value);
			return baseMapper.selectOne(queryWrapper).getName();
		} else {
			QueryWrapper<Dict> queryWrapper = new QueryWrapper<>();
			queryWrapper.eq("dict_code",dictCode);
			long parentID = baseMapper.selectOne(queryWrapper).getId();
			QueryWrapper<Dict> query = new QueryWrapper<>();
			query.eq("parent_id",parentID);
			query.eq("value",value);
			return baseMapper.selectOne(query).getName();
		}
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
