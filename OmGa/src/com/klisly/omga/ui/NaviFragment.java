package com.klisly.omga.ui;

import net.youmi.android.offers.OffersManager;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;

import com.klisly.omga.MyApplication;
import com.klisly.omga.R;
import com.klisly.omga.entity.User;
import com.klisly.omga.utils.ActivityUtil;
import com.klisly.omga.utils.LogUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

/**
 * 侧边导航栏
 * 
 * @author wizardholy
 * @email wizardholy@163.com
 * @data 2014年8月9日 下午2:53:25
 */
public class NaviFragment extends Fragment implements OnClickListener {

	private static final int DUANZIFRAGMENT = 0;
	private static final int NEIHANTUFRAGMENT = 1;
	private static final int TUWENFRAGMENT = 2;
	private static final int LOGINFRAGMENT = 3;
	private static final int USERCENTERFRAGMENT = 4;
	private static final int SETTINGSFRAGMENT = 5;
	private static final int INTROFRAGMENT = 6;
	private static final int ABOUTFRAGMENT = 7;
	
	private MainActivity mActivity;
	private LinearLayout mLlBtnUserInfo;
	private LinearLayout mLlUserOperator;
	private ImageView mIvAvatar;
	private TextView mTvUserName;
	private TextView navi_login;
	private TextView navi_duanzi;
	private TextView navi_neihantu;
	private TextView navi_tuwen;
	private TextView navi_me;
	private TextView navi_settings;
	private TextView navi_intro;
	private TextView navi_about;
	DuanziContentFragment mDuanzifragment;
	NeihantuContentFragment mNeihantufragment;
	UserCenterFragment mUserCenterFragment;
	AboutFragment mAboutFragment;
	SettingsFragment mSettingsFragment;
	private FragmentManager fragmentManager;
	//当前用户登录
	private User currentUser;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	private View rootView;// 缓存Fragment view

	/**
	 * 显示左边导航栏fragment
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		if (rootView == null) {
			rootView = inflater.inflate(R.layout.fragment_navi, null);
		}

		fragmentManager = getFragmentManager();

		init();
		
		initUserInfo();
		return rootView;

	}
	/**
	 * 初始化用户模块
	 */
	private void initUserInfo() {
		currentUser =  BmobUser.getCurrentUser(this.getActivity(),User.class);
		if (currentUser != null) {
			// 允许用户使用应用,即有了用户的唯一标识符，可以作为发布内容的字段
			mLlBtnUserInfo.setVisibility(View.VISIBLE);
			mLlUserOperator.setVisibility(View.GONE);
			BmobFile bmobFile = currentUser.getAvatar();
			mTvUserName.setText(currentUser.getUsername());
			ImageLoader.getInstance()
			.displayImage(bmobFile == null ?"":bmobFile.getFileUrl(), mIvAvatar, 
					MyApplication.getInstance().getOptions(R.drawable.user_icon_default_main),
					new SimpleImageLoadingListener(){

						@Override
						public void onLoadingComplete(String imageUri, View view,
								Bitmap loadedImage) {
							super.onLoadingComplete(imageUri, view, loadedImage);
						}
				
			});
		} else {
			mLlBtnUserInfo.setVisibility(View.GONE);
			mLlUserOperator.setVisibility(View.VISIBLE);
		}
		
	}

	@Override
	public void onAttach(Activity activity) {
		mActivity = (MainActivity) activity;
		super.onAttach(activity);
	}

	/**
	 * 初始化，设置点击事件
	 */
	private void init() {
		mLlBtnUserInfo = (LinearLayout) rootView.findViewById(R.id.ll_btn_user);
		mLlUserOperator = (LinearLayout) rootView.findViewById(R.id.ll_login);
		mIvAvatar = (ImageView) rootView.findViewById(R.id.iv_avatar);
		mTvUserName = (TextView) rootView.findViewById(R.id.tv_username);
		navi_login = (TextView) rootView.findViewById(R.id.tv_login);
		navi_duanzi = (TextView) rootView.findViewById(R.id.tv_navi_duanzi);
		navi_neihantu = (TextView) rootView.findViewById(R.id.tv_navi_neihantu);
		navi_tuwen = (TextView) rootView.findViewById(R.id.tv_navi_tuwen);
		navi_settings = (TextView) rootView.findViewById(R.id.tv_navi_settings);
		navi_me = (TextView) rootView.findViewById(R.id.tv_navi_me);
		navi_intro = (TextView) rootView.findViewById(R.id.tv_navi_intro);
		navi_about = (TextView) rootView.findViewById(R.id.tv_navi_about);
		
		changeButtonState(DUANZIFRAGMENT);
		OnTabSelected(DUANZIFRAGMENT);
		
		mLlBtnUserInfo.setOnClickListener(this);
		navi_login.setOnClickListener(this);
		navi_duanzi.setOnClickListener(this);
		navi_neihantu.setOnClickListener(this);
		navi_tuwen.setOnClickListener(this);
		navi_me.setOnClickListener(this);
		navi_settings.setOnClickListener(this);
		navi_intro.setOnClickListener(this);
		navi_about.setOnClickListener(this);
	}

