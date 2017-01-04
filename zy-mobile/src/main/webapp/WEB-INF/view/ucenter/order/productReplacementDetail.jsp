<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta http-equiv="Cache-Control" content="no-store" />
	<meta http-equiv="Pragma" content="no-cache" />
	<meta http-equiv="Expires" content="0" />
	<meta name="apple-mobile-web-app-capable" content="yes">
	<meta name="apple-mobile-web-app-status-bar-style" content="black">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">

	<title>产品更换</title>
	<%@ include file="/WEB-INF/view/include/head.jsp"%>
	<%@ include file="/WEB-INF/view/include/validate.jsp"%>
	<script src="${stccdn}/js/area.js"></script>
	<link rel="stylesheet" href="${stccdn}/css/ucenter/order.css" />
	<script>
		$(function() {

			$('#btnConfirm').click(function() {
				$.dialog({
					content : '您确定已收到商品吗？',
					callback : function(index) {
						if (index == 1) {
							location.href = '${ctx}/u/productReplacement/confirmDelivery?id=${productReplacement.id}';
						}
					}
				});
			});
		});
	</script>
</head>
<body>
<body class="header-fixed footer-fixed">
	<header class="header">
		<h1>更换产品</h1>
		<a href="${ctx}/u/productReplacement" class="button-left"><i class="fa fa-angle-left"></i></a>
	</header>

	<article class="order-list">
		<div class="form-message note note-warning">
			<p>状态 : ${productReplacement.productReplacementStatus}</p>
		</div>

		<div class="list-group">
			<div class="list-title">收货人信息</div>

			<div class="list-item">
				<div class="list-icon"><i class="fa fa-map-marker font-orange fs-24"></i></div>
				<div class="list-text fs-14 font-333 pl-5">
					<div>${order.receiverRealname}<span class="right">${productReplacement.receiverPhone}</span></div>
					<div class="fs-14 font-777">${productReplacement.receiverProvince} ${productReplacement.receiverCity} ${productReplacement.receiverDistrict} ${productReplacement.receiverAddress}</div>
				</div>
			</div>
		</div>

		<c:if test="${productReplacement.productReplacementStatus == '已发货' or productReplacement.productReplacementStatus == '已完成'}">
		<div class="list-group">
			<div class="list-title">发货信息</div>
				<div class="list-item lh-20">
					<div class="list-text fs-14">物流公司</div>
					<div class="list-unit">${productReplacement.logisticsName}</div>
				</div>
				<div class="list-item lh-20">
					<div class="list-text fs-14">物流单号</div>
					<div class="list-unit fs-12">${productReplacement.logisticsSn}</div>
				</div>
				<div class="list-item lh-20">
					<div class="list-text fs-14">物流费用</div>
					<div class="list-unit">${productReplacement.logisticsFee}</div>
				</div>
		</div>
		</c:if>

		<div class="list-group">
		<div class="list-title">更换产品</div>
		<!-- form-radio -->
		<div class="list-item form-radio">
			<label class="list-text fs-14" for="deliverType1">${productReplacement.fromProduct} 更换 ${productReplacement.toProduct}</label>
			<div class="list-unit">
				<input id="deliverType1" type="radio" name="deliverType" value="1" checked="checked">
				<label class="i-checked" for="deliverType1"></label>
			</div>
		</div>
			<div class="list-item lh-20">
				<div class="list-text fs-14">数量 </div>
				<div class="list-unit">${productReplacement.quantity}</div>
			</div>
		</div>

		<c:if test="${productReplacement.productReplacementStatus == '已发货'}">
		<div class="form-btn">
			<a id="btnConfirm" class="btn green btn-block round-2"><i class="fa fa-check"></i> 确认收货</a>
		</div>
		</c:if>

	<%@ include file="/WEB-INF/view/include/footer.jsp"%>
</body>
</html>
