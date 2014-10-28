package com.klisly.omga.utils;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.klisly.omga.MyApplication;
import com.klisly.omga.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

public class ImageUtils {
	public static void loadImage(String image,final ImageView imageView) {
		ImageLoader.getInstance()
		.displayImage(image==null?"":image, imageView, 
				MyApplication.getInstance().getOptions(R.drawable.bg_pic_loading),
				new SimpleImageLoadingListener(){

					@Override
					public void onLoadingComplete(String imageUri, View view,
							Bitmap loadedImage) {
						super.onLoadingComplete(imageUri, view, loadedImage);
						 float[] cons=ActivityUtil.getBitmapConfiguration(loadedImage, imageView, 1.0f);
                         RelativeLayout.LayoutParams layoutParams=
                             new RelativeLayout.LayoutParams((int)cons[0], (int)cons[1]);
                         layoutParams.addRule(RelativeLayout.BELOW,R.id.content_text);
                         imageView.setLayoutParams(layoutParams);
					}
			
		});
		
	}
}
