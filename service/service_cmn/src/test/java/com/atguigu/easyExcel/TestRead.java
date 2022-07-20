package com.atguigu.easyExcel;

import com.alibaba.excel.EasyExcel;

public class TestRead {
	public static void main(String[] args){
		String fileName = "/Users/xiayudi/Desktop/backend/easyExcel_test/test.xlsx";
		EasyExcel.read(fileName,UserData.class,new ExcelLisener()).sheet().doRead(); // 监听器的invoke方法就会执行了
	}
}
