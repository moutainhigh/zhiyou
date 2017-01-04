<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/view/include/head.jsp" %>
<!-- BEGIN JAVASCRIPTS -->
<script id="deliverTmpl" type="text/x-handlebars-template">
  <form id="deliverForm{{id}}" action="" data-action="" class="form-horizontal" method="post" style="width: 95%; margin: 10px;">
    <input type="hidden" name="id" value="{{id}}"/>
    <div class="form-body">
      <div class="alert alert-danger display-hide">
        <i class="fa fa-exclamation-circle"></i>
        <button class="close" data-close="alert"></button>
        <span class="form-errors">您填写的信息有误，请检查。</span>
      </div>
      <div class="form-group">
        <label class="control-label col-md-3">物流公司<span class="required"> * </span>
        </label>
        <div class="col-md-5">
          <input type="text" name="name" class="form-control" value="" />
        </div>
      </div>
      <div class="form-group">
        <label class="control-label col-md-3">物流单号<span class="required"> * </span>
        </label>
        <div class="col-md-5">
          <input type="text" name="sn" class="form-control" value="" />
        </div>
      </div>
      <div class="form-group">
        <label class="control-label col-md-3">物流费<span class="required"> * </span>
        </label>
        <div class="col-md-5">
          <input type="text" name="fee" class="form-control" value="" />
        </div>
      </div>
    </div>
    <div class="form-actions fluid">
      <div class="col-md-offset-3 col-md-9">
        <button id="deliverSubmit{{id}}" type="button" class="btn green">
          <i class="fa fa-save"></i> 保存
        </button>
        <button id="deliverCancel{{id}}" class="btn default" data-href="">
          <i class="fa fa-chevron-left"></i> 返回
        </button>
      </div>
    </div>
  </form>
</script>

<script id="remarkImpl" type="text/x-handlebars-template">
  <form id="remarkForm{{id}}" action="" data-action="" class="form-horizontal" method="post" style="width: 95%; margin: 10px;">
    <input type="hidden" name="id" value="{{id}}"/>
    <div class="form-body">
      <div class="alert alert-danger display-hide">
        <i class="fa fa-exclamation-circle"></i>
        <button class="close" data-close="alert"></button>
        <span class="form-errors">您填写的信息有误，请检查。</span>
      </div>
      <div class="form-group">
        <label class="control-label col-md-4">驳回备注 <span class="required"> * </span>
        </label>
        <div class="col-md-6">
          <textarea type="text" class="form-control" style="height : 130px !important;" name="remark"></textarea>
        </div>
      </div>
    </div>
    <div class="form-actions fluid">
      <div class="col-md-offset-5 col-md-7">
        <button id="remarkSubmit{{id}}" type="button" class="btn green">
          <i class="fa fa-save"></i> 保存
        </button>
        <button id="remarkCancel{{id}}" class="btn default" data-href="">
          <i class="fa fa-chevron-left"></i> 取消
        </button>
      </div>
    </div>
  </form>
</script>

