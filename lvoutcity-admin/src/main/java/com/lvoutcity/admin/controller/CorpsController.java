package com.lvoutcity.admin.controller;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.jfinal.core.JFinal;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.lvoutcity.admin.utils.LcBottonUtils;
import com.lvoutcity.core.OSS.ApiHttpClient;
import com.lvoutcity.core.OSS.OssClient;
import com.lvoutcity.core.api.LogDescription;
import com.lvoutcity.core.base.BaseController;
import com.lvoutcity.core.commonDefine.CommonSql;
import com.lvoutcity.core.util.Constants;
import com.lvoutcity.core.util.ExportExcel;
import com.lvoutcity.core.util.LvoutcityUtils;
import com.lvoutcity.model.ChatGroup;
import com.lvoutcity.model.ChatGroupUser;
import com.lvoutcity.model.Corps;
import com.lvoutcity.model.CorpsLeader;
import com.lvoutcity.model.Guide;
import com.lvoutcity.model.RouteMag;
import com.lvoutcity.model.Travelogue;
import com.lvoutcity.model.User;

import gridImage.GridImage;
import io.rong.models.FormatType;
import io.rong.models.InformationNotificationMessage;
import io.rong.models.SdkHttpResult;

public class CorpsController extends BaseController {

	
	
	public void index() {
		setAttr("functBtn", LcBottonUtils.getButtonOptions("777",getRequest()));
		setAttr("route_id",getPara("route_id"));
		setAttr("route_no",getPara("route_no"));
		setAttr("club_id",getPara("club_id"));
	}

	/**
	 * 新增团期页面跳转
	 */
	public void add(){
		String route_id = getPara("route_id");
		setAttr("route_id",route_id);
		setAttr("club_id",getPara("club_id"));
		setAttr("type",getPara("type"));
		setAttr("existed_times",Corps.dao.find("select mission_time from t_corps where is_delete = '1' and route_id = '"+route_id+"'"));
		// ↓编辑时有这个数据，为了防止freemarker报错这里加上
		setAttr("corp",""); 
		setAttr("leaders",""); 
	}
	
	/**
	 * 团期编辑
	 */
	public void edit(){
		String id = getPara("id");
		String route_id = getPara("route_id");
		setAttr("route_id",route_id);
		setAttr("club_id",getPara("club_id"));
		setAttr("type",getPara("type"));
		setAttr("corp",Corps.dao.findById(id));
		setAttr("leaders",Db.findFirst("select GROUP_CONCAT(nick_name) as leader_names,GROUP_CONCAT(u.id) as leader_ids from t_corps_leader cl left join t_user u on cl.leader_id = u.id where corps_id='"+id+"' group by corps_id"));
		render("add.html");
	}
	
	/**
	 * 加载领队列表
	 */
	public void leader(){
		setAttr("route_id",getPara("route_id"));
		setAttr("club_id",getPara("club_id"));
		setAttr("leaders",getPara("leaders").split(","));
	}
	
	/**
	 *  查看团期的领队
	 */
	public void showLeader(){
		if ("POST".equalsIgnoreCase(getRequest().getMethod().toUpperCase())){
			
		}else{
			setAttr("id",getPara("id"));
			render("_leader.html");
		}
	}
	/**
	 * 加载团期列表
	 */
	public void corps() {
		HashMap<String, String> params = new HashMap<String, String>();
		if (getPara("route_id") != null && !getPara("route_id").isEmpty())
			params.put("route_id", getPara("route_id"));
		if (getPara("status") != null && !getPara("status").isEmpty())
			params.put("status", getPara("status"));
		
		renderDatatableFromDb(Corps.conditionedPaginateAsc(params, (getDatatableStart() / getDatatableLength()) + 1,
				getDatatableLength(), getDatatableOrderCol(), getDatatableOrderDir()));
	}


