<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/view/include/head.jsp"%>
<!-- BEGIN JAVASCRIPTS -->
<script>
  $(function() {

  });
</script>
<!-- END JAVASCRIPTS -->

<form class="form-horizontal">
  <div class="form-body">

    <h4 class="form-section">基本信息:</h4>

    <div class="row">
      <div class="col-md-6">
        <div class="form-group">
          <label class="control-label col-md-3">进货数:</label>
          <div class="col-md-6">
            <p class="form-control-static">${inCount}</p>
          </div>
        </div>
      </div>
      <div class="col-md-6">
        <div class="form-group">
          <label class="control-label col-md-3">出货数:</label>
          <div class="col-md-9">
            <p class="form-control-static">${outCount}</p>
          </div>
        </div>
      </div>

    </div>

    <div class="row">
      <div class="col-md-6">
        <div class="form-group">
          <label class="control-label col-md-3">团队进货数:</label>
          <div class="col-md-9">
            <p class="form-control-static">${teamInCount}</p>
          </div>
        </div>
      </div>
      <div class="col-md-6">
        <div class="form-group">
          <label class="control-label col-md-3">团队出货数:</label>
          <div class="col-md-9">
            <p class="form-control-static">${teamOutCount}</p>
          </div>
        </div>
      </div>
    </div>

    <div class="row">
      <div class="col-md-6">
        <div class="form-group">
          <label class="control-label col-md-3">团队总数:</label>
          <div class="col-md-9">
            <p class="form-control-static">${teamCount}</p>
          </div>
        </div>
      </div>
    </div>

  </div>

</form>