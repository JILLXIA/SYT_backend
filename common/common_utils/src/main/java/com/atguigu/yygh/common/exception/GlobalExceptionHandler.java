package com.atguigu.yygh.common.exception;

import com.atguigu.yygh.common.result.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
// 结果用Json 格式输出

@ControllerAdvice
public class GlobalExceptionHandler {

	// 出现异常这个就会执行
	@ExceptionHandler(Exception.class)
	@ResponseBody
	public Result error(Exception e){
		e.printStackTrace();
		return Result.fail();
	}

	// 自定义异常
	@ExceptionHandler(YyghException.class)
	@ResponseBody
	public Result error(YyghException e){
		e.printStackTrace();
		return Result.build(e.getCode(),e.getMessage());
	}
}
