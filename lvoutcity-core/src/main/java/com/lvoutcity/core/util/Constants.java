package com.lvoutcity.core.util;

/**
 * 
 * @ClassName: Constants
 * @Description: 静态变量类
 * @author James w.s
 * @date 2016年5月16日 下午2:24:12
 *
 */
public class Constants {

	public static final String ISDELETE_TRUE = "0"; // 删除

	public static final String ISDELETE_FALSE = "1"; // 没有删除

	public static final String MAP_STATR_IMG = ""; // 起点图片

	public static final String MAP_END_IMG = ""; // 终点图片

	public static final String MAP_TIME_IMG = ""; // 天数图片

	public static final String MAP_ATTR_IMG = ""; // 景点图片

	public static final String MAP_RENDEZVOUS_IMG = ""; // 集合点图片

	public static final String DEFAULT_PERMISSION = "lvcPermission"; // 权限设置

	public static final String SYS_USER = "SysUser"; // 系统用户
	
	public static final String SYS_USER_BACK = "SysUserBack"; //默认系统用户数据

	public static final String DEFAULT_ADMIN = "admin"; // 默认系统用户

	public static final String TOP_LEVEL_MENU = "0";	//默认字符串
	
	public static final String LOG_PATH = "C:\\";								//日志处理
	
	public static final String SIGN_TYPE ="MD5";								//加密方式
	
	public static final String CHAT_SET = "UTF-8";								//加密字符
	
	public static final String KEY = "7t7eqw3vmm9vcpg6khigp7e0t6obljxq";		//MD5加密KEY
	
	public static final String DEFAULT_USER_AVATAR = "http://lvc998.oss-cn-hangzhou.aliyuncs.com/avatar/null_green.png"; //默认用户头像url
	
	public static final String MINI_AVATAR_STYLE = "1e_1c_0o_0l_60h_60w_90q.src"; //
	public static final String USER_AVATAR_STYLE = "128w_128h"; //默认用户头像url
	
	
	public static final String ROUTE_TYPE_SHARE="2";           //线路分享      标记
	public static final String ROUTE_TYPE_COLLECT="1";         //线路收藏      标记
	public static final String ROUTE_TYPE_CONCERN="0";         //线路关注      标记
	public static final String ROUTE_TYPE_CANCEL="0";          //取消分享
	

	public static final class ACCOUNT {
		public static final String STATUS_NOT_CONFIRMED= "0"; // 未确认
		public static final String STATUS_CONFIRMED = "1"; // 已确认
		public static final String STATUS_CLEAR = "2"; // 已结算
	};
	
	public static final class ORDER {
		public static final String STATUS_UNPAID = "0"; // 待支付
		public static final String STATUS_PAID = "1"; // 支付完成
		public static final String STATUS_ON_TRIP = "2"; // 行程中
		public static final String STATUS_TRIP_FINISH = "3"; // 行程结束
		public static final String STATUS_REFUND_APPLY = "4"; // 退款申请
		public static final String STATUS_CANCLED_USER = "5"; // 订单取消（用户）
		public static final String STATUS_CANCLED_BACK = "6"; // 订单取消（后台）
		
		
		public static final String ACCOUNT_STATUS_NOT_ACCOUNTED = "0"; // 未出账订单
		public static final String ACCOUNT_STATUS_ACCOUNTED = "1"; // 已出账订单
		
		
	};

	public static final class ROUTE {
		public static final String STATUS_CREATED = "0"; // 待提交
		public static final String STATUS_SUBMITED = "1"; // 待审核
		public static final String STATUS_ON_SHELF = "2"; // 已上架
		public static final String STATUS_OFF_SHELF = "3"; // 已下架
	}

	public static final class CORP {
		public static final String STATUS_CREATED = "0"; // 待提交
		public static final String STATUS_SUBMITED = "1"; // 待审核
		public static final String STATUS_ON_SHELF = "2"; // 已上架
		public static final String STATUS_OFF_SHELF = "3"; // 已下架
	}
	
	public static final class WALLET {
		public static final String STATUS_PRE_IN = "1"; // 预收入
		public static final String STATUS_IN = "2"; // 已收入
		public static final String STATUS_OUT = "3"; // 已支出
		//public static final String STATUS_PRE_OUT = "4"; // 预支出
	}
	
