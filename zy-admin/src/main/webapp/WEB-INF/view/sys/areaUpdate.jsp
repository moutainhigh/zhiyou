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
        },
        orderNumber: {
            required: true,
            number : true
        }
      },
      messages: {
   	  	name: {
          required : '请输入标题'
        },
        orderNumber: {
            required: '请输入排序数字',
            number : '只能输入数字'
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
    <li><a href="javascript:;" data-href="${ctx}/area">区域管理</a></li>
  </ul>
</div>
<!-- END PAGE HEADER-->

<div class="row">
  <div class="col-md-12">
    <!-- BEGIN VALIDATION STATES-->
    <div class="portlet box green">
      <div class="portlet-title">
        <div class="caption">
          <i class="fa fa-plus-circle"></i><span> 区域新增</span>
        </div>
        <div class="tools">
          <a href="javascript:;" class="collapse"> </a> <a href="javascript:;" class="reload"> </a>
        </div>
      </div>
      <div class="portlet-body form">
        <!-- BEGIN FORM-->
        <form id="dataForm" action="" data-action="${ctx}/area/update" class="form-horizontal" method="post">
        	<input type="hidden" name="id" value="${area.id}" />
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
	              	<input type="text" class="form-control" id="name" name="name" value="${area.name}" />
	              </div>
	            </div>
	            
	            <div class="form-group">
	              <label class="control-label col-md-3">排序数字<span class="required"> * </span>
	              </label>
	              <div class="col-md-5">
	              	<input type="text" class="form-control" id="orderNumber" name="orderNumber" value="${area.orderNumber}" />
	              </div>
	            </div>
            
            </div>
          <div class="form-actions fluid">
            <div class="col-md-offset-3 col-md-9">
              <button type="submit" class="btn green">
                <i class="fa fa-plus"></i> 保存
              </button>
              <button class="btn default" data-href="${ctx}/area">
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
