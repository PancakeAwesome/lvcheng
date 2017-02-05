/**
 * 
 */
package com.lvoutcity.admin.controller.bean;


/**
 * @author wj
 *
 */
public class UserBean   {
	
	private String id;
	private String userName;// 用户名
	private Integer userNo;// 旅城号
	private String isDelete;// 启用状态
	private String phoneNumber;
	private String nickName; //昵称
	private String realName;// 真实姓名
	private String gender;// 性别
	private String cityId;// 市
	private String provinceId;// 省
	private String referralPhoneNumber; // 推荐人手机号
	private String idcard; //身份证
	private String personalSign;// 个性签名
	private String clubId;

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Integer getUserNo() {
		return userNo;
	}
	public void setUserNo(Integer userNo) {
		this.userNo = userNo;
	}
	public String getIsDelete() {
		return isDelete;
	}
	public void setIsDelete(String isDelete) {
		this.isDelete = isDelete;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getCityId() {
		return cityId;
	}
	public void setCityId(String cityId) {
		this.cityId = cityId;
	}
	public String getProvinceId() {
		return provinceId;
	}
	public void setProvinceId(String provinceId) {
		this.provinceId = provinceId;
	}
	public String getReferralPhoneNumber() {
		return referralPhoneNumber;
	}
	public void setReferralPhoneNumber(String referralPhoneNumber) {
		this.referralPhoneNumber = referralPhoneNumber;
	}
	public String getIdcard() {
		return idcard;
	}
	public void setIdcard(String idcard) {
		this.idcard = idcard;
	}
	public String getPersonalSign() {
		return personalSign;
	}
	public void setPersonalSign(String personalSign) {
		this.personalSign = personalSign;
	}
	public String getClubId() {
		return clubId;
	}
	public void setClubId(String clubId) {
		this.clubId = clubId;
	}
	
	
	
}
