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
          url: '${ctx}/policyCode', // ajax source
        },
        columns: [
          {
            data: 'batchCode',
            title: '批次号',
            orderable: false
          },
          {
            data: 'code',
            title: '保险单号',
            orderable: false
          },
          {
            data: 'createdTimeLabel',
            title: '创建时间',
            orderable: false
          },
          {
            data: 'isUsed',
            title: '是否使用',
            orderable: false,
            render: function (data, type, full) {
              return data?'已使用':'';
            }
          },
          {
            data: 'usedTimeLabel',
            title: '使用时间',
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
    <li><a href="javascript:;" data-href="${ctx}/policyCode">保险单号</a></li>
  </ul>
</div>
<!-- END PAGE HEADER-->

<div class="row">
  <div class="col-md-12">
    <!-- BEGIN ALERTS PORTLET-->
    <div class="portlet light bordered">
      <div class="portlet-title">
        <div class="caption">
          <i class="icon-umbrella"></i> 保险单管理
        </div>
        <shiro:hasPermission name="policyCode:edit">
          <div class="actions">
            <a class="btn btn-circle green" data-href="${ctx}/policyCode/create">
              <i class="fa fa-plus"></i> 新增
            </a>
          </div>
        </shiro:hasPermission>
      </div>
      <div class="portlet-body clearfix">
        <div class="table-container">
          <div class="table-toolbar">
            <form id="searchForm" class="filter-form form-inline">
              <input id="_orderBy" name="orderBy" type="hidden" value=""/> <input id="_direction" name="direction" type="hidden" value=""/>
              <input id="_pageNumber" name="pageNumber" type="hidden" value="0"/> <input id="_pageSize" name="pageSize" type="hidden" value="20"/>

              <div class="form-group">
                <input type="text" name="batchCodeEQ" class="form-control" placeholder="批次号"/>
              </div>

              <div class="form-group">
                <input type="text" name="codeEQ" class="form-control" placeholder="保险单号"/>
              </div>

              <div class="form-group">
                <select name="isUsedEQ" class="form-control">
                  <option value="">-- 是否已使用 --</option>
                  <option value="true">是</option>
                  <option value="false">否</option>
                </select>
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