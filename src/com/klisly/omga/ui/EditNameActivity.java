package com.klisly.omga.ui;

import com.klisly.omga.ui.base.BaseFragment;
import com.klisly.omga.ui.base.BaseHomeActivity;

public class EditNameActivity extends BaseHomeActivity{

	@Override
	protected String getActionBarTitle() {
		// TODO Auto-generated method stub
		return "编辑昵称";
	}

	@Override
	protected boolean isHomeAsUpEnabled() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	protected void onHomeActionClick() {
		// TODO Auto-generated method stub
		setResult(RESULT_OK);
		finish();
	}

	@Override
	protected BaseFragment getFragment() {
		// TODO Auto-generated method stub
		return EditNameFragment.newInstance();
	}

	@Override
	protected void addActions() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		setResult(RESULT_OK);
		finish();
	}

}
