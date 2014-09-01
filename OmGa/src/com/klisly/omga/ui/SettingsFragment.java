package com.klisly.omga.ui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

import com.klisly.omga.MyApplication;
import com.klisly.omga.R;
import com.klisly.omga.entity.User;
import com.klisly.omga.ui.base.BaseHomeFragment;
import com.klisly.omga.utils.ActivityUtil;
import com.klisly.omga.utils.CacheUtils;
import com.klisly.omga.utils.Constant;
import com.klisly.omga.utils.LogUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.umeng.message.PushAgent;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;
import com.umeng.update.UpdateStatus;

public class SettingsFragment extends BaseHomeFragment implements OnClickListener,OnCheckedChangeListener{

	TextView mTvLogOut;
	RelativeLayout mRlUpdate ;
	RelativeLayout mRlCleanCache;
	CheckBox mPushSwitch;
	private CheckBox mAutoDownloadImageSwitch;
	static final int GO_LOGIN = 13;
	public static SettingsFragment newInstance(){
		SettingsFragment fragment = new SettingsFragment();
		return fragment;
	}
	
	@Override
	protected int getLayoutId() {
		return R.layout.fragment_settings;
	}

	@Override
	protected void findViews(View view) {
		mTvLogOut = (TextView) view.findViewById(R.id.user_logout);
		mRlUpdate = (RelativeLayout)view.findViewById(R.id.settings_update);
		mRlCleanCache = (RelativeLayout)view.findViewById(R.id.settings_cache);
		mPushSwitch = (CheckBox)view.findViewById(R.id.settings_push_switch);
		mAutoDownloadImageSwitch = (CheckBox)view.findViewById(R.id.settings_auto_download_image_switch);
	}

