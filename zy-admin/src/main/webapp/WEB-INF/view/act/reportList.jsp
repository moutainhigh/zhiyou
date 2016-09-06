<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/view/include/head.jsp" %>
<style>
  img {
    cursor: pointer;
  }
</style>
<!-- BEGIN JAVASCRIPTS -->
<script id="confirmTmpl" type="text/x-handlebars-template">
  <form id="confirmForm{{id}}" action="" data-action="${ctx}/banner/create" class="form-horizontal" method="post">
    <input type="hidden" name="id" value="{{id}}"/>
    <div class="form-body">
      <div class="alert alert-danger display-hide">
        <i class="fa fa-exclamation-circle"></i>
        <button class="close" data-close="alert"></button>
        <span class="form-errors">您填写的信息有误，请检查。</span>
      </div>
      <div class="form-group">
        <label class="control-label col-md-2">审核结果<span class="required"> * </span></label>
        <div class="col-md-5">
          <select name="isSuccess" class="form-control">
            <option value="">--请选择--</option>
            <option value="true">通过</option>
            <option value="false">拒绝</option>
          </select>
        </div>
      </div>
      <div class="form-group">
        <label class="control-label col-md-2">审核备注<span class="required"> * </span>
        </label>
        <div class="col-md-5">
          <textarea type="text" class="form-control" name="confirmRemark"></textarea>
        </div>
      </div>
    </div>
    <div class="form-actions fluid">
      <div class="col-md-offset-3 col-md-9">
        <button id="reportConfirmSubmit{{id}}" type="button" class="btn green">
          <i class="fa fa-save"></i> 保存
        </button>
        <button id="reportConfirmCancel{{id}}" class="btn default" data-href="${ctx}/banner">
          <i class="fa fa-chevron-left"></i> 返回
        </button>
      </div>
    </div>
  </form>
