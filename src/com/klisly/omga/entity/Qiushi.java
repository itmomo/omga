package com.klisly.omga.entity;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobRelation;

public class Qiushi extends BmobObject implements Serializable{
	private static final long serialVersionUID = -1541805206926921773L;
	private String username ;
	private String url_avatar ;
	private String content ;
	private String url_image ;
	private int good;
	private int bad;
	private int share;
	private int comment;
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getUrl_avatar() {
		return url_avatar;
	}
	public void setUrl_avatar(String url_avatar) {
		this.url_avatar = url_avatar;
	}
	public String getUrl_image() {
		return url_image;
	}
	public void setUrl_image(String url_image) {
		this.url_image = url_image;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	public int getGood() {
		return good;
	}
	public void setGood(int good) {
		this.good = good;
	}
	public int getBad() {
		return bad;
	}
	public void setBad(int bad) {
		this.bad = bad;
	}
	public int getShare() {
		return share;
	}
	public void setShare(int share) {
		this.share = share;
	}
	public int getComment() {
		return comment;
	}
	public void setComment(int comment) {
		this.comment = comment;
	}
	@Override
	public String toString() {
		return "Qiushi [username=" + username + ", url_avatar=" + url_avatar
				+ ", content=" + content + ", url_image=" + url_image
				+ ", good=" + good + ", bad=" + bad + ", share=" + share
				+ ", comment=" + comment + "]";
	}

}
