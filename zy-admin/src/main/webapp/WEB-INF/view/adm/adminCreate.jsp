<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/view/include/head.jsp"%>
<%@ include file="/WEB-INF/view/include/form.jsp"%>
<!-- BEGIN JAVASCRIPTS -->
<script>
  $(function() {
    $('#dataForm').validate({
      rules: {
        phone: {
          required: true,
          remote: {
	          url: '${ctx}/admin/checkUserPhone',
	          type: 'post',
	          data: {
	            phone: function() {
	              return $("#phone").val();
	            }
	          }
	       }
        }
      },
      messages: {
        phone: {
          required : '请填写手机号',
          remote: '该手机号已经存在'
        }
      }
    });
	
    $('#adminType').change(function(){
    	var $adminType = $('#adminType');
    	var $adminTypeDiv = $('#adminTypeDiv');
    	var $phoneDiv = $('#phoneDiv');
    	var $siteDiv = $('#siteDiv');
    	var $roleDiv = $('#roleDiv');
    	var $submitBtn = $('#submitBtn');
    	if($adminType.val() == '全局管理员'){
    		$adminTypeDiv.addClass('hide');
    		$phoneDiv.removeClass('hide');
    		$roleDiv.removeClass('hide');
    		$siteDiv.addClass('hide');
    		$submitBtn.removeClass('hide');
    	}
    });
    
  });
</script>
<!-- END JAVASCRIPTS -->

<!-- BEGIN PAGE HEADER-->
<div class="page-bar">
  <ul class="page-breadcrumb">
    <li><i class="fa fa-home"></i> <a href="javascript:;" data-href="${ctx}/main">首页</a> <i class="fa fa-angle-right"></i></li>
    <li><a href="javascript:;" data-href="${ctx}/admin">管理员管理</a></li>
  </ul>
</div>
<!-- END PAGE HEADER-->

<div class="row">
  <div class="col-md-12">
    <!-- BEGIN VALIDATION STATES-->
    <div class="portlet box green">
      <div class="portlet-title">
        <div class="caption">
          <i class="fa fa-plus-circle"></i><span> 管理员创建</span>
        </div>
        <div class="tools">
          <a href="javascript:;" class="collapse"> </a> <a href="javascript:;" class="reload"> </a>
        </div>
      </div>
      <div class="portlet-body form">
        <!-- BEGIN FORM-->
        <form id="dataForm" action="" data-action="${ctx}/admin/create" class="form-horizontal" method="post">
          <div class="form-body">
            <div class="alert alert-danger display-hide">
              <i class="fa fa-exclamation-circle"></i>
              <button class="close" data-close="alert"></button>
              <span class="form-errors">您填写的信息有误，请检查。</span>
            </div>
                       
            <div class="form-group" id="phoneDiv">
              <label class="control-label col-md-3">手机号<span class="required"> * </span>
              </label>
              <div class="col-md-5">
                <div class="input-icon right">
                  <i class="fa fa-user"></i> <input type="text" class="form-control" name="phone" id="phone" value="" />
                </div>
              </div>
            </div>
            <div class="form-group" id="roleDiv">
              <label class="control-label col-md-3">角色<span class="required"> * </span>
              </label>
              <div class="col-md-5">
                <div class="checkbox-list">
                	<c:forEach items="${roles}" var="role" varStatus="varStatus">
                    <label> 
                    	<input type="checkbox" value="${role.id}" name="roleIds"> ${role.name}
                    </label>
                    </c:forEach>
                </div>
              </div>
            </div>
          </div>
          <div class="form-actions fluid">
            <div class="col-md-offset-3 col-md-9">
              <button id="submitBtn" type="submit" class="btn green">
                <i class="fa fa-plus"></i> 保存
              </button>
              <button class="btn default" data-href="${ctx}/admin">
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
