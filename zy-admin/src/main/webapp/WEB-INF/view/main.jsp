<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/view/include/taglib.jsp"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!-- BEGIN JAVASCRIPTS -->
<%--<style type="text/css">--%>
	<%--a {--%>
		<%--cursor: point;--%>
	<%--}--%>
<%--</style>--%>
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
<shiro:hasPermission name="system:view">
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
	<div class="row">
		<div class="col-md-12">
			<div class="portlet light bordered">
				<div class="portlet-title">
					<div class="caption">
						<i class="icon-graph"></i>
						<span class="caption-subject bold uppercase"> 销量月度报表</span>
					</div>
				</div>
				<div class="portlet-body">
					<div id="orderChartXiao" style="height:350px;">

					</div>
				</div>
			</div>
		</div>
	</div>
	<script>
		$(function(){
			var myChartxiao = echarts.init(document.getElementById('orderChartXiao'));
			// 指定图表的配置项和数据
//			myChartxiao.setOption({
//				tooltip: {
//					trigger: 'axis',
//					axisPointer: {
//						type: 'cross',
//						crossStyle: {
//							color: '#999'
//						}
//					}
//				},
//				toolbox: {
//					feature: {
//						dataView: {show: true, readOnly: false},
//						magicType: {show: true, type: ['line', 'bar']},
//						restore: {show: true},
//						saveAsImage: {show: true}
//					}
//				},
//				legend: {
//					data:['用户名','达成量','目标量','达成率']
//				},
//				xAxis: [
//				],
//				yAxis: [
//				],
//				series: [
//
//				]
//			});
			// 异步加载数据
			$.post('${ctx}/main/ajaxChart/salesvolume',{},function(result) {
//				console.log(result.achievementList);
//                for(var j in result.achievementList){
//					result.achievementList[j]=result.achievementList[j]/100;
//				}
				console.log(result.achievementList);
				myChartxiao.setOption({
					tooltip: {
						trigger: 'axis',
						axisPointer: {
							type: 'cross',
							crossStyle: {
								color: '#999'
							}
						}
					},
					toolbox: {
						feature: {
							dataView: {show: true, readOnly: false},
							magicType: {show: true, type: ['line', 'bar']},
							restore: {show: true},
							saveAsImage: {show: true}
						}
					},
					legend: {
					    data:['用户名','达成量','目标量','达成率']
				    },
					xAxis: [
						{
							type: 'category',
							data: result.userNameList,
							axisPointer: {
								type: 'shadow'
							}
						}
					],
					yAxis: [
						{
							type: 'value',
							name: '销量',
							axisLabel: {
								formatter: '{value} '
							}
						},
						{
							type: 'value',
							name: '比率',
							axisLabel: {
								formatter: '{value}%'
							}
						}
					],
					color:['#cf3834','#31c7b2',"#589198"],
					series: [
						{
							name:'目标量',
							type:'bar',
							data:result.amountTargetList
						},
						{
							name:'达成量',
							type:'bar',
							data:result.amountReachedList
						},
						{
							name:'达成率',
							type:'line',
							yAxisIndex: 1,
							data:result.achievementList
						}
					]
				});
			});
		})
	</script>
</shiro:hasPermission>
	<shiro:hasPermission name="teamReportNew:view">
	<div class="row">
		<div class="col-md-12">
			<div class="portlet light bordered">
				<div class="portlet-title">
					<div class="caption">
						<i class="icon-graph"></i>
						<span class="caption-subject bold uppercase"> 大区总裁团队上月度报表</span>
					</div>
				</div>
				<div class="portlet-body">
					<div id="orderChartTeam" style="height:350px;">

					</div>
				</div>
			</div>
		</div>
	</div>
	</shiro:hasPermission>
