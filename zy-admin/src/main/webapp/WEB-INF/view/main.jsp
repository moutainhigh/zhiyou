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
	                  <i class="icon-diamond"></i>
	                  <span class="caption-subject bold uppercase"> 财务统计信息</span>
	              </div>
	              <div class="actions">
                      <a class="btn btn-default btn-sm" data-href="${ctx}/account">
                          <i class="fa fa-plus"></i> 查看 </a>
                  </div>
	          </div>
			<div class="portlet-body">
				<table class="table table-bordered table-hover">
	              	<thead>
	              		<tr>
							<th>统计栏目</th>
							<th>本金</th>
							<th>时光币</th>
						</tr>
	              	</thead>
					<tbody>
						<tr>
							<td>试客总本金</td>
							<td>${buyerMoneyAndPoint.money}</td>
							<td>${buyerMoneyAndPoint.point}</td>
						</tr>
						<tr>
							<td>商家总本金</td>
							<td>${merchantMoneyAndPoint.money}</td>
							<td>${merchantMoneyAndPoint.point}</td>
						</tr>
						<tr>
							<td>中转账户</td>
							<td>${sysUserMoneyAndPoint.money}</td>
							<td>${sysUserMoneyAndPoint.point}</td>
						</tr>
						<tr>
							<td>资金账户</td>
							<td>${feeUserMoneyAndPoint.money}</td>
							<td>${feeUserMoneyAndPoint.point}</td>
						</tr>
						<tr>
							<td>发放账户</td>
							<td>${grantUserMoneyAndPoint.money}</td>
							<td>${grantUserMoneyAndPoint.point}</td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
	      <!-- END Portlet PORTLET-->
	  </div>
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
	                          <td> 待审核店铺信息 </td>
	                          <td> ${userBankInfoCount} </td>
	                          <td><c:if test="${userBankInfoCount > 0}"><a data-href="${ctx}/userShopInfo" class="btn green btn-xs"><i class="fa fa-edit"></i>去审核</a></c:if></td>
	                      </tr>
	                      <tr>
	                          <td> 待审核试客信息 </td>
	                          <td> ${userBuyerInfoCount} </td>
	                          <td><c:if test="${userBuyerInfoCount > 0}"><a data-href="${ctx}/userBuyerInfo" class="btn green btn-xs"><i class="fa fa-edit"></i>去审核</a></c:if></td>
	                      </tr>
	                      <tr>
	                      	  <td> 待审核计划 </td>
	                          <td> ${taskCount} </td>
	                          <td><c:if test="${taskCount > 0}"><a data-href="${ctx}/task" class="btn green btn-xs"><i class="fa fa-edit"></i>去审核</a></c:if></td>
	                      </tr>
	                      <tr>
	                      	  <td> 待处理申诉 </td>
	                          <td> ${feedbackCount} </td>
	                          <td><c:if test="${feedbackCount > 0}"><a data-href="${ctx}/feedback" class="btn green btn-xs"><i class="fa fa-edit"></i>去审核</a></c:if></td>
	                      </tr>
	                      <tr>
                          	  <td> 待处理提现 </td>
	                          <td> ${withdrawCount} </td>
	                          <td><c:if test="${withdrawCount > 0}"><a data-href="${ctx}/withdraw" class="btn green btn-xs"><i class="fa fa-edit"></i>去审核</a></c:if></td>
	                      </tr>
	                  </tbody>
	              </table>
	          </div>
	      </div>
	      <!-- END Portlet PORTLET-->
	    </div>
	</div>
	
	<div class="row">
	
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
							<td>注册总人数</td>
							<td>${totalCount}</td>
						</tr>
						<tr>
							<td>商家注册数</td>
							<td>${merchantCount}</td>
						</tr>
						<tr>
							<td>试客注册数</td>
							<td>${buyerCount}</td>
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
                	<i class="icon-calendar"></i>
                	<span class="caption-subject bold uppercase"> 计划/任务统计信息</span>
              	</div>
              	<div class="actions">
                	<a class="btn btn-default btn-sm" data-href="${ctx}/taskItem">
                    <i class="fa fa-plus"></i> 查看 </a>
                </div>
	          </div>
	          <div class="portlet-body">
	              <table class="table table-bordered table-hover">
					<tbody>
						<tr>
							<td>进行中的计划数</td>
							<td>${totalTask}</td>
						</tr>
						<tr>
							<td>进行中的任务数</td>
							<td>${pendingCount}</td>
						</tr>
						<tr>
							<td>待接手的任务数</td>
							<td>${remainderCount}</td>
						</tr>
						<tr>
							<td>待接手计划sn</td>
							<td>${remainderTaskSns}</td>
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
		        data:['试客注册人数','商家注册人数','总注册人数']
		    },
		    xAxis: {
		        data: []
		    },
		    yAxis: {},
		    series: [
		    {
		        name: '试客注册人数',
		        type: 'line',
		        data: []
		    },
		    {
		        name: '商家注册人数',
		        type: 'line',
		        data: []
		    },
		    {
		        name: '总注册人数',
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
	               name: '试客注册人数',
	               data: result.buyerCount
	           },
	           {
	               // 根据名字对应到相应的系列
	               name: '商家注册人数',
	               data: result.merchantCount
	           },
	           {
	               // 根据名字对应到相应的系列
	               name: '总注册人数',
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
	                  <span class="caption-subject bold uppercase"> 收益日统计报表</span>
	              </div>
	          </div>
	          <div class="portlet-body">
	              <div id="profitChart" style="height:400px;">
	                  
	              </div>
	          </div>
	      </div>
		</div>
	</div>
	<script>
	$(function(){
		var myChart = echarts.init(document.getElementById('profitChart'));
		// 指定图表的配置项和数据
	    myChart.setOption({
		    tooltip: {},
		    legend: {
		        data:['平台收益总金额','用户收益总金额','团队收益总金额']
		    },
		    xAxis: {
		        data: []
		    },
		    yAxis: {},
		    series: [
		    {
		        name: '平台收益总金额',
		        type: 'line',
		        data: []
		    },
		    {
		        name: '用户收益总金额',
		        type: 'line',
		        data: []
		    },
		    {
		        name: '团队收益总金额',
		        type: 'line',
		        data: []
		    }]
		});
	 	// 异步加载数据
	   $.post('${ctx}/main/ajaxChart/profit',{},function(result) {
	       myChart.setOption({
	           xAxis: {
	               data: result.chartLabel
	           },
	           yAxis: {
	        	   splitNumber : 5
	           },
	           series: [{
	               // 根据名字对应到相应的系列
	               name: '平台收益总金额',
	               data: result.feeAmount
	           },
	           {
	               // 根据名字对应到相应的系列
	               name: '用户收益总金额',
	               data: result.accountAmount
	           },
	           {
	               // 根据名字对应到相应的系列
	               name: '团队收益总金额',
	               data: result.teamAmount
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
	                  <span class="caption-subject bold uppercase"> 计划/任务日统计报表</span>
	              </div>
	          </div>
	          <div class="portlet-body">
	              <div id="taskChart" style="height:400px;">
	                  
	              </div>
	          </div>
	      </div>
		</div>
	</div>
	
	<script>
	$(function(){
		var myChart = echarts.init(document.getElementById('taskChart'));
		// 指定图表的配置项和数据
	    myChart.setOption({
		    tooltip: {},
		    legend: {
		        data:['商家发布计划数','试客接手任务数']
		    },
		    xAxis: {
		        data: []
		    },
		    yAxis: {},
		    series: [
		    {
		        name: '商家发布计划数',
		        type: 'line',
		        data: []
		    },
		    {
		        name: '试客接手任务数',
		        type: 'line',
		        data: []
		    }]
		});
	 	// 异步加载数据
	   $.post('${ctx}/main/ajaxChart/task/daily',{},function(result) {
	       myChart.setOption({
	           xAxis: {
	               data: result.chartLabel
	           },
	           yAxis: {
	        	   splitNumber : 5
	           },
	           series: [{
	               // 根据名字对应到相应的系列
	               name: '商家发布计划数',
	               data: result.taskCount
	           },
	           {
	               // 根据名字对应到相应的系列
	               name: '试客接手任务数',
	               data: result.taskItemCount
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
	                  <span class="caption-subject bold uppercase"> 计划/任务分时统计报表</span>
	              </div>
	          </div>
	          <div class="portlet-body">
	              <div id="taskTimeChart" style="height:400px;">
	                  
	              </div>
	          </div>
	      </div>
		</div>
	</div>
	
	<script>
	$(function(){
		var myChart = echarts.init(document.getElementById('taskTimeChart'));
		// 指定图表的配置项和数据
	    myChart.setOption({
		    tooltip: {},
		    legend: {
		        data:['商家昨日发布计划数','商家今日发布计划数','试客昨日接手任务数','试客今日接手任务数']
		    },
		    xAxis: {
		        data: []
		    },
		    yAxis: {},
		    series: [
		    {
		        name: '商家昨日发布计划数',
		        type: 'line',
		        data: []
		    },
		    {
		        name: '商家今日发布计划数',
		        type: 'line',
		        data: []
		    },
		    {
		        name: '试客昨日接手任务数',
		        type: 'line',
		        data: []
		    },
		    {
		        name: '试客今日接手任务数',
		        type: 'line',
		        data: []
		    }]
		});
	 	// 异步加载数据
	   $.post('${ctx}/main/ajaxChart/task/time',{},function(result) {
	       myChart.setOption({
	           xAxis: {
	               data: result.chartLabel,
	               axisLabel: {
	            	   interval: 0,
	            	   rotate: 60
	               }
	           },
	           yAxis: {
	        	   splitNumber : 5
	           },
	           series: [{
	               // 根据名字对应到相应的系列
	               name: '商家昨日发布计划数',
	               data: result.yesterdayTaskCount
	           },
	           {
	               // 根据名字对应到相应的系列
	               name: '商家今日发布计划数',
	               data: result.taskCount
	           },
	           {
	               // 根据名字对应到相应的系列
	               name: '试客昨日接手任务数',
	               data: result.yesterdayTaskItemCount
	           },
	           {
	               // 根据名字对应到相应的系列
	               name: '试客今日接手任务数',
	               data: result.taskItemCount
	           }]
	       });
	   });
	})
	</script>