package com.lvoutcity.admin.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.jfinal.aop.Before;
import com.jfinal.ext.interceptor.POST;
import com.jfinal.plugin.activerecord.Record;
import com.lvoutcity.admin.utils.LcBottonUtils;
import com.lvoutcity.core.api.LogDescription;
import com.lvoutcity.core.base.BaseController;
import com.lvoutcity.core.util.Constants;
import com.lvoutcity.core.util.LvoutcityUtils;
import com.lvoutcity.model.Guide;
import com.lvoutcity.model.Itinerary;
import com.lvoutcity.model.RouteImg;
import com.lvoutcity.model.RouteMag;
import com.lvoutcity.model.TagLine;
import com.lvoutcity.model.TagMag;

public class RouteController extends BaseController {

	
	
	public void index() {
		setAttr("functBtn", LcBottonUtils.getButtonOptions("10014",getRequest()));
	}

	/**
	 *  线路列表查询
	 */
	@Before(POST.class)
	public void routes() {
		HashMap<String, String> params = new HashMap<String, String>();
		if (getPara("guideNo") != null && !getPara("guideNo").isEmpty())
			params.put("guide_no", getPara("guideNo"));
		if (getPara("routeNo") != null && !getPara("routeNo").isEmpty())
			params.put("route_no", getPara("routeNo"));
		if (getPara("status") != null && !getPara("status").isEmpty())
			params.put("rm.status", getPara("status"));
		if (getPara("clubId") != null && !getPara("clubId").isEmpty())
			params.put("rm.club_id", getPara("clubId"));
		
		renderDatatable(RouteMag.dao.conditionedPaginate(params, (getDatatableStart() / getDatatableLength()) + 1,
				getDatatableLength(), getDatatableOrderCol(), getDatatableOrderDir()));
	}
	

	/**
	 *  线路列表查询
	 */
	@Before(POST.class)
	public void routesForFront() {
		HashMap<String, String> params = new HashMap<String, String>();
		if (LvoutcityUtils.isNotNull(getSessionAttr("clubId")) && !Constants.LVC_CLUB_ID.equals(getSessionAttr("clubId")))
			params.put("rm.club_id", getSessionAttr("clubId"));
		
		renderDatatable(RouteMag.dao.conditionedPaginate(params,  1,
				5, getDatatableOrderCol(), getDatatableOrderDir()));
	}

	/**
	 *  根据攻略编号查询攻略名称
	 */
	public void requireGuideName() {
		renderJson("data",Guide.dao.findByNoAndClub(getPara("guideNo"),getSessionAttr("clubId")));
	}

	
	/**
	 * 　新建线路
	 */
	@LogDescription("新增线路")
	public void create() {
		try {
			if(RouteMag.nameExisted(getPara("route_name"),null)){
				renderJson("fail","线路名称重复！");
				return;
			}
			RouteMag record = new RouteMag();
			record.setClubId(getPara("club_id"));
			record.setGuideId(getPara("guide_id"));
			record.setStatus("0");
			record.setId(LvoutcityUtils.getUUID());
			record.setRouteName(getPara("route_name"));
			record.setCreateUser(getCurrentUserId());
			record.save();
			Guide guide = Guide.dao.findById(getPara("guide_id"));
			int duration = Integer.valueOf(guide.getDuration());
			int night = Integer.valueOf(guide.getNight());
			int dayCount = duration; //暂定以天为准
			//int dayCount = duration>night?duration:night;
			for(int i=0;i<dayCount;i++){
				Itinerary iti = new  Itinerary();
				iti.setId(LvoutcityUtils.getUUID());
				iti.setRouteId(record.getId());
				iti.setOrderBy(i+1);
				iti.save();
			}
			renderJson("routeId",record.getId());
		} catch (Exception e) {
			e.printStackTrace();
			renderJson("result",e.getMessage());
		}
	}
	
	/**
	 *  跳转编辑页面
	 */
	public void edit(){
		try {
			String id = getPara("id");
			String type = getPara("type");
			if(type.equals("2")){ //copy
				 String newId = copyRoute(id);
				 Record route = RouteMag.dao.searchWithGuide(newId);
				 setAttr("route",JSON.parse(route.toJson()));
				 setAttr("tagList",copyTag(id,newId));
				 setAttr("itiList",copyIti(id,newId));
				 setAttr("imgList",copyImg(id,newId));
				 //setAttr("bits",getBits(Integer.parseInt(route.get("status")),type,route.get("id")));
				 setAttr("functBtnOptin", LcBottonUtils.getButtonOptions("10014",getRequest(),route.get("status"),type,route.get("id")));
				 setAttr("type",type);
			}else{
				routeEditParams(id,type);
			}
		} catch (Exception e) {
			e.printStackTrace();
			renderJson("result",e.getMessage());
		}
	}
	
	/**
	 *  跳转添加tag的页面
	 */
	public void addTag(){
		setAttr("tagList",TagMag.dao.find("select tm.*,tl.id as selected from t_tag_mag tm left join t_tag_line tl on (tm.id=tl.tag_id and tl.line_id='"+getPara("id")+"') where tm.is_delete != 0"));;
		setAttr("id",getPara("id"));
	}
	
