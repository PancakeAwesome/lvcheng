/**
 * 
 */
package com.lvoutcity.admin.controller;


import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpSession;

import com.alibaba.fastjson.JSONArray;
import com.aliyun.oss.OSSClient;
import com.jfinal.aop.Before;
import com.jfinal.ext.interceptor.POST;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.upload.UploadFile;
import com.lvoutcity.core.OSS.OssClient;
import com.lvoutcity.core.api.LogDescription;
import com.lvoutcity.core.base.BaseController;
import com.lvoutcity.core.util.LvoutcityUtils;
import com.lvoutcity.model.PictureGroup;
import com.lvoutcity.model.PictureList;
import com.lvoutcity.model.TourismClub;
import com.lvoutcity.model.User;
import com.lvoutcity.model.UserBackgroup;

/**
 * @author wj
 *
 */
public class ClubController extends BaseController {
	

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	/**
	 * 主页跳转
	 */
	public void index() throws Exception{
		render("indexClub.html");
	}
	
	/**
	 * 俱乐部新增页面
	 */
	
	public void clubAdd()throws Exception{
		// 设置页面类型 1 是新增 2是查看  3 是编辑
		setAttr("cl", new TourismClub());
		setAttr("type",1);
		render("club-add.html");
	}
	
	/*
	 * 俱乐部介绍页面跳转
	 */
	public void clubDetail()throws Exception{
		// 暂时默认是clubid = 451217ccb4d34b52806302a6d1345052
		String clubId=getSessionAttr("clubId") ;

		if(LvoutcityUtils.isNull(clubId)){
			//跳转出错页面，返回
			render("../error/403.html");
			return;
		}
		TourismClub tc = TourismClub.dao.findById(clubId);
		setAttr("club", tc);
		String sql_pp = "select * from t_picture_group where club_id='"+tc.getId()+"' order by create_time desc";
		List<PictureGroup> pp_list = PictureGroup.dao.find(sql_pp);
		List<PictureList> list = null;
		if(pp_list!=null&& pp_list.size()>0){
			String sql = "select * from t_picture_list where group_id ='"+pp_list.get(0).getId()+"' order by create_time desc ";
			list = PictureList.dao.find(sql);
		}
		setAttr("pctList", list);
		setAttr("ppList", pp_list);
		setAttr("clubId", clubId);
		render("club-detail.html");
		
	}
	/**
	 * 俱乐部介绍查看
	 * @throws Exception
	 */
	public void clubDetailShow()throws Exception{
		// 暂时默认是clubid = 451217ccb4d34b52806302a6d1345052
	
		String clubId=getPara("clubId");

		TourismClub tc = TourismClub.dao.findById(clubId);
		setAttr("club", tc);
		String sql_pp = "select * from t_picture_group where club_id='"+tc.getId()+"' order by create_time desc";
		List<PictureGroup> pp_list = PictureGroup.dao.find(sql_pp);
		List<PictureList> list = null;
		if(pp_list!=null&& pp_list.size()>0){
			String sql = "select * from t_picture_list where group_id ='"+pp_list.get(pp_list.size()-1).getId()+"' order by create_time desc ";
			list = PictureList.dao.find(sql);
		}
		setAttr("pctList", list);
		setAttr("ppList", pp_list);
		setAttr("clubId", clubId);
		render("club-detailShow.html");
		
	}
	/**
	 * 新增或 保存俱乐部信息
	 */
	@Before(POST.class)
	@LogDescription("新增俱乐部信息")
	public void saveOrUpdate()throws Exception{
		TourismClub tc = getBean(TourismClub.class);
		// 生成uuid
		UUID ud = UUID.randomUUID();
		tc.setClubName(tc.getClubName().trim());
		tc.setId(ud.toString().replace("-", ""));
		tc.setCreateUser(getSessionAttr("current_user_id"));		
		renderJson(Ret.create("data", tc.save()));
		//OssClient os = new OssClient();
		//os.createBucket(ud.toString().replace("-", ""));
		renderJson("success", "false");
	}

	/**
	 * 查询俱乐部信息
	 */
	public void selectClub()throws Exception{
		String sql = " from t_tourism_club where is_delete = '1' ";
		TourismClub tc = getBean(TourismClub.class);
	
		if(tc.getClubNo()!=null  ){
			sql += " and club_no = "+tc.getClubNo();
		}
		if(tc.getClubName()!=null &&!tc.getClubName().isEmpty()){
			sql += " and club_name like  '%"+tc.getClubName()+"%' ";
		}
		if(tc.getLegalPerson()!=null &&!tc.getLegalPerson().isEmpty()){
			sql += " and legal_person like '%"+tc.getLegalPerson()+"%' ";
		}
		/*Page pg = TourismClub.dao.paginate((getDatatableStart() / getDatatableLength()) + 1, getDatatableLength(),
				"select * ", sql+" order by " + getDatatableOrderCol() + " " + getDatatableOrderDir());*/
		renderDatatable(TourismClub.dao.paginate((getDatatableStart() / getDatatableLength()) + 1, getDatatableLength(),
				"select * ", sql+" order by club_no asc	 "));

		
	}
	
