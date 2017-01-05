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
                         [10, 20, 50, 100],
                         [10, 20, 50, 100] // change per page values here
                         ],

        order: [], // set first column as a default sort by desc
        ajax: {
          url: '${ctx}/report/v4Order', // ajax source
        },
        columns: [
          {
            data: 'nickname',
            title: '服务商',
            orderable: false
          },
          {
            data: 'phone',
            title: '手机号',
            orderable: false
          },
          {
            data: 'v4ChildrenCount',
            title: '下线团队特级人数',
            orderable: false
          },
          {
            data: 'directV4ChildrenCount',
            title: '直属下线特级人数',
            orderable: false
          },
          {
            data: 'inQuantitySum',
            title: '进货量',
            orderable: false
          },
          {
            data: 'outQuantitySum',
            title: '出货量',
            orderable: false
          },
          {
            data: 'teamInQuantitySum',
            title: '团队进货量',
            orderable: false
          },
          {
            data: 'teamOutQuantitySum',
            title: '团队出货量',
            orderable: false
          }
          ]
      }
    });

  });

</script>
<!-- END JAVASCRIPTS -->

<div class="row" style="width: 100%;">
  <div class="col-md-12">
    <!-- BEGIN ALERTS PORTLET-->
    <div class="portlet light bordered">
      <div class="portlet-title">
        <div class="caption">
          <i class="icon-bar-chart"></i><span>特级树子报表</span>
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
              <input type="hidden" name="directV4ParentIdEQ" value="${directV4ParentIdEQ}"/>
              <div class="form-group">
                <input type="text" name="nicknameLK" class="form-control" placeholder="服务商昵称" value="${nickname}"/>
              </div>
              <div class="form-group">
                <input type="text" name="phoneEQ" class="form-control" placeholder="服务商手机"/>
              </div>

              <div class="form-group input-inline">
                <input class="Wdate form-control" type="text" id="beginDate"
                       onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',readOnly:true})" name="createdTimeGTE" value="" placeholder="下单时间起"/>
              </div>
              <div class="form-group input-inline">
                <input class="Wdate form-control" type="text" id="endDate" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',readOnly:true})"
                       name="createdTimeLT" value="" placeholder="下单时间止"/>
              </div>
              <div class="form-group input-inline">
                <input class="Wdate form-control" type="text" id="beginDate"
                       onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',readOnly:true})" name="paidTimeGTE" value="" placeholder="支付时间起"/>
              </div>
              <div class="form-group input-inline">
                <input class="Wdate form-control" type="text" id="endDate" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',readOnly:true})"
                       name="paidTimeLT" value="" placeholder="支付时间止"/>
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
