<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/view/include/head.jsp"%>

<!-- BEGIN JAVASCRIPTS -->
<script>
  var grid = new Datatable();

  $(function() {
    grid.init({
      src : $('#dataTable'),
      onSuccess : function(grid) {
        // execute some code after table records loaded
      },
      onError : function(grid) {
        // execute some code on network or other general error  
      },
      dataTable : {
        //"sDom" : "<'row'<'col-md-8 col-sm-12'pli><'col-md-4 col-sm-12'<'table-group-actions pull-right'>>r>t<'row'<'col-md-8 col-sm-12'pli><'col-md-4 col-sm-12'>r>>", 
        lengthMenu : [ [ 10, 20, 50, 100 ], [ 10, 20, 50, 100 ] ],// change per page values here
        pageLength : 20, // default record count per page
        order : [
        //[1, 'desc']
        ], // set first column as a default sort by desc
        ajax : {
          url : '${ctx}/role', // ajax source
        },
        columns : [
            {
              data : 'id',
              className : 'table-checkbox',
              title : '<input type="checkbox" class="group-checkable" data-set="#dataTable .checkboxes" />',
              width : '60',
              orderable : false,
              render : function(data, type, full) {
                return '<input type="checkbox" class="checkboxes" value="' + data + '"/>';
              }
            },
            {
              data : 'name',
              title : '名称',
              width : '400'
            },
            {
              data : '',
              title : '操作',
              width : '200',
              orderable : false,
              render : function(data, type, full) {
                return '<a class="btn btn-xs default yellow-stripe" href="javascript:;" data-href="${ctx}/role/update/' + full.id + '"><i class="fa fa-edit"></i> 编辑 </a>'
                    + '<a class="btn btn-xs default red-stripe" href="javascript:;" data-href="${ctx}/role/delete/' + full.id
                    + '" data-confirm="您确定要删除选中数据吗?"><i class="fa fa-trash-o"></i> 删除 </a>';
              }
            } ]
      }
    });

  });
</script>
<!-- END JAVASCRIPTS -->

<!-- BEGIN PAGE HEADER-->
<div class="page-bar">
  <ul class="page-breadcrumb">
    <li><i class="fa fa-home"></i> <a href="javascript:;" data-href="${ctx}/main">首页</a> <i class="fa fa-angle-right"></i></li>
    <li><a href="javascript:;" data-href="${ctx}/role">角色管理</a></li>
  </ul>
</div>
<!-- END PAGE HEADER-->

<div class="row">
  <div class="col-md-12">
    <!-- BEGIN ALERTS PORTLET-->
    <div class="portlet light bordered">
      <div class="portlet-title">
        <div class="caption">
          <i class="icon-key"></i><span> 角色管理 </span>
        </div>
        <div class="actions">
          <a class="btn btn-circle green" data-href="${ctx}/role/create"> <i class="fa fa-plus"></i> 新增
          </a>
        </div>
      </div>
      <div class="portlet-body clearfix">
        <div class="table-container">
          <div class="table-toolbar">
            <form class="filter-form form-inline">
              <input id="_sort" name="q.sort" type="hidden" value="" /> <input id="_pageNumber" name="q.pageNumber" type="hidden" value="0" /> <input id="_pageSize"
                name="q.pageSize" type="hidden" value="20" />
              <!-- <div class="form-group input-inline">
                  <label class="sr-only">名称</label>
                  <input type="text" name="q.name" maxlength="50" class="form-control" placeholder="用户名" />
                </div>
                <button class="btn purple filter-submit">
                  <i class="fa fa-check"></i> 查询
                </button> -->
            </form>
          </div>
        </div>
        <table class="table table-striped table-bordered table-hover" id="dataTable">
        </table>
      </div>
    </div>
    <!-- END ALERTS PORTLET-->
  </div>
</div>
</div>