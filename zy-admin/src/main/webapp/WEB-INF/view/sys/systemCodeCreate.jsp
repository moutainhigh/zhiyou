<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/view/include/head.jsp" %>
<!-- BEGIN JAVASCRIPTS -->
<script>
  $(function () {
    $('#form').validate({
      rules: {
        systemType: {
          required: true,
          maxlength: 50
        },
        systemName: {
          required: true,
          maxlength: 50
        },
        systemValue: {
          required: true,
          maxlength: 10
        },
        systemDesc: {
          required: true,
          maxlength: 50
        }
      },
      messages: {
        systemType: {
          required: '请输入系统类型码'
        },
        systemName : {
          required : '请输入名称'
        },
        systemValue: {
          required: '请输入默认值'
        },
        systemDesc: {
          required: '请输入描述'
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
    <li><a href="javascript:;" data-href="${ctx}/systemCode">系统默认值管理</a></li>
  </ul>
</div>
<!-- END PAGE HEADER-->

<div class="row">
  <div class="col-md-12">
    <!-- BEGIN VALIDATION STATES-->
    <div class="portlet light bordered">
      <div class="portlet-title">
        <div class="caption">
          <i class="icon-arrow-down"></i> 新增系统默认值
        </div>
        <div class="tools">
          <a href="javascript:;" class="collapse"> </a>
        </div>
      </div>
      <div class="portlet-body form">
        <!-- BEGIN FORM-->
        <form id="form" action="" data-action="${ctx}/systemCode/createSystemCode" class="form-horizontal" method="post">
          <div class="form-body">
            <div class="alert alert-danger display-hide">
              <i class="fa fa-exclamation-circle"></i>
              <button class="close" data-close="alert"></button>
              <span class="form-errors">您填写的信息有误，请检查。</span>
            </div>
            <div class="form-group">
              <label class="control-label col-md-3">系统类型码<span class="required"> * </span></label>
              <div class="col-md-5">
                <input type="text" class="form-control" name="systemType" id="systemType" value="" placeholder="请输入系统类型码"/>
              </div>
            </div>
            <div class="form-group">
              <label class="control-label col-md-3">名称<span class="required"> * </span></label>
              <div class="col-md-5">
                <input type="text" class="form-control" name="systemName" id="systemName" value="" placeholder="请输入名称"/>
              </div>
            </div>
            <div class="form-group">
              <label class="control-label col-md-3">默认值<span class="required"> * </span></label>
              <div class="col-md-5">
                <input type="text" class="form-control" name="systemValue" id="systemValue" value="" placeholder="请输入默认值"/>
              </div>
            </div>
            <div class="form-group">
              <label class="control-label col-md-3">描述<span class="required"> * </span></label>
              <div class="col-md-5">
                <input type="text" class="form-control" name="systemDesc" id="systemDesc" value="" placeholder="请输入描述"/>
              </div>
            </div>
            <div class="form-actions fluid">
              <div class="col-md-offset-3 col-md-9">
                <button type="submit" class="btn green">
                  <i class="fa fa-save"></i> 保存
                </button>
                <button class="btn default" data-href="${ctx}/systemCode">
                  <i class="fa fa-chevron-left"></i> 返回
                </button>
              </div>
            </div>
          </div>
        </form>
        <!-- END FORM-->
      </div>
    </div>
    <!-- END VALIDATION STATES-->
  </div>
</div>
