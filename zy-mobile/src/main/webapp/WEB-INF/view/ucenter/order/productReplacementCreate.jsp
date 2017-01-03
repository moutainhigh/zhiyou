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
			var area = new areaInit('receiverProvince', 'receiverCity', 'receiverDistrict');

			$('#deliverType0').click(function(){
				$('#logistics').slideUp(300);
			});
			$('#deliverType1').click(function(){
				$('#logistics').slideDown(300);
			});

			$('.valid-form').validate({
				rules : {
					'receiverRealname' : {
						required : true
					},
					'receiverPhone' : {
						required : true,
						phone : true
					},
					'receiverProvince' : {
						required : true
					},
					'receiverCity' : {
						required : true
					},
					'receiverDistrict' : {
						required : true
					},
					'receiverAddress' : {
						required : true
					},
					'receiverAreaId' : {
						required : true
					},
					'quantity' : {
						required : true
					}
				},
				messages : {}
			});
		});
	</script>
</head>
<body>
<body class="header-fixed footer-fixed">
	<header class="header">
		<h1>更换产品</h1>
		<a href="${ctx}/u" class="button-left"><i class="fa fa-angle-left"></i></a>
	</header>

	<article class="order-list">
		<div class="form-message note note-warning hide">
			<p>输入信息有误，请先更正。</p>
		</div>

		<div class="list-group mt-15">
			<div class="list-title">公司收货地址：</div>
			<div class="list-item">
				<div class="list-text fs-14">上海市普陀区云岭东路609弄1号楼十楼</div>
			</div>
		</div>
		<form id="form" class="valid-form" action="${ctx}/u/productReplacement/create" method="post">
			<div class="list-group">
				<div class="list-title">更换产品</div>
				<!-- form-radio -->
				<div class="list-item form-radio">
					<label class="list-text fs-14" for="deliverType1">优检一生1.0更换2.0</label>
					<div class="list-unit">
						<input id="deliverType1" type="radio" name="deliverType" value="1" checked="checked">
						<label class="i-checked" for="deliverType1"></label>
					</div>
				</div>
			</div>

			<div id="logistics" class="list-group">
				<div class="list-title">请输入更换的产品数量</div>
				<div class="list-item">
					<label class="list-label" for="quantity">产品数量：</label>
					<div class="list-text">
						<input type="text" name="quantity" id="quantity" class="form-input" value="">
					</div>
				</div>
			</div>

			<div class="list-group">
				<div class="list-item">
					<label class="list-label" for="realname">收件人</label>
					<div class="list-text">
						<input type="text" id="realname" name="receiverRealname" class="form-input" value="" placeholder="填写收件人姓名">
					</div>
				</div>
				<div class="list-item">
					<label class="list-label" for="phone">手机号码</label>
					<div class="list-text">
						<input type="tel" id="phone" name="receiverPhone" class="form-input" value="" placeholder="填写收件人手机号码">
					</div>
				</div>
				<div class="list-item">
					<label class="list-label">省份</label>
					<div class="list-text form-select">
						<select name="receiverProvince" id="receiverProvince">
							<option value="">请选择</option>
						</select>
					</div>
				</div>
				<div class="list-item">
					<label class="list-label">城市</label>
					<div class="list-text form-select">
						<select name="receiverCity" id="receiverCity">
							<option value="">请选择</option>
						</select>
					</div>
				</div>
				<div class="list-item">
					<label class="list-label">地区</label>
					<div class="list-text form-select">
						<select name="receiverAreaId" id="receiverDistrict">
							<option value="">请选择</option>
						</select>
					</div>
				</div>
				<div class="list-item">
					<div class="list-text">
						<textarea name="receiverAddress" class="form-input" rows="3" placeholder="填写详细地址，例如街道名称，楼层和门牌号等信息"></textarea>
					</div>
				</div>
			</div>

			<div class="form-btn">
				<input id="btnSubmit" class="btn orange btn-block round-2" type="submit" value="保 存">
			</div>
		</form>
	</article>

	<%@ include file="/WEB-INF/view/include/footer.jsp"%>
</body>
</html>
