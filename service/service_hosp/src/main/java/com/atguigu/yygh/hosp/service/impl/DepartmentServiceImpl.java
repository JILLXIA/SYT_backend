package com.atguigu.yygh.hosp.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.yygh.hosp.repository.DepartmentRepository;
import com.atguigu.yygh.hosp.service.DepartmentService;
import com.atguigu.yygh.model.hosp.Department;
import com.atguigu.yygh.vo.hosp.DepartmentQueryVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

@Service
public class DepartmentServiceImpl implements DepartmentService {
	@Autowired
	DepartmentRepository departmentRepository;

	@Override
	public void save(Map<String, Object> paramMap) {
		String paramMapToString = JSONObject.toJSONString(paramMap);
		Department department = JSONObject.parseObject(paramMapToString, Department.class);

		Department departmentExist = departmentRepository.getDepartmentByHoscodeAndDepcode(department.getHoscode(), department.getDepcode());
		if(departmentExist!=null){
			departmentExist.setUpdateTime(new Date());
			departmentExist.setIsDeleted(0);
			departmentRepository.save(departmentExist);
		} else {
			department.setCreateTime(new Date());
			department.setUpdateTime(new Date());
			department.setIsDeleted(0);
			departmentRepository.save(department);
		}
	}

	@Override
	public Page<Department> findPageDepartment(int page, int limit, DepartmentQueryVo departmentQueryVo) {
		// 创建pageAble对象，设置当前也，每夜记录数
		Pageable pageable = PageRequest.of(page-1,limit);
		// 创建example对象
		ExampleMatcher exampleMatcher = ExampleMatcher.matching()
				.withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING).withIgnoreCase();

		Department department = new Department();
		BeanUtils.copyProperties(departmentQueryVo,department);
		department.setIsDeleted(0);
		Example<Department> example = Example.of(department,exampleMatcher);
		Page<Department> all = departmentRepository.findAll(example, pageable);
		return all;
	}

	@Override
	public void remove(String hoscode, String depcode) {
		Department departmentByHoscodeAndDepcode = departmentRepository.getDepartmentByHoscodeAndDepcode(hoscode, depcode);
		if(departmentByHoscodeAndDepcode!=null){
			departmentRepository.deleteById(hoscode);
		}
	}
}
