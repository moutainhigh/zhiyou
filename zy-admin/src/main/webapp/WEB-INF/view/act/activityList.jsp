<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/view/include/head.jsp" %>

<!-- BEGIN JAVASCRIPTS -->
<script id="addApplyImpl" type="text/x-handlebars-template">
  <form id="addApplyForm{{id}}" action="" data-action="" class="form-horizontal" method="post" style="width: 95%; margin: 10px;">
    <input type="hidden" name="activityId" value="{{id}}"/>
    <div class="form-body">
      <div class="alert alert-danger display-hide">
        <i class="fa fa-exclamation-circle"></i>
        <button class="close" data-close="alert"></button>
        <span class="form-errors">您填写的信息有误，请检查。</span>
      </div>
      <div class="form-group">
        <label class="control-label col-md-2">手机<span class="required"> * </span></label>
        <div class="col-md-10">
          <textarea name="phone" class="form-control" rows="10" placeholder="请输入手机号"></textarea>
        </div>
      </div>

    </div>
    <div class="form-actions fluid">
      <div class="col-md-offset-3 col-md-9">
        <button id="addApplySubmit{{id}}" type="button" class="btn green">
          <i class="fa fa-save"></i> 保存
        </button>
        <button id="addApplyCancel{{id}}" class="btn default" data-href="">
          <i class="fa fa-chevron-left"></i> 返回
        </button>
      </div>
    </div>
  </form>
</script>

