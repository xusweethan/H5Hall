package com.m.h5hall;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Properties;
import java.util.TreeSet;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.text.format.Time;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;


public class CrashHandler implements UncaughtExceptionHandler {
	 /** Debug Log tag*/
    public static final String TAG = "CrashHandler";
    /** 是否开启日志输出,在Debug状态下开启,
    * 在Release状态下关闭以提示程序性能
    * */
    public static final boolean DEBUG = true;
    /** 系统默认的UncaughtException处理类 */
    private Thread.UncaughtExceptionHandler mDefaultHandler;
    /** CrashHandler实例 */
    private static CrashHandler INSTANCE;
    /** 程序的Context对象 */
    private Context mContext;
    /** 使用Properties来保存设备的信息和错误堆栈信息*/
    private Properties mDeviceCrashInfo = new Properties();
    private static final String VERSION_NAME = "versionName";
    private static final String VERSION_CODE = "versionCode";
    private static final String STACK_TRACE = "STACK_TRACE";
    /** 错误报告文件的扩展名 */
    private static final String CRASH_REPORTER_EXTENSION = ".txt";
    /** 保证只有一个CrashHandler实例 */
    private CrashHandler() {}
    /** 获取CrashHandler实例 ,单例模式*/
    public static CrashHandler getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CrashHandler();
        }
        return INSTANCE;
    }
    
    /**
     * 初始化,注册Context对象,
     * 获取系统默认的UncaughtException处理器,
     * 设置该CrashHandler为程序的默认处理器
     * @param ctx
     */
     public void init(Context ctx) {
         mContext = ctx;
         mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
         Thread.setDefaultUncaughtExceptionHandler(this);
     }
     /**
     * 当UncaughtException发生时会转入该函数来处理
     */
     @Override
     public void uncaughtException(Thread thread, Throwable ex) {
         if (!handleException(ex) && mDefaultHandler != null) {
             //如果用户没有处理则让系统默认的异常处理器来处理
             mDefaultHandler.uncaughtException(thread, ex);
         } else {
             //Sleep一会后结束程序
             try {
                 Thread.sleep(5000);
             } catch (InterruptedException e) {
                 Log.e(TAG, "Error : ", e);
             }
             android.os.Process.killProcess(android.os.Process.myPid());
             System.exit(10);
         }
     }
     
     /**
      * 自定义错误处理,收集错误信息
      * 发送错误报告等操作均在此完成.
      * 开发者可以根据自己的情况来自定义异常处理逻辑
      * @param ex
      * @return true:如果处理了该异常信息;否则返回false
      */
      private boolean handleException(Throwable ex) {
          if (ex == null) {
              Log.w(TAG, "handleException — ex==null");
              return true;
          }
          final String msg = ex.getLocalizedMessage();
          if(msg == null) {
              return false;
          }
          //使用Toast来显示异常信息
          new Thread() {
              @Override
              public void run() {
                  Looper.prepare();
                  Toast toast = Toast.makeText(mContext, "程序出错，即将退出!\r\n" + msg,
                          Toast.LENGTH_LONG);
                  toast.setGravity(Gravity.CENTER, 0, 0);
                  toast.show();
//                MsgPrompt.showMsg(mContext, “程序出错啦”, msg+”\n点确认退出”);
                  Looper.loop();
              }
          }.start();
          //收集设备信息
          collectCrashDeviceInfo(mContext);
          //保存错误报告文件
          saveCrashInfoToFile(ex);
          //发送错误报告到服务器
          //sendCrashReportsToServer(mContext);
          return true;
      }
      /**
       * 
      * 在程序启动时候, 可以调用该函数来发送以前没有发送的报告
      */
      public void sendPreviousReportsToServer() {
          sendCrashReportsToServer(mContext);
      }
      
      /**
      * 把错误报告发送给服务器,包含新产生的和以前没发送的.
      * @param ctx
      */
      private void sendCrashReportsToServer(Context ctx) {
          String[] crFiles = getCrashReportFiles(ctx);
          if (crFiles != null && crFiles.length > 0) {
          TreeSet<String> sortedFiles = new TreeSet<String>();
          sortedFiles.addAll(Arrays.asList(crFiles));
          for (String fileName : sortedFiles) {
          File cr = new File(ctx.getFilesDir(), fileName);
          postReport(cr);
          cr.delete();// 删除已发送的报告
          }
          }
      }
      private void postReport(File file) {
          // TODO 发送错误报告到服务器
      }
      
      /**
      * 获取错误报告文件名
      * @param ctx
      * @return
      */
      private String[] getCrashReportFiles(Context ctx) {
          File filesDir = ctx.getFilesDir();
          FilenameFilter filter = new FilenameFilter() {
              public boolean accept(File dir, String name) {
                  return name.endsWith(CRASH_REPORTER_EXTENSION);
              }
          };
          return filesDir.list(filter);
      }

      /**
      * 保存错误信息到文件中
      * @param ex
      * @return
      */
      private String saveCrashInfoToFile(Throwable ex) {
          Writer info = new StringWriter();
          PrintWriter printWriter = new PrintWriter(info);
          ex.printStackTrace(printWriter);
          Throwable cause = ex.getCause();
          while (cause != null) {
              cause.printStackTrace(printWriter);
              cause = cause.getCause();
          }
          String result = info.toString();
          printWriter.close();
          mDeviceCrashInfo.put("EXEPTION", ex.getLocalizedMessage());
          mDeviceCrashInfo.put(STACK_TRACE, result);
          try {
              Date d = new Date();
              SimpleDateFormat SDTF = new SimpleDateFormat("yyyyMMdd-HHmm");
              String datetime = SDTF.format(d);
              String fileName = "crash-" + datetime + CRASH_REPORTER_EXTENSION;
              FileOutputStream trace = null;
              
              if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
                  File sdCardDir = Environment.getExternalStorageDirectory();//获取SDCard目录
                  File saveFile = new File(sdCardDir, fileName);
                  trace = new FileOutputStream(saveFile);
                  mDeviceCrashInfo.store(trace, "");
                  trace.flush();
                  trace.close();
              } else {
            	  trace = mContext.openFileOutput(fileName,
            			  		Context.MODE_PRIVATE);
                  mDeviceCrashInfo.store(trace, "");
                  trace.flush();
                  trace.close();
              }
              
              return fileName;
          } catch (Exception e) {
              Log.e(TAG, "an error occured while writing report file…", e);
          }
          return null;
          
      }
      
      /**
      * 收集程序崩溃的设备信息
      *
      * @param ctx
      */
      public void collectCrashDeviceInfo(Context ctx) {
          try {
              PackageManager pm = ctx.getPackageManager();
              PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(),
                      PackageManager.GET_ACTIVITIES);
              if (pi != null) {
                  mDeviceCrashInfo.put(VERSION_NAME,
                          pi.versionName == null ? "not set" : pi.versionName);
                  mDeviceCrashInfo.put(VERSION_CODE, ""+pi.versionCode);
              }
          } catch (NameNotFoundException e) {
              Log.e(TAG, "Error while collect package info", e);
          }
          //使用反射来收集设备信息.在Build类中包含各种设备信息,
          //例如: 系统版本号,设备生产商 等帮助调试程序的有用信息
          //具体信息请参考后面的截图
          Field[] fields = Build.class.getDeclaredFields();
          for (Field field : fields) {
              try {
                  field.setAccessible(true);
                  mDeviceCrashInfo.put(field.getName(), ""+field.get(null));
                  if (DEBUG) {
                      Log.d(TAG, field.getName() + " : " + field.get(null));
                  }
              } catch (Exception e) {
                  Log.e(TAG, "Error while collect crash info", e);
              }
          }
      }
  }
