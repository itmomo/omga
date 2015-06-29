package com.klisly.omga.ui;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
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
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.klisly.omga.R;
import com.klisly.omga.adapter.AIContentAdapter;
import com.klisly.omga.db.DatabaseUtil;
import com.klisly.omga.entity.Qiushi;
import com.klisly.omga.ui.base.BaseFragment;
import com.klisly.omga.utils.ActivityUtil;
import com.klisly.omga.utils.Constant;
import com.klisly.omga.utils.LogUtils;
import com.klisly.omga.utils.NetworkUtil;
/**
 * 添加段子图页面
 * @author wizardholy 
 * @email wizardholy@163.com
 * @data 2014年8月22日 下午8:20:52
 */
public class DuanziContentFragment extends BaseFragment{
	
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
		BaseFragment fragment = new DuanziContentFragment();
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
		super.onAttach(activity);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		pageNum = 0;
		lastItemTime = getCurrentTime();
		LogUtils.i(TAG,"curent time:"+lastItemTime);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
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
				localDataPageNum=0;
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
		loadLocalData();
		if(mListItems.size() == 0){
			fetchData();
		}
		return contentView;
	} 
	private  int localDataPageNum=0;
	
	private void loadLocalData() {
		ArrayList<Qiushi> list = DatabaseUtil.getInstance(mContext).queryDuanziQiushis(localDataPageNum,Constant.NUMBERS_PER_PAGE);
		if(list.size()>0){
			fillNewData(list);
			localDataPageNum++;
		}
	}
	public void fetchData(){
		setState(LOADING);
		if(NetworkUtil.isAvailable(getActivity())){
			getDuanData();
		}else{
			loadLocalData();
		}
		
	}
	private HashSet<String> idSet = new HashSet<String>();
	private void removeDuplicate(List<Qiushi> list) {
		for(int i = 0 ; i < list.size();){
			if(idSet.contains(list.get(i).getObjectId())){
				list.remove(i);
			}else{
				i++;
			}
		}
	}
	
	protected void fillNewData(List<Qiushi> list) {
		LogUtils.i(TAG,"find success."+list.size());
		if(list.size()!=0){
			removeDuplicate(list);
			if(list.size()>0&&mRefreshType==RefreshType.REFRESH){
				mListItems.clear();
			}
			if(list.size()<Constant.NUMBERS_PER_PAGE){
				LogUtils.i(TAG,"已加载完所有数据~");
			}
			//缓存数据到本体
			DatabaseUtil.getInstance(mContext).insertQiushiList(list);
			
			for(Qiushi qiushi : list){
				idSet.add(qiushi.getObjectId());
			}
			
			mListItems.addAll(list);
			mAdapter.notifyDataSetChanged();
			setState(LOADING_COMPLETED);
			mPullRefreshListView.onRefreshComplete();
		}else{
			ActivityUtil.show(getActivity(), "暂无更多数据~");
			pageNum--;
			setState(LOADING_COMPLETED);
			mPullRefreshListView.onRefreshComplete();
		}
	}
	
	private void getDuanData() {
		BmobQuery<Qiushi> query = new BmobQuery<Qiushi>();
		query.order("-createdAt");
//		query.setCachePolicy(CachePolicy.CACHE_ELSE_NETWORK);
		query.setLimit(Constant.NUMBERS_PER_PAGE);
		BmobDate date = new BmobDate(new Date(System.currentTimeMillis()));
		query.addWhereLessThan("createdAt", date);
		query.addWhereEqualTo("url_image", "");
		LogUtils.i(TAG,"SIZE:"+Constant.NUMBERS_PER_PAGE*pageNum);
		query.setSkip(Constant.NUMBERS_PER_PAGE*(pageNum++));
		LogUtils.i(TAG,"SIZE:"+Constant.NUMBERS_PER_PAGE*pageNum);
		query.include("author");
		query.findObjects(getActivity(), new FindListener<Qiushi>() {
			
			@Override
			public void onSuccess(List<Qiushi> list) {
				fillNewData(list);
			}

			@Override
			public void onError(int code, String msg) {
				LogUtils.i(TAG,"find failed."+msg);
				pageNum--;
				setState(LOADING_FAILED);
				mPullRefreshListView.onRefreshComplete();
			}
		});
		
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
