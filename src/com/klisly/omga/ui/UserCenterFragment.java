package com.klisly.omga.ui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.EmailVerifyListener;
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
import com.widget.time.JudgeDate;
import com.widget.time.ScreenInfo;
import com.widget.time.WheelMain;

public class UserCenterFragment extends BaseHomeFragment implements OnClickListener{

	private ImageView mIvUserAvatar;
	private TextView mTvUserNickName;
	private TextView mTvSignature;
	private RelativeLayout mRlBtnChangeGenger;
	private TextView mTvGender;
	private RelativeLayout mRlBtnChangeBirthDate;
	private TextView mTvBirthYear;
	private RelativeLayout mRlBtnChangeInterest;
	private TextView mTvInterest;
	private LinearLayout mRlBtnVerifyEmail;
	private TextView mTvEmail;
	private TextView mTvEmailVerifyTip;
	private TextView mTvRegisterDate;
	private TextView mTvUsedPhone;
	public static final int USER_LOGIN=9;
	public static final int UPDATE_USERNAME=10;
	public static final int UPDATE_SEX = 11;
	public static final int UPDATE_AVATAR = 12;
	public static final int UPDATE_INTEREST = 13;
	public static final int UPDATE_SIGN = 14;
	public static int action=UPDATE_AVATAR;
	User user;
	@Override
	protected int getLayoutId() {
		return R.layout.fragment_user_center;
	}

	@Override
	protected void findViews(View view) {
		mIvUserAvatar = (ImageView)view.findViewById(R.id.iv_user_avatar);
		mTvUserNickName = (TextView)view.findViewById(R.id.tv_user_nick);
		mTvSignature = (TextView)view.findViewById(R.id.tv_user_signature);
		mRlBtnChangeBirthDate = (RelativeLayout) view.findViewById(R.id.rl_btn_change_birthdate);
		mTvBirthYear = (TextView)view.findViewById(R.id.tv_user_birthdate);
		mRlBtnChangeGenger = (RelativeLayout) view.findViewById(R.id.rl_btn_change_gender);
		mTvGender = (TextView)view.findViewById(R.id.tv_user_gender);
		mRlBtnChangeInterest = (RelativeLayout) view.findViewById(R.id.rl_btn_change_interest);
		mTvInterest = (TextView)view.findViewById(R.id.tv_user_interest);
		mRlBtnVerifyEmail = (LinearLayout) view.findViewById(R.id.rl_btn_verify_email);
		mTvEmail = (TextView)view.findViewById(R.id.tv_user_email);
		mTvEmailVerifyTip = (TextView)view.findViewById(R.id.tv_user_email_verify_tip);
		mTvRegisterDate = (TextView)view.findViewById(R.id.tv_user_register);
		mTvUsedPhone = (TextView)view.findViewById(R.id.tv_user_phone);
	}

	@Override
	protected void setupViews(Bundle bundle) {
		initPersonalInfo();
	}

	public void initPersonalInfo(){
		user = BmobUser.getCurrentUser(mContext,User.class);
		if(user != null){
			LogUtils.i(TAG, user.toString());
			if(user.getNickname()==null)
				user.setNickname(user.getUsername());
			mTvUserNickName.setText(user.getNickname());
			if(user.getSignature()!=null)
				mTvSignature.setText(user.getSignature());
			if(user.getGender()!=null){
				mTvGender.setText("保密");
				mSharedPreferenceUtils.setValue("sex_settings", 2);
				if(user.getGender().equals(Constant.SEX_FEMALE)){
					mTvGender.setText("女");
					mSharedPreferenceUtils.setValue("sex_settings", 0);
				}else if(user.getGender().equals(Constant.SEX_MALE)){
					mTvGender.setText("男");
					mSharedPreferenceUtils.setValue("sex_settings", 1);
				}
			}
			BmobFile avatarFile = user.getAvatar();
			if(null != avatarFile){
				ImageLoader.getInstance()
				.displayImage(avatarFile.getFileUrl(), mIvUserAvatar, 
						MyApplication.getInstance().getOptions(R.drawable.user_icon_default_main),
						new SimpleImageLoadingListener(){

							@Override
							public void onLoadingComplete(String imageUri, View view,
									Bitmap loadedImage) {
								super.onLoadingComplete(imageUri, view, loadedImage);
							}
					
				});
			}
			if(user.getBirthdate()!=null)
			mTvBirthYear.setText(user.getBirthdate().getDate().split(" ")[0]);
			if(user.getInterest()!=null)
				mTvInterest.setText(user.getInterest());
			mTvRegisterDate.setText(user.getCreatedAt().split(" ")[0]);
			if(user.getEmail()!=null&&user.getEmail().length()>0){
				mTvEmail.setText(user.getEmail());
			}else{
				mTvEmail.setVisibility(View.GONE);
			}
			if(user.getSessionToken()!=null){
				mRlBtnVerifyEmail.setVisibility(View.GONE);
			}
			System.out.println(user.getEmailVerified());
			if(user!=null&&user.getEmail()!=null && user.getEmailVerified()!=null&&user.getEmailVerified()){
				mTvEmailVerifyTip.setVisibility(View.GONE);
			}else{
				mTvEmailVerifyTip.setVisibility(View.VISIBLE);
			}
				
			
			if(user.getPhoneserial()!=null)
			mTvUsedPhone.setText(user.getPhoneserial());
		}
	}
	
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		try{
			//mIProgressControllor = (IProgressControllor)activity;
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
		mIvUserAvatar.setOnClickListener(this);
		mTvUserNickName.setOnClickListener(this);
		mTvSignature.setOnClickListener(this);
		mRlBtnChangeBirthDate.setOnClickListener(this);
		mRlBtnChangeGenger.setOnClickListener(this);
		mRlBtnChangeInterest.setOnClickListener(this);
		if(user!=null&&(user.getEmailVerified()==null || !user.getEmailVerified()))
			mRlBtnVerifyEmail.setOnClickListener(this);
	}

