package com.atguigu.yygh.cmn.controller;

import com.atguigu.yygh.cmn.service.DictService;
import com.atguigu.yygh.common.result.Result;
import com.atguigu.yygh.model.cmn.Dict;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Api(value="数据字典接口")
@RestController
@CrossOrigin
@RequestMapping("/admin/cmn/dict")
public class DictController {
	@Autowired
	private DictService dictService;

	// 导入数据字典
	@PostMapping("importData")
	public Result importDict(MultipartFile file){
		// multipartfile 用于得到传的文件
		dictService.importDictData(file);
		return Result.ok();
	}

	// 导出数据字典的方法
	@GetMapping("exportData")
	public void exportDict(HttpServletResponse response){
		// 用户自定义路径下载
		dictService.exportDictData(response);
	}

	// 根据数据id查询子数据
	@ApiOperation(value="根据数据id查询子数据")
	@GetMapping("findChildData/{id}")
	public Result findChildData(@PathVariable Long id){
		List<Dict> list = dictService.findChildData(id);
		return Result.ok(list);
	}

	// 根据dictCode和value查询
	@GetMapping("getName/{dictCode}/{value}")
	public String getName(@PathVariable String dictCode,
												@PathVariable String value)
	{
		String dictName = dictService.getDictName(dictCode,value);
		return dictName;
	}
	// 根据value查询
	@GetMapping("getName/{value}")
	public String getName(@PathVariable String value)
	{
		String dictName = dictService.getDictName("",value);
		return dictName;
	}
}
