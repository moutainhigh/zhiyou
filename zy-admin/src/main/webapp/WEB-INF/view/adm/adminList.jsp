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
          lengthMenu: [
                         [10, 20, 50, 100, -1],
                         [10, 20, 50, 100, 'All'] // change per page values here
                         ],
          pageLength: 20, // default record count per page
          order: [
                    //[1, 'desc']
          ], // set first column as a default sort by desc
          ajax: {
            url: '${ctx}/admin', // ajax source
          },
          columns : [
              {
                data : 'id',
                className: 'table-checkbox',
                title: '<input type="checkbox" class="group-checkable" data-set="#dataTable .checkboxes" />',
                width: '15%',
                orderable : false,
                render : function(data, type, full) {
                  return '<input type="checkbox" class="checkboxes" value="' + data + '"/>';
                }
              },
              {
                data : 'avatar',
                title: '头像',
                width: '20%',
                render : function(data, type, full) {
                  return '<img src="' + data + '" width="80" height="80" />';
                }
              },
              {
                data : 'phone',
                title: '账号',
                width: '20%'
              },
              {
                 data : 'nickname',
                 title: '花名',
                 width: '20%'
               },
              {
                data : 'roleNames',
                title: '角色名称',
                width: '50%'
              },
              {
                data : '',
                title: '操作',
                width: '15%',
                orderable : false,
                render : function(data, type, full) {
                  return '<a class="btn btn-xs default yellow-stripe" href="javascript:;" data-href="${ctx}/admin/update/' + full.id + '"><i class="fa fa-edit"></i> 编辑 </a>'
                    + '<a class="btn btn-xs default red-stripe" href="javascript:;" data-href="${ctx}/admin/delete/' + full.id + '" data-confirm="您确定要删除选中数据吗?"><i class="fa fa-trash-o"></i> 删除 </a>';
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
    <li><a href="javascript:;" data-href="${ctx}/admin">管理员管理</a></li>
  </ul>
</div>
<!-- END PAGE HEADER-->

<div class="row">
  <div class="col-md-12">
    <!-- BEGIN ALERTS PORTLET-->
    <div class="portlet box blue">
      <div class="portlet-title">
        <div class="caption">
          <i class="fa fa-user"></i><span> 管理员管理 </span>
        </div>
        <div class="tools">
          <a class="collapse" href="javascript:;"> </a> <a class="reload" href="javascript:;"> </a>
        </div>
      </div>
      <div class="portlet-body clearfix">
        <div class="table-container">
          <div class="table-toolbar">
            <div class="btn-group">
              <button id="" class="btn green" data-href="${ctx}/admin/create">
                新增 <i class="fa fa-plus"></i>
              </button>
            </div>
            <!-- <div class="btn-group pull-right">
              <button class="btn dropdown-toggle" data-toggle="dropdown">
                工具 <i class="fa fa-angle-down"></i>
              </button>
              <ul class="dropdown-menu pull-right">
                <li><a href="#"> 打印 </a></li>
                <li><a href="#"> 导出Excel </a></li>
              </ul>
            </div> -->
          </div>

          <div class="row">
            <div class="col-md-3 table-actions">
              <span class="table-row-checked"></span>
            </div>
            <div class="col-md-9">
              <form class="filter-form form-inline pull-right">
                <input id="_orderBy" name="orderBy" type="hidden" value=""/>
                <input id="_direction" name="direction" type="hidden" value=""/>
                <input id="_pageNumber" name="pageNumber" type="hidden" value="0"/>
                <input id="_pageSize" name="pageSize" type="hidden" value="20"/>
                <div class="form-group input-inline">
                  <label class="sr-only">用户名</label>
                  <input type="text" name="q.username" maxlength="50" class="form-control" placeholder="用户名" />
                </div>
                <button class="btn purple filter-submit">
                  <i class="fa fa-check"></i> 查询
                </button>
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