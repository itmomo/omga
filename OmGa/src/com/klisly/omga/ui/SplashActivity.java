package com.klisly.omga.ui;

import net.youmi.android.AdManager;
import android.os.Bundle;
import android.os.Handler;
import cn.bmob.v3.Bmob;

import com.klisly.omga.R;
import com.klisly.omga.ui.base.BaseActivity;
import com.klisly.omga.utils.Constant;
import com.klisly.omga.utils.LogUtils;
import com.klisly.omga.utils.UmengStat;
import com.umeng.analytics.MobclickAgent;
import com.umeng.fb.FeedbackAgent;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengRegistrar;

/**
 * 
 * @author wizardholy 
 * @email wizardholy@163.com
 * @data 2014年8月9日 上午11:34:40
 */
public class SplashActivity extends BaseActivity {

	private static final long DELAY_TIME = 2000L;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		//Bmob SDK初始化--只需要这一段代码即可完成初始化
				//请到Bmob官网(http://www.bmob.cn/)申请ApplicationId,具体地址:http://docs.bmob.cn/android/faststart/index.html?menukey=fast_start&key=start_android
		Bmob.initialize(this, Constant.BMOB_APP_ID);
		LogUtils.i(TAG,TAG + " Launched ！");
		//友盟统计反馈代码
		MobclickAgent.openActivityDurationTrack(UmengStat.IS_OPEN_ACTIVITY_AUTO_STAT);
		FeedbackAgent agent = new FeedbackAgent(this);
		agent.sync();
		//打开首页
		redirectByTime();
		//友盟推送设置
		if(sputil.getValue("isPushOn", true)){
			PushAgent mPushAgent = PushAgent.getInstance(mContext);
			mPushAgent.enable();
			LogUtils.i(TAG,"device_token:"+UmengRegistrar.getRegistrationId(mContext));
		}else{
			PushAgent mPushAgent = PushAgent.getInstance(mContext);
			mPushAgent.disable();
		}
		
		AdManager.getInstance(mContext).init("ad08ed9b63b3e95f", "abf86ba7453e1f20", false);
		
	}
	
	/**
	 * 根据时间进行页面跳转
	 */
	private void redirectByTime() {
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				redictToActivity(SplashActivity.this, MainActivity.class, null);
				finish();
			}
		}, DELAY_TIME);
	}

}
