<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/include/taglib.jsp"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="Cache-Control" content="no-store" />
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Expires" content="0" />
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="apple-mobile-web-app-status-bar-style" content="black">
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
<meta name="keywords" content="微信分销" />
<meta name="description" content="我的消息" />

<title>我的消息</title>
<%@ include file="/WEB-INF/view/include/head.jsp"%>
<link rel="stylesheet" href="${stccdn}/css/ucenter/message.css" />
<script type="text/javascript">
	$(function() {
		$('#btnReadAll').click(function(){
			$('#messageForm').submit();
		});
	});
</script>
</head>

<body class="header-fixed">

  <header class="header">
    <h1>我的消息</h1>
    <a href="${ctx}/u" class="button-left"><i class="fa fa-angle-left"></i></a>
    <c:if test="${not empty unreadMessageVos}">
    <a id="btnReadAll" href="javascript:;" class="button-right"><span>全部已读</span></a>
    </c:if>
  </header>
  
  <form id="messageForm" action="${ctx}/u/message/readAll" method="post"></form>
  
  <article>
    <c:if test="${empty unreadMessageVos && empty readMessageVos}">
    <div class="message-list">
      <div class="empty-tip">
        <i class="fa fa-comments"></i> <span>空空如也!</span>
      </div>
    </div>
    </c:if>

    <!-- 未读消息  -->
    <c:if test="${not empty unreadMessageVos}">
    <div class="list-group">
      <c:forEach items="${unreadMessageVos}" var="message">
      <a class="list-item" href="${ctx}/u/message/${message.id}">
        <div class="message">
          <div class="message-icon mt-5 left">
            <img alt="" src="${stccdn}/image/icon/icon_email.png">
          </div>
          <div class="message-info">
            <p class="font-black clearfix">
              <span class="fs-14 left">${message.messageType}</span><span class="fs-12 right">${message.createdTimeLabel}</span>
            </p>
            <p class="fs-12 font-999">${message.title}</p>
          </div>
        </div>
      </a>
      </c:forEach>
    </div>
    </c:if>

    <!-- 已读消息  -->
    <c:if test="${not empty readMessageVos}">
    <div class="list-group">
      <c:forEach items="${readMessageVos}" var="message">
      <a class="list-item" href="${ctx}/u/message/${message.id}">
        <div class="message">
          <div class="message-icon mt-5 left">
            <img alt="" src="${stccdn}/image/icon/icon_email_o.png">
          </div>
          <div class="message-info">
            <p class="font-777 clearfix">
              <span class="fs-14 left">${message.messageType}</span><span class="fs-12 right">${message.createdTimeLabel}</span>
            </p>
            <p class="fs-12 font-999">${message.title}</p>
          </div>
        </div>
      </a>
      </c:forEach>
    </div>
    </c:if>
  
  </article>

</body>
</html>
