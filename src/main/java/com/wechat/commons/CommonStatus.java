package com.wechat.commons;

public class CommonStatus {
	/**
	 * 参数
	 */
	public final static String PARAM_SING="sign";
	public final static String PARAM_USER_ID="userId";
	public final static String PARAM_TIMESTAMP="timestamp";
	public final static String PARAM_IMEI="imei";
	public final static String CURRENT_USER="current_user";
	public final static String REQUEST_IP="ip";
	
	/**
	 * 支付参数
	 */
	public final static String ENCRYPT_TYPE_RSA="RSA";//加密类型
	public final static String ENCRYPT_TYPE_3DES="3DES";//加密类型
	public final static String ENCRYPT_TYPE_DES="3DES";//加密类型
	public final static String SING_TYPE="SHA-256";//签名类型
	public final static String CHARSET="UTF-8";
	
	/**
	 * 支付返回参数
	 */
	public final static String RETURN_PARAM_NULL="RETURN_PARAM_NULL";//返回数据为null
	public final static String SYSTEM_ERROR="SYSTEM_ERROR";
	public final static String OUT_TRADE_NO_EXIST="OUT_TRADE_NO_EXIST";
	public final static String TRADE_NOT_EXIST="TRADE_NOT_EXIST";
	public final static String ILLEGAL_SIGN="ILLEGAL_SIGN";
	public final static String SUCCESS="0000";
	
	public final static String TRADE_FINI="FINI";//交易成功
	public final static String TRADE_CLOS="CLOS";//交易关闭，失败
	public final static String TRADE_WPAR="WPAR";//等待支付结果
	public final static String TRADE_BUID="BUID";//交易建立
	public final static String TRADE_REFU="REFU";//交易退款
	public final static String TRADE_ACSU="ACSU";//已受理
	
	public final static String PAY_TOOL="TRAN";
	
	public final static String PAY_RETURN="PAY_RETURN";
	public final static String PAY_NOTIFY="PAY_NOTIFY";
	
	/**
	 * 短信类
	 */
	public final static String SMS_CODE_SUCCSEE="0";//发送成功
	public final static String SMS_CODE_FAIL="1";//发送失败
	public final static int SMS_TYPE_MANUAL=0;//手动发送
	public final static int SMS_CODE_AUTO=1;//自动发送
	public final static String SMS_STATE_WAIT="WAIT";//消息发送的默认状态
	
	/**
	 * spring定时任务的cron表达式的
	 */
	public static final String EVERY_HALF_MINUTE = "30 * * * * ?"; //每半分钟触发任务
	public static final String EVERY_ONE_MINUTE = "0 0/1 * * * ?"; //每一分钟触发任务
	public static final String EVERY_TOW_MINUTE = "0 0/2 * * * ?"; //每两分钟触发任务
	public static final String EVERY_FIVE_MINUTE = "0 0/5 * * * ?"; //每五分钟触发任务
	public static final String COMMIT_PRODUCTVIEW_TIME = "0 0/30 9-17 * * ?"; //朝九晚五工作时间内每半小时 
	public static final String PRODUCTVIEW_TIME = "0/30 * 8-23 * * ?";
	public static final String EVERY_MIDNIGHT = "0 0 1 * * ?";//每天凌晨

	/**
	 * 短信验证码有效时间：分钟
	 */
	public final static Integer SYS_TIME = 30;
	public final static Integer SYS_COUNT = 10;//一个IP一天内发短信的次数
	
	
	
	
	
	
	
	
	/**
	 * 用户来源(字典表：type=source)
	 */
	public final static int SOURCE_ANDROID=1;//安卓
	public final static int SOURCE_IOS=2;//ios
	public final static int SOURCE_PC=3;//pc端
	public final static int SOURCE_OTHER=4;//其他
	
	
	/**
	 * 用户vip开通类型(字典表：type=vipType)
	 */
	public final static int VIP_MONTH=1;//月卡(30天)
	public final static int VIP_QUARTER=2;//季卡(90天)
	public final static int VIP_HALF_YEAR=3;//半年(180天)
	public final static int VIP_ONE_YEAR=4;//一年(360天)
	
	/**
	 * 用户vip状态(字典表：type=vipStatus)
	 */
	public final static int NOT_VIP=1;//未开通
	public final static int IS_VIP=2;//开通中
	public final static int VIP_IS_OVER=3;//已过期
	
