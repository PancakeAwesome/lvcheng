/**
 * 
 */
package com.lvoutcity.admin.controller;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.alibaba.fastjson.JSONArray;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.upload.UploadFile;
import com.lvoutcity.admin.utils.LcBottonUtils;
import com.lvoutcity.core.OSS.OssClient;
import com.lvoutcity.core.api.LogDescription;
import com.lvoutcity.core.base.BaseController;
import com.lvoutcity.core.commonDefine.CommonSql;
import com.lvoutcity.core.util.JsonData;
import com.lvoutcity.core.util.LvoutcityUtils;
import com.lvoutcity.model.PictureGroup;
import com.lvoutcity.model.PictureList;
import com.lvoutcity.model.User;

/**
 * @author wj
 *
 */
public class PictureController extends BaseController{

	public void index()throws Exception{
		//clubid = 0bf70c3b07f84f33b0af86bde62e213a getSessionAttr("clubId")
		setAttr("functBtn", LcBottonUtils.getButtonOptions("10011",getRequest()));
		setAttr("clubId", getSessionAttr("clubId"));
		render("indexGroup.html");
	}
	
	public void selectPictureGroup()throws Exception{
//		PictureGroup pict = PictureGroup.dao.findById("");
//		TourismClub tc = pict.getTourismClub();
		
		String sql = " FROM t_picture_group tp left join t_tourism_club tb on"
				+ "   tp.club_id = tb.id where 1 =1  ";
		//if(!getPara("clubId").isEmpty()){
		if(LvoutcityUtils.isNotNull(getSessionAttr("clubId"))){
			sql += "and tp.club_id = '"+getSessionAttr("clubId") +"' ";
		}
		if(!getPara("groupName").isEmpty()){
			sql += "and tp.group_name like '%"+getPara("groupName").trim() +"%' ";
		}
		
		renderDatatableFromDb(Db.paginate((getDatatableStart() / getDatatableLength()) + 1, getDatatableLength(),
				CommonSql.GROUP_SELECT, sql+" order by tp.create_time desc" ));
		
	}
	
	public void selectGroupNameForValidate()throws Exception{
//		PictureGroup pict = PictureGroup.dao.findById("");
//		TourismClub tc = pict.getTourismClub();
		
		String sql = " FROM t_picture_group tp left join t_tourism_club tb on"
				+ "   tp.club_id = tb.id where 1 =1 and tp.is_delete='0'  ";
		if(LvoutcityUtils.isNotNull(getSessionAttr("clubId"))){
			sql += "and tp.club_id = '"+getSessionAttr("clubId") +"' ";
		}
		if(!getPara("groupName").isEmpty()){
			sql += "and tp.group_name = '"+getPara("groupName") +"' ";
		}
		
		PictureGroup pct = PictureGroup.dao.findFirst("select tp.* "+sql);
//		Page page = Db.paginate((getDatatableStart() / getDatatableLength()) + 1, getDatatableLength(),
//				CommonSql.GROUP_SELECT, sql+" order by t.create_time desc" );
		//renderDatatableFromDb(page);
		
		if(pct == null){
			renderJson("success","null");
		}else{
			renderJson("success",pct);
		}
	}
	
	/**
	 * 新增分组
	 */
	@LogDescription("新增素材分组")
	public void saveGroup()throws Exception{
		PictureGroup pct = getBean(PictureGroup.class);
		UUID ud = UUID.randomUUID();
		pct.setId(ud.toString().replace("-", ""));
		pct.setClubId(getSessionAttr("clubId"));
		pct.setCreateTime(new Date());
		pct.setCreateUser(getSessionAttr("current_user_id"));
		//user.setUserNo(0);
		pct.save();
		
		
		renderJson("success", "false");
	}
	
