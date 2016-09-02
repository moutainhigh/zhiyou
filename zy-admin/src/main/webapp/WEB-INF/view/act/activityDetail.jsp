<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/view/include/head.jsp" %>
<%@ include file="/WEB-INF/view/include/editor.jsp" %>
<!-- BEGIN JAVASCRIPTS -->
<style>

</style>
<script>
  $(function () {

  });
</script>
<!-- END JAVASCRIPTS -->

<!-- BEGIN PAGE HEADER-->
<div class="page-bar">
  <ul class="page-breadcrumb">
    <li><i class="fa fa-home"></i> <a href="javascript:;" data-href="${ctx}/main">首页</a> <i class="fa fa-angle-right"></i></li>
    <li><a href="javascript:;" data-href="${ctx}/activity">活动管理</a></li>
  </ul>
</div>
<!-- END PAGE HEADER-->

<div class="row">
  <div class="col-md-12">
    <!-- BEGIN VALIDATION STATES-->
    <div class="portlet light bordered">
      <div class="portlet-title">
        <div class="caption">
          <i class="icon-social-dropbox"></i> 查看活动
        </div>
      </div>
      <div class="portlet-body form">
        <!-- BEGIN FORM-->
        <form class="form-horizontal">
          <div class="form-body">
            <div class="form-group">
              <label class="control-label col-md-3">标题<span class="required"> * </span></label>
              <div class="col-md-5">
                <p class="form-control-static">${activity.title}</p>
              </div>
            </div>
            <div class="form-group">
              <label class="control-label col-md-3">图片<span class="required"> * </span></label>
              <div class="col-md-5">
                <img data-target="image" class="product-image bd" src="${activity.imageBig}">
              </div>
            </div>
            <div class="form-group">
              <label class="control-label col-md-3">省市区<span class="required"> * </span></label>
              <div class="col-md-5">
                <p class="form-control-static">${activity.province} - ${activity.city} - ${activity.district}</p>
              </div>
            </div>
            <div class="form-group">
              <label class="control-label col-md-3">地址<span class="required"> * </span></label>
              <div class="col-md-5">
                <p class="form-control-static">${activity.address}</p>
              </div>
            </div>
            <div class="form-group">
              <label class="control-label col-md-3">开始时间<span class="required"> * </span></label>
              <div class="col-md-5">
                <p class="form-control-static">${activity.startTimeLabel}</p>
              </div>
            </div>

            <div class="form-group">
              <label class="control-label col-md-3">结束时间<span class="required"> * </span></label>
              <div class="col-md-5">
                <p class="form-control-static">${activity.endTimeLabel}</p>
              </div>
            </div>

            <div class="form-group">
              <label class="control-label col-md-3">报名截止时间<span class="required"> * </span></label>
              <div class="col-md-5">
                <p class="form-control-static">${activity.applyDeadlineLabel}</p>
              </div>
            </div>

            <ul class="nav nav-tabs">
              <li class="active">
                <a href="#detail" data-toggle="tab" aria-expanded="true"> 详情 </a>
              </li>
              <li class="">
                <a href="#applies" data-toggle="tab" aria-expanded="false"> 报名 </a>
              </li>
              <li class="">
                <a href="#signIns" data-toggle="tab" aria-expanded="false"> 签到 </a>
              </li>
              <li class="">
                <a href="#collects" data-toggle="tab" aria-expanded="false"> 关注 </a>
              </li>
            </ul>

            <div class="tab-content">
              <div class="tab-pane fade active in" id="detail">
                <div>
                  ${activity.detail}
                </div>
              </div>
              <div class="tab-pane fade" id="applies">
                <table class="table table-hover">
                  <thead>
                  <tr>
                    <th> 申请人 </th>
                    <th> 申请时间 </th>
                    <th> 状态 </th>
                    <th> 邀请人 </th>
                  </tr>
                  </thead>
                  <tbody>
                  <c:forEach items="${activity.activityApplies}" var="activityApply">
                  <tr>
                    <td><img src="${activityApply.user.avatarThumbnail}" /> ${activityApply.user.nickname}</td>
                    <td>${activityApply.appliedTimeLabel}</td>
                    <td>${activityApply.isCancelled ? '已取消' : '已报名'}</td>
                    <td><c:if test="${not empty activity}"><img src="${activityApply.inviter.avatarThumbnail}" /> ${activityApply.user.nickname}</c:if><c:if test="${empty activity}">-</c:if><</td>
                  </tr>
                  </c:forEach>
                  </tbody>
                </table>
              </div>
              <div class="tab-pane fade" id="signIns">
                <p> Etsy mixtape wayfarers, ethical wes anderson tofu before they sold out mcsweeney's organic lomo retro fanny pack lo-fi farm-to-table readymade. Messenger bag gentrify pitchfork tattooed craft beer, iphone
                  skateboard locavore carles etsy salvia banksy hoodie helvetica. DIY synth PBR banksy irony. Leggings gentrify squid 8-bit cred pitchfork. Williamsburg banh mi whatever gluten-free, carles pitchfork biodiesel
                  fixie etsy retro mlkshk vice blog. Scenester cred you probably haven't heard of them, vinyl craft beer blog stumptown. Pitchfork sustainable tofu synth chambray yr. </p>
              </div>
              <div class="tab-pane fade" id="collects">
                <p> Trust fund seitan letterpress, keytar raw denim keffiyeh etsy art party before they sold out master cleanse gluten-free squid scenester freegan cosby sweater. Fanny pack portland seitan DIY, art party locavore
                  wolf cliche high life echo park Austin. Cred vinyl keffiyeh DIY salvia PBR, banh mi before they sold out farm-to-table VHS viral locavore cosby sweater. Lomo wolf viral, mustache readymade thundercats
                  keffiyeh craft beer marfa ethical. Wolf salvia freegan, sartorial keffiyeh echo park vegan. </p>
              </div>
            </div>

            <%--<div class="form-group">
              <label class="control-label col-md-3">活动详情<span class="required"> * </span></label>
              <div class="col-md-5">
                <div class="form-control-static">
                  ${}
                </div>
              </div>
            </div>--%>

            <div class="form-actions fluid">
              <div class="col-md-offset-3 col-md-9">
                <button class="btn default" data-href="${ctx}/activity">
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
