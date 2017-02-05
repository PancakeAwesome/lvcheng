/**
 * order status
 */

var ORDER_STATUS = {
	STATUS_UNPAID : "0", // 待支付
	STATUS_PAID : "1", // 支付完成
	STATUS_ON_TRIP : "2", // 行程中
	STATUS_TRIP_FINISH : "3", // 行程结束
	STATUS_REFUND_APPLY : "4", // 退款申请
	STATUS_CANCLED_USER : "5", // 订单取消（用户）
	STATUS_CANCLED_BACK : "6", // 订单取消（后台）
	renderer : function(status) {
		switch (status) {
		case this.STATUS_UNPAID:
			return "待支付";
		case this.STATUS_PAID:
			return "已支付";
		case this.STATUS_ON_TRIP:
			return "行程中";
		case this.STATUS_TRIP_FINISH:
			return "行程结束";
		case this.STATUS_REFUND_APPLY:
			return "退款待审核";
		case this.STATUS_CANCLED_USER:
			return "订单取消（用户）";
		case this.STATUS_CANCLED_BACK:
			return "订单取消（后台）";

		}
	},
	colorfulRenderer : function(status) {
		switch (status) {
		case this.STATUS_UNPAID:
			return "<span style='color:orange;'>待支付</span>";
		case this.STATUS_PAID:
			return "<span style='color:green;'>已支付</span>";
		case this.STATUS_ON_TRIP:
			return "<span style='color:green;'>行程中</span>";
		case this.STATUS_TRIP_FINISH:
			return "<span style='color:green;'>行程结束</span>";
		case this.STATUS_REFUND_APPLY:
			return "<span style='color:red;'><b>退款待审核</b></span>";
		case this.STATUS_CANCLED_USER:
			return "订单取消（用户）";
		case this.STATUS_CANCLED_BACK:
			return "订单取消（后台）";

		}
	}
}
