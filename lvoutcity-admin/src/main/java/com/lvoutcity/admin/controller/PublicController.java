/**
 * 
 */
package com.lvoutcity.admin.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import com.alibaba.fastjson.JSONArray;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.lvoutcity.admin.controller.bean.UserBean;
import com.lvoutcity.admin.utils.LcBottonUtils;
import com.lvoutcity.core.OSS.ApiHttpClient;
import com.lvoutcity.core.api.LogDescription;
import com.lvoutcity.core.base.BaseController;
import com.lvoutcity.core.commonDefine.CommonSql;
import com.lvoutcity.core.util.Constants;
import com.lvoutcity.core.util.JsonData;
import com.lvoutcity.core.util.LvoutcityUtils;
import com.lvoutcity.model.NoPublic;
import com.lvoutcity.model.PictureGroup;
import com.lvoutcity.model.PictureList;
import com.lvoutcity.model.TourismClub;
import com.lvoutcity.model.User;

import io.rong.models.FormatType;
import io.rong.models.ImgTextMessage;
import io.rong.models.SdkHttpResult;
import io.rong.models.TxtMessage;

/**
 * @author wj
 *
 */
public class PublicController extends BaseController {

	/**
	 * 首页跳转
	 */
	public void index() throws Exception {
		// User user = getBackSession();
		// String clubId = user.getClubId();
		// setSessionAttr("clubId", clubId);
		setAttr("functBtn", LcBottonUtils.getButtonOptions("10009", getRequest()));
		setAttr("clubId", getSessionAttr("clubId"));
		render("indexPublic.html");
	}

	public void messageAdd() throws Exception {
		setAttr("clubId", getSessionAttr("clubId"));

		TourismClub tc = TourismClub.dao.findById(getSessionAttr("clubId").toString());
		setAttr("club", tc);
		String sql_pp = "select * from t_picture_group where club_id='" + getSessionAttr("clubId").toString()
				+ "' order by create_time desc";
		List<PictureGroup> pp_list = PictureGroup.dao.find(sql_pp);
		List<PictureList> list = null;
		if (pp_list != null && pp_list.size() > 0) {
			String sql = "select * from t_picture_list where group_id ='" + pp_list.get(pp_list.size() - 1).getId()
					+ "' order by create_time desc ";
			list = PictureList.dao.find(sql);
		}
		setAttr("pctList", list);
		setAttr("ppList", pp_list);
		setAttr("type", 1);
		NoPublic no = new NoPublic();
		setAttr("pub", no);
		render("messageEdit.html");
	}

	public void messageEdit() throws Exception {
		setAttr("clubId", getSessionAttr("clubId"));
		TourismClub tc = TourismClub.dao.findById(getSessionAttr("clubId").toString());
		setAttr("club", tc);
		String sql_pp = "select * from t_picture_group where club_id='" + getSessionAttr("clubId").toString()
				+ "' order by create_time desc";
		List<PictureGroup> pp_list = PictureGroup.dao.find(sql_pp);
		List<PictureList> list = null;
		if (pp_list != null && pp_list.size() > 0) {
			String sql = "select * from t_picture_list where group_id ='" + pp_list.get(pp_list.size() - 1).getId()
					+ "' order by create_time desc ";
			list = PictureList.dao.find(sql);
		}
		setAttr("pctList", list);
		setAttr("ppList", pp_list);
		NoPublic no = NoPublic.dao.findById(getPara(0));
		setAttr("type", 2);
		setAttr("pub", no);
		render("messageEdit.html");
	}

	public void messageShow() throws Exception {
		setAttr("clubId", getSessionAttr("clubId"));
		TourismClub tc = TourismClub.dao.findById(getSessionAttr("clubId").toString());
		setAttr("club", tc);
		String sql_pp = "select * from t_picture_group where club_id='" + getSessionAttr("clubId").toString()
				+ "' order by create_time desc";
		List<PictureGroup> pp_list = PictureGroup.dao.find(sql_pp);
		List<PictureList> list = null;
		if (pp_list != null && pp_list.size() > 0) {
			String sql = "select * from t_picture_list where group_id ='" + pp_list.get(pp_list.size() - 1).getId()
					+ "' order by create_time desc ";
			list = PictureList.dao.find(sql);
		}
		setAttr("pctList", list);
		setAttr("ppList", pp_list);
		NoPublic no = NoPublic.dao.findById(getPara(0));
		setAttr("type", 3);
		setAttr("pub", no);
		render("messageEdit.html");
	}

	@LogDescription("新增或修改公众消息")
	public void savePublic() throws Exception {
		NoPublic nopublic = getBean(NoPublic.class);
		if ("1".equals(getPara("type"))) {
			nopublic.setId(LvoutcityUtils.getUUID());
			nopublic.setCreateTime(new Date());
			nopublic.setCreateUser(getSessionAttr("current_user_id"));
			nopublic.save();
		} else {
			NoPublic.dao.findById(nopublic.getId()).set("title", nopublic.getTitle())
					.set("title_img", nopublic.getTitleImg()).set("content", nopublic.getContent())
					.set("update_user", getSessionAttr("current_user_id")).update();
		}

		renderJson("result", "success");
	}

