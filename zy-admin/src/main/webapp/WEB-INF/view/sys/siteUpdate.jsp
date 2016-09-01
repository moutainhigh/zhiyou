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
        code: {
            required: true
        }
      },
      messages: {
   	  	name: {
          required : '请输入标题'
        },
        code: {
            required: '请输入code'
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
    <li><a href="javascript:;" data-href="${ctx}/site">站点管理</a></li>
  </ul>
</div>
<!-- END PAGE HEADER-->

<div class="row">
  <div class="col-md-12">
    <!-- BEGIN VALIDATION STATES-->
    <div class="portlet box green">
      <div class="portlet-title">
        <div class="caption">
          <i class="fa fa-plus-circle"></i><span> 站点编辑</span>
        </div>
        <div class="tools">
          <a href="javascript:;" class="collapse"> </a> <a href="javascript:;" class="reload"> </a>
        </div>
      </div>
      <div class="portlet-body form">
        <!-- BEGIN FORM-->
        <form id="dataForm" action="" data-action="${ctx}/site/update" class="form-horizontal" method="post">
          <input type="hidden" class="form-control" id="id" name="id" value="${site.id}" />
          <div class="form-body">
            <div class="alert alert-danger display-hide">
              <i class="fa fa-exclamation-circle"></i>
              <button class="close" data-close="alert"></button>
              <span class="form-errors">您填写的信息有误，请检查。</span>
            </div>
            
            <div class="form-group">
              <label class="control-label col-md-3">标题<span class="required"> * </span>
              </label>
              <div class="col-md-5">
              	<input type="text" class="form-control" id="name" name="name" value="${site.name}" />
              </div>
            </div>
            
            <div class="form-group">
              <label class="control-label col-md-3">code<span class="required"> * </span>
              </label>
              <div class="col-md-5">
              	<input type="text" class="form-control" id="code" name="code" value="${site.code}" />
              </div>
            </div>
            
			<div class="form-group">
				<label class="control-label col-md-3">是否本地支付</label>
				<div class="col-md-5">
					<input type="hidden" name="_isLocalPay" value="false" />
					<label class="checkbox-inline"><input type="checkbox" id="isLocalPay" name="isLocalPay"<c:if test="${site.isLocalPay}">checked=checked </c:if> value="true" /> 是否本地支付</label>
				</div>
			</div>
			
            <div class="form-group">
              <label class="control-label col-md-3">qqLoginAppKey
              </label>
              <div class="col-md-5">
              	<input type="text" class="form-control" id="qqLoginAppKey" name="qqLoginAppKey" value="${site.qqLoginAppKey}" />
              </div>
            </div>
            
            <div class="form-group">
              <label class="control-label col-md-3">qqLoginAppSecret
              </label>
              <div class="col-md-5">
              	<input type="text" class="form-control" id="qqLoginAppSecret" name="qqLoginAppSecret" value="${site.qqLoginAppSecret}" />
              </div>
            </div>  
            
            <div class="form-group">
              <label class="control-label col-md-3">weixinLoginAppKey
              </label>
              <div class="col-md-5">
              	<input type="text" class="form-control" id="weixinLoginAppKey" name="weixinLoginAppKey" value="${site.weixinLoginAppKey}" />
              </div>
            </div>
            
            <div class="form-group">
              <label class="control-label col-md-3">weixinLoginAppSecret
              </label>
              <div class="col-md-5">
              	<input type="text" class="form-control" id="weixinLoginAppSecret" name="weixinLoginAppSecret" value="${site.weixinLoginAppSecret}" />
              </div>
            </div>
            
            <div class="form-group">
              <label class="control-label col-md-3">weixinPayMchId
              </label>
              <div class="col-md-5">
              	<input type="text" class="form-control" id="weixinPayMchId" name="weixinPayMchId" value="${site.weixinPayMchId}" />
              </div>
            </div>
            
            <div class="form-group">
              <label class="control-label col-md-3">weixinPaySecret
              </label>
              <div class="col-md-5">
              	<input type="text" class="form-control" id="weixinPaySecret" name="weixinPaySecret" value="${site.weixinPaySecret}" />
              </div>
            </div>  
            
            <div class="form-group">
              <label class="control-label col-md-3">weixinMpAppKey
              </label>
              <div class="col-md-5">
              	<input type="text" class="form-control" id="weixinMpAppKey" name="weixinMpAppKey" value="${site.weixinMpAppKey}" />
              </div>
            </div>
            
            <div class="form-group">
              <label class="control-label col-md-3">weixinMpAppSecret
              </label>
              <div class="col-md-5">
              	<input type="text" class="form-control" id="weixinMpAppSecret" name="weixinMpAppSecret" value="${site.weixinMpAppSecret}" />
              </div>
            </div>
            
            <div class="form-group">
              <label class="control-label col-md-3">alipayBargainorId
              </label>
              <div class="col-md-5">
              	<input type="text" class="form-control" id="alipayBargainorId" name="alipayBargainorId" value="${site.alipayBargainorId}" />
              </div>
            </div>
            
            <div class="form-group">
              <label class="control-label col-md-3">alipayBargainorKey
              </label>
              <div class="col-md-5">
              	<input type="text" class="form-control" id="alipayBargainorKey" name="alipayBargainorKey" value="${site.alipayBargainorKey}" />
              </div>
            </div>                                                        
            
          </div>
          <div class="form-actions fluid">
            <div class="col-md-offset-3 col-md-9">
              <button type="submit" class="btn green">
                <i class="fa fa-plus"></i> 保存
              </button>
              <button class="btn default" data-href="${ctx}/site">
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
