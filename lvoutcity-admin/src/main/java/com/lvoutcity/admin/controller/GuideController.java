package com.lvoutcity.admin.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Before;
import com.jfinal.ext.interceptor.POST;
import com.jfinal.plugin.activerecord.Db;
import com.lvoutcity.admin.utils.LcBottonUtils;
import com.lvoutcity.admin.utils.WebUtils;
import com.lvoutcity.core.base.BaseController;
import com.lvoutcity.core.util.Constants;
import com.lvoutcity.core.util.HttpUtils;
import com.lvoutcity.core.util.LvoutcityUtils;
import com.lvoutcity.core.util.SQuery;
import com.lvoutcity.model.Attractions;
import com.lvoutcity.model.Guide;
import com.lvoutcity.model.GuideMarkerLine;
import com.lvoutcity.model.MapPolyLineData;
import com.lvoutcity.model.MapRaiders;
import com.lvoutcity.model.RouteMag;
import com.lvoutcity.model.UserBackgroup;

import net.sf.json.JSONArray;

/**
 * 
* @ClassName: GuideController 
* @Description: 攻略管理 
* @author James_w.s
* @date 2016年5月11日 上午9:51:01 
*
 */
public class GuideController extends BaseController{

	Log log = LogFactory.getLog(GuideController.class);
	
	/**
	 * 
	* @Title: index 
	* @Description: 攻略管理
	* @param  :   设定文件
	* @return void    返回类型 
	* @datetiem 2016上午9:55:01
	* @throws
	 */
	public void index(){
		setAttr("functBtn", LcBottonUtils.getButtonOptions("10013",getRequest()));
		render("index.html");
	}
	
	/**
	 * 
	* @Title: add 
	* @Description: 添加景点
	* @param    : 设定文件
	* @return void    返回类型 
	* @datetiem 2016上午9:57:02
	* @throws
	 */
	public void add(){
		setAttr("type","add");
		setAttr("bean",new Guide());
		//查询标注
		getAttrList();
		render("add.html");
	}
	
	/**
	 * 
	* @Title: add 
	* @Description: 添加景点
	* @param    : 设定文件
	* @return void    返回类型 
	* @datetiem 2016上午9:57:02
	* @throws
	 */
	public void map(){
		setAttr("type",getPara("type"));
		String guideId = getPara("guideId");
		if(LvoutcityUtils.isNotNull(guideId)){
			int size = 0;
			Guide bean = Guide.dao.findById(guideId);
			if(Integer.valueOf(bean.getDuration()) > Integer.valueOf(bean.getNight())){
				size = Integer.valueOf(bean.getDuration());
			}else{
				size = Integer.valueOf(bean.getNight());
			}
			
			StringBuffer sb = new StringBuffer();
			for(int i=1; i<= size; i++){
					sb.append("<input type=\"button\" class=\"button\" title= '点击添加途经点' value=\"第"+i+"天\" onClick=\"setDay("+i+")\" />  ");
			}
			
			setAttr("input", sb.toString());
			getAttrList();
			GuideMarkerLine g = GuideMarkerLine.dao.queryGuideMarkerLine(bean.getId());
			if(LvoutcityUtils.isNotNull(g)){
				setAttr("polyList",g.getPolyline());
				setAttr("markers", g.getMarker());
				setAttr("mapInfo", JSON.parseObject( g.getMapInfo(), HashMap.class));
			}
			setAttr("bean",bean);
		}else{
			setAttr("bean",new Guide());
			getAttrList();
		}
		render("map.html");
	}
	
