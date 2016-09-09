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
        "sDom" : "<'table-responsive't><'row'>",
        lengthMenu : [ [ 10, 20, 50, 100, -1 ], [ 10, 20, 50, 100, 'All' ] // change per page values here
        ],
        pageLength : 20, // default record count per page
        order : [

        ], // set first column as a default sort by desc
        ajax : {
          url : '${ctx}/helpCategory', // ajax source
        },
        columns : [ {
          data : 'code',
          title : 'code',
          width : '200px'
        }, {
          data : 'name',
          title : '名称',
          width : '150px'
        }, {
          data : 'userType',
          title : '针对用户分类',
          width : '150px'
        }, {
          data : 'indexNumber',
          title : '排序数字',
          width : '150px'
        }, {
          data : 'id',
          title : '操作',
          width : '25%',
          orderable : false,
          render : function(data, type, full) {
            var optionHtml = '<a class="btn btn-xs default yellow-stripe" href="javascript:;" data-href="${ctx}/help/' + data + '"><i class="fa fa-view"></i> 查看 </a>';
            <shiro:hasPermission name="help:edit">
            optionHtml += '<a class="btn btn-xs default yellow-stripe" href="javascript:;" data-href="${ctx}/helpCategory/update/' + data + '"><i class="fa fa-edit"></i> 编辑 </a>';
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
    <li><a href="javascript:;" data-href="${ctx}/helpCategory">帮助管理</a></li>
  </ul>
</div>
<!-- END PAGE HEADER-->

<div class="row">
  <div class="col-md-12">
    <!-- BEGIN ALERTS PORTLET-->
    <div class="portlet light bordered">
      <div class="portlet-title">
        <div class="caption">
          <i class="icon-speech"></i> 帮助管理
        </div>
        <div class="actions">
          <a class="btn btn-circle green" data-href="${ctx}/helpCategory/create">
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