	/**
	 *  更新线路的标签
	 */
	@LogDescription("更新线路的标签")
	public void updateTag(){
		try {
			String tagIds = getPara("tagIds");
			List<TagLine> exists = TagLine.dao.find("select * from t_tag_line where line_id='"+getPara("id")+"'");
			exists.forEach(t->t.delete());
			if(!tagIds.isEmpty()){
				String[] tagArr = tagIds.split(",");
				for(String tagId : tagArr){
					TagLine rel = new TagLine();
					rel.setId(LvoutcityUtils.getUUID());
					rel.setLineId(getPara("id"));
					rel.setTagId(tagId);
					rel.save();
				}
			}
			renderJson("result","success");
		}catch(Exception e){
			e.printStackTrace();
			renderJson("result",e.getMessage());
		}
	}
	
	/**
	 *  更新线路记录
	 */
	@LogDescription("更新线路记录")
	public void update(){
		
		try{
		Map<String, String[]> params = new HashMap(getParaMap());
		if(RouteMag.nameExisted(params.get("route_name")[0],getPara("id"))){
			renderJson("fail","线路名称重复！");
			return;
		}
		//可优化  Db.batchUpdate
			List<Itinerary> itiList = Itinerary.dao.findByRouteId(getPara("id"));
			String[] names = params.remove("iti.name");
			String[] mornings = params.remove("iti.morning");
			String[] noons = params.remove("iti.noon");
			String[] atNights = params.remove("iti.at_night");
			String[] lodgings = params.remove("iti.lodging");
			String[] traffics = params.remove("iti.traffic");
			for(int i=0;i<itiList.size();i++){
				Itinerary iti = itiList.get(i);
				iti.setName(names[i]);
				iti.setMorning(mornings[i]);
				iti.setAtNight(atNights[i]);
				iti.setLodging(lodgings[i]);
				iti.setTraffic(traffics[i]);
				iti.setNoon(noons[i]);
				iti.update();
			}
			String routeId = getPara("id");
			String[] imgList = params.remove("title_img[]");
			RouteImg.dao.removeAllImg(routeId);
			if(imgList!=null)
				for(int i=0,size=imgList.length;i<size;i++){
					RouteImg ri = new RouteImg();
					ri.setId(LvoutcityUtils.getUUID());
					ri.setUrl(imgList[i]);
					ri.setRouteId(routeId);
					ri.save();
				}
			if(params.get("gold_limit")!=null&&params.get("gold_limit")[0]==""){
				params.remove("gold_limit");
			}
			RouteMag route = RouteMag.dao.findById(routeId);
			params.entrySet().forEach( map -> route.set(map.getKey(),map.getValue()[0]));
			route.update();
		renderJson("result","success");
		}catch(Exception e){
			e.printStackTrace();
			throw e;
		}
	}
	
	/**
	 *  保存日程详情
	 */
	@LogDescription("保存日程详情")
	public void saveRemark() {

		try {
			Itinerary iti = Itinerary.dao.findById(getPara("id"));
			if(iti!=null){
				iti.setRemark(getPara("remark"));
				iti.setDetailTitle(getPara("detail_title"));
				iti.update();
			}
			renderJson("result", "success");
		} catch (Exception e) {
			e.printStackTrace();
			renderJson("result", e.getMessage());
		}
	}
	/**
	 *  跳转到线路订单查看
	 */
	public void orders(){
		setAttr("routeNo",getPara("routeNo"));
	}
	
