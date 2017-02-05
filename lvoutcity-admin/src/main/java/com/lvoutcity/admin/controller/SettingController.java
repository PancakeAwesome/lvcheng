package com.lvoutcity.admin.controller;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.redis.Redis;
import com.lvoutcity.core.api.LogDescription;
import com.lvoutcity.core.base.BaseController;
import com.lvoutcity.core.util.Constants;
import com.lvoutcity.core.util.LvoutcityUtils;
import com.lvoutcity.model.Activity;
import com.lvoutcity.model.AppLoginImg;
import com.lvoutcity.model.HotCity;
import com.lvoutcity.model.Protocol;
import com.lvoutcity.model.TagMag;

public class SettingController extends BaseController {

	public void index() {
		setAttr("protocol",Protocol.instance());
	}
    
	/**
	 * @description 获取热门城市
	 */
	
	public void hotCity() {
		if (getRequest().getMethod().toUpperCase().equals("POST")) {
			String sql = " from t_hot_city where is_delete=1";
			if (LvoutcityUtils.isNotNull(getPara("h_name"))) {
				sql = sql + " and h_name like '%" + getPara("h_name") + "%'";
			}
			renderDatatable(HotCity.dao.paginate((getDatatableStart() / getDatatableLength()) + 1, getDatatableLength(),
					"select *", sql));
		}
	}
  
	/**
	 * 增加热门城市
	 */
	@LogDescription("增加热门城市")
	public void hotCityAdd() {
		if(getPara("type").equals("1")){
			setAttr("hotcity",HotCity.dao.findById(getPara("id")));
		}
		setAttr("type",getPara("type"));
	}
	
	@LogDescription("新增热门城市")
	public void saveHotCity() {
		try {
			HotCity hc = getBean(HotCity.class);
			if(getPara("type").equals("1")){
				hc.update();
				renderJson("result","success");
			}else if(LvoutcityUtils.isNotNull(HotCity.dao.findFirst("select * from t_hot_city where is_delete='1' and id = '"+hc.getId()+"'"))){
				renderJson("result","dupilcate");
			}else{
				hc.save();
				renderJson("result","success");
			}
		} catch (Exception e) {
			e.printStackTrace();
			renderJson("result", e.getMessage());
//			throw new RuntimeException("abc");
		}
		
		//{"sign_type":"md5","dataJSON":{"data":[{"name":"马","credentials":"身份证","idNum":"1","phone":"2","touristType":"1"},{"name":"李","credentials":"身份证","idNum":"3","phone":"4","touristType":"2"}]},"childNum":"1","adultNum":"1","folkNum":"2","corpsId":"4a4e6230a67a436aa308358dc65fe731"}

	}
	@LogDescription("删除热门城市")
	public void deleteHotCity(){
		String[] ids = getPara("ids").split(",");
		for(String id : ids){
			HotCity c = HotCity.dao.findById(id);
			//c.setIsDelete("0");
			c.delete();
		}
		renderJson("result","success");
	}
	
	@LogDescription("保存协议")
	public void saveProtocol(){
		Protocol p = Protocol.instance();
		Protocol pn = getBean(Protocol.class);
		p._setAttrs(pn);
		p.setUpdateUser(getCurrentUserId());
		p.update();
		System.out.print("asdfsd"+p.getMapMe());
		Redis.use().set("refreshInterfaceTime", p.getMapMe());
		renderJson("result","success");
	}
	
	/**
	 * 页面跳转
	 */
	public void appStart(){
		setAttr("img_last_time",Protocol.instance().getImgLastTime());
		setAttr("img_list",AppLoginImg.dao.getLogin());
	}
	/**
	 * 保存app启动画面设定的内容
	 */
	@LogDescription("保存app启动画面设定的内容")
	public void saveAppStart(){
		int img_last_time = getParaToInt("img_last_time");
		Protocol p =Protocol.instance();
		p.setImgLastTime(img_last_time);
		p.setUpdateUser(getCurrentUserId());
		p.update();
		Db.update("delete from t_app_login_img where true");
		
		
		List<HashMap<String, String>> imgList = getListMap("img_list");
		for(int i=0;i<imgList.size();i++){
			HashMap<String, String> img = imgList.get(i);
			AppLoginImg ali = new AppLoginImg();
			ali.setId(LvoutcityUtils.getUUID());
			ali.setUrl(img.get("url"));
			ali.setCreateUser(getCurrentUserId());
			ali.setAppType(img.get("appType"));
			ali.setSize(img.get("size"));
			ali.save();
		}
		
		Integer size = getParaToInt("length");
		
		renderJson("result","success");
	}
	
	
	
