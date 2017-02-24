<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/view/include/head.jsp" %>
<script id="modifyValidTimeTmpl" type="text/x-handlebars-template">
  <form id="modifyForm" action="" data-action="" class="form-horizontal" method="post" style="width: 95%; margin: 10px;">
    <input type="hidden" name="ids" value="{{ids}}"/>
    <div class="form-body">
      <div class="alert alert-danger display-hide">
        <i class="fa fa-exclamation-circle"></i>
        <button class="close" data-close="alert"></button>
        <span class="form-errors">您填写的信息有误，请检查。</span>
      </div>
      <div class="form-group">
        <label class="control-label col-md-2">生效时间起<span class="required"> * </span></label>
        <div class="col-md-5">
          <input class="Wdate form-control" type="text" id="beginTime"
                 onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',readOnly:true})" name="beginTime" value="" placeholder="生效时间起"/>
        </div>
      </div>
      <div class="form-group">
        <label class="control-label col-md-2">生效时间止<span class="required"> * </span></label>
        <div class="col-md-5">
          <input class="Wdate form-control" type="text" id="endTime"
                 onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',readOnly:true})" name="endTime" value="" placeholder="生效时间止"/>
        </div>
      </div>
    </div>
    <div class="form-actions fluid">
      <div class="col-md-offset-3 col-md-9">
        <button id="modifySubmit" type="button" class="btn green">
          <i class="fa fa-save"></i> 保存
        </button>
        <button id="modifyCancel" class="btn default" data-href="">
          <i class="fa fa-chevron-left"></i> 返回
        </button>
      </div>
    </div>
  </form>
