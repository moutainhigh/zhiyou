<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/view/include/head.jsp"%>
<style>
	img {
		cursor: pointer; 
	}
</style>
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
          pageLength: 20, // default record count per page
          order: [], // set first column as a default sort by desc
          ajax: {
            url: '${ctx}/address', // ajax source
          },
          columns : [
			 {
			    data : '',
			    title: '基本信息',
			    width: '120px',
                render : function(data, type, full) {
                	return '<p>姓名:' + full.realname + '</p><p>手机号:' + full.phone + '</p>';
                  }
			 },
			 {
			    data : '',
			    title: '省市区',
			    width: '120px',
                render : function(data, type, full) {
                	return '<p>' + full.province + '-' + full.city + '-' + full.district;
                }
			 },
             {
  				data : 'address',
  				title : '详细地址',
  				orderable : false,
  				width : '200px'
  			 },
             {
   				data : 'isDefault',
   				title : '是否默认地址',
   				orderable : false,
   				width : '100px',
                render : function(data, type, full) {
                	return data?'是':'';
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
    <li><a href="javascript:;" data-href="${ctx}/address">收货地址管理</a></li>
  </ul>
</div>
<!-- END PAGE HEADER-->

<div class="row">
  <div class="col-md-12">
    <!-- BEGIN ALERTS PORTLET-->
    <div class="portlet light bordered">
      <div class="portlet-title">
        <div class="caption">
          <i class="fa fa-institution"></i> 收货地址管理
        </div>
        <div class="tools">
          <a class="collapse" href="javascript:;"> </a> <a class="reload" href="javascript:;"> </a>
        </div>
      </div>
      <div class="portlet-body clearfix">
        <div class="table-container">
          <div class="table-toolbar">
            <%-- <div class="btn-group">
               <button id="" class="btn green" data-href="${ctx}/gift/create">
                新增 <i class="fa fa-plus"></i>
              </button>
            </div> --%>
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
              <form class="filter-form pull-right">
                <input id="_orderBy" name="orderBy" type="hidden" value=""/>
                <input id="_direction" name="direction" type="hidden" value=""/>
                <input id="_pageNumber" name="pageNumber" type="hidden" value="0"/>
                <input id="_pageSize" name="pageSize" type="hidden" value="20" />
                
                <div class="form-group input-inline">
                  <label class="sr-only"> 姓名： </label>
                  <input type="text" class="form-control" name="realnameLK" placeholder="姓名"/>
                </div>
                
                <div class="form-group input-inline">
                <button class="btn purple filter-submit">
                  <i class="fa fa-check"></i> 查询
                </button>
                </div>
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