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
          <i class="fa icon-home"></i> 收货地址管理
        </div>
      </div>
      <div class="portlet-body clearfix">
        <div class="table-container">
          <div class="table-toolbar">
          <form class="filter-form form-inline">
                <input id="_orderBy" name="orderBy" type="hidden" value=""/>
                <input id="_direction" name="direction" type="hidden" value=""/>
                <input id="_pageNumber" name="pageNumber" type="hidden" value="0"/>
                <input id="_pageSize" name="pageSize" type="hidden" value="20" />
                
                <div class="form-group">
                  <input type="text" class="form-control" name="realnameLK" placeholder="姓名"/>
                </div>
                
                <div class="form-group input-inline">
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