	/**
	 * 批量新增团期
	 */
	@LogDescription("批量新增团期")
	public void create() {
		try {
			String d = getPara("dates");
			String[] dates = d.split(",");
			String[] leaders = getPara("leader_id").split(","); 
			RouteMag route = RouteMag.dao.findById(getPara("route_id"));
			Guide g = Guide.dao.findById(route.getGuideId());
			int day = g.dayCount();
			for(String date : dates){
				Corps c = new Corps();
				c.setId(LvoutcityUtils.getUUID());
				c.setRouteId(getPara("route_id"));
				c.setStock(getParaToInt("stock"));
				c.setMissionTime( new java.text.SimpleDateFormat("yyyy-MM-dd").parse(date.trim()));
				c.setChildPrice(BigDecimal.valueOf(Float.valueOf(getPara("child_price"))));
				c.setNowThePrice(BigDecimal.valueOf(Float.valueOf(getPara("now_the_price"))));
				c.setOriginalPrice(BigDecimal.valueOf(Float.valueOf(getPara("original_price"))));
				// 处理开始时间和结束时间
				int st = getParaToInt("sign_up_begin");
				int et = getParaToInt("sign_up_end");
				c.setSignUpBegin(st);
				c.setSignUpEnd(et);
				Calendar cld_st = Calendar.getInstance();
				cld_st.setTime(c.getMissionTime());
				cld_st.add(Calendar.DAY_OF_MONTH, 0-st);
				Calendar cld_et = Calendar.getInstance();
				cld_et.setTime(c.getMissionTime());
				cld_et.add(Calendar.DAY_OF_MONTH,0-et);
				Calendar cld_me = Calendar.getInstance();//计算 mission end
				cld_me.setTime(c.getMissionTime());
				cld_me.add(Calendar.DAY_OF_MONTH, day-1);
				// 计算好了的报名时间
				cld_st.set(Calendar.HOUR_OF_DAY, 0);
				cld_st.set(Calendar.MINUTE, 0);
				cld_st.set(Calendar.SECOND, 0);
				c.setStartTime(cld_st.getTime());
				cld_et.set(Calendar.HOUR_OF_DAY, 23);
				cld_et.set(Calendar.MINUTE, 59);
				cld_et.set(Calendar.SECOND, 59);
				c.setEndTime(cld_et.getTime());
				// 团期结束日期
				c.setMissionEnd(cld_me.getTime());
				//入库
				c.save();
				// 绑定领队
				bindLeaders(leaders,c.getId());
				// 把领队加群
//				addToIMGroup(c,leaders);

			}
			
			
			renderJson("result","success");
		} catch (Exception e) {
			e.printStackTrace();
			renderJson("result",e.getMessage());
		}
	}
	
	// IM加群
	public void addToIMGroup(Corps c, String[] ids) throws Exception {
			RouteMag route =RouteMag.dao.findById(c.getRouteId()); 
			List<String> im_users = Arrays.asList(ids);
			SdkHttpResult result = ApiHttpClient.createGroup(im_users, c.getId(), c.groupName(), FormatType.json);
			
//			 测试代码，打印一下群里的用户
			SdkHttpResult rs = ApiHttpClient.queryGroupUserList(c.getId(),FormatType.json);
			System.out.println(rs.getResult());
			if (result.getHttpCode() == 200) { 
				ChatGroup cg = ChatGroup.dao.findById(c.getId());
				if(cg==null){
					// 创建群记录
					cg = new ChatGroup();
					cg.setGroupName(c.groupName());
					cg.setId(c.getId());
					cg.setSystemGroup(Constants.GROUP.CORP_GROUP);
					cg.setCreateUser("0");
					cg.setRouteId(c.getRouteId());
					cg.setCorpsId(c.getId());
					cg.setGuideId(route.getGuideId());
					cg.save();
				}
				//创建群组用户记录
				StringBuffer sb = new StringBuffer("("); 
				Arrays.asList(ids).forEach(id->sb.append("'").append(id).append("',"));
				sb.deleteCharAt(sb.length()-1);
				sb.append(")");
				List<User> newLeaders = User.dao.find("select u.* from t_user u left join  t_chat_group_user cgu on (u.id=cgu.user_id and cgu.group_id='"+c.getId()+"') where cgu.id is null and u.id in "+sb.toString()); 
				StringBuffer un = new StringBuffer();
				for(User u : newLeaders){
					SdkHttpResult r = ApiHttpClient.joinGroup(u.getId(),cg.getId(),cg.getGroupName(),FormatType.json);
					if(r.getHttpCode()==200){
						ChatGroupUser cgu = new ChatGroupUser(u,cg);
						cgu.setStatus(Constants.GROUP_USER.ADMIN);
						cgu.save();
						un.append(u.getNickName()).append(" ");
					}
				}
				//群头像
				cg.setGroupAvatar(generateGrid(cg));
				cg.update();
				//发通知
				if(!un.toString().isEmpty()){
					List<String> gid = new ArrayList<String>();
					gid.add(cg.getId());
					ApiHttpClient.publishGroupMessage("0", gid, new InformationNotificationMessage(un.append("加入了该群。").toString()), "", "", FormatType.json);
				}
			}
	}
	
