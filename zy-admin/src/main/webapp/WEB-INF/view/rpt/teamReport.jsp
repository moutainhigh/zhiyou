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
        order: [], // set first column as a default sort by desc
        ajax: {
          url: '${ctx}/report/team', // ajax source
        },
        columns: [
          {
            data: 'nickname',
            title: '服务商',
            orderable: false
          },
          {
            data: 'rootRootName',
            title: '系统',
            orderable: false
          },
          {
            data: 'v4UserNickname',
            title: '直属特级',
            orderable: false
          },
          {
            data: 'v3Count',
            title: '一级',
            orderable: false
          },
          {
            data: 'v2Count',
            title: '二级',
            orderable: false
          },
          {
            data: 'v1Count',
            title: '三级',
            orderable: false
          },

          {
            data: 'v0Count',
            title: '意向服务商',
            orderable: false
          },
          {
            data: 'v123Count',
            title: '服务商总数量',
            orderable: false
          }
          ]
      }
    });

  });

  <shiro:hasPermission name="teamReport:export">
  function reportExport() {
    location.href = '${ctx}/report/team/export?' + $('#searchForm').serialize();
  }
  </shiro:hasPermission>
  
</script>
<!-- END JAVASCRIPTS -->

<!-- BEGIN PAGE HEADER-->
<div class="page-bar">
  <ul class="page-breadcrumb">
    <li><i class="fa fa-home"></i> <a href="javascript:;" data-href="${ctx}/main">首页</a> <i class="fa fa-angle-right"></i></li>
    <li><a href="javascript:;" data-href="${ctx}/report/team">特级服务商下线人数报表</a></li>
  </ul>
</div>
<!-- END PAGE HEADER-->

<div class="row">
  <div class="col-md-12">
    <!-- BEGIN ALERTS PORTLET-->
    <div class="portlet light bordered">
      <div class="portlet-title">
        <div class="caption">
          <i class="icon-bar-chart"></i><span>特级服务商下线人数报表</span>
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
                <select class="form-control pull-left" id="province" name="provinceIdEQ">
                  <option value="">-- 请选择省 --</option>
                </select>
                <select class="form-control pull-left" id="city" name="cityIdEQ">
                  <option value="">-- 请选择市 --</option>
                </select>
                <select class="form-control pull-left" id="district" name="districtEQ">
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
                <input type="text" name="nickNameLK" class="form-control" placeholder="服务商昵称"/>
              </div>
              <div class="form-group">
                <input type="text" name="phoneEQ" class="form-control" placeholder="服务商手机"/>
              </div>
              
              <div class="form-group input-inline">
                <button class="btn blue filter-submit">
                  <i class="fa fa-search"></i> 查询
                </button>
              </div>
              <shiro:hasPermission name="teamReport:export">
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
