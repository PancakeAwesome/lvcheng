package com.lvoutcity.model;

import java.util.List;

import com.lvoutcity.model.base.BaseAttractions;

/**
 * Generated by JFinal.
 */
@SuppressWarnings("serial")
public class Attractions extends BaseAttractions<Attractions> {
	public static final Attractions dao = new Attractions();
	
	public Attractions findByScenicNo(int scenicNo){
		return Attractions.dao.findFirst("select * from  t_attractions where order_no= "+scenicNo+" and enabled ='1' and is_delete='1'");
	}

	public Attractions queryLeaderPoint(String guideId){
		return Attractions.dao.findById("select attractions.longitude,attractions.latitude "
				+ " from t_attractions as attractions left join t_map_raiders as raiders on "
				+ " raiders.attr_id=attractions.id where raiders.type='3' and   raiders.guide_id='"+guideId+"'");
	}
    public List<Attractions> queryAttrasByGuideId(String guideId,String clubId){
    	return Attractions.dao.find("select attractions.description,attractions.longitude,"
    			+ "attractions.latitude,attractions.name,"
    			+" case  when attractions.order_no>=0 then concat('/attra/spotDetail?spotId=', attractions.id)  end  as attraUrl" 
    			+ " from t_attractions as attractions left join t_map_poly_line_data as line"
    			+ " on line.city=attractions.city "
    			+ " where line.guide_id='"+guideId+"' and attractions.club_id= '"+clubId+"'");
    }
    
	
	private String btnAttr;

	public String getBtnAttr() {
		return btnAttr;
	}
    public void setAttraUrl(java.lang.String attraUrl){
    	set("attraUrl",attraUrl);
    }
    public java.lang.String getAttraUrl(){
    	return get("attraUrl");
    }
	public void setBtnAttr(String btnAttr) {
		this.btnAttr = btnAttr;
	}

}
