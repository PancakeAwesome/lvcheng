/**
 * 
 */
package com.lvoutcity.core.util;

/**
 * @author wj
 *
 */
public class JsonData {
	//0 成功  	1 失败
	 	private String code = "0";

	    private String msg = "";

	    private Object data;

	    public JsonData(){
	    	
	    }
		public JsonData(String code, String msg, Object data) {
			this.code = code;
			this.msg = msg;
			this.data = data;
			// TODO Auto-generated constructor stub
		}
		public JsonData(String code, String msg) {
			this.code = code;
			this.msg = msg;
			// TODO Auto-generated constructor stub
		}
		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}

		public String getMsg() {
			return msg;
		}

		public void setMsg(String msg) {
			this.msg = msg;
		}

		public Object getData() {
			return data;
		}

		public void setData(Object data) {
			this.data = data;
		}
	    
	    
}
