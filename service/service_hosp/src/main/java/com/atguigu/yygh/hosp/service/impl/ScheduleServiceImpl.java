package com.atguigu.yygh.hosp.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.atguigu.yygh.hosp.repository.ScheduleRepository;
import com.atguigu.yygh.hosp.service.ScheduleService;
import com.atguigu.yygh.model.hosp.Department;
import com.atguigu.yygh.model.hosp.Schedule;
import com.atguigu.yygh.vo.hosp.ScheduleQueryVo;
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
public class ScheduleServiceImpl implements ScheduleService {
	@Autowired
	private ScheduleRepository scheduleRepository;

	@Override
	public void save(Map<String, Object> paramMap) {
		String paramMapToString = JSONObject.toJSONString(paramMap);
		Schedule schedule = JSONObject.parseObject(paramMapToString, Schedule.class);
		Schedule scheduleExist = scheduleRepository.getScheduleByHoscodeAndHosScheduleId(schedule.getHoscode(), schedule.getHosScheduleId());
		if(scheduleExist!=null){
			scheduleExist.setUpdateTime(new Date());
			scheduleExist.setIsDeleted(0);
			scheduleExist.setStatus(1);
			scheduleRepository.save(scheduleExist);
		} else {
			schedule.setCreateTime(new Date());
			schedule.setUpdateTime(new Date());
			schedule.setIsDeleted(0);
			schedule.setStatus(1);
			scheduleRepository.save(schedule);
		}
	}

	@Override
	public Page<Schedule> findPageSchedule(int page, int limit, ScheduleQueryVo scheduleQueryVo) {
		// 创建pageAble对象，设置当前也，每夜记录数
		Pageable pageable = PageRequest.of(page-1,limit);
		// 创建example对象
		ExampleMatcher exampleMatcher = ExampleMatcher.matching()
				.withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING).withIgnoreCase();

		Schedule schedule = new Schedule();
		BeanUtils.copyProperties(scheduleQueryVo,schedule);
		schedule.setIsDeleted(0);
		schedule.setStatus(1);
		Example<Schedule> example = Example.of(schedule,exampleMatcher);
		Page<Schedule> all = scheduleRepository.findAll(example, pageable);
		return all;
	}

	@Override
	public void remove(String hoscode, String hosScheduleId) {
		// 查询数据库
		Schedule scheduleExist = scheduleRepository.getScheduleByHoscodeAndHosScheduleId(hoscode,hosScheduleId);
		if(scheduleExist!=null){
			scheduleRepository.deleteById(scheduleExist.getId());
		}
	}
}
