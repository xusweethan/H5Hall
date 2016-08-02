package com.m.h5hall.utils;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

import com.m.h5hall.MyApplication;

/**
 * 加密解密管理类。
 * 加密算法：将文件的数据流的每个字节与该字节的下标异或。
 * 解密算法：已经加密的文件再执行一次对文件的数据流的每个字节与该字节的下标异或
 * @author yangsc
 */
public class FileEnDecryptUtils {
	
	private final static int REVERSE_LENGTH = 56;
	
	private static boolean crypt(String file, boolean curStatus) {
		int len = REVERSE_LENGTH;
		try {
			File f = new File(file);
			RandomAccessFile raf = new RandomAccessFile(f, "rw");
			long totalLen = raf.length();

			if (totalLen < REVERSE_LENGTH)
				len = (int) totalLen;

			FileChannel channel = raf.getChannel();
			MappedByteBuffer buffer = channel.map(
					FileChannel.MapMode.READ_WRITE, 0, REVERSE_LENGTH);
			byte tmp;
			for (int i = 0; i < len; ++i) {
				byte rawByte = buffer.get(i);
				tmp = (byte) (rawByte ^ i);
				buffer.put(i, tmp);
			}
			buffer.force();
			buffer.clear();
			channel.close();
			raf.close();
			
			MyApplication.getInstance().getMyPrefs().setFileEncryptStatus(file, !curStatus);
			
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * 加密
	 * @param strFile 源文件绝对路径
	 * @return
	 */
	public static boolean encrypt(String fileUrl) {
		boolean encrypted = MyApplication.getInstance().getMyPrefs().getFileEncryptStatus(fileUrl);
		if (encrypted)
			return true;
		
		return crypt(fileUrl, encrypted);
	}
	/**
	 * 解密
	 * @param strFile 源文件绝对路径
	 * @return
	 */
	public static boolean decrypt(String fileUrl) {
		boolean encrypted = MyApplication.getInstance().getMyPrefs().getFileEncryptStatus(fileUrl);
		if (!encrypted)
			return true;
		
		return crypt(fileUrl, encrypted);
	}
}
