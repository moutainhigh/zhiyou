<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=UTF-8"%>
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

<title>个人资料</title>
<%@ include file="/WEB-INF/view/include/head.jsp"%>
<script>
  $(function() {

  });
</script>
</head>
<body class="header-fixed">
  <header class="header">
    <h1>个人资料</h1>
    <a href="${ctx}/u/userInfo" class="button-left"><i class="fa fa-angle-left"></i></a>
    <a href="${ctx}/u/portrait/edit" class="button-right"><i class="fa fa-edit"></i></a>
  </header>
  <article class="">
    <div class="list-group">
      <div class="list-item">
        <div class="list-text">所在地</div>
        <div class="list-unit">${portrait.province} ${portrait.city} ${portrait.district}</div>
      </div>
      <div class="list-item">
        <div class="list-text">性别</div>
        <div class="list-unit">${portrait.gender}</div>
      </div>

      <div class="list-item">
        <div class="list-text">生日</div>
        <div class="list-unit">${portrait.birthdayLabel}</div>
      </div>

      <div class="list-item">
        <div class="list-text">职业</div>
        <div class="list-unit">
          <span>${portrait.jobName}</span>
        </div>
      </div>
      
      <div class="list-item">
        <div class="list-text">标签</div>
        <div class="list-unit">
          <c:forEach items="${portrait.tagNames}" var="tag" varStatus="index">
            <em class="label mb-5 inline-block
            <c:if test="${index.index % 5 == 0 }"> blue</c:if>
            <c:if test="${index.index % 5 == 1 }"> red</c:if>
            <c:if test="${index.index % 5 == 2 }"> orange</c:if>
            <c:if test="${index.index % 5 == 3 }"> green</c:if>
            <c:if test="${index.index % 5 == 4 }"> yellow</c:if>
            ">${tag}</em>
          </c:forEach>
        </div>
      </div>
      
    </div>

  </article>
</body>
</html>
