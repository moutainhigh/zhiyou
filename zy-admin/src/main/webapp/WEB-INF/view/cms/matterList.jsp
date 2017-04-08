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
        lengthMenu : [ [ 10, 20, 50, 100 ], [ 10, 20, 50, 100 ] ],// change per page values here
        pageLength: 20, // default record count per page
        order: [], // set first column as a default sort by desc
        ajax: {
          url: '${ctx}/matter', // ajax source
        },
        columns: [
          {
            data: 'image',
            title: '主图',
            orderable: false,
            width: '160px',
            render: function (data, type, full) {
              if(full.type == 1){
                return '<a target="_blank" href="' + full.url + '"><img style="width:160px;height:100px;"  src="${ctx}/image/icon_video.jpg"/></a>';
              }else if(full.type == 2){
                return '<a target="_blank" href="' + full.url + '"><img style="width:160px;height:100px;"  src="' + full.url + '"/></a>';
              }else if(full.type == 3){
                return '<a target="_blank" href="' + full.url + '"><img style="width:160px;height:100px;"  src="${ctx}/image/pdf.jpg"/></a>';
              }else if(full.type == 4){
                return '<a target="_blank" href="' + full.url + '"><img style="width:160px;height:100px;"  src="${ctx}/image/icon_voice.jpg"/></a>';
              }
            }
          },
          {
            data: 'title',
            title: '标题',
            orderable: false,
            width: '80px'
          },
          {
            data: 'type',
            title: '文件类型',
            orderable: false,
            width: '60px',
            render: function (data, type, full) {
              if (data == 1) {
                return '视频';
              }else if (data == 2){
                return '图片';
              }else if (data == 3){
                return '文档';
              }else if (data == 4){
                return '音频';
              }
            }
          },
          {
            data: 'description',
            title: '简述',
            orderable: false,
            width: '100px'
          },
          {
            data: 'url',
            title: '链接',
            orderable: false,
            width: '100px'
          },
          {
            data: 'authority',
            title: '权限',
            orderable: false,
            width: '60px',
            render: function (data, type, full) {
//              console.info(data);
              if (data == 0) {
                return '无权限';
              }else if (data == 1){
                return 'VIP';
              }else if (data == 2){
                return '市级服务商';
              }else if (data == 3){
                return '省级服务商';
              }else if (data == 4){
                return '特级服务商';
              }
            }
          },
          {
            data: 'status',
            title: '是否上架',
            orderable: false,
            width: '30px',
            render: function (data, type, full) {
              if (data == 1) {
                return '<i class="fa fa-check font-green"></i>';
              }
              return '';
            }
          },
          {
            data: 'author',
            title: '上传用户',
            orderable: false,
            width: '40px'
          },
          {
            data: 'uploadTime',
            title: '上传时间',
            width: '60px'
          },
          {
            data: 'clickedCount',
            title: '点击量',
            width: '20px'
          },
          {
            data: 'collectedCount',
            title: '关注量',
            width: '20px'
          },
          {
            data: 'downloadCount',
            title: '下载量',
            width: '20px'
          },
          {
            data: 'id',
            title: '操作',
            width: '120px',
            orderable: false,
            render: function (data, type, full) {
//              console.info(full.status);
              var optionHtml = '';
              optionHtml += '<a class="btn btn-xs default yellow-stripe" href="javascript:;" data-href="${ctx}/matter/update?id=' + data + '"><i class="fa fa-edit"></i> 编辑 </a>';
              if (full.status == 1) {
                optionHtml += '<a class="btn btn-xs default red-stripe" href="javascript:;" data-href="${ctx}/matter/updateStatus?id=' + data + '&status=0"><i class="fa fa-times"></i> 下架 </a>';
              } else if(full.status == 0){
                optionHtml += '<a class="btn btn-xs default green-stripe" href="javascript:;" data-href="${ctx}/matter/updateStatus?id=' + data + '&status=1"><i class="fa fa-check"></i> 上架 </a>';
              }
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
    <li><a href="javascript:;" data-href="${ctx}/matter">资源管理</a></li>
  </ul>
</div>
<!-- END PAGE HEADER-->

<div class="row">
  <div class="col-md-12">
    <!-- BEGIN ALERTS PORTLET-->
    <div class="portlet light bordered">
      <div class="portlet-title">
        <div class="caption">
          <i class="icon-arrow-down"></i> 资源管理
        </div>
        <div class="actions">
          <a class="btn btn-circle green" data-href="${ctx}/matter/create">
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
                <select name="type" class="form-control">
                  <option value="">-- 类型 --</option>
                  <option value="1">视频</option>
                  <option value="2">图片</option>
                  <%--<option value="3">文档</option>--%>
                  <option value="4">音频</option>
                </select>
              </div>
              <div class="form-group">
                <input type="text" name="title" class="form-control" placeholder="标题"/>
              </div>
              <div class="form-group">
                <select name="authority" class="form-control">
                  <option value="">-- 权限 --</option>
                  <option value="0">无权限</option>
                  <option value="1">VIP</option>
                  <option value="2">市级服务商</option>
                  <option value="3">省级服务商</option>
                  <option value="4">特级服务商</option>
                </select>
              </div>
              <div class="form-group">
                <input class="Wdate form-control" type="text" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',readOnly:true})" name="uploadStartTime" value="" placeholder="上传开始时间" />
              </div>
              <div class="form-group">
                <input class="Wdate form-control" type="text" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',readOnly:true})"
                       name="uploadEndTime" value="" placeholder="上传结束时间" />
              </div>
              <%--<div class="form-group">--%>
                <%--<select name="bannerPositionEQ" class="form-control">--%>
                  <%--<option value="">-- Banner 位置--</option>--%>
                  <%--<c:forEach items="${bannerPositions}" var="bannerPosition">--%>
                    <%--<option value="${bannerPosition.ordinal()}">${bannerPosition}</option>--%>
                  <%--</c:forEach>--%>
                <%--</select>--%>
              <%--</div>--%>
              <div class="form-group">
                <select name="status" class="form-control">
                  <option value="">-- 状态 --</option>
                  <option value="1">上架</option>
                  <option value="0">下架</option>
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