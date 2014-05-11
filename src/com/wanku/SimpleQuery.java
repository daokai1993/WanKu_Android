package com.wanku;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.ab.activity.AbActivity;
import com.ab.http.AbHttpUtil;
import com.ab.http.AbRequestParams;
import com.ab.http.AbStringHttpResponseListener;
import com.ab.view.sliding.AbSlidingTabView;
import com.ab.view.titlebar.AbTitleBar;
import com.wanku.fragment.Fragment_1;
import com.wanku.fragment.Fragment_10;
import com.wanku.fragment.Fragment_11;
import com.wanku.fragment.Fragment_12;
import com.wanku.fragment.Fragment_2;
import com.wanku.fragment.Fragment_3;
import com.wanku.fragment.Fragment_4;
import com.wanku.fragment.Fragment_5;
import com.wanku.fragment.Fragment_6;
import com.wanku.fragment.Fragment_7;
import com.wanku.fragment.Fragment_8;
import com.wanku.fragment.Fragment_9;
import com.wanku.fragment.Fragment_99;
import com.wanku.fragment.Fragment_ALL;

public class SimpleQuery extends AbActivity {

	private AbSlidingTabView mAbSlidingTabView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setAbContentView(R.layout.sliding_tab);

		AbTitleBar mAbTitleBar = this.getTitleBar();
		mAbTitleBar.setTitleText(R.string.app_name);
		mAbTitleBar.setLogo(R.drawable.ic_launcher);
		mAbTitleBar.setTitleBarBackground(R.drawable.top_bg2);
		mAbTitleBar.setTitleTextMargin(10, 0, 0, 0);
		mAbTitleBar.setLogoLine(R.drawable.line);
		mAbTitleBar.setLogoOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

			}
		});
		View rightView = mInflater.inflate(R.layout.more_btn, null);
		rightView.findViewById(R.id.moreBtn).setOnClickListener(
				new OnClickListener() {

					@SuppressWarnings("deprecation")
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						showDialog(1);
					}
				});
		mAbTitleBar.addRightView(rightView);
		initTitleRightLayout();

		// AbSlidingTabView2这个类包含了另外的一种效果，和AbSlidingTabView是不同的
		mAbSlidingTabView = (AbSlidingTabView) findViewById(R.id.mAbSlidingTabView);

		// 如果里面的页面列表不能下载原因：
		// Fragment里面用的AbTaskQueue,由于有多个tab，顺序下载有延迟，还没下载好就被缓存了。改成用AbTaskPool，就ok了。
		// 或者setOffscreenPageLimit(0)

		// 缓存数量
		mAbSlidingTabView.getViewPager().setOffscreenPageLimit(15);

		// 禁止滑动
		/*
		 * mAbSlidingTabView.getViewPager().setOnTouchListener(new
		 * OnTouchListener(){
		 * 
		 * @Override public boolean onTouch(View v, MotionEvent event) { return
		 * true; }
		 * 
		 * });
		 */

		Fragment_ALL page1 = new Fragment_ALL();
		Fragment_1 page2 = new Fragment_1();
		Fragment_2 page3 = new Fragment_2();
		Fragment_3 page4 = new Fragment_3();
		Fragment_4 page5 = new Fragment_4();
		Fragment_5 page6 = new Fragment_5();
		Fragment_6 page7 = new Fragment_6();
		Fragment_7 page8 = new Fragment_7();
		Fragment_8 page9 = new Fragment_8();
		Fragment_9 page10 = new Fragment_9();
		Fragment_10 page11 = new Fragment_10();
		Fragment_11 page12 = new Fragment_11();
		Fragment_12 page13 = new Fragment_12();
		Fragment_99 page14 = new Fragment_99();

		List<Fragment> mFragments = new ArrayList<Fragment>();
		mFragments.add(page1);
		mFragments.add(page2);
		mFragments.add(page3);
		mFragments.add(page4);
		mFragments.add(page5);
		mFragments.add(page6);
		mFragments.add(page7);
		mFragments.add(page8);
		mFragments.add(page9);
		mFragments.add(page10);
		mFragments.add(page11);
		mFragments.add(page12);
		mFragments.add(page13);
		mFragments.add(page14);

		List<String> tabTexts = new ArrayList<String>();
		tabTexts.add("所有");
		tabTexts.add("动作");
		tabTexts.add("休闲");
		tabTexts.add("益智");
		tabTexts.add("角色");
		tabTexts.add("策略");
		tabTexts.add("体育");
		tabTexts.add("竞速");
		tabTexts.add("射击");
		tabTexts.add("塔防");
		tabTexts.add("卡牌");
		tabTexts.add("经营");
		tabTexts.add("养成");
		tabTexts.add("其他");

		// 设置样式
		mAbSlidingTabView.setTabTextColor(Color.BLACK);
		mAbSlidingTabView.setTabSelectColor(Color.rgb(30, 168, 131));
		mAbSlidingTabView.setTabBackgroundResource(R.drawable.tab_bg);
		mAbSlidingTabView.setTabLayoutBackgroundResource(R.drawable.slide_top);
		// 演示增加一组
		mAbSlidingTabView.addItemViews(tabTexts, mFragments);

		/*
		 * //演示增加一个 mAbSlidingTabView.addItemView("角色", page5);
		 * mAbSlidingTabView.addItemView("策略", page6);
		 * mAbSlidingTabView.addItemView("体育", page7);
		 * mAbSlidingTabView.addItemView("竞速", page8);
		 * mAbSlidingTabView.addItemView("射击", page9);
		 * mAbSlidingTabView.addItemView("塔防", page10);
		 * mAbSlidingTabView.addItemView("卡牌", page11);
		 * mAbSlidingTabView.addItemView("经营", page12);
		 * mAbSlidingTabView.addItemView("养成", page13);
		 * mAbSlidingTabView.addItemView("其他", page14);
		 */

		mAbSlidingTabView.setTabPadding(20, 8, 20, 8);

		SQLiteDatabase db = this.openOrCreateDatabase("listinfo", MODE_APPEND,
				null);
		try {
			db.execSQL("CREATE TABLE main_info (id INTEGER PRIMARY KEY, name TEXT, cmt TEXT,img TEXT,type INTEGER)");
		} catch (Exception e) {

		}

	}

	@Override
	protected Dialog onCreateDialog(int id) {
		// TODO Auto-generated method stub
		if (id == 1) {
			LayoutInflater factory = LayoutInflater.from(this);
			final View textEntryView = factory.inflate(
					R.layout.dialog_recomment, null);
			return new AlertDialog.Builder(SimpleQuery.this)
					.setIcon(R.drawable.rmt_good)
					.setTitle("我要推荐")
					.setView(textEntryView)
					.setPositiveButton("取消",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int whichButton) {
									/* 左键事件 */
								}
							})
					.setNegativeButton("推荐",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int whichButton) {
									/* 右键事件 */
									AbHttpUtil mAbHttpUtil = AbHttpUtil
											.getInstance(SimpleQuery.this);
									String url = "http://wanku.sinaapp.com/json/recomment.php";
									// 绑定参数
									AbRequestParams params = new AbRequestParams();
									params.put("rcmd_name", ((EditText)textEntryView.findViewById(R.id.rcmt_name_edit)).getText().toString());
									params.put("rcmd_version",  ((EditText)textEntryView.findViewById(R.id.rcmt_version_edit)).getText().toString());
									params.put("rcmd_cmt",  ((EditText)textEntryView.findViewById(R.id.rcmt_cmt_edit)).getText().toString());
									params.put("rcmd_cmter",  ((EditText)textEntryView.findViewById(R.id.rcmt_cmter_edit)).getText().toString());
									params.put("rcmd_cmter_email", ((EditText)textEntryView.findViewById(R.id.rcmt_cmter_email_edit)).getText().toString());
									mAbHttpUtil.post(url, params,
											new AbStringHttpResponseListener() {

												// 获取数据成功会调用这里
												@Override
												public void onSuccess(
														int statusCode,
														String content) {
													showDialog(
															"返回结果",
															content,
															new android.content.DialogInterface.OnClickListener() {
																@Override
																public void onClick(
																		DialogInterface arg0,
																		int arg1) {
																}
															});
												};

												// 开始执行前
												@Override
												public void onStart() {
													// 显示进度框
													showProgressDialog();
												}

												// 失败，调用
												@Override
												public void onFailure(
														int statusCode,
														String content,
														Throwable error) {
													showToast(error
															.getMessage());
												}

												// 完成后调用，失败，成功
												@Override
												public void onFinish() {
													// 移除进度框
													removeProgressDialog();
												};

											});
								}
							}).create();
		}
		return null;
	}

	@Override
	protected void onStart() {
		super.onStart();

	}

	private void initTitleRightLayout() {

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_exit) {
			finish();
		}
		return super.onOptionsItemSelected(item);
	}

}
