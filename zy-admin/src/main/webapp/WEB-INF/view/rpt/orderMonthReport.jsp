<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/view/include/head.jsp" %>
<!-- BEGIN JAVASCRIPTS -->
<script>
  var grid = new Datatable();

  $(function () {
    
    var area = new areaInit('province', 'city', 'district');
    
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
          url: '${ctx}/report/order/month?showAll=${showAll}', // ajax source
        },
        columns: [
          {
            data: 'nickname',
            title: '服务商',
            orderable: false
          },
          {
            data: 'rootName',
            title: '系统',
            orderable: false
          },
          {
            data: 'v4UserNickname',
            title: '直属上级',
            orderable: false
          }
          <c:forEach items='${timeLabels}' var='timeLabel' varStatus='varStatus'>
          ,{
            data: '',
            title: '${timeLabel}',
            orderable: false,
            render: function (data, type, full) {
              return full.orderReportVoItems[${varStatus.index}].quantity;
            }
          }
          </c:forEach>
          ]
      }
    });

  });

  <shiro:hasPermission name="orderReport:export">
  function reportExport() {
    location.href = '${ctx}/report/order/month/export?' + $('#searchForm').serialize();
  }
  </shiro:hasPermission>
</script>
<!-- END JAVASCRIPTS -->

<!-- BEGIN PAGE HEADER-->
<div class="page-bar">
  <ul class="page-breadcrumb">
    <li><i class="fa fa-home"></i> <a href="javascript:;" data-href="${ctx}/main">首页</a> <i class="fa fa-angle-right"></i></li>
    <li><a href="javascript:;" data-href="${ctx}/report/order">服务商进货(月)报表</a></li>
  </ul>
</div>
<!-- END PAGE HEADER-->

<div class="row">
  <div class="col-md-12">
    <!-- BEGIN ALERTS PORTLET-->
    <div class="portlet light bordered">
      <div class="portlet-title">
        <div class="caption">
          <i class="icon-bar-chart"></i><span>服务商进货(月)报表</span>
        </div>
        <div class="actions">
          <a class="btn btn-circle green" data-href="${ctx}/report/order/month?showAll=true">
            <i class="fa  fa-search"></i> 查看全部
          </a>
        </div>
      </div>
      <div class="portlet-body clearfix">
        <div class="table-container">
          <div class="table-toolbar">
            <form class="filter-form form-inline" id="searchForm">
              <input id="_orderBy" name="orderBy" type="hidden" value=""/>
              <input id="_direction" name="direction" type="hidden" value=""/>
              <input id="_pageNumber" name="pageNumber" type="hidden" value=""/>
              <input id="_pageSize" name="pageSize" type="hidden" value=""/>

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
              
              <div class="form-group">
                <select name="rootRootNameLK" class="form-control">
                  <option value="">-- 系统--</option>
                  <c:forEach items="${rootNames}" var="rootName">
                  <option value="${rootName}">${rootName}</option>
                  </c:forEach>
                </select>
              </div>
              
              <div class="form-group">
                <input type="text" name="v4UserNicknameLK" class="form-control" placeholder="直属特级"/>
              </div>
              
              <div class="form-group">
                <select name="userRankEQ" class="form-control">
                  <option value="">-- 等级 --</option>
                  <c:forEach items="${userRankMap}" var="userRankMap">
                  <c:if test="${userRankMap.key != 'V0'}">
                  <option value="${userRankMap.key}">${userRankMap.value}</option>
                  </c:if>
                  </c:forEach>
                </select>
              </div>
              
              <div class="form-group">
                <input type="text" name="nicknameLK" class="form-control" placeholder="服务商昵称"/>
              </div>
              <div class="form-group">
                <input type="text" name="phoneEQ" class="form-control" placeholder="服务商手机"/>
              </div>

              <div class="form-group input-inline">
                <button class="btn blue filter-submit">
                  <i class="fa fa-search"></i> 查询
                </button>
              </div>
              <shiro:hasPermission name="orderReport:export">
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
