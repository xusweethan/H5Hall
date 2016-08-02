package com.m.h5hall.utils;

import java.io.InputStream;

import com.m.h5hall.MyApplication;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class ImageUtil {
	/**
	 * 通过图片路径加载sd卡上的图片，并进行压缩，返回缩略图
	 * @param imagePath
	 * @param 缩放比例temp=5 即缩放为原来的1/5
	 * @return
	 */
     public static  Bitmap getBitmap(String imagePath,int temp){
    	 if (imagePath  == null) {
			return  null;
		}
    	 BitmapFactory.Options options =new BitmapFactory.Options();
         options.inJustDecodeBounds =true;
         // 获取这个图片的宽和高
         Bitmap bitmap =BitmapFactory.decodeFile(imagePath, options); //此时返回bm为空
          //计算缩放比
         int w = options.outWidth;  
         int h = options.outHeight;  
         //现在主流手机比较多是800*480分辨率，所以高和宽我们设置为  
         float hh = 800f;//这里设置高度为800f  
         float ww = 480f;//这里设置宽度为480f  
         //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可  
         int flag = 1;
         if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放  
             flag = options.outWidth;  
         } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放  
             flag = options.outHeight;  
         }  
         int be = (int)(flag/ (float)temp);
         while(be <= 0){
        	 be = 2;
         }
         options.inSampleSize = be;
         //重新读入图片，注意这次要把options.inJustDecodeBounds 设为 false哦
         options.inJustDecodeBounds =false;
         bitmap=BitmapFactory.decodeFile(imagePath,options);
    	 return bitmap;
     }
     /**
      * 通过资源文件在R中的id来获取图片缩略图
      * @param resoursId
      * @param 缩放比例temp=5 即缩放为原来的1/5
      * @return
      */
     public static Bitmap getBitmap(int resoursId,int temp) {
    	 
    	 BitmapFactory.Options options =new BitmapFactory.Options();
         options.inJustDecodeBounds =true;
         // 获取这个图片的宽和高
         Resources resources = MyApplication.getInstance().getResources();
         Bitmap bitmap = BitmapFactory.decodeResource(resources, resoursId, options); //此时只返回边界信息
       //计算缩放比
         int w = options.outWidth;  
         int h = options.outHeight;  
         //现在主流手机比较多是800*480分辨率，所以高和宽我们设置为  
         float hh = 800f;//这里设置高度为800f  
         float ww = 480f;//这里设置宽度为480f  
         //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可  
         int flag = 1;
         if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放  
             flag = options.outWidth;  
         } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放  
             flag = options.outHeight;  
         }  
         int be = (int)(flag/ (float)temp);
         while(be <= 0){
        	 be = 2;
         }
         options.inSampleSize = be;
         //重新读入图片，注意这次要把options.inJustDecodeBounds 设为 false哦
         options.inJustDecodeBounds =false;
         bitmap = BitmapFactory.decodeResource(resources, resoursId, options);
		 return bitmap;
	 }
     /**
      * 将InputStream 转换为bitmap，并按temp比例缩放
      * @param inputStream
      * @param temp
      * @return
      */
     public static Bitmap getBitmap(InputStream inputStream,int temp){
    	 if (inputStream == null) {
			return null;
		}
    	 BitmapFactory.Options options =new BitmapFactory.Options();
         options.inJustDecodeBounds =true;
         // 获取这个图片的宽和高
         Resources resources = MyApplication.getInstance().getResources();
         Bitmap bitmap = BitmapFactory.decodeStream(inputStream, null, options);//此时只返回边界信息
          //计算缩放比
//         int be = (int)(options.outHeight/ (float)200);
//         if (be <= 0)
//             be = 1;
       //计算缩放比
         int w = options.outWidth;  
         int h = options.outHeight;  
         //现在主流手机比较多是800*480分辨率，所以高和宽我们设置为  
         float hh = 800f;//这里设置高度为800f  
         float ww = 480f;//这里设置宽度为480f  
         //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可  
         int flag = 1;
         if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放  
             flag = options.outWidth;  
         } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放  
             flag = options.outHeight;  
         }  
         int be = (int)(flag/ (float)temp);
         while(be <= 0){
        	 be = 2;
         }
         options.inSampleSize = be;
         //重新读入图片，注意这次要把options.inJustDecodeBounds 设为 false哦
         options.inJustDecodeBounds =false;
         bitmap = BitmapFactory.decodeStream(inputStream, null, options);
		 return bitmap;
     }
}
