<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/view/include/head.jsp"%>
<%@ include file="/WEB-INF/view/include/form.jsp"%>
<script>
  $(function() {
    $('#form').validate({
      rules: {
        'logisticsName' : {
          required: true
        },
        'logisticsSn' : {
          required: true
        },
        'logisticsFeePayType' : {
          required: true
        },
        'logisticsFee' : {
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
    <li><a href="javascript:;" data-href="${ctx}/order">订单管理</a></li>
  </ul>
</div>
<!-- END PAGE HEADER-->

<div class="row">
  <div class="col-md-12">
    <!-- BEGIN VALIDATION STATES-->
    <div class="portlet light bordered">
      <div class="portlet-title">
        <div class="caption">
          <i class="icon-docs"></i><span> 平台发货</span>
        </div>
        <div class="tools">
          <a href="javascript:;" class="collapse"> </a> <a href="javascript:;" class="reload"> </a>
        </div>
      </div>
      <div class="portlet-body form">
        <!-- BEGIN FORM-->
        <form id="form" action="" data-action="${ctx}/order/deliver" class="form-horizontal" method="post">
          <input type="hidden" name="id" value="${order.id}"/>
        
          <div class="form-body">
            <div class="alert alert-danger display-hide">
              <i class="fa fa-exclamation-circle"></i>
              <button class="close" data-close="alert"></button>
              <span class="form-errors">您填写的信息有误，请检查。</span>
            </div>
            
            <div class="form-group">
              <label class="control-label col-md-3">订单单号 </label>
              <div class="col-md-5">
                <div class="form-control-static">${order.sn}</div>
              </div>
            </div>
            
            <div class="form-group">
              <label class="control-label col-md-3">订单标题 </label>
              <div class="col-md-5">
                <div class="form-control-static">${order.title}</div>
              </div>
            </div>
            
            <div class="form-group">
              <label class="control-label col-md-3">数量</label>
              <div class="col-md-5">
                <div class="form-control-static">${order.quantity}</div>
              </div>
            </div>
            
	        <div class="form-group">
	          <label class="control-label col-md-3">收件人地址 </label>
	          <div class="col-md-5">
	            <div class="form-control-static">${order.receiverProvince} ${order.receiverCity} ${order.receiverDistrict} ${order.receiverAddress} ${order.receiverRealname} ${order.receiverPhone}</div>
	          </div>
	        </div>
	
            <div class="form-group">
              <label class="control-label col-md-3">物流费支付方式<span class="font-red">*</span></label>
              <div class="col-md-5">
                <select name="logisticsFeePayType" class="form-control">
                  <option value="">-- 请选择 --</option>
                  <c:forEach items="${logisticsFeePayTypes}" var="logisticsFeePayType">
                  <option value="${logisticsFeePayType}">${logisticsFeePayType}</option>
                  </c:forEach>
                </select>
              </div>
            </div>

	        <div class="form-group">
	          <label class="control-label col-md-3">发货人物流公司 <span class="font-red">*</span></label>
	          <div class="col-md-5">
	            <input type="text" name="logisticsName" id="logisticsName" class="form-control" value="${order.logisticsName}" >
	          </div>
	        </div>
	        
	        <div class="form-group">
	          <label class="control-label col-md-3">发货人物流单号 <span class="font-red">*</span></label>
	          <div class="col-md-5">
	            <input type="text" name="logisticsSn" id="logisticsSn" class="form-control" value="${order.logisticsSn}">
	          </div>
	        </div>
          
          <div class="form-group">
            <label class="control-label col-md-3">物流费 <span class="font-red">*</span></label>
            <div class="col-md-5">
              <input type="text" name="logisticsFee" id="logisticsFee" class="form-control" value="${order.logisticsFee}" >
            </div>
          </div>
	        
          </div>
          <div class="form-actions fluid">
            <div class="col-md-offset-3 col-md-9">
              <button type="submit" class="btn green">
                <i class="fa fa-save"></i> 保存
              </button>
              <button class="btn default" data-href="${ctx}/order">
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
