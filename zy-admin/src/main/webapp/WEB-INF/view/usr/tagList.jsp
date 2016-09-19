<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/view/include/head.jsp"%>
<%@ include file="/WEB-INF/view/include/form.jsp"%>
<!-- BEGIN JAVASCRIPTS -->
<script type="text/javascript">
function deleteTag(id) {
	alert(id);
}
</script>
<!-- BEGIN PAGE HEADER-->
<style>
.checker-wrap{
	display: inline-block;
	width: 200px; 
	margin-bottom: 5px;
}
</style>
<div class="page-bar">
  <ul class="page-breadcrumb">
    <li><i class="fa fa-home"></i> <a href="javascript:;" data-href="${ctx}/main">首页</a> <i class="fa fa-angle-right"></i></li>
    <li><a href="javascript:;" data-href="${ctx}/tag">标签库管理</a></li>
  </ul>
</div>
<!-- END PAGE HEADER-->

<div class="row">
  <div class="col-md-12">
    <!-- BEGIN VALIDATION STATES-->
    <div class="portlet box grey-cararra">
      <div class="portlet-title">
        <div class="caption">
          <i class="icon-tag"></i><span> 标签库管理</span>
        </div>
        <div class="actions">
          <a class="btn btn-circle green" data-href="${ctx}/tag/create">
            <i class="fa fa-plus"></i> 新增
          </a>
        </div>
      </div>
      <div class="portlet-body form">
        <!-- BEGIN FORM-->
        <form id="dataForm" data-action="" class="form-horizontal" method="post">
          <div class="form-body">
            <div class="alert alert-danger display-hide">
              <i class="fa fa-exclamation-circle"></i>
              <button class="close" data-close="alert"></button>
              <span class="form-errors">您填写的信息有误，请检查。</span>
            </div>
            <div class="form-group">
              <label class="control-label col-md-3"></label>
              <div class="col-md-5">
                <div class="checkbox-list">
                	<c:forEach items="${tagMap}" var="map">
                		<label class="label label-success" style="height: 26px;padding: 8px 6px;">${map.key}</label>
                		<c:forEach items="${map.value}" var="tag">
                			<div class="checker-wrap">
                			<a href="javascript:void(0)" data-href="${ctx}/tag/delete/${tag.id}" data-confirm="您确定要删除数据么？" title="点击删除标签">${tag.tagName}</a>
                			</div>
                		</c:forEach>
                	</c:forEach>
                </div>
              </div>
            </div>
          </div>
        </form>
        <!-- END FORM-->
      </div>
    </div>
    <!-- END VALIDATION STATES-->
  </div>
</div>
