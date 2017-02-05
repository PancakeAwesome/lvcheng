package com.lvoutcity.admin.controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.upload.UploadFile;
import com.lvoutcity.core.OSS.OssClient;
import com.lvoutcity.core.base.BaseController;
import com.lvoutcity.core.util.Constants;
import com.lvoutcity.core.util.JsonData;
import com.lvoutcity.core.util.LvoutcityUtils;
import com.lvoutcity.model.Area;
import com.lvoutcity.model.DataDictionary;
import com.lvoutcity.model.PageView;
import com.lvoutcity.model.TourismClub;
import com.lvoutcity.model.UserBackgroup;

public class CommonController extends BaseController{

	public void areaList(){
		renderJson(Area.dao.find("select * from t_area where parent_id = ? order by id asc",getParaToInt(0, 1)));
	}
	
	public void selectClublist(){
		renderJson(TourismClub.dao.find("select * from t_tourism_club where enabled ='1' and is_delete='1' "));

	}
	
	public void selectClublistForAdd(){
		List<TourismClub> tb = new ArrayList<TourismClub>();
		
		 tb = 	TourismClub.dao.find("select * from t_tourism_club where enabled ='1' and is_delete='1' ");
		 tb.add(0, TourismClub.dao.findById(Constants.ADMIN_ID));
		 renderJson(tb);
	}
	public void selectClublistByUser(){
		// 获取session
		String club = getSessionAttr("clubId");//"02d8806009484975925015ef328a4450";//"0000000000000000000000000000000";//getSessionAttr("clubId");
		JsonData js = new JsonData();
		// 如果是旅城号
		if(Constants.ADMIN_ID.equals(club)){
			js.setCode("0");
			List<TourismClub> tb = 	TourismClub.dao.find("select * from t_tourism_club where enabled ='1' and is_delete='1' ");
			js.setData(tb);
		}else{
			js.setCode("1");
			List<TourismClub> tb = 	TourismClub.dao.find("select * from t_tourism_club where id='"+club+"' ");
			js.setData(tb);
		}
		renderJson(js);

	}
	
	/**
	 * 通用图片上传
	 */
	public void uploadPictures() {
		try {
			UploadFile ut = this.getFile();
			String id = LvoutcityUtils.getUUID();
			String type = "";
			String[] tyleList = ut.getOriginalFileName().split("\\.");
			if (tyleList != null && tyleList.length > 0) {
				type = tyleList[tyleList.length - 1];
			}
			OssClient oss = new OssClient();
			String url = oss.uploadFile(id + "." + type,
					ut.getUploadPath() + "/" + ut.getOriginalFileName());
			
			//添加样式
			if(LvoutcityUtils.isNotNull(getPara("style"))){
				url = url.replace("oss-cn", "img-cn");
				url = url+getPara("style");
			}
			
			if ("w".equals(getPara("type"))) {// 识别是否是富文本编辑器
				renderJson(url);
			} else {
				renderJson("url", url);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	
	/**
	 * 图片上传且检查大小是否符合
	 * @throws Exception 
	 */
	public void uploadSizeLimitedPics() throws Exception {
		try {
			UploadFile ut = this.getFile();
			String size = getPara("size");
			String filepath = ut.getUploadPath() + "/" + ut.getOriginalFileName();
			if(false && LvoutcityUtils.isNotNull(size)){
				String[] size_arr = size.split("\\*");
				int width = Integer.valueOf(size_arr[0]);
				int height = Integer.valueOf(size_arr[1]);
				BufferedImage image = ImageIO.read(new File(filepath));
				 if(image.getWidth()!=width||image.getHeight()!=height){
					 renderJson("result", "请选择大小为"+size+"的图片上传！");
					 return;
				 }
			}
			String id = LvoutcityUtils.getUUID();
			String type = "";
			String[] tyleList = ut.getOriginalFileName().split("\\.");
			if (tyleList != null && tyleList.length > 0) {
				type = tyleList[tyleList.length - 1];
			}
			OssClient oss = new OssClient();
			String url = oss.uploadFile(id + "." + type,filepath);
			if ("w".equals(getPara("type"))) {// 识别是否是富文本编辑器
				renderJson(url);
			} else {
				renderJson("url", url);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}

	}
	
	/**
	 * 
	 * @Title: selectDict 
	 * @Description: 数据字典查询
	 * @param     设定文件 
	 * @return void    返回类型 
	 * @create_time 2016下午2:56:51
	 * @create_user James w.s
	 * @throws
	 */
	public void selectDict(){
		renderJson(DataDictionary.dao.find("select * from t_data_dictionary t where 1=1 and is_delete = '1' and type_id = (select c.id from t_data_dictionary_type c where c.is_delete ='1' and c.t_code = '"+getPara("tCode")+"' )"));
		
//		DataDictionary.dao.p
	}
	
	
	/**
	 * 线路访问量接口
	 * @parm routeId 线路id
	 */
	public void viewTimes() throws Exception{
		// 
		String routeId = getPara("routeId");
		if(!LvoutcityUtils.isNotNull(routeId)){
			return;
		}
		String date = LvoutcityUtils.formateDate(Constants.DATE.DATE_DAY);
		// 查看是否存在数据，不存在新建
		PageView pv = PageView.dao.findFirst("select * from t_page_view where route_id='"+routeId+"' and date_time='"+date+"' ");
		if(pv==null){
			PageView pv1 = new PageView();
			pv1.setRouteId(routeId);
			pv1.setDateTime(new Date());
			pv1.save();
		}else{
			Db.update("update t_page_view set count = count+1 where route_id='"+routeId+"' and date_time='"+date+"' ");
		}
		renderJson("success");
	}
	
	/**
	 * 修改密码
	 * @throws Exception
	 */
	public void updatePass() throws Exception{
		String userId = getPara("id");
		String old_pass = getPara("old_pass");
		String new_pass = getPara("new_pass");
		String new_pass_again = getPara("new_pass_again");
		// 判断旧密码是否正确
		UserBackgroup ub = UserBackgroup.dao.findFirst("select * from t_user_backgroup where user_id ='" + userId + "' ");
		System.out.println(LvoutcityUtils.encryptPassword(old_pass));
		if(!LvoutcityUtils.encryptPassword(old_pass).equals(ub.getPassword())){
			renderJson("result", "error");
			return;
		}
		//更新密码
		// 加密注销掉	
		/*String sql =" update t_user_backgroup set password ='"+new_pass+"' "
				+ " where user_id ='"+userId+"' ";*/
		String sql =" update t_user_backgroup set password ='"+LvoutcityUtils.encryptPassword(new_pass)+"' "
				+ " where user_id ='"+userId+"' ";
		Db.update(sql);
		renderJson("result","success");
	}
	
	
	/**
	 * 加载节日数据
	 */
	public void loadFestival(){
		renderJson("festival",renderListFromDb(Db.find("select dd.name, dd.value from t_data_dictionary dd left join t_data_dictionary_type ddt on ddt.id = dd.type_id where ddt.t_code = 'jr' order by dd.value")));
	}
}
