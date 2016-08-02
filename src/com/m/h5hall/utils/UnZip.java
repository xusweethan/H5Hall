package com.m.h5hall.utils;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;


public class UnZip {

	private boolean status = false;

	public UnZip(String file, String outPath) {
		String unzipfile = file; //解压缩的文件名包含路径 
		try { 
			ZipInputStream zin = new ZipInputStream(new FileInputStream(unzipfile)); 
			ZipEntry entry; 
			//创建文件夹 
			while ( (entry = zin.getNextEntry()) != null) { 
				if (entry.isDirectory()) { 
					File directory = new File(outPath, entry.getName()); 
					if (!directory.exists()) 
						directory.mkdirs(); 
				}
					
				if (!entry.isDirectory()) { 
					File myFile = new File(entry.getName()); 
					
					FileOutputStream fout = new FileOutputStream(outPath+myFile.getPath()); 
					DataOutputStream dout = new DataOutputStream(fout); 
					byte[] b = new byte[1024]; 
					int len = 0; 
					while ( (len = zin.read(b)) != -1) { 
						dout.write(b, 0, len); 
					} 
				
					dout.close(); 
					fout.close(); 
					zin.closeEntry(); 
					status=true;
				} 
			} 
		} catch (IOException e) { 
			e.printStackTrace(); 
		}

	}

	public boolean getStatus(){
		return status;
	}

}
