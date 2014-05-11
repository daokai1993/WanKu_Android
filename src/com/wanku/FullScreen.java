package com.wanku;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.ab.bitmap.AbImageDownloader;
import com.ab.global.AbConstant;

public class FullScreen extends Activity {
	private AbImageDownloader mAbImageDownloader = null;
	private ImageView fullscreenimageView;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final View view = View.inflate(this, R.layout.fullscreen, null);
		setContentView(view);
		fullscreenimageView = (ImageView) view.findViewById(R.id.fullScreen);
		Intent intent = this.getIntent();
		String imageurl = intent.getStringExtra("imgurl");
		// 图片下载器
		mAbImageDownloader = new AbImageDownloader(this);
		mAbImageDownloader.setType(AbConstant.ORIGINALIMG);
		mAbImageDownloader.setLoadingImage(R.drawable.image_loading);
		mAbImageDownloader.setErrorImage(R.drawable.image_error);
		mAbImageDownloader.setNoImage(R.drawable.image_no);
		mAbImageDownloader.display(fullscreenimageView, imageurl);
		fullscreenimageView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

	}

}