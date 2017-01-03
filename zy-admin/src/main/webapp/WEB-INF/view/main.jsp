<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/view/include/taglib.jsp"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!-- BEGIN JAVASCRIPTS -->
<style type="text/css">
	a {
		cursor: point;
	}
</style>
<!-- END JAVASCRIPTS -->

<!-- BEGIN PAGE HEADER-->
<h3 class="page-title">
  ${sys} <small>管理系统</small>
</h3>
<div class="page-bar">
  <ul class="page-breadcrumb">
    <li><i class="fa fa-home"></i> <a class="ajaxify" data-href="${ctx}/main">首页</a>
  </ul>
  <!-- <div class="page-toolbar">
    <div id="" class="pull-right tooltips btn btn-fit-height grey-salt" data-placement="top" data-original-title="">
      <i class="icon-calendar"></i>&nbsp; <span class="thin uppercase visible-lg-inline-block"></span>&nbsp; <i
        class="fa fa-angle-down"></i>
    </div>
  </div> -->
</div>
<!-- END PAGE HEADER-->

	<div class="row">
      <div class="col-md-6">
      <!-- BEGIN Portlet PORTLET-->
        <div class="portlet light bordered">
            <div class="portlet-title">
                <div class="caption">
                    <i class="icon-clock"></i>
                    <span class="caption-subject bold uppercase"> 待客服处理</span>
                </div>
            </div>
            <div class="portlet-body">
                <table class="table table-bordered table-hover">
                    <tbody>
                        <tr>
                            <td> 待审核银行卡 </td>
                            <td> ${userBankInfoCount} </td>
                            <td><c:if test="${userBankInfoCount > 0}"><a data-href="${ctx}/userBankInfo" class="btn green btn-xs"><i class="fa fa-edit"></i>去审核</a></c:if></td>
                        </tr>
                        <tr>
                            <td> 待审核实名认证 </td>
                            <td> ${userInfoCount} </td>
                            <td><c:if test="${userInfoCount > 0}"><a data-href="${ctx}/userInfo" class="btn green btn-xs"><i class="fa fa-edit"></i>去审核</a></c:if></td>
                        </tr>
                        <tr>
                            <td> 待预审核检测报告 </td>
                            <td> ${reportPreCount} </td>
                            <td><c:if test="${reportPreCount > 0}"><a data-href="${ctx}/report" class="btn green btn-xs"><i class="fa fa-edit"></i>去审核</a></c:if></td>
                        </tr>
                        <tr>
                            <td> 待审核检测报告 </td>
                            <td> ${reportCount} </td>
                            <td><c:if test="${reportCount > 0}"><a data-href="${ctx}/report" class="btn green btn-xs"><i class="fa fa-edit"></i>去审核</a></c:if></td>
                        </tr>
                        <tr>
                            <td> 待平台发货订单 </td>
                            <td> ${orderPlatformDeliverCount} </td>
                            <td><c:if test="${orderPlatformDeliverCount > 0}"><a data-href="${ctx}/order/platformDeliverList" class="btn green btn-xs"><i class="fa fa-edit"></i>去发货</a></c:if></td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>
      <!-- END Portlet PORTLET-->
      </div>
  
	  <div class="col-md-6">
		<div class="portlet light bordered">
          <div class="portlet-title">
            	<div class="caption">
              	<i class="icon-users"></i>
              	<span class="caption-subject bold uppercase"> 用户统计信息</span>
            	</div>
            	<div class="actions">
              	<a class="btn btn-default btn-sm" data-href="${ctx}/user">
                  <i class="fa fa-plus"></i> 查看 </a>
              </div>
          </div>
          <div class="portlet-body">
              <table class="table table-bordered table-hover">
				<tbody>
				<tr>
  					<td>服务商总人数</td>
  					<td>${agentCount}</td>
				</tr>
				</tbody>
			 </table>
          </div>
        </div>
      </div>
    
      <div class="col-md-6">
        <div class="portlet light bordered">
          <div class="portlet-title">
              <div class="caption">
                <i class="icon-diamond"></i>
                <span class="caption-subject bold uppercase"> 待处理财务相关</span>
              </div>
          </div>
          <div class="portlet-body">
            <table class="table table-bordered table-hover">
              <tbody>
              <tr>
                  <td> 待处理提现 </td>
                  <td> ${withdrawCount} </td>
                  <td><c:if test="${withdrawCount > 0}"><a data-href="${ctx}/withdraw" class="btn green btn-xs"><i class="fa fa-edit"></i>去审核</a></c:if></td>
              </tr>
              <tr>
                  <td> 待处理充值 </td>
                  <td> ${depositCount} </td>
                  <td><c:if test="${depositCount > 0}"><a data-href="${ctx}/deposit" class="btn green btn-xs"><i class="fa fa-edit"></i>去审核</a></c:if></td>
              </tr>
              <tr>
                  <td> 待确认支付 </td>
                  <td> ${paymentCount} </td>
                  <td><c:if test="${paymentCount > 0}"><a data-href="${ctx}/payment" class="btn green btn-xs"><i class="fa fa-edit"></i>去确认</a></c:if></td>
              </tr>
              </tbody>
             </table>
            </div>
          </div>
      </div>
      
	</div>
	
	<div class="row">
		<div class="col-md-12">
			<div class="portlet light bordered">
				<div class="portlet-title">
	              <div class="caption">
	                  <i class="icon-graph"></i>
	                  <span class="caption-subject bold uppercase"> 注册日统计报表</span>
	              </div>
	          </div>
	          <div class="portlet-body">
	              <div id="registerChart" style="height:400px;">
	                  
	              </div>
	          </div>
			</div>
		</div>
	</div>
	<script>
	$(function(){
		var myChart = echarts.init(document.getElementById('registerChart'));
		// 指定图表的配置项和数据
	    myChart.setOption({
		    tooltip: {},
		    legend: {
		        data:['服务商注册人数']
		    },
		    xAxis: {
		        data: []
		    },
		    yAxis: {},
		    series: [
		    {
		        name: '服务商注册人数',
		        type: 'line',
		        data: []
		    }]
		});
	 	// 异步加载数据
	   $.post('${ctx}/main/ajaxChart/register',{},function(result) {
	       myChart.setOption({
	           xAxis: {
	               data: result.chartLabel
	           },
	           yAxis: {
	        	   splitNumber : 5
	           },
	           series: [{
	               // 根据名字对应到相应的系列
	               name: '服务商注册人数',
	               data: result.userCount
	           }]
	       });
	   });
	})
	</script>
	
	<div class="row">
		<div class="col-md-12">
			<div class="portlet light bordered">
	          <div class="portlet-title">
	              <div class="caption">
	                  <i class="icon-graph"></i>
	                  <span class="caption-subject bold uppercase"> 订单日统计报表</span>
	              </div>
	          </div>
	          <div class="portlet-body">
	              <div id="orderChart" style="height:400px;">
	                  
	              </div>
	          </div>
	      </div>
		</div>
	</div> 
	<script>
	$(function(){
		var myChart = echarts.init(document.getElementById('orderChart'));
		// 指定图表的配置项和数据
	    myChart.setOption({
		    tooltip: {},
		    legend: {
		        data:['订单支数']
		    },
		    xAxis: {
		        data: []
		    },
		    yAxis: {},
		    series: [
		    {
		        name: '订单支数',
		        type: 'line',
		        data: []
		    }]
		});
	 	// 异步加载数据
	   $.post('${ctx}/main/ajaxChart/order',{},function(result) {
	       myChart.setOption({
	           xAxis: {
	               data: result.chartLabel
	           },
	           yAxis: {
	        	   splitNumber : 5
	           },
	           series: [{
	               // 根据名字对应到相应的系列
	               name: '订单支数',
	               data: result.orderCount
	           },]
	       });
	   });
	})
	</script>
	
	</script>