	@Override
	protected void setupViews(Bundle bundle) {
		if(mSharedPreferenceUtils.getValue(Constant.PREFERENCE_AUTO_DOWNLOAD_IMAGE, true)){
			mAutoDownloadImageSwitch.setChecked(true);
		}else{
			mAutoDownloadImageSwitch.setChecked(false);
		}
		if(mSharedPreferenceUtils.getValue(Constant.PREFERENCE_PUSH_SERVICE, true)){
			mPushSwitch.setChecked(true);
		}else{
			mPushSwitch.setChecked(false);
		}
		if(!isLogined()){
			mTvLogOut.setText("登录");
		}else{
			mTvLogOut.setText("注销登录");
		}
		
	}

	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try{
//			mIProgressControllor = (IProgressControllor)activity;
		}catch(ClassCastException e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 判断用户是否登录
	 * @return
	 */
	private boolean isLogined(){
		BmobUser user = BmobUser.getCurrentUser(mContext, User.class);
		if(user != null){
			return true;
		}
		return false;
	}
	
	@Override
	protected void setListener() {
		mTvLogOut.setOnClickListener(this);
		mRlUpdate.setOnClickListener(this);
		mRlCleanCache.setOnClickListener(this);
		mPushSwitch.setOnCheckedChangeListener(this);
		mAutoDownloadImageSwitch.setOnCheckedChangeListener(this);
	}

	@Override
	protected void fetchData() {
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.settings_update:
			Toast.makeText(mContext, "正在检查。。。", Toast.LENGTH_SHORT).show();
			UmengUpdateAgent.setUpdateAutoPopup(false);
			UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {

				@Override
				public void onUpdateReturned(int updateStatus, UpdateResponse updateInfo) {
					switch (updateStatus) {
			        case UpdateStatus.Yes: // has update
			            UmengUpdateAgent.showUpdateDialog(mContext, updateInfo);
			            break;
			        case UpdateStatus.No: // has no update
			            Toast.makeText(mContext, "没有更新", Toast.LENGTH_SHORT).show();
			            break;
			        case UpdateStatus.NoneWifi: // none wifi
			            Toast.makeText(mContext, "没有wifi连接， 只在wifi下更新", Toast.LENGTH_SHORT).show();
			            break;
			        case UpdateStatus.Timeout: // time out
			            Toast.makeText(mContext, "请检查网络", Toast.LENGTH_SHORT).show();
			            break;
			        }
				}
			});
			UmengUpdateAgent.forceUpdate(mContext);
			break;
		case R.id.settings_cache:
			ImageLoader.getInstance().clearDiscCache();
			ActivityUtil.show(getActivity(), "清除缓存完毕");
			break;
		case R.id.user_logout:
			if(isLogined()){
				BmobUser.logOut(mContext);
				setupViews(getArguments());
			}else{
				redictToLogin(GO_LOGIN);
			}
		default:
			break;
		}
	}
	private void redictToLogin(int requestCode){
		Intent intent = new Intent();
		intent.setClass(getActivity(), UserLoginActivity.class);
		startActivityForResult(intent, requestCode);
	}
	String dateTime;
	AlertDialog albumDialog;
	public void showAlbumDialog(){
		albumDialog = new AlertDialog.Builder(mContext).create();
		albumDialog.setCanceledOnTouchOutside(true);
		View v = LayoutInflater.from(mContext).inflate(R.layout.dialog_usericon, null);
		albumDialog.show();
		albumDialog.setContentView(v);
		albumDialog.getWindow().setGravity(Gravity.CENTER);
		
		
		TextView albumPic = (TextView)v.findViewById(R.id.album_pic);
		TextView cameraPic = (TextView)v.findViewById(R.id.camera_pic);
		albumPic.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				albumDialog.dismiss();
				Date date1 = new Date(System.currentTimeMillis());
				dateTime = date1.getTime() + "";
				getAvataFromAlbum();
			}
		});
		cameraPic.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				albumDialog.dismiss();
				Date date = new Date(System.currentTimeMillis());
				dateTime = date.getTime() + "";
				getAvataFromCamera();
			}
		});
	}
	

	
	private void getAvataFromCamera(){
		File f = new File(CacheUtils.getCacheDirectory(mContext, true, "icon") + dateTime);
		if (f.exists()) {
			f.delete();
		}
		try {
			f.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Uri uri = Uri.fromFile(f);
		Log.e("uri", uri + "");
		
		Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		camera.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		startActivityForResult(camera, 1);
	}
	
	private void getAvataFromAlbum(){
		Intent intent2 = new Intent(Intent.ACTION_GET_CONTENT);
		intent2.setType("image/*");
		startActivityForResult(intent2, 2);
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		switch (buttonView.getId()) {
		case R.id.settings_push_switch:
			if(isChecked){
				//接受推送，储存值
				mSharedPreferenceUtils.setValue(Constant.PREFERENCE_PUSH_SERVICE, true);
				PushAgent mPushAgent = PushAgent.getInstance(mContext);
				mPushAgent.enable();
			}else{
				//关闭推送，储存值
				mSharedPreferenceUtils.setValue(Constant.PREFERENCE_PUSH_SERVICE, false);
				PushAgent mPushAgent = PushAgent.getInstance(mContext);
				mPushAgent.disable();
			}
			break;
		case R.id.settings_auto_download_image_switch:
			if(isChecked){
				mSharedPreferenceUtils.setValue(Constant.PREFERENCE_AUTO_DOWNLOAD_IMAGE, true);
			}else{
				mSharedPreferenceUtils.setValue(Constant.PREFERENCE_AUTO_DOWNLOAD_IMAGE, false);
			}
			break;
		default:
			break;
		}
			
	}
	
	String iconUrl;
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == Activity.RESULT_OK){
			switch (requestCode) {
			case 1:
				String files =CacheUtils.getCacheDirectory(mContext, true, "icon") + dateTime;
				File file = new File(files);
				if(file.exists()&&file.length() > 0){
					Uri uri = Uri.fromFile(file);
				}else{
					
				}
				break;
			case 2:
				if (data == null) {
					return;
				}
				break;
			default:
				break;
			}
		}
	}
	

}