	/**
	 * 生成头像并上传
	 * @param avatars
	 * @return
	 */
	public String generateGrid(ChatGroup cg){
		OssClient client = new OssClient();
		List<String> img_files = new ArrayList();
		List<String> avatars = cg.topNineAvatar();
		avatars.forEach(avt->{
			try {
				String[] parts = avt.split("/");
				String key = parts[parts.length - 1].split("@")[0];
				client.getObject("avatar/" + key, key, Constants.MINI_AVATAR_STYLE);
				img_files.add(key);
			} catch (Exception e) {
				String[] parts = Constants.DEFAULT_USER_AVATAR.split("/");
				String key = parts[parts.length - 1].split("@")[0];
				img_files.add(key);
				e.printStackTrace();
			}
		});
		String avatar_name =   cg.getId()+"."+GridImage.DEFAULT_TYPE;
		String path = GridImage.mergeGrid(img_files.toArray(new String[0]),"../webapps/"+JFinal.me().getContextPath()+"/temp/"+avatar_name);
		String url = client.uploadFile(avatar_name, path);
		return url.replaceAll(client.endpoint, client.imgProcessEndPoint)+ "@"+Constants.USER_AVATAR_STYLE;
	}

	/**
	 * 更新团期记录
	 */
	@LogDescription("修改团期")
	public void update(){
		try {
			String id = getPara("id");
			String[] leaders = getPara("leader_id").split(","); 
			Corps c = Corps.dao.findById(id);
			c.setStock(getParaToInt("stock"));
			if(c.getStatus().equals("3"))//如果是下架状态编辑就直接进入待审核状态
				c.setStatus("1");
			
			c.setChildPrice(BigDecimal.valueOf(Float.valueOf(getPara("child_price"))));
			c.setNowThePrice(BigDecimal.valueOf(Float.valueOf(getPara("now_the_price"))));
			c.setOriginalPrice(BigDecimal.valueOf(Float.valueOf(getPara("original_price"))));
			// 根据提前的天数处理开始时间和结束时间---
			int st = getParaToInt("sign_up_begin");
			int et = getParaToInt("sign_up_end");
			c.setSignUpBegin(st);
			c.setSignUpEnd(et);
			Calendar cld_st = Calendar.getInstance();
			cld_st.setTime(c.getMissionTime());
			cld_st.add(Calendar.DAY_OF_MONTH, 0-st);
			Calendar cld_et = Calendar.getInstance();
			cld_et.setTime(c.getMissionTime());
			cld_et.add(Calendar.DAY_OF_MONTH,0-et);
			// 计算好了的报名时间
			cld_st.set(Calendar.HOUR_OF_DAY, 0);
			cld_st.set(Calendar.MINUTE, 0);
			cld_st.set(Calendar.SECOND, 0);
			c.setStartTime(cld_st.getTime());
			cld_et.set(Calendar.HOUR_OF_DAY, 23);
			cld_et.set(Calendar.MINUTE, 59);
			cld_et.set(Calendar.SECOND, 59);
			c.setEndTime(cld_et.getTime());
			//入库
			c.update();
			// 绑定领队
			bindLeaders(leaders,c.getId());
			renderJson("result","success");
			// 把领队加群
//			addToIMGroup(c,leaders);
		}catch(Exception e){
			e.printStackTrace();
			renderJson("result",e.getMessage());
		}
	}
	
