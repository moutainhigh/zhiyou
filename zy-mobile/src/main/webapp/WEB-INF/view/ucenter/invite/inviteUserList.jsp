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
<meta name="keywords" content="微信分销" />
<meta name="description" content="我的团队" />

<title>我的团队</title>
<%@ include file="/WEB-INF/view/include/head.jsp"%>
<link rel="stylesheet" href="${stccdn}/css/ucenter/invite.css" />
<script type="text/javascript">
  $(function() {

  });
</script>
</head>
<body>
  <header class="header">
    <h1>我的团队成员</h1>
    <a href="${ctx}/ucenter" class="button-left"><i class="fa fa-angle-left"></i></a>
  </header>

  <article class="invite-list">
    <c:if test="${empty list}">
      <div class="empty-tip">
        <i class="fa fa-user"></i>
        <span>您还没有团队成员</span>
      </div>
    </c:if>

    <c:forEach items="${list}" var="inviteUser" varStatus="varStatus">
      <a class="invite" href="${ctx}/ucenter/invite/${inviteUser.id}">
        <div class="invite-wrap relative">
          <div class="fs-16 bold lh-24">${inviteUser.realname}</div>
          <div class="fs-12 lh-20">手机:${inviteUser.phone}, 代理等级:<span class="font-orange">${inviteUser.userRank}</span>
          </div>
          <span class="btn btn-sm orange">查看</span>
        </div>
      </a>
    </c:forEach>

  </article>
  <%@ include file="/WEB-INF/view/include/footer.jsp"%>
</body>
</html>
