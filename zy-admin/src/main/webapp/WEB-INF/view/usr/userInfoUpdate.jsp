<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/view/include/head.jsp"%>
<!-- BEGIN JAVASCRIPTS -->
<script>

	var area = new areaInit('province', 'city', 'district', '${userInfo.areaId}');

  $(function() {
    $('#form').validate({
	    rules : {
		    'realname': {
			    required: true
		    },
		    'birthday': {
			    required: true
		    },
		    'gender': {
			    required: true
		    },
		    'areaId': {
			    required: true
		    },
		    'jobId': {
			    required: true
		    }

	    }
    });

	  <%--$('.btn-submit').click(function() {--%>
		  <%--if($('#form').valid()) {--%>
			  <%--$.post('${ctx}/userInfo/update', $('#form').serialize(), function(data) {--%>
				  <%--if(data.code == 0) {--%>
					  <%--parent.grid.getDataTable().ajax.reload(null, false);--%>
					  <%--layer.closeAll();--%>
				  <%--} else {--%>
					  <%--layer.alert(data.message);--%>
				  <%--}--%>
			  <%--});--%>
      <%--}--%>
	  <%--});--%>
  });
</script>
<!-- END JAVASCRIPTS -->

<div class="row" style="width: 100%;">
  <div class="col-md-12">
    <!-- BEGIN VALIDATION STATES-->
    <div class="portlet light bordered">
      <div class="portlet-title">
        <div class="caption">
          <i class="icon-graduation"></i><span> 用户认证修改 </span>
        </div>
      </div>
      <div class="portlet-body form">
        <!-- BEGIN FORM-->
        <form id="form" action="" data-action="${ctx}/userInfo/update" class="form-horizontal" method="post">
          <input type="hidden" name="userId" value="${userInfo.userId}" />
          <input type="hidden" name="id" value="${userInfo.id}" />
          <div class="form-body">
            <div class="alert alert-danger display-hide">
              <i class="fa fa-exclamation-circle"></i>
              <button class="close" data-close="alert"></button>
              <span class="form-errors">您填写的信息有误，请检查。</span>
            </div>

            <div class="form-group">
              <label class="control-label col-md-3">真实姓名: <span class="required"> * </span>
              </label>
              <div class="col-md-4">
                  <input type="type" name="realname" class="form-control" value="${userInfo.realname}"/>
              </div>
            </div>

            <div class="form-group">
              <label class="control-label col-md-3">出生年月: <span class="required"> * </span>
              </label>
              <div class="col-md-4">
                 <c:if test="${userInfo.birthdayLabel == null}">
                     <input type="date" name="birthday" class="form-control" value="1900-01-01">
                 </c:if>
                 <c:if test="${userInfo.birthdayLabel != null}">
                     <input type="date" name="birthday" class="form-control" value="${userInfo.birthdayLabel}" placeholder="填写生日  1900-01-01">
                 </c:if>
              </div>
            </div>

            <div class="form-group">
              <label class="control-label col-md-3">性别: <span class="required"> * </span></label>
              <div class="col-md-4">
                <div class="radio-list form-control-static">
                  <label style="float: left;">
                    <input type="radio" value="0" name="gender"<c:if test="${userInfo.gender == '男'}"> checked="checked"</c:if>> 男
                  </label>
                  <label style="float: left; margin-left: 5px;">
                    <input type="radio" value="1" name="gender"<c:if test="${userInfo.gender == '女'}"> checked="checked"</c:if>> 女
                  </label>
                </div>
              </div>
            </div>

            <div class="form-group">
              <label class="control-label col-md-3">所在地<span class="required"> * </span></label>
              <div class="col-md-4">
                <select name="" id="province" class="form-control" style="width: 33%;float: left;">
                  <option value="">选择省</option>
                </select>
                <select name="" id="city" class="form-control" style="width: 33%;float: left;">
                  <option value="">选择市</option>
                </select>
                <select name="areaId" id="district" class="form-control" style="width: 33%;">
                  <option value="">选择区</option>
                </select>
              </div>
            </div>

            <div class="form-group">
              <label class="control-label col-md-3">职业<span class="required"> * </span></label>
              <div class="col-md-4">
                <select name="jobId" class="form-control">
                  <option value="">请选择</option>
                  <c:forEach items="${jobs}" var="job">
                    <option value="${job.id}"<c:if test="${userInfo.jobId eq job.id}"> selected="selected"</c:if>>${job.jobName}</option>
                  </c:forEach>
                </select>
              </div>
            </div>

          </div>
          <div class="form-actions fluid">
            <div class="col-md-offset-3 col-md-9">
                <button type="submit" class="btn green">
                    <i class="fa fa-save"></i> 保存
                </button>
            <button class="btn default" data-href="${ctx}/userInfo">
                <i class="fa fa-chevron-left"></i> 返回
            </button>
            </div>
          </div>
        </form>
        <!-- END FORM-->
      </div>
    </div>
    <!-- END VALIDATION STATES-->
  </div>
</div>
