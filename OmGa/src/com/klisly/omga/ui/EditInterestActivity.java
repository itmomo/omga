package com.klisly.omga.ui;

import com.klisly.omga.ui.base.BaseFragment;
import com.klisly.omga.ui.base.BaseHomeActivity;

public class EditInterestActivity extends BaseHomeActivity{

	@Override
	protected String getActionBarTitle() {
		// TODO Auto-generated method stub
		return "编辑兴趣";
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
		return EditInterestFragment.newInstance();
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
