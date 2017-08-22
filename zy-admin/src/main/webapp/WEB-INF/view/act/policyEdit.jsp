<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/view/include/head.jsp" %>
<!-- BEGIN JAVASCRIPTS -->
<script>
  $(function () {
    $('#form').validate({
      rules: {
        realname: {
          required: true,
          maxlength: 50
        },
        idCardNumber: {
          required: true,
          maxlength: 50
        },
        birthday: {
          required: true,
          maxlength: 10
        }
      },
      messages: {
        realname: {
          required: '请输入姓名'
        },
        idCardNumber : {
          required : '请输入身份证号'
        },
        birthday: {
          required: '请填写生日'
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
    <li><a href="javascript:;" data-href="${ctx}/policy">保险申请管理</a></li>
  </ul>
</div>
<!-- END PAGE HEADER-->

<div class="row">
  <div class="col-md-12">
    <!-- BEGIN VALIDATION STATES-->
    <div class="portlet light bordered">
      <div class="portlet-title">
        <div class="caption">
          <i class="icon-arrow-down"></i> 编辑保险申请单
        </div>
        <div class="tools">
          <a href="javascript:;" class="collapse"> </a>
        </div>
      </div>
      <div class="portlet-body form">
        <!-- BEGIN FORM-->
        <form id="form" action="" data-action="${ctx}/policy/editPolicy" class="form-horizontal" method="post">
          <div class="form-body">
            <input id="systemFlag" name="id" type="hidden" value="${policyDetailVo.id}"/>
            <div class="alert alert-danger display-hide">
              <i class="fa fa-exclamation-circle"></i>
              <button class="close" data-close="alert"></button>
              <span class="form-errors">您填写的信息有误，请检查。</span>
            </div>
            <div class="form-group">
              <label class="control-label col-md-3">姓名<span class="required"> * </span></label>
              <div class="col-md-5">
                <input type="text" class="form-control" name="realname" id="realname" value="${policyDetailVo.realname}" placeholder="请输入姓名"/>
              </div>
            </div>
            <div class="form-group">
              <label class="control-label col-md-3">身份证号<span class="required"> * </span></label>
              <div class="col-md-5">
                <input type="text" class="form-control" name="idCardNumber" id="idCardNumber" value="${policyDetailVo.idCardNumber}" placeholder="请输入身份证号"/>
              </div>
            </div>
            <div class="form-group">
              <label class="control-label col-md-3">生日<span class="required"> * </span></label>
              <div class="col-md-5">
                <input type="date" name="birthday" id="birthday" class="form-control" value="${policyDetailVo.birthdayLable}" placeholder="填写生日  1900-01-01">
              </div>
            </div>
            <div class="form-actions fluid">
              <div class="col-md-offset-3 col-md-9">
                <button type="submit" class="btn green">
                  <i class="fa fa-save"></i> 保存
                </button>
                <button class="btn default" data-href="${ctx}/policy">
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
