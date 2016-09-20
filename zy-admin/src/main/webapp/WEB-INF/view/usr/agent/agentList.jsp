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
          order: [], // set first column as a default sort by desc
          ajax: {
            url: '${ctx}/agent', // ajax source
				},
				columns : [
						{
			               data : 'avatar',
			               title: '昵称',
			               width: '200px',
			               render : function(data, type, full) {
			                  return '<img src="' + data + '" width="30" height="30" style="border-radius: 40px !important; margin-right:5px"/>' + full.nickname;
			               }
			            },
						{
							data : 'phone',
							title : '手机号',
							orderable : false,
							width : '100px'
						},
						{
							data : 'qq',
							title : 'QQ',
							orderable : false,
							width : '100px'
						},
						{
							data : 'inviterNickname',
							title : '邀请人昵称',
							orderable : false,
							width : '60px'
						},
						{
							data : 'inviteCode',
							title : '邀请码',
							orderable : false,
							width : '60px'
						},
						{
							data : 'userRank',
							title : 'VIP',
							orderable : false,
							width : '60px'
						},
						{
							data : 'isFrozen',
							title : '是否冻结',
							orderable : false,
							width : '60px',
							render : function(data, type, full) {
								if (full.isFrozen == 0) {
									return '';
								}else if(full.isFrozen == 1){
									return '<label class="label label-danger">是</label>';
								}
							}
						},
						{
			                  data : 'userType',
			                  title: '类型',
			                  width: '60px'
			              },
						{
							data : 'registerTime',
							title : '注册时间',
							orderable : false,
							width : '100px'
						},
						{
							data : 'id',
							title : '操作',
							width : '15%',
							orderable : false,
							render : function(data, type, full) {
								var optionHtml = '';
								if(full.userType != '平台') {
									<shiro:hasPermission name="user:edit">
					                optionHtml += '<a class="btn btn-xs default yellow-stripe" href="javascript:;" data-href="${ctx}/agent/update/' + data + '"><i class="fa fa-edit"></i> 编辑 </a>';
					                </shiro:hasPermission>
					                <shiro:hasPermission name="user:addVip">
					                optionHtml += '<a class="btn btn-xs default yellow-stripe" href="javascript:;" onclick="addVip(' + full.id + ')"><i class="fa fa-user"></i> 加VIP </a>';
					                </shiro:hasPermission>
					                <shiro:hasPermission name="user:freeze">
					                if(full.isFrozen) {
					                	optionHtml += '<a class="btn btn-xs default yellow-stripe" href="javascript:;" data-href="${ctx}/agent/unFreeze/' + data + '" data-confirm="您确定要解冻代理['+full.nickname+']？"><i class="fa fa-smile-o"></i> 解冻 </a>';
					                }else {
					                	optionHtml += '<a class="btn btn-xs default yellow-stripe" href="javascript:;" data-href="${ctx}/agent/freeze/' + data + '" data-confirm="您确定要冻结代理['+full.nickname+']？"><i class="fa fa-meh-o"></i> 冻结 </a>';
					                }
					                </shiro:hasPermission>
								}
								return optionHtml;
							}
						} ]
			}
		});

	});
	
var $addVipDialog;
function addVip(id) {
	$addVipDialog = $.window({
		content: "<form action='' class='form-horizontal' style='margin-top: 20px;'>"
		+"<div class='form-body'>"
			+"<div class='form-group'>"
			+"<label class='control-label col-md-3'>加VIP等级:</label>"
			+"<div class='col-md-5'>"
			+"<select class='form-control' id='userRank'><option value=''>不加VIP</option><option value='V1'>V1</option><option value='V4'>V4</option></select>"
			+"</div>"
			+"</div>"
			+"<div class='form-group'>"
			+"<label class='control-label col-md-3'>备注信息:</label>"
			+"<div class='col-md-5'><textarea class='form-control' style='width: 220px;height: 120px;' id='remark'></textarea></div>"
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
		title : '加VIP',
		width : 420,
		height : 320,
		button: false
	});
}
function submitBtn(id) {
	var remark = $('#remark').val();
	var userRank = $('#userRank').find("option:selected").val();
	if(remark == '' || remark == null) {
		alert('请输入备注信息');
		return ;
	}
	$.post('${ctx}/agent/addVip',{id : id, userRank : userRank, remark : remark}, function(result) {
		toastr.success(result.message, '提示信息');
		$addVipDialog.close();
		grid.getDataTable().ajax.reload(null, false);
	})
	
}
function closeBtn() {
	$addVipDialog.close();
}
</script>
<script type="text/javascript">
	function exportUsers(){
		location.href = '${ctx}/agent/agentsExport?' + $('#searchForm').serialize();
	  }
</script>
<!-- END JAVASCRIPTS -->

<!-- BEGIN PAGE HEADER-->
<div class="page-bar">
  <ul class="page-breadcrumb">
    <li><i class="fa fa-home"></i> <a href="javascript:;" data-href="${ctx}/main">首页</a> <i class="fa fa-angle-right"></i></li>
    <li><a href="javascript:;" data-href="${ctx}/agent">代理管理</a></li>
  </ul>
</div>
<!-- END PAGE HEADER-->

<div class="row">
  <div class="col-md-12">
    <!-- BEGIN ALERTS PORTLET-->
    <div class="portlet light bordered">
      <div class="portlet-title">
        <div class="caption">
          <i class="fa fa-user"></i><span>代理管理 </span>
        </div>
        <div class="tools">
          <a class="collapse" href="javascript:;"> </a> <a class="reload" href="javascript:;"> </a>
        </div>
      </div>
      <div class="portlet-body clearfix">
        <div class="table-container">
          <div class="table-toolbar">
            <div class="btn-group">
              <button id="" class="btn green" data-href="${ctx}/agent/create">
                新增 <i class="fa fa-plus"></i>
              </button>
            </div>
            <!-- <div class="btn-group pull-right">
              <button class="btn dropdown-toggle" data-toggle="dropdown">
                工具 <i class="fa fa-angle-down"></i>
              </button>
              <ul class="dropdown-menu pull-right">
                <li><a href="#"> 打印 </a></li>
                <li><a href="javascript:void(0)" onClick="" class="easyui-linkbutton" data-options="iconCls:'icon-xls',plain:true">导出代理数据</a></li>
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
                  <label class="sr-only">昵称</label>
                  <input type="text" name="nicknameLK" maxlength="50" class="form-control" placeholder="昵称" />
                </div>
                <div class="form-group input-inline">
                  <label class="sr-only">手机</label>
                  <input type="text" name="phoneEQ" maxlength="50" class="form-control" placeholder="手机" />
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
