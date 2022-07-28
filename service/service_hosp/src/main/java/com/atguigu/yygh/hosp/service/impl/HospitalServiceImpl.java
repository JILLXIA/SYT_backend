package com.atguigu.yygh.hosp.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.yygh.hosp.repository.HospitalRepository;
import com.atguigu.yygh.hosp.service.HospitalService;
import com.atguigu.yygh.model.hosp.Hospital;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

@Service
public class HospitalServiceImpl implements HospitalService {

	@Autowired
	HospitalRepository hospitalRepository;

	@Override
	public void save(Map<String, Object> paramMap) {
		// map集合转化为对象
		String mapString = JSONObject.toJSONString(paramMap);
		Hospital hospital = JSONObject.parseObject(mapString, Hospital.class);

		// 判断是否存在相同数据，不存在就进行添加，存在就直接修改
		String hosCode = hospital.getHoscode();
		Hospital hospitalExist = hospitalRepository.getHospitalByHoscode(hosCode);
		if(hospitalExist!=null){
			hospital.setStatus(hospitalExist.getStatus());
			hospital.setCreateTime(hospitalExist.getCreateTime());
			hospital.setUpdateTime(new Date());
			hospital.setIsDeleted(0);
			hospitalRepository.save(hospital);
		} else {
			hospital.setStatus(0);
			hospital.setCreateTime(new Date());
			hospital.setUpdateTime(new Date());
			hospital.setIsDeleted(0);
			hospitalRepository.save(hospital);
		}
	}

	@Override
	public Hospital getByHoscode(String hoscode) {
		Hospital hospital = hospitalRepository.getHospitalByHoscode(hoscode);
		return hospital;
	}
}
