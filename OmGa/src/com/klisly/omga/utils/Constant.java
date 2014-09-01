package com.klisly.omga.utils;

/**
 * @author kingofglory
 *         email: kingofglory@yeah.net
 *         blog:  http:www.google.com
 * @date 2014-2-21
 * TODO
 */

public interface Constant {
	
	String BMOB_APP_ID = "c1422153da15d63c250b507a1ad646d7";
	String TABLE_AI = "Mood";
	String TABLE_COMMENT = "Comment";
	
	String NETWORK_TYPE_WIFI = "wifi";
	String NETWORK_TYPE_MOBILE = "mobile";
	String NETWORK_TYPE_ERROR = "error";
	
	
	int AI = 0;
	int HEN = 1;
	int CHUN_LIAN = 2;
	int BIAN_BAI = 3;
	
	int CONTENT_TYPE = 4;
	
	String PRE_NAME = "my_pre";
	
	public static final int DUANZI = 0;
	public static final int NEIHANTU = 1;

	public static final int PUBLISH_COMMENT = 1;
	public static final int NUMBERS_PER_PAGE = 15;//每次请求返回评论条数
	public static final int SAVE_FAVOURITE = 2;
	public static final int GET_FAVOURITE = 3;
	public static final int GO_SETTINGS = 4;
	
	public static final String SEX_MALE = "male";
	public static final String SEX_FEMALE = "female";
	public static final String SEX_SECRET = "secret";
	//用户名或密码错误
	public static final int BMOB_CODE_PASSWD_OR_UNAME_ERROR = 101;
	//用户名或密码错误
	public static final int BMOB_CODE_MULTI_USER_NAME_ERROR = 202;
	// 内容：AppKey is Null, Please initialize BmobSDK.含义：AppKey为空，请初始化。
	public static final int BMOB_CODE_APPKEY_NULL = 9001;
	//内容：Parse data error 含义：解析返回数据出错
	public static final int BMOB_CODE_PARSE_DATA_ERROR = 9002;
	//内容：upload file error 含义：上传文件出错
	public static final int BMOB_CODE_FILE_UPLOAD_ERROR = 9003;
	//内容：upload file failure  含义：文件上传失败
	public static final int BMOB_CODE_FILE_UPLOAD_FAILED = 9004;
	//内容：A batch operation can not be more than 50  含义：批量操作只支持最多50条
	public static final int BMOB_CODE_BATCH_OPERATION_MAX_50_ERROR = 9005;
	//内容：objectId is null 含义：objectId为空
	public static final int BMOB_CODE_OBJECT_ID_NULL=9006;
	//内容：BmobFile File size must be less than 10M. 含义：文件大小超过10M
	public static final int BMOB_CODE_FILE_SIZE＿BEYOND_10M=9007;
	//内容：BmobFile File does not exist.含义：上传文件不存在
	public static final int BMOB_CODE_FILE_UPLOAD_NOT_EXIST=9008;
	//内容：No cache data.含义：没有缓存数据
	public static final int BMOB_CODE_NO_CACHE_DATA=9009;
	//内容：The network is not normal.含义：无网络;
	public static final int BMOB_CODE_NETWORK_ERROR = 9010;
	//内容：BmobUser does not support batch operations.含义：BmobUser类不支持批量操作
	public static final int BMOB_CODE_BMOB_USER_NOT_SUPPORT_BATCH_OPERATION=9011;
	//内容：context is null.含义：上下文为空
	public static final int BMOB_CODE_CONTEXT_NULL=9012;

	//内容： BmobObject Object names(database table name) format is not correct.
	//含义：BmobObject（数据表名称）格式不正确
	public static final int BMOB_CODE_BMOBOBJECT_FORMAT_ERROR=9013;

	public static final String PREFERENCE_PUSH_SERVICE="PREFERENCE_PUSH_SERVICE";
	public static final String PREFERENCE_AUTO_DOWNLOAD_IMAGE="AUTO_DOWNLOAD_IMAGE";
}
