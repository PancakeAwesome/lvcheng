package com.lvoutcity.admin.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.aop.Before;
import com.jfinal.ext.interceptor.POST;
import com.jfinal.kit.Ret;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.redis.Redis;
import com.lvoutcity.admin.utils.LcBottonUtils;
import com.lvoutcity.admin.utils.WebUtils;
import com.lvoutcity.core.base.BaseController;
import com.lvoutcity.core.util.Constants;
import com.lvoutcity.core.util.HttpUtils;
import com.lvoutcity.core.util.LvoutcityUtils;
import com.lvoutcity.model.Attractions;
import com.lvoutcity.model.Protocol;
import com.lvoutcity.model.User;

import net.sf.json.JSONArray;
import redis.clients.jedis.Jedis;

/**
 * 
 * <一句话功能简述> <功能详细描述>
 * 
 * @author Administrator
 * @version [版本号, 2016年5月7日]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class AttractionsController extends BaseController {

	private Log log = LogFactory.getLog(AttractionsController.class);

	private static AttractionsController attrInstance;
	
	public static AttractionsController getInstance(){
		if(LvoutcityUtils.isNotNull(attrInstance)){
			attrInstance = new AttractionsController();
		}
		return attrInstance;
	}
	
	/**
	 * 主页跳转
	 */
	public void index() {
		
		StringBuffer sb = new StringBuffer();
		sb.append(" select t.id,t.name,t.longitude,t.latitude , '景点' as city ");
		sb.append("   from t_attractions t ");
		sb.append(" where t.is_delete = '1' and t.type='1' ");
		sb.append(" and t.club_id ='"+((User)getSessionAttr(Constants.SYS_USER)).getClubId()+"'");
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
		//System.out.println(jsonArray);
		setAttr("list",jsonArray);
		setAttr("aaa",LcBottonUtils.getButtonOptions("10012", getRequest(),(User)getSessionAttr(Constants.SYS_USER)) ? "1":"0");
		render("index.html");
	}

	public void getList(){
		StringBuffer sb = new StringBuffer();
		sb.append(" select t.id,t.name,t.longitude,t.latitude , '景点' as city ");
		sb.append("   from t_attractions t ");
		sb.append(" where t.is_delete = '1' and t.type='1' ");
		if(LvoutcityUtils.isNotNull(getPara("clubId"))){
			sb.append(" and t.club_id ='"+getPara("clubId")+"' ");
		}
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
//		JSONArray jsonArray = JSONArray.fromObject(map);
		//System.out.println(jsonArray);		
		renderJson("result",map);
	}
	/**
	 * 
	 * @Title: getListAttr @Description: 获取景点列表 @param 设定文件 @return void
	 * 返回类型 @user James w.s @create_time 2016上午10:42:56 @throws
	 */
	public void getListAttr() {
		Attractions bean = getBean(Attractions.class);
		StringBuffer sb = new StringBuffer();
		sb.append(" select * from t_attractions t where ");
		sb.append(" t.is_delete = '1' and t.type = '1' ");
		if (LvoutcityUtils.isNotNull(bean)) {
			if (LvoutcityUtils.isNotNull(bean.getName())) {
				sb.append(" t.name like '%" + bean.getName() + "%'");
			}
		}
		Page<Attractions> page = Attractions.dao.paginate((getDatatableStart() / getDatatableLength()) + 1,
				getDatatableLength(), sb.toString(), null);
		renderDatatable(page);
	}
	

	/**
	 * 
	 * @Title: getListMarke @Description: 获取所有数据坐标点 @param 设定文件 @return void
	 * 返回类型 @user James w.s @create_time 2016下午1:42:09 @throws
	 */
	public void getListMarke() {

	}

	/**
	 * 添加景点 <功能详细描述> [参数说明]
	 * 
	 * @return void [返回类型说明]
	 * @exception throws
	 *                [违例类型] [违例说明]
	 * @see [类、类#方法、类#成员]
	 */
	public void addAttr() {
		try {
			Attractions attr = new Attractions();
			attr.setLatitude(getPara("latitude"));
			attr.setLongitude(getPara("longitude"));
			setAttr("attr", attr);
			// 缩放信息
//			setAttr("centerLng",getPara("centerLng"));
//			setAttr("centerLat",getPara("centerLat"));
//			setAttr("zoom",getPara("zoom"));
			render("add.html");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @Title: saveAttr @Description: 添加数据 @param 设定文件 @return void
	 * 返回类型 @datetiem 2016下午11:43:57 @throws
	 */
	@Before(POST.class)
	public void saveAttr() {
		try {
			Attractions attr = getBean(Attractions.class);
			if(LvoutcityUtils.isNull(attr)){
				renderJson("error", "数据异常!请重新添加!");
				return;
			}
		
			if ( LvoutcityUtils.isNotNull(attr.getId())) {
				Attractions bean = getBean(Attractions.class);
				if (LvoutcityUtils.isNotNull(bean)) {
					Attractions newBean = Attractions.dao.findById(bean.getId());
					if (LvoutcityUtils.isNotNull(bean.getName())) {
						newBean.set("name", bean.getName());
					}
					if (LvoutcityUtils.isNotNull(bean.getDistance())) {
						newBean.set("distance", bean.getDistance());
					}
					if (LvoutcityUtils.isNotNull(bean.getLongitude())) {
						newBean.set("longitude", bean.getLongitude());
					}
					if (LvoutcityUtils.isNotNull(bean.getLatitude())) {
						newBean.set("latitude", bean.getLatitude());
						newBean.setAddress(MapJson(attr.getLongitude(), attr.getLatitude())[2]);
						newBean.setCity(MapJson(attr.getLongitude(), attr.getLatitude())[0]);
						newBean.setProvince(MapJson(attr.getLongitude(), attr.getLatitude())[1]);
					}
					if (LvoutcityUtils.isNotNull(bean.getEnabled())) {
						newBean.set("enabled", bean.getEnabled());
					}
					if (LvoutcityUtils.isNotNull(bean.getAllowPush())) {
						newBean.set("allow_push", bean.getAllowPush());
					}
					if (LvoutcityUtils.isNotNull(bean.getDescription())) {
						newBean.set("description", bean.getDescription());
					}
					if (LvoutcityUtils.isNotNull(bean.getIsDelete())) {
						newBean.set("is_delete", bean.getIsDelete());
					}
					if(LvoutcityUtils.isNotNull(bean.getRemark())){
						newBean.set("remark", bean.getRemark());
					}
					newBean.update();
					HashMap<String,Object> ret = new HashMap<String,Object>();
					ret.put("success", "操作成功!");
					ret.put("attr", attr);
					renderJson(ret);
				}
			} else {
				attr.setId(LvoutcityUtils.getUUID());
				attr.setCreateTime(new Date());
				attr.setIsDelete("1");
				attr.setClubId(getSessionAttr("clubId").toString());
				if(validationName(attr)){
					renderJson("error", "景点名称相同!");
					return;
				}
				String [] mapJson = MapJson(attr.getLongitude(), attr.getLatitude());
				attr.setAddress(mapJson[2]);
				attr.setCity(mapJson[0]);
				attr.setProvince(mapJson[1]);
				attr.setCreateUser(getSessionAttr("current_user_id").toString());
				attr.setType("1");
				attr.save();
				HashMap<String,Object> ret = new HashMap<String,Object>();
				ret.put("success", "添加成功!");
				ret.put("attr", attr);
				renderJson(ret);
			}
			try {
				Protocol ds = Protocol.instance();
				Redis.use().getJedis().geoadd(getSessionAttr("clubId"), Double.valueOf(attr.getLongitude()),Double.valueOf(attr.getLatitude()), attr.getId()+"_"+attr.getName()+"_"+ds.getNotice()+"_"+attr.getRemark()+"_"+attr.getDescription());
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
			renderJson("error", "操作失败");
		}
	}

	/**
	 * 
	 * @Title: editAttr 
	 * @Description: 修改景点信息
	 * @param     设定文件 
	 * @return void    返回类型 
	 * @create_time 2016下午7:28:51
	 * @create_user James w.s
	 * @throws
	 */
	public void editAttractions(){
		System.out.println(getPara("id"));
		Attractions attr = Attractions.dao.findById(getPara("id"));
		setAttr("attr", attr);
		render("add.html");
	}
	
	public String[] MapJson(String lng,String lat){
		String d = HttpUtils.getHttpURLGet("http://restapi.amap.com/v3/geocode/regeo?key=9e039affcbfc4476e0315ff422a832fb&location="+lng+","+lat+"&radius=1000&extensions=base&batch=false&roadlevel=1");
		JSONObject ad = JSONObject.parseObject(d);
		JSONObject ad2 = JSONObject.parseObject(ad.get("regeocode").toString());
		System.out.println(ad2);
		JSONObject ad3 = JSONObject.parseObject(ad2.get("addressComponent").toString());
		System.out.println(ad3);
		JSONObject ad4 = JSONObject.parseObject(ad3.toJSONString());
		return new String [] {LvoutcityUtils.isNotNull(ad4.get("city")) && !ad4.get("city").toString().equals("[]") ? ad4.get("city").toString() : ad4.get("province").toString(),LvoutcityUtils.isNotNull(ad4.get("province")) ? ad4.get("province").toString(): null,LvoutcityUtils.isNotNull(ad4.get("township")) ? ad4.get("township").toString() : null};
	}
	/**
	 * 
	 * @Title: delete @Description: 删除标点 @param 设定文件 @return void
	 * 返回类型 @create_time 2016下午2:10:59 @create_user James w.s @throws
	 */
	public void delete() {
		Attractions bean = Attractions.dao.findById(getPara("id"));
		if (LvoutcityUtils.isNotNull(bean)) {
			bean.deleteById(bean.getId());
			
			Protocol ds = Protocol.dao.findFirst("select * from t_protocol d where 1=1 ");
			Redis.use().getJedis().zrem("attrasinfo", bean.getId()+"_"+bean.getName()+"_"+ds.getNotice()+"_"+bean.getRemark()+"_"+bean.getDescription());

			renderJson("success", "删除成功!");
			return;
		}
		renderJson("error", "删除失败!");
	}

	/**
	 * 
	 * @Title: edit @Description: 修改景点 @param 设定文件 @return void
	 * 返回类型 @create_time 2016下午2:16:21 @create_user James w.s @throws
	 */
	public void editAttr() {
		Attractions bean = getBean(Attractions.class);
		if (LvoutcityUtils.isNotNull(bean)) {
			Attractions newBean = Attractions.dao.findById(bean.getId());
			if (LvoutcityUtils.isNotNull(bean.getName())) {
				newBean.set("name", bean.getName());
			}
			if (LvoutcityUtils.isNotNull(bean.getDistance())) {
				newBean.set("distance", bean.getDistance());
			}
			if (LvoutcityUtils.isNotNull(bean.getLongitude())) {
				newBean.set("longitude", bean.getLongitude());
			}
			if (LvoutcityUtils.isNotNull(bean.getLatitude())) {
				newBean.set("latitude", bean.getLatitude());
			}
			if (LvoutcityUtils.isNotNull(bean.getEnabled())) {
				newBean.set("enabled", bean.getEnabled());
			}
			if (LvoutcityUtils.isNotNull(bean.getAllowPush())) {
				newBean.set("allow_push", bean.getAllowPush());
			}
			if (LvoutcityUtils.isNotNull(bean.getDescription())) {
				newBean.set("description", bean.getDescription());
			}
			if(LvoutcityUtils.isNotNull(bean.getIsDelete())){
				newBean.set("is_delete", bean.getIsDelete());
			}
			Protocol ds = Protocol.dao.findFirst("select * from t_protocol d where 1=1 ");
			Redis.use().getJedis().geoadd(getSessionAttr("clubId"), Double.valueOf(newBean.getLongitude()),Double.valueOf(newBean.getLatitude()), newBean.getId()+"_"+newBean.getName()+"_"+ds.getNotice()+"_"+newBean.getRemark()+"_"+newBean.getDescription());

			newBean.update();
			renderJson("success", "修改成功!");
		}
	}

	/**
	 * . @Title: view @Description:查看详情 @param 设定文件 @return void
	 * 返回类型 @create_time 2016下午2:16:01 @create_user James w.s @throws
	 */
	public void view() {

	}
	
	/**
	 * 
	 * @Title: valiDateName 
	 * @Description: 景点名称校验
	 * @param     设定文件 
	 * @return void    返回类型 
	 * @create_time 2016上午9:37:37
	 * @create_user James w.s
	 * @throws
	 */
	public boolean validationName(Attractions bean){
		if(LvoutcityUtils.isNotNull(bean.getName())){
			return LvoutcityUtils.isNotNull(Attractions.dao.findFirst("select * from t_attractions t where t.name= '"+bean.getName()+"' and t.club_id='"+bean.getClubId()+"'")) ? true : false;
		}
		return true;
	}
	

	public void getAttr(){
		Attractions attr = Attractions.dao.findById(getPara("id"));
		renderJson("attr",attr);
	}
}
