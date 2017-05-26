<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/view/include/head.jsp"%>
<%@ include file="/WEB-INF/view/include/form.jsp"%>
<script>
  $(function() {
    $('#form').validate({
      rules: {
        'isUseLogistics' : {
          required: true
        },
        'logisticsFee' : {
          digit: true,
          min : 0
        }
      },
      submitHandler : 
        function(form){
          var val = $('#isUseLogistics').find('option:selected').val();
          if(val == 'true') {
            var logisticsName = $('#logisticsName').val();
            var logisticsSn = $('#logisticsSn').val();
            var logisticsFee = $('#logisticsFee').val();
            if(!logisticsName) {
              layer.alert('请填写物流公司名称');
              return;
            }
            if(!logisticsSn) {
              layer.alert('请填写物流单号');
              return;
            }
            if(!logisticsFee) {
              layer.alert('请填写物流费');
              return;
            }
          }
    	  
  		  $(form).find(':submit').prop('disabled', true);
  		  Layout.postForm(form);
  		}
    });
    
    $('#isUseLogistics').change(function(){
      var val = $(this).find('option:selected').val();
      if(val == 'true') {
        $('#logistics').show();
      } else if(val == 'false') {
        $('#logistics').hide();
      }
    });
    
  });
</script>
<!-- END JAVASCRIPTS -->

<!-- BEGIN PAGE HEADER-->
<div class="page-bar">
  <ul class="page-breadcrumb">
    <li><i class="fa fa-home"></i> <a href="javascript:;" data-href="${ctx}/main">首页</a> <i class="fa fa-angle-right"></i></li>
    <li><a href="javascript:;" data-href="${ctx}/order/paid">订单管理</a></li>
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
      </div>
      <div class="portlet-body form">
        <!-- BEGIN FORM-->
        <form id="form" action="" data-action="${ctx}/order/paid/deliver" class="form-horizontal" method="post">
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
              <label class="control-label col-md-3">发货方式<span class="font-red">*</span> </label>
              <div class="col-md-5">
                <select name="isUseLogistics" id="isUseLogistics" class="form-control">
                  <option value="">-- 请选择 --</option>
                  <option value="false">面对面发货</option>
                  <option value="true">物流发货</option>
                </select>
              </div>
            </div>
            
            <span id="logistics" style="display: none;">
	        <div class="form-group">
	          <label class="control-label col-md-3">物流公司 <span class="font-red">*</span></label>
	          <div class="col-md-5">
	            <input type="text" name="logisticsName" id="logisticsName" class="form-control" value="${order.logisticsName}" />
	          </div>
	        </div>
	        
	        <div class="form-group">
	          <label class="control-label col-md-3">物流单号 <span class="font-red">*</span></label>
	          <div class="col-md-5">
	            <input type="text" name="logisticsSn" id="logisticsSn" class="form-control" value="${order.logisticsSn}" />
	          </div>
	        </div>
          
          <div class="form-group">
            <label class="control-label col-md-3">物流费 <span class="font-red">*</span></label>
            <div class="col-md-5">
              <input type="text" name="logisticsFee" id="logisticsFee" class="form-control" value="${order.logisticsFee}" />
            </div>
          </div>
	      </span>  
          
          </div>
          <div class="form-actions fluid">
            <div class="col-md-offset-3 col-md-9">
              <button type="submit" class="btn green">
                <i class="fa fa-save"></i> 保存
              </button>
              <button class="btn default" data-href="${ctx}/order/paid">
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
