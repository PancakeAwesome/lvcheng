package com.lvoutcity.core.OSS;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.rong.models.FormatType;
import io.rong.models.ImgTextMessage;
import io.rong.models.MsgObj;
import io.rong.models.PlatformNotification;
import io.rong.models.PushMessage;
import io.rong.models.PushNotification;
import io.rong.models.SdkHttpResult;
import io.rong.models.TagObj;
import io.rong.models.TxtMessage;
/**
 * 一些api的调用示例
 */
public class Example {

	/**
	 * 本地调用测试
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {

		String key = "pwe86ga5eb2o6";//替换成您的appkey
		String secret = "jFvRjezbiBNZ";//替换成匹配上面key的secret
		String tempid = "eSOFfF1aQMeaDL9IPaaFTv";
		SdkHttpResult result = null;
		List<String> lt = new ArrayList<>();
		lt.add("27d7da4e32644e4aaafda7211cf01a2b");
		lt.add("584b3f3ee615428896d14bc5d57ef749");
		ImgTextMessage info = new ImgTextMessage("我是一只 小小的鸟儿，自由的飞翔我是一只 小小的鸟儿，自由的飞翔我是一只 小小的鸟儿，自由的飞翔我是一只 小小的鸟儿，自由的飞翔我是一只 小小的鸟儿，自由的飞翔我是一只 小小的鸟儿，自由的飞翔我是一只 小小的鸟儿，自由的飞翔我是一只 小小的鸟儿，自由的飞翔我是一只 小小的鸟儿，自由的飞翔我是一只 小小的鸟儿，自由的飞翔我是一只 小小的鸟儿，自由的飞翔我是一只 小小的鸟儿，自由的飞翔我是一只 小小的鸟儿，自由的飞翔我是一只 小小的鸟儿，自由的飞翔我是一只 小小的鸟儿，自由的飞翔","huhu","http://0bf70c3b07f84f33b0af86bde62e213a.oss-cn-hangzhou.aliyuncs.com/383b1e79cf3b49ab8cf8622492bd99a3.jpg");
		//result = ApiHttpClient.sendCode("15295552013", "eSOFfF1aQMeaDL9IPaaFTv", FormatType.json);
		result = ApiHttpClient.publishMessage("xx 俱乐部",
				lt, info, "pushContent",
				"pushData", FormatType.json);
		System.out.println("sendCode=" + result);
		System.out.println(result.getHttpCode());
		System.out.println(result.getResult());
		//获取token
//		result = ApiHttpClient.getToken("402880ef4a", "asdfa",
//				"http://aa.com/a.png", FormatType.json);
//		System.out.println("gettoken=" + result);
//		
//		//发消息(push内容为消息内容)
//		List<String> toIds = new ArrayList<String>();
//		toIds.add("id1");
//		toIds.add("id2");
//		toIds.add("id3");
//		result = ApiHttpClient.publishMessage( "fromUserId", toIds,
//				new TxtMessage("txtMessagehaha"), FormatType.json);
//		System.out.println("publishMessage=" + result);
//		
//		//发消息 语音类型
//		result = ApiHttpClient.publishMessage("fromUserId", toIds,
//				new VoiceMessage("txtMessagehaha", 100L), FormatType.json);
//		System.out.println("publishMessage=" + result);
//		
//		//发消息 图片类型
//		result = ApiHttpClient.publishMessage( "fromUserId", toIds,
//				new ImgMessage("txtMessagehaha", "http://aa.com/a.png"),
//				FormatType.json);
//		System.out.println("publishMessage=" + result);
//		
//		//发消息(push内容为填写内容)
//		result = ApiHttpClient.publishMessage( "fromUserId", toIds,
//				new TxtMessage("txtMessagehaha"), "pushContent", "pushData",
//				FormatType.json);
//		System.out.println("publishMessageAddpush=" + result);
//		
//		//发送系统消息
//		result = ApiHttpClient.publishSystemMessage( "fromUserId",
//				toIds, new TxtMessage("txtMessagehaha"), "pushContent",
//				"pushData", FormatType.json);
//		System.out.println("publishSystemMessage=" + result);
//		
//		//创建聊天室
//		List<ChatroomInfo> chats = new ArrayList<ChatroomInfo>();
//		chats.add(new ChatroomInfo("idtest", "name"));
//		chats.add(new ChatroomInfo("id%s+-{}{#[]", "name12312"));
//		result = ApiHttpClient.createChatroom( chats,
//				FormatType.json);
//		System.out.println("createchatroom=" + result);
//		List<String> chatIds = new ArrayList<String>();
//		chatIds.add("id");
//		chatIds.add("id%+-:{}{#[]");
//		result = ApiHttpClient.queryChatroom( chatIds,
//				FormatType.json);
//		System.out.println("queryChatroom=" + result);
//		
//		//发送聊天室消息
//		result = ApiHttpClient.publishChatroomMessage(
//				"fromUserId", chatIds, new TxtMessage("txtMessagehaha"),
//				FormatType.json);
//		System.out.println("publishChatroomMessage=" + result);
//		
//		//销毁聊天室
//		result = ApiHttpClient.destroyChatroom( chatIds,
//				FormatType.json);
//		System.out.println("destroyChatroom=" + result);
//		List<GroupInfo> groups = new ArrayList<GroupInfo>();
//		groups.add(new GroupInfo("id1", "name1"));
//		groups.add(new GroupInfo("id2", "name2"));
//		groups.add(new GroupInfo("id3", "name3"));
//		result = ApiHttpClient.syncGroup( "userId1", groups,
//				FormatType.json);
//		System.out.println("syncGroup=" + result);
//		
//		//加入群
//		result = ApiHttpClient.joinGroup( "userId2", "id1",
//				"name1", FormatType.json);
//		System.out.println("joinGroup=" + result);
//		
//		//批量加入群
//		List<String> list = new ArrayList<String>();
//		list.add("userId3");
//		list.add("userId1");
//		list.add("userId3");
//		list.add("userId2");
//		list.add("userId3");
//		list.add("userId3");
//		result = ApiHttpClient.joinGroupBatch( list, "id1",
//				"name1", FormatType.json);
//		System.out.println("joinGroupBatch=" + result);
//		
//		//发送群消息
//		result = ApiHttpClient.publishGroupMessage("userId1",
//				chatIds, new TxtMessage("txtMessagehaha"), "pushContent",
//				"pushData", FormatType.json);
//		System.out.println("publishGroupMessage=" + result);
//		
//		//退出群
//		result = ApiHttpClient.quitGroup( "userId1", "id1",
//				FormatType.json);
//		System.out.println("quitGroup=" + result);
//		
//		//批量退出群
//		result = ApiHttpClient.quitGroupBatch( list, "id1",
//				FormatType.json);
//		System.out.println("quitGroupBatch=" + result);
//		
//		//解散群
//		result = ApiHttpClient.dismissGroup( "userIddismiss",
//				"id1", FormatType.json);
//		
//		//获取消息历史记录下载地址
//		result = ApiHttpClient.getMessageHistoryUrl( "2014112811",
//				FormatType.json);
//		System.out.println("getMessageHistoryUrl=" + result);
//		
//		//删除历史记录(只是删除了历史记录，不会删除消息)
//		result = ApiHttpClient.deleteMessageHistory( "2014122811",
//		FormatType.json);
//		System.out.println("deleteMessageHistory=" + result);
//		
//		//封禁用户相关操作**********begin**********封禁用户相关操作//
//		result = ApiHttpClient.blockUser("2", 10,FormatType.json);
//		System.out.println("blockUser=" + result);
//		//封禁用户
//		result = ApiHttpClient.blockUser( "id2", 10,FormatType.json);
//		System.out.println("blockUser=" + result);
//		result = ApiHttpClient.blockUser( "id3", 10,FormatType.json);
//		System.out.println("blockUser=" + result);
//		//查询封禁用户
//		result = ApiHttpClient.queryBlockUsers( FormatType.json);
//		System.out.println("queryBlockUsers=" + result);
//		//解除封禁
//		result = ApiHttpClient.unblockUser( "id1", FormatType.json);
//		System.out.println("unblockUser=" + result);
//		result = ApiHttpClient.queryBlockUsers( FormatType.json);
//		System.out.println("queryBlockUsers=" + result);
//		result = ApiHttpClient.unblockUser( "id2", FormatType.json);
//		System.out.println("unblockUser=" + result);
//		result = ApiHttpClient.unblockUser("id3", FormatType.json);
//		System.out.println("unblockUser=" + result);
//		result = ApiHttpClient.queryBlockUsers( FormatType.json);
//		System.out.println("queryBlockUsers=" + result);
//		//封禁用户相关操作**********end**********封禁用户相关操作//
//		
//		//检查用户在线状态
//		result = ApiHttpClient.checkOnline( "143", FormatType.json);
//		System.out.println("checkOnline=" + result);
//		
//		//添加黑名单
//		List<String> toBlackIds = new ArrayList<String>();
//		toBlackIds.add("22");
//		toBlackIds.add("12");
//		result = ApiHttpClient.blackUser( "3706", toBlackIds,
//				FormatType.json);
//		System.out.println("blackUser=" + result);
//		
//		//查询黑名单
//		result = ApiHttpClient.QueryblackUser( "3706",FormatType.json);
//		System.out.println("QueryblackUser=" + result);
//		
//		//解除黑名单
//		result = ApiHttpClient.unblackUser( "3706", toBlackIds,
//				FormatType.json);
//		System.out.println("unblackUser=" + result);
//		
//		result = ApiHttpClient.QueryblackUser( "3706",FormatType.json);
//		System.out.println("QueryblackUser=" + result);	
//		
//		//刷新群信息
//		result = ApiHttpClient.refreshGroupInfo("id1", "name4",
//				FormatType.json);
//		System.out.println("refreshGroupInfo=" + result);
//		
//		PushMessage message = createPushMessage();//融云消息
//		//PushMessage message = createPushMessage2();//不落地push
//		result = ApiHttpClient.push( message, FormatType.json);
//		System.out.println("push=" + result);
//		//给用户打标签
//		UserTag tag = new UserTag();
//		tag.setTags(new String[] { "a", "b" });
//		tag.setUserId("userId11111");
//		result = ApiHttpClient.setUserTag( tag, FormatType.json);
//		System.out.println("tag=" + result);

	}

	/**
	 * 创建发送落地消息的内容
	 */
	private static PushMessage createPushMessage()
			throws UnsupportedEncodingException {
		List<String> osList = new ArrayList<>();
		osList.add("ios");
		osList.add("android");

		TagObj tag = new TagObj();
		tag.setIs_to_all(false);//给全量用户发 设置为true则下面标签和userids将无效
		
		//给打标签的发则设置标签
		List<String> tagas = new ArrayList<String>();
		tagas.add("a");
		tagas.add("b");
		tagas.add("3");
		tag.setTag(tagas);
		
		//给特定用户ID发,优先级高于上面的标签
		List<String> tagus = new ArrayList<String>();
		tagus.add("123");
		tagus.add("456");
		tag.setUserid(tagus);
		
		PushMessage pmsg = new PushMessage();
		pmsg.setPlatform(osList);
		PushNotification noti = new PushNotification();
		noti.setAlert("ddd");
		noti.setAndroid(createPush());
		noti.setIos(createPush());
		pmsg.setNotification(noti);
		
		//下面两个参数的有无 控制发送的落地消息还是不落地的push
		//PushMessage实体中的 fromuserid 和 message为null即为不落地的push
		pmsg.setFromuserid("fromuseId1");
		MsgObj msg = new MsgObj();
		TxtMessage message = new TxtMessage("hello", "one extra");
		msg.setContent(message.toString());
		msg.setObjectName(message.getType());
		pmsg.setMessage(msg);
		//上面两个参数的有无 控制发送的落地消息还是不落地的push
		
		pmsg.setAudience(tag);
		System.out.println(pmsg.toString());
		return pmsg;
	}
	
