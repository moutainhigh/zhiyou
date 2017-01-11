<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/view/include/head.jsp" %>
<!-- BEGIN JAVASCRIPTS -->
<script>

  function sum() {
    $.post("${ctx}/order/sum", $('#searchForm').serialize(), function(result) {
      if(result.code == 0) {
        var data = result.data;
        $('#countNumber').text(data.countNumber + '个');
        $('#sumQuantity').text(data.sumQuantity == null? 0 + '支' : data.sumQuantity + '支');
        $('#sumAmount').text(data.sumAmount == null? 0.00 + '元' : data.sumAmount.toFixed(2) + '元');
      }
    });
  }

  $(function () {

	  $('#dataTable').on('click', '.detail-view', function() {
		  var id = $(this).data('id');
		  $.ajax({
			  url: '${ctx}/order/detail?id=' + id +　'&isPure=true',
			  dataType: 'html',
			  success: function(data) {
				  layer.open({
					  type: 1,
					  skin: 'layui-layer-rim', //加上边框
					  area: ['1080px', '720px'], //宽高
					  content: data
				  });
			  }
		  });
	  });

    sum();
    
    $('.filter-submit').click(function(){
      sum();
    })
    
    var grid = new Datatable();

    $('#statusTab li').bind('click', function () {
      $this = $(this);
      $('input[name="orderStatusEQ"]').val($this.data('order-status'));
      $('input[name="isPlatformDeliverEQ"]').val($this.data('is-platform-deliver'));
      grid.getDataTable().ajax.reload(null, false);
      
      sum();
      
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
          url: '${ctx}/order', // ajax source
        },
        columns: [
          {
            data: 'sn',
            title: '订单',
            orderable: false,
            render: function (data, type, full) {
              return '<p>sn: ' + full.sn + '</p><p> 标题：' + full.title + '</p>类型: ' + full.orderType;
            }
          },
          {
            data: 'orderStatus',
            title: '订单状态',
            orderable: false,
            render: function (data, type, full) {
              return '<label class="label label-' + full.orderStatusStyle + '">' + data + '</label>';
            }
          },
          {
            data: '',
            title: '买家信息',
            orderable: false,
            render: function (data, type, full) {
              return formatUser(full.user);
            }
          },
          {
            data: '',
            title: '卖家信息',
            orderable: false,
            render: function (data, type, full) {
              return formatUser(full.seller);
            }
          },
          {
            data: '',
            title: '金额信息',
            orderable: false,
            render: function (data, type, full) {
              if(full.amountLabel){
                return '<p>单价：' + full.priceLabel + '</p>' + '<p>数量：' + full.quantity + '</p>' + '<p>总金额：' + full.amountLabel + '</p>';
              } else {
                return '-';
              }
            }
          },
          {
            data: 'createdTimeLabel',
            title: '下单时间',
            orderable: false
          },
          {
            data: 'isPlatformDeliver',
            title: '平台发货',
            orderable: false,
            render: function (data, type, full) {
              return data ? '是' : '否';
            }
          },
          {
            data: 'isPayToPlatform',
            title: '支付给平台',
            orderable: false,
            render: function (data, type, full) {
              return data ? '是' : '否';
            }
          },
          {
            data: '',
            title: '物流信息',
            orderable: false,
            render: function (data, type, full) {
              if(full.logisticsName){
                return '<p>物流公司：' + full.logisticsName + '</p>' + '<p>物流单号：' + full.logisticsSn + '</p>';
              } else {
                return '-';
              }
            }
          },
          {
            data: 'buyerMemo',
            title: '买家留言',
            orderable: false
          },
          {
            data: 'paidTimeLabel',
            title: '支付时间',
	          orderable: false
          },

          {
            data: 'remark',
            title: '备注',
            orderable: false
          },
          /* {
            data: 'refund',
            title: '退款金额',
            orderable: true
          },
          {
            data: 'refundedTimeLabel',
            title: '退款时间',
            orderable: false
          },
          {
            data: 'refundRemark',
            title: '退款备注',
            orderable: false
          }, */ {
            data: 'id',
            title: '操作',
            orderable: false,
            render: function (data, type, full) {
              var optionHtml = '<a class="btn btn-xs default blue-stripe detail-view" href="javascript:;" data-id="' + full.id + '"><i class="fa fa-search"></i> 查看 </a>';
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
    <li><a href="javascript:;" data-href="${ctx}/order">订单管理</a></li>
  </ul>
</div>
<!-- END PAGE HEADER-->

<div class="row">
  <div class="col-md-12">
    <!-- BEGIN ALERTS PORTLET-->
    <div class="portlet light bordered">
      <div class="portlet-title">
        <div class="caption">
          <i class="icon-docs"></i><span>订单管理 </span>
        </div>
        <div class="tools"></div>
      </div>
      <div class="row">
        <div class="col-md-4">
            <div class="note note-info">
                <h4 class="block">订单数</h4>
                <p id="countNumber">0 个</p>
            </div>
        </div>
        <div class="col-md-4">
            <div class="note note-success">
                <h4 class="block">订单支数</h4>
                <p id="sumQuantity">0 支</p>
            </div>
        </div>
        <div class="col-md-4">
            <div class="note note-danger">
                <h4 class="block">订单总额</h4>
                <p id="sumAmount">0.00 元</p>
            </div>
        </div>
      </div>
      <div class="portlet-body clearfix">
        <div class="table-container">
          <div class="table-toolbar">
            <form class="filter-form form-inline" id="searchForm">
              <ul id="statusTab" class="nav nav-tabs">
                <li class="active"><a href="javascript:void(0)" data-toggle="tab" aria-expanded="false"> 全部</a></li>
                <li class="" data-order-status="0"><a href="javascript:void(0)" data-toggle="tab" aria-expanded="false"> 待付款</a></li>
                <li class="" data-order-status="1"><a href="javascript:void(0)" data-toggle="tab" aria-expanded="false"> 待确认</a></li>
                <li class="" data-order-status="2"><a href="javascript:void(0)" data-toggle="tab" aria-expanded="false"> 已支付</a></li>
                <li class="" data-order-status="3"><a href="javascript:void(0)" data-toggle="tab" aria-expanded="false"> 已发货</a></li>
                <li class="" data-order-status="4"><a href="javascript:void(0)" data-toggle="tab" aria-expanded="false"> 已完成</a></li>
                <li class="" data-order-status="5"><a href="javascript:void(0)" data-toggle="tab" aria-expanded="false"> 已退款</a></li>
                <li class="" data-order-status="6"><a href="javascript:void(0)" data-toggle="tab" aria-expanded="false"> 已取消</a></li>
              </ul>
              <input id="_orderBy" name="orderBy" type="hidden" value=""/>
              <input id="_direction" name="direction" type="hidden" value=""/>
              <input id="_pageNumber" name="pageNumber" type="hidden" value="0"/>
              <input id="_pageSize" name="pageSize" type="hidden" value="20"/>
              <input type="hidden" name="orderStatusEQ" value=""/>
              <input type="hidden" name="isPlatformDeliverEQ" value=""/>

              <div class="form-group">
                <input type="text" name="userPhoneEQ" class="form-control" placeholder="用户手机号"/>
              </div>

              <div class="form-group">
                <input type="text" name="userNicknameLK" class="form-control" placeholder="用户昵称"/>
              </div>

              <div class="form-group">
                <input type="text" name="snLK" class="form-control" placeholder="订单号"/>
              </div>
              
              <div class="form-group">
                <input type="text" name="titleLK" class="form-control" placeholder="标题"/>
              </div>
              
              <div class="form-group">
                <input type="text" name="logisticsSnLK" class="form-control" placeholder="物流单号"/>
              </div>              
              
              <div class="form-group">
                <select name="isPayToPlatformEQ" class="form-control">
                  <option value="">-- 是否支付给平台 --</option>
                  <option value="1">是</option>
                  <option value="0">否</option>
                </select>
              </div>

              <div class="form-group">
                <select name="orderTypeEQ" class="form-control">
                  <option value="">-- 是否补单 --</option>
                  <option value="1">是</option>
                  <option value="0">否</option>
                </select>
              </div>

              <div class="form-group">
                <input class="Wdate form-control" type="text" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',readOnly:true})" name="createdTimeGTE" value="" placeholder="下单时间起" />
              </div>
              <div class="form-group">
                <input class="Wdate form-control" type="text" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',readOnly:true})"
                  name="createdTimeLT" value="" placeholder="下单时间止" />
              </div>

              <div class="form-group">
                <input class="Wdate form-control" type="text" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',readOnly:true})" name="paidTimeGTE" value="" placeholder="支付时间起" />
              </div>
              <div class="form-group">
                <input class="Wdate form-control" type="text" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',readOnly:true})"
                  name="paidTimeLT" value="" placeholder="支付时间止" />
              </div>

              <div class="form-group">
                <select class="form-control" name="paidTimeOrderBy">
                  <option value="">-- 请选择排序字段 --</option>
                  <option value="1">支付时间</option>
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
