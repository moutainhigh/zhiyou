<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/view/include/head.jsp"%>
<%@ include file="/WEB-INF/view/include/form.jsp"%>
<!-- BEGIN JAVASCRIPTS -->
<script type="text/javascript">
  $(function(){
    
    $('.modify').click(function() {
      var $this = $(this);
      $this.hide();
      $this.parent().find('.jobName').show().focus();
    });
    
    $('.jobName').blur(function() {
      var $this = $(this);
      var val = $this.val();
      var id = $this.parent().find('.jobId').val();
      $.post('${ctx}/job/update', {id: id, jobName: val}, function(result) {
        if(result.code == 0) {
          $this.hide();
          var labelHref = $this.parent().find('.modify');
          labelHref.show().text(result.data);
        } else {
          layer.alert(result.message);
        }
      })
    });
    
  });
</script>
<!-- BEGIN PAGE HEADER-->
<style>
.checker-wrap {
  display: inline-block;
  width: 200px;
  margin-top: 10px;
}
</style>
<div class="page-bar">
  <ul class="page-breadcrumb">
    <li><i class="fa fa-home"></i> <a href="javascript:;" data-href="${ctx}/main">首页</a> <i class="fa fa-angle-right"></i></li>
    <li><a href="javascript:;" data-href="${ctx}/job">职位管理</a></li>
  </ul>
</div>
<!-- END PAGE HEADER-->

<div class="row">
  <div class="col-md-12">
    <!-- BEGIN VALIDATION STATES-->
    <div class="portlet box grey-cararra">
      <div class="portlet-title">
        <div class="caption">
          <i class="icon-badge"></i><span> 职位管理</span>
        </div>
        <div class="actions">
          <a class="btn btn-circle green" data-href="${ctx}/job/create"> <i class="fa fa-plus"></i> 新增
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
                  <label class="label label-success" style="height: 26px; padding: 8px 6px;">所有职位</label>
                  <c:forEach items="${jobs}" var="job">
                    <div class="checker-wrap">
                    <form data-action="${ctx}/job/update" class="form-horizontal" method="post">
                        <a href="javascript:void(0)" title="点击修改" class="modify">${job.jobName}</a>
                        <input type="hidden" name="id" class="jobId" value="${job.id}">
                        <input type="input" name="jobName" class="jobName" style="display: none;" value="${job.jobName}" />
                      </form>
                    </div>
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