	/**
	 * 标签管理页面跳转
	 */
	public void tagMgr(){
		
	}
	
	/**
	 * 查找标签列表
	 */
	public void tags(){
		String sql = " from t_tag_mag where is_delete=1";
		if(LvoutcityUtils.isNotNull(getPara("name"))){
			sql = sql + " and t_name like '%"+getPara("name")+"%'";
		}
		renderDatatable(TagMag.dao.paginate((getDatatableStart() / getDatatableLength()) + 1, getDatatableLength(),
				"select *", sql));
	}
	
	@LogDescription("保存标签")
	public void saveTag(){
		TagMag t = getBean(TagMag.class);
		if(LvoutcityUtils.isNotNull(t.getId())){
			t.update();
		}else{
			t.setId(LvoutcityUtils.getUUID());
			t.save();
		}
		
		renderJson("result","success");
	}
	
	@LogDescription("删除标签")
	public void deleteTags(){
		String[] ids = getParaValues("ids[]");
		for(String id:ids){
			TagMag t = TagMag.dao.findById(id);
			t.setIsDelete(Constants.ISDELETE_TRUE);
			t.update();
		}
		renderJson("result","success");
	}
	
	public void activity(){
		Activity reg_act = Activity.getRegisterActivity();
		Activity inv_act = Activity.getInviterActivity();
		setAttr("reg_act",reg_act);
		setAttr("inv_act",inv_act);
	}
	
	@LogDescription("保存活动设定")
	public void saveActivity() throws ParseException{
		List<HashMap<String, String>> data = getListMap("data");
		SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE.DATE_DAY);
		for(HashMap<String, String> d : data){
			Activity act = null;
			if(d.get("target").equals("inv"))
				act = Activity.getInviterActivity();
			else
				act = Activity.getRegisterActivity();
			
			if(d.get("enable")==null)
				act.setEnable("0");
			else if(d.get("enable")!=null)
				act.setEnable(d.get("enable"));
			
			Calendar c = Calendar.getInstance();
			if(LvoutcityUtils.isNotNull(d.get("startDate"))){
				Date start = sdf.parse(d.get("startDate"));
				c.setTime(start);
				c.set(Calendar.HOUR_OF_DAY, 0);
				c.set(Calendar.MINUTE, 0);
				c.set(Calendar.SECOND, 0);
				act.setStartDate(c.getTime());
			}
			if(LvoutcityUtils.isNotNull(d.get("endDate"))){
				Date end = sdf.parse(d.get("endDate"));
				c.setTime(end);
				c.set(Calendar.HOUR_OF_DAY, 23);
				c.set(Calendar.MINUTE, 59);
				c.set(Calendar.SECOND, 59);
				act.setEndDate(c.getTime());
			}
			if(act.getStartDate()!=null&&act.getEndDate()!=null){
				if (act.getStartDate().compareTo(act.getEndDate()) > 0) {
					renderJson("result", "开始时间请勿晚于结束时间！");
					return;
				}
			}
			
			if(LvoutcityUtils.isNotNull(d.get("goldReg")))
				act.setGoldReg(BigDecimal.valueOf(Double.valueOf(d.get("goldReg"))));
			if(LvoutcityUtils.isNotNull(d.get("goldLogin")))
				act.setGoldLogin(BigDecimal.valueOf(Double.valueOf(d.get("goldLogin"))));
			if(LvoutcityUtils.isNotNull(d.get("pointReg")))
				act.setPointReg(Integer.valueOf(d.get("pointReg")));
			if(LvoutcityUtils.isNotNull(d.get("pointLogin")))
				act.setPointLogin(Integer.valueOf(d.get("pointLogin")));
			if(LvoutcityUtils.isNotNull(d.get("quota")))
				act.setQuota(Integer.valueOf(d.get("quota")));
			
			act.setUpdateUser(getCurrentUserId());
			act.update();
		}
		renderJson("result","success");
	}
	
}
