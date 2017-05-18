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
        <div class="actions">
          <a class="btn btn-circle default" data-href="${ctx}/activity">
            <i class="fa fa-chevron-left"></i> 返回
          </a>
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

            <div class="form-group">
              <label class="control-label col-md-3">活动限制人数<span class="required"> * </span></label>
              <div class="col-md-5">
                <p class="form-control-static">${activity.maxCount}</p>
              </div>
            </div>

            <div class="form-group">
              <label class="control-label col-md-3">活动票务类型<span class="required"> * </span></label>
              <div class="col-md-5">
                <c:if test="${activity.ticketType == 1}"><p class="form-control-static">自购</p></c:if>
                <c:if test="${activity.ticketType == 2}"><p class="form-control-static">团购</p></c:if>
              </div>
            </div>

            <ul class="nav nav-tabs">
              <li class="active">
                <a href="#detail" data-toggle="tab" aria-expanded="true"> 详情 </a>
              </li>
              <li class="">
                <a href="#applies" data-toggle="tab" aria-expanded="false"> 报名 <span class="badge badge-primary"> ${activity.activityApplies.size()} </span></a>
              </li>
              <li class="">
                <a href="#signIns" data-toggle="tab" aria-expanded="false"> 签到 <span class="badge badge-primary"> ${activity.activitySignIns.size()} </span></a>
              </li>
              <li class="">
                <a href="#collects" data-toggle="tab" aria-expanded="false"> 关注 <span class="badge badge-primary"> ${activity.activityCollects.size()} </span></a>
              </li>
              <li class="">
                <a href="#signInQrCode" data-toggle="tab" aria-expanded="false"> 签到二维码 </a>
              </li>
              <li class="">
                <a href="#detailQrCode" data-toggle="tab" aria-expanded="false"> 分享二维码 </a>
              </li>
            </ul>

            <div class="tab-content">
              <div class="tab-pane fade active in" id="detail">
                <div class="form-group">
                  <label class="control-label col-md-3">详情<span class="required"> * </span></label>
                  <div class="col-md-5">
                    <div style="width:750px;" class="form-control-static">${activity.detail}</div>
                  </div>
                </div>
              </div>
              <div class="tab-pane fade" id="signInQrCode">
                <div class="form-group">
                  <label class="control-label col-md-3">签到二维码<span class="required"> * </span></label>
                  <div class="col-md-5">
                    <img style="width:480px;height:480px;" src="${ctx}/activity/signInQrCode?id=${activity.id}"/>
                    <p class="help-block">请右键另存为并进行二次处理</p>
                  </div>
                </div>
              </div>
              <div class="tab-pane fade" id="detailQrCode">
                <div class="form-group">
                  <label class="control-label col-md-3">分享二维码<span class="required"> * </span></label>
                  <div class="col-md-5">
                    <img style="width:240px;height:240px;" src="${ctx}/activity/detailQrCode?id=${activity.id}"/>
                    <p class="help-block">打开微信扫一扫</p>
                  </div>
                </div>
              </div>
              <div class="tab-pane fade" id="applies">
                <table class="table table-hover table-bordered">
                  <thead>
                  <tr>
                    <th> 报名用户</th>
                    <th> 手机</th>
                    <th> 报名时间</th>
                    <th> 报名状态</th>
                    <th> 邀请人</th>
                    <th> 邀请人手机</th>
                  </tr>
                  </thead>
                  <tbody>
                  <c:if test="${empty activity.activityApplies}">
                    <tr>
                      <td colspan="6">暂无数据</td>
                    </tr>
                  </c:if>
                  <c:if test="${not empty activity.activityApplies}">
                    <c:forEach items="${activity.activityApplies}" var="activityApply">
                      <tr>
                        <td><img class="image-40 img-circle" src="${activityApply.user.avatarThumbnail}"/> ${activityApply.user.nickname}</td>
                        <td>${activityApply.user.phone}</td>
                        <td>${activityApply.appliedTimeLabel}</td>
                        <td><c:if test="${not activityApply.isCancelled}"><span class="font-green">已报名</span></c:if><c:if test="${activityApply.isCancelled}">已取消</c:if></td>
                        <td><c:if test="${not empty activityApply.inviter}"><img class="image-40 img-circle"
                                                                                 src="${activityApply.inviter.avatarThumbnail}"/> ${activityApply.inviter.nickname}</c:if></td>
                        <td><c:if test="${not empty activityApply.inviter}">${activityApply.inviter.phone}</c:if></td>
                      </tr>
                    </c:forEach>
                  </c:if>
                  </tbody>
                </table>
              </div>
              <div class="tab-pane fade" id="signIns">
                <table class="table table-hover table-bordered">
                  <thead>
                  <tr>
                    <th> 签到用户</th>
                    <th> 手机</th>
                    <th> 签到时间</th>
                  </tr>
                  </thead>
                  <tbody>
                  <c:if test="${empty activity.activitySignIns}">
                    <tr>
                      <td colspan="3">暂无数据</td>
                    </tr>
                  </c:if>
                  <c:if test="${not empty activity.activitySignIns}">
                    <c:forEach items="${activity.activitySignIns}" var="activitySignIn">
                      <tr>
                        <td><img class="image-40 img-circle" src="${activitySignIn.user.avatarThumbnail}"/> ${activitySignIn.user.nickname}</td>
                        <td>${activitySignIn.user.phone}</td>
                        <td>${activitySignIn.signedInTimeLabel}</td>
                      </tr>
                    </c:forEach>
                  </c:if>
                  </tbody>
                </table>
              </div>
              <div class="tab-pane fade" id="collects">
                <table class="table table-hover table-bordered">
                  <thead>
                  <tr>
                    <th> 关注者</th>
                    <th> 手机</th>
                    <th> 关注时间</th>
                  </tr>
                  </thead>
                  <tbody>
                  <c:if test="${empty activity.activityCollects}">
                    <tr>
                      <td colspan="3">暂无数据</td>
                    </tr>
                  </c:if>
                  <c:if test="${not empty activity.activityCollects}">
                    <c:forEach items="${activity.activityCollects}" var="activityCollect">
                      <tr>
                        <td><img class="image-40 img-circle" src="${activityCollect.user.avatarThumbnail}"/> ${activityCollect.user.nickname}</td>
                        <td>${activityCollect.user.phone}</td>
                        <td>${activityCollect.collectedTimeLabel}</td>
                      </tr>
                    </c:forEach>
                  </c:if>
                  </tbody>
                </table>
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

        </form>
        <!-- END FORM-->
      </div>
    </div>
    <!-- END VALIDATION STATES-->
  </div>
</div>
