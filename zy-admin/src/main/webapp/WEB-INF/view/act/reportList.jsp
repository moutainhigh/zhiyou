<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/view/include/head.jsp" %>
<style>
  .imagescan {
    cursor: pointer;
    width: 80px;
    height: 80px;
  }

  .mr-10 {
    margin-left: 10px;
  }

  .text {
    width: 320px;
    height: 100px;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
    cursor: pointer;
  }
</style>
<!-- BEGIN JAVASCRIPTS -->
<script id="confirmTmpl" type="text/x-handlebars-template">
  <form id="confirmForm{{id}}" action="" data-action="" class="form-horizontal" method="post" style="width: 95%; margin: 10px;">
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
        <button id="reportConfirmCancel{{id}}" class="btn default" data-href="">
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
      var step = $(this).data('step');
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
          var url = '';
          if (step == 1) {
            url = '${ctx}/report/preConfirm';
          } else {
            url = '${ctx}/report/confirm';
          }
          $.post(url, $form.serialize(), function (data) {
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
        lengthMenu: [[10, 20, 50, 100], [10, 20, 50, 100]],// change per page values here
        pageLength: 20, // default record count per page
        order: [], // set first column as a default sort by desc
        ajax: {
          url: '${ctx}/report', // ajax source
        },
        columns: [
          {
            data: 'id',
            title: '编号'
          },
          {
            data: '',
            title: '服务商信息',
            orderable: false,
            render: function (data, type, full) {
              return formatUser(full.user);
            }
          },
          {
			data: '',
          	title: '商品',
          	orderable: false,
          	width: '120px',
          	render: function (data, type, full) {
          	  return full.product ? '<img src="' + full.product.image1Thumbnail + '" style="width: 80px; height: 80px;"><div>' + full.product.title + '</div>' : '无产品信息';
          	}
          },
          {
            data: '',
            title: '客户信息',
            orderable: false,
            render: function (data, type, full) {
              return '<p>姓名: ' + full.realname + '</p><p>性别: ' + full.gender + '</p><p>年龄: ' + full.age + '</p>'
                + '<p>地区:' + full.province + '-' + full.city + '-' + full.district + '</p>';
            }
          },
          {
            data: 'phone',
            title: '客户手机',
            orderable: false
          },
          {
            data: 'appliedTimeLabel',
            title: '申请时间',
            orderable: false
          },
          {
            data: 'reportResult',
            title: '检测结果',
            orderable: false
          },
          {
            data: '',
            title: '图片',
            orderable: false,
            width: '200px',
            render: function (data, type, full) {
              var html = '<span class="layer-phote-' + full.id + '">';
              var imageBigs = full.imageBigs;
              var imageThumbnails = full.imageThumbnails;
              for (var i = 0; i < imageThumbnails.length; i++) {
                html += '<img class="imagescan mr-10" layer-src="' + imageBigs[i] + '" src="' + imageThumbnails[i] + '" >';
              }
              return html + '</span>';
            }
          },
          {
            data: 'text',
            title: '客户检测',
            orderable: false,
            render: function (data, type, full) {
              return '<p>第' + full.times + '次检测</p><p>检测: ' + full.reportedDateLabel + '</p><p>心得: <div class="text" style="width: 200px;" title=' + data + '>' + data + '</div></p>';
            }
          },
          {
            data: 'preConfirmStatus',
            title: '审核状态',
            orderable: false,
            width: '120px',
            render: function (data, type, full) {
              var result = '<p>初审: ';
              if (data == '待审核') {
                result += '<label class="label label-danger">' + data + '</label>';
              } else if (data == '已通过') {
                result += '<label class="label label-success">' + data + '</label>';
              } else if (data == '未通过') {
                result += '<label class="label label-default">' + data + '</label>';
              }
              var confirmStatus = full.confirmStatus;
              result += '</p><p>终审: ';
              if (confirmStatus == '待审核') {
                result += '<label class="label label-danger">' + confirmStatus + '</label>';
              } else if (confirmStatus == '已通过') {
                result += '<label class="label label-success">' + confirmStatus + '</label>';
              } else if (confirmStatus == '未通过') {
                result += '<label class="label label-default">' + confirmStatus + '</label>';
              }
              return result + '</p>';
            }
          },
          {
            data: 'preConfirmedTimeLabel',
            title: '审核时间',
            orderable: false,
            render: function (data, type, full) {
              var preConfirmedTimeLabel = data == null ? '-' : data;
              var confirmedTimeLabel = full.confirmedTimeLabel == null ? '-' : full.confirmedTimeLabel;
              return '<p>初审: ' + preConfirmedTimeLabel + '</p><p>终审: ' + confirmedTimeLabel + '</p>';
            }
          },
          {
            data: 'confirmRemark',
            title: '审核备注',
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
              <shiro:hasPermission name="report:edit">
              optionHtml += '<a class="btn btn-xs default blue-stripe" href="javascript:;" data-href="${ctx}/report/update?id=' + full.id + '"><i class="fa fa-edit"></i> 编辑</a>';
              </shiro:hasPermission>
              <shiro:hasPermission name="report:preConfirm">
              if (full.preConfirmStatus == '待审核') {
                optionHtml += '<a class="btn btn-xs default yellow-stripe report-confirm" href="javascript:;" data-step="1" data-id="' + full.id + '"><i class="fa fa-edit"></i> 预审核 </a>';
              }
              </shiro:hasPermission>
              <shiro:hasPermission name="report:confirm">
              if (full.preConfirmStatus == '已通过' && full.confirmStatus == '待审核') {
                optionHtml += '<a class="btn btn-xs default yellow-stripe report-confirm" href="javascript:;" data-step="2" data-id="' + full.id + '"><i class="fa fa-edit"></i> 审核 </a>';
              }
              </shiro:hasPermission>
              return optionHtml;
            }
          }
        ]
      }
    });

    $('#dataTable').on('click', '.imagescan', function () {
      var $this = $(this);
      layer.photos({
        photos: '.' + $this.parent().attr('class')
      });
    });

    $('#dataTable').on('click', '.text', function () {
      var text = $(this).text();
      if (text != '') {
        layer.alert($(this).text());
      }
    })

  });
  <shiro:hasPermission name="report:export">
  function reportExport() {
    location.href = '${ctx}/report/export?' + $('#searchForm').serialize();
  }
  </shiro:hasPermission>
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
        <shiro:hasPermission name="report:edit">
          <div class="actions">
            <a class="btn btn-circle green" data-href="${ctx}/report/create">
              <i class="fa fa-plus"></i> 新增
            </a>
          </div>
        </shiro:hasPermission>
      </div>
      <div class="portlet-body clearfix">
        <div class="table-container">
          <div class="table-toolbar">
            <form id="searchForm" class="filter-form form-inline">
              <input id="_orderBy" name="orderBy" type="hidden" value=""/> <input id="_direction" name="direction" type="hidden" value=""/>
              <input id="_pageNumber" name="pageNumber" type="hidden" value="0"/> <input id="_pageSize" name="pageSize" type="hidden" value="20"/>

              <div class="form-group">
                <input type="text" name="idEQ" class="form-control" placeholder="编号"/>
              </div>

              <div class="form-group">
                <input type="text" name="realnameLK" class="form-control" placeholder="姓名"/>
              </div>

              <div class="form-group">
                <input type="text" name="phoneEQ" class="form-control" placeholder="手机号"/>
              </div>

              <div class="form-group">
                <select name="reportResultEQ" class="form-control">
                  <option value="">-- 检测结果 --</option>
                  <c:forEach items="${reportResults}" var="reportResult">
                    <option value="${reportResult}">${reportResult}</option>
                  </c:forEach>
                </select>
              </div>

              <div class="form-group">
                <select name="preConfirmStatusEQ" class="form-control">
                  <option value="">-- 初审状态 --</option>
                  <c:forEach items="${confirmStatus}" var="confirmStatus">
                    <option value="${confirmStatus}">${confirmStatus}</option>
                  </c:forEach>
                </select>
              </div>


              <div class="form-group input-inline">
                <input class="Wdate form-control" type="text"
                       onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',readOnly:true})" name="preConfirmedTimeGTE" value="" placeholder="初审时间起"/>
              </div>
              <div class="form-group input-inline">
                <input class="Wdate form-control" type="text" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',readOnly:true})"
                       name="preConfirmedTimeLT" value="" placeholder="初审时间止"/>
              </div>

              <div class="form-group">
                <select name="confirmStatusEQ" class="form-control">
                  <option value="">-- 终审状态 --</option>
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
              <shiro:hasPermission name="report:export">
                <div class="form-group">
                  <button type="button" class="btn yellow" onClick="reportExport()">
                    <i class="fa fa-file-excel-o"></i> 导出Excel
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