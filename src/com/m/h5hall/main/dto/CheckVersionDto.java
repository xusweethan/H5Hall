package com.m.h5hall.main.dto;

import com.m.h5hall.comm.dto.BaseDto;

public class CheckVersionDto extends BaseDto {
	public static String PATH = "/api/checkVersion/{curVersion}";
	/** 网络请求返回的版本号 */
	public String version;
	/** 网络请求返回的新版本下载路径 */
	public String path;
};
