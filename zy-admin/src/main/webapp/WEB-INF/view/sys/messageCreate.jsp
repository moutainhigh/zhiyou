<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/view/include/head.jsp"%>
<%@ include file="/WEB-INF/view/include/form.jsp"%>
<!-- BEGIN JAVASCRIPTS -->
<style>
#tableContainer {
  display: none;
}
</style>
<script>
var grid = new Datatable();

$(function() {
  
    $('#userList').click(function(){
    	$('#tableContainer').show();
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
                url: '${ctx}/message/userList', // ajax source
    				},
    				columns : [
    						{
    							data : 'id',
    							className : 'table-checkbox',
    							title : '<input type="checkbox" class="group-checkable" data-set="#dataTable .checkboxes" />',
    							width : '15%',
    							orderable : false,
    							render : function(data, type, full) {
    								return '<input type="checkbox" class="checkboxes" value="' + data + '"/>';
    							}
    						},
    						{
    			               data : 'avatar',
    			               title: '头像',
    			               width: '100px',
    			               render : function(data, type, full) {
    			                  return '<img src="' + data + '" width="80" height="80" />';
    			               }
    			            },
    						{
    							data : 'id',
    							title : 'userId',
    							orderable : false,
    							width : '50px'
    						},
    						{
    							data : 'phone',
    							title : '手机号',
    							orderable : false,
    							width : '100px'
    						},
    						{
    							data : 'nickname',
    							title : '昵称',
    							orderable : false,
    							width : '100px'
    						},
    						{
    							data : 'isVip',
    							title : '是否VIP',
    							orderable : false,
    							width : '100px',
    							render : function(data, type, full) {
    								if (full.isLecturer) {
    									return '是';
    								}else if(!full.isBindPhone){
    									return '否';
    								}
    							}
    						},
    						{
    							data : 'isLecturer',
    							title : '讲师',
    							orderable : false,
    							width : '60px',
    							render : function(data, type, full) {
    								if (full.isLecturer) {
    									return '是';
    								}else if(!full.isBindPhone){
    									return '否';
    								}
    							}
    						},
    						{
    							data : 'registerTime',
    							title : '注册时间',
    							orderable : false,
    							width : '100px'
    						}]
    			}
    		});
    	$('#userList').hide();
    	$('#send').show();
	})
	
	 $('#createForm').validate({
      	rules : {
			'content' : {
				required : true
			}
      	},
     	 messages : {
	        'content' : {
	        	required : '请输入发送内容'
	        }
      	},
      	submitHandler : function(form) {
      		$('#nicknameSub').val($('#nickname').val());
    		$('#phoneSub').val($('#phone').val());
    		$('#beginDateSub').val($('#beginDate').val());
    		$('#endDateSub').val($('#endDate').val()); 
    		
			$(form).find(':submit').prop('disabled', true);
			Layout.postForm(form);
		}
     });
    
});
</script>

<div class="row">
  <div class="col-md-12">
    <!-- BEGIN VALIDATION STATES-->
    
    <div class="portlet box yellow">
      <div class="portlet-title">
        <div class="caption">
          <i class="fa fa-edit"></i><span> 消息创建 </span>
        </div>
        <div class="tools">
          <a href="javascript:;" class="collapse"> </a> <a href="javascript:;" class="reload"> </a>
        </div>
      </div>
      <div class="portlet-body form">
        <!-- BEGIN FORM-->
        <form id="createForm" action="" data-action="${ctx}/message/create" class="form-horizontal" method="post">
            <input type="hidden" name="nicknameLK" id="nicknameSub" />
            <input type="hidden" name="phoneEQ" id="phoneSub" />
            <input type="hidden" name="registerTimeGTE" id="beginDateSub" />
            <input type="hidden" name="registerTimeLT" id="endDateSub" />
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
                  <input type="text" id="batchNumber" class="form-control" name="batchNumber" value="${batchNumber}" placeholder="请输入批次号" readonly="readonly"/>
              </div>
            </div>
            
            <div class="form-group">
              <label class="control-label col-md-3">内容
              </label>
              <div class="col-md-4">
                 <textarea rows="3" cols="60" class="form-control" name="content">${content}</textarea>
              </div>
            </div>
         
 			<div class="form-group">
              <label class="control-label col-md-3">备注
              </label>
              <div class="col-md-4">
              	<input type="text" class="form-control" value="每次发送消息的接收人，不能超过${maxSendCount}条记录"  disabled="disabled"/>
              </div>
            </div>
            
          </div>
          <div class="form-actions fluid">
            <div class="col-md-offset-3 col-md-9">
              <button type="button" class="btn green" id="userList">选择发送的用户</button>
              <button type="submit" class="btn green" id="send" style="display:none;">发送</button>
              <button class="btn default" data-href="${ctx}/message">
                <i class="fa fa-arrow-left"></i> 返回
              </button>
            </div>
          </div>
        </form>
        <!-- END FORM-->
      </div>
    </div>
    

        <div class="table-container" id="tableContainer">
		  
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
                  <label class="sr-only">昵称</label>
                  <input type="text" name="nicknameLK" id="nickname" maxlength="50" class="form-control" placeholder="昵称" />
                </div>
                <div class="form-group input-inline">
                  <label class="sr-only">手机</label>
                  <input type="text" name="phoneEQ" id="phone" maxlength="50" class="form-control" placeholder="手机" />
                </div> 
                <div class="form-group input-inline">
                  <label class="sr-only">注册时间起</label>
                  <input class="Wdate form-control" type="text" id="beginDate" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',readOnly:true})"
                  	name="registerTimeGTE" value="" placeholder="注册时间起" />
                </div>
                <div class="form-group input-inline">
                 <label class="sr-only">注册时间止</label>
                  <input class="Wdate form-control" type="text" id="endDate" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',readOnly:true})" 
                  name="registerTimeLT" value="" placeholder="注册时间止"/>
                </div>
                <div class="form-group input-inline">
	                <button class="btn blue filter-submit">
	                  <i class="fa fa-check"></i> 筛选
	                </button>
	             </div>
              </form>
            </div>
          </div>
          <table class="table table-striped table-bordered table-hover" id="dataTable">
          </table>
        </div>
      <!-- END ALERTS PORTLET-->
    </div>
    <!-- END VALIDATION STATES-->
  </div>
