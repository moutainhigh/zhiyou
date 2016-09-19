<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/view/include/head.jsp" %>

<!-- BEGIN JAVASCRIPTS -->
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
        lengthMenu: [[10, 20, 50, 100, -1], [10, 20, 50, 100, 'All'] // change per page values here
        ],
        pageLength: 20, // default record count per page
        order: [], // set first column as a default sort by desc
        ajax: {
          url: '${ctx}/admin', // ajax source
        },
        columns: [
          {
            data: '',
            title: '用户信息',
            render: function (data, type, full) {
              return formatUser(data.user);
            }
          },
          {
            data: 'roleNames',
            title: '角色名称'
          },
          {
            data: '',
            title: '操作',
            orderable: false,
            render: function (data, type, full) {
              return '<a class="btn btn-xs default yellow-stripe" href="javascript:;" data-href="${ctx}/admin/update?id=' + full.id + '"><i class="fa fa-edit"></i> 编辑 </a>'
                + '<a class="btn btn-xs default red-stripe" href="javascript:;" data-href="${ctx}/admin/delete?id=' + full.id
                + '" data-confirm="您确定要删除选中数据吗?"><i class="fa fa-trash-o"></i> 删除 </a>';
            }
          }]
      }
    });

  });
</script>
<!-- END JAVASCRIPTS -->

<!-- BEGIN PAGE HEADER-->
<div class="page-bar">
  <ul class="page-breadcrumb">
    <li><i class="fa fa-home"></i> <a href="javascript:;" data-href="${ctx}/main">首页</a> <i class="fa fa-angle-right"></i></li>
    <li><a href="javascript:;" data-href="${ctx}/admin">管理员管理</a></li>
  </ul>
</div>
<!-- END PAGE HEADER-->

<div class="row">
  <div class="col-md-12">
    <!-- BEGIN ALERTS PORTLET-->
    <div class="portlet light bordered">
      <div class="portlet-title">
        <div class="caption">
          <i class="icon-user"></i><span> 管理员管理 </span>
        </div>
        <div class="actions">
          <a class="btn btn-circle green" data-href="${ctx}/admin/create"> <i class="fa fa-plus"></i> 新增
          </a>
        </div>
      </div>
      <div class="portlet-body clearfix">
        <div class="table-container">
          <div class="table-toolbar">
            <form class="filter-form form-inline">
              <input id="_orderBy" name="orderBy" type="hidden" value=""/>
              <input id="_direction" name="direction" type="hidden" value=""/>
              <input id="_pageNumber" name="pageNumber" type="hidden" value="0"/>
              <input id="_pageSize" name="pageSize" type="hidden" value="20"/>

              <div class="form-group">
                <input type="text" name="phoneEQ" class="form-control" placeholder="手机"/>
              </div>
              <div class="form-group">
                <input type="text" name="nicknameLK" class="form-control" placeholder="昵称"/>
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
      <!-- END ALERTS PORTLET-->
    </div>
  </div>
</div>