package com.klisly.omga.ui;


import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.OtherLoginListener;

import com.klisly.omga.R;
import com.klisly.omga.proxy.UserProxy;
import com.klisly.omga.proxy.UserProxy.ILoginListener;
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
public class UserLoginActivity extends BasePageActivity 
	implements OnClickListener,ILoginListener{

	ActionBar actionbar;
	TextView loginTitle;
	private RelativeLayout mRlQQLogin;
	private RelativeLayout mRlSinaLogin;
	private Button mBtnLogin;
	private TextView mTvBtnForget;
	private TextView mTvBtnRegister;
	private DeletableEditText mEtUserEmail;
	private DeletableEditText mEtPassword;
	Button registerButton;
	UserProxy userProxy;
	private static final int USER_REGISTER_CODE=0;
	private static final int USER_FINDPASSWD_CODE=1;
	@Override
	protected void setLayoutView() {
		setContentView(R.layout.activity_login);
	}

	@Override
	protected void findViews() {
		actionbar = (ActionBar)findViewById(R.id.actionbar_login);
		mRlSinaLogin = (RelativeLayout) findViewById(R.id.rl_btn_sina_login);
		mRlQQLogin = (RelativeLayout) findViewById(R.id.rl_btn_qq_login);
		mBtnLogin = (Button) findViewById(R.id.btn_login);
		mTvBtnForget = (TextView) findViewById(R.id.tv_btn_forget);
		mTvBtnRegister = (TextView) findViewById(R.id.tv_btn_register);
		mEtUserEmail = (DeletableEditText)findViewById(R.id.et_userName);
		mEtPassword = (DeletableEditText)findViewById(R.id.et_passwd);
		
	}

	@Override
	protected void setupViews(Bundle bundle) {
		 actionbar.setTitle(mContext.getResources().getString(R.string.login_caption));
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
		 mRlQQLogin.setOnClickListener(this);
		 mRlSinaLogin.setOnClickListener(this);
		 mBtnLogin.setOnClickListener(this);
		 mTvBtnForget.setOnClickListener(this);
		 mTvBtnRegister.setOnClickListener(this);
	}

	@Override
	protected void fetchData() {
	}

	@Override
	public void onClick(View v) {
		Intent intent = null;
		switch (v.getId()) {
			case R.id.rl_btn_qq_login:
				BmobUser.qqLogin(this,  "1102290758",  new OtherLoginListener() {
				    @Override
				    public void onSuccess(JSONObject userAuth) {
				    	onLoginSuccess();
				    }

				    @Override
				    public void onFailure(int code, String msg) {
				    	ActivityUtil.show(getApplicationContext(),"第三方登陆失败："+msg);
				    }
				});
				break;
			case R.id.rl_btn_sina_login:
				BmobUser.weiboLogin(this, "1568284227", "http://omyga.bmob.cn/", new OtherLoginListener() {
				    @Override
				    public void onSuccess(JSONObject userAuth) {
				    	onLoginSuccess();
				    }

				    @Override
				    public void onFailure(int code, String msg) {
				    	ActivityUtil.show(getApplicationContext(),"第三方登陆失败："+code);
				    }
				});
				break;
			case R.id.btn_login:
				if(TextUtils.isEmpty(mEtUserEmail.getText().toString().trim())){
					mEtUserEmail.setShakeAnimation();
					Toast.makeText(mContext, "请输入邮箱地址", Toast.LENGTH_SHORT).show();
					return;
				}
				if(!StringUtils.isValidEmail(mEtUserEmail.getText())){
					mEtUserEmail.setShakeAnimation();
					Toast.makeText(mContext, "邮箱格式不正确", Toast.LENGTH_SHORT).show();
					return;
				}
				if(TextUtils.isEmpty(mEtPassword.getText().toString().trim())){
					mEtPassword.setShakeAnimation();
					Toast.makeText(mContext, "请输入密码", Toast.LENGTH_SHORT).show();
					return;
				}
				userProxy.setOnLoginListener(this);
				LogUtils.i(TAG,"login begin....");
				userProxy.login(mEtUserEmail.getText().toString().trim(),ActivityUtil.Md5(mEtPassword.getText().toString().trim()));
				break;
			case R.id.tv_btn_forget:
				intent = new Intent();
				intent.setClass(this.mContext, UserForgetActivity.class);
				startActivityForResult(intent, USER_FINDPASSWD_CODE);
				break;
			case R.id.tv_btn_register:
				intent = new Intent();
				intent.setClass(this.mContext, UserRegisterActivity.class);
				startActivityForResult(intent, USER_REGISTER_CODE);
				break;
		}
	}

	@Override
	public void onLoginSuccess() {
		LogUtils.i(TAG,"login sucessed!");
		setResult(RESULT_OK);
		finish();
	}

	@Override
	public void onLoginFailure(String msg) {
		ActivityUtil.show(this, msg);
		LogUtils.i(TAG,"登录失败!"+msg);
	}
	
	@Override
	protected void onActivityResult(int requestCode , int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		if(resultCode == RESULT_OK){
			switch (requestCode) {
			case USER_REGISTER_CODE:
				onLoginSuccess();
				break;
			default:
				break;
			}
		}
	}
}
