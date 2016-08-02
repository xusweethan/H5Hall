package com.m.h5hall.main.activity;

import java.util.Hashtable;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import android.widget.Toast;

import com.m.h5hall.MyApplication;
import com.m.h5hall.MyPreferences;
import com.m.h5hall.R;
import com.m.h5hall.all.FragmentPageAll;
import com.m.h5hall.me.FragmentPageMe;
import com.m.h5hall.recommend.FragmentPageRecommend;
import com.m.ms.api.pay.GPay;
import com.m.ms.api.pay.GPayAPI;
import com.umeng.analytics.MobclickAgent;

public class MainActivity extends FragmentActivity {
	private FragmentTabHost mTabHost; 
	private SharedPreferences mPrefs;
	
	/** 当前订阅状态 */
	private int gPaySate = -1;
	
	/** 当前标签页序号 */
	private int mPrevTab = -1;
	
	/** 再点一次退出的计时器 */
	private long exitTime = 0;
	
	private Class mFragmentArray[] = {
			FragmentPageRecommend.class,
			FragmentPageAll.class,
			FragmentPageMe.class
	};
	
	private Hashtable<Integer, Fragment> mFragements = new Hashtable<Integer, Fragment>();
    
	/** 底部菜单栏图标集合 */
    private int mTabItemImageArray[] = {
    		R.drawable.tab_item_recommend,
    		R.drawable.tab_item_all,
    		R.drawable.tab_item_me
    };  
    /** 底部菜单栏名称集合 */
    private String mTabItemTextArray[] = {"Recommend", "All", "Local"};
    
    private Handler mHandler = null;
//    private Class<BaseFrame> mFrameClass = null;
    
    
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//buy();
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		MyPreferences myPreferences = MyApplication.getInstance().getMyPrefs();
		String loginName = myPreferences.getLoginName();
		String loginPwd = myPreferences.getLoginPwd();
//		if (!TextUtils.isEmpty(loginName) && !TextUtils.isEmpty(loginPwd)) {
//			System.out.println("自动登录");
//			MyApplication.getInstance().getUserData().login(MainActivity.this, loginName, loginPwd, false);
//		}
		//MyApplication.getInstance().getMyDbHelper();
		
		mFragmentArray[1] = FragmentPageAll.class;
		
		mPrefs = getPreferences(0);
		mPrefs.edit().putBoolean("mainLaunch", true).commit();
		
		setContentView(R.layout.activity_main);
        initView();
        
        mHandler = new Handler() {
        	public void handleMessage (Message msg) {
        		switch(msg.what) {
        		
        		}	
        	}
        };
	}
	
	public Handler getHandler() {
		return mHandler;
	}
	
	
	private void initView(){  
        //实例化布局对象  
		LayoutInflater layoutInflater = LayoutInflater.from(this);  
                      
        //实例化TabHost对象，得到TabHost  
        mTabHost = (FragmentTabHost)findViewById(android.R.id.tabhost);  
        mTabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);  
        mTabHost.setOnTabChangedListener(new OnTabChangeListener() {

			@Override
			public void onTabChanged(String tabId) 
			{
				mPrevTab = mTabHost.getCurrentTab();
//				if (mPrevTab != -1) 
//				{
//					mTextTitle.setText(mTitleTextArray[mPrevTab]); 
//				} 
//				else 
//				{	
//					mTextTitle.setText(mTitleTextArray[0]);
//				}
			}
        	
        });
        
        //得到fragment的个数  
        int count = mFragmentArray.length;     
                      
        for(int i = 0; i < count; i++){    
            //为每一个Tab按钮设置图标、文字和内容  
            TabSpec tabSpec = mTabHost.newTabSpec(mTabItemTextArray[i]).setIndicator(getTabItemView(i));  
            //将Tab按钮添加进Tab选项卡中  
            mTabHost.addTab(tabSpec, mFragmentArray[i], null);
        }  
    }  
	
	private View getTabItemView(int index){
		LayoutInflater layoutInflater = LayoutInflater.from(this);
        View view = layoutInflater.inflate(R.layout.tab_item, null);  
          
        ImageView imageView = (ImageView) view.findViewById(R.id.imageview);  
        imageView.setImageResource(mTabItemImageArray[index]);
              
        TextView textView = (TextView) view.findViewById(R.id.textview);          
        textView.setText(mTabItemTextArray[index]);  
	    
	    return view;  
	}  
	
	@Override 
	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}
	
	@Override 
	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
		
	}
	
	
	@Override
	protected void onDestroy() {
		MyApplication.getInstance().getUserData().setLoginInfo(null);
		super.onDestroy();
	}
	
	@Override
	public void onBackPressed() {
		
		if ((java.lang.System.currentTimeMillis() - exitTime) > 2000) {
			Toast.makeText(getApplicationContext(), "Press again to exit the program",
					Toast.LENGTH_SHORT).show();
			exitTime = java.lang.System.currentTimeMillis();
		} else {
			super.onBackPressed();
			MyApplication.getInstance().finish();
		}
//		MyFragment fragment = mFragements.get(mTabHost.getCurrentTab());
//		fragment.onBackPressed();
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		Log.v("mainactivity", "onActivityResult requestCode = " + requestCode
				+ "; resultCode =  " + resultCode);
	}
	
	public void setTabFragment(int tabIndex, Fragment fragment) {
		mFragements.put(tabIndex, fragment);
	}
	
	public Fragment getTabFragment(int tabIndex) {
		return mFragements.get(tabIndex);
	}

	public void setCurrentTab(int tabIndex) {
		mTabHost.setCurrentTab(tabIndex);
	}
	
