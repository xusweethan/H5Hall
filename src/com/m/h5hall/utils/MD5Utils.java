package com.m.h5hall.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5加密算法类
 * @author yangsc
 */
public class MD5Utils {

	public static String md5(String data) {
		try {
			MessageDigest digest = MessageDigest.getInstance("MD5");
			return hex(digest.digest(data.getBytes()));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String hex(byte[] bytes) {
		String hex = "";
		for (byte b : bytes) {
			hex += String.format("%02x", b & 0xff);
		}
		return hex;
	}

}