<script>

  $(function () {

	  var remarkTemplate = Handlebars.compile($('#remarkImpl').html());
	  $('#dataTable').on('click', '.reject-remark', function () {
		  var id = $(this).data('id');
		  var data = {
			  id: id
		  };
		  var html = remarkTemplate(data);
		  var index = layer.open({
			  type: 1,
			  //skin: 'layui-layer-rim', //加上边框
			  area: ['600px', '260px'], //宽高
			  content: html
		  });

		  $form = $('#remarkForm' + id);
		  $form.validate({
			  rules: {
				  'remark': {
					  required: true
				  }
			  },
			  messages: {}
		  });

		  $('#remarkSubmit' + id).bind('click', function () {
			  var result = $form.validate().form();
			  if (result) {
				  var url = '${ctx}/productReplacement/reject';
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

		  $('#remarkCancel' + id).bind('click', function () {
			  layer.close(index);
		  })

	  });

	  var template = Handlebars.compile($('#deliverTmpl').html());
	  $('#dataTable').on('click', '.deliver-confirm', function () {
		  var id = $(this).data('id');

		  var data = {
			  id: id
		  };
		  var html = template(data);
		  var index = layer.open({
			  type: 1,
			  //skin: 'layui-layer-rim', //加上边框
			  area: ['600px', '400px'], //宽高
			  content: html
		  });

		  $form = $('#deliverForm' + id);
		  $form.validate({
			  rules: {
				  'name': {
					  required: true
				  },
				  'sn': {
					  required: true
          },
          'fee': {
	          required: true
          }
			  },
			  messages: {}
		  });

		  $('#deliverSubmit' + id).bind('click', function () {
			  var result = $form.validate().form();
			  if (result) {
				  var url = url = '${ctx}/productReplacement/deliver';
				  $.post(url, $form.serialize(), function (data) {
					  if (data.code === 0) {
						  layer.close(index);
						  grid.getDataTable().ajax.reload(null, false);
						  layer.alert('发货成功');
					  } else {
						  layer.alert('发货失败' + data.message);
					  }
				  });
			  }
		  })

		  $('#deliverCancel' + id).bind('click', function () {
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
        lengthMenu: [[10, 20, 50, 100], [10, 20, 50, 100]],// change per page values here
        pageLength: 20, // default record count per page
        order: [], // set first column as a default sort by desc
        ajax: {
          url: '${ctx}/productReplacement', // ajax source
        },
        columns: [
          {
            data: '',
            title: '更换商品',
            orderable: false,
            render: function (data, type, full) {
              return full.fromProduct + ' 更换 ' + full.toProduct;
            }
          },
          {
            data: 'productReplacementStatus',
            title: '状态',
            orderable: false,
            render: function (data, type, full) {
              return '<label class="label label-' + full.productReplacementStatusStyle + '">' + data + '</label>';
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
            data: '',
            title: '收件人信息',
            orderable: false,
	          render: function (data, type, full) {
		          return '<p>' + full.receiverProvince + '-' + full.receiverCity + '-' + full.receiverDistrict + '</p>' +
                '<p>' + full.receiverAddress + '</p>' +
                '<p>' + full.receiverRealname + '</p>' +
                '<p>' + full.receiverPhone + '</p>';
	          }
          },
          {
            data: 'createdTimeLabel',
            title: '更换时间',
            orderable: false
          },
          {
            data: '',
            title: '物流信息',
            orderable: false,
            render: function (data, type, full) {
              if(full.logisticsName){
                return '<p>物流公司：' + full.logisticsName + '</p><p>物流单号：' + full.logisticsSn + '</p><p>物流费：' + full.logisticsFee + '</p>';
              } else {
                return '-';
              }
            }
          },
          {
            data: 'remark',
            title: '备注',
            orderable: false
          },
          {
            data: 'id',
            title: '操作',
            orderable: false,
            render: function (data, type, full) {
              var optionHtml = '';
              if (full.productReplacementStatus == '已申请') {
	              <shiro:hasPermission name="productReplacement:deliver">
                optionHtml += '<a class="btn btn-xs default green-stripe deliver-confirm" href="javascript:;" data-id="' + full.id + '"><i class="fa fa-car"></i> 发货 </a>';
	              </shiro:hasPermission>
	              <shiro:hasPermission name="productReplacement:reject">
	              optionHtml += '<a class="btn btn-xs default green-stripe reject-remark" href="javascript:;" data-id="' + full.id + '"><i class="fa fa-times"></i> 驳回 </a>';
	              </shiro:hasPermission>
              }
              return optionHtml;
            }
          }]
      }
    });

  });

</script>

<!-- BEGIN PAGE HEADER-->
<div class="page-bar">
  <ul class="page-breadcrumb">
    <li><i class="fa fa-home"></i> <a href="javascript:;" data-href="${ctx}/main">首页</a> <i class="fa fa-angle-right"></i></li>
    <li><a href="javascript:;" data-href="${ctx}/productReplacement">换货管理</a></li>
  </ul>
</div>
<!-- END PAGE HEADER-->

<div class="row">
  <div class="col-md-12">
    <!-- BEGIN ALERTS PORTLET-->
    <div class="portlet light bordered">
      <div class="portlet-title">
        <div class="caption">
          <i class="icon-vector"></i><span>换货管理 </span>
        </div>
        <div class="tools"></div>
      </div>

      <div class="portlet-body clearfix">
        <div class="table-container">
          <div class="table-toolbar">
            <form class="filter-form form-inline" id="searchForm">
              <input id="_orderBy" name="orderBy" type="hidden" value=""/>
              <input id="_direction" name="direction" type="hidden" value=""/>
              <input id="_pageNumber" name="pageNumber" type="hidden" value="0"/>
              <input id="_pageSize" name="pageSize" type="hidden" value="20"/>

              <div class="form-group">
                <input type="text" name="userPhoneEQ" class="form-control" placeholder="用户手机号"/>
              </div>

              <div class="form-group">
                <input type="text" name="userNicknameLK" class="form-control" placeholder="用户昵称"/>
              </div>


              <div class="form-group">
                <input type="text" name="logisticsSnLK" class="form-control" placeholder="物流单号"/>
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
