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
          pageLength: 20, // default record count per page
          order: [
                    [1, 'desc']
          ], // set first column as a default sort by desc
          ajax: {
            url: '${ctx}/platformLogistics', // ajax source
          },
          columns : [
			  {
			      data : 'name',
			      title: '物流公司',
			      width: '20%'
			  },
              {
                  data : 'sn',
                  title: '物流单号',
                  width: '40%'
              },
              {
                  data : 'platformLogisticsStatus',
                  title: '状态',
                  width: '60px'
              }]
        }
      });

});

  function exportData(){
	var platformLogisticsStatusEQ = $('#platformLogisticsStatusEQ').val();
	if(platformLogisticsStatusEQ != '待发货' && platformLogisticsStatusEQ != '已使用' && platformLogisticsStatusEQ != '已发货'){
		toastr.error('只可以导出待发货和已使用的物流数据', '错误信息');
		return;
	}
	location.href = '${ctx}/platformLogistics/exportData?' + $('#searchForm').serialize();
  }
</script>
<!-- END JAVASCRIPTS -->

<!-- BEGIN PAGE HEADER-->
<div class="page-bar">
  <ul class="page-breadcrumb">
    <li><i class="fa fa-home"></i> <a href="javascript:;" data-href="${ctx}/main">首页</a> <i class="fa fa-angle-right"></i></li>
    <li><a href="javascript:;" data-href="${ctx}/platformLogistics">平台物流管理</a></li>
  </ul>
</div>
<!-- END PAGE HEADER-->

<div class="row">
  <div class="col-md-12">
    <!-- BEGIN ALERTS PORTLET-->
    <div class="portlet box blue">
      <div class="portlet-title">
        <div class="caption">
          <i class="fa fa-user"></i> 平台物流管理
        </div>
        <div class="tools">
          <a class="collapse" href="javascript:;"> </a> <a class="reload" href="javascript:;"> </a>
        </div>
      </div>
      <div class="portlet-body clearfix">
        <div class="table-container">
          <div class="table-toolbar">
            <div class="btn-group">
              <a id="" class="btn green" target="_blank" href="${ctx}/platformLogistics/toImport">
              	  导入物流数据 <i class="fa fa-plus"></i>
              </a>
            </div>
            <div class="btn-group pull-right">
              <button class="btn dropdown-toggle" data-toggle="dropdown">
                工具 <i class="fa fa-angle-down"></i>
              </button>
              <ul class="dropdown-menu pull-right">
                <li><a href="javascript:void(0)" onClick="exportData()"> 导出物流数据 </a></li>
              </ul>
            </div>            
          </div>

          <div class="row">
            <div class="col-md-3 table-actions">
              <span class="table-row-checked"></span>
            </div>
            <div class="col-md-9">
              <form class="filter-form form-inline pull-right" id="searchForm">
                <input id="_orderBy" name="orderBy" type="hidden" value=""/>
                <input id="_direction" name="direction" type="hidden" value=""/>
                <input id="_pageNumber" name="pageNumber" type="hidden" value="0"/>
                <input id="_pageSize" name="pageSize" type="hidden" value="20"/>
                <div class="form-group input-inline">
                  <label class="sr-only"> 物流公司： </label>
                  <input type="text" name="nameLK" maxlength="50" class="form-control" placeholder="物流公司" />
                </div>
                <div class="form-group input-inline">
                  <label class="sr-only"> 物流单号： </label>
                  <input type="text" name="snLK" maxlength="50" class="form-control" placeholder="物流单号" />
                </div>                 
                <div class="form-group input-inline">
                  <label class="sr-only"> 状态： </label>
                  <select name="platformLogisticsStatusEQ" id="platformLogisticsStatusEQ" class="form-control">
                  	<option value="">--请选择平台物流状态--</option>
                  	<c:forEach items="${platformLogisticsStatuss}" var="platformLogisticsStatus">
                  		<option value="${platformLogisticsStatus}">${platformLogisticsStatus}</option>
                  	</c:forEach>
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