	/**
	 *  删除线路
	 */
	@LogDescription("删除线路")
	public void delete() {
		try {
			String id = getPara("id");
			RouteMag route = RouteMag.dao.findById(id);
			if (route != null) {
				route.setIsDelete("0");
				route.update();
				renderJson("result", "success");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			renderJson("result", e.getMessage());
		}

	}
	
	/**
	 *  日程之图文介绍详情
	 */
	public void detail(){
		setAttr("id",getPara("id"));
		Itinerary iti = Itinerary.dao.findById(getPara("id"));
		setAttr("iti",iti);
		if(getPara("type").equals("1"))
			renderHtml(iti.getRemark());
	}
	
	/**
	 * 提交
	 */
	@LogDescription("提交线路")
	public void submit(){
		Map<String,Object> p = new HashMap<String, Object>();
		p.put("verify_user", null);
		p.put("submit_user", getCurrentUserId());
		p.put("submit_time", new Date());
		updateStatus(Constants.ROUTE.STATUS_SUBMITED,p);
	}
	/**
	 * 审核通过
	 */
	@LogDescription("审核通过线路")
	public void verify(){
		Map<String,Object> p = new HashMap<String, Object>();
		p.put("verify_user", getCurrentUserId());
		updateStatus(Constants.ROUTE.STATUS_ON_SHELF,p);
	}
	/**
	 * 审核不通过
	 */
	@LogDescription("审核不通过线路")
	public void refuse(){
		Map<String,Object> p = new HashMap<String, Object>();
		p.put("refuse_msg", getPara("reason"));
		p.put("verify_user", getCurrentUserId());
		updateStatus(Constants.ROUTE.STATUS_CREATED,p);
	}
	/**
	 * 撤销
	 */
	@LogDescription("撤销线路")
	public void revoke(){
		Map<String,Object> p = new HashMap<String, Object>();
		p.put("verify_user", null);
		updateStatus(Constants.ROUTE.STATUS_CREATED,p);
	}
	/**
	 * 上架
	 */
	@LogDescription("上架线路")
	public void onShelf(){
		updateStatus(Constants.ROUTE.STATUS_ON_SHELF,null);
	}
	/**
	 * 下架
	 */
	@LogDescription("下架线路")
	public void offShelf(){
		updateStatus(Constants.ROUTE.STATUS_OFF_SHELF,null);
	}
	
	/**
	 * 更新线路状态
	 * @param status
	 * @param arg 补充的其他需要更新的属性的键值对
	 */
	private void updateStatus(String status,Map<String,Object> arg){
		try {
			String id = getPara("id");
			RouteMag rt = RouteMag.dao.findById(id);
			rt.setStatus(status);
			if(arg!=null){
				arg.entrySet().forEach( pair -> rt.set(pair.getKey(), pair.getValue()));
			}
			rt.update();
			routeEditParams(id,"1");
			render("edit.html");
		}catch(Exception e){
			e.printStackTrace();
			renderJson("result", e.getMessage());
		}
	}
	
	
	/**
	 * 加载edit页面所需要的数据以供渲染
	 * @param id route id
	 * @param type 0 编辑，1查看，2复制
	 */
	private void routeEditParams(String id,String type){
		Record route = RouteMag.dao.searchWithGuide(id);
		setAttr("route",JSON.parse(route.toJson()));
		setAttr("tagList",TagMag.dao.findByLineId(id));
		setAttr("itiList",Itinerary.dao.findByRouteId(id));
		setAttr("type",type);
		setAttr("imgList",RouteImg.dao.findUrlByRouteId(id));
//		setAttr("bits",getBits(Integer.parseInt(route.get("status")),type,route.get("id")));
		setAttr("functBtnOptin", LcBottonUtils.getButtonOptions("10014",getRequest(),route.get("status"),type,route.get("id")));
	}
	
	/**
	 * 线路复制
	 * @param id
	 * @return
	 */
	@LogDescription("复制线路")
	private String copyRoute(String id){
		RouteMag route = RouteMag.dao.findById(id);
		route.setId(LvoutcityUtils.getUUID());
		route.setRouteName(route.getRouteName()+"_副本");
		route.setStatus("0");
		route.setSubmitUser(null);
		route.setSubmitTime(null);
		route.setVerifyUser(null);
		route.setRouteNo(null);
		route.save();
		return route.getId();
	}
	
	/**
	 * 线路复制之标签复制
	 * @param id
	 * @param newId
	 * @return
	 */
	private List<TagMag> copyTag(String id,String newId){
		List<TagMag> tags = TagMag.dao.findByLineId(id);
		for(TagMag tag : tags){
			TagLine tl = new TagLine();
			tl.setId(LvoutcityUtils.getUUID());
			tl.setTagId(tag.getId());
			tl.setLineId(newId);
			tl.save();
		}
		return tags;
	}
	
	/**
	 * 线路复制之题图复制
	 * @param id
	 * @param newId
	 * @return
	 */
	private List<String> copyImg(String id,String newId){
		List<String> imgs = RouteImg.dao.findUrlByRouteId(id);
		imgs.forEach(img->{
			RouteImg i = new RouteImg();
			i.setId(LvoutcityUtils.getUUID());
			i.setUrl(img);
			i.setRouteId(newId);
			i.save();
		});
		return imgs;
	}
	
	/**
	 * 线路复制之日程复制
	 * @param id
	 * @param newId
	 * @return
	 */
	private List<Itinerary> copyIti(String id,String newId){
		List<Itinerary> itis = Itinerary.dao.findByRouteId(id);
		for(Itinerary iti : itis){
			iti.setId(LvoutcityUtils.getUUID());
			iti.setRouteId(newId);
			iti.save();
		}
		return itis;
	}
	
	/**
	 * 获取不同状态下编辑页面上需要显示按钮的标志串
	 * @param status
	 * @param type
	 * @param id
	 * @return
	 */
//	private Integer getBits(Integer status, String type, String id){
//		Integer bits = 0; 
//		if(type.equals("3")){
//			bits = 1;
//		}else if(type.equals("0")||type.equals("2")){ // 编辑 复制
//			bits = 3;
//		}else{
////			Integer status = ;
//			switch (status) {
//			case 1:
//				bits+=4;
//				bits+=8;
//				if(!RouteMag.dao.hasOrder(id)){//有订单就不能撤销
//					bits += 64;
//				}
//				break;
//			case 2:
//				bits+=32;
//				if(!RouteMag.dao.hasOrder(id)){//有订单就不能撤销
//					bits += 64;
//				}
//				break;
//			case 3:
//				bits+=16;
//				if(!RouteMag.dao.hasOrder(id)){//有订单就不能撤销
//					bits += 64;
//				}
//				break;
//			}
//		}
//		return bits;
//	}
	
}
