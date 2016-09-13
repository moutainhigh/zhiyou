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

<title>${user.nickname}</title>
<%@ include file="/WEB-INF/view/include/head.jsp"%>
<script type="text/javascript">
  $(function() {

  });
</script>
</head>
<body>
  <header class="header">
    <h1>${user.nickname}</h1>
    <a href="${ctx}/u/team" class="button-left"><i class="fa fa-angle-left"></i></a>
  </header>

  <article>
    
    <ul class="breadcrumb mt-10">
      <li><a href="${ctx}/u/team/"><i class="fa fa-home"></i> 我的团队</a></li>
      <c:if test="${not empty parentLv3 && principalUserId != parentLv3.id}">
      <li><a href="${ctx}/u/team/">${parentLv3.nickname}</a></li>
      </c:if>
      <c:if test="${not empty parentLv2 && principalUserId != parentLv2.id}">
      <li><a href="${ctx}/u/team/${parentLv2.id}">${parentLv2.nickname}</a></li>
      </c:if>
      <c:if test="${not empty parentLv1 && principalUserId != parentLv1.id}">
      <li><a href="${ctx}/u/team/${parentLv1.id}">${parentLv1.nickname}</a></li>
      </c:if>
      <li><a href="javascript:;">${user.nickname}</a></li>
    </ul>
    
    <div class="list-group">
      <div class="list-item">
        <i class="list-icon fa fa-user"></i>
        <div class="list-text">昵称</div>
        <div class="list-unit">${user.nickname}</div>
        <img class="image-40 round ml-10" src="${user.avatarThumbnail}">
      </div>
      <div class="list-item">
        <i class="list-icon fa fa-phone"></i>
        <div class="list-text">手机</div>
        <div class="list-unit">${user.phone}</div>
      </div>
      <div class="list-item">
        <i class="list-icon fa fa-map-marker"></i>
        <div class="list-text">地区</div>
        <div class="list-unit">${address.province} ${address.city} ${address.district}</div>
      </div>
    </div>
    
    <c:if test="${not empty list}">
    <div class="list-group">
      <div class="list-title">&lt;${user.nickname}&gt;的直接下级代理</div>
      <c:forEach items="${list}" var="inviteUser" varStatus="varStatus">
      <a class="list-item invite" href="${ctx}/u/team/${inviteUser.id}">
        <div class="avatar">
          <img class="image-40 round mr-10" src="${inviteUser.avatarThumbnail}">
        </div>
        <div class="list-text">
          <div class="fs-15">${inviteUser.nickname}</div>
          <div class="font-777 fs-14"><i class="fa fa-phone font-999"></i> ${inviteUser.phone}</div>
        </div>
        <div class="list-unit">
          <c:if test="${inviteUser.userRank == 'V1'}"><label class="label purple">三级代理</label></c:if>
          <c:if test="${inviteUser.userRank == 'V2'}"><label class="label blue">二级代理</label></c:if>
          <c:if test="${inviteUser.userRank == 'V3'}"><label class="label orange">一级代理</label></c:if>
          <c:if test="${inviteUser.userRank == 'V4'}"><label class="label red">特级代理</label></c:if>
        </div>
        <i class="list-arrow"></i>
      </a>
      </c:forEach>
    </div>
    </c:if>
  </article>
  <%@ include file="/WEB-INF/view/include/footer.jsp"%>
</body>
</html>
