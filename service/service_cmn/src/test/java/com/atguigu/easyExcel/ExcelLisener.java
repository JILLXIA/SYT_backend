package com.atguigu.easyExcel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;

import java.util.Map;

// 读取表格中的数据
public class ExcelLisener extends AnalysisEventListener<UserData> {
	// 一行一行读取excel，从第二行开始读取
	@Override
	public void invoke(UserData userData, AnalysisContext analysisContext) {
		System.out.println("userData"+userData);
	}

	@Override
	public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
		System.out.println("headMap"+headMap);
	}
	// 读取之后执行
	@Override
	public void doAfterAllAnalysed(AnalysisContext analysisContext) {
		System.out.println("analysisContext"+analysisContext);
	}
}
