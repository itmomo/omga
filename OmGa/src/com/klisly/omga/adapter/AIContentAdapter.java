package com.klisly.omga.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

import com.klisly.omga.MyApplication;
import com.klisly.omga.R;
import com.klisly.omga.entity.Qiushi;
import com.klisly.omga.utils.ActivityUtil;
import com.klisly.omga.utils.LogUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

/**
 * 内容适配类，填充每一项条目
 * @author wizardholy 
 * @email wizardholy@163.com
 * @data 2014年8月9日 下午12:00:10
 */
public class AIContentAdapter extends BaseContentAdapter<Qiushi>{
	
	public static final String TAG = "AIContentAdapter";
	public static final int SAVE_FAVOURITE = 2;
	public static Context  mContext=null;
	public AIContentAdapter(Context context, List<Qiushi> list) {
		super(context, list);
		this.mContext = context;
	}
	public void refresh() {    
        notifyDataSetChanged();    
     }   
	@Override
	public View getConvertView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
	    final ViewHolder viewHolder;
		if(convertView == null){
			viewHolder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.ai_item, null);
			viewHolder.header = (RelativeLayout) convertView.findViewById(R.id.header);
			viewHolder.userName = (TextView)convertView.findViewById(R.id.user_name);
			viewHolder.userLogo = (ImageView)convertView.findViewById(R.id.user_logo);
			viewHolder.contentText = (TextView)convertView.findViewById(R.id.content_text);
			viewHolder.contentImage = (ImageView)convertView.findViewById(R.id.content_image);
			viewHolder.love = (TextView)convertView.findViewById(R.id.item_action_love);
			viewHolder.share = (TextView)convertView.findViewById(R.id.item_action_share);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder)convertView.getTag();
		}
		final Qiushi entity = dataList.get(position);
		LogUtils.i("user",entity.toString());
