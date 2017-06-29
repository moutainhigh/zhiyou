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
        pageLength: 50,
        order: [], // set first column as a default sort by desc
        ajax: {
          url: '${ctx}/report/cityAgent', // ajax source
        },
        columns: [
          {
            data: 'id',
            title: '序号',
            orderable: false
          },
          {
            data: 'city',
            title: '省份',
            orderable: false
          },
          {
            data: 'v4Count',
            title: '特级服务商人数',
            orderable: false
          },
          {
            data: 'v3Count',
            title: '省级服务商人数',
            orderable: false
          },
          {
            data: 'activeV4Count',
            title: '活跃特级服务商人数',
            orderable: false
          },
          {
            data: 'activeCityOrderNumber',
            title: '当前省份特级活跃度',
            orderable: false
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
    <li><a href="javascript:;" data-href="${ctx}/report/cityAgent">服务商活跃度排行—省份</a></li>
  </ul>
</div>
<!-- END PAGE HEADER-->

<div class="row">
  <div class="col-md-12">
    <!-- BEGIN ALERTS PORTLET-->
    <div class="portlet light bordered">
      <div class="portlet-title">
        <div class="caption">
          <i class="icon-bar-chart"></i><span>服务商活跃度排行—省份</span>
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
