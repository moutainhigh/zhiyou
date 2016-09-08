package com.zy.entity.sys;

import io.gd.generator.annotation.Type;

@Type(label = "审核状态")
public enum ConfirmStatus {
	待审核, 已通过, 未通过
}
