package com.m.h5hall.comm.widget;

import com.m.h5hall.R;

import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class MineDialog extends Dialog {
	public interface OnListItemClickListener {
		public void onItemClick(AdapterView<?> parent, View view,
				int position, long id);
	}
	
	private OnListItemClickListener mListItemClickListener;
	
	public interface OnNegtiveClickListener {
		public void onClick(View v);
	}
	
	private OnNegtiveClickListener mNegtiveListener;
	private ListView mLv;
	
	private boolean canBackPressed = true;
	
    public MineDialog(Context context) {
        super(context, R.style.MineDialog);
        setContentView(R.layout.mine_dialog);
        
        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.BOTTOM);
        dialogWindow.setBackgroundDrawableResource(R.drawable.mine_transparent);
        
        WindowManager m = dialogWindow.getWindowManager();
        Display d = m.getDefaultDisplay();
        lp.y = 0;
        lp.width = d.getWidth();
        dialogWindow.setAttributes(lp);
        
        TextView view = (TextView)findViewById(R.id.mine_dialog_negetive_btn);
        view.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				dismiss();
			}
        });
        
    	mLv = (ListView) findViewById(R.id.mine_dialog_listview);
    	
    }
    
    public MineDialog setAdapter(BaseAdapter adapter) {
    	mLv.setAdapter(adapter);
    	return this;
    }
 
    public void setListItemClickListener(OnListItemClickListener n) {
    	mListItemClickListener = n;

    	mLv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (mListItemClickListener != null) {
					mListItemClickListener.onItemClick(parent, view, position, id);
					dismiss();
				}
			}
		});
    	
    }
    
    public void setNegtiveClickListener(OnNegtiveClickListener n) {
    	mNegtiveListener = n;
    	
    	TextView v = (TextView)findViewById(R.id.mine_dialog_negetive_btn);
    	v.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (mNegtiveListener != null) {
					mNegtiveListener.onClick(arg0);
					dismiss();
				}
			}
    		
    	});
    }
    
    public void hideNagetive() {
    	TextView v = (TextView)findViewById(R.id.mine_dialog_negetive_btn);
    	v.setVisibility(View.GONE);
    	canBackPressed = false;
    }
    
    @Override
    public void onBackPressed() {  
    	if (canBackPressed) {
    		dismiss();  
    	}
    }
}
