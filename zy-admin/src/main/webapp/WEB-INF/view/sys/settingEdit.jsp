<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/view/include/head.jsp"%>
<%@ include file="/WEB-INF/view/include/form.jsp"%>

<script>

$(function(){
	<c:if test="${show}">
	$('#withdrawFeeRateScriptCheckBtn').click(function(){
		$.post('${ctx}/setting/checkWithdrawFeeRateScript', 
			{withdrawFeeRateScript: $('#withdrawFeeRateScript').val()}, 
			function(result) {
				if(result.code == 0) {
					$('#withdrawFeeRateScriptResult').html(result.data);
				} else {
					$('#withdrawFeeRateScriptResult').text('校验失败:' + result.message);
				}
			}
		);
	});
	
	$('#bonusRateScriptCheckBtn').click(function(){
		$.post('${ctx}/setting/checkBonusRateScript', 
			{bonusRateScript: $('#bonusRateScript').val()}, 
			function(result) {
				if(result.code == 0) {
					$('#bonusRateScriptResult').html(result.data);
				} else {
					$('#bonusRateScriptResult').text('校验失败:' + result.message);
				}
			}
		);
	});
	
	$('#basicCoinScriptCheckBtn').click(function(){
		$.post('${ctx}/setting/checkBasicCoinScript', 
			{basicCoinScript: $('#basicCoinScript').val()}, 
			function(result) {
				if(result.code == 0) {
					$('#basicCoinScriptResult').html(result.data);
				} else {
					$('#basicCoinScriptResult').text('校验失败:' + result.message);
				}
			}
		);
	});
	
	$('#achievementAwardScriptCheckBtn').click(function(){
		$.post('${ctx}/setting/checkAchievementAwardScript', 
			{achievementAwardScript: $('#achievementAwardScript').val()}, 
			function(result) {
				if(result.code == 0) {
					$('#achievementAwardScriptResult').html(result.data);
				} else {
					$('#achievementAwardScriptResult').text('校验失败:' + result.message);
				}
			}
		);
	});
	
	$('#signInPointScriptCheckBtn').click(function(){
		$.post('${ctx}/setting/checkSignInPointScript', 
			{signInPointScript: $('#signInPointScript').val()}, 
			function(result) {
				if(result.code == 0) {
					$('#signInPointScriptResult').html(result.data);
				} else {
					$('#signInPointScriptResult').text('校验失败:' + result.message);
				}
			}
		);
	});
	</c:if>
});

</script>

<div class="page-bar">
	<ul class="page-breadcrumb">
		<li><i class="fa fa-home"></i> <a href="javascript:;"
			data-href="${ctx}/main">首页</a> <i class="fa fa-angle-right"></i></li>
		<li><a href="javascript:;" data-href="${ctx}/setting/edit">系统设置</a></li>
	</ul>
</div>

