package com.klisly.omga.entity;

import java.util.Date;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobRelation;

import com.klisly.omga.utils.LogUtils;
/**
 * 自定义用户类
 * @author wizardholy 
 * @email wizardholy@163.com
 * @data 2014年8月11日 下午11:05:56
 */
public class User extends BmobUser{

	private static final long serialVersionUID = -2680830980440856655L;
	public static final String TAG = "User";
	private String signature;
	private BmobFile avatar;
	private String nickname;
	private String gender;
	private BmobDate birthdate;
	private String interest;
	private String phoneserial;
	//在项目中用户收藏的糗事id用如下方式存放entity.getObjectId()+";"
	private String favorits;
	public String getSignature() {
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}
	public BmobFile getAvatar() {
		return avatar;
	}
	public void setAvatar(BmobFile avatar) {
		this.avatar = avatar;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}

	public BmobDate getBirthdate() {
		return birthdate;
	}
	public void setBirthdate(BmobDate birthdate) {
		this.birthdate = birthdate;
	}
	public String getInterest() {
		return interest;
	}
	public void setInterest(String interest) {
		this.interest = interest;
	}
	public String getPhoneserial() {
		return phoneserial;
	}
	public void setPhoneserial(String phoneserial) {
		this.phoneserial = phoneserial;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getFavorits() {
		return favorits;
	}
	public void setFavorits(String favorits) {
		this.favorits = favorits;
	}
	@Override
	public String toString() {
		return "User [signature=" + signature + ", avatar=" + avatar
				+ ", gender=" + gender + ", birthdate=" + birthdate + ", interest="
				+ interest+ ", favorits="+ favorits 
				+ ", phoneserial=" + phoneserial + "]";
	}
	
}
