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
            title: '<input type="checkbox" name="checkAll" id="checkAll" value=""/>',
            render: function (data, type, full) {
              if(full.profitStatus == '待发放'){
                return '<input type="checkbox" name="id" id="id" value="' + data + '"/>';
              } else {
                return '<input type="checkbox" name="id" disabled="disabled" id="id" value="' + data + '"/>';
              }
            }
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
          },
          {
            data: '',
            title: '操作',
            orderable: false,
            render: function (data, type, full) {
              var optionHtml = '';
              <shiro:hasPermission name="profit:grant">
              if(full.profitStatus == '待发放'){
                optionHtml += '<a class="btn btn-xs default yellow-stripe" href="javascript:;" data-href="${ctx}/profit/grant?id=' + full.id + '" data-confirm="您确定发放奖励？"><i class="fa fa-send-o"></i> 发放 </a>';
              }
              </shiro:hasPermission>
              <shiro:hasPermission name="profit:cancel">
	              optionHtml += '<a class="btn btn-xs default yellow-stripe" href="javascript:;" data-href="${ctx}/profit/cancel?id=' + full.id + '" data-confirm="您确定取消奖励？"><i class="fa fa-send-o"></i> 取消 </a>';
              </shiro:hasPermission>
              return optionHtml;
            }
          }]
      }
    });

    $('#dataTable').on('click', '#checkAll', function(){
      var isChecked = $(this).attr("checked");
      if(isChecked == 'checked'){
        $("input[name='id']").each(function() {
        	var disableAttr = $(this).attr("disabled");
        	if(disableAttr == undefined){
        	  $(this).parent().addClass("checked");
        	  $(this).attr("checked",'true');
        	}
        });
      } else {
        $("input[name='id']").each(function() {
        	var disableAttr = $(this).attr("disabled");
        	if(disableAttr == undefined){
        	  $(this).parent().removeClass("checked");
        	  $(this).removeAttr("checked");
        	}
        });
      }
    }
    );
    
    <shiro:hasPermission name="profit:grant">
    $('.table-toolbar').on('click', '#grantBtn',function(){
      layer.confirm('您确认要批量发放奖励吗？', {
        btn: ['发放','取消']
      }, function(){
        var ids = '';
        $("input[name='id']").each(function() {
          var isChecked = $(this).attr("checked");
        	if(isChecked == 'checked'){
        	  ids += $(this).val() + ',';
        	}
        })
        if(ids.length > 0){
          $.ajax({
  	    	  url : '${ctx}/profit/batchGrant',
  	          dataType:"json",
  	          type: "post",
  	          data : {
  	        	  ids : ids
  	          },
  	          success: function( result ) {
  	        	  layer.alert(result.message, {icon: 1});
  	        	  grid.getDataTable().ajax.reload(null, false);
  	          } 
  	      });
          
        } else {
          layer.alert("请至少选择一条记录！", {icon: 2});
        }
        
      }, function(){
        
      });
    });
    </shiro:hasPermission>
    
    <shiro:hasPermission name="profit:cancel">
    $('.table-toolbar').on('click', '#cancelBtn',function(){
      layer.confirm('您确认要批量取消发放奖励吗？', {
        btn: ['确认','取消']
      }, function(){
        var ids = '';
        $("input[name='id']").each(function() {
          var isChecked = $(this).attr("checked");
        	if(isChecked == 'checked'){
        	  ids += $(this).val() + ',';
        	}
        })
        if(ids.length > 0){
          $.ajax({
  	    	  url : '${ctx}/profit/batchCancel',
  	          dataType:"json",
  	          type: "post",
  	          data : {
  	        	  ids : ids
  	          },
  	          success: function( result ) {
  	        	  layer.alert(result.message, {icon: 1});
  	        	  grid.getDataTable().ajax.reload(null, false);
  	          } 
  	      });
          
        } else {
          layer.alert("请至少选择一条记录！", {icon: 2});
        }
        
      }, function(){
        
      });
    });
    </shiro:hasPermission>
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
                <input class="Wdate form-control" type="text" id="beginDate"
                       onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',readOnly:true})" name="createdTimeGTE" value="" placeholder="收益创建时间起"/>
              </div>
              <div class="form-group">
                <input class="Wdate form-control" type="text" id="endDate" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',readOnly:true})"
                       name="createdTimeLT" value="" placeholder="收益创建时间止"/>
              </div>
              
              <div class="form-group">
                <input class="Wdate form-control" type="text" id="beginDate"
                       onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',readOnly:true})" name="grantedTimeGTE" value="" placeholder="收益发放时间起"/>
              </div>
              <div class="form-group">
                <input class="Wdate form-control" type="text" id="endDate" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',readOnly:true})"
                       name="grantedTimeLT" value="" placeholder="收益发放时间止"/>
              </div>
              
              <div class="form-group">
                <button class="btn blue filter-submit">
                  <i class="fa fa-search"></i> 查询
                </button>
              </div>
              
              <shiro:hasPermission name="profit:grant">
              <div class="form-group">
                <button type="button" class="btn green" id="grantBtn">
                  <i class="fa fa-send-o"></i> 批量发放
                </button>
              </div>
              </shiro:hasPermission>
              
              <shiro:hasPermission name="profit:cancel">
              <div class="form-group">
                <button type="button" class="btn cancel" id="cancelBtn">
                  <i class="fa fa-times"></i> 批量取消
                </button>
              </div>
              </shiro:hasPermission>
              
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