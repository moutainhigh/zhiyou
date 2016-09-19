<%@ page language="java" pageEncoding="UTF-8"%>
<script type="text/javascript" src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
<script>
	WeixinApi = {
		getConfig: function (apiList) {
			var config = {
				debug: true,
				appId: '${weixinJsModel.appId}',
				timestamp: ${weixinJsModel.timestamp},
				nonceStr: '${weixinJsModel.nonceStr}',
				signature: '${weixinJsModel.signature}',
				jsApiList: apiList
			};
			return config;
		}
	}
</script>