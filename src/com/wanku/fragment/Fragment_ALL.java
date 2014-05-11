package com.wanku.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.ab.task.AbTaskItem;
import com.ab.task.AbTaskListener;
import com.ab.task.AbTaskQueue;
import com.ab.view.listener.AbOnListViewListener;
import com.ab.view.pullview.AbPullListView;
import com.wanku.FullQuery;
import com.wanku.R;
import com.wanku.tool.SimpleQueryJson;

public class Fragment_ALL extends Fragment {

	private Activity mActivity = null;
	private List<Map<String, Object>> list = null;
	private List<Map<String, Object>> newList = null;
	private AbPullListView mAbPullListView = null;
	private AbTaskQueue mAbTaskQueue = null;
	private ImageListAdapter myListViewAdapter = null;
	private int EndId = 0;
	private SQLiteDatabase db;
	private SharedPreferences sp;
	private Editor editor;
	private ArrayList<Integer> idlist = new ArrayList<Integer>();
	private boolean isPause = false;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mActivity = this.getActivity();

		db = this.getActivity().openOrCreateDatabase("listinfo",
				Context.MODE_APPEND, null);
		sp = this.getActivity().getSharedPreferences("DATA",
				Context.MODE_APPEND);
		editor = sp.edit();

		View view = inflater.inflate(R.layout.pull_list, null);

		mAbTaskQueue = AbTaskQueue.getInstance();
		// 获取ListView对象
		mAbPullListView = (AbPullListView) view.findViewById(R.id.mListView);
		// 设置进度条的样式
		mAbPullListView.getHeaderView().setHeaderProgressBarDrawable(
				this.getResources().getDrawable(R.drawable.progress_circular));
		mAbPullListView.getFooterView().setFooterProgressBarDrawable(
				this.getResources().getDrawable(R.drawable.progress_circular));
		// ListView数据
		list = new ArrayList<Map<String, Object>>();

