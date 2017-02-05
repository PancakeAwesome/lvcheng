package com.lvoutcity.core.api;

/**
 * Api常量
 * 
 * @author gdh
 *
 */
public interface Constant {

	String TOKEN_KEY = "_TOKEN";
	String SIGN_KEY = "_SIGN";
	String BODY_KEY = "_BODY";
	String TokenBean = "_TokenBean";
	
	
	Integer BIZ_SUCCESS = 100000;
	Integer BIZ_FAIL = 100001;
	Integer TOKEN_FAIL = 900000;
	Integer SIGN_FAIL = 900001;
	Integer UNSUPPORT = 900002;
	
	
	
	String DATA_EL = "^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))";
	
	
}