	/**
	 * 账户操作类型。记录user_account
	 */ 
	public final static int ACCOUNT_UPDATE_WITHDRAW_APPLY = 1;//提现申请
	public final static int ACCOUNT_UPDATE_WITHDRAW_SUCC = 2;//提现成功
	public final static int ACCOUNT_UPDATE_WITHDRAW_BACK=3;//提现退款
	public final static int ACCOUNT_UPDATE_CHALLENGE_PAY=4;//挑战金支付
	public final static int ACCOUNT_UPDATE_RECHARGE_SUCC=5;//充值成功
	public final static int ACCOUNT_UPDATE_BUY_VIP=6;//购买畅读卡
	
	/**
	 * 账变类型。记录account_record,对应字典表type：accountType
	 */
	public final static int ACCOUNT_RECORD_RECHARE = 1;//充值
	public final static int ACCOUNT_RECORD_WITHDRAW = 2;//提现
	public final static int ACCOUNT_RECORD_PAY_CHALLENGE=3;//支付挑战金
	public final static int ACCOUNT_RECORD_BUY_BOOK=4;//购买书籍
	public final static int ACCOUNT_RECORD_BUY_VIP=5;//购买畅读卡
	
	
	public final static String SHRIO_SESSION = ":ShrioSession";//session共享
	public final static String PRAISE_NUM = "_PRAISE_NUM";//点赞数量
	public final static String IS_PRAISE = "_IS_PRAISE";//是否点赞
	
	/**
	 * 用户性别(字典表：type=sex)
	 */
	public static final int USER_MALE=1;//男
	public static final int USER_FEMALE=2;//女
	
	/**
	 * 用户登录状态
	 */
	public static final int LOGIN_STATUS_LOGIN_OUT = 0;//未登录
	public static final int LOGIN_STATUS_LOGIN_IN = 1;//已登录
	
	/**
	 * 用户标签类型
	 */
	public static final int USER_TAG_PERSONAL = 0;//个人标签
	public static final int USER_TAG_PUBLIC = 1;//公共标签
	
	/**
	 * 书架中的书籍类型(字典表：type=bookshelfType)
	 */
	public static final int BOOKSHELF_BOOK = 1;//书籍
	public static final int BOOKSHELF_BOOK_DIVIDE_READ = 2;//好书拆读
	public static final int BOOKSHELF_NOTE_FACTORY = 3;//笔记工厂
	
	/**
	 * 书架书籍阅读状态(字典表：type=readStatus)
	 */
	public static final int BOOK_NO_READ = 1;//未读
	public static final int BOOK_READING = 2;//阅读中
	public static final int BOOK_READ_FINISH = 3;//已阅完
	
	/**
	 * 书籍是否在书架中
	 */
	public static final int BOOK_NOT_IN_BOOK_SHELF = 0;//未在书架中
	public static final int BOOK_IN_BOOK_SHELF = 1;//在书架中
	
	/**
	 * 是否热门书籍
	 */
	public static final int BOOK_IS_NOT_HOT = 0;//非热门
	public static final int BOOK_IS_HOT = 1;//热门
	
	/**
	 * 书籍/好书推荐分类(标签)(字典表：type=bookClassify)
	 */
	public static final int BOOK_CLASSIFY_ECONOMY_MANAGE = 1;//经管
	public static final int BOOK_CLASSIFY_MARKETING = 2;//营销
	public static final int BOOK_CLASSIFY_NET = 3;//互联网
	public static final int BOOK_CLASSIFY_JOB = 4;//职场
	public static final int BOOK_CLASSIFY_BIOGRAPHY = 5;//传记
	public static final int BOOK_CLASSIFY_ENCOURAGE = 6;//励志
	public static final int BOOK_CLASSIFY_PHILOSOPHY = 7;//哲学
	public static final int BOOK_CLASSIFY_PSYCHOLOGY = 8;//心理学
	public static final int BOOK_CLASSIFY_HISTORY = 9;//历史
	public static final int BOOK_CLASSIFY_FINANCE = 10;//金融
	public static final int BOOK_CLASSIFY_NOVEL = 11;//小说
	public static final int BOOK_CLASSIFY_OTHER = 12;//其他
	
	/**
	 * 笔记工厂分类(标签)(字典表：type=noteFactoryClassify)
	 */
	public static final int NOTE_FACTORY_BUSINESS_MODEL = 1;//商业模式
	public static final int NOTE_FACTORY_SYB = 2;//创业
	public static final int NOTE_FACTORY_INVEST = 3;//投融资
	public static final int NOTE_FACTORY_NET = 4;//互联网
	public static final int NOTE_FACTORY_MANAGE = 5;//管理
	public static final int NOTE_FACTORY_MARKETING = 6;//营销
	public static final int NOTE_FACTORY_JOB = 7;//职场
	public static final int NOTE_FACTORY_ENCOURAGE = 8;//励志
	public static final int NOTE_FACTORY_PHILOSOPHY = 9;//哲学
	public static final int NOTE_FACTORY_PSYCHOLOGY = 10;//心理学
	public static final int NOTE_FACTORY_HISTORY = 11;//历史
	public static final int NOTE_FACTORY_FINANCE = 12;//金融
	public static final int NOTE_FACTORY_TECHNOLOGY = 13;//科技
	
