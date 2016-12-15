<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/view/include/head.jsp" %>
<script>

  $(function () {

    var grid = new Datatable();

    grid.init({
      src: $('#dataTable'),
      onSuccess: function (grid) {
        // execute some code after table records loaded
      },
      onError: function (grid) {
        // execute some code on network or other general error
      },
      dataTable: {
        //"sDom" : "<'row'<'col-md-8 col-sm-12'pli><'col-md-4 col-sm-12'<'table-group-actions pull-right'>>r>t<'row'<'col-md-8 col-sm-12'pli><'col-md-4 col-sm-12'>r>>",
        lengthMenu: [[10, 20, 50, 100], [10, 20, 50, 100]],// change per page values here
        pageLength: 20, // default record count per page
        order: [], // set first column as a default sort by desc
        ajax: {
          url: '${ctx}/reportVisitedLog', // ajax source
        },
        columns: [
          {
            data: 'relationship',
            title: '与检测人关系',
	          orderable: false
          },
	        {
		        data: '',
		        title: '一访',
		        orderable: false,
            render : function(data, type, full) {
		          return '<p>客服: ' + full.customerServiceName1 + '</p><p>日期: ' + full.visitedTime1Label + '</p><p>状态: ' + full.visitedStatus1 + '</p>';
            }
	        },
	        {
		        data: '',
		        title: '二访',
		        orderable: false,
		        render : function(data, type, full) {
			        return '<p>客服: ' + full.customerServiceName2 + '</p><p>日期: ' + full.visitedTime2Label + '</p><p>状态: ' + full.visitedStatus2 + '</p>';
		        }
	        },
	        {
		        data: '',
		        title: '三访',
		        orderable: false,
		        render : function(data, type, full) {
			        return '<p>客服: ' + full.customerServiceName3 + '</p><p>日期: ' + full.visitedTime3Label + '</p><p>状态: ' + full.visitedStatus3 + '</p>';
		        }
	        },
	        {
		        data: '',
		        title: '结果',
		        orderable: false,
		        render : function(data, type, full) {
			        return '<p>最终回访结果: ' + full.visitedStatus + '</p><p>最终审核状态: ' + full.confirmStatus + '</p>';
		        }
	        },
	        {
		        data: '',
		        title: '生活习惯',
		        orderable: false,
		        render : function(data, type, full) {
			        return '<p>作息时间: ' + full.restTimeLabel + '</p><p>睡眠质量: ' + full.sleepQuality + '</p><p>饮酒: ' + full.drink + '</p><p>抽烟: ' + full.smoke + '</p>' +
                '<p>锻炼身体: ' + full.exercise + '</p><p>兴趣爱好: ' + full.hobby + '</p>';
		        }
	        },
	        {
		        data: '',
		        title: '健康状况',
		        orderable: false,
		        render : function(data, type, full) {
			        return '<p>检测原因: ' + full.cause + '</p><p>健康状况: ' + full.health + '</p><p>当前病状: ' + full.sickness + '</p><p>家族遗传史: ' + full.familyHistory + '</p>';
		        }
	        },
	        {
		        data: '',
		        title: '保健品',
		        orderable: false,
		        render : function(data, type, full) {
			        return '<p>服用保健品: ' + full.healthProduct + '</p><p>月均消费: ' + full.monthlyCost + '</p><p>产品名称: ' + full.productName + '</p>';
		        }
	        },
	        {
		        data: '',
		        title: '其他',
		        orderable: false,
		        render : function(data, type, full) {
			        return '<p>干扰因素: ' + full.interferingFactors + '</p><p>产品分享: ' + full.productSharing + '</p><p>沟通方式: ' + full.contactWay + '</p>';
		        }
	        },
          {
          	data: 'visitedInfo',
            title: '回访记录',
	          orderable: false
          },
	        {
		        data: 'id',
		        title: '操作',
		        orderable: false,
		        render: function (data, type, full) {
			        var optionHtml = '';
			        <shiro:hasPermission name="report:edit">
              if(full.confirmStatus == '待审核') {
	              optionHtml += '<a class="btn btn-xs yellow blue-stripe" href="javascript:;" data-href="${ctx}/reportVisitedLog/update?id=' + full.id + '"><i class="fa fa-edit"></i> 再次回访</a>';
              }
			        </shiro:hasPermission>
			        return optionHtml;
		        }
	        }
        ]
      }
    });


  });
</script>
<!-- END JAVASCRIPTS -->

<!-- BEGIN PAGE HEADER-->
<div class="page-bar">
  <ul class="page-breadcrumb">
    <li><i class="fa fa-home"></i> <a href="javascript:;" data-href="${ctx}/main">首页</a> <i class="fa fa-angle-right"></i></li>
    <li><a href="javascript:;" data-href="${ctx}/reportVisitedLog">回访记录</a></li>
  </ul>
</div>
<!-- END PAGE HEADER-->

<div class="row">
  <div class="col-md-12">
    <!-- BEGIN ALERTS PORTLET-->
    <div class="portlet light bordered">
      <div class="portlet-title">
        <div class="caption">
          <i class="icon-umbrella"></i> 回访记录
        </div>
      </div>
      <div class="portlet-body clearfix">
        <div class="table-container">
          <div class="table-toolbar">
            <form id="searchForm" class="filter-form form-inline">
              <input type="hidden" name="reportId" value="${reportId}"/>
              <input id="_orderBy" name="orderBy" type="hidden" value=""/> <input id="_direction" name="direction" type="hidden" value=""/>
              <input id="_pageNumber" name="pageNumber" type="hidden" value="0"/> <input id="_pageSize" name="pageSize" type="hidden" value="20"/>

              <div class="form-group">
                <input type="text" name="customerServiceName1LK" class="form-control" placeholder="一访客服名"/>
              </div>

              <div class="form-group">
                <input type="text" name="customerServiceName2LK" class="form-control" placeholder="二访客服名"/>
              </div>

              <div class="form-group">
                <input type="text" name="customerServiceName3LK" class="form-control" placeholder="三访客服名"/>
              </div>

              <div class="form-group">
                <button class="btn blue filter-submit">
                  <i class="fa fa-search"></i> 查询
                </button>
              </div>

            </form>
          </div>

          <table class="table table-striped table-bordered table-hover" id="dataTable">
          </table>
        </div>

      </div>
    </div>
    <!-- END ALERTS PORTLET-->
  </div>
</div>