</script>
<script>

  $(function () {

    var grid = new Datatable();

    var template = Handlebars.compile($('#confirmTmpl').html());

    $('#dataTable').on('click', '.report-confirm', function () {
      var id = $(this).data('id');
      var data = {
        id: id
      };
      var html = template(data);
      var index = layer.open({
        type: 1,
        //skin: 'layui-layer-rim', //加上边框
        area: ['600px', '360px'], //宽高
        content: html
      });

      $form = $('#confirmForm' + id);
      $form.validate({
        rules: {
          isSuccess: {
            required: true
          },
          confirmRemark: {}
        },
        messages: {}
      });

      $('#reportConfirmSubmit' + id).bind('click', function () {
        var result = $form.validate().form();
        if (result) {
          $.post('${ctx}/report/confirm', $form.serialize(), function (data) {
            if (data.code === 0) {
              layer.close(index);
              grid.getDataTable().ajax.reload(null, false);
            } else {
              layer.alert('审核失败,原因' + data.message);
            }
          });
        }
      })

      $('#reportConfirmCancel' + id).bind('click', function () {
        layer.close(index);
      })

    });


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
        lengthMenu: [[10, 20, 50, 100, -1], [10, 20, 50, 100, 'All'] // change per page values here
        ],
        pageLength: 20, // default record count per page
        order: [], // set first column as a default sort by desc
        ajax: {
          url: '${ctx}/report', // ajax source
        },
        columns: [
          {
            data: '',
            title: '用户信息',
            render: function (data, type, full) {
              return '<p>姓名: ' + full.realname + '</p><p>性别: ' + full.gender + '</p><p>年龄: ' + full.age + '</p>';
            }
          },
          {
            data: 'text',
            title: '评论',
            orderable: false
          },
          {
            data: 'reportResult',
            title: '检测结果',
            orderable: false
          },
          {
            data: 'image1Thumbnail',
            title: '图片1',
            orderable: false,
            render: function (data, type, full) {
              return '<img class="imagescan" data-url="' + full.image1Big + '" src="' + data + '" style="width: 80px; height: 80px;">';
            }
          },
          {
            data: 'image2Thumbnail',
            title: '图片2',
            orderable: false,
            render: function (data, type, full) {
              return '<img class="imagescan" data-url="' + full.image2Big + '" src="' + data + '" style="width: 80px; height: 80px;">';
            }
          },
          {
            data: 'image3Thumbnail',
            title: '图片3',
            orderable: false,
            render: function (data, type, full) {
              return '<img class="imagescan" data-url="' + full.image3Big + '" src="' + data + '" style="width: 80px; height: 80px;">';
            }
          },
          /*{
            data: 'image4Thumbnail',
            title: '图片4',
            orderable: false,
            width: '100px',
            render: function (data, type, full) {
              return '<img class="imagescan" data-url="' + data + '" src="' + data + '" style="width: 80px; height: 80px;">';
            }
          },
          {
            data: 'image5Thumbnail',
            title: '图片5',
            orderable: false,
            width: '100px',
            render: function (data, type, full) {
              return '<img class="imagescan" data-url="' + data + '" src="' + data + '" style="width: 80px; height: 80px;">';
            }
          },
          {
            data: 'image6Thumbnail',
            title: '图片6',
            orderable: false,
            width: '100px',
            render: function (data, type, full) {
              return '<img class="imagescan" data-url="' + data + '" src="' + data + '" style="width: 80px; height: 80px;">';
            }
          },*/
          {
            data: 'confirmStatus',
            title: '审核状态',
            orderable: false,
            render: function (data, type, full) {
              var result = '';
              if (data == '未审核') {
                result = '<label class="label label-danger">未审核</label>';
              } else if (data == '审核通过') {
                result = '<label class="label label-success">审核通过</label>';
              } else if (data == '审核未通过') {
                result = '<label class="label label-default">审核未通过</label>';
              }
              return result;
            }
          },
          {
            data: 'confirmRemark',
            title: '审核备注',
            orderable: false
          },
          {
            data: 'confirmedTime',
            title: '审核时间',
            orderable: false
          },
          {
            data: 'isSettledUp',
            title: '已结算',
            orderable: false,
            render: function (data, type, full) {
              if (data) {
                return '<i class="fa fa-check font-green"></i>';
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
              <shiro:hasPermission name="report:confirm">
              if (full.confirmStatus == '未审核') {
                optionHtml += '<a class="btn btn-xs default yellow-stripe report-confirm" href="javascript:;" data-id="' + full.id + '"><i class="fa fa-edit"></i> 审核 </a>';
              }
              </shiro:hasPermission>
              return optionHtml;
            }
          }
        ]
      }
    });
    $('#dataTable').on('click', '.imagescan', function () {
      var url = $(this).attr('data-url');
      $.imagescan({
        url: url
      });
    });
  });

</script>
<!-- END JAVASCRIPTS -->


<!-- BEGIN PAGE HEADER-->
<div class="page-bar">
  <ul class="page-breadcrumb">
    <li><i class="fa fa-home"></i> <a href="javascript:;" data-href="${ctx}/main">首页</a> <i class="fa fa-angle-right"></i></li>
    <li><a href="javascript:;" data-href="${ctx}/report">检测报告</a></li>
  </ul>
</div>
<!-- END PAGE HEADER-->

<div class="row">
  <div class="col-md-12">
    <!-- BEGIN ALERTS PORTLET-->
    <div class="portlet light bordered">
      <div class="portlet-title">
        <div class="caption">
          <i class="icon-volume-1"></i> 检测报告
        </div>
      </div>
      <div class="portlet-body clearfix">
        <div class="table-container">
          <div class="table-toolbar">
            <form class="filter-form form-inline">
              <input id="_orderBy" name="orderBy" type="hidden" value=""/> <input id="_direction" name="direction" type="hidden" value=""/>
              <input id="_pageNumber" name="pageNumber" type="hidden" value="0"/> <input id="_pageSize" name="pageSize" type="hidden" value="20"/>

              <div class="form-group">
                <input type="text" name="realnameLK" class="form-control" placeholder="姓名"/>
              </div>

              <div class="form-group">
                <select name="confirmStatusEQ" class="form-control">
                  <option value="">-- 审核状态 --</option>
                  <c:forEach items="${confirmStatus}" var="confirmStatus">
                    <option value="${confirmStatus}">${confirmStatus}</option>
                  </c:forEach>
                </select>
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