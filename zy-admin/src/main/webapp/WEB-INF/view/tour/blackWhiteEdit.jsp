<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/view/include/head.jsp"%>
<%@ include file="/WEB-INF/view/include/form.jsp"%>
<!-- BEGIN JAVASCRIPTS -->

<script>
	$(function() {

		$('#createForm').validate({
			rules : {
				phone : {
					required: true,
					number : true
				},
				type : {
					required : true
				},
				number : {
					required : true,
					number : true

				}
			},
			messages: {
				phone: {
					required: '请输入手机号',
					number : '只能输入数字'
				},
				type : {
					required : '请选择黑/白类型'
				},
				number: {
					required: '请输入限制人数',
					number : '只能输入数字'
				},

			},
		});
	});

	$form = $('#createForm');

	$('#addBlackWhiteSubmit').bind('click', function () {
		var count = 8;
		var result = $form.validate().form();
		if (result) {
			var number = $("#number").val();
			var type = $("#selectType").val();
			if(type == 1  && (number==null||number > count)){
				layer.alert('操作失败，黑名单人数不得大于'+count+'个');
				return false;
			}else if(type == 2 ){
				if(number != null && number <= count){
					layer.alert('操作失败，白名单人数不得小于'+count+'个');
					return false;
				}
			}
		}
	});

	$("#selectType").change(function () {
		$("#number").val("");
	});


</script>
<!-- END JAVASCRIPTS -->

<!-- BEGIN PAGE HEADER-->
<div class="page-bar">
	<ul class="page-breadcrumb">
		<li><i class="fa fa-home"></i> <a href="javascript:;" data-href="${ctx}/main">首页</a> <i class="fa fa-angle-right"></i></li>
		<li><a href="javascript:;" data-href="${ctx}/tour/blackOrWhite">黑白名单管理</a></li>
	</ul>
</div>
<!-- END PAGE HEADER-->

<div class="row">
	<div class="col-md-12">
		<!-- BEGIN VALIDATION STATES-->
		<div class="portlet light bordered">
			<div class="portlet-title">
				<div class="caption">
					<i class="icon-speech"></i><span> 编辑黑白名单</span>
				</div>
				<div class="tools">
					<a href="javascript:;" class="collapse"> </a> <a href="javascript:;" class="reload"> </a>
				</div>
			</div>
			<div class="portlet-body form">
				<!-- BEGIN FORM-->
				<form id="createForm" action="" data-action="${ctx}/tour/editBlackWhite" class="form-horizontal" method="post">
					<div class="form-body">
						<input type="hidden" name="id" value="${blackOrWhiteAdminVo.id}"/>
						<input type="hidden" name="userId" value="${blackOrWhiteAdminVo.user.id}"/>
						<div class="form-group">
							<label class="control-label col-md-3">昵称<span class="required"> * </span>
							</label>
							<div class="col-md-5">
								<input type="text" readonly="true" class="form-control" id="nickname" name="nickname" value="${blackOrWhiteAdminVo.user.nickname}"/>
							</div>
						</div>
						<div class="form-group">
							<label class="control-label col-md-3">手机号<span class="required"> * </span>
							</label>
							<div class="col-md-5">
								<input type="text" readonly="true" class="form-control" id="phone" name="phone" value="${blackOrWhiteAdminVo.user.phone}" placeholder="请输入手机号" />
							</div>
						</div>
						<div class="form-group">
							<label class="control-label col-md-3">黑/白名单类型<span class="required"> * </span></label>
							<div class="col-md-5">
								<select style="display: block; width: 40%;" id="selectType" class="form-control pull-left" id="type" name="type">
									<option value="1" <c:if test="${blackOrWhiteAdminVo.type == 1}"> selected="selected"</c:if>>黑名单</option>
									<option value="2" <c:if test="${blackOrWhiteAdminVo.type == 2}"> selected="selected"</c:if>>白名单</option>
								</select>
							</div>
						</div>
						<div class="form-group">
							<label class="control-label col-md-3">限制人数<span class="required"> * </span>
							</label>
							<div class="col-md-5">
								<input type="text" class="form-control" id="number" name="number" value="${blackOrWhiteAdminVo.number}" placeholder="请输入限制人数"/>
							</div>
						</div>
					</div>
					<div class="form-actions fluid">
						<div class="col-md-offset-3 col-md-9">
							<button id="addBlackWhiteSubmit" type="submit" class="btn green">
								<i class="fa fa-save"></i> 保存
							</button>
							<button class="btn default" data-href="${ctx}/tour/blackOrWhite">
								<i class="fa fa-chevron-left"></i> 返回
							</button>
						</div>
					</div>

				</form>
				<!-- END FORM-->
			</div>
		</div>
		<!-- END VALIDATION STATES-->
	</div>
</div>
