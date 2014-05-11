package com.wanku.HorizontalListView;

import java.util.ArrayList;

import com.ab.bitmap.AbImageDownloader;
import com.ab.global.AbConstant;
import com.wanku.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class HorizontalListViewAdapter extends BaseAdapter {

	private ArrayList<String> imageUrl;
	private AbImageDownloader mAbImageDownloader = null;

	public HorizontalListViewAdapter(Context con) {
		mInflater = LayoutInflater.from(con);
	}

	public HorizontalListViewAdapter(Context con, ArrayList<String> imageUrl) {
		mInflater = LayoutInflater.from(con);
		this.imageUrl = imageUrl;
		mAbImageDownloader = new AbImageDownloader(con);
		mAbImageDownloader.setWidth(240);
		mAbImageDownloader.setHeight(400);
		mAbImageDownloader.setType(AbConstant.SCALEIMG);
		mAbImageDownloader.setLoadingImage(R.drawable.image_loading);
		mAbImageDownloader.setErrorImage(R.drawable.image_error);
		mAbImageDownloader.setNoImage(R.drawable.image_no);
	}

	@Override
	public int getCount() {
		if(imageUrl==null)
		{return 0;}
		return imageUrl.size();
	}

	private LayoutInflater mInflater;

	@Override
	public Object getItem(int position) {
		return position;
	}

	private ViewHolder vh = new ViewHolder();

	private static class ViewHolder {
		private ImageView im;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.horizontallistview_item,
					null);
			vh.im = (ImageView) convertView.findViewById(R.id.iv_pic);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}
		// 设置加载中的View
		mAbImageDownloader.setLoadingView(convertView
				.findViewById(R.id.progressBar));
		// 图片的下载
		mAbImageDownloader.display(vh.im, imageUrl.get(position));
		return convertView;
	}
}