package com.klisly.omga.db;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper{
	
	public static final String DATA_BASE_NAME = "omga.db";
	public static final int DATA_BASE_VERSION = 1;
	public static final String TABLE_QIUSHI = "Qiushi";
	private SQLiteDatabase mSqLiteDatabase;
	
	public static final String QIUSHI_ID = "_id";
	public static final String QIUSHI_OBJECT_ID="objectId";
	public static final String QIUSHI_USERNAME = "username";
	public static final String QIUSHI_URL_AVATAR = "url_avatar";
	public static final String QIUSHI_CONTENT = "content";
	public static final String QIUSHI_URL_IMAGE = "url_image";
	public static final String QIUSHI_GOOD = "good";
	public static final String QIUSHI_BAD = "bad";
	public static final String QIUSHI_COMMENT = "commnent";
	public static final String QIUSHI_SHARE = "share";	
	public static final String QIUSHI_UPDATE_AT="update_at";
	
	public DBHelper(Context context) {
		super(context, DATA_BASE_NAME, null, DATA_BASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		onCreateQiushiTable(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}
	
	private void onCreateQiushiTable(SQLiteDatabase db){
		  StringBuilder createQiushiTableStr=new StringBuilder();
	      createQiushiTableStr.append("CREATE TABLE IF NOT EXISTS ")
	      		.append(DBHelper.TABLE_QIUSHI)
	      		.append(" ( ").append(QIUSHI_ID).append(" INTEGER PRIMARY KEY AUTOINCREMENT,")
	      		.append(QIUSHI_OBJECT_ID).append(" varchar(100),")
	      		.append(QIUSHI_USERNAME).append(" varchar(100),")
	      		.append(QIUSHI_URL_AVATAR).append(" varchar(350),")
	      		.append(QIUSHI_CONTENT).append(" TEXT,")
	      		.append(QIUSHI_URL_IMAGE).append(" varchar(350),")
	      		.append(QIUSHI_GOOD).append(" Integer,")
	      		.append(QIUSHI_BAD).append(" Integer,")
	      		.append(QIUSHI_SHARE).append(" Integer,")
	      		.append(QIUSHI_COMMENT).append(" Integer,")
	      		.append(QIUSHI_UPDATE_AT).append(" varchar(350));");
	      db.execSQL(createQiushiTableStr.toString());
	}
	
	
    /**
     * 获取数据库操作对象
     * @param isWrite 是否可写
     * @return
     */
    public synchronized SQLiteDatabase getDatabase(boolean isWrite) {

        if(mSqLiteDatabase == null || !mSqLiteDatabase.isOpen()) {
            try {
                mSqLiteDatabase=getWritableDatabase();
            } catch(Exception e) {
                // 当数据库不可写时
            	e.printStackTrace();
                mSqLiteDatabase=getReadableDatabase();
                return mSqLiteDatabase;
            }
        } 
        return mSqLiteDatabase;
    }
    
    public int delete(String table, String whereClause, String[] whereArgs) {
        getDatabase(true);
        return mSqLiteDatabase.delete(table, whereClause, whereArgs);
    }

    public long insert(String table, String nullColumnHack, ContentValues values) {
        getDatabase(true);
        return mSqLiteDatabase.insertOrThrow(table, nullColumnHack, values);
    }

    public int update(String table, ContentValues values, String whereClause, String[] whereArgs) {
        getDatabase(true);
        return mSqLiteDatabase.update(table, values, whereClause, whereArgs);
    }

    public Cursor rawQuery(String sql, String[] selectionArgs) {
        getDatabase(false);
        return mSqLiteDatabase.rawQuery(sql, selectionArgs);
    }

    public void execSQL(String sql) {
        getDatabase(true);
        mSqLiteDatabase.execSQL(sql);
    }

    public Cursor query(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having,
        String orderBy) {
        getDatabase(false);
        return mSqLiteDatabase.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);
    }

}
