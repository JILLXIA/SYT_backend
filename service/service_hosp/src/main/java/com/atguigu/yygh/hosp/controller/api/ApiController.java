package com.atguigu.yygh.hosp.controller.api;

import com.atguigu.yygh.common.exception.YyghException;
import com.atguigu.yygh.common.helper.HttpRequestHelper;
import com.atguigu.yygh.common.result.Result;
import com.atguigu.yygh.common.result.ResultCodeEnum;
import com.atguigu.yygh.common.utils.MD5;
import com.atguigu.yygh.hosp.service.DepartmentService;
import com.atguigu.yygh.hosp.service.HospitalService;
import com.atguigu.yygh.hosp.service.HospitalSetService;
import com.atguigu.yygh.hosp.service.ScheduleService;
import com.atguigu.yygh.model.hosp.Department;
import com.atguigu.yygh.model.hosp.Hospital;
import com.atguigu.yygh.model.hosp.Schedule;
import com.atguigu.yygh.vo.hosp.DepartmentQueryVo;
import com.atguigu.yygh.vo.hosp.ScheduleQueryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/api/hosp")
public class ApiController {
	@Autowired
	HospitalService hospitalService;

	@Autowired
	HospitalSetService hospitalSetService;

	@Autowired
	DepartmentService departmentService;

	@Autowired
	ScheduleService scheduleService;
	// 上传医院接口
	@PostMapping("saveHospital")
	public Result saveHosp(HttpServletRequest request){
		// 获取医院信息
		Map<String, String[]> requestMap = request.getParameterMap();
		Map<String, Object> paramMap = HttpRequestHelper.switchMap(requestMap);
		// 获取医院系统中传递过来的签名
		String hospSign = (String)paramMap.get("sign");

		// 根据医院编号查询数据库
		String hoscode = (String)paramMap.get("hoscode");
		String signKey = hospitalSetService.getSignKey(hoscode);

		// 签名加密
		String signKeyMd5 = MD5.encrypt(signKey);

		//签名校验
		if(!hospSign.equals(signKeyMd5)) {
			throw new YyghException(ResultCodeEnum.SIGN_ERROR);
		}

		//传输过程中“+”转换为了“ ”，因此我们要转换回来
		String logoDataString = (String)paramMap.get("logoData");
		if(!StringUtils.isEmpty(logoDataString)) {
			String logoData = logoDataString.replaceAll("", "+");
			paramMap.put("logoData", logoData);
		}

		//调用service
		hospitalService.save(paramMap);
		return Result.ok();
	}

	// 查询医院
	@PostMapping("hospital/show")
	public Result getHospital(HttpServletRequest request){
		Map<String, String[]> requestMap = request.getParameterMap();
		Map<String, Object> paramMap = HttpRequestHelper.switchMap(requestMap);

		// 获取医院编号
		String hoscode = (String) paramMap.get("hoscode");

		// 获取医院系统中传递过来的签名
		String hospSign = (String)paramMap.get("sign");

		// 根据医院编号查询数据库
		String signKey = hospitalSetService.getSignKey(hoscode);

		// 签名加密
		String signKeyMd5 = MD5.encrypt(signKey);

		//签名校验
		if(!hospSign.equals(signKeyMd5)) {
			throw new YyghException(ResultCodeEnum.SIGN_ERROR);
		}

		// 根据医院编号查询 医院
		Hospital hospital = hospitalService.getByHoscode(hoscode);
		return Result.ok(hospital);
	}

	@PostMapping("saveDepartment")
	public Result saveDepartment(HttpServletRequest request){
		Map<String, String[]> requestMap = request.getParameterMap();
		Map<String, Object> paramMap = HttpRequestHelper.switchMap(requestMap);

		// 获取医院编号
		String hoscode = (String) paramMap.get("hoscode");

		// 获取医院系统中传递过来的签名
		String hospSign = (String)paramMap.get("sign");

		// 根据医院编号查询数据库
		String signKey = hospitalSetService.getSignKey(hoscode);

		// 签名加密
		String signKeyMd5 = MD5.encrypt(signKey);

		//签名校验
		if(!hospSign.equals(signKeyMd5)) {
			throw new YyghException(ResultCodeEnum.SIGN_ERROR);
		}
		departmentService.save(paramMap);
		return Result.ok();
	}

