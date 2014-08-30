package com.klisly.omga.proxy;

import java.util.Date;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.listener.ResetPasswordListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

import com.klisly.omga.R;
import com.klisly.omga.entity.User;
import com.klisly.omga.utils.Constant;
import com.klisly.omga.utils.LogUtils;

import android.content.Context;

public class UserProxy {

	public static final String TAG = "UserProxy";
	
	private Context mContext;
	
	public UserProxy(Context context){
		this.mContext = context;
	}
	
	public void signUp(String userName,String password,String email){
		User user = new User();
		user.setUsername(email);
		user.setNickname(userName);
		user.setAvatar(null);
		user.setPassword(password);
		user.setEmail(email);
		user.setBirthdate(new BmobDate(new Date(System.currentTimeMillis())));
		user.setGender(Constant.SEX_SECRET);
		user.setSignature("这个家伙很懒，什么也不说。。。");
		user.setInterest(" 吃饭，睡觉，打豆豆");
		user.setFavorits("");
		user.setPhoneserial("未知");
		user.signUp(mContext, new SaveListener() {
			@Override
			public void onSuccess() {
				// TODO Auto-generated method stub
				if(signUpLister != null){
					signUpLister.onSignUpSuccess();
				}else{
					LogUtils.i(TAG,"signup listener is null,you must set one!");
				}
			}

			@Override
			public void onFailure(int code, String msg) {
				
				if(signUpLister != null){
					LogUtils.e(TAG, "sign up failed:"+code+":"+msg);
					String message = mContext.getResources().getString(R.string.network_tip);
					if(code == Constant.BMOB_CODE_NETWORK_ERROR){
						message = mContext.getResources().getString(R.string.network_tip);
					}else if(code == Constant.BMOB_CODE_PASSWD_OR_UNAME_ERROR){
						message = mContext.getResources().getString(R.string.login_fail_tip);
					}else if(code == Constant.BMOB_CODE_MULTI_USER_NAME_ERROR){
						message = mContext.getResources().getString(R.string.register_mutli_name);
					}
					signUpLister.onSignUpFailure(message);
					
				}else{
					LogUtils.i(TAG,"signup listener is null,you must set one!");
				}
			}
		});
	}
	
	public interface ISignUpListener{
		void onSignUpSuccess();
		void onSignUpFailure(String msg);
	}
	private ISignUpListener signUpLister;
	public void setOnSignUpListener(ISignUpListener signUpLister){
		this.signUpLister = signUpLister;
	}
	
	
	public User getCurrentUser(){
		User user = BmobUser.getCurrentUser(mContext, User.class);
		if(user != null){
			LogUtils.i(TAG,"本地用户信息" + user.getObjectId() + "-"
					+ user.getUsername() + "-"
					+ user.getSessionToken() + "-"
					+ user.getCreatedAt() + "-"
					+ user.getUpdatedAt() + "-"
					+ user.getSignature() + "-"
					+ user.getGender());
			return user;
		}else{
			LogUtils.i(TAG,"本地用户为null,请登录。");
		}
		return null;
	}
	
	public void login(String userEmail,String password){
		final BmobUser user = new BmobUser();
		user.setUsername(userEmail);
		user.setEmail(userEmail);;
		user.setPassword(password);
		user.login(mContext, new SaveListener() {
			
			@Override
			public void onSuccess() {
				// TODO Auto-generated method stub
				if(loginListener != null){
					loginListener.onLoginSuccess();
				}else{
					LogUtils.i(TAG, "login listener is null,you must set one!");
				}
			}

			@Override
			public void onFailure(int code, String msg) {
				if(loginListener != null){
					String message = "";
					if(code == Constant.BMOB_CODE_NETWORK_ERROR){
						message = mContext.getResources().getString(R.string.network_tip);
					}else if(code == Constant.BMOB_CODE_PASSWD_OR_UNAME_ERROR){
						message = mContext.getResources().getString(R.string.login_fail_tip);
					}
					loginListener.onLoginFailure(message);
				}else{
					LogUtils.i(TAG,"login listener is null,you must set one!");
				}
			}
		});
	}
	
	public interface ILoginListener{
		void onLoginSuccess();
		void onLoginFailure(String msg);
	}
	private ILoginListener loginListener;
	public void setOnLoginListener(ILoginListener loginListener){
		this.loginListener  = loginListener;
	}
	
	public void logout(){
		BmobUser.logOut(mContext);
		LogUtils.i(TAG, "logout result:"+(null == getCurrentUser()));
	}
	
	public void update(String... args){
		User user = getCurrentUser();
		user.setUsername(args[0]);
		user.setEmail(args[1]);
		user.setPassword(args[2]);
		user.setGender(args[3]);
		user.setSignature(args[4]);
		//...
		user.update(mContext, new UpdateListener() {
			
			@Override
			public void onSuccess() {
				// TODO Auto-generated method stub
				if(updateListener != null){
					updateListener.onUpdateSuccess();
				}else{
					LogUtils.i(TAG,"update listener is null,you must set one!");
				}
			}

			@Override
			public void onFailure(int code, String msg) {
				// TODO Auto-generated method stub
				if(updateListener != null){
					updateListener.onUpdateFailure(msg);
				}else{
					LogUtils.i(TAG, "update listener is null,you must set one!");
				}
			}
		});
	}
	
	public interface IUpdateListener{
		void onUpdateSuccess();
		void onUpdateFailure(String msg);
	}
	private IUpdateListener updateListener;
	public void setOnUpdateListener(IUpdateListener updateListener){
		this.updateListener = updateListener;
	}
	
	public void resetPassword(String email){
		BmobUser.resetPassword(mContext, email, new ResetPasswordListener() {
			
			@Override
			public void onSuccess() {
				// TODO Auto-generated method stub
				if(resetPasswordListener != null){
					resetPasswordListener.onResetSuccess();
				}else{
					LogUtils.i(TAG,"reset listener is null,you must set one!");
				}
			}

			@Override
			public void onFailure(int arg0, String msg) {
				// TODO Auto-generated method stub
				if(resetPasswordListener != null){
					resetPasswordListener.onResetFailure(msg);
				}else{
					LogUtils.i(TAG,"reset listener is null,you must set one!");
				}
			}
		});
	}
	public interface IResetPasswordListener{
		void onResetSuccess();
		void onResetFailure(String msg);
	}
	private IResetPasswordListener resetPasswordListener;
	public void setOnResetPasswordListener(IResetPasswordListener resetPasswordListener){
		this.resetPasswordListener = resetPasswordListener;
	}

}