	/**
	 * 俱乐部的具体信息查看
	 */
	public void clubShow()throws Exception{
		 
		//System.out.println(request.getParameter("id").toString());
		TourismClub tc = TourismClub.dao.findById(getPara(0));
		setAttr("cl", tc);
		// 设置页面类型 1 是新增 2是查看  3 是编辑
		setAttr("type",2);
		render("club-add.html");
	}

	/**
	 * 俱乐部信息编辑
	 */
	public void clubEdit()throws Exception{
		 
		//System.out.println(request.getParameter("id").toString());
		TourismClub tc = TourismClub.dao.findById(getPara(0));
		setAttr("cl", tc);
		// 设置页面类型 1 是新增 2是查看  3 是编辑
		setAttr("type",3);
		render("club-add.html");
	}
	
	/**
	 * 俱乐部删除,逻辑删除
	 */
	@LogDescription("删除俱乐部")
	public void deleteClub()throws Exception{
		String idList = getPara("idList");
		JSONArray data = JSONArray.parseArray(idList);
		String listData ="'-1'";
		for(int i =0;i<data.size();i++){
			listData += ",'"+data.get(i)+"'";
		}
		String sql = "update  t_tourism_club set is_delete ='0'  where id in ("+listData+") ";
		Db.update(sql);
//		String sql = "delete from t_tourism_club where id in ("+listData+") ";
//		Db.update(sql);
		renderJson("success", "false");
	}
	
	/**
	 * 俱乐部更新
	 */
	@Before(POST.class)
	@LogDescription("修改俱乐部")
	public void updateClub()throws Exception{
		//new TourismClub().set("", "").update();
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
		//getBean(TourismClub.class).save();
		renderJson("success", "false");
	}
	
	/**
	 * 启用禁用更新
	 */
	@LogDescription("更改俱乐部状态")
	public void upateEnable()throws Exception{
		//new TourismClub().set("", "").update();
		TourismClub.dao.findById(getPara("id")).set("enabled", getPara("enabled")).update();
		//getBean(TourismClub.class).save();
		renderJson("success", "false");
	}
	
	public void clubLogo()throws Exception{
		
		UploadFile ut = this.getFile();
		String clubId = getPara("clubLogo");
		String localFilePath = ut.getUploadPath()+"/"+ut.getOriginalFileName();
		System.out.println("localFilePath===="+localFilePath);
		String type="";
		String [] tyleList = ut.getOriginalFileName().split("\\.");
		if(tyleList!=null && tyleList.length>0){
			type = tyleList[tyleList.length-1];
		}
		//System.out.println(ut.getContentType());
		OssClient os = new OssClient();
		String url =  os.uploadFile(LvoutcityUtils.getUUID()+"."+type, localFilePath);
		
		renderJson("url",url);
	}
	public void clubPicture()throws Exception{
		
		UploadFile ut = this.getFile();
		String clubId = getPara("clubPicture");
		String localFilePath = ut.getUploadPath()+"/"+ut.getOriginalFileName();
		String type="";
		String [] tyleList = ut.getOriginalFileName().split("\\.");
		if(tyleList!=null && tyleList.length>0){
			type = tyleList[tyleList.length-1];
		}
//		type
		OssClient oss = new OssClient();
		String url = oss.uploadFile(LvoutcityUtils.getUUID()+"."+type, localFilePath);
		renderJson("url",url);
	}
	
	@LogDescription("更改俱乐部介绍")
	public void updateDetail()throws Exception{
		TourismClub tc = getBean(TourismClub.class);
		TourismClub.dao.findById(tc.getId())
		.set("avatar", tc.getAvatar())
		.set("bg_img", tc.getBgImg())
		.set("intro", tc.getIntro())
		.update();
		//getBean(TourismClub.class).save();
		renderJson("success", "false");
	}
	
	public void groupAndList()throws Exception{
		List<PictureList> list = null;
		if(getPara(0)!=null&& !getPara(0).isEmpty()){
			String sql = "select * from t_picture_list where group_id ='"+getPara(0)+"' order by create_time desc ";
			list = PictureList.dao.find(sql);
		}
		setAttr("pctList", list);
		render("groupAndList.html");
	}
	
	/**
	 * 从图库获取图片url
	 */
	public void updateClubPicture()throws Exception{
		String id = getPara("id");
		String clubId = getPara("clubId");
		PictureList pt = PictureList.dao.findById(id);
		
		//TourismClub.dao.findById(clubId).set("bg_img", pt.getImgUrl()).update();
		renderJson("url",pt.getImgUrl());
	}
	
	
	
}
