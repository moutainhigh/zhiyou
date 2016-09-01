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
          order: [
                    [1, 'desc']
          ], // set first column as a default sort by desc
          ajax: {
            url: '${ctx}/feedback', // ajax source
          },
          columns : [
              {
				data : 'userId',
				title : '用户id',
				width : '80px'
			  },
              {
                  data : 'feedbackType',
                  title: '反馈类型',
                  width: '80px'
              },
              {
                  data : 'feedbackStatus',
                  title: '反馈状态',
                  width: '80px',
					render : function(data, type, full) {
						var result = '';
						if(data == '等待客服接手'){
							result =  '<label class="label label-danger">等待客服接手</label>';	
	                	}else if(data == '客服已接手'){
	                		result = '<label class="label label-success">客服已接手</label>';	
	                	}else if(data == '已关闭'){
	                		result = '<label class="label label-default">已关闭</label>';
	                	}
						return result;
	                  }
              },
              {
                  data : 'content',
                  title: '内容',
                  width: '200px'
              },
              {
                  data : 'createdTime',
                  title: '创建时间',
                  width: '160px'
              },
              {
                  data : 'reply',
                  title: '客服回复',
                  width: '200px'
              },
              {
                  data : 'repliedTime',
                  title: '回复时间',
                  width: '160px'
              },
              {
                data : 'id',
                title: '操作',
                width: '120px',
                orderable : false,
                render : function(data, type, full) {
                	var optionHtml = '';
                	if(full.feedbackStatus == '等待客服接手') {
                		optionHtml = '<a class="btn btn-xs default yellow-stripe" href="javascript:;" onclick="solve(' + full.id + ')"><i class="fa fa-user"></i> 接手申诉 </a>'
                	}else if(full.feedbackStatus == '客服已接手') {
                		optionHtml = '<a class="btn btn-xs default yellow-stripe" href="javascript:;" onclick="reply(' + full.id + ')"><i class="fa fa-edit"></i> 回复 </a>'
                	}
                    return optionHtml;
                }
              } ]
        }
      });

});

function solve(id) {
	$.post('${ctx}/feedback/solve', {id: id}, function(result){
		//grid.getDataTable().ajax.reload();
		grid.getDataTable().ajax.reload(null, false);
	});
}

var $replyDialog;
function reply(id) {
	$replyDialog = $.window({
		content: "<form action='' class='form-horizontal' style='margin-top: 20px;'>"
		+"<div class='form-body'>"
		+"<div class='form-group'>"
		+"<label class='control-label col-md-3'>回复信息:</label>"
		+"<div class='col-md-5'><textarea class='form-control' style='width: 220px;height: 120px;' id='reply'></textarea></div>"
		+"</div>"
		+"</div>"
		+"<div class='form-actions fluid'>"
		+"<div class='col-md-offset-3 col-md-9'>"
		+"<button type='button' class='btn green' onclick='submitBtn("+id+")'>"
		+"保存</button>"
		+"<button type='button' class='btn default' onclick='closeBtn()' style='margin-left: 20px;'>"
		+"取消</button>"
		+"</div>"
		+"</div>"
		+"</form>",
		title : '申诉回复',
		width : 420,
		height : 320,
		button: false
	});
}
function submitBtn(id) {
	var reply = $('#reply').val();
	$.post('${ctx}/feedback/reply',{id : id, reply : reply}, function(result) {
		toastr.success(result.message, '提示信息');
		$replyDialog.close();
		grid.getDataTable().ajax.reload(null, false);
	})
	
}
function closeBtn() {
	$replyDialog.close();
}

</script>
<!-- END JAVASCRIPTS -->

<!-- BEGIN PAGE HEADER-->
<div class="page-bar">
  <ul class="page-breadcrumb">
    <li><i class="fa fa-home"></i> <a href="javascript:;" data-href="${ctx}/main">首页</a> <i class="fa fa-angle-right"></i></li>
    <li><a href="javascript:;" data-href="${ctx}/feedback">申诉管理</a></li>
  </ul>
</div>
<!-- END PAGE HEADER-->

<div class="row">
  <div class="col-md-12">
    <!-- BEGIN ALERTS PORTLET-->
    <div class="portlet box blue">
      <div class="portlet-title">
        <div class="caption">
          <i class="fa fa-user"></i> 申诉管理
        </div>
        <div class="tools">
          <a class="collapse" href="javascript:;"> </a> <a class="reload" href="javascript:;"> </a>
        </div>
      </div>
      <div class="portlet-body clearfix">
        <div class="table-container">
          <div class="table-toolbar">
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
              <form class="filter-form form-inline pull-right">
                <input id="_orderBy" name="orderBy" type="hidden" value=""/>
                <input id="_direction" name="direction" type="hidden" value=""/>
                <input id="_pageNumber" name="pageNumber" type="hidden" value="0"/>
                <input id="_pageSize" name="pageSize" type="hidden" value="20"/>
                <div class="form-group input-inline">
                 
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