	/**
	 * 挑战结果(字典表：type=challengeResult)
	 */
	public static final int CHALLENGE_RESULT_WAITING = 0;//待加入，默认状态，需支付成功后才可加入
	public static final int CHALLENGE_RESULT_WILL_START = 1;//即将挑战
	public static final int CHALLENGE_RESULT_FAIL = 2;//挑战失败
	public static final int CHALLENGE_RESULT_IN_PROGRESS = 3;//挑战进行中
	public static final int CHALLENGE_RESULT_SUCC = 4;//挑战成功
	public static final int CHALLENGE_RESULT_DISABLED = 5;//挑战已失效
	
	/**
	 * 挑战状态(字典表：type=challengeStatus)
	 */
	public static final int CHALLENGE_WILL_WAITING = 0;//待发起，默认状态，无实际意义，字典表不录入
	public static final int CHALLENGE_WILL_START = 1;//即将开始
	public static final int CHALLENGE_FIGHTING = 2;//激战正酣
	public static final int CHALLENGE_FINISH = 3;//挑战结束
	public static final int CHALLENGE_DISABLED = 4;//挑战已失效
	
	/**
	 * 挑战金支付状态
	 */
	public static final int CHALLENGE_PAY_STATUS_WAITING = 1;//待支付
	public static final int CHALLENGE_PAY_STATUS_FAIL = 2;//支付失败
	public static final int CHALLENGE_PAY_STATUS_SUCC = 3;//支付成功
	
	/**
	 * 书籍是否在挑战中(字典表：type=challengeFlag)
	 */
	public static final int BOOK_NO_CHALLENGE = 0;//未在挑战中
	public static final int BOOK_CHALLENGING = 1;//挑战中
	
	/**
	 * 是否已购买(拆读书籍)
	 */
	public static final int DIVIDE_BOOK_NO_BUG = 0;//未购买
	public static final int DIVIDE_BOOK_IS_BUG = 1;//已购买
	
	/**
	 * 是否挑战发起人
	 */
	public static final int NOT_CHALLENGE_LEADER = 0;//非发起人
	public static final int CHALLENGE_LEADER = 1;//挑战发起人
	
	/**
	 * 挑战用户点赞
	 */
	public static final int CHALLENGE_PRAISE_CANCEL = 0;//取消 点赞
	public static final int CHALLENGE_PRAISE = 1;//点赞
	
	/**
	 * banner图使用状态
	 */
	public static final int BANNER_FORBID = 0;//禁止
	public static final int BANNER_ALLOW = 1;//使用
	
	/**
	 * 书单收藏状态
	 */
	public static final int BOOKLIST_COLLECT_CANCEL = 0;//取消收藏
	public static final int BOOKLIST_COLLECT = 1;//收藏
	
	/**
	 * 提现状态
	 */
	public static final int WITHDRAW_DEALING = 1;//提现处理中
	public static final int WITHDRAW_SUCC = 2;//提现成功
	public static final int WITHDRAW_FAIL = 3;//提现失败
	
	
	/**
	 * 充值状态
	 */
	public static final int RECHARGE_DEALING = 1;//充值处理中
	public static final int RECHARGE_SUCC = 2;//充值成功
	public static final int RECHARGE_FAIL = 3;//充值失败
	
	/**
	 * 消息类型
	 */
	public static final String MSG_JOIN_CHALLENGE = "join_challenge";//加入挑战
	public static final String MSG_CHALLENGE_THUMBSUP = "challenge_thumbsUp";//点赞
	public static final String MSG_CHALLENGE_START = "challenge_start";//挑战开始
	public static final String MSG_CHALLENGE_SUCC = "challenge_succ";//挑战成功
	public static final String MSG_CHALLENGE_FAIL = "challenge_fail";//挑战失败
	public static final String MSG_USER_WITHDRAW = "user_withdraw";//用户提现
	
	public static final String BOOK_READ_OVER = "_BOOK_OR_NOTE_IS_READ_OVER_";//已读完书签
	
	public static final String DEFAULT_USER_HEAD = "/head/user_head.png";//用户默认头像路径
	
	/**
	 * 藏书所属人类型
	 */
	public static final int OWNERTYPE_USER = 0; // 用户
	public static final int OWNERTYPE_BOOKBAR = 1; // 书吧
	
	
}


