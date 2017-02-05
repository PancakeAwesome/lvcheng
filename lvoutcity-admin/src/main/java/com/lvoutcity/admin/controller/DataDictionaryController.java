package com.lvoutcity.admin.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jfinal.aop.Before;
import com.jfinal.ext.interceptor.POST;
import com.jfinal.plugin.activerecord.Db;
import com.lvoutcity.core.base.BaseController;
import com.lvoutcity.core.util.Constants;
import com.lvoutcity.core.util.LvoutcityUtils;
import com.lvoutcity.core.util.SQuery;
import com.lvoutcity.core.util.ZTree;
import com.lvoutcity.model.DataDictionary;
import com.lvoutcity.model.DataDictionaryType;


/**
 * 
 * @ClassName: DataDictionary 
 * @Description: 数据字典 
 * @author James w.s
 * @date 2016年5月31日 下午1:19:15 
 *
 */

public class DataDictionaryController  extends BaseController{

	/**
	 * 
	 * @Title: index 
	 * @Description: 列表页面
	 * @param     设定文件 
	 * @return void    返回类型 
	 * @create_time 2016下午1:20:02
	 * @create_user James w.s
	 * @throws
	 */
	public void index(){
		render("index.html");
	}
	
	/**
	 * 
	 * @Title: valueIndex 
	 * @Description: value显示页面
	 * @param     设定文件 
	 * @return void    返回类型 
	 * @create_time 2016下午1:20:37
	 * @create_user James w.s
	 * @throws
	 */
	public void data(){
		
		try {
			System.out.println(getPara("typeId"));
			setAttr("typeId", getPara("typeId"));
			render("data.html");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @Title: getIndexList 
	 * @Description: 查询数据
	 * @param     设定文件 
	 * @return void    返回类型 
	 * @create_time 2016下午1:23:04
	 * @create_user James w.s
	 * @throws
	 */
	public void getIndexList(){
		try {
			SQuery sql = SQuery.create("select * from t_data_dictionary_type d ");
			sql.append(" where 1=1 and d.is_delete = '1' ");
			sql.append(" order by d.create_time desc ");
			List<DataDictionaryType> list = DataDictionaryType.dao.find(sql.getSQuery());
			
			List<ZTree> nodeList = new ArrayList<ZTree>();
			for(DataDictionaryType ar : list){
				ZTree node = new ZTree(ar.getId(),ar.getName(),ar.getSuperId());
				nodeList.add(node);
			}
			renderJson(list);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	/**
	 * 
	 * @Title: getValueList 
	 * @Description: 页面查询
	 * @param     设定文件 
	 * @return void    返回类型 
	 * @create_time 2016下午1:22:41
	 * @create_user James w.s
	 * @throws
	 */
	public void getValueList(){
		try {
			String typeId = getPara("typeId");
			SQuery sql = SQuery.create("select d.id as id,d.name as name ,d.d_code as code,d.value as value,d.remark as remark ");
			sql.append(" from t_data_dictionary d ");
			sql.append(" where 1=1 and d.is_delete = '1' ");
			if(LvoutcityUtils.isNotNull(typeId)){
				sql.append(" and d.type_id ='"+typeId+"'");
			}
			sql.append(" order by d.value ");
			renderDatatable(LvoutcityUtils.getPage(Db.paginate((getDatatableStart() / getDatatableLength()) + 1, getDatatableLength(),
					sql.isSql() ,sql.isFrom() )));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	/**
	 * 
	 * @Title: saveOrUpdate 
	 * @Description: 添加或修改
	 * @param     设定文件 
	 * @return void    返回类型 
	 * @create_time 2016下午1:21:16
	 * @create_user James w.s
	 * @throws
	 */
	@Before(POST.class)
	public void saveOrUpdate(){
		try {
			DataDictionaryType data = getBean(DataDictionaryType.class);
			if(validDictCode(data)){
				renderJson("error","编号重复!");
				return;
			}
			if(LvoutcityUtils.isNotNull(data) && LvoutcityUtils.isNotNull(data.getId())){
				DataDictionaryType newData = DataDictionaryType.dao.findById(data.getId());
				if(LvoutcityUtils.isNotNull(newData)){
					if(LvoutcityUtils.isNotNull(data.getName())){
						newData.set("name", data.getName());
					}
					if(LvoutcityUtils.isNotNull(data.getValue())){
						newData.set("value", data.getValue());
					}
					if(LvoutcityUtils.isNotNull(data.getTCode())){
						newData.set("t_code", data.getTCode());
					}
					newData.update();
				}
				renderJson("success","修改成功");
			}else{
				data.setCreateTime(new Date());
				data.setId(LvoutcityUtils.getUUID());
				data.setIsDelete(Constants.ISDELETE_FALSE);
				data.save();
				renderJson("success","添加成功");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			renderJson("error","数据异常请联系管理员!");
		}
	}
	
	/**
	 * 
	 * @Title: validDictCode 
	 * @Description: 类型编号是否重复
	 * @param @param data
	 * @param @return    设定文件 
	 * @return boolean    返回类型 
	 * @create_time 2016下午3:02:45
	 * @create_user James w.s
	 * @throws
	 */
	private boolean validDictCode(DataDictionaryType data) {
		if(LvoutcityUtils.isNotNull(data) && LvoutcityUtils.isNotNull(data.getTCode())){
			List<DataDictionaryType>  list =  DataDictionaryType.dao.find("select * from t_data_dictionary_type d where d.t_code ='"+data.getTCode()+"'");
			return !list.isEmpty() ? true : false;
		}
		return false;
	}

	/**
	 * 
	 * @Title: saveOrUpdateData 
	 * @Description: 字典数据添加
	 * @param     设定文件 
	 * @return void    返回类型 
	 * @create_time 2016下午2:53:09
	 * @create_user James w.s
	 * @throws
	 */
	@Before(POST.class)
	public void saveOrUpdateData() {
		DataDictionary bean = getBean(DataDictionary.class);
		try {
			if (LvoutcityUtils.isNotNull(bean) && LvoutcityUtils.isNotNull(bean.getId())) {
				DataDictionary newData = DataDictionary.dao.findById(bean.getId());
				if (LvoutcityUtils.isNotNull(newData)) {
					if (LvoutcityUtils.isNotNull(bean.getName())) {
						newData.set("name", bean.getName());
					}
					if (LvoutcityUtils.isNotNull(bean.getValue())) {
						newData.set("value", bean.getValue());
					}
					newData.update();
				}
				renderJson("success", "修改成功");
			} else {
				bean.setCreateTime(new Date());
				bean.setId(LvoutcityUtils.getUUID());
				bean.setIsDelete(Constants.ISDELETE_FALSE);
				bean.save();
				renderJson("success", "添加成功");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	/**
	 * 
	 * @Title: add 
	 * @Description: 添加页面
	 * @param     设定文件 
	 * @return void    返回类型 
	 * @create_time 2016下午1:23:44
	 * @create_user James w.s
	 * @throws
	 */
	public void addIndex(){
		
		DataDictionaryType da = new DataDictionaryType();
		String superId = getPara("superId");
		if(LvoutcityUtils.isNotNull(superId)){
			da.setSuperId("0");
		}else{
			da.setSuperId("0");
		}
		setAttr("bean", da);
		render("addIndex.html");
	}
	
	
	/**
	 * 
	 * @Title: editIndex 
	 * @Description: 
	 * @param     设定文件 
	 * @return void    返回类型 
	 * @create_time 2016下午1:24:14
	 * @create_user James w.s
	 * @throws
	 */
	public void editIndex(){
		String id = getPara("id");
		String view = getPara("view");
		if("1".equals(view)){
			setAttr("type",1);
		}else{
			setAttr("type",0);
		}
		DataDictionaryType bean = DataDictionaryType.dao.findById(id);
		setAttr("bean", bean);
		render("addIndex.html");
	}
	
	/**
	 * 
	 * @Title: addValue 
	 * @Description: 添加页面
	 * @param     设定文件 
	 * @return void    返回类型 
	 * @create_time 2016下午1:26:24
	 * @create_user James w.s
	 * @throws
	 */
	public void addValue(){
		String superId = getPara("superId");
		DataDictionary da = new DataDictionary();
		da.setTypeId(superId);
		setAttr("bean", da);
		render("addValue.html");
	}
	
	/**
	 * 
	 * @Title: editValue 
	 * @Description: 页面修改 
	 * @param     设定文件 
	 * @return void    返回类型 
	 * @create_time 2016下午6:13:26
	 * @create_user James w.s
	 * @throws
	 */
	public void editValue(){
		String id = getPara("id");
		DataDictionary da = DataDictionary.dao.findById(id);
		setAttr("bean", da);
		if("1".equals(getPara("view"))) {
			setAttr("type",1);
		} 
		else{
			setAttr("type",0);
		}
		render("addValue.html");
	}
	
	/**
	 * 
	 * @Title: deleteIndex 
	 * @Description:删除类型
	 * @param     设定文件 
	 * @return void    返回类型 
	 * @create_time 2016下午2:51:54
	 * @create_user James w.s
	 * @throws
	 */
	public void deleteIndex(){
		try {
			String id = getPara("id");
			DataDictionaryType bean = DataDictionaryType.dao.findById(id);
			if(LvoutcityUtils.isNotNull(bean)){
				bean.deleteById(bean.getId());
			}
			renderJson("success", "删除成功");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			renderJson("error", "删除失败");
		}
	}
	
	/**
	 * 
	 * @Title: deletValue 
	 * @Description: 删除字典数据
	 * @param     设定文件 
	 * @return void    返回类型 
	 * @create_time 2016下午2:52:25
	 * @create_user James w.s
	 * @throws
	 */
	public void deletValue(){
		try {
			String id = "";
//			Map<String,String[]> idMap = getParaMap();
			List<HashMap<String, String>> idList = getListMap("id");
			for (int i = 0; i < idList.size(); i++) {
				id = idList.get(i).get("id");
				DataDictionary bean = DataDictionary.dao.findById(id);
				if(LvoutcityUtils.isNotNull(bean)){
					bean.deleteById(bean.getId());
				}
			}
			renderJson("success", "删除成功");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			renderJson("error", "删除失败");
		}
	}
	
}
