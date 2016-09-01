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

<title>自我画像</title>
<%@ include file="/WEB-INF/view/include/head.jsp"%>
<%@ include file="/WEB-INF/view/include/validate.jsp"%>
<script>
  $(function() {

  });
</script>
</head>
<body class="header-fixed">
  <header class="header">
    <h1>自我画像</h1>
    <a href="${ctx}/u/userInfo" class="button-left"><i class="fa fa-angle-left"></i></a>
    <a href="${ctx}/u/portrait/edit" class="button-right"><i class="fa fa-edit"></i></a>
  </header>
  <article class="">
    <div class="list-group">
      <div class="list-item">
        <label class="list-label">所在地</label>
        <div class="list-text">
          <span>${portrait.province} ${portrait.city} ${portrait.district}</span>
        </div>
      </div>
      <div class="list-item">
        <label class="list-label">家乡</label>
        <div class="list-text">
          <span>${portrait.homeProvince} ${portrait.homeDistrict}</span>
        </div>
      </div>
      <div class="list-item">
        <label class="list-label">性别</label>
        <div class="list-text">
          <span>${portrait.gender}</span>
        </div>
      </div>

      <div class="list-item">
        <label class="list-label">生日</label>
        <div class="list-text">
          <span><fmt:formatDate value="${portrait.birthday}" pattern="yyyy年MM月dd日" /></span>
        </div>
      </div>

      <div class="list-item">
        <label class="list-label">收入水平</label>
        <div class="list-text">
          <span> <c:if test="${portrait.consumptionLevel  eq 'C2000'}">
                    2000元以下
                </c:if> <c:if test="${portrait.consumptionLevel  eq 'C2001_5000'}">
                      2001元 ~ 5000元
                  </c:if> <c:if test="${portrait.consumptionLevel  eq 'C5001_10000'}">
                      5001元 ~ 10000元
                  </c:if> <c:if test="${portrait.consumptionLevel  eq 'C10001_20000'}">
                      10001元 ~ 20000元
                  </c:if> <c:if test="${portrait.consumptionLevel  eq 'C20000'}">
                      20000元以上
                  </c:if>
          </span>
        </div>
      </div>

      <div class="list-item">
        <label class="list-label">职业</label>
        <div class="list-text">
          <span>${portrait.jobName}</span>
        </div>
      </div>

      <div style="min-height: 48px; padding: 12px 15px 12px 65px;">
        <label class="list-label left" style="width: 50px; margin-left: -50px;">标签</label>
        <div class="right">
          <c:forEach items="${portrait.tags}" var="tag" varStatus="index">
            <em
              class="label mb-5 inline-block
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
