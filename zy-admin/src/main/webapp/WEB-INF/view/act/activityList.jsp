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
        lengthMenu: [
          [10, 20, 50, 100, -1],
          [10, 20, 50, 100, 'All'] // change per page values here
        ],
        pageLength: 20, // default record count per page
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
            title: '标题'
          },
          {
            data: 'areaId',
            title: '详细地址',
            render: function (data, type, full) {
              return '<p>' + full.province + '-' + full.city + '-' + full.district + '</p>'
                + '<p class="small">'+ full.address + '</p>';
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
            data: 'collectedCount',
            title: '关注数'
          },
          {
            data: 'appliedCount',
            title: '报名数',
            width: '60px'
          },
          {
            data: 'signedInCount',
            title: '签到数'
          },
          {
            data: 'isReleased',
            title: '是否上架',
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
              //optionHtml += '<a class="btn btn-xs default red-stripe" href="javascript:;" data-href="${ctx}/activity/delete?id=' + data + '" data-confirm="您确定要删除选中数据吗?"><i class="fa fa-trash-o"></i> 删除 </a>';
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
    <li><a href="javascript:;" data-href="${ctx}/activity">Banner管理</a></li>
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