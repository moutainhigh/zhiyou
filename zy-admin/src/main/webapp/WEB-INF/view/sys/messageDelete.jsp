<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/view/include/head.jsp"%>
<%@ include file="/WEB-INF/view/include/form.jsp"%>
<!-- BEGIN JAVASCRIPTS -->
<script>
  $(function() {
    $('#updateForm').validate({
      rules : {
			'batchNumber' : {
				required : true
			}
      },
      messages : {
	        'batchNumber' : {
	        	required : '请输入批次号'
	        }
      	}
    });
    
  });
</script>
<!-- END JAVASCRIPTS -->
<style>
.portlet > .portlet-body.blue, .portlet.blue {
    background-color: white;
}
</style>
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
    <!-- BEGIN VALIDATION STATES-->
    
    <div class="portlet box yellow">
      <div class="portlet-title">
        <div class="caption">
          <i class="fa fa-edit"></i><span> 消息删除 </span>
        </div>
        <div class="tools">
          <a href="javascript:;" class="collapse"> </a> <a href="javascript:;" class="reload"> </a>
        </div>
      </div>
      <div class="portlet-body form">
        <!-- BEGIN FORM-->
        <form id="updateForm" action="" data-action="${ctx}/message/deleteByBatchNumber" class="form-horizontal" method="post">
          <div class="form-body">
            <div class="alert alert-danger display-hide">
              <i class="fa fa-exclamation-circle"></i>
              <button class="close" data-close="alert"></button>
              <span class="form-errors">您填写的信息有误，请检查。</span>
            </div>
            
            <div class="form-group">
              <label class="control-label col-md-3">批次号
              </label>
              <div class="col-md-4">
                <div class="input-icon right">
                  <i class="fa fa-user"></i> 
                  <input type="text" id="batchNumber" class="form-control" name="batchNumber" value="" placeholder="请输入批次号"/>
                </div>
              </div>
            </div>
            
          </div>
          <div class="form-actions fluid">
            <div class="col-md-offset-3 col-md-9">
              <button type="submit" class="btn green">删除</button>
              <button class="btn default" data-href="${ctx}/message">
                <i class="fa fa-arrow-left"></i> 返回
              </button>
            </div>
          </div>
        </form>
        <!-- END FORM-->
      </div>
    </div>
    
    <div class="portlet box blue">
      <div class="portlet-title">
        <div class="caption">
          <i class="fa"></i><span> 消息批次列表 </span>
        </div>
        <div class="tools">
          <a href="javascript:;" class="collapse"> </a> <a href="javascript:;" class="reload"> </a>
        </div>
      </div>
      <div class="table-scrollable">
         <table class="table table-striped table-bordered table-hover" id="dataTable">
         	<thead>
         		<tr role="row">
         			<th class="sorting_disabled" rowspan="1" colspan="1" style="width: 20%;" aria-label="批次号">批次号</th>
         			<th class="sorting_disabled" rowspan="1" colspan="1" style="width: 50%;" aria-label="消息内容">消息内容</th>
         			<th class="sorting_disabled" rowspan="1" colspan="1" style="width: 15%;" aria-label="创建时间">创建时间</th>
         			<th class="sorting_disabled" rowspan="1" colspan="1" style="width: 15%;" aria-label="消息类型">消息类型</th>
         		</tr>
         	</thead>
         	<tbody>
         		<c:forEach items="${messages}" var="message">
         			<tr class="even" role="row">
         				<td>${message.batchNumber}</td>
         				<td>${message.content}</td>
         				<td><fmt:formatDate value="${message.createdTime}" pattern="yyyy-MM-dd HH:mm:ss"></fmt:formatDate></td>
         				<td>${message.messageType}</td>
         			</tr>
         		</c:forEach>
         	</tbody>
         </table>
      </div>
    </div>
    <!-- END VALIDATION STATES-->
  </div>
</div>
