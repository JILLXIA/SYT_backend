package com.atguigu.yygh.hosp.controller;

import com.atguigu.yygh.common.exception.YyghException;
import com.atguigu.yygh.common.result.Result;
import com.atguigu.yygh.hosp.service.HospitalSetService;
import com.atguigu.yygh.model.hosp.HospitalSet;
import com.atguigu.yygh.vo.hosp.HospitalQueryVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.atguigu.yygh.common.util.MD5;

import java.util.List;
import java.util.Random;

import static com.atguigu.yygh.common.result.ResultCodeEnum.*;

@Api("医院制度管理")
@RestController
@RequestMapping("admin/hosp/hospitalSet")
@CrossOrigin // 跨域访问
public class HospitalSetController {
	// 注入Service
	@Autowired
	private HospitalSetService hospitalSetService;

	// 1.查询医院设置表所有信息
	// 查询方式的注解
	@ApiOperation("获取所有医院设置信息")
	@GetMapping("findAll")
	public Result findAllHospitalSet(){
		// mp在service中的封装
		List<HospitalSet> list = hospitalSetService.list();
		try {
			// int a = 1 / 0;
		}catch(Exception e){
			// 需要手动抛出异常
			throw new YyghException(SERVICE_ERROR);
		}
		// 会把list 转化成json，利用jeckson
		return Result.ok(list);
	}

	// 2. 逻辑删除
	@ApiOperation("逻辑删除医院设置信息")
	@DeleteMapping("{id}")
	public Result removeHospitalSet(@PathVariable Long id){
		// 通过路径传值需要注解
		// delete请求不能用浏览器直接访问到
		boolean flag = hospitalSetService.removeById(id);
		if(flag){
			return Result.ok(flag);
		}
		return Result.fail(hospitalSetService.removeById(id));
	}

	// 3. 条件查询 带分页
	// 创建vo类
	//controller,条件对象，分页数据
	@PostMapping("findPage/{current}/{limit}")
	public Result findPageHospital(@PathVariable long current, @PathVariable long limit,
																 @RequestBody(required = false) HospitalQueryVo hospitalQueryVo){
		// RequestBody 利用json传数据，方便前端，比如改成PostMapping
		// page对象
		Page<HospitalSet> page = new Page<>(current,limit);
		// 构造分页查询条件,条件值可能有，可能没有
		QueryWrapper<HospitalSet> queryWrapper = new QueryWrapper();
		String hosname = hospitalQueryVo.getHosname(); // 医院名称
		String hoscode = hospitalQueryVo.getHoscode(); // 医院编号
		if(!StringUtils.isEmpty(hosname)){
			queryWrapper.like("hosname",hospitalQueryVo.getHosname());
		}
		if(!StringUtils.isEmpty(hoscode)){
			queryWrapper.like("hoscode",hospitalQueryVo.getHoscode());
		}
		// 调用方法实现分页查询
		Page<HospitalSet> hospitalSetPage = hospitalSetService.page(page,queryWrapper);
		return Result.ok(hospitalSetPage);
	}

	// 4. 添加医院设置
	@PostMapping("saveHospitalSet")
	public Result saveHospitalSet(@RequestBody HospitalSet hospitalSet){
		// 设置状态 1可以用 0不能用
		hospitalSet.setStatus(1);
		// 签名密钥
		Random random = new Random();
		hospitalSet.setSignKey(MD5.encrypt(System.currentTimeMillis()+""+random.nextInt(1000)));

		// 调用service方法
		boolean save = hospitalSetService.save(hospitalSet);
		if(save){
			return Result.ok();
		}
		return Result.fail();

	}
	// 5. 根据id 找医院
	@GetMapping("getHospitalSet")
	public Result getHospitalSet(@PathVariable Long id){
		return Result.ok(hospitalSetService.getById(id));
	}

	// 6. 修改医院信息
	@PostMapping("updateHospitalSet")
	public Result updateHospitalSet(@RequestBody HospitalSet hospitalSet){
		boolean update = hospitalSetService.updateById(hospitalSet);
		if(update){
			return Result.ok();
		}
		return Result.fail();
	}

	// 7. 批量删除
	@DeleteMapping("batchRemove")
	public Result batchRemoveHospitalSet(@RequestBody List<Long> idLists){
		hospitalSetService.removeByIds(idLists);
		return Result.ok();
	}

	// 8. 医院设置锁定、解锁
	@PutMapping("lockHospitalSet/{id}/{status}")
	public Result lockHospitalSet(@PathVariable Long id,@PathVariable int status){
		// put 是幂等
		HospitalSet hospitalSet = hospitalSetService.getById(id);
		hospitalSet.setStatus(status);
		hospitalSetService.updateById(hospitalSet);
		return Result.ok();
	}

	// 9. 发送签名的key，发送短信
	@PutMapping("sendKey/{id}")
	public Result lockHospitalSet(@PathVariable Long id){
		HospitalSet hospitalSet = hospitalSetService.getById(id);
		String signKey = hospitalSet.getSignKey();
		String hoscode = hospitalSet.getHoscode();
		//TODO 发送短信
		return Result.ok();

	}

}
