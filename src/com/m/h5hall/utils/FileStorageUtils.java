package com.m.h5hall.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.os.Environment;
import android.provider.MediaStore.Video.Thumbnails;
import android.util.Log;

import com.m.h5hall.MyApplication;

/**
 * 文件存储类
 * @author yangsc
 */
public class FileStorageUtils {
	public final static String mBaseDir = MyApplication.getInstance().getApplicationContext()
			.getExternalFilesDir(null).getPath();
	
	/**
	 * 获取文件
	 * @param dir 文件路径
	 * @param file 文件名
	 * @return 文件对象
	 */
	public static File getFile(String dir, String file) {
		File mediaStorage = new File(mBaseDir, dir);
	    if (!mediaStorage.exists() && !mediaStorage.mkdirs()) {
	        Log.e("FileStorageUtils", "failed to create directory: " + mediaStorage);
	        return null;
	    }
		
	    return new File(mediaStorage, file);
	}
	
	public static File copyToTemp(File file, String tempName) {
		String[] nameA = tempName.split("\\.");
		File tmp = null;
		
		try {
			tmp = File.createTempFile(nameA[0], nameA[1]);
			FileOutputStream os = new FileOutputStream(tmp);
			FileInputStream is = new FileInputStream(file);
			byte[] b = new byte[1024];
			int tempbyte;
			while ((tempbyte = is.read(b)) != -1) {
				os.write(b, 0, tempbyte);
			}
			os.close();
			is.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return tmp;
	}
	
	public static void deleteFile(String dir, String file) {
		File _file = new File(mBaseDir + "/" + dir + "/" + file);
		if (_file.exists())
			_file.delete();
		
		MyApplication.getInstance().getMyPrefs().setFileEncryptStatus(mBaseDir + "/" + dir + "/" + file, false);
	}

	/**
	 * 删除文件夹
	 * @param dir 文件夹完整绝对路径
	 */
	public static void delDir(String dir) {
	     try {
	        delAllFile(mBaseDir + "/" + dir); //删除完里面所有内容
	        String filePath = mBaseDir + "/" + dir;
	        filePath = filePath.toString();
	        File myFilePath = new File(filePath);
	        myFilePath.delete(); //删除空文件夹
	     } catch (Exception e) {
	       e.printStackTrace(); 
	     }
	}

	/**
	 * 删除指定文件夹下所有文件
	 * @param path 文件夹完整绝对路径
	 * @return true or false
	 */
   public static boolean delAllFile(String path) {
       boolean flag = false;
       File file = new File(path);
       if (!file.exists()) {
         return flag;
       }
       if (!file.isDirectory()) {
         return flag;
       }
       String[] tempList = file.list();
       File temp = null;
       for (int i = 0; i < tempList.length; i++) {
          if (path.endsWith(File.separator)) {
             temp = new File(path + tempList[i]);
          } else {
              temp = new File(path + File.separator + tempList[i]);
          }
          if (temp.isFile()) {
             temp.delete();
             MyApplication.getInstance().getMyPrefs().setFileEncryptStatus(temp.getPath(), false);
          }
          if (temp.isDirectory()) {
             delAllFile(path + "/" + tempList[i]);//先删除文件夹里面的文件
          
             File myFilePath = new File(path + "/" + tempList[i]);
             boolean ret = myFilePath.delete(); //删除空文件夹
             Log.i("", "remove " + myFilePath.getPath() + " " + (ret ? "succ" : "fail"));
             flag = true;
          }
       }
       return flag;
    }
	
	public static void endecrypt(String dir, String file, int op) { // op: 1 encrypt  2 decrypt
		if (op == 1)
			FileEnDecryptUtils.encrypt(mBaseDir + "/" + dir + "/" + file);
		else
			FileEnDecryptUtils.decrypt(mBaseDir + "/" + dir + "/" + file);
	}
	
	public static void extractThumbnailFromVideoFile(String dir, String file) {
		try {
    		Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(mBaseDir + "/" + dir + "/" + file, Thumbnails.MINI_KIND);  
    		bitmap = ThumbnailUtils.extractThumbnail(bitmap, 200, 200);
    		
    		File output = new File(mBaseDir + "/" + dir + "/" + file.substring(0, file.lastIndexOf(".")) + ".jpg");
			OutputStream os = new FileOutputStream(output);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 80, os);
			os.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void saveImage(Bitmap bmp, String dir, String file) {
		try {
    		File output = new File(mBaseDir + "/" + dir + "/" + file);
			OutputStream os = new FileOutputStream(output);
			bmp.compress(Bitmap.CompressFormat.JPEG, 80, os);
			os.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	/**
	 * 保存图片文件
	 * @param dir 文件路径
	 * @param file 文件名
	 * @param decrypt
	 * @return 图片对象
	 */
	public static Bitmap getBitmapFromImageFile(String dir, String file, boolean decrypt) {
		if (decrypt)
			FileEnDecryptUtils.decrypt(mBaseDir + "/" + dir + "/" + file); // decrypt
		Bitmap bmp = BitmapUtils.loadBitmap(new File(mBaseDir + "/" + dir + "/" + file));
		FileEnDecryptUtils.encrypt(mBaseDir + "/" + dir + "/" + file); // encrypt
		return bmp;
	}
	
	/**
	 * 从文件获取图片，并压缩
	 * @param dir 文件路径
	 * @param file 文件名
	 * @param decrypt 是否解密
	 * @param size 压缩倍数
	 * @return 图片对象
	 */
	public static Bitmap getBitmapFromImageFile(String dir, String file, boolean decrypt, int size) {
		if (decrypt)
			FileEnDecryptUtils.decrypt(mBaseDir + "/" + dir + "/" + file); // decrypt
		Bitmap bmp = BitmapUtils.loadBitmap(new File(mBaseDir + "/" + dir + "/" + file), size);
		FileEnDecryptUtils.encrypt(mBaseDir + "/" + dir + "/" + file); // encrypt
		return bmp;
	}
}