	/**
	 * 绑定领队
	 * @param leaders
	 * @param corp_id
	 */
	private void bindLeaders(String[] leaders,String corp_id){
		//删除已绑
		CorpsLeader.dao.deleteByCorpId(corp_id);
		if(leaders.length>0){
			for(String l_id : leaders){
				CorpsLeader cl = new CorpsLeader();
				cl.setId(LvoutcityUtils.getUUID());
				cl.setCorpsId(corp_id);
				cl.setLeaderId(l_id);
				cl.save();
			}
		}
	}
	
	/**
	 * 提交
	 */
	@LogDescription("提交团期")
	public void submit(){
		HashMap<String,String> p = new HashMap<String,String>();
		p.put("refuse_reason", null);
		p.put("verify_user", null);
		updateStatus("submit",Constants.CORP.STATUS_SUBMITED,p);
	}
	/**
	 * 审核通过
	 */
	@LogDescription("审核通过团期")
	public void verify(){
		HashMap<String,String> p = new HashMap<String,String>();
		p.put("verify_user", getCurrentUserId());
		updateStatus("verify",Constants.CORP.STATUS_ON_SHELF,p);
	}
	/**
	 * 驳回
	 */
	@LogDescription("驳回团期")
	public void refuse(){
		HashMap<String,String> p = new HashMap<String,String>();
		p.put("refuse_reason", getPara("reason"));
		p.put("verify_user", getCurrentUserId());
		updateStatus("refuse",Constants.CORP.STATUS_CREATED,p);
	}
	/**
	 * 撤销
	 */
	@LogDescription("撤销团期")
	public void revoke(){
		HashMap<String,String> p = new HashMap<String,String>();
		p.put("refuse_reason", null);
		p.put("verify_user", null);
		updateStatus("revoke",Constants.CORP.STATUS_CREATED,p);
	}
	/**
	 * 上架
	 */
	@LogDescription("上架团期")
	public void onShelf(){
		updateStatus("on_shelf",Constants.CORP.STATUS_ON_SHELF,null);
	}
	/**
	 * 下架
	 */
	@LogDescription("下架团期")
	public void offShelf(){
		updateStatus("off_shelf",Constants.CORP.STATUS_OFF_SHELF,null);
	}
	
	/**
	 * 删除（批量）
	 */
	@LogDescription("删除团期")
	public void delete() {
		try {
			String[] ids= getPara("ids").split(",");
			List<Corps> clist = new ArrayList<Corps>();
			for(String id:ids){
				Corps c = Corps.dao.findById(id);
				if(availableOperations(c).contains("delete")){
					c.setIsDelete("0");
					c.update();
					clist.add(c);
				}
			}
			// 需不需要级联删除？
			HashMap<String,String> map = new HashMap<String,String>();
			map.put("result","success");
			map.put("count",String.valueOf(clist.size()));
			renderJson(map);
		} catch (Exception e) {
			e.printStackTrace();
			renderJson("result", e.getMessage());
		}

	}
	/**
	 * 获取该团期当前允许的操作列表
	 * @param c  
	 * @return
	 */
	private ArrayList<String> availableOperations(Corps c){
		ArrayList<String> list = new ArrayList<String>();
		switch(c.getStatus()){
			case "0"://待提交
				list.add("edit");
				list.add("submit");
				list.add("delete");
				break;
			case "1"://待审核
				list.add("verify");
				list.add("refuse");
				if(!c.hasOrder())
					list.add("revoke");
				break;
			case "2"://已上架
				if(!c.hasOrder())
					list.add("revoke");
				
				list.add("off_shelf");
				break;
			case "3"://已下架
				list.add("edit");
				list.add("on_shelf");
				break;  
		}
		return list;
	}
	
