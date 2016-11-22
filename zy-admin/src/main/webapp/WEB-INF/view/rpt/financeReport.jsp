<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/view/include/head.jsp" %>
<!-- BEGIN JAVASCRIPTS -->
<script>
	
  function sum() {
    $.post("${ctx}/report/finance/sum", $('#searchForm').serialize(), function(result) {
      if(result.code == 0) {
        var data = result.data;
        if(data != null) {
          $('#totalPaymentAmount').text(data.totalPaymentAmount);
          $('#totalDepositAmount').text(data.totalDepositAmount);
          $('#totalWithdrawAmount').text(data.totalWithdrawAmount);
          $('#totalProfitAmount').text(data.totalProfitAmount);
          $('#totalAccountAmount').text(data.totalAccountAmount);
        }
        
      }
    });
  }

  var grid = new Datatable();

  $(function () {
    
	sum();
    
    $('.filter-submit').click(function(){
      sum();
    })
    
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
        lengthMenu: [
                         [10, 20, 50, 100],
                         [10, 20, 50, 100] // change per page values here
                         ],

        order: [], // set first column as a default sort by desc
        ajax: {
          url: '${ctx}/report/finance', // ajax source
        },
        columns: [
          {
            data: 'userNickname',
            title: '服务商',
            orderable: false
          },
          {
            data: 'userPhone',
            title: '手机号',
            orderable: false
          },
          {
            data: 'depositAmount',
            title: '积分充值',
            orderable: false
          },
          {
            data: 'withdrawAmount',
            title: '积分提现',
            orderable: false
          },
          {
            data: 'paymentAmount',
            title: '积分支付',
            orderable: false
          },
          {
            data: 'profitAmount',
            title: '积分收益',
            orderable: false
          },
          {
            data: 'accountAmount',
            title: '积分余额',
            orderable: false
          }
          ]
      }
    });

  });
  
</script>
<!-- END JAVASCRIPTS -->

<!-- BEGIN PAGE HEADER-->
<div class="page-bar">
  <ul class="page-breadcrumb">
    <li><i class="fa fa-home"></i> <a href="javascript:;" data-href="${ctx}/main">首页</a> <i class="fa fa-angle-right"></i></li>
    <li><a href="javascript:;" data-href="${ctx}/report/finance">财务报表</a></li>
  </ul>
</div>
<!-- END PAGE HEADER-->

<div class="row">
  <div class="col-md-12">
    <!-- BEGIN ALERTS PORTLET-->
    <div class="portlet light bordered">
      <div class="portlet-title">
        <div class="caption">
          <i class="icon-bar-chart"></i><span>财务报表</span>
        </div>
      </div>
      <div class="row">
        <div class="col-md-2">
              <div class="note note-info">
                  <h4 class="block">充值总积分</h4>
                  <p id="totalPaymentAmount">0.00</p>
              </div>
          </div>
          <div class="col-md-2">
              <div class="note note-success">
                  <h4 class="block">提现总积分</h4>
                  <p id="totalDepositAmount">0.00</p>
              </div>
          </div>
          <div class="col-md-2">
              <div class="note note-danger">
                  <h4 class="block">支付总积分</h4>
                  <p id="totalWithdrawAmount">0.00</p>
              </div>
          </div>
          <div class="col-md-2">
              <div class="note note-danger">
                  <h4 class="block">收益总积分</h4>
                  <p id="totalProfitAmount">0.00</p>
              </div>
          </div>
          <div class="col-md-2">
              <div class="note note-danger">
                  <h4 class="block">余额总积分</h4>
                  <p id="totalAccountAmount">0.00</p>
              </div>
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
                <input type="text" name="nicknameLK" class="form-control" placeholder="服务商昵称"/>
              </div>
              <div class="form-group">
                <input type="text" name="phoneEQ" class="form-control" placeholder="服务商手机"/>
              </div>
              <div class="form-group input-inline">
                <input class="Wdate form-control" type="text" id="beginDate"
                       onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',readOnly:true})" name="timeGTE" value="" placeholder="时间起"/>
              </div>
              <div class="form-group input-inline">
                <input class="Wdate form-control" type="text" id="endDate" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',readOnly:true})"
                       name="timeLT" value="" placeholder="时间止"/>
              </div>

              <div class="form-group input-inline">
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
