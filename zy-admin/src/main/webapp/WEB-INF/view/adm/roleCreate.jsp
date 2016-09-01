<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/view/include/head.jsp"%>
<%@ include file="/WEB-INF/view/include/form.jsp"%>
<!-- BEGIN JAVASCRIPTS -->
<script>
  $(function() {
    $('#dataForm').validate({
      rules: {
    	  name: {
          required: true
        }
      },
      messages: {
    	  
      }
    });

  });
</script>
<!-- END JAVASCRIPTS -->
<style>
.checker-wrap{
	display: inline-block;
	width: 200px; 
	margin-bottom: 5px;
}
</style>
<!-- BEGIN PAGE HEADER-->
<div class="page-bar">
  <ul class="page-breadcrumb">
    <li><i class="fa fa-home"></i> <a href="javascript:;" data-href="${ctx}/main">首页</a> <i class="fa fa-angle-right"></i></li>
    <li><a href="javascript:;" data-href="${ctx}/role">角色管理</a></li>
  </ul>
</div>
<!-- END PAGE HEADER-->

<div class="row">
  <div class="col-md-12">
    <!-- BEGIN VALIDATION STATES-->
    <div class="portlet box green">
      <div class="portlet-title">
        <div class="caption">
          <i class="fa fa-plus-circle"></i><span> 角色创建</span>
        </div>
      </div>
      <div class="portlet-body form">
        <!-- BEGIN FORM-->
        <form id="dataForm" data-action="${ctx}/role/create" class="form-horizontal" method="post">
          <div class="form-body">
            <div class="alert alert-danger display-hide">
              <i class="fa fa-exclamation-circle"></i>
              <button class="close" data-close="alert"></button>
              <span class="form-errors">您填写的信息有误，请检查。</span>
            </div>
            <div class="form-group">
              <label class="control-label col-md-3">名称<span class="required"> * </span>
              </label>
              <div class="col-md-5">
              	<input type="text" class="form-control" name="name" id="name" value="${role.name}" />
              </div>
            </div>
            <div class="form-group">
              <label class="control-label col-md-3">权限<span class="required"> * </span>
              </label>
              <div class="col-md-5">
                <div class="checkbox-list">
                	<c:forEach items="${permissionMap}" var="permissionEntry">
                		<label class="label label-success" style="height: 26px;padding: 8px 6px;">${permissionEntry.key}</label>
                		<c:forEach items="${permissionEntry.value}" var="subPermissionEntry">
                			<div class="checker-wrap">
                			<input type="checkbox" value="${subPermissionEntry.value}" name="permissions['${subPermissionEntry.key}']">${subPermissionEntry.value}
                			</div>
                		</c:forEach>
                	</c:forEach>
                </div>
              </div>
            </div>
          </div>
          <div class="form-actions fluid">
            <div class="col-md-offset-3 col-md-9">
              <button type="submit" class="btn green">
                <i class="fa fa-plus"></i> 保存
              </button>
              <button class="btn default" data-href="${ctx}/role">
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
