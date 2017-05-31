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
        order: [], // set first column as a default sort by desc
        ajax: {
          url: '${ctx}/activityTeamApply', // ajax source
        },
        columns: [
	        {
		        data: '',
		        title: '活动',
		        orderable: false,
            render: function(data, type, full) {
              return '<img class="image-view" data-title="活动" src="' + full.activity.imageThumbnail + '" width="120" height="80" />' + full.activity.title;
            }
	        },
	        {
		        data: '',
		        title: '用户',
		        orderable: false,
            render: function(data, type, full) {
              return formatUser(full.buyer);
            }
	        },
          {
            data: 'paidStatus',
            title: '活动报名支付状态',
	          orderable: false,
	          render: function(data, type, full) {
		          if (data == '未支付') {
		          	return '<label class="label label-info">未支付</label>';
              } else {
			          return '<label class="label label-success">已支付</label>';
              }
	          }
          },
	        {
		        data: 'amountLabel',
		        title: '支付报名费用',
		        orderable: false
	        },
	        {
		        data: 'count',
		        title: '购票数量'
	        },
	        {
		        data: 'createTime',
		        title: '下单时间'
	        },
	        {
		        data: 'paidTime',
		        title: '支付时间'
	        },
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
    <li><a href="javascript:;" data-href="${ctx}/activityTeamApply">活动团队报名管理</a></li>
  </ul>
</div>
<!-- END PAGE HEADER-->

<div class="row">
  <div class="col-md-12">
    <!-- BEGIN ALERTS PORTLET-->
    <div class="portlet light bordered">
      <div class="portlet-title">
        <div class="caption">
          <i class="icon-social-dropbox"></i> 活动团队报名管理
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
                <input type="text" name="activityTitleLK" class="form-control" placeholder="活动标题"/>
              </div>

              <div class="form-group">
                <input type="text" name="phoneEQ" class="form-control" placeholder="手机号"/>
              </div>

              <div class="form-group">
                <input type="text" name="nicknameLK" class="form-control" placeholder="昵称"/>
              </div>

              <div class="form-group">
                <select name="paidStatus" class="form-control">
                  <option value="">-- 报名支付状态 --</option>
                  <option value="0">未支付</option>
                  <option value="1">已支付</option>
                </select>
              </div>

                <div class="form-group input-inline">
                    <input class="Wdate form-control" type="text"
                           onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',readOnly:true})" name="createTimeGTE" value="" placeholder="下单时间起"/>
                </div>
                <div class="form-group input-inline">
                    <input class="Wdate form-control" type="text" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',readOnly:true})"
                           name="createTimeLT" value="" placeholder="下单时间止"/>
                </div>

                <div class="form-group input-inline">
                    <input class="Wdate form-control" type="text"
                           onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',readOnly:true})" name="paidTimeGTE" value="" placeholder="支付时间起"/>
                </div>
                <div class="form-group input-inline">
                    <input class="Wdate form-control" type="text" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',readOnly:true})"
                           name="paidTimeLT" value="" placeholder="支付时间止"/>
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