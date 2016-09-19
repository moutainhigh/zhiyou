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
		<div class="portlet light bordered">
			<div class="portlet-title">
				<div class="caption">
					<i class="fa"></i> 系统设置
				</div>
			</div>
			<div class="portlet-body clearfix">
				<div class="tabbable tabbable-custom">
					<ul class="nav nav-tabs">
						<li class="active"><a data-toggle="tab" href="#tab__base"> 基本信息</a></li>
						<li><a data-toggle="tab" href="#tabWithdrawFeeRateScript"> 提现费率</a></li>
					</ul>
					<div class="tab-content">
						<div id="tab__base" class="tab-pane active" >
							
							<form id="dataForm" action="" data-action="${ctx}/setting/edit/base" class="form-horizontal" method="post">
								<div class="form-body">
									<div class="row">
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

					</div>
					
				</div>
				
			</div>

		</div>

	</div>
</div>


