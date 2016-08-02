package com.m.h5hall.utils;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.m.h5hall.MyApplication;

/**
 * 图片处理类
 * 
 * @author yangsc
 */
public class BitmapUtils {
	public static final String PACKGET_NAME_SCHOOL = "school";
	public static final int mTrillion = (int) (0.5 * 1024);
	public static final String SCHOOL_IMG_PATH = Environment
			.getExternalStorageDirectory()
			+ File.separator
			+ PACKGET_NAME_SCHOOL;
	public static final Uri IMAGE_CAPTURE_URI;
	static {
		IMAGE_CAPTURE_URI = Uri.fromFile(new File(Environment
				.getExternalStorageDirectory(), "tmp_avatar.jpg"));
	}

	public static Bitmap getViewBitmap(View view, int bgColor) {
		int width = view.getWidth();
		int height = view.getHeight();
		Bitmap bitmap = Bitmap.createBitmap(width, height,
				Bitmap.Config.RGB_565);
		if (bitmap != null) {
			Canvas canvas = new Canvas(bitmap);
			canvas.drawColor(bgColor);
			view.draw(canvas);
		}
		return bitmap;
	}

	/**
	 * 保存图片
	 * 
	 * @param context
	 *            上下文对象
	 * @param bitmap
	 *            图片对象
	 * @param filename
	 *            文件名
	 * @return 文件路径
	 */
	public static String saveBitmap(Context context, Bitmap bitmap,
			String filename) {
		if (bitmap != null) {
			File dir = Environment
					.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
			if (!dir.exists())
				dir.mkdirs();

			File file = new File(dir, filename);
			if (file.exists()) {
				file.delete();
				file = new File(dir, filename);
			}
			try {
				OutputStream os = new FileOutputStream(file);
				bitmap.compress(Bitmap.CompressFormat.PNG, 80, os);
				os.close();
				return file.getPath();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 加载图片
	 * 
	 * @param file
	 *            文件类
	 * @return 图片对象
	 */
	public static Bitmap loadBitmap(File file) {
		if (file.exists()) {
			synchronized (file) {
				BitmapFactory.Options opts = new BitmapFactory.Options();
				opts.inDither = false;
				opts.inPreferredConfig = Bitmap.Config.RGB_565;
				opts.inSampleSize = 4;
//				opts.inJustDecodeBounds = true;
				opts.inScaled = false;
				Bitmap btm = BitmapFactory.decodeFile(file.getPath(), opts);
				return btm;
			}
		}
		return null;
	}
	
	/**
	 * 加载图片，并压缩
	 * @param file 文件类
	 * @param size 缩小倍数
	 * @return 图片对象
	 */
	public static Bitmap loadBitmap(File file, int size) {
		if (file.exists()) {
			synchronized (file) {
				BitmapFactory.Options opts = new BitmapFactory.Options();
				opts.inDither = false;
				opts.inPreferredConfig = Bitmap.Config.RGB_565;
				opts.inSampleSize = size;
				opts.inScaled = false;
				Bitmap btm = BitmapFactory.decodeFile(file.getPath(), opts);
				return btm;
			}
		}
		return null;
	}

	/**
	 * Generate a no repeat image name. Current, use current time.
	 * 
	 * @return file name
	 */
	public static String generateImageName() {
		Time time = new Time(); // or Time t=new Time("GMT+8"); 加上Time Zone资料。
		time.setToNow(); // get the system current time
		StringBuffer timeString = new StringBuffer();
		timeString.append(time.year);
		timeString.append(time.month + 1);
		timeString.append(time.monthDay);
		timeString.append(time.hour);
		timeString.append(time.minute);
		timeString.append(time.second);

		return timeString.toString();
	}

	/**
	 * judge if sdCard exist
	 * 
	 * @return true if exist, or false
	 */
	public static boolean isSDCardExist() {
		return Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);
	}

	/**
	 * 图片压缩方法实现
	 * 
	 * @param srcPath
	 * @return
	 */
	public static Bitmap getimage(Context context, String srcPath) {
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		// 开始读入图片，此时把options.inJustDecodeBounds 设回true了
		newOpts.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(srcPath, newOpts);

		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		int hh = wm.getDefaultDisplay().getHeight();// 这里设置高度为800f
		int ww = wm.getDefaultDisplay().getWidth();// 这里设置宽度为480f
		System.out.println("width = " + ww + ", height = " + hh);
		// 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
		int be = 1;// be=1表示不缩放
		if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
			be = newOpts.outWidth / ww;
		} else if (w < h && h > hh) {// 如果高度高的话根据高度固定大小缩放
			be = newOpts.outHeight / hh;
		}
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be;// 设置缩放比例
		// 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
		return BitmapFactory.decodeFile(srcPath, newOpts);
	}

	/**
	 * 质量压缩
	 * 
	 * @param image
	 * @return
	 */
	public static Bitmap compressImage(Bitmap image) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		int options = 100;
		while (baos.toByteArray().length / 1024 > 100) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
			baos.reset();// 重置baos即清空baos
			options -= 10;// 每次都减少10
			image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中

		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
		// Drawable drawable1 = new BitmapDrawable(bitmap);
		// faceImage.setImageDrawable(drawable1);
		// btcontruler = true;
		try {
			FileOutputStream out = new FileOutputStream(
					Environment.getExternalStorageDirectory()
							+ "/faceImage1.jpg");
			Log.i("path", Environment.getExternalStorageDirectory()
					+ "/faceImage1.jpg");
			bitmap.compress(Bitmap.CompressFormat.PNG, 80, out);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bitmap;
	}

	/**
	 * Get the image real size.
	 * 
	 * @param name
	 *            the image name.
	 * @return the image size.
	 */

	public static int getImageSize(Uri uri) {
		try {
			File file = new File(uri.getPath());
			BufferedInputStream bufferedInputStream = new BufferedInputStream(
					new FileInputStream(file));
			int bytesAvailable = bufferedInputStream.available();
			bufferedInputStream.close();
			return bytesAvailable / 1024;
		} catch (FileNotFoundException e) {
			Log.v("img size", "The img comress error." + e);
			return 0;
		} catch (IOException e) {
			Log.v("img size", "The img comress error." + e);
			return 0;
		}
	}

	/**
	 * Copy a new file.
	 * 
	 * @param path
	 * @param desPath
	 */
	public static void save(String path, String desPath) {
		try {
			InputStream inStream = new FileInputStream(path);
			FileOutputStream out = new FileOutputStream(desPath);
			byte[] b = new byte[inStream.available()];
			inStream.read(b);
			out.write(b);
			out.close();
			inStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Delete the unusing old file from the sdcard.
	 * 
	 * @param uri
	 *            the old file.
	 */
	public static void deleteOldFile(Uri uri) {
		File file = new File(uri.getPath());
		if (file.exists()) {
			file.delete();
		}
	}

	/**
	 * Control downsampling and whether the image should be completely decoded,
	 * or just is size returned.
	 * 
	 * @param uri
	 *            the original image. URI is a parse from the path.
	 * @param desName
	 *            the destination file.
	 * @param sampleSize
	 */
	public static void bitmapOptionsCompressJPGPath(Uri uri, String desName,
			int sampleSize) {
		Bitmap bitmap = null;
		try {
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = false;
			options.inSampleSize = sampleSize;
			bitmap = BitmapFactory.decodeFile(uri.getPath(), options);
			File file = new File(SCHOOL_IMG_PATH, desName);
			FileOutputStream outStream = new FileOutputStream(file);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 80, outStream);
			outStream.flush();
			outStream.close();
		} catch (Exception e) {
			Log.e("Could not save", e.toString());
			e.printStackTrace();
		} finally {
			if (bitmap != null) {
				bitmap.recycle();
			}
		}
	}

	/*
	 * Save the signature as a png file.
	 */
	public static boolean compressionQuality(String desPath, int quality,
			Uri uri) {
		try {
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = false;
			Bitmap bitmap = BitmapFactory.decodeFile(uri.getPath(), options);
			File file = new File(SCHOOL_IMG_PATH, desPath);
			FileOutputStream outStream = new FileOutputStream(file);
			bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outStream);
			outStream.flush();
			outStream.close();
			if (bitmap != null) {
				bitmap.recycle();
			}
		} catch (Exception e) {
			Log.e("Could not save", e.toString());
			e.printStackTrace();
		}
		return true;
	}

	/**
	 * Control downsampling and whether the image should be completely decoded,
	 * or just is size returned.
	 * 
	 * @param uri
	 *            the original image. The uri come from the intent.
	 * @param desName
	 *            the destination file.
	 * @param sampleSize
	 */
	public static void bitmapOptionsCompressJPG(Uri uri, String desName,
			int sampleSize) {
		try {
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = false;
			options.inSampleSize = sampleSize;
			Bitmap bitmap = BitmapFactory.decodeStream(MyApplication
					.getInstance().getContentResolver().openInputStream(uri),
					null, options);
			File file = new File(SCHOOL_IMG_PATH, desName);
			FileOutputStream outStream = new FileOutputStream(file);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 80, outStream);
			outStream.flush();
			outStream.close();
			if (bitmap != null) {
				bitmap.recycle();
			}
		} catch (Exception e) {
			Log.e("Could not save", e.toString());
			e.printStackTrace();
		}
	}

