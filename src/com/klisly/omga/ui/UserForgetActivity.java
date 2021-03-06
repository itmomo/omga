package com.klisly.omga.ui;


import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.klisly.omga.R;
import com.klisly.omga.proxy.UserProxy;
import com.klisly.omga.proxy.UserProxy.ISignUpListener;
import com.klisly.omga.ui.base.BasePageActivity;
import com.klisly.omga.utils.ActivityUtil;
import com.klisly.omga.utils.LogUtils;
import com.klisly.omga.utils.StringUtils;
import com.klisly.omga.view.DeletableEditText;
import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.Action;
/**
 * 用户登录页面
 * @author wizardholy 
 * @email wizardholy@163.com
 * @data 2014年8月9日 下午4:11:57
 */
public class UserForgetActivity extends BasePageActivity 
	implements OnClickListener,ISignUpListener{

	ActionBar actionbar;
	TextView loginTitle;
	private TextView mTvBtnFind;
	private DeletableEditText mEtUserName;
	private DeletableEditText mEtEmail;
	Button registerButton;
	UserProxy userProxy;
	
	@Override
	protected void setLayoutView() {
		setContentView(R.layout.activity_forget);
	}

	@Override
	protected void findViews() {
		actionbar = (ActionBar)findViewById(R.id.actionbar_forget);
		mTvBtnFind = (TextView) findViewById(R.id.btn_find);
		mEtUserName = (DeletableEditText)findViewById(R.id.et_userName);
		mEtEmail = (DeletableEditText)findViewById(R.id.et_email);
	}

	@Override
	protected void setupViews(Bundle bundle) {
		 actionbar.setTitle(mContext.getResources().getString(R.string.forget_caption));
		 actionbar.setDisplayHomeAsUpEnabled(true);
		 actionbar.setHomeAction(new Action() {
			
			@Override
			public void performAction(View view) {
				finish();
			}
			
			@Override
			public int getDrawable() {
				return R.drawable.logo;
			}
		});
		userProxy = new UserProxy(mContext);
	}

	@Override
	protected void setListener() {
		 mTvBtnFind.setOnClickListener(this);
	}

	@Override
	protected void fetchData() {
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btn_find:
				
				if(TextUtils.isEmpty(mEtUserName.getText().toString().trim())){
					mEtUserName.setShakeAnimation();
					Toast.makeText(mContext, "请输入用户名", Toast.LENGTH_SHORT).show();
					return;
				}
				
				if(TextUtils.isEmpty(mEtEmail.getText().toString().trim())){
					mEtEmail.setShakeAnimation();
					Toast.makeText(mContext, "请输入邮箱地址", Toast.LENGTH_SHORT).show();
					return;
				}
				
				if(!StringUtils.isValidEmail(mEtEmail.getText())){
					mEtEmail.setShakeAnimation();
					Toast.makeText(mContext, "邮箱格式不正确", Toast.LENGTH_SHORT).show();
					return;
				}
				
				userProxy.setOnSignUpListener(this);
				LogUtils.i(TAG,"register begin....");
				userProxy.resetPassword(mEtEmail.getText().toString().trim());
				break;
		}
	}

	@Override
	public void onSignUpSuccess() {
		// TODO Auto-generated method stub
		ActivityUtil.show(this, "注册成功");
		LogUtils.i(TAG,"register successed！");
		this.finish();
	}

	@Override
	public void onSignUpFailure(String msg) {
		// TODO Auto-generated method stub
		ActivityUtil.show(this, msg);
		LogUtils.i(TAG,"register failed！");
	}


}