		// 使用自定义的Adapter
		myListViewAdapter = new ImageListAdapter(mActivity, list,
				R.layout.list_items, new String[] { "itemsIcon", "itemsTitle",
						"itemsText" }, new int[] { R.id.itemsIcon,
						R.id.itemsTitle, R.id.itemsText });
		mAbPullListView.setAdapter(myListViewAdapter);
		// item被点击事件
		mAbPullListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				startActivity(new Intent(mActivity,FullQuery.class).putExtra("id",String.valueOf(idlist.get((int)id))));
			}
		});

		return view;
	}
	
	

	@Override
	public void onPause() {
		super.onPause();
		isPause = true;
	}



	@Override
	public void onResume() {
		super.onResume();
		isPause = false;
	}



	@Override
	public void onStart() {
		super.onStart();

		// 定义两种查询的事件
		final AbTaskItem item1 = new AbTaskItem();
		item1.listener = new AbTaskListener() {

			@Override
			public void update() {
				list.clear();
				if (newList != null && newList.size() > 0) {
					list.addAll(newList);
					myListViewAdapter.notifyDataSetChanged();
					newList.clear();
				}
				mAbPullListView.stopRefresh();
			}

			@Override
			public void get() {
				try {
					Thread.sleep(1000);
					// Json解析
					String urlStr = "http://wanku.sinaapp.com/json/simplequery.php";
					JSONArray array = new SimpleQueryJson().getSimpleJson(urlStr);
					// 向db里面加入数据
					for (int i = 0; i < array.length(); i++) {
						JSONObject obj = array.getJSONObject(i);
						if (i == 0) {
							int exsitId = sp.getInt("MaxId", 0);
							int newId = obj.getInt("id");
							if (newId > exsitId) {
								editor.putInt("MaxId", newId);
								editor.commit();
							} else {
								break;
							}

						}
						try {
							db.execSQL("Insert into main_info (id,name,cmt,img,type) values("
									+ obj.getInt("id")
									+ ",'"
									+ obj.getString("name")
									+ "','"
									+ obj.getString("cmt")
									+ "','"
									+ obj.getString("img")
									+ "',"
									+ obj.getInt("type_id") + ")");
						} catch (Exception e) {
						}
						EndId = obj.getInt("id");
					}
					Cursor cursor = db
							.rawQuery(
									"select id,name,cmt,img,type from main_info order by id desc limit 20",
									null);
					if (cursor.moveToFirst()) {
						newList = new ArrayList<Map<String, Object>>();
						Map<String, Object> map = null;
						idlist = new ArrayList<Integer>();
						do {
							map = new HashMap<String, Object>();
							map.put("itemsTitle", cursor.getString(cursor
									.getColumnIndex("name")));
							map.put("itemsText", cursor.getString(cursor
									.getColumnIndex("cmt")));
							map.put("itemsIcon", cursor.getString(cursor
									.getColumnIndex("img")));
							EndId = cursor.getInt(cursor.getColumnIndex("id"));
							idlist.add(EndId);
							newList.add(map);
						} while (!cursor.isLast() && cursor.moveToNext());
					}
				} catch (Exception e) {
				}
			};
		};

		final AbTaskItem item2 = new AbTaskItem();
		item2.listener = new AbTaskListener() {

			@Override
			public void update() {
				if (newList != null && newList.size() > 0) {
					list.addAll(newList);
					myListViewAdapter.notifyDataSetChanged();
					newList.clear();
				}
				mAbPullListView.stopLoadMore();
			}

			// 上拉
			@Override
			public void get() {
				try {
					Thread.sleep(1000);
					newList = new ArrayList<Map<String, Object>>();
					Map<String, Object> map = null;

					int MinId = sp.getInt("MaxId", 0);
					Cursor cursor = db.rawQuery(
							"select id from main_info where id = "
									+ (EndId - 20), null);
					if (cursor.moveToFirst()) {
						MinId = cursor.getInt(cursor.getColumnIndex("id"));
					}
					if (EndId > MinId) {
						cursor = db.rawQuery(
								"select id,name,cmt,img,type from main_info where id < "
										+ EndId + " order by id desc limit 20",
								null);
						if (cursor.moveToFirst()) {
							while (!cursor.isLast()) {
								map = new HashMap<String, Object>();
								map.put("itemsTitle", cursor.getString(cursor
										.getColumnIndex("name")));
								map.put("itemsText", cursor.getString(cursor
										.getColumnIndex("cmt")));
								map.put("itemsIcon", cursor.getString(cursor
										.getColumnIndex("img")));
								EndId = cursor.getInt(cursor
										.getColumnIndex("id"));
								idlist.add(EndId);
								newList.add(map);
								cursor.moveToNext();
							}
						}
					} else {
						String urlStr = "http://wanku.sinaapp.com/json/simplequery.php";
						String params = "?" + "id=" + EndId;
						JSONArray array = new SimpleQueryJson().getSimpleJson(
								urlStr, params);

						for (int i = 0; i < array.length(); i++) {
							map = new HashMap<String, Object>();
							JSONObject obj = array.getJSONObject(i);
							map.put("itemsIcon", obj.getString("img"));
							map.put("itemsTitle", obj.getString("name"));
							map.put("itemsText", obj.getString("cmt"));
							EndId = obj.getInt("id");
							idlist.add(EndId);
							newList.add(map);
							try {
								db.execSQL("Insert into main_info (id,name,cmt,img,type) values("
										+ obj.getInt("id")
										+ ",'"
										+ obj.getString("name")
										+ "','"
										+ obj.getString("cmt")
										+ "','"
										+ obj.getString("img")
										+ "',"
										+ obj.getInt("type_id") + ")");
							} catch (Exception e) {
							}
						}
					}
				} catch (Exception e) {
					newList.clear();
				}
			};
		};

		mAbPullListView.setAbOnListViewListener(new AbOnListViewListener() {

			@Override
			public void onRefresh() {
				mAbTaskQueue.execute(item1);
			}

			@Override
			public void onLoadMore() {
				mAbTaskQueue.execute(item2);
			}

		});
		if(!isPause){
		// 默认数据加载
		newList = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = null;
		Cursor cursor = db
				.rawQuery(
						"select id,name,cmt,img,type from main_info order by id desc limit 20",
						null);
		if (cursor.moveToFirst()) {
			do {
				map = new HashMap<String, Object>();
				map.put("itemsTitle",
						cursor.getString(cursor.getColumnIndex("name")));
				map.put("itemsText",
						cursor.getString(cursor.getColumnIndex("cmt")));
				map.put("itemsIcon",
						cursor.getString(cursor.getColumnIndex("img")));
				EndId = cursor.getInt(cursor.getColumnIndex("id"));
				idlist.add(EndId);
				newList.add(map);
			} while (!cursor.isLast() && cursor.moveToNext());
		}

		// 第一次下载数据
		mAbTaskQueue.execute(item1);
		}
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}
}
