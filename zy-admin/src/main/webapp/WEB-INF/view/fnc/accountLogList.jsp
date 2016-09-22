<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/view/include/head.jsp" %>

<!-- BEGIN JAVASCRIPTS -->
<script>
  var grid = new Datatable();

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
        lengthMenu: [[10, 20, 50, 100], [10, 20, 50, 100]],// change per page values here
        pageLength: 20, // default record count per page
        order: [], // set first column as a default sort by desc
        ajax: {
          url: '${ctx}/accountLog', // ajax source
        },
        columns: [
          {
            data: '',
            title: '流水情况',
            orderable: false,
            render: function (data, type, full) {
              return '<p>标题：' + full.title + '</p>'
                +'<p>对应单据：' + full.accountLogType + ' ' + full.refSn + '</p>';
            }
          },
          {
            data: 'user',
            title: '用户信息',
            orderable: false,
            render: function (data, type, full) {
              return formatUser(full.user)
            }
          },
          {
            data: 'refUser',
            title: '关联用户',
            orderable: false,
            render: function (data, type, full) {
              return formatUser(full.refUser)
            }
          },
          {
            data: 'currencyType',
            title: '货币类型',
            orderable: false

          },
          {
            data: 'beforeAmountLabel',
            title: '交易前',
            orderable: false

          },
          {
            data: 'transAmountLabel',
            title: '交易金额',
            orderable: false,
            render: function (data, type, full) {
              var result;
              if (full.inOut == '收入') {
                result = '<span class="currency-in">' + data + '</span>';
              } else {
                result = '<span class="currency-out">' + data + '</span>';
              }
              return result;
            }
          },
          {
            data: 'afterAmountLabel',
            title: '交易后(余额)',
            orderable: false
          },
          {
            data: 'transTime',
            title: '流水时间',
            orderable: false
          }
        ]
      }
    });

    $('#accountLogTypeIN').select2({
      multiple: true,
      placeholder: '-- 单据类型 --',
      data: [
        {
          id: '0',
          text: '充值单'
        },
        {
          id: '1',
          text: '支付单'
        },
        {
          id: '2',
          text: '提现单'
        },
        {
          id: '3',
          text: '收益单'
        },
        {
          id: '4',
          text: '转账单'
        }
      ]

    });

  });
</script>
<script type="text/javascript">
  <shiro:hasPermission name="transaction:export">
  function transactionExport() {
    location.href = '${ctx}/accountLog/accountLogExport?' + $('#searchForm').serialize();
  }
  </shiro:hasPermission>
</script>
<!-- BEGIN PAGE HEADER-->
<div class="page-bar">
  <ul class="page-breadcrumb">
    <li><i class="fa fa-home"></i> <a href="javascript:;" data-href="${ctx}/main">首页</a> <i class="fa fa-angle-right"></i></li>
    <li><a href="javascript:;" data-href="${ctx}/accountLog">流水管理</a></li>
  </ul>
</div>
<!-- END PAGE HEADER-->

<div class="row">
  <div class="col-md-12">
    <!-- BEGIN ALERTS PORTLET-->
    <div class="portlet light bordered">
      <div class="portlet-title">
        <div class="caption">
          <i class="icon-calculator"></i><span>流水管理 </span>
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
                <input type="text" name="phoneEQ" value="${phone}" class="form-control" placeholder="手机号"/>
              </div>

              <c:if test="${empty fromParent}">

                <div class="form-group">
                  <input type="text" name="nicknameLK" class="form-control" placeholder="昵称"/>
                </div>

                <div class="form-group">
                  <input type="text" name="titleLK" class="form-control" placeholder="标题"/>
                </div>

                <div class="form-group">
                  <input type="text" id="accountLogTypeIN" name="accountLogTypeIN" class="form-control" style="min-width:200px;"/>
                </div>

                <div class="form-group">
                  <input type="text" name="refSnEQ" class="form-control" placeholder="单据号"/>
                </div>

                <div class="form-group">
                  <select name="inOutEQ" class="form-control">
                    <option value="">-- 资金流向 --</option>
                    <option value="0">收入</option>
                    <option value="1">支出</option>
                  </select>
                </div>

                <div class="form-group">
                  <input class="Wdate form-control" type="text" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',readOnly:true})" name="transTimeGTE" value=""
                         placeholder="流水产生时间起"/>
                </div>

                <div class="form-group">
                  <input class="Wdate form-control" type="text" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',readOnly:true})" name="transTimeLT" value=""
                         placeholder="流水产生时间止"/>
                </div>

                <div class="form-group">
                  <button class="btn blue filter-submit">
                    <i class="fa fa-search"></i> 查询
                  </button>
                </div>
                <shiro:hasPermission name="transaction:export">
                  <div class="form-group">
                    <button class="btn yellow" onClick="transactionExport()">
                      <i class="fa fa-file-excel-o"></i> 导出Excel
                    </button>
                  </div>
                </shiro:hasPermission>
              </c:if>
              <c:if test="${not empty fromParent}">
                <div class="form-group">
                  <button type="button" class="btn grey" data-href="${ctx}/withdraw">
                    <i class="fa fa-mail-reply"></i> 返回
                  </button>
                </div>
              </c:if>
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