//	public <T> void setTargetFrame(Class<T> _class) {
//		mFrameClass = (Class<BaseFrame>) _class;
//	}

	public FragmentTabHost getTabHost() {
		return mTabHost;
	}
	
	public View getMainView()
	{
		return this.findViewById(android.R.id.tabhost);
	}
//	public Class<BaseFrame> getTargetFrame() {
//		return mFrameClass;
//	}
	
//	public void setTextTitle(String title) {
//		mTextTitle.setText(title);
//	}
//	
//	public void showTopLeftBtn(OnClickListener listener) {
//		mBtnTopLeft.setVisibility(View.VISIBLE);
//		mBtnTopLeft.setOnClickListener(listener);
//	}
//	
//	public void showTopRightBtn(int resId, OnClickListener listener) {
//		mBtnTopRightImg.setVisibility(View.VISIBLE);
//		mBtnTopRightImg.setImageResource(resId);
//		mBtnTopRightImg.setOnClickListener(listener);
//	}
//	
//	public void hideTopLeftBtn() {
//		mBtnTopLeft.setVisibility(View.INVISIBLE);
//	}
//	
//	public void hideTopRightBtn() {
//		mBtnTopRightImg.setVisibility(View.INVISIBLE);
//	}
	
//	private int getSubscriptionState() {
//		final SharedPreferences preferences = getSharedPreferences("buy",
//				Context.MODE_PRIVATE);
//		boolean state = preferences.getBoolean("state", false);
//
//		if (state) {
//			GPay.getAPI().pay(MainActivity.this, "0B0D10AD",
//					new GPayAPI.GPayCallback() {
//
//						@Override
//						public void onResult(int code, String msg) {
//							if (code == GPayAPI.SUCCESS) {
//								Toast.makeText(getBaseContext(), "success",
//										Toast.LENGTH_SHORT).show();
//								gPaySate = 1;
//							} else if (code == 2001) {
//								gPaySate = 2;
//							} else {
//								gPaySate = 0;
//								Editor editor = preferences.edit();
//								editor.putBoolean("state", false);
//								editor.commit();
//								Toast.makeText(getBaseContext(),
//										"failed" + code + msg,
//										Toast.LENGTH_SHORT).show();
//								// 请在此处添加信息用以提示用户计费失败，失败信息请从msg中获取
//							}
//						}
//					});
//
//		}
//		return gPaySate;
//	}
	
	private void buy() {
		MobclickAgent.onEvent(MainActivity.this,"Pay","start");
		GPay.getAPI().pay(MainActivity.this, "949879DD", new GPayAPI.GPayCallback() {
			
            @Override
            public void onResult(int code, String msg)
            {
            	
                if(code == GPayAPI.SUCCESS)
                {
                	Toast.makeText(getBaseContext(), "success",Toast.LENGTH_SHORT).show();
                	MobclickAgent.onEvent(MainActivity.this,"Pay","success");
                    //Please enter your code here
                }else if (code == -2001) {
                	buy();
				}else {
					MobclickAgent.onEvent(MainActivity.this,"Pay","failed"+code);
					Toast.makeText(getBaseContext(), "failed"+code+msg,Toast.LENGTH_SHORT).show();
					MainActivity.this.finish();
                    //请在此处添加信息用以提示用户计费失败，失败信息请从msg中获取
                }
            }
        });
	}

}