	@Override
	protected void fetchData() {
		
	}

	@Override
	public void onClick(View v) {
		if(!isLogined()){
			redictToLogin(USER_LOGIN);
			return;
		}
		Intent intent=null;
		switch (v.getId()) {
		case R.id.tv_user_nick:
			intent = new Intent();
			intent.setClass(mContext, EditNameActivity.class);
			startActivityForResult(intent, UPDATE_USERNAME);
			break;
		case R.id.iv_user_avatar:
			showAlbumDialog();
			break;
		case R.id.tv_user_signature:
			intent = new Intent();
			intent.setClass(mContext, EditSignActivity.class);
			startActivityForResult(intent, UPDATE_SIGN);
			break;
		case R.id.rl_btn_verify_email:
			showEmailVerfyDialog();
			break;
		case R.id.rl_btn_change_gender:
			editGenderDialog();
			break;
		case R.id.rl_btn_change_birthdate:
			editBirthDateDialog();
			break;
		case R.id.rl_btn_change_interest:
			intent = new Intent();
			intent.setClass(mContext, EditInterestActivity.class);
			startActivityForResult(intent, UPDATE_INTEREST);
			break;
		default:
			break;
		}
	}
	private void showEmailVerfyDialog() {
		AlertDialog.Builder builder = new Builder(getActivity());
	    builder.setMessage("确认发送验证邮件吗？");
	    builder.setTitle("提示");
	    builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(final DialogInterface dialog, int which) {
				BmobUser.requestEmailVerify(getActivity(), user.getEmail(), new EmailVerifyListener() {
				    @Override
				    public void onSuccess() {
				        ActivityUtil.show(getActivity(), "邮件发送成功,请到邮箱中进行激活。");
				        dialog.dismiss();
				    }
				    @Override
				    public void onFailure(int code, String e) {
				    	 ActivityUtil.show(getActivity(), "请求验证邮件失败:" + e);
				    	 LogUtils.e(TAG, "请求验证邮件失败:"+code+":" + e);
				    }
				});
				
			}
		});
	    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
	    builder.create().show();
	}
	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	WheelMain wheelMain;
	private void editBirthDateDialog() {
		LayoutInflater inflater=LayoutInflater.from(mContext);
		final View timepickerview=inflater.inflate(R.layout.timepicker, null);
		ScreenInfo screenInfo = new ScreenInfo(getActivity());
		wheelMain = new WheelMain(timepickerview);
		wheelMain.screenheight = screenInfo.getHeight();
		String time = mTvBirthYear.getText().toString();
		Calendar calendar = Calendar.getInstance();
		if(JudgeDate.isDate(time, "yyyy-MM-dd")){
			try {
				calendar.setTime(dateFormat.parse(time));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		wheelMain.initDateTimePicker(year,month,day);
		new AlertDialog.Builder(mContext)
		.setTitle("选择时间")
		.setView(timepickerview)
		.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				try {
					updateBirthDate(dateFormat.parse(wheelMain.getTime()));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		})
		.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		})
		.show();
		
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
				// TODO Auto-generated method stub
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
	
	AlertDialog contentDialog;
	public void editGenderDialog(){
		contentDialog = new AlertDialog.Builder(mContext).create();
		contentDialog.setCanceledOnTouchOutside(true);
		View v = LayoutInflater.from(mContext).inflate(R.layout.dialog_edit_gender, null);
		contentDialog.show();
		contentDialog.setContentView(v);
		contentDialog.getWindow().setGravity(Gravity.CENTER);
		
		
		RelativeLayout mBtnChangefemale = (RelativeLayout)v.findViewById(R.id.rl_btn_change_female);
		RelativeLayout mBtnChageMale = (RelativeLayout)v.findViewById(R.id.rl_btn_male);
		RelativeLayout mBtnChageSecret = (RelativeLayout)v.findViewById(R.id.rl_btn_secret);
		mBtnChangefemale.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				updateSex( Constant.SEX_FEMALE);
				contentDialog.dismiss();
			}
		});
		mBtnChageMale.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				updateSex( Constant.SEX_MALE);
				contentDialog.dismiss();
			}
		});
		mBtnChageSecret.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				updateSex( Constant.SEX_SECRET);
				contentDialog.dismiss();
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
	
	private void updateBirthDate(Date birthDate){
		User user = BmobUser.getCurrentUser(mContext, User.class);
		if(user!=null){
			user.setBirthdate(new BmobDate(birthDate));
			user.update(mContext, new UpdateListener() {
				
				@Override
				public void onSuccess() {
					initPersonalInfo();
				}
				
				@Override
				public void onFailure(int arg0, String arg1) {
					ActivityUtil.show(getActivity(), "更新信息失败。请检查网络~");
					LogUtils.i(TAG,"更新失败1-->"+arg1);
				}
			});
		}else{
			redictToLogin(UPDATE_SEX);
		}
		
	}
	private void updateSex(String sex){
		User user = BmobUser.getCurrentUser(mContext, User.class);
		if(user!=null){
			user.setGender(sex);
			user.update(mContext, new UpdateListener() {
				
				@Override
				public void onSuccess() {
					initPersonalInfo();
				}
				
				@Override
				public void onFailure(int arg0, String arg1) {
					ActivityUtil.show(getActivity(), "更新信息失败。请检查网络~");
					LogUtils.i(TAG,"更新失败1-->"+arg1);
				}
			});
		}else{
			redictToLogin(UPDATE_SEX);
		}
		
	}

	private void redictToLogin(int requestCode){
		Intent intent = new Intent();
		intent.setClass(getActivity(), UserLoginActivity.class);
		startActivityForResult(intent, requestCode);
		ActivityUtil.show(mContext, "请先登录。");
	}
	
	String iconUrl;
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == Activity.RESULT_OK){
			switch (requestCode) {
			case USER_LOGIN:
			case UPDATE_SIGN:
			case UPDATE_INTEREST:
			case UPDATE_USERNAME:
				initPersonalInfo();
				break;
			case 1:
				String files =CacheUtils.getCacheDirectory(mContext, true, "icon") + dateTime;
				File file = new File(files);
				if(file.exists()&&file.length() > 0){
					Uri uri = Uri.fromFile(file);
					startPhotoZoom(uri);
				}else{
					
				}
				break;
			case 2:
				if (data == null) {
					return;
				}
				startPhotoZoom(data.getData());
				break;
			case 3:
				if (data != null) {
					Bundle extras = data.getExtras();
					if (extras != null) {
						Bitmap bitmap = extras.getParcelable("data");
						// 设置头像
						iconUrl = saveToSdCard(bitmap);
						mIvUserAvatar.setImageBitmap(bitmap);
						updateIcon(iconUrl);
					}
				}
				break;
			default:
				break;
			}
		}
	}
	
	private void updateIcon(String avataPath){
		if(avataPath!=null){
			final BmobFile file = new BmobFile(new File(avataPath));
			file.upload(mContext, new UploadFileListener() {
				
				@Override
				public void onSuccess() {
					LogUtils.i(TAG, "上传文件成功。"+file.getFileUrl());
					User currentUser = BmobUser.getCurrentUser(mContext, User.class);
					currentUser.setAvatar(file);
					currentUser.update(mContext, new UpdateListener() {
						
						@Override
						public void onSuccess() {
							ActivityUtil.show(getActivity(), "更改头像成功。");
						}

						@Override
						public void onFailure(int arg0, String arg1) {
							
							ActivityUtil.show(getActivity(), "更新头像失败。请检查网络~");
							LogUtils.i(TAG,"更新失败2-->"+arg1);
						}
					});
				}

				@Override
				public void onProgress(Integer arg0) {
					
				}

				@Override
				public void onFailure(int arg0, String arg1) {
					ActivityUtil.show(getActivity(), "上传头像失败。请检查网络~");
					LogUtils.i(TAG, "上传文件失败。"+arg1);
				}
			});
		}
	}
	
	
	
	public void startPhotoZoom(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// 锟斤拷锟斤拷锟斤拷锟絚rop=true锟斤拷锟斤拷锟斤拷锟节匡拷锟斤拷锟斤拷Intent锟斤拷锟斤拷锟斤拷锟斤拷示锟斤拷VIEW锟缴裁硷拷
		// aspectX aspectY 锟角匡拷叩谋锟斤拷锟�
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 锟角裁硷拷图片锟斤拷锟�
		intent.putExtra("outputX", 120);
		intent.putExtra("outputY", 120);
		intent.putExtra("crop", "true");
		intent.putExtra("scale", true);// 去锟斤拷锟节憋拷
		intent.putExtra("scaleUpIfNeeded", true);// 去锟斤拷锟节憋拷
		// intent.putExtra("noFaceDetection", true);//锟斤拷锟斤拷识锟斤拷
		intent.putExtra("return-data", true);
		startActivityForResult(intent, 3);

	}
	
	public String saveToSdCard(Bitmap bitmap){
		String files =CacheUtils.getCacheDirectory(mContext, true, "icon") + dateTime+"_12";
		File file=new File(files);
        try {
            FileOutputStream out=new FileOutputStream(file);
            if(bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)){
                out.flush();
                out.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        LogUtils.i(TAG, file.getAbsolutePath());
        return file.getAbsolutePath();
	}
}
