<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/view/include/head.jsp" %>

<!-- BEGIN JAVASCRIPTS -->
<script>
  var grid = new Datatable();

  var area = new areaInit('province', 'city', 'district');

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
        order: [], // set first column as a default sort by desc
        ajax: {
          url: '${ctx}/report/activitySummary', // ajax source
        },
        columns: [
//          {
//            data: 'id',
//            title: '序号',
//            orderable: false
//          },
          {
            data: 'title',
            title: '标题',
            orderable: false
          },
          {
            data: 'startTime',
            title: '开始时间',
            orderable: false,
            render: function (data, type, full) {
              return full.startTimeLabel;
            }
          },
          {
            data: 'endTime',
            title: '结束时间',
            orderable: false,
            render: function (data, type, full) {
              return full.endTimeLabel;
            }
          },
          {
            data: 'areaId',
            title: '活动地点',
            orderable: false,
            render: function (data, type, full) {
              return '<p>' + full.province + '-' + full.city + '-' + full.district + '</p>'
                + '<p class="small">' + full.address + '</p>';
            }
          },
          {
            data: 'viewedCount',
            title: '浏览人数'
          },
          {
            data: 'collectedCount',
            title: '关注人数'
          },
          {
            data: 'nonPayment',
            title: '未支付人数',
            orderable: false
          },
          {
            data: 'payment',
            title: '已购票数',
            orderable: false
          },
          {
            data: 'appliedCount',
            title: '已报名人数',
            width: '80px'
          },
          {
            data: 'signedInCount',
            title: '已签到人数'
          },
          {
            data: '',
            title: '签到率(%)',
            orderable: false,
            render: function (data, type, full) {
              if (full.appliedCount != null && full.signedInCount != null && full.appliedCount != 0){
                return parseFloat(full.signedInCount / full.appliedCount*100).toFixed(2);
              }else {
                return '0.00';
              }
            }
          }]
      }
    });

  });

  <shiro:hasPermission name="activitySummaryReport:export">
  function reportExport() {
    location.href = '${ctx}/report/activitySummary/export?' + $('#searchForm').serialize();
  }
  </shiro:hasPermission>

</script>
<!-- END JAVASCRIPTS -->

<!-- BEGIN PAGE HEADER-->
<div class="page-bar">
  <ul class="page-breadcrumb">
    <li><i class="fa fa-home"></i> <a href="javascript:;" data-href="${ctx}/main">首页</a> <i class="fa fa-angle-right"></i></li>
    <li><a href="javascript:;" data-href="${ctx}/activitySummary">活动汇总报表</a></li>
  </ul>
</div>
<!-- END PAGE HEADER-->

<div class="row">
  <div class="col-md-12">
    <!-- BEGIN ALERTS PORTLET-->
    <div class="portlet light bordered">
      <div class="portlet-title">
        <div class="caption">
          <i class="icon-bar-chart"></i> 活动汇总报表
        </div>
      </div>
      <div class="portlet-body clearfix">
        <div class="table-container">
          <div class="table-toolbar">
            <form class="filter-form form-inline" id="searchForm">
              <input id="_orderBy" name="orderBy" type="hidden" value=""/>
              <input id="_direction" name="direction" type="hidden" value=""/>
              <input id="_pageNumber" name="pageNumber" type="hidden" value="0"/>
              <input id="_pageSize" name="pageSize" type="hidden" value="20"/>
              <div class="form-group">
                <input type="text" name="titleLK" class="form-control" placeholder="标题"/>
              </div>

              <div class="form-group">
                <select class="form-control pull-left" id="province" name="provinceIdEQ">
                  <option value="">-- 请选择省 --</option>
                </select>
                <select class="form-control pull-left" id="city" name="cityIdEQ">
                  <option value="">-- 请选择市 --</option>
                </select>
                <select class="form-control pull-left" id="district" name="areaIdEQ">
                  <option value="">-- 请选择区 --</option>
                </select>
              </div>

              <div class="form-group input-inline">
                <input class="Wdate form-control" type="text"
                       onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',readOnly:true})" name="startTimeGTE" value="" placeholder="开始时间起"/>
              </div>
              <div class="form-group input-inline">
                <input class="Wdate form-control" type="text" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',readOnly:true})"
                       name="startTimeLT" value="" placeholder="结束时间止"/>
              </div>

              <div class="form-group">
                <button class="btn blue filter-submit">
                  <i class="fa fa-search"></i> 查询
                </button>
              </div>

              <shiro:hasPermission name="activitySummaryReport:export">
                <div class="form-group">
                  <button type="button" class="btn yellow" onClick="reportExport()">
                    <i class="fa fa-file-excel-o"></i> 导出Excel
                  </button>
                </div>
              </shiro:hasPermission>
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