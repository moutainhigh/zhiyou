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
          url: '${ctx}/profit', // ajax source
        },
        columns: [
          {
            data: 'id',
            title: 'id'
          },
          {
            data: 'title',
            title: '收益基本信息',
            orderable: false,
            render: function (data, type, full) {
              return '<p>sn: ' + full.sn + '</p><p> 标题：' + full.title + '</p>';
            }
          },
          {
            data: 'user',
            title: '用户信息',
            orderable: false,
            render: function (data, type, full) {
              return formatUser(full.user);
            }
          },
          {
            data: 'amountLabel',
            title: '收益',
            orderable: false
          },
          {
            data: 'profitType',
            title: '业务名',
            orderable: false
          },
          {
            data: 'createdTime',
            title: '创建时间',
            orderable: true
          },

          {
            data: 'profitStatus',
            title: '收益单状态',
            orderable: false,
            render: function (data, type, full) {
              return '<label class="label label-' + full.profitStatusStyle + '">' + data + '</label>';
            }
          },
          {
            data: 'grantedTime',
            title: '收益时间',
            orderable: true
          },
          {
            data: 'remark',
            title: '备注',
            orderable: false
          }]
      }
    });

  });
</script>

<!-- BEGIN PAGE HEADER-->
<div class="page-bar">
  <ul class="page-breadcrumb">
    <li><i class="fa fa-home"></i> <a href="javascript:;" data-href="${ctx}/main">首页</a> <i class="fa fa-angle-right"></i></li>
    <li><a href="javascript:;" data-href="${ctx}/profit">收益管理</a></li>
  </ul>
</div>
<!-- END PAGE HEADER-->

<div class="row">
  <div class="col-md-12">
    <!-- BEGIN ALERTS PORTLET-->
    <div class="portlet light bordered">
      <div class="portlet-title">
        <div class="caption">
          <i class="icon-present"></i><span>收益管理 </span>
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
                <input type="text" name="phoneEQ" class="form-control" placeholder="手机号"/>
              </div>

              <div class="form-group">
                <input type="text" name="nicknameLK" class="form-control" placeholder="昵称"/>
              </div>

              <div class="form-group">
                <input type="text" name="snEQ" class="form-control" placeholder="收益单号"/>
              </div>

              <div class="form-group">
                <select name="profitTypeEQ" class="form-control">
                  <option value="">-- 收益类型 --</option>
                  <c:forEach items="${profitTypes}" var="profitType">
                    <option value="${profitType}">${profitType}</option>
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
        <!-- END ALERTS PORTLET-->
      </div>
    </div>
  </div>
</div>