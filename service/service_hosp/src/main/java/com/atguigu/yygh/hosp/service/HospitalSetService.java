package com.atguigu.yygh.hosp.service;

import com.atguigu.yygh.model.hosp.HospitalSet;
import com.baomidou.mybatisplus.extension.service.IService;
import jdk.internal.org.objectweb.asm.tree.IincInsnNode;

public interface HospitalSetService extends IService<HospitalSet> {
	String getSignKey(String hoscode);
}
