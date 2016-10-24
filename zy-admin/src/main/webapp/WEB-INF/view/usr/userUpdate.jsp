<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/view/include/head.jsp"%>
<%@ include file="/WEB-INF/view/include/form.jsp"%>
<!-- BEGIN JAVASCRIPTS -->
<script>
  var id = ${user.id};
  $(function() {
    $('#updateForm').validate({
      rules : {
	    	newPassword : {
				minlength : 6
			},
			rePassword : {
				equalTo : "#newPassword"
			}
      },
      messages : {
        password : {
          minlength : '密码不得少于6个字符'
        },
        passwordSure : {
			equalTo : '两次输入的密码不一致'
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
    <li><a href="javascript:;" data-href="${ctx}/user">用户管理</a></li>
  </ul>
</div>
<!-- END PAGE HEADER-->

<div class="row">
  <div class="col-md-12">
    <!-- BEGIN VALIDATION STATES-->
    <div class="portlet light bordered">
      <div class="portlet-title">
        <div class="caption">
          <i class="fa fa-user"></i><span> 用户信息修改 </span>
        </div>
      </div>
      <div class="portlet-body form">
        <!-- BEGIN FORM-->
        <form id="updateForm" action="" data-action="${ctx}/user/update" class="form-horizontal" method="post">
          <input type="hidden" name="id" value="${user.id}" />
          <div class="form-body">
            <div class="alert alert-danger display-hide">
              <i class="fa fa-exclamation-circle"></i>
              <button class="close" data-close="alert"></button>
              <span class="form-errors">您填写的信息有误，请检查。</span>
            </div>
            <div class="form-group">
              <label class="control-label col-md-3">昵称:
              </label>
              <div class="col-md-4">
                  <div class="form-control-static">${user.nickname}</div>
              </div>
            </div>
            <div class="form-group">
              <label class="control-label col-md-3">手机号:
              </label>
              <div class="col-md-4">
                <div class="input-icon right">
                 <div class="form-control-static"> ${user.phone}</div>
                </div>
              </div>
            </div>
            <div class="form-group">
              <label class="control-label col-md-3">密码:
              </label>
              <div class="col-md-4">
                <input type="password" id="newPassword" class="form-control" name="newPassword" value="" />
              </div>
            </div>
            <div class="form-group">
              <label class="control-label col-md-3">确认密码:
              </label>
              <div class="col-md-4">
                <input type="password" class="form-control" name="rePassword" value="" />
              </div>
            </div>
          </div>
          <div class="form-actions fluid">
            <div class="col-md-offset-3 col-md-9">
              <button type="submit" class="btn green">
                <i class="fa fa-save"></i> 保存
              </button>
              <button class="btn default" data-href="${ctx}/user">
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
