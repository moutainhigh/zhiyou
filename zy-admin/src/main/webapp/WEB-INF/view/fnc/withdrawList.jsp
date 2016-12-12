<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/view/include/head.jsp" %>
<!-- BEGIN JAVASCRIPTS -->
<script>
  var grid = new Datatable();

  $(function () {
    grid
      .init({
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
            url: '${ctx}/withdraw', // ajax source
          },
          columns: [
            /*  {
             data: 'id',
             title: 'id'
             },*/
            {
              data: 'title',
              title: '提现信息',
              orderable: false,
              render: function (data, type, full) {
                return '<p>sn: ' + full.sn + '</p><p>标题：' + full.title + '</p>';
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
              title: '申请金额',
              orderable: false,
              render: function (data, type, full) {
                return '<p>总金额：' + full.amountLabel + '</p>'
                  + '<p> 手续费率：' + full.feeRate + '</p>'
                  + '<p>手续费：' + full.feeLabel + '</p>';
              }
            },
            {
              data: 'realAmountLabel',
              title: '实际到账',
              orderable: false
            },
            {
              data: 'createdTime',
              title: '创建时间',
              orderable: false
            },
            {
              data: 'withdrawStatus',
              title: '提现状态',
              orderable: false,
              render: function (data, type, full) {
                return '<label class="label label-' + full.withdrawStatusStyle + '">' + data + '</label>';
              }
            },
            {
              data: 'isToBankCard',
              title: '提现到',
              orderable: false,
              render: function (data, type, full) {
                return data ? '<i class="fa fa-credit-card"></i> 银行卡' : '<i class="fa fa-weixin "></i> 微信';
              }
            },
            {
              data: '',
              title: '银行卡信息',
              orderable: false,
              render: function (data, type, full) {
                if (full.isToBankCard) {
                  return '<p>开户名：' + full.bankCard.realname + '</p><p>开户行：' + full.bankCard.bankName + '</p><p>开户行支行：' + full.bankCard.bankBranchName + '</p>'
                    + '<p>卡号：' + full.bankCard.cardNumber + '</p>';
                }
                return '';
              }
            },

            {
              data: 'withdrawedTime',
              title: '提现时间',
              orderable: false
            },
            {
              data: 'remark',
              title: '备注',
              orderable: false
            },
            {
              data: '',
              title: '操作',
              orderable: false,
              render: function (data, type, full) {
                var operationHtml = '';
                <shiro:hasPermission name="withdraw:confirm">
                if (full.withdrawStatus == '已申请') {
                  operationHtml += '<p><a class="btn btn-xs default yellow-stripe" href="javascript:;" onclick="confirm(' + full.id
                    + ')"><i class="fa fa-edit"></i> 确认提现 </a></p>';
                }
                </shiro:hasPermission>
                //operationHtml += '<p><a class="btn btn-xs default yellow-stripe" href="javascript:;" data-href="${ctx}/transaction?phoneEQ='+full.phone+'&fromParent=true"><i class="fa fa-file-sound-o"></i> 查看流水 </a></p>';
                return operationHtml;
              }
            }]
        }
      });

  });
  var $confirmDialog;
  function confirm(id) {
    $confirmDialog = $.window({
      content: "<form action='' class='form-horizontal' style='margin-top: 20px;'>" + "<div class='form-body'>" + "<div class='form-group'>"
      + "<label class='control-label col-md-3'>提现操作:</label>" + "<div class='col-md-5'><select name='confirmSelect' class='form-control' style='width: 220px'>"
      + "<option value=''>请选择提现操作</option>" + "<option value='true'>提现成功</option>" + "<option value='false'>取消提现</option>" + "</select></div>" + "</div>"
      + "<div class='form-group'	>" + "<label class='control-label col-md-3'>备注:</label>"
      + "<div class='col-md-5'><textarea class='form-control' style='width: 220px;height: 120px;' id='confirmRemark'></textarea></div>" + "</div>" + "</div>"
      + "<div class='form-actions fluid'>" + "<div class='col-md-offset-3 col-md-9'>" + "<button type='button' class='btn green' onclick='submitBtn(" + id + ")'>"
      + "保存</button>" + "<button type='button' class='btn default' onclick='closeBtn()' style='margin-left: 20px;'>" + "取消</button>" + "</div>" + "</div>" + "</form>",
      title: '确认提现',
      width: 420,
      height: 320,
      button: false
    });
  }
  function submitBtn(id) {
    var isSuccess = $('select[name="confirmSelect"]').val();
    if (isSuccess == '') {
      alert('请选择提现操作');
      return;
    }
    var confirmRemark = $('#confirmRemark').val();
    if (confirmRemark == '') {
      alert('请填写备注');
      return;
    } else {
      $.post('${ctx}/withdraw/confirm', {
        id: id,
        isSuccess: isSuccess,
        confirmRemark: confirmRemark
      }, function (result) {
        toastr.success(result.message, '提示信息');
        $confirmDialog.close();
        grid.getDataTable().ajax.reload(null, false);
      })
    }

  }

  function closeBtn() {
    $confirmDialog.close();
  }
</script>

<!-- BEGIN PAGE HEADER-->
<div class="page-bar">
  <ul class="page-breadcrumb">
    <li><i class="fa fa-home"></i> <a href="javascript:;" data-href="${ctx}/main">首页</a> <i class="fa fa-angle-right"></i></li>
    <li><a href="javascript:;" data-href="${ctx}/withdraw">提现管理</a></li>
  </ul>
</div>
<!-- END PAGE HEADER-->

<div class="row">
  <div class="col-md-12">
    <!-- BEGIN ALERTS PORTLET-->
    <div class="portlet light bordered">
      <div class="portlet-title">
        <div class="caption">
          <i class="icon-logout"></i><span>提现管理 </span>
        </div>
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
                <input type="text" name="userPhoneEQ" class="form-control" placeholder="手机号"/>
              </div>

              <div class="form-group">
                <input type="text" name="userNicknameLK" class="form-control" placeholder="昵称"/>
              </div>

              <div class="form-group">
                <select name="withdrawStatusEQ" class="form-control">
                  <option value="">-- 提现状态 --</option>
                  <option value="0">已申请</option>
                  <option value="1">提现成功</option>
                  <option value="2">已取消</option>
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
