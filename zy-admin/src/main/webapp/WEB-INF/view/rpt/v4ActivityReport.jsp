<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/view/include/head.jsp" %>
<!-- BEGIN JAVASCRIPTS -->
<script>
	
  var grid = new Datatable();

  $(function () {
    
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
        pageLength: 100,
        order: [], // set first column as a default sort by desc
        ajax: {
          url: '${ctx}/report/v4Activity', // ajax source
        },
        columns: [
          {
            data: 'bossName',
            title: '总经理团队',
            orderable: false
          },
          {
            data: 'v4Count',
            title: '团队特级总人数',
            orderable: false
          },
          {
            data: 'activityCount',
            title: '活跃人数',
            orderable: false
          },
          {
            data: 'inactiveCount',
            title: '不活跃人数',
            orderable: false
          },
          {
            data: 'recentCount',
            title: '新增特级',
            orderable: false
          },
          {
            data: 'activityRateLabel',
            title: '当前活跃度',
            orderable: false
          },
          {
            data: 'lastActivityRateLabel',
            title: '上期活跃度',
            orderable: false
          },
          {
            data: 'activityRateOrderNumber',
            title: '当前活跃度排名',
            orderable: false
          },
          {
            data: 'lastActivityRateOrderNumber',
            title: '上期活跃度排名',
            orderable: false
          },
          {
            data: 'growth',
            title: '增长情况',
            orderable: false,
            render: function(data, type, full) {
              if (data == '上升') {
                return '<span style="color: red;">↑</span>'
              } else if (data == '下降') {
                return '<span style="color: green">↓</span>'
              } else {
                return '—'
              }
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
    <li><a href="javascript:;" data-href="${ctx}/report/v4Activity">特级服务商团队活跃度报表-总经理</a></li>
  </ul>
</div>
<!-- END PAGE HEADER-->

<div class="row">
  <div class="col-md-12">
    <!-- BEGIN ALERTS PORTLET-->
    <div class="portlet light bordered">
      <div class="portlet-title">
        <div class="caption">
          <i class="icon-bar-chart"></i><span>特级服务商团队活跃度报表-总经理</span>
        </div>
      </div>

      <div class="portlet-body clearfix">
        <div class="table-container">
          <div class="table-toolbar">
            <form class="filter-form form-inline" id="searchForm">
              <input id="_orderBy" name="orderBy" type="hidden" value=""/>
              <input id="_direction" name="direction" type="hidden" value=""/>
              <input id="_pageNumber" name="pageNumber" type="hidden" value="0"/>
              <input id="_pageSize" name="pageSize" type="hidden" value="50"/>

            </form>
          </div>
          <table class="table table-striped table-bordered table-hover" id="dataTable">
          </table>
        </div>
      </div>
      <!-- END ALERTS PORTLET-->
    </div>
  </div>
</div>
