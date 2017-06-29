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
          url: '${ctx}/report/profitMOM?monthLabel=${monthLabel}', // ajax source
        },
        columns: [
          {
            data: 'bossNickname',
            title: '总经理昵称',
            orderable: false
          },
          {
            data: 'bossName',
            title: '总经理团队',
            orderable: false
          }
          <c:forEach items='${monthForHeaders}' var='month' varStatus='varStatus'>
          ,{
            data: '',
            title: '${month} 个人收入',
            orderable: false,
            render: function (data, type, full) {
              if (full.profitMOMReportVoItems.length > 0) {
                if ('${month}' == full.profitMOMReportVoItems[0].dateLabel) {
                  return full.profitMOMReportVoItems[0].profit;
                }
                return full.profitMOMReportVoItems[1].profit
              } else {
                return '0.00';
              }
            }
          }
          </c:forEach>
          <c:forEach items='${monthForHeaders}' var='month' varStatus='varStatus'>
          ,{
            data: '',
            title: '${month} 团队收入',
            orderable: false,
            render: function (data, type, full) {
              if (full.profitMOMReportVoItems.length > 0) {
                if ('${month}' == full.profitMOMReportVoItems[0].dateLabel) {
                  return full.profitMOMReportVoItems[0].teamProfit;
                }
                return full.profitMOMReportVoItems[1].teamProfit
              } else {
                return '0.00';
              }
            }
          }
          </c:forEach>
          <c:forEach items='${monthForHeaders}' var='month' varStatus='varStatus'>
          ,{
            data: '',
            title: '${month} 人均收入',
            orderable: false,
            render: function (data, type, full) {
              if (full.profitMOMReportVoItems.length > 0) {
                if ('${month}' == full.profitMOMReportVoItems[0].dateLabel) {
                  return full.profitMOMReportVoItems[0].avgProfit;
                }
                return full.profitMOMReportVoItems[1].avgProfit
              } else {
                return '0.00';
              }
            }
          }
          </c:forEach>
          <c:forEach items='${monthForHeaders}' var='month' varStatus='varStatus'>
          ,{
            data: '',
            title: '${month} 个人收入团队占比',
            orderable: false,
            render: function (data, type, full) {
              if (full.profitMOMReportVoItems.length > 0) {
                if ('${month}' == full.profitMOMReportVoItems[0].dateLabel) {
                  return full.profitMOMReportVoItems[0].inTeamProfitRate;
                }
                return full.profitMOMReportVoItems[1].inTeamProfitRate
              } else {
                return '0.00';
              }
            }
          }
          </c:forEach>
          ,{
            data: 'profitMOMRateLabel',
            title: '收入环比',
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
    <li><a href="javascript:;" data-href="${ctx}/report/profitMOM">总经理个人收入环比</a></li>
  </ul>
</div>
<!-- END PAGE HEADER-->

<div class="row">
  <div class="col-md-12">
    <!-- BEGIN ALERTS PORTLET-->
    <div class="portlet light bordered">
      <div class="portlet-title">
        <div class="caption">
          <i class="icon-bar-chart"></i><span>总经理个人收入环比</span>
        </div>
        <div class="actions">
          <div class="btn-group">
            <a href="" class="btn dark btn-outline btn-circle btn-sm dropdown-toggle" data-toggle="dropdown" data-hover="dropdown" data-close-others="true" aria-expanded="false">查询月份
              <span class="fa fa-angle-down"> </span>
            </a>
            <ul class="dropdown-menu pull-right">
              <c:forEach items="${queryDateLabels}" var="queryDateLabel">
                <li>
                  <a data-href="${ctx}/report/profitMOM?monthLabel=${queryDateLabel}"> ${queryDateLabel}</a>
                </li>
              </c:forEach>
            </ul>
          </div>
        </div>
      </div>

      <div class="portlet-body clearfix">
        <div class="table-container">
          <div class="table-toolbar">
            <form class="filter-form form-inline" id="searchForm">
              <input id="_orderBy" name="orderBy" type="hidden" value=""/>
              <input id="_direction" name="direction" type="hidden" value=""/>
              <input id="_pageNumber" name="pageNumber" type="hidden" value="0"/>
              <input id="_pageSize" name="pageSize" type="hidden" value="100"/>

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
