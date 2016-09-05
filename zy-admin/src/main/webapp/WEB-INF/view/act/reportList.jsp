<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/view/include/head.jsp"%>
<style>
img {
  cursor: pointer;
}
</style>
<!-- BEGIN JAVASCRIPTS -->
<script>
  var grid = new Datatable();

  $(function() {
    grid.init({
      src : $('#dataTable'),
      onSuccess : function(grid) {
        // execute some code after table records loaded
      },
      onError : function(grid) {
        // execute some code on network or other general error
      },
      dataTable : {
        //"sDom" : "<'row'<'col-md-8 col-sm-12'pli><'col-md-4 col-sm-12'<'table-group-actions pull-right'>>r>t<'row'<'col-md-8 col-sm-12'pli><'col-md-4 col-sm-12'>r>>",
        lengthMenu : [ [ 10, 20, 50, 100, -1 ], [ 10, 20, 50, 100, 'All' ] // change per page values here
        ],
        pageLength : 20, // default record count per page
        order : [], // set first column as a default sort by desc
        ajax : {
          url : '${ctx}/report', // ajax source
        },
        columns : [ {
          data : '',
          title : '用户信息',
          width : '120px',
          render : function(data, type, full) {
            return '<p>姓名: ' + full.realname + '</p><p>性别: ' + full.gender + '</p><p>年龄: ' + full.age + '</p>';
          }
        }, {
          data : 'text',
          title : '试用评论',
          orderable : false,
          width : '200px'
        }, {
          data : 'reportResult',
          title : '试用结果',
          orderable : false,
          width : '100px'
        }, {
          data : 'image1Thumbnail',
          title : '试用图片1',
          orderable : false,
          width : '100px',
          render : function(data, type, full) {
            return '<img class="imagescan" data-url="' + data + '" src="' + data + '" style="width: 80px; height: 80px;">';
          }
        }, {
          data : 'image2Thumbnail',
          title : '试用图片2',
          orderable : false,
          width : '100px',
          render : function(data, type, full) {
            return '<img class="imagescan" data-url="' + data + '" src="' + data + '" style="width: 80px; height: 80px;">';
          }
        }, {
          data : 'image3Thumbnail',
          title : '试用图片3',
          orderable : false,
          width : '100px',
          render : function(data, type, full) {
            return '<img class="imagescan" data-url="' + data + '" src="' + data + '" style="width: 80px; height: 80px;">';
          }
        }, {
          data : 'image4Thumbnail',
          title : '试用图片4',
          orderable : false,
          width : '100px',
          render : function(data, type, full) {
            return '<img class="imagescan" data-url="' + data + '" src="' + data + '" style="width: 80px; height: 80px;">';
          }
        }, {
          data : 'image5Thumbnail',
          title : '试用图片5',
          orderable : false,
          width : '100px',
          render : function(data, type, full) {
            return '<img class="imagescan" data-url="' + data + '" src="' + data + '" style="width: 80px; height: 80px;">';
          }
        }, {
          data : 'image6Thumbnail',
          title : '试用图片6',
          orderable : false,
          width : '100px',
          render : function(data, type, full) {
            return '<img class="imagescan" data-url="' + data + '" src="' + data + '" style="width: 80px; height: 80px;">';
          }
        }, {
          data : 'confirmStatus',
          title : '审核状态',
          orderable : false,
          width : '60px',
          render : function(data, type, full) {
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
        }, {
          data : 'confirmRemark',
          title : '审核备注',
          orderable : false,
          width : '200px'
        }, {
          data : 'confirmedTime',
          title : '审核时间',
          orderable : false,
          width : '200px'
        }, {
          data : 'isSettledUp',
          title : '已结算',
          orderable : false,
          width : '120px',
          render : function(data, type, full) {
            if (data) {
              return '<i class="fa fa-check font-green"></i> <span class="badge badge-success"> 已结算 </span>';
            }
            return '';
          }
        } ]
      }
    });
    $('#dataTable').on('click', '.imagescan', function() {
      var url = $(this).attr('data-url');
      $.imagescan({
        url : url
      });
    });
  });
</script>
<!-- END JAVASCRIPTS -->

<!-- BEGIN PAGE HEADER-->
<div class="page-bar">
  <ul class="page-breadcrumb">
    <li><i class="fa fa-home"></i> <a href="javascript:;" data-href="${ctx}/main">首页</a> <i class="fa fa-angle-right"></i></li>
    <li><a href="javascript:;" data-href="${ctx}/report">试用报告管理</a></li>
  </ul>
</div>
<!-- END PAGE HEADER-->

<div class="row">
  <div class="col-md-12">
    <!-- BEGIN ALERTS PORTLET-->
    <div class="portlet light bordered">
      <div class="portlet-title">
        <div class="caption">
          <i class="icon-volume-1"></i> 试用报告管理
        </div>
      </div>
      <div class="portlet-body clearfix">
        <div class="table-container">
          <div class="table-toolbar">
            <form class="filter-form form-inline">
              <input id="_orderBy" name="orderBy" type="hidden" value="" /> <input id="_direction" name="direction" type="hidden" value="" /> <input id="_pageNumber"
                name="pageNumber" type="hidden" value="0" /> <input id="_pageSize" name="pageSize" type="hidden" value="20" />

              <div class="form-group">
                <input type="text" name="realnameLK" class="form-control" placeholder="姓名" />
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