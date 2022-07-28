package com.atguigu.yygh.hosp.service;

import com.atguigu.yygh.hosp.repository.ScheduleRepository;
import com.atguigu.yygh.model.hosp.Schedule;
import com.atguigu.yygh.vo.hosp.ScheduleQueryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface ScheduleService {

	void save(Map<String, Object> paramMap);

	Page<Schedule> findPageSchedule(int page, int limit, ScheduleQueryVo scheduleQueryVo);

	void remove(String hoscode, String hosScheduleId);
}