	/*
	 * Save the signature as a png file.
	 */
	public static boolean save(String path, Bitmap bp) {
		try {
			FileOutputStream out = new FileOutputStream(path);
			bp.compress(Bitmap.CompressFormat.PNG, 80, out);
			out.close();
			if (bp != null) {
				bp.recycle();
			}
		} catch (Exception e) {
			Log.v("save", ".save.Exception", e);
			return false;
		}
		return true;
	}

	/**
	 * Compress the png image.
	 * 
	 * @param uri
	 *            the original image.
	 * @param desName
	 *            the destination file
	 * @param maxSize
	 *            the img max size.
	 */
	public static void imgScaledCompressionPNG(Uri uri, String desName,
			int maxSize) {
		try {
			int realSize = getImageSize(uri);
			if (realSize < maxSize) {
				return;
			}
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = false;
			Bitmap bitmap = BitmapFactory.decodeFile(uri.getPath(), options);
			int width = options.outWidth;
			int height = options.outHeight;
			Bitmap newBitmap = Bitmap.createScaledBitmap(bitmap,
					(int) (width * 0.8), (int) (height * 0.8), false);
			if (bitmap != null) {
				bitmap.recycle();
			}

			File newFile = new File(SCHOOL_IMG_PATH, desName);
			FileOutputStream outStream = new FileOutputStream(newFile);
			newBitmap.compress(Bitmap.CompressFormat.PNG, 80, outStream);
			outStream.flush();
			outStream.close();
			if (newBitmap != null) {
				newBitmap.recycle();
			}
			Uri newUri = Uri.fromFile(newFile);
			int size = getImageSize(newUri);
			if (size > maxSize) {
				imgScaledCompressionPNG(newUri, desName, maxSize);
			}
		} catch (FileNotFoundException e) {
			Log.v("compress", "The img comress error." + e);
		} catch (IOException e) {
			Log.v("compress", "The img comress error." + e);
		}
	}

