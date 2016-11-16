<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/view/include/head.jsp" %>

<!-- BEGIN JAVASCRIPTS -->

<script>
  $(function () {

    $('#form').validate({
      rules: {
        'batchCode': {
          required: true
        },
        'time': {
          required: true
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
    <li><a href="javascript:;" data-href="${ctx}/policyCode">保险单号</a></li>
  </ul>
</div>
<!-- END PAGE HEADER-->

<div class="row">
  <div class="col-md-12">
    <!-- BEGIN VALIDATION STATES-->
    <div class="portlet light bordered">
      <div class="portlet-title">
        <div class="caption">
          <i class="icon-umbrella"></i> 保险单号生成
        </div>
      </div>
      <div class="portlet-body form">
        <!-- BEGIN FORM-->
        <form id="form" action="" data-action="${ctx}/policyCode/create" class="form-horizontal" method="post">
          <div class="form-body">
            <div class="alert alert-danger display-hide">
              <i class="fa fa-exclamation-circle"></i>
              <button class="close" data-close="alert"></button>
              <span class="form-errors">您填写的信息有误，请检查。</span>
            </div>
            
            <div class="form-group">
              <label class="control-label col-md-3">批次号<span class="required"> * </span></label>
              <div class="col-md-5">
                <input type="text" class="form-control" name="batchCode" id="batchCode" value="" placeholder="唯一不能重复"/>
              </div>
            </div>
            
            <div class="form-group">
              <label class="control-label col-md-3">数量<span class="required"> * </span></label>
              <div class="col-md-5">
                <input type="number" class="form-control" name="time" id="time" value="" placeholder="生成的保险单号数量"/>
              </div>
            </div>
            
          </div>
          <div class="form-actions fluid">
            <div class="col-md-offset-3 col-md-9">
              <button type="submit" class="btn green">
                <i class="fa fa-save"></i> 保存
              </button>
              <button class="btn default" data-href="${ctx}/policyCode">
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
