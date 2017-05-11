<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/view/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text; charset=utf-8">
<meta http-equiv="Cache-Control" content="no-store" />
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Expires" content="0" />
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="apple-mobile-web-app-status-bar-style" content="black">
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">

<title>代付的活动报名单</title>
<%@ include file="/WEB-INF/view/include/head.jsp"%>
<script type="text/javascript">
  $(function() {

  });
</script>
</head>
<body>
  <header class="header">
    <a href="${ctx}/u/" class="button-left"><i class="fa fa-angle-left"></i></a>
  </header>

  <article>
    <c:if test="${not empty activityApplyVos}">
    <div class="list-group">
      <c:forEach items="${activityApplyVos}" var="vo">
      <c:if test="${vo.activityApplyStatus == '已报名'}">
        <a class="list-item invite" href="${ctx}/u/activity/activityApply/${vo.id}/payer">
      </c:if>
      <c:if test="${vo.activityApplyStatus == '已支付'}">
        <a class="list-item invite" href="javascript:;">
      </c:if>
        <div class="avatar">
          <img class="image-40 mr-10" src="${vo.activityImageThumbnail}">
        </div>
        <div class="list-text">
          <div class="fs-15">${vo.activityTitle}  代付金额: <label style="color: red;">${vo.amountLabel}</label></div>
          <div class="font-777 fs-14"><i class="fa fa-phone font-999"></i> ${vo.user.phone}  ${vo.user.nickname}</div>
        </div>
        <div class="list-unit">
          <c:if test="${vo.activityApplyStatus == '已报名'}">
            <label class="label green">去付款</label>
          </c:if>
          <c:if test="${vo.activityApplyStatus == '已支付'}">
            <label class="label blue">已付款</label>
          </c:if>
        </div>
        <c:if test="${vo.activityApplyStatus == '已报名'}">
        <i class="list-arrow"></i>
        </c:if>
      </a>
      </c:forEach>
    </div>
    </c:if>
  </article>
  <%@ include file="/WEB-INF/view/include/footer.jsp"%>
</body>
</html>
