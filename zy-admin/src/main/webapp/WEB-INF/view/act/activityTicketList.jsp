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
          url: '${ctx}/activityTeamApply/ticket', // ajax source
        },
        columns: [
	        {
		        data: 'codeImageUrl',
		        title: '二维码',
		        orderable: false,
                width: '60px',
            render: function(data, type, full) {
                return '<a target="_blank" href="' + data + '"><img style="width:120px;height:80px;"  src="' + full.codeImageUrl + '"/></a>';
            }
	        },
	        {
		        data: '',
		        title: '活动标题',
		        orderable: false,
            render: function(data, type, full) {
              return full.activityTeamApply.activity.title;
            }
	        },
	        {
		        data: '',
		        title: '活动开始时间',
                orderable: false,
            render: function(data, type, full) {
              return full.activityTeamApply.activity.startTimeLabel;
            }
	        },
	        {
		        data: '',
		        title: '购票人昵称',
		        orderable: false,
            render: function(data, type, full) {
              return full.activityTeamApply.buyer.nickname;
            }
	        },
	        {
		        data: '',
		        title: '购票人手机号',
		        orderable: false,
            render: function(data, type, full) {
              return full.activityTeamApply.buyer.phone;
            }
	        },
	        {
		        data: '',
		        title: '购票时间',
                orderable: false,
            render: function(data, type, full) {
              return full.activityTeamApply.paidTime;
            }
	        },
            {
                data: '',
                title: '使用人昵称',
                orderable: false,
                render: function(data, type, full) {
                    if(full.isUsed == 0){
                        return "";
                    }else{
                        if(null == full.usedUser  ){
                            return "";
                        }
                        return full.usedUser.nickname;
                    }
                }
            },
            {
                data: '',
                title: '使用人手机号',
                orderable: false,
                render: function(data, type, full) {
                    if(full.isUsed == 0){
                        return "";
                    }else{
                        if(null == full.usedUser  ){
                            return "";
                        }
                        return full.usedUser.phone;
                    }
                }
            },
          {
            data: 'isUsed',
            title: '状态',
            render: function(data, type, full) {
              if (data == 0) {
                return '<label class="label label-info">未使用</label>';
              } else {
                  return '<label class="label label-success">已使用</label>';
              }
            }
          },
            {
                data: 'id',
                title: '操作',
                width: '80px',
                orderable: false,
                render: function (data, type, full) {
                    var optionHtml = '';
                    if (null != full.usedUser && full.isUsed == 1) {
                        <shiro:hasPermission name="activityTicket:edit">
                            optionHtml += '<a class="btn btn-xs default yellow-stripe" href="javascript:;" data-href="${ctx}/activityTeamApply/ticketReset/' + data + '" data-confirm="您确定要重置此二维码吗？"><i class="fa fa-smile-o"></i> 重置 </a>';
                        </shiro:hasPermission>
                    }
                    return optionHtml;
                }
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
    <li><a href="javascript:;" data-href="${ctx}/activityTeamApply/ticket">活动票务管理</a></li>
  </ul>
</div>
<!-- END PAGE HEADER-->

<div class="row">
  <div class="col-md-12">
    <!-- BEGIN ALERTS PORTLET-->
    <div class="portlet light bordered">
      <div class="portlet-title">
        <div class="caption">
          <i class="icon-social-dropbox"></i> 活动票务管理
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
                <input type="text" name="buyerNicknameLK" class="form-control" placeholder="购买人昵称"/>
              </div>

              <div class="form-group">
                <input type="text" name="buyerPhoneEQ" class="form-control" placeholder="购买人手机号"/>
              </div>

              <div class="form-group">
                <input type="text" name="usedPhoneEQ" class="form-control" placeholder="使用人手机号"/>
              </div>

              <div class="form-group">
                <select name="isUsed" class="form-control">
                  <option value="">-- 是否已使用 --</option>
                  <option value="0">未使用</option>
                  <option value="1">已使用</option>
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