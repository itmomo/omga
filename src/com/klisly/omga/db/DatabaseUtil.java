package com.klisly.omga.db;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.klisly.omga.entity.Qiushi;
import com.klisly.omga.utils.LogUtils;

public class DatabaseUtil {
	private static final String TAG = "DatabaseUtil";

	private static DatabaseUtil instance;

	/** 数据库帮助类 **/
	private DBHelper dbHelper;

	public synchronized static DatabaseUtil getInstance(Context context) {
		if (instance == null) {
			instance = new DatabaseUtil(context);
		}
		return instance;
	}

	/**
	 * 初始化
	 * 
	 * @param context
	 */
	private DatabaseUtil(Context context) {
		dbHelper = new DBHelper(context);
	}

	/**
	 * 销毁
	 */
	public static void destory() {
		if (instance != null) {
			instance.onDestory();
		}
	}

	/**
	 * 销毁
	 */
	public void onDestory() {
		instance = null;
		if (dbHelper != null) {
			dbHelper.close();
			dbHelper = null;
		}
	}

	public void deleteQiushi(Qiushi qiushi) {
		String where = DBHelper.QIUSHI_ID + " = '" + qiushi.getObjectId() + "'";
		dbHelper.delete(DBHelper.TABLE_QIUSHI, where, null);
	}

	public long insertQiushi(Qiushi qiushi) {
		ContentValues conv;
		long uri = 0;
		Cursor cursor = null;
		String where = DBHelper.QIUSHI_OBJECT_ID + " = '" + qiushi.getObjectId() + "'";
		cursor = dbHelper.query(DBHelper.TABLE_QIUSHI, null, where, null, null,
				null, null);
		if (cursor != null && cursor.getCount() > 0) {
			cursor.moveToFirst();
			conv = fillQiuShiContentValue(false, qiushi);
			dbHelper.update(DBHelper.TABLE_QIUSHI, conv, where, null);
		} else {
			conv = fillQiuShiContentValue(true, qiushi);
			uri = dbHelper.insert(DBHelper.TABLE_QIUSHI, null, conv);
		}
		if (cursor != null) {
			cursor.close();
			dbHelper.close();
		}
		return uri;
	}

	private ContentValues fillQiuShiContentValue(boolean isInsertId,
			Qiushi qiushi) {
		ContentValues conv = new ContentValues();
		if (isInsertId)
			conv.put(DBHelper.QIUSHI_OBJECT_ID, qiushi.getObjectId());
		conv.put(DBHelper.QIUSHI_USERNAME, qiushi.getUsername());
		conv.put(DBHelper.QIUSHI_URL_AVATAR, qiushi.getUrl_avatar());
		conv.put(DBHelper.QIUSHI_URL_IMAGE, qiushi.getUrl_image());
		conv.put(DBHelper.QIUSHI_CONTENT, qiushi.getContent());
		conv.put(DBHelper.QIUSHI_GOOD, qiushi.getGood());
		conv.put(DBHelper.QIUSHI_BAD, qiushi.getBad());
		conv.put(DBHelper.QIUSHI_SHARE, qiushi.getShare());
		conv.put(DBHelper.QIUSHI_COMMENT, qiushi.getComment());
		conv.put(DBHelper.QIUSHI_UPDATE_AT, qiushi.getUpdatedAt());
		return conv;
	}

	/**
	 * 批量插入缓存
	 * 
	 * @param lists
	 */
	public void insertQiushiList(List<Qiushi> lists) {
		if (lists != null && lists.size() > 0) {
			for (Iterator iterator = lists.iterator(); iterator.hasNext();) {
				Qiushi qiushi = (Qiushi) iterator.next();
				insertQiushi(qiushi);
			}
		}
	}

	public ArrayList<Qiushi> queryDuanziQiushis(int page, int pageSize) {
		ArrayList<Qiushi> contents = new ArrayList<Qiushi>();
		String sql = "select * from " + DBHelper.TABLE_QIUSHI + " where "
				+ DBHelper.QIUSHI_URL_IMAGE + "=''  " + " Limit " + pageSize
				+ " Offset " + (page * pageSize);
		contents = queryQiushis(sql);
		return contents;
	}

	public ArrayList<Qiushi> queryQiushis(String sql) {
		Cursor cursor = dbHelper.rawQuery(sql, null);
		LogUtils.i(TAG, cursor.getCount() + "");
		if (cursor == null) {
			return null;
		}
		ArrayList<Qiushi> contents = new ArrayList<Qiushi>();
		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			Qiushi content = new Qiushi();
			content.setObjectId(cursor.getString(cursor
					.getColumnIndex(DBHelper.QIUSHI_OBJECT_ID)));
			content.setUsername(cursor.getString(cursor
					.getColumnIndex(DBHelper.QIUSHI_USERNAME)));
			content.setUrl_avatar(cursor.getString(cursor
					.getColumnIndex(DBHelper.QIUSHI_URL_AVATAR)));
			content.setContent(cursor.getString(cursor
					.getColumnIndex(DBHelper.QIUSHI_CONTENT)));
			content.setUrl_image(cursor.getString(cursor
					.getColumnIndex(DBHelper.QIUSHI_URL_IMAGE)));
			content.setGood(cursor.getInt(cursor
					.getColumnIndex(DBHelper.QIUSHI_GOOD)));
			content.setBad(cursor.getInt(cursor
					.getColumnIndex(DBHelper.QIUSHI_BAD)));
			content.setComment(cursor.getInt(cursor
					.getColumnIndex(DBHelper.QIUSHI_COMMENT)));
			content.setShare(cursor.getInt(cursor
					.getColumnIndex(DBHelper.QIUSHI_SHARE)));
			contents.add(content);
		}
		if (cursor != null) {
			cursor.close();
		}
		return contents;
	}

	public void clearQiushiTable() {
		String sql = "delete from "+DBHelper.TABLE_QIUSHI;
		dbHelper.execSQL(sql);
	}
}