<div class="row">
	<div class="col-md-12">
		<div class="portlet box blue">
			<div class="portlet-title">
				<div class="caption">
					<i class="fa"></i> 系统设置
				</div>
			</div>
			<div class="portlet-body clearfix">
				<div class="tabbable tabbable-custom">
					<ul class="nav nav-tabs">
						<li class="active"><a data-toggle="tab" href="#tab__base"> 基本信息</a></li>
						<li><a data-toggle="tab" href="#tabBonusRateScript"> 团队奖励比例</a></li>
						<li><a data-toggle="tab" href="#tabWithdrawFeeRateScript"> 提现费率</a></li>
						<li><a data-toggle="tab" href="#tabBasicCoinScript"> 基础服务费</a></li>
						<li><a data-toggle="tab" href="#tabAchievementAwardScript"> 获得成就奖励</a></li>
						<li><a data-toggle="tab" href="#tabSignInPointScript"> 签到积分</a></li>
					</ul>
					<div class="tab-content">
						<div id="tab__base" class="tab-pane active" >
							
							<form id="dataForm" action="" data-action="${ctx}/setting/edit/base" class="form-horizontal" method="post">
								<div class="form-body">
									<div class="row">
										<div class="col-md-6">
											<div class="form-group">
												<label class="control-label col-md-3">试客违约保证金:</label>
												<div class="col-md-9">
													<p class="form-control-static">
													<input type="text" class="form-control" id="buyerDefaultCoin" name="buyerDefaultCoin" value="${setting.buyerDefaultCoin}" />
													</p>
												</div>
											</div>
										</div>
										<!--/span-->
										<div class="col-md-6">
											<div class="form-group">
												<label class="control-label col-md-3">团队奖励深度:</label>
												<div class="col-md-9">
													<p class="form-control-static">
													<input type="text" class="form-control" id="bonusDeepth" name="bonusDeepth" value="${setting.bonusDeepth}" />
													</p>
												</div>
											</div>
										</div>
										<!--/span-->
									</div>
									<div class="row">
										<div class="col-md-6">
											<div class="form-group">
												<label class="control-label col-md-3">差价保证金:</label>
												<div class="col-md-9">
													<p class="form-control-static">
													<input type="text" class="form-control" id="adjustMoney" name="adjustMoney" value="${setting.adjustMoney}" />
													</p>
												</div>
											</div>
										</div>
										<!--/span-->
										<div class="col-md-6">
											<div class="form-group">
												<label class="control-label col-md-3">B商品费(金币):</label>
												<div class="col-md-9">
													<p class="form-control-static">
													<input type="text" class="form-control" id="giftCoin" name="giftCoin" value="${setting.giftCoin}" />
													</p>
												</div>
											</div>
										</div>
										<!--/span-->
									</div>
									<div class="row">
										<div class="col-md-6">
											<div class="form-group">
												<label class="control-label col-md-3">平台发B商品打包费(金币):</label>
												<div class="col-md-9">
													<p class="form-control-static">
													<input type="text" class="form-control" id="giftPackingCoin" name="giftPackingCoin" value="${setting.giftPackingCoin}" />
													</p>
												</div>
											</div>
										</div>
										<!--/span-->
										<div class="col-md-6">
											<div class="form-group">
												<label class="control-label col-md-3">B商品物流费用(金币):</label>
												<div class="col-md-9">
													<p class="form-control-static">
													<input type="text" class="form-control" id="giftLogisticsCoin" name="giftLogisticsCoin" value="${setting.giftLogisticsCoin}" />
													</p>
												</div>
											</div>
										</div>
										<!--/span-->
									</div>
									
									<div class="row">
										
										<div class="col-md-6">
											<div class="form-group">
												<label class="control-label col-md-3">商家违约保证金:</label>
												<div class="col-md-9">
													<p class="form-control-static">
													<input type="text" class="form-control" id="merchantDefaultCoin" name="merchantDefaultCoin" value="${setting.merchantDefaultCoin}" />
													</p>
												</div>
											</div>
										</div>
									</div>
									
									
									<div class="row">
										
										<div class="col-md-6">
											<div class="form-group">
												<label class="control-label col-md-3">试客付款超时期限(小时):</label>
												<div class="col-md-9">
													<p class="form-control-static">
													<input type="text" class="form-control" id="buyerPayExpireInHours" name="buyerPayExpireInHours" value="${setting.buyerPayExpireInHours}" />
													</p>
												</div>
											</div>
										</div>
										
										<div class="col-md-6">
											<div class="form-group">
												<label class="control-label col-md-3">试客注册就送时光币:</label>
												<div class="col-md-9">
													<p class="form-control-static">
													<input type="text" class="form-control" id="buyerRegisterGrantCoin" name="buyerRegisterGrantCoin" value="${setting.buyerRegisterGrantCoin}" />
													</p>
												</div>
											</div>
										</div>
										
									</div>
									
									<div class="row">
										<div class="col-md-6">
											<div class="form-group">
												<label class="control-label col-md-3">商家发货超时期限(小时):</label>
												<div class="col-md-9">
													<p class="form-control-static">
													<input type="text" class="form-control" id="merchantDeliverExpireInHours" name="merchantDeliverExpireInHours" value="${setting.merchantDeliverExpireInHours}" />
													</p>
												</div>
											</div>
										</div>
										<div class="col-md-6">
											<div class="form-group">
												<label class="control-label col-md-3">商家确认完成超时期限(小时):</label>
												<div class="col-md-9">
													<p class="form-control-static">
													<input type="text" class="form-control" id="merchantConfirmExpireInHours" name="merchantConfirmExpireInHours" value="${setting.merchantConfirmExpireInHours}" />
													</p>
												</div>
											</div>
										</div>
									</div>
									
									<div class="row">
										<div class="col-md-6">
											<div class="form-group">
												<label class="control-label col-md-3">发货后多少时间能够评价(小时):</label>
												<div class="col-md-9">
													<p class="form-control-static">
													<input type="text" class="form-control" id="canCommentAfterDeliveredInHours" name="canCommentAfterDeliveredInHours" value="${setting.canCommentAfterDeliveredInHours}" />
													</p>
												</div>
											</div>
										</div>
										<div class="col-md-6">
											<div class="form-group">
												<label class="control-label col-md-3">是否开发环境:</label>
												<div class="col-md-9">
													<p class="form-control-static">
													<select name="isDev" class="form-control" style="width : 205px;">
														<option value="1"<c:if test="${setting.isDev == '1'}"> selected="selected"</c:if>>是</option>
														<option value="0"<c:if test="${setting.isDev == '0'}"> selected="selected"</c:if>>否</option>
													</select>
													</p>
												</div>
											</div>
										</div>
									</div>
									
									<div class="row">
										<div class="col-md-6">
											<div class="form-group">
												<label class="control-label col-md-3">最大自动审核数:</label>
												<div class="col-md-9">
													<p class="form-control-static">
													<input type="text" class="form-control" id="maxAutoConfirmCount" name="maxAutoConfirmCount" value="${setting.maxAutoConfirmCount}" />
													</p>
												</div>
											</div>
										</div>
									</div>
									
									<div class="row">
										<div class="col-md-6"><div class="form-group"></div></div>
										<div class="col-md-6">
											<div class="form-group">
												<button  type="submit" class="btn green">
													<i class="fa fa-plus"></i> 保存
												</button>
											</div>
										</div>
									</div>
									
								</div>
							</form>
						</div>
						<div id="tabBonusRateScript" class="tab-pane ">
							<form id="dataForm" action="" data-action="${ctx}/setting/edit/bonusRateScript" class="form-horizontal" method="post">
								<div class="form-group">
									<label class="control-label col-md-3">团队奖励比例:</label>
									<div class="col-md-9">
										<p class="form-control-static">
										<textarea rows="" cols="" name="bonusRateScript" id="bonusRateScript" style="width: 400px; height: 320px;">${setting.bonusRateScript}</textarea>
										</p>
									</div>
								</div>
							
								<div class="form-group">
									<label class="control-label col-md-3">校验结果:</label>
									<div class="col-md-9">
										<p id="bonusRateScriptResult" class="form-control-static">
										</p>
									</div>
								</div>
								<c:if test="${show}">
								<div class="form-group">
									<label class="control-label col-md-3"></label>
									<div class="col-md-9">
										<button id="bonusRateScriptCheckBtn" type="button" class="btn yellow">
											<i class="fa fa-plus"></i> 校验
										</button>
										<button type="submit" class="btn green">
											<i class="fa fa-plus"></i> 保存
										</button>
									</div>
								</div>
								</c:if>
							</form>
						</div>
						<div id="tabWithdrawFeeRateScript" class="tab-pane ">
							<form id="withdrawFeeRateScriptForm" action="" data-action="${ctx}/setting/edit/withdrawFeeRateScript" class="form-horizontal" method="post">
								<div class="form-group">
									<label class="control-label col-md-3">提现费率:</label>
									<div class="col-md-9">
										<p class="form-control-static">
										<textarea rows="" cols="" id="withdrawFeeRateScript" name="withdrawFeeRateScript" style="width: 400px; height: 320px;">${setting.withdrawFeeRateScript}</textarea>
										</p>
									</div>
								</div>
								<div class="form-group">
									<label class="control-label col-md-3">校验结果:</label>
									<div class="col-md-9">
										<p id="withdrawFeeRateScriptResult" class="form-control-static">
										</p>
									</div>
								</div>
								<c:if test="${show}">
								<div class="form-group">
									<label class="control-label col-md-3"></label>
									<div class="col-md-9">
										<button id="withdrawFeeRateScriptCheckBtn" type="button" class="btn yellow">
											<i class="fa fa-plus"></i> 校验
										</button>
										<button type="submit" class="btn green">
											<i class="fa fa-plus"></i> 保存
										</button>
									</div>
								</div>
								</c:if>
							</form>
						</div>
						<div id="tabBasicCoinScript" class="tab-pane ">
							<form id="dataForm" action="" data-action="${ctx}/setting/edit/basicCoinScript" class="form-horizontal" method="post">
								<div class="form-group">
									<label class="control-label col-md-3">基础服务费:</label>
									<div class="col-md-9">
										<p class="form-control-static">
										<textarea rows="" cols="" name="basicCoinScript" id="basicCoinScript" style="width: 600px; height: 400px;">${setting.basicCoinScript}</textarea>
										</p>
									</div>
								</div>
							
								<div class="form-group">
									<label class="control-label col-md-3">校验结果:</label>
									<div class="col-md-9">
										<p id="basicCoinScriptResult" class="form-control-static">
										</p>
									</div>
								</div>
								<c:if test="${show}">
								<div class="form-group">
									<label class="control-label col-md-3"></label>
									<div class="col-md-9">
										<button id="basicCoinScriptCheckBtn" type="button" class="btn yellow">
											<i class="fa fa-plus"></i> 校验
										</button>
										<button type="submit" class="btn green">
											<i class="fa fa-plus"></i> 保存
										</button>
									</div>
								</div>
								</c:if>
							</form>
						</div>
						
						<div id="tabAchievementAwardScript" class="tab-pane ">
							<form id="dataForm" action="" data-action="${ctx}/setting/edit/achievementAwardScript" class="form-horizontal" method="post">
								<div class="form-group">
									<label class="control-label col-md-3">获得成就奖励:</label>
									<div class="col-md-9">
										<p class="form-control-static">
										<textarea rows="" cols="" name="achievementAwardScript" id="achievementAwardScript" style="width: 600px; height: 400px;">${setting.achievementAwardScript}</textarea>
										</p>
									</div>
								</div>
							
								<div class="form-group">
									<label class="control-label col-md-3">校验结果:</label>
									<div class="col-md-9">
										<p id="achievementAwardScriptResult" class="form-control-static">
										</p>
									</div>
								</div>
								<c:if test="${show}">
								<div class="form-group">
									<label class="control-label col-md-3"></label>
									<div class="col-md-9">
										<button id="achievementAwardScriptCheckBtn" type="button" class="btn yellow">
											<i class="fa fa-plus"></i> 校验
										</button>
										<button type="submit" class="btn green">
											<i class="fa fa-plus"></i> 保存
										</button>
									</div>
								</div>
								</c:if>
							</form>
						</div>
						
						<div id="tabSignInPointScript" class="tab-pane ">
							<form id="dataForm" action="" data-action="${ctx}/setting/edit/signInPointScript" class="form-horizontal" method="post">
								<div class="form-group">
									<label class="control-label col-md-3">获得成就奖励:</label>
									<div class="col-md-9">
										<p class="form-control-static">
										<textarea rows="" cols="" name="signInPointScript" id="signInPointScript" style="width: 600px; height: 400px;">${setting.signInPointScript}</textarea>
										</p>
									</div>
								</div>
							
								<div class="form-group">
									<label class="control-label col-md-3">校验结果:</label>
									<div class="col-md-9">
										<p id="signInPointScriptResult" class="form-control-static">
										</p>
									</div>
								</div>
								<c:if test="${show}">
								<div class="form-group">
									<label class="control-label col-md-3"></label>
									<div class="col-md-9">
										<button id="signInPointScriptCheckBtn" type="button" class="btn yellow">
											<i class="fa fa-plus"></i> 校验
										</button>
										<button type="submit" class="btn green">
											<i class="fa fa-plus"></i> 保存
										</button>
									</div>
								</div>
								</c:if>
							</form>
						</div>
						
					</div>
					
				</div>
				
			</div>

		</div>

	</div>
</div>


