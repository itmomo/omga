package com.klisly.omga.ui;

import net.youmi.android.spot.SpotDialogListener;
import net.youmi.android.spot.SpotManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;
import cn.bmob.v3.BmobUser;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnClosedListener;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnOpenListener;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.klisly.omga.MyApplication;
import com.klisly.omga.R;
import com.klisly.omga.proxy.UserProxy;
import com.klisly.omga.utils.ActivityUtil;
import com.klisly.omga.utils.LogUtils;

public class MainActivity extends SlidingFragmentActivity implements
		OnClickListener {

	public static final String TAG = "MainActivity";
	private NaviFragment naviFragment;
	private ImageView leftMenu;
	private ImageView rightMenu;
	private SlidingMenu mSlidingMenu;
	// 当前用户登录
	private BmobUser currentUser;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.center_frame);
		leftMenu = (ImageView) findViewById(R.id.topbar_menu_left);
		rightMenu = (ImageView) findViewById(R.id.topbar_menu_right);
		leftMenu.setOnClickListener(this);
		rightMenu.setOnClickListener(this);
		initFragment();
		currentUser = BmobUser.getCurrentUser(MainActivity.this);

		// 插播接口调用
		// 开发者可以到开发者后台设置展示频率，需要到开发者后台设置页面（详细信息->业务信息->无积分广告业务->高级设置）
		// 自4.03版本增加云控制是否开启防误点功能，需要到开发者后台设置页面（详细信息->业务信息->无积分广告业务->高级设置）

		// 加载插播资源
		SpotManager.getInstance(this).loadSpotAds();
		// 设置展示超时时间，加载超时则不展示广告，默认0，代表不设置超时时间
		SpotManager.getInstance(this).setSpotTimeout(5000);// 设置5秒
		SpotManager.getInstance(this).setShowInterval(20);// 设置20秒的显示时间间隔
		// 如需要使用自动关闭插屏功能，请取消注释下面方法
		// SpotManager.getInstance(this)
		// .setAutoCloseSpot(true);// 设置自动关闭插屏开关
		// SpotManager.getInstance(this)
		// .setCloseTime(6000); // 设置关闭插屏时间
	}

	private void initFragment() {
		mSlidingMenu = getSlidingMenu();
		setBehindContentView(R.layout.frame_navi); // 给滑出的slidingmenu的fragment制定layout
		naviFragment = new NaviFragment();
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.frame_navi, naviFragment).commit();
		// 设置slidingmenu的属性
		mSlidingMenu.setMode(SlidingMenu.LEFT);// 设置slidingmeni从哪侧出现
		mSlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);// 只有在边上才可以打开
		mSlidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);// 偏移量
		mSlidingMenu.setFadeEnabled(true);
		mSlidingMenu.setFadeDegree(0.5f);
		mSlidingMenu.setMenu(R.layout.frame_navi);

		// 导航打开监听事件
		mSlidingMenu.setOnOpenListener(new OnOpenListener() {
			@Override
			public void onOpen() {
				naviFragment.initUserInfo();
			}
		});
		// 导航关闭监听事件
		mSlidingMenu.setOnClosedListener(new OnClosedListener() {

			@Override
			public void onClosed() {
				naviFragment.initUserInfo();
			}
		});
		// 加载广告
		SpotManager.getInstance(this).loadSpotAds();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.topbar_menu_left:
			mSlidingMenu.toggle();
			break;
		case R.id.topbar_menu_right:
			if (currentUser != null) {
				// 允许用户使用应用,即有了用户的唯一标识符，可以作为发布内容的字段
				String name = currentUser.getUsername();
				String email = currentUser.getEmail();
				LogUtils.i(TAG, "username:" + name + ",email:" + email);
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, EditActivity.class);
				startActivity(intent);
			} else {
				// 缓存用户对象为空时， 可打开用户注册界面…
				Toast.makeText(MainActivity.this, "请先登录。", Toast.LENGTH_SHORT)
						.show();
				// redictToActivity(mContext, RegisterAndLoginActivity.class,
				// null);
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, UserLoginActivity.class);
				startActivity(intent);
			}
			break;
		default:
			break;
		}
	}

	@Override
	protected void onStop() {
		// 如果不调用此方法，则按home键的时候会出现图标无法显示的情况。
		SpotManager.getInstance(MainActivity.this).disMiss(false);
		super.onStop();
	};

	@Override
	protected void onDestroy() {
		SpotManager.getInstance(this).unregisterSceenReceiver();
		super.onDestroy();
	}

	private static long firstTime;
	private static boolean show = false;

	/**
	 * 连续按两次返回键就退出
	 */
	@Override
	public void onBackPressed() {
		if (firstTime + 2000 > System.currentTimeMillis()) {
			if (ActivityUtil.hasNetwork(MainActivity.this)&&!show&&SpotManager.getInstance(MainActivity.this).checkLoadComplete()) {
				LogUtils.i(TAG, "show ad");
				// 展示插播广告，可以不调用loadSpot独立使用
				SpotManager.getInstance(MainActivity.this).showSpotAds(
						MainActivity.this, new SpotDialogListener() {
							@Override
							public void onShowSuccess() {
								Log.i("MainActivity", "展示成功");
								show = true;
							}

							@Override
							public void onShowFailed() {
								Log.i("MainActivity", "展示失败");
								show = true;
							}

						}); // //
			} else{
				LogUtils.i(TAG, "exit app");
				SpotManager.getInstance(MainActivity.this).disMiss(true);
				MyApplication.getInstance().exit();
				show = false;
				super.onBackPressed();
			}
		} 
		firstTime = System.currentTimeMillis();
	}
}
