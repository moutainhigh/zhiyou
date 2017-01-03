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

	<title>我的订单</title>
	<%@ include file="/WEB-INF/view/include/head.jsp"%>
	<%@ include file="/WEB-INF/view/include/validate.jsp"%>
	<script src="${stccdn}/js/area.js"></script>
	<link rel="stylesheet" href="${stccdn}/css/ucenter/order.css" />
	<script>
		$(function() {
			var area = new areaInit('province', 'city', 'district');

			$('#deliverType0').click(function(){
				$('#logistics').slideUp(300);
			});
			$('#deliverType1').click(function(){
				$('#logistics').slideDown(300);
			});

			$('.valid-form').validate({
				rules : {
					'realname' : {
						required : true
					},
					'phone' : {
						required : true,
						phone : true
					},
					'province' : {
						required : true
					},
					'city' : {
						required : true
					},
					'areaId' : {
						required : true
					},
					'address' : {
						required : true
					}
				},
				messages : {
					'realname' : {
						required : '请输入收货人姓名'
					},
					'phone' : {
						required : '请输入收货人手机号',
						phone : '请输入正确的手机号'
					},
					'province' : {
						required : '请选择省'
					},
					'city' : {
						required : '请选择市'
					},
					'areaId' : {
						required : '请选择区'
					},
					'address' : {
						required : '请输入详细地址'
					}
				}
			});
		});
	</script>
</head>
<body>
<body class="header-fixed footer-fixed">
	<header class="header">
		<h1>订单</h1>
		<a href="${ctx}/u" class="button-left"><i class="fa fa-angle-left"></i></a>
	</header>

	<article class="order-list">
		<div class="form-message note note-warning hide">
			<p>输入信息有误，请先更正。</p>
		</div>

		<div class="list-group mt-15">
			<div class="list-title">收货地址：</div>
			<div class="list-item">
				<div class="list-text fs-14">上海市普陀区云岭东路609弄1号楼十楼</div>
			</div>
		</div>
		<form id="addressForm" class="valid-form" action="" method="post">
			<div class="list-group">
				<div class="list-title">单选</div>
				<!-- form-radio -->
				<div class="list-item form-radio">
					<label class="list-text fs-14" for="deliverType1">2.0 、产品以及输入对应的产品数量</label>
					<div class="list-unit">
						<input id="deliverType1" type="radio" name="deliverType" value="1" checked="checked">
						<label class="i-checked" for="deliverType1"></label>
					</div>
				</div>
			</div>

			<div id="logistics" class="list-group">
				<div class="list-title">请输入对应的产品数量</div>
				<div class="list-item">
					<label class="list-label" for="logisticsNo">产品数量：</label>
					<div class="list-text">
						<input type="text" name="logisticsNo" class="form-input" value="">
					</div>
				</div>
			</div>

			<div class="list-group">
				<div class="list-item">
					<label class="list-label" for="realname">收件人</label>
					<div class="list-text">
						<input type="text" id="realname" name="realname" class="form-input" value="" placeholder="填写收件人姓名">
					</div>
				</div>
				<div class="list-item">
					<label class="list-label" for="phone">手机号码</label>
					<div class="list-text">
						<input type="tel" id="phone" name="phone" class="form-input" value="" placeholder="填写收件人手机号码">
					</div>
				</div>
				<div class="list-item">
					<label class="list-label">省份</label>
					<div class="list-text form-select">
						<select name="province" id="province">
							<option value="">请选择</option>
						</select>
					</div>
				</div>
				<div class="list-item">
					<label class="list-label">城市</label>
					<div class="list-text form-select">
						<select name="city" id="city">
							<option value="">请选择</option>
						</select>
					</div>
				</div>
				<div class="list-item">
					<label class="list-label">地区</label>
					<div class="list-text form-select">
						<select name="areaId" id="district">
							<option value="">请选择</option>
						</select>
					</div>
				</div>
				<div class="list-item">
					<div class="list-text">
						<textarea name="address" class="form-input" rows="3" placeholder="填写详细地址，例如街道名称，楼层和门牌号等信息"></textarea>
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