//		User user = entity.getAuthor();
//		if(user == null){
//			LogUtils.i("user","USER IS NULL");
//		}
//		if(user.getAvatar()==null){
//			LogUtils.i("user","USER avatar IS NULL");
//		}
//		if(user.getAvatar()!=null){
//			avatarUrl = user.getAvatar().getFileUrl();
//		}
		String avatarUrl = null;
		avatarUrl = entity.getUrl_avatar();
		ImageLoader.getInstance()
		.displayImage(avatarUrl, viewHolder.userLogo, 
				MyApplication.getInstance().getOptions(R.drawable.user_icon_default_main),
				new SimpleImageLoadingListener(){

					@Override
					public void onLoadingComplete(String imageUri, View view,
							Bitmap loadedImage) {
						// TODO Auto-generated method stub
						super.onLoadingComplete(imageUri, view, loadedImage);
					}
			
		});

		viewHolder.userName.setText(entity.getUsername());
		if(entity.getUsername().trim().length()<=0){
			viewHolder.header.setVisibility(View.GONE);
		}
		
		viewHolder.contentText.setText(entity.getContent());
		if(null == entity.getUrl_image()||entity.getUrl_image().trim().length()<=0){
			viewHolder.contentImage.setVisibility(View.GONE);
		}else{
			viewHolder.contentImage.setVisibility(View.VISIBLE);
			ImageLoader.getInstance()
			.displayImage(entity.getUrl_image()==null?"":entity.getUrl_image(), viewHolder.contentImage, 
					MyApplication.getInstance().getOptions(R.drawable.bg_pic_loading),
					new SimpleImageLoadingListener(){
	
						@Override
						public void onLoadingComplete(String imageUri, View view,
								Bitmap loadedImage) {
							// TODO Auto-generated method stub
							super.onLoadingComplete(imageUri, view, loadedImage);
							 float[] cons=ActivityUtil.getBitmapConfiguration(loadedImage, viewHolder.contentImage, 1.0f);
	                         RelativeLayout.LayoutParams layoutParams=
	                             new RelativeLayout.LayoutParams((int)cons[0], (int)cons[1]);
	                         layoutParams.addRule(RelativeLayout.BELOW,R.id.content_text);
	                         viewHolder.contentImage.setLayoutParams(layoutParams);
						}
				
			});
		}
		viewHolder.love.setText(entity.getGood()+"");
		LogUtils.i("love",entity.getGood()+"..");
		//设置是否赞过的标志
		if(entity.getGood()>0){
			viewHolder.love.setTextColor(Color.parseColor("#D95555"));
		}else{
			viewHolder.love.setTextColor(Color.parseColor("#000000"));
		}
		viewHolder.love.setOnClickListener(new OnClickListener() {
//			boolean oldFav = entity.getMyFav();
			@Override
			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				if(MyApplication.getInstance().getCurrentUser()==null){
//					ActivityUtil.show(mContext, "请先登录。");
//					Intent intent = new Intent();
//					intent.setClass(mContext, RegisterAndLoginActivity.class);
//					MyApplication.getInstance().getTopActivity().startActivity(intent);
//					return;
//				}
//				if(entity.getMyLove()){
//					ActivityUtil.show(mContext, "您已赞过啦");
//					return;
//				}
				
//				if(DatabaseUtil.getInstance(mContext).isLoved(entity)){
//					ActivityUtil.show(mContext, "您已赞过啦");
//					entity.setMyLove(true);
//					entity.setLove(entity.getLove()+1);
//					viewHolder.love.setTextColor(Color.parseColor("#D95555"));
//					viewHolder.love.setText(entity.getLove()+"");
//					return;
//				}
//				
//				entity.setLove(entity.getLove()+1);
//				viewHolder.love.setTextColor(Color.parseColor("#D95555"));
//				viewHolder.love.setText(entity.getLove()+"");
//
//				entity.increment("love",1);
//				if(entity.getMyFav()){
//					entity.setMyFav(false);
//				}
//				entity.update(mContext, new UpdateListener() {
//					
//					@Override
//					public void onSuccess() {
//						// TODO Auto-generated method stub
//						entity.setMyLove(true);
//						entity.setMyFav(oldFav);
//						DatabaseUtil.getInstance(mContext).insertFav(entity);
////						DatabaseUtil.getInstance(mContext).queryFav();
//						LogUtils.i(TAG, "点赞成功~");
//					}
//
//					@Override
//					public void onFailure(int arg0, String arg1) {
//						// TODO Auto-generated method stub
//						entity.setMyLove(true);
//						entity.setMyFav(oldFav);
//					}
//				});
			}
		});
		
		viewHolder.share.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
			    ShareSDK.initSDK(mContext);
		        OnekeyShare oks = new OnekeyShare();
		        //关闭sso授权
		        oks.disableSSOWhenAuthorize();
		        
		        // 分享时Notification的图标和文字
		        oks.setNotification(R.drawable.ic_launcher, mContext.getResources().getString(R.string.app_name));
		        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
		        oks.setTitle( mContext.getResources().getString(R.string.share));
		        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
		        oks.setTitleUrl("http://omyga.bmob.cn/");
		        // text是分享文本，所有平台都需要这个字段
		        oks.setText(entity.getContent());
		        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
		        if(entity.getUrl_image()!=null && entity.getUrl_image().length()>0)
		        	oks.setImageUrl(entity.getUrl_image());
		        // url仅在微信（包括好友和朋友圈）中使用
		        oks.setUrl("http://omyga.bmob.cn/");
		        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
		        oks.setComment("评论一番吧^_^");
		        // site是分享此内容的网站名称，仅在QQ空间使用
		        oks.setSite( mContext.getResources().getString(R.string.app_name));
		        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
		        oks.setSiteUrl("http://omyga.bmob.cn/");

		        // 启动分享GUI
		        oks.show( mContext);
			}
		});
//		
//		if(entity.getMyFav()){
//			viewHolder.favMark.setImageResource(R.drawable.ic_action_fav_choose);
//		}else{
//			viewHolder.favMark.setImageResource(R.drawable.ic_action_fav_normal);
//		}
//		viewHolder.favMark.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				//收藏
//				ActivityUtil.show(mContext, "收藏");
//				onClickFav(v,entity);
//				
//			}
//		});
		return convertView;
	}
	private String shareDefaultImage="";


	public static class ViewHolder{
		public RelativeLayout header;
		public ImageView userLogo;
		public TextView userName;
		public TextView contentText;
		public ImageView contentImage;
		public TextView love;
		public TextView share;
	}
	