	/**
	 * 读取图片
	 * 
	 * @param context
	 *            上下文对象
	 * @param resId
	 *            资源ID
	 * @return 图片对象
	 */
	public static Bitmap readBitmap(Context context, int resId) {
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Bitmap.Config.RGB_565;
		opt.inPurgeable = true;
		opt.inInputShareable = true;

		// 获取资源图片
		InputStream is = context.getResources().openRawResource(resId);
		return BitmapFactory.decodeStream(is, null, opt);
	}
	
	
	/**
     * 读取图片属性：旋转的角度
     * @param path 图片绝对路径
     * @return degree旋转的角度
     */
    public static int readPictureDegree(String path) {
        int degree  = 0;
        try {
                ExifInterface exifInterface = new ExifInterface(path);
                int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                        degree = 90;
                        break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                        degree = 180;
                        break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                        degree = 270;
                        break;
                }
        } catch (IOException e) {
                e.printStackTrace();
        }
        return degree;
    }
   /*
    * 旋转图片 
    * @param angle 
    * @param bitmap 
    * @return Bitmap 
    */ 
   public static Bitmap rotaingImageView(int angle , Bitmap bitmap) {  
       //旋转图片 动作   
       Matrix matrix = new Matrix();
       matrix.postRotate(angle);  
       System.out.println("angle2=" + angle);  
       // 创建新的图片   
       Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,  
               bitmap.getWidth(), bitmap.getHeight(), matrix, true);  
       return resizedBitmap;  
   }
}
