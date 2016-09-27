<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/view/include/head.jsp"%>
<!-- BEGIN JAVASCRIPTS -->
<script>
  $(function() {
    var setting = {
      async: {
        enable: true,
        url: '${ctx}/stastics',
        autoParam:['id=parentId']
      },
      callback: {
        onClick: function(event, treeId, treeNode) {
          $.ajax({
            url: '${ctx}/stastics/order?userId=' + treeNode.id,
            dataType: 'html',
            success: function(data) {
              layer.open({
                type: 1,
                skin: 'layui-layer-rim', //加上边框
                area: ['960px', '640px'], //宽高
                content: data
              });
            }
          })

        }
      }
    };

    $.fn.zTree.init($("#tree"), setting);

  });
</script>
<!-- END JAVASCRIPTS -->

<!-- BEGIN PAGE HEADER-->
<div class="page-bar">
  <ul class="page-breadcrumb">
    <li><i class="fa fa-home"></i> <a href="javascript:;" data-href="${ctx}/main">首页</a> <i class="fa fa-angle-right"></i></li>
    <li><a href="javascript:;" data-href="${ctx}/userTree">用户报表</a></li>
  </ul>
</div>
<!-- END PAGE HEADER-->

<div class="row">
  <div class="col-md-12">
    <!-- BEGIN VALIDATION STATES-->
    <div class="portlet light bordered">
      <div class="portlet-title">
        <div class="caption">
          <i class="icon-present"></i><span> 用户报表</span>
        </div>
      </div>
      <div class="portlet-body">
        <!-- BEGIN FORM-->
        <!-- END FORM-->
        <div class="row">
          <div class="col-md-2">

          </div>
          <div class="col-md-10 ztree" id="tree">

          </div>
        </div>
      </div>
    </div>
    <!-- END VALIDATION STATES-->
  </div>
</div>