	public void selectPublic() throws Exception {
		String sql = " from ( ";
		sql += CommonSql.PUBLIC_SELECT+" ,'1' as sort ";
		sql += " from t_no_public t1 where t1.id in(" + Constants.CLUB_INIT + ") "
				+ "and t1.club_id ='"+getSessionAttr("clubId")+"' ";
		sql += " union  (" + CommonSql.PUBLIC_SELECT+" ,'2' as sort ";

		sql += " FROM t_no_public t1  where 1 =1 and " + " t1.is_delete = '0'  ";
		if (getPara("title") != null && !getPara("title").isEmpty()) {
			sql += "and t1.title like '%" + getPara("title") + "%' ";
		}
		// if(LvoutcityUtils.isNotNull(getPara("clubId"))){
		if (LvoutcityUtils.isNotNull(getSessionAttr("clubId"))) {
			sql += "and t1.club_id = '" + getSessionAttr("clubId") + "' ";
		}
		sql += " ) ) t  order by t.sort asc,t.status asc,t.create_time desc ";
		Page page = Db.paginate((getDatatableStart() / getDatatableLength()) + 1, getDatatableLength(), " select * ",
				sql);
		renderDatatableFromDb(page);
	}

	/*
	 * 保存并发送
	 */
	@LogDescription("保存并发送消息")
	public void saveAndSendPublic() throws Exception {
		NoPublic nopublic = getBean(NoPublic.class);
		if ("1".equals(getPara("type"))) {
			nopublic.setId(LvoutcityUtils.getUUID());
			nopublic.setCreateTime(new Date());
			nopublic.setSendTime(new Date());
			nopublic.save();
		} else {
			NoPublic.dao.findById(nopublic.getId()).set("title", nopublic.getTitle())
					.set("title_img", nopublic.getTitleImg()).set("content", nopublic.getContent())
					.set("send_time", new Date()).update();
		}

		TourismClub tc = TourismClub.dao.findById(nopublic.getClubId());
		/*
		 * nopublic.setId(LvoutcityUtils.getUUID()); nopublic.setCreateTime(new
		 * Date()); nopublic.save();
		 */
		// 次数省略群发信息逻辑
		// 获取需要发送的对象
		List<User> userList = User.dao
				.find("select tu.id from t_user tu inner join t_user_member tm on " + "tu.id=tm.user_id ");
		List<String> toUserIds = new ArrayList<String>();
		for (User u : userList) {
			toUserIds.add(u.getId());
		}

		// 调用融云发送信息
		// ImgTextMessage msg = new ImgTextMessage(nopublic.getContent(),
		// nopublic.getTitle(), "");
		// SdkHttpResult result =
		// ApiHttpClient.publishMessage(getSessionAttr("clubId"), toUserIds,
		// msg, FormatType.json);
		String logo = Constants.DEFAULT_USER_AVATAR;
		;
		if (LvoutcityUtils.isNotNull(tc.getAvatar())) {
			logo = tc.getAvatar();
		}
		String clubName = tc.getClubName().trim();
		if (Constants.LVC_CLUB_ID.equals(tc.getId())) {
			clubName = "旅城公告";
		}
		// 调用融云发送信息
		ImgTextMessage msg = new ImgTextMessage(nopublic.getZhaiYao(), nopublic.getTitle(), nopublic.getTitleImg(),
				tc.getId() + "&clubId", LvoutcityUtils.getAppUrl() + nopublic.getId());
		SdkHttpResult result = ApiHttpClient.publishMessage(clubName + "&&" + logo, toUserIds, msg, FormatType.json);
		String sql = "update t_no_public set status = '1',send_time='"
				+ LvoutcityUtils.formateDate(Constants.DATE.DATE_ALL) + "' where id = '" + nopublic.getId() + "' ";
		Db.update(sql);
		renderJson("result", "success");
	}

	/**
	 * 删除
	 */
	@LogDescription("删除公众消息")
	public void deletePublic() throws Exception {
		String idList = getPara("idList");
		JSONArray data = JSONArray.parseArray(idList);
		String listData = "'0'";
		for (int i = 0; i < data.size(); i++) {
			listData += ",'" + data.get(i) + "'";
		}
		String sql = "update t_no_public set is_delete = '1' where id in (" + listData + ") ";
		Db.update(sql);
		renderJson("result", new JsonData("0", "删除成功！"));
	}

	/**
	 * 发送
	 */
	@LogDescription("发送消息")
	public void sendPublic() throws Exception {

		String idList = getPara("id");
		NoPublic no = NoPublic.dao.findById(idList);
		if (no != null && "1".equals(no.getStatus())) {
			renderJson("result", new JsonData("2", "发送过的消息不能重复发送！"));
			return;
		}
		TourismClub tc = TourismClub.dao.findById(no.getClubId());

		// 获取需要发送的对象
		List<User> userList = User.dao
				.find("select tu.id from t_user tu inner join t_user_member tm on " + "tu.id=tm.user_id ");
		List<String> toUserIds = new ArrayList<String>();
		// toUserIds.add("27d7da4e32644e4aaafda7211cf01a2b");
		for (User u : userList) {
			toUserIds.add(u.getId());
		}
		String logo = Constants.DEFAULT_USER_AVATAR;
		if (LvoutcityUtils.isNotNull(tc.getAvatar())) {
			logo = tc.getAvatar();
		}
		String clubName = tc.getClubName().trim();
		if (Constants.LVC_CLUB_ID.equals(tc.getId())) {
			clubName = "旅城公告";
		}
		// 调用融云发送信息
		ImgTextMessage msg = new ImgTextMessage(no.getZhaiYao(), no.getTitle(), no.getTitleImg(),
				tc.getId() + "&clubId", LvoutcityUtils.getAppUrl() + no.getId());
		SdkHttpResult result = ApiHttpClient.publishMessage(clubName + "&&" + logo, toUserIds, msg, FormatType.json);

		String sql = "update t_no_public set status = '1',send_time='"
				+ LvoutcityUtils.formateDate(Constants.DATE.DATE_ALL) + "' where id = '" + idList + "' ";
		Db.update(sql);
		renderJson("result", new JsonData("0", "发送成功！"));
	}

}
