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
        lengthMenu : [ [ 10, 20, 50, 100, -1 ], [ 10, 20, 50, 100, 'All' ] // change per page values here
        ],
        pageLength : 20, // default record count per page
        order : [], // set first column as a default sort by desc
        ajax : {
          url : '${ctx}/bank', // ajax source
        },
        columns : [ {
          data : 'name',
          title : '银行名称',
          width : '120px'
        }, {
          data : 'code',
          title : '代码',
          width : '120px'
        }, {
          data : 'isDeleted',
          title : '是否已删除',
          orderable : false,
          width : '100px',
          render : function(data, type, full) {
            return data ? '是' : '';
          }
         },
         {
           data: 'id',
           title: '操作',
           width: '120px',
           orderable: false,
           render: function (data, type, full) {
             var optionHtml = '';
             <shiro:hasPermission name="bank:edit">
             optionHtml += '<a class="btn btn-xs default yellow-stripe" href="javascript:;" data-href="${ctx}/bank/update?id=' + data + '"><i class="fa fa-edit"></i> 编辑 </a>';
             optionHtml += '<a class="btn btn-xs default red-stripe" href="javascript:;" data-href="${ctx}/bank/delete?id=' + data + '" data-confirm="您确定要删除选中数据吗?"><i class="fa fa-trash-o"></i> 删除 </a>';
             </shiro:hasPermission>
             return optionHtml;
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
    <li><a href="javascript:;" data-href="${ctx}/bank">银行管理</a></li>
  </ul>
</div>
<!-- END PAGE HEADER-->

<div class="row">
  <div class="col-md-12">
    <!-- BEGIN ALERTS PORTLET-->
    <div class="portlet light bordered">
      <div class="portlet-title">
        <div class="caption">
          <i class="icon-home"></i> 银行管理
        </div>
        <div class="actions">
          <a class="btn btn-circle green" data-href="${ctx}/bank/create">
            <i class="fa fa-plus"></i> 新增
          </a>
        </div>
      </div>
      <div class="portlet-body clearfix">
        <div class="table-container">
          <div class="table-toolbar">
            <form class="filter-form form-inline">
              <input id="_orderBy" name="orderBy" type="hidden" value="" /> <input id="_direction" name="direction" type="hidden" value="" /> <input id="_pageNumber"
                name="pageNumber" type="hidden" value="0" /> <input id="_pageSize" name="pageSize" type="hidden" value="20" />

              <div class="form-group">
                <input type="text" class="form-control" name="nameLK" placeholder="银行名称" />
              </div>

              <div class="form-group">
                <select name="isDeletedEQ" class="form-control">
                  <option value="">-- 是否已删除 --</option>
                  <option value="1">-- 是 --</option>
                  <option value="0">-- 否 --</option>
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
      <!-- END ALERTS PORTLET-->
    </div>
  </div>
</div>