	public static final class PAY {
		public static final String TYPE_ALIPAY = "1"; // 支付宝
		public static final String TYPE_WXPAY = "2"; //微信支付
		public static final String TYPE_GOLD = "3"; // 金币支付
		public static final String STATUS_SUCCESS = "1"; // 成功
		public static final String STATUS_FAIL = "0"; // 失败
		public static final String OPERATION_PAY = "0"; // 支付
		public static final String OPERATION_REFUND = "1"; // 退款
		
		
	}
	public static final class PICTURE {
		public static final String ACTIVITY = "http://lvc998.oss-cn-hangzhou.aliyuncs.com/activity.jpg"; 
		public static final String SERVICE = "http://lvc998.oss-cn-hangzhou.aliyuncs.com/sevice.jpg";
		public static final String LOGO = "http://lvc998.oss-cn-hangzhou.aliyuncs.com/logo_28.png"; 
	}
	
	public static final class PUSHMESSAGE{
		public static final String  OBJECTNAME_CMDNTF = "RC:CmdNtf";  //文本消息
		public static final String  OBJECTNAME_TEX = "RC:TxtMsg";  //文本消息
		public static final String  OBJECTNAME_IMG = "RC:ImgMsg";  //图片消息RC:CmdNtf
		public static final String  OBJECTNAME_VC  = "RC:VcMsg";  //语音消息
		public static final String  OBJECTNAME_IMGTEXT = "RC:ImgTextMsg";  //图文消息
		public static final String  OBJECTNAME_LBS="RC:LBSMsg";  //位置消息  
		public static final String  OBJECTNAME_ContactNtf="RC:ContactNtf";  //添加联系人消息
		public static final String  OBJECTNAME_InfoNtf="RC:InfoNtf";  //提示条（小灰条）通知消息
	}
	
	public static final class DATE{
		public static final String  DATE_ALL = "yyyy-MM-dd HH:mm:ss";  //
		public static final String  DATE_MONTH = "yyyy-MM";  //
		public static final String  DATE_DAY = "yyyy-MM-dd";  
		public static final String  DATE_EXPORT = "yyyyMMddHHmmss";  //
	}
	public static final class personInfo{
		public static final String MEMBER_LEVEL_NORMAL = "普通会员";
		public static final String MEMBER_LEVEL_SILVER = "银牌会员";
		public static final String MEMBER_LEVEL_GOLD = "金牌会员";
		public static final String MEMBER_LEVEL_DIAMOND = "钻石会员";
	}
	public static final class ACTIVITY{
		public static final String TARGET_REGISTER = "0";
		public static final String TARGET_INVITER = "1";
	}
	// 内置超级管理员旅城号
	public static final int ADMIN_NO =1000000000;
	// 内置超级管理员id
	public static final String ADMIN_ID ="01234567890123456789012345678901";
	public static final String ADMIN_SERVICE ="01234567890123456789012345678902";

	//默认旅城ID
	public static final String LVC_CLUB_ID = "01234567890123456789012345678901";
	
	public static final class CLUB{
		public static final String INIT_1 = "00000000000000000000000000000001";
		public static final String INIT_2 = "00000000000000000000000000000002";
	}
	
	public static final class GROUP{
		public static final String CHAT_GROUP = "0";
		public static final String CORP_GROUP = "1";
		public static final String GUIDE_GROUP = "2";
	}
	public static final class GROUP_USER{
		public static final String MEMBER = "0";
		public static final String ADMIN = "1";
	}
	
	public static final String CLUB_INIT ="'00000000000000000000000000000001','00000000000000000000000000000002'";
	//默认旅城code
	public static final String LVC_CLUB_CODE="0";
	
	public static final String SUCCESS_CODE="success";
	
	public static final String COOKIE_NAME="session_id";
	// session 有效时间
	public static final int SESSION_TIME=1800;
	
	public static final int ROW_NUMBER = 2000;
	
	//活动阶段
	public static final int STAGE_REG = 1;
	public static final int STAGE_LOGIN = 2;
}