	/**
	 * 更新团期状态
	 * @param oper 操作
	 * @param status 目标状态
	 * @param params 额外需要更新的字段 key，value
	 */
	private void updateStatus(String oper,String status,HashMap<String,String> params){
		try {
			String[] ids= getPara("ids").split(",");
			int count=0;
			for(String id:ids){
				Corps c = Corps.dao.findById(id);
				if(status==Constants.CORP.STATUS_ON_SHELF){
					List<CorpsLeader> cl = CorpsLeader.dao.find("select * from t_corps_leader where corps_id='"+id+"'"); 
					String[] leaders = new String[cl.size()];
					for(int i=0;i<cl.size();i++){
						leaders[i]=cl.get(i).getLeaderId();
					}
					addToIMGroup(c, leaders);
				}
				if(availableOperations(c).contains(oper)){
					c.setStatus(status);
					if(params!=null){
						params.entrySet().forEach(map -> c.set(map.getKey(),map.getValue()));
					}
					c.update();
					count++;
				}
			}
			HashMap<String,String> map = new HashMap<String,String>();
			map.put("result","success");
			map.put("count",String.valueOf(count));
			renderJson(map);
		}catch(Exception e){
			e.printStackTrace();
			renderJson("result", e.getMessage());
		}
	}
	
	/**
	 * 获取团期的成员
	 * @throws Exception 
	 */
	public void members() throws Exception{
		if ("POST".equalsIgnoreCase(getRequest().getMethod().toUpperCase())){
			String id = getPara("id");
			String tourist_type = getPara("tourist_type");
			String phone = getPara("phone");
			String select_sql = "select ti.*,u.id as user_id,ord.status,ord.order_no, pu.nick_name as payer_name, pu.phone_number as payer_phone_number ";
			String sql = "from t_tourist_info ti left join t_order_record ord on ord.id = ti.order_id left join t_user pu on pu.id = ord.create_user left join  t_user u ON u.phone_number = ti.phone"
					+ "  where ord.is_delete = '1' "
					+ "  and ord.status not in ('"+Constants.ORDER.STATUS_CANCLED_BACK+"','"+Constants.ORDER.STATUS_CANCLED_USER+"')"
					+ "  and ti.corps_id = '"+id+"'";
			if(LvoutcityUtils.isNotNull(phone))
				sql+=" and phone='"+phone+"'";
			if(LvoutcityUtils.isNotNull(tourist_type))
				sql+=" and tourist_type='"+tourist_type+"'";
			sql += " order by ord.create_time desc";
			renderDatatableFromDb(Db.paginate((getDatatableStart() / getDatatableLength()) + 1, getDatatableLength(), select_sql, sql));
		}else{
			setAttr("id",getPara("id"));
			SdkHttpResult rs = ApiHttpClient.queryGroupUserList(getPara("id"),FormatType.json);
			Object ob =  JSON.parse(rs.getResult());
			setAttr("im_result",ob);
		}
	}
	
	/**
	 * 把成员加入群
	 * @throws Exception
	 */
	@LogDescription("团期人员加群")
	public void joinGroup() throws Exception{
		String id = getPara("id");
		String userId = getPara("userId");
		Corps c = Corps.dao.findById(id);
		String[] ids = new String[1];
		ids[0] = userId;
		addToIMGroup(c,ids);
		renderJson("result","success");
	}
	
