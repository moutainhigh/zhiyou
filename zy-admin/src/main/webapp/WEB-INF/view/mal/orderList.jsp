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
          order: [], // set first column as a default sort by desc
          ajax: {
            url: '${ctx}/order', // ajax source
				},
				columns : [
						{
							data : 'sn',
							title : '订单',
							orderable : false,
							width : '300px',
						    render : function(data, type, full) {
						    	return 'sn: ' + full.sn + '<br /> 标题：' + full.title;
						      }
						},
						{
							data : 'orderStatus',
							title : '订单状态',
							orderable : false,
							width : '100px',
							render : function(data, type, full) {
								var result = '';
								if(data == '待支付'){
									result =  '<label class="label label-danger">待支付</label>';	
			                	}else if(data == '已支付'){
			                		result = '<label class="label label-success">已支付</label>';	
			                	}else if(data == '订单取消'){
			                		result = '<label class="label label-default">订单取消</label>';
			                	}else if(data == '已完成'){
			                		result =  '<label class="label label-default">已完成</label>';
			                	}
								return result;
			                  }
						},
						{
						    data : '',
						    title: '用户信息',
						    width: '180px',
						    orderable : false,
			                render : function(data, type, full) {
			                	return '<p>昵称: ' + full.userNickname + '</p><p>手机号: ' + full.userPhone + '</p>';
			                  }
						},
						{
							data : 'totalMoney',
							title : '交易金额',
							orderable : false,
							width : '160px',
						    render : function(data, type, full) {
						    	return '优惠金额: ' + full.discountFee.toFixed(2) + '<br />  总金额：' + full.amount.toFixed(2);
						    }
						},
						{
							data : 'createdTime',
							title : '下单时间',
							orderable : true,
							width : '120px'
						},
						{
							data : 'paidTime',
							title : '支付时间',
							orderable : true,
							width : '120px'
						},
						{
							data : 'remark',
							title : '备注',
							orderable : false,
							width : '120px'
						},
						{
							data : 'refund',
							title : '退款金额',
							orderable : true,
							width : '120px'
						},
						{
							data : 'refundedTime',
							title : '退款时间',
							orderable : true,
							width : '120px'
						},
						{
							data : 'refundRemark',
							title : '退款备注',
							orderable : false,
							width : '120px'
						}]
			}
		});

	});

	
</script>

<!-- BEGIN PAGE HEADER-->
<div class="page-bar">
  <ul class="page-breadcrumb">
    <li><i class="fa fa-home"></i> <a href="javascript:;" data-href="${ctx}/main">首页</a> <i class="fa fa-angle-right"></i></li>
    <li><a href="javascript:;" data-href="${ctx}/order">订单管理</a></li>
  </ul>
</div>
<!-- END PAGE HEADER-->

<div class="row">
  <div class="col-md-12">
    <!-- BEGIN ALERTS PORTLET-->
    <div class="portlet light bordered">
      <div class="portlet-title">
        <div class="caption">
          <i class="fa fa-clipboard"></i><span>订单管理 </span>
        </div>
        <div class="tools">
          
        </div>
      </div>
      <div class="portlet-body clearfix">
        <div class="table-container">
          <div class="table-toolbar">
            <!-- <div class="btn-group">
              <button id="" class="btn green" data-href="${ctx}/user/create">
                新增 <i class="fa fa-plus"></i>
              </button>
            </div>
            <div class="btn-group pull-right">
              <button class="btn dropdown-toggle" data-toggle="dropdown">
                工具 <i class="fa fa-angle-down"></i>
              </button>
              <ul class="dropdown-menu pull-right">
                <li><a href="#"> 打印 </a></li>
                <li><a href="javascript:void(0)" onClick="" class="easyui-linkbutton" data-options="iconCls:'icon-xls',plain:true">导出用户数据</a></li>
              </ul>
            </div> -->
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
                  <label class="sr-only">手机号</label>
                  <input type="text" name="phoneEQ" maxlength="50" class="form-control" placeholder="手机号" />
                </div>
                
                <div class="form-group input-inline">
                  <label class="sr-only">昵称</label>
                  <input type="text" name="nicknameLK" maxlength="50" class="form-control" placeholder="昵称" />
                </div>
                
                <div class="form-group input-inline">
                  <label class="sr-only">订单号</label>
                  <input type="text" name=refIdEQ maxlength="50" class="form-control" placeholder="订单号" />
                </div>
                <div class="form-group input-inline">
                  <label class="sr-only">订单状态</label>
                  <select name="orderStatusEQ" class="form-control">
                  	<option value="">请选择订单状态</option>
                  	<option label="0">待支付</option>
                  	<option label="1">已支付</option>
                  	<option label="2">已发货</option>
                  	<option label="3">已完成</option>
                  	<option label="4">已退款</option>
                  	<option label="5">已取消</option>
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
