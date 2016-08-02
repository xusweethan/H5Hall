package com.m.h5hall.main.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class ShareActivity extends Activity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		Intent intent = getIntent();  
	    String action = intent.getAction();  
	    String type = intent.getType();  
	    
	    if (Intent.ACTION_SEND.equals(action) && type != null) {  
            if ("text/plain".equals(type)) {  
                handleSendText(intent); // Handle text being sent  
            }
	}

}

	private void handleSendText(Intent intent) {
		 String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);  
	     String sharedTitle = intent.getStringExtra(Intent.EXTRA_TITLE);  
	     if (sharedText != null) {  
	            // Update UI to reflect text being shared  
	     }  
	}
}