</script>
<script>

  $(function () {

    var grid = new Datatable();

    grid.init({
      src: $('#dataTable'),
      onSuccess: function (grid) {
        // execute some code after table records loaded
      },
      onError: function (grid) {
        // execute some code on network or other general error
      },
      dataTable: {
        //"sDom" : "<'row'<'col-md-8 col-sm-12'pli><'col-md-4 col-sm-12'<'table-group-actions pull-right'>>r>t<'row'<'col-md-8 col-sm-12'pli><'col-md-4 col-sm-12'>r>>",
        lengthMenu: [[10, 20, 50, 100], [10, 20, 50, 100]],// change per page values here
        pageLength: 20, // default record count per page
        order: [], // set first column as a default sort by desc
        ajax: {
          url: '${ctx}/policy', // ajax source
        },
        columns: [
	        {
		        data: 'id',
		        title: '<input type="checkbox" name="checkAll" id="checkAll" value=""/>',
		        render: function (data, type, full) {
			        if(full.policyStatus == '审核中'){
				        return '<input type="checkbox" name="id" id="id" value="' + data + '"/>';
			        } else {
				        return '<input type="checkbox" name="id" disabled="disabled" id="id" value="' + data + '"/>';
			        }
		        }
	        },
          {
            data: 'reportId',
            title: '检测报告编号'
          },
          {
            data: '',
            title: '客户信息',
            orderable: false,
            render: function (data, type, full) {
              return '<p>姓名: ' + full.realname + '</p><p>性别: ' + full.gender + '</p><p>手机号: ' + full.phone + '</p>';
            }
          },
          {
            data: '',
            title: '用户信息',
            orderable: false,
            render: function (data, type, full) {
              return formatUser(full.user);
            }
          },
          {
            data: 'birthday',
            title: '出生年月',
            orderable: false
          },
	        {
		        data: 'image1Thumbnail',
		        title: '身份证',
		        orderable: false,
		        render: function (data, type, full) {
		        	if(data != null) {
				        return '<img class="image-view" data-title="身份证正面照" data-src="' + full.image1 + '" src="' + data + '" width="120" height="80" />';
              }
			        return '';
		        }
	        },
          {
            data: 'idCardNumber',
            title: '身份证号',
            orderable: false
          },
          {
            data: 'code',
            title: '保险单号',
            orderable: false
          },
	        {
		        data: 'policyStatus',
		        title: '进度状态',
		        orderable: false,
		        render: function (data, type, full) {
			        return '<label class="label label-' + full.policyStatusStyle + '">' + data + '</label>';
		        }
	        },
	        {
		        data: '',
		        title: '生效时间',
		        orderable: false,
            render: function (data, type, full) {
		        	if(full.validTimeBeginLabel != null) {
		        		return '起: <p>' + full.validTimeBeginLabel + '</p>止: <p>' + full.validTimeEndLabel + '</p>';
              }
              return '';
            }
	        },
          {
		        data: 'createdTimeLabel',
		        title: '创建时间',
		        orderable: false
	        }
        ]
      }
    });

	  $('#dataTable').on('click', '.image-view', function () {
		  var url = $(this).attr('data-src');
		  var title = $(this).attr('data-title');
		  $.imageview({
			  url: url,
			  title: title
		  });
	  });

	  $('#dataTable').on('click', '#checkAll', function(){
			  var isChecked = $(this).attr("checked");
			  if(isChecked == 'checked'){
				  $("input[name='id']").each(function() {
					  var disableAttr = $(this).attr("disabled");
					  if(disableAttr == undefined){
						  $(this).parent().addClass("checked");
						  $(this).attr("checked",'true');
					  }
				  });
			  } else {
				  $("input[name='id']").each(function() {
					  var disableAttr = $(this).attr("disabled");
					  if(disableAttr == undefined){
						  $(this).parent().removeClass("checked");
						  $(this).removeAttr("checked");
					  }
				  });
			  }
		  }
	  );

	  <shiro:hasPermission name="policy:modify">
	  var modifyTemplate = Handlebars.compile($('#modifyValidTimeTmpl').html());
	  $('.table-toolbar').on('click', '#modifyValidTimeBtn',function(){
			  var ids = '';
			  $("input[name='id']").each(function() {
				  var isChecked = $(this).attr("checked");
				  if(isChecked == 'checked'){
					  ids += $(this).val() + ',';
				  }
			  })
			  if(ids.length > 0){
				  var data = {
					  ids: ids
				  };
				  var html = modifyTemplate(data);
				  var index = layer.open({
					  type: 1,
					  //skin: 'layui-layer-rim', //加上边框
					  area: ['600px', '360px'], //宽高
					  content: html
				  });

				  $form = $('#modifyForm');
				  $form.validate({
					  rules: {
						  'beginTime': {
							  required: true
						  },
						  'endTime': {
							  required: true
						  }
					  },
					  messages: {}
				  });

				  $('#modifySubmit').bind('click', function () {
					  var result = $form.validate().form();
					  if (result) {
						  var url = '${ctx}/policy/modifyValidTime';
						  $.post(url, $form.serialize(), function (data) {
							  if (data.code === 0) {
								  layer.alert('操作成功');
								  layer.close(index);
								  grid.getDataTable().ajax.reload(null, false);
							  } else {
								  layer.alert('操作失败:' + data.message);
							  }
						  });
					  }
					  layer.close(index);
				  })

				  $('#modifyCancel').bind('click', function () {
					  layer.close(index);
				  })

			  } else {
				  layer.alert("请至少选择一条记录！", {icon: 2});
			  }
	  });

	  $('.table-toolbar').on('click', '#failBtn',function(){
		  layer.confirm('您确认要批量设置审核失败？', {
			  btn: ['确定','取消']
		  }, function(){
			  var ids = '';
			  $("input[name='id']").each(function() {
				  var isChecked = $(this).attr("checked");
				  if(isChecked == 'checked'){
					  ids += $(this).val() + ',';
				  }
			  })
			  if(ids.length > 0){
				  $.ajax({
					  url : '${ctx}/policy/fail',
					  dataType:"json",
					  type: "post",
					  data : {
						  ids : ids
					  },
					  success: function( result ) {
						  layer.alert(result.message, {icon: 1});
						  grid.getDataTable().ajax.reload(null, false);
					  }
				  });

			  } else {
				  layer.alert("请至少选择一条记录！", {icon: 2});
			  }

		  }, function(){

		  });
	  });

	  </shiro:hasPermission>

  });
  <shiro:hasPermission name="policy:export">
  function policyExport() {
    location.href = '${ctx}/policy/export?' + $('#searchForm').serialize();
  }
  </shiro:hasPermission>