	/**
	 * 点击导航栏切换 同时更改标题
	 */
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.ll_btn_user:
			changeButtonState(USERCENTERFRAGMENT);
			OnTabSelected(USERCENTERFRAGMENT);
			break;
		case R.id.tv_login:
			changeButtonState(LOGINFRAGMENT);
			OnTabSelected(LOGINFRAGMENT);
			break;
		case R.id.tv_navi_me:
			changeButtonState(USERCENTERFRAGMENT);
			OnTabSelected(USERCENTERFRAGMENT);
			break;
		case R.id.tv_navi_duanzi:
			changeButtonState(DUANZIFRAGMENT);
			OnTabSelected(DUANZIFRAGMENT);
			break;
		case R.id.tv_navi_neihantu:
			changeButtonState(NEIHANTUFRAGMENT);
			OnTabSelected(NEIHANTUFRAGMENT);
			break;
		case R.id.tv_navi_tuwen:
			changeButtonState(TUWENFRAGMENT);
			OnTabSelected(TUWENFRAGMENT);
			break;
		case R.id.tv_navi_settings://
			changeButtonState(SETTINGSFRAGMENT);
			OnTabSelected(SETTINGSFRAGMENT);
			break;
		case R.id.tv_navi_intro:
			changeButtonState(INTROFRAGMENT);
			OnTabSelected(INTROFRAGMENT);
			break;
		case R.id.tv_navi_about:
			changeButtonState(ABOUTFRAGMENT);
			OnTabSelected(ABOUTFRAGMENT);
			break;
			
		}
		mActivity.getSlidingMenu().toggle();
	}

	private void changeButtonState(int index) {
		if(index == LOGINFRAGMENT){
			navi_login.setSelected(true);
		}else{
			navi_login.setSelected(false);
		}
		if(index == DUANZIFRAGMENT){
			navi_duanzi.setSelected(true);
		}else{
			navi_duanzi.setSelected(false);
		}
		if(index == TUWENFRAGMENT){
			navi_tuwen.setSelected(true);
		}else{
			navi_tuwen.setSelected(false);
		}
		if(index == NEIHANTUFRAGMENT){
			navi_neihantu.setSelected(true);
		}else{
			navi_neihantu.setSelected(false);
		}
		if(index == SETTINGSFRAGMENT){
			navi_settings.setSelected(true);
		}else{
			navi_settings.setSelected(false);
		}
		if(index == INTROFRAGMENT){
			navi_intro.setSelected(true);
		}else{
			navi_intro.setSelected(false);
		}
		if(index == ABOUTFRAGMENT){
			navi_about.setSelected(true);
		}else{
			navi_about.setSelected(false);
		}
		if(index == USERCENTERFRAGMENT){
			navi_me.setSelected(true);
		}else{
			navi_me.setSelected(false);
		}
	}
	// 选中导航中对应的tab选项
	private void OnTabSelected(int index) {
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		Intent intent = null;
		switch (index) {
		case LOGINFRAGMENT:
			// 缓存用户对象为空时， 可打开用户注册界面…
			intent = new Intent();
			intent.setClass(mActivity, UserLoginActivity.class);
			startActivity(intent);
			break;
		case DUANZIFRAGMENT:
			hideFragments(transaction);
			if (null == mDuanzifragment) {
				mDuanzifragment = new DuanziContentFragment();
				transaction.add(R.id.center, mDuanzifragment);
			} else {
				transaction.show(mDuanzifragment);
			}
			break;
		case NEIHANTUFRAGMENT:
			hideFragments(transaction);
			if (null == mNeihantufragment) {
				mNeihantufragment = new NeihantuContentFragment();
				transaction.add(R.id.center, mNeihantufragment);
			} else {
				transaction.show(mNeihantufragment);
			}
			break;
		case TUWENFRAGMENT:
			hideFragments(transaction);
			if (null == mDuanzifragment) {
				mDuanzifragment = new DuanziContentFragment();
				transaction.add(R.id.center, mDuanzifragment);
			} else {
				transaction.show(mDuanzifragment);
			}
			break;
		case USERCENTERFRAGMENT:
			if (currentUser != null) {
				hideFragments(transaction);
				if (null == mUserCenterFragment) {
					mUserCenterFragment = new UserCenterFragment();
					transaction.add(R.id.center, mUserCenterFragment);
				} else {
					transaction.show(mUserCenterFragment);
				}
			} else {
				// 缓存用户对象为空时， 可打开用户注册界面…
				Toast.makeText(mActivity, "请先登录", Toast.LENGTH_SHORT).show();
				intent = new Intent();
				intent.setClass(mActivity, UserLoginActivity.class);
				startActivity(intent);
			}
			break;
		case SETTINGSFRAGMENT:
			hideFragments(transaction);
			if (null == mSettingsFragment) {
				mSettingsFragment = new SettingsFragment();
				transaction.add(R.id.center, mSettingsFragment);
			} else {
				transaction.show(mSettingsFragment);
			}
			break;
		case INTROFRAGMENT:
			OffersManager.getInstance(mActivity).showOffersWall();
			break;
		case ABOUTFRAGMENT:
			hideFragments(transaction);
			if (null == mAboutFragment) {
				mAboutFragment = new AboutFragment();
				transaction.add(R.id.center, mAboutFragment);
			} else {
				transaction.show(mAboutFragment);
			}
			break;
		}
		transaction.commit();
	}

	/**
	 * 将所有fragment都置为隐藏状态
	 * 
	 * @param transaction
	 *            用于对Fragment执行操作的事务
	 */
	private void hideFragments(FragmentTransaction transaction) {
		if (mDuanzifragment != null) {
			transaction.hide(mDuanzifragment);
		}
		if (mNeihantufragment != null) {
			transaction.hide(mNeihantufragment);
		}
		if (mUserCenterFragment != null) {
			transaction.hide(mUserCenterFragment);
		}
		if (mAboutFragment != null) {
			transaction.hide(mAboutFragment);
		}
		if (mSettingsFragment != null) {
			transaction.hide(mSettingsFragment);
		}
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
	
}
