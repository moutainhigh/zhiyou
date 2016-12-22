<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/view/include/head.jsp" %>

<!-- BEGIN JAVASCRIPTS -->
<script id="addUserTmpl" type="text/x-handlebars-template">
  <form id="addUserForm" action="" data-action="" class="form-horizontal" method="post" style="width: 95%; margin: 10px;">
    <div class="form-body">
      <div class="alert alert-danger display-hide">
        <i class="fa fa-exclamation-circle"></i>
        <button class="close" data-close="alert"></button>
        <span class="form-errors">您填写的信息有误，请检查。</span>
      </div>
      <div class="form-group">
        <label class="control-label col-md-2">手机号<span class="required"> * </span></label>
        <div class="col-md-6">
          <input type="text" name="phone" id="phone" class="form-control"/>
        </div>
      </div>
      <div class="form-group">
        <label class="control-label col-md-2">备注<span class="required"> * </span>
        </label>
        <div class="col-md-6">
          <textarea type="text" class="form-control" name="remark"></textarea>
        </div>
      </div>
    </div>
    <div class="form-actions fluid">
      <div class="col-md-offset-3 col-md-7">
        <button id="addUserSubmit" type="button" class="btn green">
          <i class="fa fa-save"></i> 保存
        </button>
        <button id="addUserCancel" class="btn default" data-href="">
          <i class="fa fa-chevron-left"></i> 返回
        </button>
      </div>
    </div>
  </form>
</script>

<script>

  $(function () {

	  var template = Handlebars.compile($('#addUserTmpl').html());
	  $('.portlet').on('click', '.btn-create', function () {
		  var html = template();
		  var index = layer.open({
			  type: 1,
			  //skin: 'layui-layer-rim', //加上边框
			  area: ['600px', '360px'], //宽高
			  content: html
		  });

		  $form = $('#addUserForm');
		  $form.validate({
			  rules: {
				  'phone': {
					  required: true,
					  remote: {
						  url: '${ctx}/orderFillUser/checkUserPhone',
						  type: 'post',
						  data: {
							  phone: function() {
								  return $("#phone").val();
							  }
						  }
					  }
				  },
          'remark': {
	          required: true
          }
			  },
			  messages: {
				  phone: {
					  required : '请填写手机号',
					  remote: '该手机号已经存在'
				  }
			  }
		  });

		  $('#addUserSubmit').bind('click', function () {
			  var result = $form.validate().form();
			  if (result) {
				  var url = '${ctx}/orderFillUser/create';
				  $.post(url, $form.serialize(), function (data) {
					  if (data.code === 0) {
						  layer.close(index);
						  grid.getDataTable().ajax.reload(null, false);
					  } else {
						  layer.alert(data.message);
					  }
				  });
			  }
		  })

		  $('#addUserCancel').bind('click', function () {
			  layer.close(index);
		  })

	  });

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
        lengthMenu : [ [ 10, 20, 50, 100 ], [ 10, 20, 50, 100 ] ],// change per page values here
        pageLength: 20, // default record count per page
        order: [], // set first column as a default sort by desc
        ajax: {
          url: '${ctx}/orderFillUser', // ajax source
        },
        columns: [
          {
            data: '',
            title: '用户信息',
            render: function (data, type, full) {
              return formatUser(full.user);
            }
          },
          {
            data: 'remark',
            title: '备注'
          },
          {
            data: '',
            title: '操作',
            orderable: false,
            render: function (data, type, full) {
              return '<a class="btn btn-xs default red-stripe" href="javascript:;" data-href="${ctx}/orderFillUser/delete?id=' + full.id
                + '" data-confirm="您确定要删除选中数据吗?"><i class="fa fa-trash-o"></i> 删除 </a>';
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
    <li><a href="javascript:;" data-href="${ctx}/orderFillUser">用户补单管理</a></li>
  </ul>
</div>
<!-- END PAGE HEADER-->

<div class="row">
  <div class="col-md-12">
    <!-- BEGIN ALERTS PORTLET-->
    <div class="portlet light bordered">
      <div class="portlet-title">
        <div class="caption">
          <i class="icon-user"></i><span> 用户补单管理 </span>
        </div>
        <div class="actions">
          <a class="btn btn-circle green btn-create"> <i class="fa fa-plus"></i> 新增
          </a>
        </div>
      </div>
      <div class="portlet-body clearfix">
        <div class="table-container">
          <div class="table-toolbar">
            <form class="filter-form form-inline">
              <input id="_orderBy" name="orderBy" type="hidden" value=""/>
              <input id="_direction" name="direction" type="hidden" value=""/>
              <input id="_pageNumber" name="pageNumber" type="hidden" value="0"/>
              <input id="_pageSize" name="pageSize" type="hidden" value="20"/>

              <div class="form-group">
                <input type="text" name="userPhoneEQ" class="form-control" placeholder="手机"/>
              </div>
              <div class="form-group">
                <input type="text" name="userNicknameLK" class="form-control" placeholder="昵称"/>
              </div>
              <div class="form-group">
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