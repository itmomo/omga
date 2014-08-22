package com.klisly.omga.ui.base;


import com.klisly.omga.utils.Constant;
import com.klisly.omga.utils.Sputil;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * 
 * @author wizardholy 
 * @email wizardholy@163.com
 * @data 2014年8月9日 上午12:04:41
 */
public abstract class BaseFragment extends Fragment{
	public static String TAG;
	protected Context mContext;
	protected Sputil sputil;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		TAG = this.getClass().getSimpleName();
		mContext = getActivity();
		if(null == sputil){
			sputil = new Sputil(mContext, Constant.PRE_NAME);
		}
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onPageStart(TAG);
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPageEnd(TAG);
	}
}
