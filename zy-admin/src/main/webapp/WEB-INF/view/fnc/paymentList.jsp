<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/view/include/head.jsp"%>
<script type="text/javascript" src="${ctx}/plugin/My97DatePicker/WdatePicker.js"></script>
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
          order: [], // set first column as a default sort by desc
        	  ajax: {
                  url: '${ctx}/payment', // ajax source
                },
				columns : [
						{
							data : 'sn',
							title : '支付单sn',
							orderable : false,
							width : '100px'
						},
						{
							data : 'title',
							title : '标题',
							orderable : false,
							width : '50px'
						},
						{
							data : 'createdTime',
							title : '创建时间',
							orderable : true,
							width : '50px'
						},
						{
							data : 'paidTime',
							title : '支付时间',
							orderable : true,
							width : '50px'
						},
						{
							data : 'paymentType',
							title : '支付方式',
							orderable : false,
							width : '50px'
						},
						{
							data : 'paymentStatus',
							title : '支付状态',
							orderable : false,
							width : '50px',
			                render : function(data, type, full) {
			                	if(data == '待支付'){
			                		return '<label class="label label-danger">待支付</label>';	
			                	}else if(data == '已支付'){
			                		return '<label class="label label-success">已支付</label>';	
			                	}else if(data == '支付取消'){
			                		return '<label class="label label-default">支付取消</label>';
			                	}
			                  }
						},
						{
							data : 'amount',
							title : '应付金额',
							orderable : false,
							width : '50px'
						},
						{
							data : 'nickname',
							title : '用户昵称',
							orderable : false,
							width : '50px'
						},
						{
							data : 'phone',
							title : '手机号',
							orderable : false,
							width : '50px'
						},
			              {
			                data : '',
			                title: '操作',
			                width: '10%',
			                orderable : false,
			                render : function(data, type, full) {
			                	var optionHtml = '';
			                	if(full.paymentType == '线下支付' && full.paymentStatus == '待支付'){
			                		optionHtml = '<a class="btn btn-xs default yellow-stripe" href="javascript:;" data-href="${ctx}/payment/confirmPaid/' + full.id + '" data-confirm="您确定用户【'+full.nickname+'】的支付单【'+full.title+'】已支付?"><i class="fa fa-edit"></i> 确认已支付 </a>';	
			                	}
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
    <li><a href="javascript:;" data-href="${ctx}/payment">支付单管理</a></li>
  </ul>
</div>
<!-- END PAGE HEADER-->

<div class="row">
  <div class="col-md-12">
    <!-- BEGIN ALERTS PORTLET-->
    <div class="portlet light bordered">
      <div class="portlet-title">
        <div class="caption">
          <i class="fa fa-list"></i><span>支付单管理 </span>
        </div>
        <div class="tools">
          <a class="collapse" href="javascript:;"> </a> <a class="reload" href="javascript:;"> </a>
        </div>
      </div>
      <div class="portlet-body clearfix">
        <div class="table-container">
          <div class="table-toolbar">
            <div class="btn-group">
              <%-- <button id="" class="btn green" data-href="${ctx}/order/create">
                新增 <i class="fa fa-plus"></i>
              </button> --%>
            </div>
            <!-- <div class="btn-group pull-right">
              <button class="btn dropdown-toggle" data-toggle="dropdown">
                工具 <i class="fa fa-angle-down"></i>
              </button>
              <ul class="dropdown-menu pull-right">
                <li><a href="#"> 打印 </a></li>
                <li><a href="javascript:void(0)" onClick="" class="easyui-linkbutton" data-options="iconCls:'icon-xls',plain:true">导出支付单数据</a></li>
              </ul>
            </div> -->
          </div>

          <div class="row">
            <div class="col-md-3 table-actions">
              <span class="table-row-checked"></span>
            </div>
            <div class="col-md-9">
              <form class="filter-form pull-right" id="searchForm">
                <input id="_orderBy" name="orderBy" type="hidden" value=""/>
                <input id="_direction" name="direction" type="hidden" value=""/>
                <input id="_pageNumber" name="pageNumber" type="hidden" value="0"/>
                <input id="_pageSize" name="pageSize" type="hidden" value="20"/>
                <div class="form-group input-inline">
                
              	  <label class="sr-only">支付状态</label>
                   <select name="paymentStatusEQ" class="form-control">
               		<option value="">--请选择支付状态-- </option>
           			<option value="待支付">待支付</option>
           			<option value="已支付">已支付</option>
           			<option value="支付取消">支付取消</option>
                   </select>
                </div>
                <div class="form-group input-inline">
                  <button class="btn blue filter-submit">
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