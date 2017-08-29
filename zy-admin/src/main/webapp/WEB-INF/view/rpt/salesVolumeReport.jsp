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
        lengthMenu: [
                         [10, 20, 50, 100, -1],
                         [10, 20, 50, 100, '全部'] // change per page values here
                         ],

        order: [], // set first column as a default sort by desc
        ajax: {
          url: '${ctx}/report/salesVolumeReport?queryDate=${queryDate}', // ajax source
        },
        columns: [
          {
            data: 'userName',
            title: '姓名',
            orderable: false
          },
          {
            data: 'userPhone',
            title: '手机号',
            orderable: false
          },
          {
            data: 'areaType',
            title: '所属大区',
            orderable: false
          },
          {
            data: 'isBoss',
            title: '是否大区总裁',
            orderable: false,
            render: function (data, type, full) {
              if (data == 0){
                return '否' ;
              }else if (data == 1){
                return '是';
              }
            }
          },
          {
            data: 'amountReached',
            title: '达成量',
            orderable: true
          },
          {
            data: 'amountTarget',
            title: '目标量',
            orderable: true
          },
          {
            data: 'achievement',
            title: '达成率(%)',
            orderable: true
          },
          {
            data: 'ranking',
            title: '排名',
            orderable: true
          },
          {
            data: 'rankDetail',
            title: '排名升降情况',
            orderable: false,
            render: function (data, type, full) {
              if (full.type != null && full.number != null){
                if (full.type == 1 ){
                  return '<span style="color: red;">↑</span>' + full.number;
                }else if (full.type == 2){
                  return '—';
                }else if (full.type == 3){
                  return '<span style="color: blue">↓</span>'  + full.number;
                }
              }else {
                return '';
              }
            }
          }
        ]
      }
    });

  });

  <shiro:hasPermission name="salesVolumeReport:export">
  function reportExport() {
    location.href = '${ctx}/report/salesVolumeReport/export?queryDate=${queryDate}&' + $('#searchForm').serialize();
  }
  </shiro:hasPermission>
  
</script>
<!-- END JAVASCRIPTS -->

<!-- BEGIN PAGE HEADER-->
<div class="page-bar">
  <ul class="page-breadcrumb">
    <li><i class="fa fa-home"></i> <a href="javascript:;" data-href="${ctx}/main">首页</a> <i class="fa fa-angle-right"></i></li>
    <li><a href="javascript:;" data-href="${ctx}/report/salesVolumeReport?queryDate=">销量报表</a></li>
  </ul>
</div>
<!-- END PAGE HEADER-->

<div class="row">
  <div class="col-md-12">
    <!-- BEGIN ALERTS PORTLET-->
    <div class="portlet light bordered">
      <div class="portlet-title">
        <div class="caption">
          <i class="icon-docs"></i><span>销量报表 (${queryDate}) </span>
        </div>
        <div class="tools"></div>
      </div>
      <div class="portlet-title">
        <div class="row">
          <div class="col-md-4">
            <div class="note note-info">
              <h4 class="block">达成销量</h4>
              <p id="countNumber">${countNumber} </p>
            </div>
          </div>
          <div class="col-md-4">
            <div class="note note-success">
              <h4 class="block">平均目标</h4>
              <p id="sumQuantity">${sumQuantity} </p>
            </div>
          </div>
          <div class="col-md-4">
            <div class="note note-danger">
              <h4 class="block">总目标</h4>
              <p id="sumAmount">${sumAmount} </p>
            </div>
          </div>
        </div>

        <div class="actions">
            <div class="btn-group">
                <a href="" class="btn dark btn-outline btn-circle btn-sm dropdown-toggle" data-toggle="dropdown" data-hover="dropdown" data-close-others="true" aria-expanded="false">查询月份
                    <span class="fa fa-angle-down"> </span>
                </a>
                <ul class="dropdown-menu pull-right">
                  <c:forEach items="${queryTimeLabels}" var="queryTimeLabel">
                    <li>
                        <a data-href="${ctx}/report/salesVolumeReport?queryDate=${queryTimeLabel}"> ${queryTimeLabel}
                        </a>
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
              <input id="_pageSize" name="pageSize" type="hidden" value="20"/>

              <div class="form-group">
                <select name="areaTypeEQ" class="form-control">
                  <option value="">-- 选择大区--</option>
                  <c:forEach items="${largeAreas}" var="code" >
                    <option value="${code.systemValue}"> ${code.systemName} </option>
                  </c:forEach>
                </select>
              </div>

              <div class="form-group">
                <select name="isBoss" class="form-control">
                  <option value="">-- 大区总裁--</option>
                    <option value="0"> 否 </option>
                    <option value="1"> 是 </option>
                </select>
              </div>

              <div class="form-group">
                <input type="text" name="userNameLK" class="form-control" placeholder="姓名"/>
              </div>
              <div class="form-group">
                <input type="text" name="userPhoneLK" class="form-control" placeholder="手机号"/>
              </div>

              <div class="form-group input-inline">
                <button class="btn blue filter-submit">
                  <i class="fa fa-search"></i> 查询
                </button>
              </div>
              <shiro:hasPermission name="salesVolumeReport:export">
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

