package com.klisly.omga.ui;


import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.klisly.omga.MyApplication;
import com.klisly.omga.R;
import com.klisly.omga.proxy.UserProxy;
import com.klisly.omga.proxy.UserProxy.ILoginListener;
import com.klisly.omga.proxy.UserProxy.ISignUpListener;
import com.klisly.omga.ui.base.BasePageActivity;
import com.klisly.omga.utils.ActivityUtil;
import com.klisly.omga.utils.LogUtils;
import com.klisly.omga.utils.StringUtils;
import com.klisly.omga.view.DeletableEditText;
import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.Action;
import com.tencent.tauth.IRequestListener;
/**
 * 用户登录页面
 * @author wizardholy 
 * @email wizardholy@163.com
 * @data 2014年8月9日 下午4:11:57
 */
public class UserRegisterActivity extends BasePageActivity 
	implements OnClickListener,ISignUpListener{

	ActionBar actionbar;
	TextView loginTitle;
	private TextView mTvBtnRegister;
	private DeletableEditText mEtUserName;
	private DeletableEditText mEtPassword;
	private DeletableEditText mEtEmail;
	Button registerButton;
	UserProxy userProxy;
	
	@Override
	protected void setLayoutView() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_register);
	}

	@Override
	protected void findViews() {
		// TODO Auto-generated method stub
		actionbar = (ActionBar)findViewById(R.id.actionbar_register);
		mTvBtnRegister = (TextView) findViewById(R.id.btn_register);
		mEtUserName = (DeletableEditText)findViewById(R.id.et_userName);
		mEtPassword = (DeletableEditText)findViewById(R.id.et_passwd);
		mEtEmail = (DeletableEditText)findViewById(R.id.et_email);
	}

	@Override
	protected void setupViews(Bundle bundle) {
		// TODO Auto-generated method stub
		 actionbar.setTitle(mContext.getResources().getString(R.string.register_caption));
		 actionbar.setDisplayHomeAsUpEnabled(true);
		 actionbar.setHomeAction(new Action() {
			
			@Override
			public void performAction(View view) {
				// TODO Auto-generated method stub
				finish();
			}
			
			@Override
			public int getDrawable() {
				// TODO Auto-generated method stub
				return R.drawable.logo;
			}
		});
		userProxy = new UserProxy(mContext);
	}

	@Override
	protected void setListener() {
		 mTvBtnRegister.setOnClickListener(this);
	}

	@Override
	protected void fetchData() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			case R.id.btn_register:
				
				if(TextUtils.isEmpty(mEtUserName.getText().toString().trim())){
					mEtUserName.setShakeAnimation();
					Toast.makeText(mContext, "请输入用户名", Toast.LENGTH_SHORT).show();
					return;
				}
				if(TextUtils.isEmpty(mEtPassword.getText().toString().trim())){
					mEtPassword.setShakeAnimation();
					Toast.makeText(mContext, "请输入密码", Toast.LENGTH_SHORT).show();
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
				userProxy.signUp(mEtUserName.getText().toString().trim(),
						ActivityUtil.Md5(mEtPassword.getText().toString().trim()), 
						mEtEmail.getText().toString().trim());
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