</script>
<!-- END JAVASCRIPTS -->

<!-- BEGIN PAGE HEADER-->
<div class="page-bar">
  <ul class="page-breadcrumb">
    <li><i class="fa fa-home"></i> <a href="javascript:;" data-href="${ctx}/main">首页</a> <i class="fa fa-angle-right"></i></li>
    <li><a href="javascript:;" data-href="${ctx}/policy">保险单</a></li>
  </ul>
</div>
<!-- END PAGE HEADER-->

<div class="row">
  <div class="col-md-12">
    <!-- BEGIN ALERTS PORTLET-->
    <div class="portlet light bordered">
      <div class="portlet-title">
        <div class="caption">
          <i class="icon-umbrella"></i> 保险单管理
        </div>
      </div>
      <div class="portlet-body clearfix">
        <div class="table-container">
          <div class="table-toolbar">
            <form id="searchForm" class="filter-form form-inline">
              <input id="_orderBy" name="orderBy" type="hidden" value=""/> <input id="_direction" name="direction" type="hidden" value=""/>
              <input id="_pageNumber" name="pageNumber" type="hidden" value="0"/> <input id="_pageSize" name="pageSize" type="hidden" value="20"/>

              <div class="form-group">
                <input type="text" name="realnameLK" class="form-control" placeholder="客户姓名"/>
              </div>

              <div class="form-group">
                <input type="text" name="phoneEQ" class="form-control" placeholder="客户手机"/>
              </div>

              <div class="form-group">
                <input type="text" name="codeEQ" class="form-control" placeholder="保险单号"/>
              </div>

              <div class="form-group input-inline">
                <input class="Wdate form-control" type="text"
                       onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',readOnly:true})" name="createdTimeGTE" value="" placeholder="创建时间起"/>
              </div>
              <div class="form-group input-inline">
                <input class="Wdate form-control" type="text" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',readOnly:true})"
                       name="createdTimeLT" value="" placeholder="创建时间止"/>
              </div>

              <div class="form-group">
                <select name="policyStatusEQ" class="form-control">
                  <option value="">-- 保险进度状态 --</option>
                  <option value="0">审核中</option>
                  <option value="1">已生效</option>
                  <option value="2">未通过</option>
                  <option value="3">已到期</option>
                </select>
              </div>

              <div class="form-group">
                <button class="btn blue filter-submit">
                  <i class="fa fa-search"></i> 查询
                </button>
              </div>

              <shiro:hasPermission name="policy:export">
                <div class="form-group">
                  <button type="button" class="btn yellow" onClick="policyExport()">
                    <i class="fa fa-file-excel-o"></i> 导出Excel
                  </button>
                </div>
              </shiro:hasPermission>

              <shiro:hasPermission name="policy:modify">
              <div class="form-group">
                <button type="button" class="btn green" id="modifyValidTimeBtn">
                  <i class="fa fa-send-o"></i> 设置生效时间
                </button>
              </div>
              <div class="form-group">
                <button type="button" class="btn grey" id="failBtn">
                  <i class="fa fa-send-o"></i> 审核失败
                </button>
              </div>
              </shiro:hasPermission>

            </form>
          </div>

          <table class="table table-striped table-bordered table-hover" id="dataTable">
          </table>
        </div>

      </div>
    </div>
    <!-- END ALERTS PORTLET-->
  </div>
</div>