<shiro:hasPermission name="teamProvinceReport:view">
	<script>
		$(function(){
			var myChartTeam = echarts.init(document.getElementById('orderChartTeam'));
			// 指定图表的配置项和数据
			myChartTeam.setOption({
				tooltip : {
					trigger: 'item',
				},
				legend: {
					data:['新晋特级','特级']
				},
				toolbox: {
					show : true,
					feature : {
						dataView : {show: true, readOnly: false},
						magicType : {show: true, type: ['line', 'bar']},
						restore : {show: true},
						saveAsImage : {show: true}
					}
				},
				xAxis: [
				],
				yAxis: [
				],
				series: [

				]
			});
// 异步加载数据
			$.post('${ctx}/report/teamReportNew/ajaxTeamReportNew',{},function(result) {
				myChartTeam.setOption({
					xAxis: [
						{
							type: 'category',
							data: result.data.namearry,
							axisPointer: {
								type: 'shadow'
							}
						}
					],
					yAxis: [
						{
							type: 'value',
							name: '人数',
							axisLabel: {
								formatter: '{value} '
							}
						}
					],
					series: [
						{
							name:'新晋特级',
							type:'bar',
							data: result.data.newextraNumberarry,
						},
						{
							name:'特级',
							type:'bar',
							data: result.data.extraNumberarry,
						}
					]
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
						<span class="caption-subject bold uppercase"> 省份服务商活跃报表(${date})</span>
					</div>
				</div>
				<div class="portlet-body">
					<div id="orderChartMap" style="height:800px;">

					</div>
				</div>
			</div>
		</div>
	</div>
	<script>
		var V4Active='${v4Active}';
		var v4AvticeRate='${v4AvticeRate}';
		var V3='${v3}';
		var V4='${v4}';
		var name='${name}';
		var arrayV4Active=V4Active.split(",");
		var arrayv4AvticeRate=v4AvticeRate.split(",");
		var arrayV3=V3.split(",");
		var arrayV4=V4.split(",");
		var arrayname=name.split(",");
		var str="";
		function randomData() {
			return Math.round(Math.random()*1000);
		}
		function makeMapData() {
			var mapData = [];

			for(var j=0;j<arrayname.length;j++){

				if(arrayV3[j]!=0||arrayV4[j]!=0){
					if(arrayname[j].length<4){
						var arrayname2=arrayname[j].substring(0,2);
					}else {
						var arrayname2=arrayname[j].substring(0,2);
						if(arrayname[j]=="内蒙古自治区"||arrayname[j]=="黑龙江省"){
							var arrayname2=arrayname[j].substring(0,3);
						}
					}
					mapData.push({
						name: arrayname2,
//						selected:true,
						value: randomData()
					});
				}
			}
			return mapData;
		}
		$.get('${ctx}/map/json/china.json', function (chinaJson) {
			echarts.registerMap('china', chinaJson);
			var chart = echarts.init(document.getElementById('orderChartMap'));
			chart.setOption({
				tooltip : {
					trigger: 'item',
					formatter: function(param){
						if(arrayname.length==1){
							str = param.name + "：<br>特级人数：" + 0 + "<br>" + "省级人数：" + 0 + "<br>特级活跃人数：" + 0 + "<br>" + "特级活跃度：" + 0;
						}else {
							for (var i = 0; i < arrayname.length; i++) {
								var name = arrayname[i].substring(0, param.name.length);
								if (param.name == name) {
									str = param.name + "：<br>特级人数：" + arrayV4[i] + "<br>" + "省级人数：" + arrayV3[i] + "<br>特级活跃人数：" + arrayV4Active[i] + "<br>" + "特级活跃度：" + arrayv4AvticeRate[i];
								}
							}
						}
						return str;
					}
				},
				visualMap: {
					min: 0,
					max: 2500,
					left: 'left',
					top: 'bottom',
					text: ['高','低'],           // 文本，默认为数值文本
					calculable: true
				},
				series: [
					{
						name: '中国',
						type: 'map',
						mapType: 'china',
						selectedMode : 'multiple',
						label: {
							normal: {
								show: true
							},
							emphasis: {
								show: true
							}
						},
						itemStyle: {
							normal: {
								borderColor: '#389BB7',
								areaColor: '#fff',
							},
							emphasis: {
								areaColor: '#389BB7',
								borderWidth: 0
							}
						},
						data:makeMapData()
					}
				]
			});
			chart.on('click',  function (param) {
//				console.log(param);
//				console.log(param.tooltip);
			});
		});

	</script>
</shiro:hasPermission>