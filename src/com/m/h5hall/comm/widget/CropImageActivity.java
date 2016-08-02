package com.m.h5hall.comm.widget;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import com.edmodo.cropper.CropImageView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.m.h5hall.R;

import com.m.h5hall.utils.BitmapUtils;
import com.m.h5hall.utils.Const;
import com.m.h5hall.utils.FileStorageUtils;

@ContentView(R.layout.mine_crop_image_activity)
public class CropImageActivity extends Activity{
	
	public static final String EXTRA_ACTIVITY = "CropImageActivity.EXTRA_ACTIVITY";

	// Static final constants
    private static final int DEFAULT_ASPECT_RATIO_VALUES = 10;
    private static final int ROTATE_NINETY_DEGREES = 90;
    private static final String ASPECT_RATIO_X = "ASPECT_RATIO_X";
    private static final String ASPECT_RATIO_Y = "ASPECT_RATIO_Y";
    private static final int ON_TOUCH = 1;

    // Instance variables
    private int mAspectRatioX = DEFAULT_ASPECT_RATIO_VALUES;
    private int mAspectRatioY = DEFAULT_ASPECT_RATIO_VALUES;

    private Bitmap croppedImage;
    private int tag;

    // Saves the state upon rotating the screen/restarting the activity
    @Override
    protected void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putInt(ASPECT_RATIO_X, mAspectRatioX);
        bundle.putInt(ASPECT_RATIO_Y, mAspectRatioY);
    }

    // Restores the state upon rotating the screen/restarting the activity
    @Override
    protected void onRestoreInstanceState(Bundle bundle) {
        super.onRestoreInstanceState(bundle);
        mAspectRatioX = bundle.getInt(ASPECT_RATIO_X);
        mAspectRatioY = bundle.getInt(ASPECT_RATIO_Y);
    }
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		ViewUtils.inject(this);

		Intent data = getIntent();
		if ("ParentInfoActivity".equals(data.getStringExtra(EXTRA_ACTIVITY))) {
			tag = 1;
		} else if ("TeacherInfoActivity".equals(data.getStringExtra(EXTRA_ACTIVITY))) {
			tag = 2;
		} else if ("BabyInfoActivity".equals(data.getStringExtra(EXTRA_ACTIVITY))) {
			tag = 3;
		}
		
        // Initialize components of the app
        final CropImageView cropImageView = (CropImageView) findViewById(R.id.CropImageView);
        Uri uri = data.getData();
        String path = uri.getPath();
        int degree = BitmapUtils.readPictureDegree(path);
        Log.e("degree", degree+"");
        Bitmap bitmap = BitmapUtils.getimage(CropImageActivity.this, path);
        Bitmap newBitmap = BitmapUtils.rotaingImageView(degree, bitmap);
        cropImageView.setImageBitmap(newBitmap);
        
        // Set initial spinner value
//        cropImageView.setGuidelines(CropImageView.);
        
        cropImageView.setFixedAspectRatio(true);
        
        //Sets the rotate button
        final Button rotateButton = (Button) findViewById(R.id.Button_rotate);
        rotateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cropImageView.rotateImage(ROTATE_NINETY_DEGREES);
            }
        });
        
        // Sets initial aspect ratio to 10/10, for demonstration purposes
        cropImageView.setAspectRatio(DEFAULT_ASPECT_RATIO_VALUES, DEFAULT_ASPECT_RATIO_VALUES);

        final Button cropButton = (Button) findViewById(R.id.Button_crop);
        cropButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                croppedImage = cropImageView.getCroppedImage();
//                ImageView croppedImageView = (ImageView) findViewById(R.id.croppedImageView);
//                croppedImageView.setImageBitmap(croppedImage);
                if (tag == 1) {
//                	FileStorageUtils.saveImage(croppedImage, ParentInfoActivity.mPersonDir, ParentInfoActivity.TAG_TAKE_PHOTO+".jpg");
                }
                CropImageActivity.this.setResult(Activity.RESULT_OK);
                CropImageActivity.this.finish();
            }
        });
        
        final Button cancelButton = (Button) findViewById(R.id.Button_cancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				CropImageActivity.this.finish();
			}
		});

    }

}
