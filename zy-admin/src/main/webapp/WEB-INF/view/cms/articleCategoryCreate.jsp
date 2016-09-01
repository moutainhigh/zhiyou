<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/view/include/head.jsp"%>
<%@ include file="/WEB-INF/view/include/form.jsp"%>
<script type="text/javascript">
	$(function() {
		
		$("#createForm").validate({
			rules : {
				'name' : {
					required : true,
					maxlength : 30
				},
				'indexNumber' : {
					required : true
				},
				'isVisiable' : {
					required : true
				}
			},
			messages : {
				'name' : {
					required : '请填写名称'
				},
				'indexNumber' : {
					required : '请填写显示排列顺序'
				},
				'isVisiable' : {
					required : '请选择是否可见'
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
    <li><a href="javascript:;" data-href="${ctx}/articleCategory">文章类别管理</a></li>
  </ul>
</div>
<!-- END PAGE HEADER-->

<div class="row">
  <div class="col-md-12">
    <!-- BEGIN VALIDATION STATES-->
    <div class="portlet box yellow">
      <div class="portlet-title">
        <div class="caption">
          <i class="fa fa-edit"></i><span> 文章类别创建 </span>
        </div>
        <div class="tools">
          <a href="javascript:;" class="collapse"> </a> <a href="javascript:;" class="reload"> </a>
        </div>
      </div>
      <div class="portlet-body form">
        <!-- BEGIN FORM-->
        <form id="createForm" action="" data-action="${ctx}/articleCategory/create" class="form-horizontal" method="post">
          <div class="form-body">
            <div class="alert alert-danger display-hide">
              <i class="fa fa-exclamation-circle"></i>
              <button class="close" data-close="alert"></button>
              <span class="form-errors">您填写的信息有误，请检查。</span>
            </div>
            
            <c:if test="${not empty articleCategories}">
            	<div class="form-group">
	              <label class="control-label col-md-3">父类导航节点
	              </label>
	              <div class="col-md-4">
	                  <select name="parentId" class="form-control">
	              		<option value="">-请选择父类导航节点-</option>
	              		<c:forEach items="${articleCategories}" var="articleCategory">
	              			<option value="${articleCategory.id}">${articleCategory.name}</option>
	              		</c:forEach>
	              	</select>
	              </div>
	            </div>
            </c:if>
            
            <div class="form-group">
              <label class="control-label col-md-3">名称
              </label>
              <div class="col-md-4">
                  <input type="text" name="name" id="name" class="form-control" maxlength="40"
                      value="${articleCategory.name}" placeholder="请输入名称" />
              </div>
            </div>

            <div class="form-group">
              <label class="control-label col-md-3">显示排列顺序
              </label>
              <div class="col-md-4">
				 <input type="text" name="indexNumber" id="indexNumber" class="form-control" maxlength="40"
                      value="${articleCategory.indexNumber}" placeholder="请填写显示排列顺序" />
              </div>
            </div>
            
            <div class="form-group">
              <label class="control-label col-md-3">是否可见
              </label>
              <div class="col-md-4">
				 <select name="isVisiable" class="form-control">
				 	<option value="">--请选择是否可见--</option>
				 	<option value="true">--是--</option>
				 	<option value="false">--否--</option>
				 </select>
              </div>
            </div>
            
          </div>
          
          <div class="form-actions fluid">
            <div class="col-md-offset-3 col-md-9">
              <button type="submit" class="btn green">保存</button>
              <button class="btn default" data-href="${ctx}/articleCategory">
                <i class="fa fa-arrow-left"></i> 返回
              </button>
            </div>
          </div>
        </form>
        
      </div>
    </div>
  </div>
</div>
