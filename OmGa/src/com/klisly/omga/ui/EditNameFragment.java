package com.klisly.omga.ui;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.UpdateListener;

import com.klisly.omga.R;
import com.klisly.omga.entity.User;
import com.klisly.omga.ui.base.BaseHomeFragment;
import com.klisly.omga.utils.ActivityUtil;

public class EditNameFragment extends BaseHomeFragment{

	private Button commit;
	private EditText input;
	
	public static EditNameFragment newInstance(){
		EditNameFragment fragment = new EditNameFragment();
		return fragment;
	}
	
	@Override
	protected int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.edit_user_sign;
	}

	@Override
	protected void findViews(View view) {
		// TODO Auto-generated method stub
		input = (EditText)view.findViewById(R.id.sign_comment_content);
		commit = (Button)view.findViewById(com.klisly.omga.R.id.sign_comment_commit);
	}

	@Override
	protected void setupViews(Bundle bundle) {
		User user = BmobUser.getCurrentUser(getActivity(),User.class);
		input.setText(user.getNickname());
		
	}

	@Override
	protected void setListener() {
		// TODO Auto-generated method stub
		commit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(TextUtils.isEmpty(input.getText().toString().trim())){
					ActivityUtil.show(getActivity(), "请先输入你的名字");
				}else{
					updateUserName(input.getText().toString().trim());
				}
			}
		});
	}

	@Override
	protected void fetchData() {
		
	}
	
	private void updateUserName(String username){
		User user = BmobUser.getCurrentUser(mContext, User.class);
		if(user != null && username != null){
			user.setNickname(username);
			user.update(mContext, new UpdateListener() {
				
				@Override
				public void onSuccess() {
					// TODO Auto-generated method stub
					getActivity().setResult(Activity.RESULT_OK);
					getActivity().finish();
				}
				
				@Override
				public void onFailure(int arg0, String arg1) {
					// TODO Auto-generated method stub
					ActivityUtil.show(getActivity(), "更改信息失败。请检查网络");
				}
			});
		}
	}

}
