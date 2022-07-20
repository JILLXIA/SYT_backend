package com.atguigu.easyExcel;

import com.alibaba.excel.EasyExcel;


import java.util.ArrayList;
import java.util.List;

public class TestWrite {
	public static void main(String[] args){
		String fileName = "/Users/xiayudi/Desktop/backend/easyExcel_test/test.xlsx";
		List<UserData> list = new ArrayList<UserData>();
		for(int i = 0;i<10;i++){
			UserData ud = new UserData();
			ud.setId(i);
			ud.setUserName("lucy"+i);
			list.add(ud);
		}
		EasyExcel.write(fileName,UserData.class).sheet("用户信息").doWrite(list);
	}
}
