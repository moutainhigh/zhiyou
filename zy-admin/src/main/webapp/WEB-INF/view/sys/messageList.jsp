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
            url: '${ctx}/message', // ajax source
          },
          columns : [
			  {
			      data : 'batchNumber',
			      title: '批次号',
			      width: '20%'
			  },
              {
                  data : 'content',
                  title: '标题',
                  width: '40%'
              },
              {
                  data : 'userId',
                  title: '用户id',
                  width: '20px'
              },
              {
                  data : 'isRead',
                  title: '是否已读',
                  width: '60px',
				  render : function(data, type, full) {
					  if(data) {
						  return '<i class="fa fa-check"></i>';
					  }
					  return '<i class="fa fa-times X"></i>';
				  }
              },
              {
                  data : 'messageType',
                  title: '消息类型',
                  width: '60px'
              },
				{
					data : 'id',
					title : '操作',
					width : '10%',
					orderable : false,
					render : function(data, type, full) {
						var optionHtml = '';
						<shiro:hasPermission name="message:message:edit">
		                  optionHtml = '<a class="btn btn-xs default yellow-stripe" href="javascript:;" data-href="${ctx}/message/delete/' + data + '" data-confirm="您确定要删除选中数据吗?"><i class="fa fa-trash-o"></i> 删除 </a>';
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
    <li><a href="javascript:;" data-href="${ctx}/message">消息管理</a></li>
  </ul>
</div>
<!-- END PAGE HEADER-->

<div class="row">
  <div class="col-md-12">
    <!-- BEGIN ALERTS PORTLET-->
    <div class="portlet box blue">
      <div class="portlet-title">
        <div class="caption">
          <i class="fa fa-user"></i> 消息管理
        </div>
        <div class="tools">
          <a class="collapse" href="javascript:;"> </a> <a class="reload" href="javascript:;"> </a>
        </div>
      </div>
      <div class="portlet-body clearfix">
        <div class="table-container">
          <div class="table-toolbar">
            <div class="btn-group">
              <button id="" class="btn green" data-href="${ctx}/message/create">
              	  新增 <i class="fa fa-plus"></i>
              </button>
            </div>
            <%-- <div class="btn-group">
                <button id="" class="btn grey" data-href="${ctx}/message/deleteByBatchNumber">
	              	  删除 <i class="fa fa-minus-square"></i>
	            </button>
            </div> --%>
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
                  <label class="sr-only"> 标题： </label>
                  <input type="text" name="titleLK" maxlength="50" class="form-control" placeholder="标题" />
                 </div>
                 <div class="form-group input-inline">
                  <label class="sr-only"> 类型： </label>
                  <select name="messageTypeEQ" class="form-control">
                  	<option value="">--请选择消息类型--</option>
                  	<c:forEach items="${messageTypes}" var="messageType">
                  		<option value="${messageType}">--${messageType}--</option>
                  	</c:forEach>
                  </select>
                  </div>
                  <div class="form-group input-inline">
                   <label class="sr-only"> 批次号： </label>
                  <input type="text" name="batchNumberEQ" maxlength="50" class="form-control" placeholder="批次号" />
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