	// 查询科室接口
	@PostMapping("department/list")
	public Result findDepartment(HttpServletRequest request){
		Map<String, String[]> requestMap = request.getParameterMap();
		Map<String, Object> paramMap = HttpRequestHelper.switchMap(requestMap);

		// 获取医院编号
		int page = StringUtils.isEmpty(paramMap.get("page")) ? 1 : Integer.parseInt((String) paramMap.get("page"));
		int limit = StringUtils.isEmpty(paramMap.get("limit")) ? 1 : Integer.parseInt((String) paramMap.get("limit"));
		String hoscode = (String) paramMap.get("hoscode");

		// 获取医院系统中传递过来的签名
		String hospSign = (String)paramMap.get("sign");

		// 根据医院编号查询数据库
		String signKey = hospitalSetService.getSignKey(hoscode);

		// 签名加密
		String signKeyMd5 = MD5.encrypt(signKey);

		//签名校验
		if(!hospSign.equals(signKeyMd5)) {
			throw new YyghException(ResultCodeEnum.SIGN_ERROR);
		}

		DepartmentQueryVo departmentQueryVo = new DepartmentQueryVo();
		departmentQueryVo.setHoscode(hoscode);

		Page<Department> pageModel = departmentService.findPageDepartment(page,limit,departmentQueryVo);
		return Result.ok(pageModel);
	}

	@PostMapping("department/remove")
	public Result removeDepartment(HttpServletRequest request){
		Map<String, String[]> requestMap = request.getParameterMap();
		Map<String, Object> paramMap = HttpRequestHelper.switchMap(requestMap);

		String hoscode = (String) paramMap.get("hoscode");
		String depCode = (String) paramMap.get("depcode");
		// 获取医院系统中传递过来的签名
		String hospSign = (String)paramMap.get("sign");

		// 根据医院编号查询数据库
		String signKey = hospitalSetService.getSignKey(hoscode);

		// 签名加密
		String signKeyMd5 = MD5.encrypt(signKey);

		//签名校验
		if(!hospSign.equals(signKeyMd5)) {
			throw new YyghException(ResultCodeEnum.SIGN_ERROR);
		}

		departmentService.remove(hoscode, depCode);
		return Result.ok();
	}

	@PostMapping("saveSchedule")
	public Result saveSchedule(HttpServletRequest request){
		Map<String, String[]> requestMap = request.getParameterMap();
		Map<String, Object> paramMap = HttpRequestHelper.switchMap(requestMap);
		String hoscode = (String) paramMap.get("hoscode");
		String depCode = (String) paramMap.get("depcode");
		// 获取医院系统中传递过来的签名
		String hospSign = (String)paramMap.get("sign");

		// 根据医院编号查询数据库
		String signKey = hospitalSetService.getSignKey(hoscode);

		// 签名加密
		String signKeyMd5 = MD5.encrypt(signKey);

		//签名校验
		if(!hospSign.equals(signKeyMd5)) {
			throw new YyghException(ResultCodeEnum.SIGN_ERROR);
		}
		scheduleService.save(paramMap);
		return Result.ok();
	}

	@PostMapping("schedule/list")
	public Result findSchedule(HttpServletRequest request){
		Map<String, String[]> requestMap = request.getParameterMap();
		Map<String, Object> paramMap = HttpRequestHelper.switchMap(requestMap);
		String hoscode = (String) paramMap.get("hoscode");
		// String depcode = (String) paramMap.get("depcode");
		// 获取医院系统中传递过来的签名
		String hospSign = (String)paramMap.get("sign");

		// 根据医院编号查询数据库
		String signKey = hospitalSetService.getSignKey(hoscode);

		// 签名加密
		String signKeyMd5 = MD5.encrypt(signKey);

		//签名校验
		if(!hospSign.equals(signKeyMd5)) {
			throw new YyghException(ResultCodeEnum.SIGN_ERROR);
		}

		int page = StringUtils.isEmpty(paramMap.get("page")) ? 1 : Integer.parseInt((String) paramMap.get("page"));
		int limit = StringUtils.isEmpty(paramMap.get("limit")) ? 1 : Integer.parseInt((String) paramMap.get("limit"));
		ScheduleQueryVo scheduleQueryVo = new ScheduleQueryVo();
		scheduleQueryVo.setHoscode(hoscode);
		// scheduleQueryVo.setDepcode(depcode);
		Page<Schedule> pageModel = scheduleService.findPageSchedule(page,limit,scheduleQueryVo);
		return Result.ok(pageModel);
	}

	@PostMapping("schedule/remove")
	public Result removeSchedule(HttpServletRequest request){
		Map<String, String[]> requestMap = request.getParameterMap();
		Map<String, Object> paramMap = HttpRequestHelper.switchMap(requestMap);

		// 获取医院编号/排班编号
		String hoscode = (String) paramMap.get("hoscode");
		String hosScheduleId = (String) paramMap.get("hosScheduleId");

		// 获取医院系统中传递过来的签名
		String hospSign = (String)paramMap.get("sign");

		// 根据医院编号查询数据库
		String signKey = hospitalSetService.getSignKey(hoscode);

		// 签名加密
		String signKeyMd5 = MD5.encrypt(signKey);

		//签名校验
		if(!hospSign.equals(signKeyMd5)) {
			throw new YyghException(ResultCodeEnum.SIGN_ERROR);
		}
		scheduleService.remove(hoscode,hosScheduleId);
		return Result.ok();
	}
}