<script>
  var grid = new Datatable();

  var addApplyTemplate = Handlebars.compile($('#addApplyImpl').html());
  $('#dataTable').on('click', '.btn-add-apply', function () {
	  var id = $(this).data('id');
	  var data = {
		  id: id
	  };
	  var html = addApplyTemplate(data);
	  var index = layer.open({
		  type: 1,
		  //skin: 'layui-layer-rim', //加上边框
		  area: ['600px', '380px'], //宽高
		  content: html
	  });

	  $form = $('#addApplyForm' + id);
	  $form.validate({
		  rules: {
			  'phone': {
				  required: true
			  }
		  },
		  messages: {}
	  });

	  $('#addApplySubmit' + id).bind('click', function () {
		  var result = $form.validate().form();
		  if (result) {
			  var url = '${ctx}/activityApply/create';
			  $.post(url, $form.serialize(), function (data) {
				  if (data.code === 0) {
					  layer.alert('操作成功');
					  layer.close(index);
					  grid.getDataTable().ajax.reload(null, false);
				  } else {
					  layer.alert('操作失败,原因' + data.message);
				  }
			  });
		  }
	  })

	  $('#addApplyCancel' + id).bind('click', function () {
		  layer.close(index);
	  })

  });

  $(function () {
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
        order: [], // set first column as a default sort by desc
        ajax: {
          url: '${ctx}/activity', // ajax source
        },
        columns: [
          {
            data: 'image',
            title: '主图',
            orderable: false,
            render: function (data, type, full) {
              return '<a target="_blank" href="' + data + '"><img style="width:180px;height:80px;"  src="' + full.imageThumbnail + '"/></a>';
            }
          },
          {
            data: 'title',
            title: '标题',
            orderable: false
          },
	        {
		        data: 'amountLabel',
		        title: '报名费',
                orderable: false
	        },
	        {
		        data: 'ticketType',
                orderable: false,
		        title: '活动票务类型',
              render: function (data, type, full) {
                if(full.ticketType == 1){
                  return "自购";
                }else if(full.ticketType == 2){
                  return "团购";
                }
              }
	        },
	        {
		        data: 'level',
		        title: '自购权限',
                orderable: false,
              render: function (data, type, full) {
                if(full.level == 4){
                  return "特级服务商";
                }else if(full.level == 3){
                  return "省级服务商";
                }else if(full.level == 2){
                  return "市级服务商";
                }else if(full.level == 1){
                  return "VIP";
                }else if(full.level == 0){
                  return "无权限";
                }
              }
	        },
          {
            data: 'areaId',
            title: '详细地址',
            orderable: false,
            render: function (data, type, full) {
              return '<p>' + full.province + '-' + full.city + '-' + full.district + '</p>'
                + '<p class="small">' + full.address + '</p>';
            }
          },
          {
            data: 'startTime',
            title: '开始时间',
            render: function (data, type, full) {
              return full.startTimeLabel;
            }
          },
          {
            data: 'endTime',
            title: '结束时间',
            render: function (data, type, full) {
              return full.endTimeLabel;
            }
          },
          {
            data: 'applyDeadline',
            title: '报名截止时间',
            render: function (data, type, full) {
              return full.applyDeadlineLabel;
            }
          },
          {
            data: 'viewedCount',
            title: '浏览数'
          },
          {
            data: 'appliedCount',
            title: '报名数',
            width: '60px'
          },
          {
            data: 'maxCount',
            title: '活动限制人数',
            orderable: false
          },
          {
            data: 'signedInCount',
            title: '签到数'
          },
          {
            data: 'collectedCount',
            title: '关注数'
          },
          {
            data: 'isReleased',
            title: '是否上架',
            orderable: false,
            render: function (data, type, full) {
              if (data) {
                return '<i class="fa fa-check font-green"></i> <span class="badge badge-success"> 已上架 </span>';
              }
              return '';
            }
          },
          {
            data: 'id',
            title: '操作',
            orderable: false,
            render: function (data, type, full) {
              var optionHtml = '';
              optionHtml += '<a class="btn btn-xs default green-stripe" href="javascript:;" data-href="${ctx}/activity/detail?id=' + data + '"><i class="fa fa-search"></i> 查看 </a>';
              optionHtml += '<a class="btn btn-xs default yellow-stripe" href="javascript:;" data-href="${ctx}/activity/update?id=' + data + '"><i class="fa fa-edit"></i> 编辑 </a>';
              if (full.isReleased) {
                optionHtml += '<a class="btn btn-xs default red-stripe" href="javascript:;" data-href="${ctx}/activity/release?id=' + data + '&isReleased=false"><i class="fa fa-times"></i> 下架 </a>';
              } else {
                optionHtml += '<a class="btn btn-xs default green-stripe" href="javascript:;" data-href="${ctx}/activity/release?id=' + data + '&isReleased=true"><i class="fa fa-check"></i> 上架 </a>';
              }
              <shiro:hasPermission name="activityApply:edit">
              if (full.status != '活动已结束') {
	              optionHtml += '<a class="btn btn-xs default green-stripe btn-add-apply" href="javascript:;" data-id="' + full.id + '"><i class="fa fa-plus"></i> 免费报名 </a>';
              }
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
    <li><a href="javascript:;" data-href="${ctx}/activity">活动管理</a></li>
  </ul>
</div>
<!-- END PAGE HEADER-->

<div class="row">
  <div class="col-md-12">
    <!-- BEGIN ALERTS PORTLET-->
    <div class="portlet light bordered">
      <div class="portlet-title">
        <div class="caption">
          <i class="icon-social-dropbox"></i> 活动管理
        </div>
        <div class="actions">
          <a class="btn btn-circle green" data-href="${ctx}/activity/create">
            <i class="fa fa-plus"></i> 新增
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
                <input type="text" name="titleLK" class="form-control" placeholder="标题"/>
              </div>
              <div class="form-group">
                <select name="isReleasedEQ" class="form-control">
                  <option value="">-- 是否上架 --</option>
                  <option value="true">是</option>
                  <option value="false">否</option>
                </select>
              </div>

              <div class="form-group">
                <button class="btn blue filter-submit">
                  <i class="fa fa-search"></i> 查询
                </button>
              </div>

              <%-- <div class="form-group">
                 <div class="btn-group">
                   <button class="btn green" data-href="${ctx}/activity/create">
                     <i class="fa fa-plus"></i> 新增
                   </button>
                 </div>
               </div>--%>

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