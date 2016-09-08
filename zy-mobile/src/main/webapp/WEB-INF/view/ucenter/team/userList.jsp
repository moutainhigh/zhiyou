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
<body class="invite-list">
  <header class="header">
    <h1>我的团队</h1>
    <a href="${ctx}/u" class="button-left"><i class="fa fa-angle-left"></i></a>
  </header>

  <article>
    <c:if test="${empty list}">
      <div class="empty-tip">
        <i class="fa fa-user"></i>
        <span>您还没有团队成员</span>
      </div>
    </c:if>
    
    <div class="list-group">
      <div class="list-item">
        <i class="list-icon fa fa-users"></i>
        <div class="list-text">直接下级代理数</div>
        <div class="list-unit">2</div>
      </div>
      <div class="list-item">
        <i class="list-icon fa fa-users"></i>
        <div class="list-text">下级代理总数</div>
        <div class="list-unit">12</div>
      </div>
    </div>
    
    <div class="list-group">
      <div class="list-title">我的直接下级代理</div>
      <a class="list-item invite" href="${ctx}/ucenter/invite/${inviteUser.id}">
        <div class="avatar"><img class="image-40 round mr-10" src="http://image.mayishike.com/avatar/6fa938ce-9350-4df1-84aa-c03d5cd01947"></div>
        <div class="list-text">
          <div class="fs-15">哆来嘧</div>
          <div class="font-777 fs-14"><i class="fa fa-phone font-999"></i> 13747388829</div>
        </div>
        <div class="list-unit"><label class="label purple">三级代理</label></div>
        <i class="list-arrow"></i>
      </a>
      <a class="list-item invite" href="${ctx}/ucenter/invite/${inviteUser.id}">
        <div class="avatar"><img class="image-40 round mr-10" src="http://image.mayishike.com/avatar/6fa938ce-9350-4df1-84aa-c03d5cd01947"></div>
        <div class="list-text">
          <div class="fs-15">发索拉</div>
          <div class="font-777 fs-14"><i class="fa fa-phone font-999"></i> 13702971837</div>
        </div>
        <div class="list-unit"><label class="label red">特级代理</label></div>
        <i class="list-arrow"></i>
      </a>
    </div>
    
    <c:if test="${not empty list}">
    <div class="list-group">
      <div class="list-title">我的直接下级代理</div>
      <c:forEach items="${list}" var="inviteUser" varStatus="varStatus">
      <a class="list-item invite" href="${ctx}/ucenter/invite/${inviteUser.id}">
        <div class="avatar"><img class="image-40 round" src="http://image.mayishike.com/avatar/6fa938ce-9350-4df1-84aa-c03d5cd01947"></div>
        <div class="list-text">
          <div class="fs-15">${inviteUser.realname}</div>
          <div class="font-777 fs-14"><i class="fa fa-phone font-999">${inviteUser.phone}</i></div>
          <div class="list-unit">
            <c:if test="${user.userRank == 'V1'}"><label class="label purple">三级代理</label></c:if>
            <c:if test="${user.userRank == 'V2'}"><label class="label blue">二级代理</label></c:if>
            <c:if test="${user.userRank == 'V3'}"><label class="label orange">一级代理</label></c:if>
            <c:if test="${user.userRank == 'V4'}"><label class="label red">特级代理</label></c:if>
          </div>
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