	/**
	 * 创建发送不落地的push内容
	 */
	private static PushMessage createPushMessage2()
			throws UnsupportedEncodingException {
		List<String> osList = new ArrayList<>();
		osList.add("ios");
		osList.add("android");

		TagObj tag = new TagObj();
		tag.setIs_to_all(false);//给全量用户发 设置为true则下面标签和userids将无效
		
		//给打标签的发则设置标签
		List<String> tagas = new ArrayList<String>();
		tagas.add("a");
		tagas.add("b");
		tagas.add("3");
		tag.setTag(tagas);
		
		//给特定用户ID发,优先级高于上面的标签
		List<String> tagus = new ArrayList<String>();
		tagus.add("123");
		tagus.add("456");
		tag.setUserid(tagus);
		
		PushMessage pmsg = new PushMessage();
		pmsg.setPlatform(osList);
		PushNotification noti = new PushNotification();
		noti.setAlert("ddd");
		noti.setAndroid(createPush());
		noti.setIos(createPush());
		pmsg.setNotification(noti);
		
		pmsg.setAudience(tag);
		System.out.println(pmsg.toString());
		return pmsg;
	}

	private static PlatformNotification createPush() {
		PlatformNotification data = new PlatformNotification();
		data.setAlert("override alert");
		Map<String, String> map = new HashMap<>();
		map.put("id", "1");
		map.put("name", "2");
		data.setExtras(map);
		return data;
	}
}
