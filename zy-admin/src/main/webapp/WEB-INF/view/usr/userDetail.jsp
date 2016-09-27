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
    <li><a href="javascript:;" data-href="${ctx}/user">用户管理</a></li>
  </ul>
</div>
<!-- END PAGE HEADER-->

<div class="row">
  <div class="col-md-12">
    <!-- BEGIN VALIDATION STATES-->
    <div class="portlet light bordered">
      <div class="portlet-title">
        <div class="caption">
          <i class="icon-user"></i> 查看用户
        </div>
        <div class="actions">
          <a class="btn btn-circle default" data-href="${ctx}/user">
            <i class="fa fa-chevron-left"></i> 返回
          </a>
        </div>
      </div>
      <div class="portlet-body form">
        <form class="form-horizontal">
          <div class="form-body">

            <div class="row">
              <div class="col-md-6">
                <div class="form-group">
                  <label class="control-label col-md-3">昵称:</label>
                  <div class="col-md-9">
                    <p class="form-control-static">${user.nickname}</p>
                  </div>
                </div>
              </div>
              <div class="col-md-6">
                <div class="form-group">
                  <label class="control-label col-md-3">手机号:</label>
                  <div class="col-md-9">
                    <p class="form-control-static">${user.phone}</p>
                  </div>
                </div>
              </div>
            </div>

            <div class="row">
              <div class="col-md-6">
                <div class="form-group">
                  <label class="control-label col-md-3">用户类型:</label>
                  <div class="col-md-9">
                    <p class="form-control-static">${user.userType}</p>
                  </div>
                </div>
              </div>
              <div class="col-md-6">
                <div class="form-group">
                  <label class="control-label col-md-3">用户等级:</label>
                  <div class="col-md-9">
                    <p class="form-control-static">${user.userRank}</p>
                  </div>
                </div>
              </div>
            </div>

            <div class="row">
              <div class="col-md-6">
                <div class="form-group">
                  <label class="control-label col-md-3">qq:</label>
                  <div class="col-md-9">
                    <p class="form-control-static">${user.qq}</p>
                  </div>
                </div>
              </div>
              <div class="col-md-6">
                <div class="form-group">
                  <label class="control-label col-md-3">是否冻结:</label>
                  <div class="col-md-9">
                    <p class="form-control-static"><c:if test="${user.isFrozen}">已冻结</c:if></p>
                  </div>
                </div>
              </div>
            </div>

          </div>

        </form>

        <ul class="nav nav-tabs">
          <li class="active"><a href="#applies" data-toggle="tab" aria-expanded="false"> 用户认证信息 </a></li>
          <%-- <li class=""><a href="#signIns" data-toggle="tab" aria-expanded="false"> 收益单 <span class="badge badge-primary"> ${order.profits.size()} </span></a></li>
          <li class=""><a href="#collects" data-toggle="tab" aria-expanded="false"> 转账单 <span class="badge badge-primary"> ${order.transfers.size()} </span></a></li> --%>
        </ul>

        <div class="tab-content">

          <div class="tab-pane fade active in" id="applies">
            <table class="table table-hover table-bordered">
              <thead>
              <tr>
                <th>真实姓名</th>
                <th>身份证号</th>
                <th>图片1</th>
                <th>图片2</th>
                <th>审核状态</th>
                <th>审核备注</th>
                <th>申请时间</th>
                <th>审核通过时间</th>
                <th>性别</th>
                <th>生日</th>
                <th>职业</th>
                <th>所在地</th>
                <th>标签</th>
              </tr>
              </thead>
              <tbody>
              <c:if test="${empty user.userInfo}">
                <tr>
                  <td colspan="18">暂无数据</td>
                </tr>
              </c:if>
              <c:if test="${not empty user.userInfo}">
                <tr>
                  <td>${user.userInfo.realname}</td>
                  <td>${user.userInfo.idCardNumber}</td>
                  <td><c:if test="${not empty user.userInfo.image1Thumbnail}"><img class="imageview" data-url="${user.userInfo.image1}" src="${user.userInfo.image1Thumbnail}"/></c:if></td>
                  <td><c:if test="${not empty user.userInfo.image2Thumbnail}"><img class="imageview" data-url="${user.userInfo.image2}" src="${user.userInfo.image2Thumbnail}"/></c:if></td>
                  <td>${user.userInfo.confirmStatus}</td>
                  <td>${user.userInfo.confirmRemark}</td>
                  <td>${user.userInfo.appliedTime}</td>
                  <td>${user.userInfo.confirmedTime}</td>
                  <td>${user.userInfo.gender}</td>
                  <td>${user.userInfo.birthdayLabel}</td>
                  <td>${user.userInfo.jobName}</td>
                  <td>${user.userInfo.province} - ${user.userInfo.city} - ${user.userInfo.district}</td>
                  <td>${user.userInfo.tagNames}</td>
                </tr>
              </c:if>
              </tbody>
            </table>
          </div>
          <%-- <div class="tab-pane fade" id="signIns">
            <table class="table table-hover table-bordered">
              <thead>
              <tr>
                <th>单号</th>
                <th>标题</th>
                <th>币种</th>
                <th>金额</th>
                <th>用户</th>
                <th>创建时间</th>
                <th>收益时间</th>
                <th>收益单类型</th>
              </tr>
              </thead>
              <tbody>
              <c:if test="${empty order.profits}">
                <tr>
                  <td colspan="7">暂无数据</td>
                </tr>
              </c:if>
              <c:if test="${not empty order.profits}">
                <c:forEach items="${order.profits}" var="profit">
                  <tr>
                    <td>${profit.sn}</td>
                    <td>${profit.title}</td>
                    <td>${profit.currencyType}</td>
                    <td>${profit.amountLabel}</td>
                    <td><p><img src="${profit.user.avatarThumbnail}" width="30" height="30" style="border-radius: 40px !important; margin-right:5px"/>: ${profit.user.nickname}</p>
                      <p>手机号: ${profit.user.phone}</p>
                      <p>等级: ${profit.user.userRankLabel}</p></td>
                    <td>${profit.createdTimeLabel}</td>
                    <td>${profit.grantedTimeLabel}</td>
                    <td>${profit.profitType}</td>
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
                <th>单号</th>
                <th>标题</th>
                <th>状态</th>
                <th>币种</th>
                <th>金额</th>
                <th>转出用户</th>
                <th>转入用户</th>
                <th>创建时间</th>
                <th>转账时间</th>
                <th>转账类型</th>
                <th>转账备注</th>
                <th>备注</th>
              </tr>
              </thead>
              <tbody>
              <c:if test="${empty order.transfers}">
                <tr>
                  <td colspan="12">暂无数据</td>
                </tr>
              </c:if>
              <c:if test="${not empty order.transfers}">
                <c:forEach items="${order.transfers}" var="transfer">
                  <tr>
                    <td>${transfer.sn}</td>
                    <td>${transfer.title}</td>
                    <td><label class="label label-${transfer.transferStatusStyle}">${transfer.transferStatus}</label></td>
                    <td>${transfer.currencyType}</td>
                    <td>${transfer.amount}</td>
                    <td><p><img src="${transfer.fromUser.avatarThumbnail}" width="30" height="30"
                                style="border-radius: 40px !important; margin-right:5px"/>: ${transfer.fromUser.nickname}</p>
                      <p>手机号: ${transfer.fromUser.phone}</p>
                      <p>等级: ${transfer.fromUser.userRankLabel}</p></td>
                    <td><p><img src="${transfer.toUser.avatarThumbnail}" width="30" height="30"
                                style="border-radius: 40px !important; margin-right:5px"/>: ${transfer.toUser.nickname}</p>
                      <p>手机号: ${transfer.toUser.phone}</p>
                      <p>等级: ${transfer.toUser.userRankLabel}</p></td>
                    <td>${transfer.createdTimeLabel}</td>
                    <td>${transfer.transferredTimeLabel}</td>
                    <td>${transfer.transferType}</td>
                    <td>${transfer.transferRemark}</td>
                    <td>${transfer.remark}</td>
                  </tr>
                </c:forEach>
              </c:if>
              </tbody>
            </table>
          </div>--%>
        </div> 
      </div>
    </div>
    <!-- END VALIDATION STATES-->
  </div>
</div>
