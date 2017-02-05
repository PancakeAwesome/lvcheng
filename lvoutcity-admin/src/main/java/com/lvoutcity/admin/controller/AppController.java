package com.lvoutcity.admin.controller;

import java.util.Date;

import com.jfinal.plugin.activerecord.Db;
import com.lvoutcity.admin.controller.bean.QuestionAppBean;
import com.lvoutcity.admin.utils.WebUtils;
import com.lvoutcity.core.base.BaseController;
import com.lvoutcity.core.util.Constants;
import com.lvoutcity.core.util.LvoutcityUtils;
import com.lvoutcity.core.util.SQuery;
import com.lvoutcity.model.Feedback;
import com.lvoutcity.model.User;
import com.lvoutcity.model.UserBackgroup;

/**
 * @author zhanghd
 *time 2016-5-10
 */

public class AppController extends BaseController {

	/**
	 * 默认方法
	 */
	public void index() {
		render("appQAB-list.html");
}

	/**
	 * 查询APP问题反馈信息
	 */
	public void selectApp(){

		try {
			SQuery sql = SQuery.create();
			sql.append("select f.id, u.user_no,u.nick_name,u.real_name,u.phone_number, ");
			sql.append(" f.content,f.status,f.confirmer,b.user_name,date_format(f.confirm_time,'%Y-%m-%d %H:%i:%s') as confirm_time,date_format(f.create_time,'%Y-%m-%d %H:%i:%s') as create_time ");
			sql.append(" from t_feedback f  ");
			sql.append(" left join t_user u ");
			sql.append(" on u.id = f.user_id");
			sql.append(" left join t_user_backgroup b ");
			sql.append(" on b.user_id = f.confirmer ");
			sql.append(" where 1=1 ");
			QuestionAppBean questionAppBean = getBean(QuestionAppBean.class);
			if(LvoutcityUtils.isNotNull(questionAppBean.getStatus()) && !"-1".equals(questionAppBean.getStatus())){
				sql .append(" and f.status = "+questionAppBean.getStatus() +" ") ;
			}
			if(questionAppBean.getPhoneNumber()!=null){
				sql.append("  and u.phone_number ="+questionAppBean.getPhoneNumber() +" ");
			}
			sql.append(" order by f.create_time desc ");
			renderDatatable(LvoutcityUtils.getPage(Db.paginate((getDatatableStart() / getDatatableLength()) + 1, getDatatableLength(),
					sql.isSql(),  sql.isFrom())));//sql.getSQuery()+" order by f.create_time desc"
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}



	/**
	 * 查看用户详细信息
	 */
	public void appShow(){

		try {
			setAttr("type",2);
			String id = getPara("id");
			Feedback feedback = Feedback.dao.findById(id);
			User user = User.dao.findById(feedback.getUserId());
			//判断用户是否存在
			if(LvoutcityUtils.isNotNull(feedback.getConfirmer())){
				UserBackgroup user2 = User.dao.queryUserId(feedback.getConfirmer());
				feedback.setConfirmUser(user2.getUserName());
			}
			setAttr("user", user);
			setAttr("app",feedback);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//反查出推荐人手机号
		// 设置页面类型 1 是新增 2是查看  3 是编辑*/
		render("appQShow.html");
	}





	/**
	 * 启用禁用更新
	 */
	public void upateEnable(){
		//new TourismClub().set("", "").update();
		String id = getPara("id");
		try {
			Feedback f = Feedback.dao.findById(id);
			if(LvoutcityUtils.isNotNull(f)){
				f.set("status", getPara("status"));
				f.setConfirmTime(new Date());
				f.set("confirmer", getSessionAttr("current_user_id"));
				f.update();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			renderJson("error", "操作失败!");
		}
		//getBean(TourismClub.class).save();
		renderJson("success", "操作成功!");
	}
}
