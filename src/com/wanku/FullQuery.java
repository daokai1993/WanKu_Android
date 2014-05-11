package com.wanku;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.ab.activity.AbActivity;
import com.ab.http.AbHttpUtil;
import com.ab.http.AbStringHttpResponseListener;
import com.ab.view.titlebar.AbTitleBar;
import com.wanku.HorizontalListView.HorizontalListView;
import com.wanku.HorizontalListView.HorizontalListViewAdapter;

public class FullQuery extends AbActivity {

	private TextView fullquery_type;
	private TextView fullquery_size;
	private TextView fullquery_osreq;
	private TextView fullquery_ad;
	private TextView fullquery_version;
	private TextView fullquery_cost;
	private TextView fullquery_cmt;
	
	
	private Map<String, String> fullquery_osreq_map = new HashMap<String, String>();
	private Map<String, String> fullquery_cost_map = new HashMap<String, String>();
	private Map<String, String> fullquery_type_map = new HashMap<String, String>();

	private HorizontalListViewAdapter hlva;
	private HorizontalListView hlv;
	private JSONObject jobj;
	private ArrayList<String> imageurls = new ArrayList<String>();
	private AbTitleBar mAbTitleBar;
	private String gamename;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setAbContentView(R.layout.fullquery);
		
		
		// 获取id
		Intent intent = getIntent();
		String id = intent.getStringExtra("id");
		//下载按钮
		mAbBottomBar.setVisibility(View.VISIBLE);
		View view = mInflater.inflate(R.layout.bottom_bar, null);
		Button downloadBtn = (Button) view.findViewById(R.id.bottom_download);
		downloadBtn.setOnClickListener(new android.view.View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setAction("android.intent.action.VIEW");
				Uri content_url = Uri
						.parse("http://www.baidu.com/baidu?ie=utf-8&word="
								+ gamename);
				intent.setData(content_url);
				startActivity(intent);

			}
		});
		mAbBottomBar.setBottomView(view);
		
		// 存入数据map
		fullquery_osreq_map.put("1", "Android1.6及以上");
		fullquery_osreq_map.put("2", "Android2.1及以上");
		fullquery_osreq_map.put("3", "Android2.2及以上");
		fullquery_osreq_map.put("4", "Android2.3及以上");
		fullquery_osreq_map.put("5", "Android4.0及以上");

		fullquery_cost_map.put("0", "无收费");
		fullquery_cost_map.put("1", "广告收费");
		fullquery_cost_map.put("2", "内购");
		fullquery_cost_map.put("3", "道具收费");
		fullquery_cost_map.put("4", "关卡收费");
		fullquery_cost_map.put("5", "点卡收费");
		fullquery_cost_map.put("6", "商城收费");
		fullquery_cost_map.put("7", "强化收费");
		fullquery_cost_map.put("99", "其他收费");

		fullquery_type_map.put("1", "动作");
		fullquery_type_map.put("2", "休闲");
		fullquery_type_map.put("3", "益智");
		fullquery_type_map.put("4", "角色");
		fullquery_type_map.put("5", "策略");
		fullquery_type_map.put("6", "体育");
		fullquery_type_map.put("7", "竞速");
		fullquery_type_map.put("8", "射击");
		fullquery_type_map.put("9", "塔防");
		fullquery_type_map.put("10", "卡牌");
		fullquery_type_map.put("11", "经营");
		fullquery_type_map.put("12", "养成");
		fullquery_type_map.put("99", "其他");

		// View 获取id
		fullquery_type = (TextView) this.findViewById(R.id.fullquery_type);
		fullquery_size = (TextView) this.findViewById(R.id.fullquery_size);
		fullquery_osreq = (TextView) this.findViewById(R.id.fullquery_osreq);
		fullquery_ad = (TextView) this.findViewById(R.id.fullquery_ad);
		fullquery_version = (TextView) this
				.findViewById(R.id.fullquery_version);
		fullquery_cost = (TextView) this.findViewById(R.id.fullquery_cost);
		fullquery_cmt = (TextView) this.findViewById(R.id.fullquery_cmt);
		
		// 标题
		mAbTitleBar = this.getTitleBar();
		mAbTitleBar.setTitleText(R.string.app_name);
		mAbTitleBar.setLogo(R.drawable.back_n);
		mAbTitleBar.setTitleBarBackground(R.drawable.top_bg2);
		mAbTitleBar.setTitleTextMargin(10, 0, 0, 0);
		mAbTitleBar.setLogoLine(R.drawable.line);

		// 横向listView
		hlv = (HorizontalListView) findViewById(R.id.horizontallistview);
		hlva = new HorizontalListViewAdapter(this, imageurls);
		hlva.notifyDataSetChanged();
		hlv.setAdapter(hlva);

		hlv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int id,
					long arg3) {
				startActivity(new Intent(FullQuery.this,FullScreen.class).putExtra("imgurl",imageurls.get((int)arg3)));
				
			}
		});
		
		

		// 一个菜谱的url地址
		String urlString = "http://wanku.sinaapp.com/json/fullquery.php?id="
				+ id;

		AbHttpUtil mAbHttpUtil = AbHttpUtil.getInstance(this);
		mAbHttpUtil.get(urlString, new AbStringHttpResponseListener() {

			// 获取数据成功会调用这里
			@Override
			public void onSuccess(int statusCode, String content) {
				try {
					imageurls.clear();
					jobj = new JSONObject(content);
					imageurls.add(jobj.getString("img"));
					imageurls.add(jobj.getString("img1"));
					imageurls.add(jobj.getString("img2"));
					gamename = jobj.getString("name");
					mAbTitleBar.setTitleText(gamename);
					fullquery_type.setText(fullquery_type_map.get(jobj
							.getString("type_id")));
					fullquery_cmt.setText(jobj.getString("cmt"));
					fullquery_size.setText(jobj.getString("size"));
					fullquery_osreq.setText(fullquery_osreq_map.get(jobj
							.getString("osreq_id")));
					if (jobj.getString("ad").equals("0")) {
						fullquery_ad.setText("无广告");
					} else {
						fullquery_ad.setText("无广告");
					}

					fullquery_version.setText(jobj.getString("version"));
					fullquery_cost.setText(fullquery_cost_map.get(jobj
							.getString("cost_id")));
				} catch (Exception e) {

				}
				hlva.notifyDataSetChanged();

				/*
				 * showDialog("返回结果", content, new OnClickListener() {
				 * 
				 * @Override public void onClick(DialogInterface arg0, int arg1)
				 * { } });
				 */
			}

			// 失败，调用
			@Override
			public void onFailure(int statusCode, String content,
					Throwable error) {
				showToast(error.getMessage());
			}

			// 开始执行前
			@Override
			public void onStart() {
				// 显示进度框
				showProgressDialog();
			}

			// 完成后调用，失败，成功
			@Override
			public void onFinish() {
				// 移除进度框
				removeProgressDialog();
			};
		});

	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

}