	/**
	 * 
	 * @Title: getAttrList 
	 * @Description: 查询标注
	 * @param   :  设定文件
	 * @return void    返回类型 
	 * @create_time 2016下午1:43:19
	 * @create_user James w.s
	 * @throws
	 */
	public void getAttrList(){
		StringBuffer sb = new StringBuffer();
		sb.append(" select t.id,t.name,t.longitude,t.latitude , '景点' as city ");
		sb.append("   from t_attractions t ");
		sb.append("	left join ");
		sb.append(" t_map_raiders m on ");
		sb.append(" m.attr_id = t.id ");
		sb.append(" where t.is_delete = '1' and t.type='1' ");
		sb.append(" and t.club_id='"+((UserBackgroup)getSessionAttr(Constants.SYS_USER_BACK)).getClubId()+"'");
		List<Attractions> list = Attractions.dao.find(sb.toString());
		// 转换成List<Map<String,Object>> 格式处理
		List<Map<String,Object>> map = new ArrayList<Map<String,Object>>();
		for(Attractions attr : list){
			Map<String,Object> json = new HashMap<String,Object>();
			json.put("id", attr.getId());
			json.put("name", attr.getName());
			json.put("longitude", attr.getLongitude());
			json.put("latitude", attr.getLatitude());
			json.put("type", attr.getCity());
			json.put("v_o", "5");
			map.add(json);
		}
		JSONArray jsonArray = JSONArray.fromObject(map);  
		System.out.println(jsonArray);
		setAttr("list",jsonArray);
	}
	
	/**
	 * 
	 * @Title: getAttrPolyLineMarker 
	 * @Description: 刷新移动标点数据
	 * @param     :设定文件
	 * @return void    返回类型 
	 * @create_time 2016上午10:02:44
	 * @create_user James w.s
	 * @throws
	 */
	public void getAttrPolyLineMarker(){
		String attrId = getPara("attrId");
		JSONArray array =getAttPolylineList(null,attrId);
		renderDatatable(array);
	}
	
	/**
	 * 
	 * @Title: getAttPolylineList 
	 * @Description: 连线数据库
	 * @param   :  设定文件
	 * @return void    返回类型 
	 * @create_time 2016下午3:14:22
	 * @create_user James w.s
	 * @throws
	 */
	public JSONArray getAttPolylineList(String param,String attrId){
		try {
			SQuery sb =SQuery.create();
			sb.append(" select t.id,t.name,t.longitude,t.latitude,  ");
			sb.append(" case m.type when 1 then '结合点' ");
			sb.append(" 			when 2 then '起点' ");
			sb.append(" 			when 3 then '终点' ");
			sb.append(" 			when 4 then '时间点' ");
			sb.append(" 			when 5 then '景点' ");
			sb.append(" end city ,m.type as address ");
			sb.append("   from t_attractions t  ");
			sb.append("	left join ");
			sb.append(" t_map_raiders m on ");
			sb.append(" m.attr_id = t.id ");
			sb.append(" left join ");
			sb.append(" t_guide g on ");
			sb.append(" g.id =  m.guide_id ");
			sb.append(" where ");
			if(LvoutcityUtils.isNotNull(param)){
				sb.append(" g.id = '"+param+"'");
			}
			if(LvoutcityUtils.isNotNull(attrId)){
				sb.append(" t.id = '"+attrId+"'");
			}
			sb.append(" and t.is_delete='1' ");
			sb.append("	and t.type = '3' ");
			sb.append(" order by m.order_by desc ");
			List<Attractions> list = Attractions.dao.find(sb.getSQuery());
			// 转换成List<Map<String,Object>> 格式处理
			List<Map<String,Object>> map = new ArrayList<Map<String,Object>>();
			for(Attractions attr : list){
				Map<String,Object> json = new HashMap<String,Object>();
				json.put("id", attr.getId());
				json.put("name", attr.getName());
				json.put("longitude", attr.getLongitude());
				json.put("latitude", attr.getLatitude());
				json.put("type", attr.getCity());
				json.put("v_o", attr.getAddress());
				map.add(json);
			}
			JSONArray jsonArray = JSONArray.fromObject(map);  
			setAttr("line",jsonArray);
			System.out.println(jsonArray);
			return jsonArray;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.debug(e);
		}
		return null;
	}
	
	
	
	/**
	 * 
	 * @Title: editAttrGuide 
	 * @Description: 景点修改
	 * @param   :  设定文件
	 * @return void    返回类型 
	 * @create_time 2016下午1:36:47
	 * @create_user James w.s
	 * @throws
	 */
	public void editAttrGuide(){
		Attractions  bean = getBean(Attractions.class);
		if(LvoutcityUtils.isNotNull(bean)){
			Attractions newBean = Attractions.dao.findById(bean.getId());
			newBean.
			set("longitude",bean.getLongitude()).
			set("latitude",bean.getLatitude());
			boolean updateIs = newBean.update();
			getAttrList();
			if(updateIs){
				renderJson("success", "修改成功!");
				return;
			}
			renderJson("error", "修改失败!");
		}
	}
	
	
	
