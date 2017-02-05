package com.lvoutcity.admin.controller;

import com.alibaba.druid.sql.builder.UpdateBuilder;
import com.alibaba.fastjson.JSONArray;
import com.jfinal.plugin.activerecord.Db;
import com.lvoutcity.core.api.LogDescription;
import com.lvoutcity.core.base.BaseController;
import com.lvoutcity.model.Area;
import com.lvoutcity.model.TourismClub;

public class new1 extends BaseController {
	
	@LogDescription("初始跳转页面")
	public void index() throws Exception{
		render("indexClub.html");
	}
	
	@LogDescription("获取俱乐部列表")
	public void getClubList(){
		String sql = "from t_tourism_club where is_delete = '1'";
		TourismClub tc = getBean(TourismClub.class);
		
//		增加查询条件
		if (tc.getClubNo() != null) {
			sql += "and club_no = '" + tc.getClubNo() + "'";
		}
		if(tc.getClubName()!=null &&!tc.getClubName().isEmpty()){
			sql += " and club_name like  '%"+tc.getClubName()+"%' ";
		}
		if(tc.getLegalPerson()!=null &&!tc.getLegalPerson().isEmpty()){
			sql += " and legal_person like '%"+tc.getLegalPerson()+"%' ";
		}
		
		renderDatatable(Db.paginate(getDatatableStart()/getDatatableLength()+1, getDatatableLength(), "select *", sql + "order by user_no desc"));
	}
	
	@LogDescription("增加俱乐部")
	public void addClub(){
		TourismClub tb = new TourismClub();
		setAttr("type", 1);
		render("club-add.html");
	}
	
	@LogDescription("查看俱乐部")
	public void clubDetail(){
		TourismClub tc = TourismClub.dao.findByClubId(getPara(0));
		setAttr("tc", tc);
		setAttr("type", 2);
		render("club-add.html");
	}
	
	@LogDescription("删除俱乐部")
	public void deleteClub(){
		String idList = getPara("idlist");
		JSONArray id = JSONArray.parseArray(idList);
//		组装sql语句
		String sqString = "'-1'";
		for (int i = 0; i < id.size(); i++) {
			sqString += ",'" + id.get(i) + "'";
		}
		String sql = "update t_tourism_club set is_delete = '0' in (" + sqString + ")";
		Db.update(sql);
		
		renderJson("success","false");
	}
	
	@LogDescription("编辑俱乐部")
	public void editCllub(){
		TourismClub tc = TourismClub.dao.findByClubId(getPara("id"));
		setAttr("type", 3);
		setAttr("tc", tc);
		render("add-club.html");
	}
	
	@LogDescription("更新俱乐部信息")
	public void updateClub(){
		TourismClub tc = getBean(TourismClub.class);
		TourismClub.dao.findById(tc.getId())
		.set("club_name", tc.getClubName().trim())
		.set("club_short_name", tc.getClubShortName())
		.set("legal_person", tc.getLegalPerson())
		.set("organization_code", tc.getOrganizationCode())
		.set("bank_account", tc.getBankAccount())
		.set("bank_name", tc.getBankName())
		.set("address", tc.getAddress())
		.set("contact_person", tc.getContactPerson())
		.set("contact_phone", tc.getContactPhone())
		.set("enabled", tc.getEnabled())
		.update();
		
		renderJson("success","false");
	}
	
	@LogDescription("俱乐部介绍")
	public void clubIntro(){
		String clubId = getPara("clubId");
	}
	
	@LogDescription("引入省市列表")
	public void areaList(){
		renderJson(Area.dao.find("select * from t_area where parent_id = ? order by id asc",getParaToInt(0, 1)));
	}
}