	/**
	 *  游记
	 */
	public void travelogue(){
		String id = getPara("id");//corps id
		Travelogue tl = Travelogue.dao.findFirst("select * from t_travelogue where corps_id = '"+id+"'");
		if(LvoutcityUtils.isNull(tl)){
			tl = new Travelogue();
			tl.setId(LvoutcityUtils.getUUID());
			tl.setCorpsId(id);
			tl.save();
		}
		setAttr("id",tl.getId());
		setAttr("travelogue",tl);
	}
	/**
	 *  存游记
	 */
	@LogDescription("保存游记")
	public void saveTravelogue(){
		String id = getPara("id");
		Travelogue tl = Travelogue.dao.findById(id);
		tl.setTitle(getPara("title"));
		tl.setContent(getPara("content"));
		tl.update();
		renderJson("result","success");
	}

	/**
	 * 成员导出
	 * @throws Exception 
	 */
	public void export() throws Exception{
		String id = getPara("id");
		String tourist_type = getPara("tourist_type");
		String phone = getPara("phone");
		String select_sql = "select ti.*,pu.nick_name as payer_name, pu.phone_number as payer_phone_number,"
				+ "  case when ti.tourist_type='1' then '成人' else '儿童' end as type,"
				+ " case when ord.status='0' then '待支付' "
				+ " when ord.status='1' then '已支付' "
				+ " when ord.status='2' then '行程中' "
				+ " when ord.status='3' then '行程结束' "
				+ " when ord.status='4' then '退款待审核' "
				+ " when ord.status='5' then '订单取消（用户）' "
				+ " when ord.status='6' then '订单取消（后台）' "
				+ "else '' end as status ";
		String sql = "from t_tourist_info ti left join t_order_record ord on ord.id = ti.order_id left join t_user pu on pu.id = ord.create_user left join  t_user u ON u.id = ti.user_id"
				+ "  where ord.is_delete = '1' "
				+ "  and ord.status not in ('"+Constants.ORDER.STATUS_CANCLED_BACK+"','"+Constants.ORDER.STATUS_CANCLED_USER+"')"
				+ "  and ti.corps_id = '"+id+"'";
		if(LvoutcityUtils.isNotNull(phone))
			sql+=" and phone='"+phone+"'";
		if(LvoutcityUtils.isNotNull(tourist_type))
			sql+=" and tourist_type='"+tourist_type+"'";
		//renderDatatableFromDb(Db.paginate((getDatatableStart() / getDatatableLength()) + 1, getDatatableLength(), select_sql, sql));
		// 查询线路和团期
		Record rd = Db.findFirst("SELECT DATE_FORMAT(t1.mission_time, '%Y-%m-%d')as mission_time,t2.route_name FROM  t_corps t1 LEFT JOIN  t_route_mag t2 ON t1.route_id = t2.id where t1.id='"+id+"'");
		String name ="";
		name = rd.getStr("route_name")+"-"+rd.getStr("mission_time")+"-成员";
		Record rc = Db.findFirst(" select count(1) as total "+sql);
		// 计算需要导出的总数
		long num = rc.getLong("total");
		String fileName = "../webapps/"+getRequest().getContextPath()+"/temp/"+name+LvoutcityUtils.formateDate(Constants.DATE.DATE_EXPORT)+".xls";
		//fileName = "E:"+File.separator+name+LvoutcityUtils.formateDate(Constants.DATE.DATE_EXPORT)+".xls";		
		ExportExcel et = new ExportExcel();
		et.createWork(fileName);
		String[] header = { "成员编号", "真实姓名", "类别", "身份证","是否支付", "手机号", "订单支付人","支付人联系方式" };
		String[] key = { "tourist_no", "real_name","type", "id_number","status", "phone", "payer_name","payer_phone_number"};
		try{			
			// 遍历total
			
			for(long i =0;i<num/Constants.ROW_NUMBER+1;i++){
				String limit = " limit "+i*Constants.ROW_NUMBER+","+Constants.ROW_NUMBER+" ";
				List<Record>  lt  = Db.find(select_sql+sql+limit);
				et.exportExcel(fileName,"xx", header,key, lt, i*Constants.ROW_NUMBER+1, i);
			}
				
		}catch(Exception e){
			e.getMessage();
		}
		renderFile(new File(fileName));
		
	}
}
