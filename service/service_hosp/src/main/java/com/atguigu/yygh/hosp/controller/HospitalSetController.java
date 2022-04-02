package com.atguigu.yygh.hosp.controller;

import com.atguigu.yygh.hosp.service.HospitalSetService;
import com.atguigu.yygh.model.hosp.HospitalSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("admin/hosp/hospitalSet")
public class HospitalSetController {
	// 注入Service
	@Autowired
	private HospitalSetService hospitalSetService;

	// 1.查询医院设置表所有信息
	// 查询方式的注解
	@GetMapping("findAll")
	public List<HospitalSet> findAllHospitalSet(){
		// mp在service中的封装
		List<HospitalSet> list = hospitalSetService.list();
		// 会把list 转化成json，利用jeckson
		return list;
	}

	// 2. 逻辑删除
	@DeleteMapping("{id}")
	public boolean removeHospitalSet(@PathVariable Long id){
		// 通过路径传值需要注解
		// delete请求不能用浏览器直接访问到
		return hospitalSetService.removeById(id);
	}
}
