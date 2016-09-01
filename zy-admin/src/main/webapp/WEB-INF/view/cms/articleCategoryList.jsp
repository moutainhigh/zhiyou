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
                    [1, 'desc']
          ], // set first column as a default sort by desc
          ajax: {
            url: '${ctx}/articleCategory', // ajax source
          },
          columns : [
              {
                  data : 'name',
                  title: '标题',
                  orderable : false,
                  width: '35%'
              },
              {
                  data : 'parentId',
                  title: '父节点id',
                  orderable : false,
                  width: '35%'
              },
              {
                  data : 'indexNumber',
                  title: '顺序',
                  orderable : false,
                  width: '35%'
              },
              {
                  data : 'isVisiable',
                  title: '是否可见',
                  orderable : false,
                  width: '60px',
				  render : function(data, type, full) {
					  if(data) {
						  return '是';
					  }
					  return '否';
				  }
              },
              {
                data : 'id',
                title: '操作',
                width: '120px',
                orderable : false,
                render : function(data, type, full) {
                	var optionHtml = '';
                	<shiro:hasPermission name="article:edit">
                		optionHtml += '<a class="btn btn-xs default yellow-stripe" href="javascript:;" data-href="${ctx}/articleCategory/update?id=' + data + '"><i class="fa fa-edit"></i> 编辑 </a>';
                	</shiro:hasPermission>
                    return optionHtml;
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
    <li><a href="javascript:;" data-href="${ctx}/articleCategory">文章管理</a></li>
  </ul>
</div>
<!-- END PAGE HEADER-->

<div class="row">
  <div class="col-md-12">
    <!-- BEGIN ALERTS PORTLET-->
    <div class="portlet box blue">
      <div class="portlet-title">
        <div class="caption">
          <i class="fa fa-user"></i> 文章类别管理
        </div>
        <div class="tools">
          <a class="collapse" href="javascript:;"> </a> <a class="reload" href="javascript:;"> </a>
        </div>
      </div>
      <div class="portlet-body clearfix">
        <div class="table-container">
          <div class="table-toolbar">
          	<div class="btn-group">
               <button id="" class="btn green" data-href="${ctx}/articleCategory/create">
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
                
                  <label class="sr-only"> 是否可见： </label>
                  <select name="isVisiableEQ" class="form-control">
                  	<option value="">--请选择是否可见--</option>
                  	<option value="true">--是--</option>
                  	<option value="false">--否--</option>
                  </select>
                 
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