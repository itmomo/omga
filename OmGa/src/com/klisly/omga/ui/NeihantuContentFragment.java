package com.klisly.omga.ui;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.listener.FindListener;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshBase.State;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.klisly.omga.MyApplication;
import com.klisly.omga.R;
import com.klisly.omga.adapter.AIContentAdapter;
import com.klisly.omga.db.DatabaseUtil;
import com.klisly.omga.entity.Qiushi;
import com.klisly.omga.ui.base.BaseFragment;
import com.klisly.omga.utils.ActivityUtil;
import com.klisly.omga.utils.Constant;
import com.klisly.omga.utils.LogUtils;
/**
 * 内涵图页面
 * @author wizardholy 
 * @email wizardholy@163.com
 * @data 2014年8月21日 下午9:42:11
 */

public class NeihantuContentFragment extends BaseFragment{
	
	private View contentView ;
	private int pageNum;
	private String lastItemTime;//当前列表结尾的条目的创建时间，
	
	private ArrayList<Qiushi> mListItems;
	private PullToRefreshListView mPullRefreshListView;
	private AIContentAdapter mAdapter;
	private ListView actualListView;
	
	private TextView networkTips;
	private ProgressBar progressbar;
	private boolean pullFromUser;
	public enum RefreshType{
		REFRESH,LOAD_MORE
	}
	private RefreshType mRefreshType = RefreshType.LOAD_MORE;
	
	public static BaseFragment newInstance(int index){
		BaseFragment fragment = new NeihantuContentFragment();
		Bundle args = new Bundle();
		args.putInt("page",index);
		fragment.setArguments(args);
		return fragment;
	}
	
	private String getCurrentTime(){
		 SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	     String times = formatter.format(new Date(System.currentTimeMillis()));
	     return times;
	}
	
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		pageNum = 0;
		lastItemTime = getCurrentTime();
		LogUtils.i(TAG,"curent time:"+lastItemTime);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		contentView = inflater.inflate(R.layout.fragment_qiangcontent, null);
		mPullRefreshListView = (PullToRefreshListView)contentView
				.findViewById(R.id.pull_refresh_list);
		networkTips = (TextView)contentView.findViewById(R.id.networkTips);
		progressbar = (ProgressBar)contentView.findViewById(R.id.progressBar);
		mPullRefreshListView.setMode(Mode.BOTH);
		mPullRefreshListView.setOnRefreshListener(new OnRefreshListener2<ListView>() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
				String label = DateUtils.formatDateTime(getActivity(), System.currentTimeMillis(),
						DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
				mPullRefreshListView.setMode(Mode.BOTH);
				pullFromUser = true;
				mRefreshType = RefreshType.REFRESH;
				pageNum = 0;
				lastItemTime = getCurrentTime();
				fetchData();
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
				mRefreshType = RefreshType.LOAD_MORE;
				fetchData();
			}
		});
		mPullRefreshListView.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {

			@Override
			public void onLastItemVisible() {
				
			}
		});
		
		actualListView = mPullRefreshListView.getRefreshableView();
		mListItems = new ArrayList<Qiushi>();
		mAdapter = new AIContentAdapter(mContext, mListItems);
		actualListView.setAdapter(mAdapter);
		if(mListItems.size() == 0){
			fetchData();
		}
		mPullRefreshListView.setState(State.RELEASE_TO_REFRESH, true);
		
		return contentView;
	}
	
	public void fetchData(){
		setState(LOADING);
		BmobQuery<Qiushi> query = new BmobQuery<Qiushi>();
		query.order("-createdAt");
//		query.setCachePolicy(CachePolicy.NETWORK_ONLY);
		query.setLimit(Constant.NUMBERS_PER_PAGE);
		BmobDate date = new BmobDate(new Date(System.currentTimeMillis()));
		query.addWhereLessThan("createdAt", date);
		LogUtils.i(TAG,"SIZE:"+Constant.NUMBERS_PER_PAGE*pageNum);
		query.setSkip(Constant.NUMBERS_PER_PAGE*(pageNum++));
		query.addWhereNotEqualTo("url_image", "");
		LogUtils.i(TAG,"SIZE:"+Constant.NUMBERS_PER_PAGE*pageNum);
		query.include("author");
		query.findObjects(getActivity(), new FindListener<Qiushi>() {
			
			@Override
			public void onSuccess(List<Qiushi> list) {
				fillNewData(list);
			}

			@Override
			public void onError(int arg0, String arg1) {
				// TODO Auto-generated method stub
				LogUtils.i(TAG,"find failed."+arg1);
				pageNum--;
				setState(LOADING_FAILED);
				mPullRefreshListView.onRefreshComplete();
			}
		});
	}
	
	protected void fillNewData(List<Qiushi> list) {
		LogUtils.i(TAG,"find success."+list.size());
		if(list.size()!=0&&list.get(list.size()-1)!=null){
			if(mRefreshType==RefreshType.REFRESH){
				mListItems.clear();
			}
			if(list.size()<Constant.NUMBERS_PER_PAGE){
				LogUtils.i(TAG,"已加载完所有数据~");
			}
			
			//缓存数据到本体
			DatabaseUtil.getInstance(mContext).insertQiushiList(list);
			
			mListItems.addAll(list);
			mAdapter.notifyDataSetChanged();;
			
			setState(LOADING_COMPLETED);
			mPullRefreshListView.onRefreshComplete();
		}else{
			ActivityUtil.show(getActivity(), "暂无更多数据~");
			pageNum--;
			setState(LOADING_COMPLETED);
			mPullRefreshListView.onRefreshComplete();
		}
		
	}

	private static final int LOADING = 1;
	private static final int LOADING_COMPLETED = 2;
	private static final int LOADING_FAILED =3;
	private static final int NORMAL = 4;
	public void setState(int state){
		switch (state) {
		case LOADING:
			if(mListItems.size() == 0){
				mPullRefreshListView.setVisibility(View.GONE);
				progressbar.setVisibility(View.VISIBLE);
			}
			networkTips.setVisibility(View.GONE);
			
			break;
		case LOADING_COMPLETED:
			networkTips.setVisibility(View.GONE);
			progressbar.setVisibility(View.GONE);
			
		    mPullRefreshListView.setVisibility(View.VISIBLE);
		    mPullRefreshListView.setMode(Mode.BOTH);
			
			break;
		case LOADING_FAILED:
			if(mListItems.size()==0){
				mPullRefreshListView.setVisibility(View.VISIBLE);
				mPullRefreshListView.setMode(Mode.PULL_FROM_START);
				networkTips.setVisibility(View.VISIBLE);
			}
			progressbar.setVisibility(View.GONE);
			break;
		case NORMAL:
			
			break;
		default:
			break;
		}
	}
}