	/**
	 * 
	 * @Title: getPolyList 
	 * @Description: 查询折线数据
	 * @param   :  设定文件
	 * @return void    返回类型 
	 * @create_time 2016下午6:28:57
	 * @create_user James w.s
	 * @throws
	 */
	public void getPolyList(String id){
		try {
			StringBuffer sb = new StringBuffer();
			sb.append(" select t.id,t.longitude,t.latitude , t.guide_id ");
			sb.append("   from t_map_poly_line_data t ");
			sb.append(" where 1=1 and t.guide_id = '"+id+"'");
			List<MapPolyLineData> list = MapPolyLineData.dao.find(sb.toString());
			// 转换成List<Map<String,Object>> 格式处理
			List<Map<String,Object>> map = new ArrayList<Map<String,Object>>();
			for(MapPolyLineData attr : list){
				Map<String,Object> json = new HashMap<String,Object>();
				json.put("id", attr.getId());
				json.put("lng", attr.getLongitude());
				json.put("lat", attr.getLatitude());
				json.put("guideId", attr.getGuideId());
				map.add(json);
			}
			JSONArray jsonArray = JSONArray.fromObject(map);  
			System.out.println(jsonArray);
			setAttr("polyList",jsonArray);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	/**
	 * 
	 * @Title: getGuideList 
	 * @Description: 攻略查询
	 * @param  :   设定文件
	 * @return void    返回类型 
	 * @create_time 2016下午1:48:23
	 * @create_user James w.s
	 * @throws
	 */
	public void getGuideList(){
		StringBuffer sb = new StringBuffer();
		sb.append(" select g.guide_no as guideNo,g.name,g.enabled,DATE_FORMAT(g.create_time,'%Y-%m-%d %H:%m:%s') as createTime,u.user_name as createUser,c.user_name as commitName, ");
		sb.append(" g.description as description ,t.club_name as clubName,g.duration as duration,g.night as night , g.id as id ");
		StringBuffer sb1 = new StringBuffer();
		sb1.append("  from  t_guide g ");
		sb1.append(" left join t_user_backgroup u on u.user_id = g.user_id  ");
		sb1.append(" left join t_tourism_club t on g.club_id = t.id ");
		sb1.append(" left join t_user_backgroup c on c.id = g.commit_user ");
		// 查询条件添加
		sb1.append(" where 1 =1");
		if(LvoutcityUtils.isNotNull(getPara("clubId"))){
			sb1.append(" and g.club_id='"+getPara("clubId")+"' ");
		}
		if(LvoutcityUtils.isNotNull(getPara("guideNo"))){
			sb1.append(" and g.guide_no='"+getPara("guideNo")+"' ");
		}
		if(LvoutcityUtils.isNotNull(getPara("name"))){
			sb1.append(" and g.name like '%"+getPara("name")+"%' ");
		}
		if(LvoutcityUtils.isNotNull(getPara("enabled"))){
			if(getPara("enabled").equals("999"))
				sb1.append(" and g.enabled is null ");
			else	
				sb1.append(" and g.enabled = '"+getPara("enabled")+"' ");
		}
		renderDatatable(LvoutcityUtils.getPage(Db.paginate((getDatatableStart() / getDatatableLength()) + 1, getDatatableLength(), sb.toString(), sb1.toString() + " order by g.create_time desc ")));
	}
	
	
	/**
	 * 
	* @Title: saveOrUpdate 
	* @Description: 添加攻略
	* @param  :   设定文件
	* @return void    返回类型 
	* @datetiem 2016上午10:20:26
	* @throws
	 */
	@Before(POST.class)
	public void saveOrUpdate(){
		try {
			Guide bean = getBean(Guide.class);
			Map<String,String[]> map = getParaMap();
			String[] cities = map.get("cities[]");
			if(LvoutcityUtils.isNotNull(bean) && LvoutcityUtils.isNotNull(bean.getId())){
				Guide newBean = Guide.dao.findById(bean.getId());
				if("1".equals(bean.getCopyData().trim())){
					//复制攻略
					newBean.setId(LvoutcityUtils.getUUID());
					newBean.setName(bean.getName());
					newBean.setCreateTime(new Date());
					newBean.setIsDelete("1");
					newBean.setClubId(((UserBackgroup)getSessionAttr(Constants.SYS_USER_BACK)).getClubId());
					newBean.setUserId(getSessionAttr("current_user_id"));
					newBean.set("enabled", null);
					newBean.set("guide_no", null);
					newBean.save();
					GuideMarkerLine g = GuideMarkerLine.dao.findFirst("select * from t_guide_marker_line where guide_id='"+bean.getId()+"'");
					if(g!=null){
						g.setId(LvoutcityUtils.getUUID());
						g.setGuideId(newBean.getId());
						g.save();
					}
					MapPolyLineData.dao.updateCityData(newBean.getId(),cities);
					renderJson("success",newBean);
					return;
				}
				if(!RouteMag.dao.queryRoutesList(bean.getId()).isEmpty()){//@DEBUG
					renderJson("error", "已经关联线路的攻略无法修改!");
					return;
				}
				newBean.
						set("name", bean.getName()).
						set("duration",bean.getDuration().trim()).
						set("night",bean.getNight().trim()).
						set("description",bean.getDescription()).
						set("enabled", LvoutcityUtils.isNotNull(bean.getEnabled()) ? bean.getEnabled() : newBean.getEnabled());
				MapPolyLineData.dao.updateCityData(bean.getId(),cities);
				
				if(newBean.update()==true){
					renderJson("success",bean);
				}
				return ;
			}else{
				bean.setId(LvoutcityUtils.getUUID());
				bean.setCreateTime(new Date());
				bean.setIsDelete("1");
				bean.setClubId(((UserBackgroup)getSessionAttr(Constants.SYS_USER_BACK)).getClubId());
				bean.setUserId(getSessionAttr("current_user_id"));
				boolean save = bean.save();
				setAttr("guide",bean);
				MapPolyLineData.dao.updateCityData(bean.getId(),cities);
				if(save == true){
					renderJson("success", bean);
				}
				return ;
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.debug(e);
		}
	}
	
	/**
	 * 判断攻略是否关联
	 */
	public void isRelated(){
		String id = getPara("id");
		if(!RouteMag.dao.queryRoutesList(id).isEmpty()){
			renderJson("error", "已经关联线路的攻略无法修改!");
			return;
		}else{
			renderJson("success", "1");

		}
	}
	
	/**
	 * 
	 * @Title: updateEnabled 
	 * @Description: 修改为上架或下架
	 * @param     设定文件 
	 * @return void    返回类型 
	 * @create_time 2016 2016年7月1日 下午4:59:50
	 * @create_user James w.s
	 * @throws
	 */
	public void updateEnabled(){
		try {
			Map<String,String[]> idMap = getParaMap();
			String[] idList = idMap.get("idList[]");
//			List<Guide> guideList  = new ArrayList<Guide>();
			for (int i = 0; i < idList.length; i++) {
				Guide bean = Guide.dao.findById(idList[i]);
				StringBuffer sb = new StringBuffer();
				if(getPara("enabled").equals("1")&&LvoutcityUtils.isNull(MapPolyLineData.dao.queryMapPolyLine(bean.getId()))){
					sb.append(" <b>途径城市</b>  ");
				}
				GuideMarkerLine g = GuideMarkerLine.dao.queryGuideMarkerLine(bean.getId());
				if(LvoutcityUtils.isNull(g)){
					sb.append(" <b>地图信息</b> ");
				}else{
					if(LvoutcityUtils.isNull(g.getPolyline())){
						sb.append(" <b>路线折线</b> ");
					}
					// TODO
					List<HashMap> markers = JSON.parseArray(g.getMarker(),HashMap.class);
					StringBuffer typestr = new StringBuffer();
					markers.forEach(marker -> {
						typestr.append("'").append(marker.get("type").toString()).append("'");
						
					});
					if(typestr.indexOf("'1'")==-1){
						sb.append(" <b>集合点</b>");
					}
					if(typestr.indexOf("'2'")==-1){
						sb.append(" <b>出发点</b>");
					}
					if(typestr.indexOf("'3'")==-1){
						sb.append(" <b>终点</b>");
					}
				}
				
				if(!sb.toString().isEmpty()){
					renderJson("error","请先录入 "+sb.toString()+ " 再上架！");
					return;
				}
				bean.set("enabled",getPara("enabled"));
				bean.update();
			}
			renderJson("success","操作成功!");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}	
	
	public void extMapAllOverlays(String id){
		String lineList = getPara("lineinfo");
		String marker = getPara("mapinfo");
		String mapimfo = getPara("mapzoom");
		JSONObject line = JSONObject.parseObject(lineList);
		com.alibaba.fastjson.JSONArray  lineJSONArray  = line.getJSONArray("data");
		JSONObject markerObject = JSONObject.parseObject(marker);
		com.alibaba.fastjson.JSONArray  markerArray  = markerObject.getJSONArray("data");
//		List<GuideMarkerLine> list = GuideMarkerLine.dao.find("select * from t_guide_marker_line l where l.guide_id='"+id+"'");
//		if(!list.isEmpty()){
//		}
		Db.update("delete from t_guide_marker_line  where guide_id ='"+id+"'");
		GuideMarkerLine bean = new GuideMarkerLine();
		bean.setGuideId(id);
		bean.setId(LvoutcityUtils.getUUID());
		bean.setMapInfo(mapimfo);
		bean.setPolyline(lineJSONArray.toString());
		bean.setMarker(markerArray.toString());
		bean.save();
	}
	
	
	/**
	 * 
	 * @Title: saveOrUpdateMarker 
	 * @Description:绑定数据PolyLine
	 * @param     设定文件 
	 * @return void    返回类型 
	 * @create_time 2016下午6:48:18
	 * @create_user James w.s
	 * @throws
	 */
	@Before(POST.class)
	public void saveOrUpdateMarker() {

		try {
			String guideId = getPara("guideId");
			if (!RouteMag.dao.queryRoutesList(guideId).isEmpty()) {// @DEBUG
				renderJson("error", "已经关联线路无法修改!");
				return;
			}
			// 处理手机地图使用数据
			extMapAllOverlays(guideId);
			renderJson("success", "操作成功!");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
	}
	
	/**
	 * @param polyLine 
	 * @param guideId 
	 * 
	 * @Title: savePolyLine 
	 * @Description: 处理折线
	 * @param   :  设定文件
	 * @return void    返回类型 
	 * @create_time 2016下午4:37:28
	 * @create_user James w.s
	 * @throws
	 */
	public void savePolyLine(String guideId, String polyLine){
		
//		List<MapPolyLineData> dd = MapPolyLineData.dao.find("select * from t_map_poly_line_data t where t.guide_id='"+guideId+"'");
//		if(!dd.isEmpty()){
//		}
		Db.update("delete from t_map_poly_line_data  where guide_id='"+guideId+"'");
		//
		JSONObject object = JSONObject.parseObject(polyLine);
		com.alibaba.fastjson.JSONArray  lineJSONArray  = object.getJSONArray("data");
		
		List<MapPolyLineData> list = new ArrayList<MapPolyLineData>();
		for(int i=0;i<lineJSONArray.size();i++){
			JSONObject json = lineJSONArray.getJSONObject(i);
			String d = HttpUtils.getHttpURLGet("http://restapi.amap.com/v3/geocode/regeo?key=9e039affcbfc4476e0315ff422a832fb&location="+json.getString("lng")+","+json.getString("lat")+"&radius=1000&extensions=base&batch=false&roadlevel=1");
			JSONObject ad = JSONObject.parseObject(d);
			JSONObject ad2 = JSONObject.parseObject(ad.get("regeocode").toString());
			System.out.println(ad2);
			JSONObject ad3 = JSONObject.parseObject(ad2.get("addressComponent").toString());
			System.out.println(ad3);
			JSONObject ad4 = JSONObject.parseObject(ad3.toJSONString());
			System.out.println(ad4.get("province"));
			MapPolyLineData m = new MapPolyLineData();
			m.setId(LvoutcityUtils.getUUID());
			System.out.println(ad4.get("city").toString().equals("[]"));
			m.setCity(LvoutcityUtils.isNotNull(ad4.get("city")) && !ad4.get("city").toString().equals("[]") ? ad4.get("city").toString() : ad4.get("province").toString());
			m.setLongitude(json.getString("lng"));
			m.setGuideId(guideId);
			m.setLatitude(json.getString("lat"));//,"lat":
			m.setProvince(LvoutcityUtils.isNotNull(ad4.get("province")) ? ad4.get("province").toString() : null);
			m.setAddress(LvoutcityUtils.isNotNull(ad4.get("township")) ? ad4.get("township").toString() : null);
			list.contains(json.get(""));
			list.add(m);
		}
		//查询是否存在数据
		if(!list.isEmpty()){
			Db.batchSave(list, list.size()); 
		}
	}
	
	/**
	 * 
	 * @Title: getMarkerIocs 
	 * @Description: 
	 * @param @return    设定文件 
	 * @return String    返回类型 
	 * @create_time 2016上午11:04:11
	 * @create_user James w.s
	 * @throws
	 */
	public String getMarkerIocs(String type){
		switch(type){
		case "1": return "集合点";
		case "2": return "起点";
		case "3": return "终点";
		default : 
			int index = type.indexOf("D");
			String str = type.substring(index+1,type.length());
			return "第"+str+"天";
		}
	}
	
	public String getMarkerOrderBy(String type){
		switch(type){
		case "1": return "-1";
		case "2": return "0";
		default : 
			int index = type.indexOf("D");
			String str = type.substring(index+1,type.length());
			return str;
		case "3": return "999";
		}
	}
	
	
	/**
	 * 
	 * @Title: deleteMarker 
	 * @Description: 删除
	 * @param  :   设定文件
	 * @return void    返回类型 
	 * @create_time 2016下午1:51:40
	 * @create_user James w.s
	 * @throws
	 */
	public void deleteMarker(String guideId){
		
	}
	
	
	
	/**
	 * 查看详情
	 */
	public void edit(){
		String id = getPara("id");
		try {
			String view = getPara("view");
//			int size = 0;
			Guide bean = Guide.dao.findById(id);
//			将复制的guide中的guideno和enable字段设置为空
//			bean.setGuideNo(null);
//			bean.setEnabled(null);
			/*if(Integer.valueOf(bean.getDuration()) > Integer.valueOf(bean.getNight())){
				size = Integer.valueOf(bean.getDuration());
			}else{
				size = Integer.valueOf(bean.getNight());
			}
			
			StringBuffer sb = new StringBuffer();
			for(int i=1; i<= size; i++){
				
				if(i == 1){
					sb.append("<input type=\"button\" class=\"button\"  value=\"第"+i+"天\" onClick=\"setDay("+i+")\" />  ");
				}else{
					sb.append( "\t <input type=\"button\" class=\"button\"  value=\"第"+i+"天\" onClick=\"setDay("+i+")\" />\t ");
				}
			}
			
			setAttr("input", sb.toString());
			getAttrList();
			GuideMarkerLine g = GuideMarkerLine.dao.findFirst("select * from t_guide_marker_line where guide_id='"+bean.getId()+"'");
			if(LvoutcityUtils.isNotNull(g)){
				setAttr("polyList",g.getPolyline());
				setAttr("line", g.getMarker());
				setAttr("mapInfo", JSON.parseObject( g.getMapInfo(), HashMap.class));
			}*/
			if(LvoutcityUtils.isNull(view)){
				setAttr("type","edit");
			}else{
				if("2".equals(view)){ //复制
					bean.setCopyData("1");
					bean.setId(null);
					bean.setName(bean.getName()+"_副本");
					setAttr("type","copy");
				}else{
					setAttr("type","view");
				}
			}
			setAttr("bean",bean);
			setAttr("cities",MapPolyLineData.dao.findByGuideId(bean.getId()));
			render("add.html");
		} catch (NumberFormatException e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	/**
	 * 删除
	 */
	public void delete(){
		String id = "";
		Map<String,String[]> idMap = getParaMap();
		String[] idList = idMap.get("idList[]");
		for (int i = 0; i < idList.length; i++) {
			id = idList[i];
			Guide bean = Guide.dao.findById(id);
			if(LvoutcityUtils.isNotNull(bean)){
				if(!RouteMag.dao.queryRoutesList(bean.getId()).isEmpty()){
					renderJson("error","存在线路信息不可以删除!");
					return;
				}
				boolean d = bean.deleteById(bean.getId());
				if(d){
					renderJson("success", "false");
				}else{
					renderJson("error");
				}
			}
		}
	}
	
	
	
	/**
	 * 
	 * @Title: saveMapRaiders 
	 * @Description: 攻略地图编辑 页面请求
	 * @param   :  设定文件
	 * @return void    返回类型 
	 * @create_time 2016下午1:58:00
	 * @create_user James w.s
	 * @throws
	 */
	@SuppressWarnings("unused")
	public void addMapRaiders(){
		MapRaiders map = getBean(MapRaiders.class);
		
		//景点id查询
		Attractions newBean = Attractions.dao.findById(map.getAttrId());
		//查询攻略id
		Guide bean = Guide.dao.findById(map.getGuideId());
		
		List<MapRaiders> list = MapRaiders.dao.queryMapRaidersList(bean.getId());
		StringBuffer sb = new StringBuffer();
		if(LvoutcityUtils.isNotNull(bean) && LvoutcityUtils.isNotNull(bean.getId())){
			int size = 0;
			if(Integer.valueOf(bean.getDuration()) > Integer.valueOf(bean.getNight())){
				size = Integer.valueOf(bean.getDuration());
			}else{
				size = Integer.valueOf(bean.getNight());
			}
			int d = 1 ;
			sb.append("<option value ='-1'/> 请选择 </option>");
			for(MapRaiders ra : list){
				sb.append("<option value =\""+ra.getId()+"\" /> 攻略第"+d+"天 </option> " );
				
				/*if(d == 0){
					sb.append("<input type=\"checkbox\" name=\"\" id=\"chckeId\" value =\""+ra.getId()+"\" /> 攻略第"+size--+"天  \t " );
				}else{
					
					if(d % 3 != 0){
						sb.append("<input type=\"checkbox\" name=\"\" id=\"chckeId\" value =\""+ra.getId()+"\" /> 攻略第"+size--+"天  \t " );
					}else{
						sb.append(" \t <input type=\"checkbox\" name=\"\" id=\"chckeId\" value =\""+ra.getId()+"\" /> 攻略第"+size--+"天  <br/>" );
					}
				}
				*/
				d++;
			}
			
			//景点 判断是否为空
			if(LvoutcityUtils.isNotNull(newBean) && LvoutcityUtils.isNotNull(newBean.getId())){
				map.setAttrId(newBean.getId());
				map.setName(newBean.getName());
			}
			map.setGuideId(bean.getId());
		}
		setAttr("mapBean",map);
		setAttr("chcke",sb.toString());
		render("attr_guide.html");
	}
	
	/**
	 * 
	 * @Title: saveOrUpdateMap 
	 * @Description: 攻略地图编辑 
	 * @param    : 设定文件
	 * @return void    返回类型 
	 * @create_time 2016下午2:36:59
	 * @create_user James w.s
	 * @throws
	 */
	@Before(POST.class)
	public void saveOrUpdateMap(){
		try {
			MapRaiders map = getBean(MapRaiders.class);
			map.setSuperMapId(getPara("superMapId"));
			if(LvoutcityUtils.isNotNull(map) && LvoutcityUtils.isNotNull(map.getId())){
				MapRaiders newMap = MapRaiders.dao.findById(map.getId());
				newMap.set("type", map.getType());
				if(LvoutcityUtils.isNotNull(map.getName())){
					newMap.set("name", map.getName());
				}
				newMap.update();
				setAttr("mapBean",newMap);
			}else{
				//判断是否数据保存
				if(LvoutcityUtils.isNull(map.getGuideId())){
					renderJson("error", "请先保存攻略数据在操作地图信息");
					return;
				}
				
				//查询数据是否充分
				if(!getSaveMapTypeValid( map)){
					renderJson("error", "已经存在相同的类型不能再次添加");
					return;
				}
				
//				if(!"5".equals(map.getType())){
//					Attractions attr = new Attractions();
//					attr.setId(LvoutcityUtils.getUUID());
//					attr.setLatitude(map.getLat());
//					attr.setLongitude(map.getLng());
//					attr.setName(map.getName());
//					attr.setCreateTime(new Date());
//					attr.setIsDelete("1");
//					attr.setType("0");
//					attr.save();
//					map.setAttrId(attr.getId());
//				}
				
				SimpleDateFormat sd = new SimpleDateFormat("YYmmddhhMMss");
				map.setOrderBy(sd.format(new Date())+map.getType());
				map.setId(LvoutcityUtils.getUUID());
				if(LvoutcityUtils.isNotNull(map.getSuperMapId())){
					int d = MapRaiders.dao.getMapRaidersCount(map.getSuperMapId());
					MapRaiders superMap = MapRaiders.dao.findById(map.getSuperMapId());
					if(d > 0){
						map.setStatus(superMap.getId());
						map.setOrderBy(superMap.getOrderBy()+"-"+d);
					}
				}
				map.setCreateTime(new Date());
				map.setIsDelete(Constants.ISDELETE_FALSE);
				map.save();
				setAttr("mapBean",map);
			}
			renderJson("success", "false");
		} catch (Exception e) {
			e.printStackTrace();
			renderJson("error", 0);
		}
	}
	
	/**
	 * 
	 * @Title: deleteMap 
	 * @Description: 标注删除
	 * @param   :  设定文件
	 * @return void    返回类型 
	 * @create_time 2016下午2:44:35
	 * @create_user James w.s
	 * @throws
	 */
	public void deleteMap(){
		String id = getPara("id");
		List<MapRaiders> map = MapRaiders.dao.find("select * from t_map_raiders m where m.attr_id='"+id+"' ");
		if(!map.isEmpty()){
			MapRaiders bean = map.get(0);
			MapRaiders a =  MapRaiders.dao.findById(bean.getId());
			Attractions attr = Attractions.dao.findById(id);
			attr.deleteById(attr);
			/**
			 * 删除 地图上标注 然后再进行 在进行数据查询有没有关联的数据 在删除
			 */
			if(LvoutcityUtils.isNotNull(a)){
				a.deleteById(id);
			}
		}
	}
	
	
	/**
	 * 
	 * @Title: getSaveMapTypeValid 
	 * @Description: 添加类型是否重复
	 * @param  :   设定文件
	 * @return void    返回类型 
	 * @create_time 2016下午6:34:09
	 * @create_user James w.s
	 * @throws
	 */
	public boolean getSaveMapTypeValid(MapRaiders map){
		Guide bean = Guide.dao.findById(map.getGuideId());
		List<MapRaiders> list = MapRaiders.dao.find(SQuery.create("select * from t_map_raiders t where t.guide_id ='"+bean.getId()+"' and t.attr_id='"+map.getType()+"'").getSQuery());
		
		//? renderJson("","此景点已经添加过!") : renderJson("success","1") 
		return list.isEmpty() ;
	}
	
	/**
	 * 
	 * @Title: getAttractionsVaild 
	 * @Description: 校验景点是否重复添加
	 * @param   :  设定文件
	 * @return void    返回类型 
	 * @create_time 2016下午3:17:05
	 * @create_user James w.s
	 * @throws
	 */
	public void getAttractionsVaild(){
		MapRaiders map = getBean(MapRaiders.class);
		List<MapRaiders> list = MapRaiders.dao.find(SQuery.create("select * from t_map_raiders t where t.guide_id ='"+map.getGuideId()+"' and t.attr_id='"+map.getAttrId()+"'").getSQuery());
		renderJson(list.isEmpty()? "success":"error", list.isEmpty()? "1":"此景点已经重复添加!");
	}
}