	/**
	 * 查询分组
	 */
	public void selectGroup()throws Exception{
			PictureGroup uct = PictureGroup.dao.findById(getPara("id"));
			if(uct == null){
				renderJson("success","null");
			}else{
				renderJson("success",uct);
			}
		
	}
	/**
	 * 保存分组
	 */
	public void updateGroup()throws Exception{
		PictureGroup pct = getBean(PictureGroup.class);
		//user.setUserNo(0);

		new PictureGroup().set("id", pct.getId())
		.set("club_id", getSessionAttr("clubId"))
		.set("group_name", pct.getGroupName())
		.set("update_user",getSessionAttr("current_user_id"))
		.update();
		
		
		renderJson("success", "false");
		
	}
	
	/**
	 * 图库管理页面跳转
	 */
	public void pictureShow()throws Exception{
		String sql = "select * from t_picture_list where group_id ='"+getPara(0)+"' order by create_time desc ";
		List<PictureList> list = PictureList.dao.find(sql);
		String size = "0";
		if(list!=null){
			size = list.size()+"";
		}
		setAttr("pctList", list);
		setAttr("cc",size);
		setAttr("clubId", getSessionAttr("clubId"));
		setAttr("groupId",getPara(0));
		render("pictureShow.html");
	}
	
	/**
	 * 图片上传
	 */
	@LogDescription("上传素材图片")
	public void pictureUpLoad()throws Exception{
		UploadFile ut = this.getFile();
		String id = LvoutcityUtils.getUUID();
		String groupId = getPara("groupId");
//		String clubId = getPara("clubId");
		String localFilePath = ut.getUploadPath()+"/"+ut.getOriginalFileName();
		String type="";
		String [] tyleList = ut.getOriginalFileName().split("\\.");
		if(tyleList!=null && tyleList.length>0){
			type = tyleList[tyleList.length-1];
		}
		OssClient oss = new OssClient();
		String url = oss.uploadFile(id+"."+type, localFilePath);
		
		// 新增数据
		PictureList pl = new PictureList();
		pl.setGroupId(groupId);
		pl.setId(id);
		pl.setCreateTime(new Date());
		pl.setCreateUser(getSessionAttr("current_user_id"));
		pl.setImgUrl(url);
		pl.setImgName(ut.getOriginalFileName());
		pl.setKey(id+"."+type);
		pl.save();
		
		renderJson("success","true");
	}
	
	/*
	 * 验证图片是否重名
	 */
	public void repeatName()throws Exception{
		
	}
	
	/**
	 * 删除图片
	 */
	@LogDescription("删除图库图片")
	public void deletePicture()throws Exception{
		String idList = getPara("idList");
		JSONArray data = JSONArray.parseArray(idList);
		String listData ="'0'";
		OssClient os = new OssClient();
		
		for(int i =0;i<data.size();i++){
			os.deleteObject(data.get(i).toString());
			listData += ",'"+data.get(i).toString().split("\\.")[0]+"'";
			//os.deleteObject("lvc866", data.get(i).toString());
		}
		String sql = "delete from t_picture_list where id in ("+listData+") ";
		Db.update(sql);
		renderJson("success", "true");
	}
	
	public void picRepo(){
		//String clubId= getPara("club_id");
		String clubId=getSessionAttr("clubId");
		String sql_pp = "select * from t_picture_group where club_id='"+clubId+"' order by create_time desc";
		List<PictureGroup> pp_list = PictureGroup.dao.find(sql_pp);
		setAttr("ppList", pp_list);
		setAttr("multiSelect", getPara("multiSelect"));
		setAttr("bind_field", getPara("bind_field"));
	}
	
	@LogDescription("删除素材分组")
	public void deleteGroup(){
		String idList = getPara("idList");
		JSONArray data = JSONArray.parseArray(idList);
		String listData ="'-1'";
		for(int i =0;i<data.size();i++){
			listData += ",'"+data.get(i)+"'";
		}
		String sql = "delete from t_picture_group where id in ("+listData+") ";
		Db.update(sql);
		renderJson("success", "false");
	}
}
