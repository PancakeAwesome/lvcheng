
var WALLET_STATUS = {
	STATUS_PRE_IN : "1", // 预收入
	STATUS_IN : "2", // 已收入
	STATUS_OUT : "3", //已支出
	renderer : function(status) {
		switch (status) {
		case this.STATUS_PRE_IN:
			return "预收入";
		case this.STATUS_IN:
			return "已收入";
		case this.STATUS_OUT:
			return "已支出";

		}
	}
}