//	private void onClickFav(View v,final Qiushi qiushi) {
//		// TODO Auto-generated method stub
//		User user = BmobUser.getCurrentUser(mContext, User.class);
//		if(user != null && user.getSessionToken()!=null){
//			BmobRelation favRelaton = new BmobRelation();
//			
//			qiushi.setMyFav(!qiushi.getMyFav());
//			if(qiushi.getMyFav()){
//				((ImageView)v).setImageResource(R.drawable.ic_action_fav_choose);
//				favRelaton.add(qiushi);
//				user.setFavorite(favRelaton);
//				ActivityUtil.show(mContext, "收藏成功。");
//				user.update(mContext, new UpdateListener() {
//					
//					@Override
//					public void onSuccess() {
//						// TODO Auto-generated method stub
//						DatabaseUtil.getInstance(mContext).insertFav(qiangYu);
//						LogUtils.i(TAG, "收藏成功。");
//						//try get fav to see if fav success
////						getMyFavourite();
//					}
//
//					@Override
//					public void onFailure(int arg0, String arg1) {
//						// TODO Auto-generated method stub
//						LogUtils.i(TAG, "收藏失败。请检查网络~");
//						ActivityUtil.show(mContext, "收藏失败。请检查网络~"+arg0);
//					}
//				});
//				
//			}else{
//				((ImageView)v).setImageResource(R.drawable.ic_action_fav_normal);
//				favRelaton.remove(qiangYu);
//				user.setFavorite(favRelaton);
//				ActivityUtil.show(mContext, "取消收藏。");
//				user.update(mContext, new UpdateListener() {
//					
//					@Override
//					public void onSuccess() {
//						// TODO Auto-generated method stub
//						DatabaseUtil.getInstance(mContext).deleteFav(qiangYu);
//						LogUtils.i(TAG, "取消收藏。");
//						//try get fav to see if fav success
////						getMyFavourite();
//					}
//
//					@Override
//					public void onFailure(int arg0, String arg1) {
//						// TODO Auto-generated method stub
//						LogUtils.i(TAG, "取消收藏失败。请检查网络~");
//						ActivityUtil.show(mContext, "取消收藏失败。请检查网络~"+arg0);
//					}
//				});
//			}
//			
//
//		}else{
//			//前往登录注册界面
//			ActivityUtil.show(mContext, "收藏前请先登录。");
//			Intent intent = new Intent();
//			intent.setClass(mContext, RegisterAndLoginActivity.class);
//			MyApplication.getInstance().getTopActivity().startActivityForResult(intent, SAVE_FAVOURITE);
//		}
//	}
//	
//	private void getMyFavourite(){
//		User user = BmobUser.getCurrentUser(mContext, User.class);
//		if(user!=null){
//			BmobQuery<QiangYu> query = new BmobQuery<QiangYu>();
//			query.addWhereRelatedTo("favorite", new BmobPointer(user));
//			query.include("user");
//			query.order("createdAt");
//			query.setLimit(Constant.NUMBERS_PER_PAGE);
//			query.findObjects(mContext, new FindListener<QiangYu>() {
//				
//				@Override
//				public void onSuccess(List<QiangYu> data) {
//					// TODO Auto-generated method stub
//					LogUtils.i(TAG,"get fav success!"+data.size());
//					ActivityUtil.show(mContext, "fav size:"+data.size());
//				}
//
//				@Override
//				public void onError(int arg0, String arg1) {
//					// TODO Auto-generated method stub
//					ActivityUtil.show(mContext, "获取收藏失败。请检查网络~");
//				}
//			});
//		}else{
//			//前往登录注册界面
//			ActivityUtil.show(mContext, "获取收藏前请先登录。");
//			Intent intent = new Intent();
//			intent.setClass(mContext, RegisterAndLoginActivity.class);
//			MyApplication.getInstance().getTopActivity().startActivityForResult(intent,Constant.GET_FAVOURITE);
//		}
//	}
}