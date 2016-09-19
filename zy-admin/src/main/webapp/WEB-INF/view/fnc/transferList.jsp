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
        lengthMenu: [[10, 20, 50, 100, -1], [10, 20, 50, 100, 'All'] // change per page values here
        ],
        pageLength: 20, // default record count per page
        order: [], // set first column as a default sort by desc
        ajax: {
          url: '${ctx}/transfer', // ajax source
        },
        columns: [
          {
            data: 'id',
            title: 'id'
          },
          {
            data: 'sn',
            title: '转账单号'
          },
          {
            data: 'title',
            title: '转账标题',
            orderable: false
          },
          /* {
           data: 'currencyType',
           title: '币种',
           orderable: false
           },*/
          {
            data: 'amount',
            title: '金额',
            orderable: false,
            render: function (data, type, full) {
              return data.toFixed(2);
            }
          },
          {
            data: 'createdTimeLabel',
            title: '创建时间',
            orderable: false
          },

          {
            data: '',
            title: '转出用户',
            orderable: false,
            render: function (data, type, full) {
              return formatUser(full.fromUser);
            }
          },
          {
            data: '',
            title: '转入用户',
            orderable: false,
            render: function (data, type, full) {
              return formatUser(full.toUser);
            }
          },
          {
            data: 'transferType',
            title: '转账类型',
            orderable: false
          },
          {
            data: 'transferStatus',
            title: '转账单状态',
            orderable: false,
            render: function (data, type, full) {
              return '<label class="label label-' + full.transferStatusStyle + '">' + data + '</label>';
            }
          },
          {
            data: 'transferredTimeLabel',
            title: '转账时间',
            orderable: false
          }
          {
            data: 'transferRemark',
            title: '转账备注',
            orderable: false
          },
          {
            data: 'remark',
            title: '备注',
            orderable: false
          },
        ]
      }
    });

  });

</script>

<!-- BEGIN PAGE HEADER-->
<div class="page-bar">
  <ul class="page-breadcrumb">
    <li><i class="fa fa-home"></i> <a href="javascript:;" data-href="${ctx}/main">首页</a> <i class="fa fa-angle-right"></i></li>
    <li><a href="javascript:;" data-href="${ctx}/account">转账单管理</a></li>
  </ul>
</div>
<!-- END PAGE HEADER-->

<div class="row">
  <div class="col-md-12">
    <!-- BEGIN ALERTS PORTLET-->
    <div class="portlet light bordered">
      <div class="portlet-title">
        <div class="caption">
          <i class="icon-loop"></i><span>转账单管理 </span>
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
                <input type="text" name="snEQ" class="form-control" placeholder="转账单号"/>
              </div>

              <div class="form-group">
                <select name="transferStatusEQ" class="form-control">
                  <option value="">-- 转账状态 --</option>
                  <c:forEach items="${transferStatuses}" var="transferStatus">
                    <option value="${transferStatus}">${transferStatus}</option>
                  </c:forEach>
                </select>
              </div>

              <div class="form-group">
                <select name="transferTypeEQ" class="form-control">
                  <option value="">-- 转账类型 --</option>
                  <c:forEach items="${transferTypes}" var="transferType">
                    <option value="${transferType}">${transferType}</option>
                  </c:forEach>
                </select>
              </div>

              <div class="form-group input-inline">
                <input class="Wdate form-control" type="text" id="beginDate"
                       onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',readOnly:true})" name="createdTimeGTE" value="" placeholder="创建时间起"/>
              </div>
              <div class="form-group input-inline">
                <input class="Wdate form-control" type="text" id="endDate" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',readOnly:true})"
                       name="createdTimeLT" value="" placeholder="创建时间止"/>
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
