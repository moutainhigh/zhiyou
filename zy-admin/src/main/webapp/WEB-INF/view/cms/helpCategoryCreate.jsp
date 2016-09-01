<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/view/include/head.jsp"%>
<%@ include file="/WEB-INF/view/include/form.jsp"%>
<!-- BEGIN JAVASCRIPTS -->

 <script>
	$(function() {

		$('#createForm').validate({
			rules : {
				code : {
					required : true
				},
				name : {
					required : true
				},
				indexNumber : {
					required : true,
					digits : true
				},
				userType : {
					required : true
				}
			},
			messages : {
				code : {
					required : '请输入code'
				},
				name : {
					required : '请输入内容'
				},
				indexNumber : {
					required : '请输入排序数字',
					digits : '只能输入数字'
				},
				userType : {
					requered : '请你选择针对用户类型'
				}
			}
		});
		
	});
	 
</script> 
<!-- END JAVASCRIPTS -->

<!-- BEGIN PAGE HEADER-->
<div class="page-bar">
	<ul class="page-breadcrumb">
		<li><i class="fa fa-home"></i> <a href="javascript:;" data-href="${ctx}/main">首页</a> <i class="fa fa-angle-right"></i></li>
		<li><a href="javascript:;" data-href="${ctx}/helpCategory">帮助管理</a></li>
	</ul>
</div>
<!-- END PAGE HEADER-->

<div class="row">
	<div class="col-md-12">
		<!-- BEGIN VALIDATION STATES-->
		<div class="portlet box green">
			<div class="portlet-title">
				<div class="caption">
					<i class="fa fa-plus-circle"></i><span> 创建帮助归类</span>
				</div>
				<div class="tools">
					<a href="javascript:;" class="collapse"> </a> <a href="javascript:;" class="reload"> </a>
				</div>
			</div>
			<div class="portlet-body form">
				<!-- BEGIN FORM-->
				<form id="createForm" action="" data-action="${ctx}/helpCategory/create" class="form-horizontal" method="post">
					<div class="form-body">
						<div class="alert alert-danger display-hide">
							<i class="fa fa-exclamation-circle"></i>
							<button class="close" data-close="alert"></button>
							<span class="form-errors">您填写的信息有误，请检查。</span>
						</div>
						<div class="form-group">
							<label class="control-label col-md-3">code<span class="required"> * </span></label>
							<div class="col-md-5">
								<input type="text" class="form-control" name="code" id="code" value="${helpCategory.code}" placeholder="只允许输入字母组合"/>
							</div>
						</div>
						
						<div class="form-group">
							<label class="control-label col-md-3">内容<span class="required"> * </span>
							</label>
							<div class="col-md-5">
								<input type="text" class="form-control" name="name" id="name" value="${helpCategory.name}" />		
							</div>
						</div>
						
						<div class="form-group">
							<label class="control-label col-md-3">排序数字<span class="required"> * </span>
							</label>
							<div class="col-md-5">
								<input type="text" class="form-control" name="indexNumber" id="indexNumber" value="${helpCategory.indexNumber}" />		
							</div>
						</div>
						
						<div class="form-group">
							<label class="control-label col-md-3">针对用户类型<span class="required"> * </span></label>
							<div class="col-md-5">
								<div class="input-icon right">
									<select name="userType" id="userType" class="form-control">
					               		<option value="">--请选择针对用户类型-- </option>
					               		<option value="0"<c:if test="${helpCategory.userType == '0' }"> selected="selected"</c:if>>商家</option>
					               		<option value="1"<c:if test="${helpCategory.userType == '1' }"> selected="selected"</c:if>>试客</option>
					                  </select>
								</div>
							</div>
						</div>
						
					</div>
					<div class="form-actions fluid">
						<div class="col-md-offset-3 col-md-9">
							<button type="submit" class="btn green">
								<i class="fa fa-plus"></i> 保存</button>
							<button class="btn default" data-href="${ctx}/helpCategory">
								<i class="fa fa-arrow-left"></i> 返回
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
