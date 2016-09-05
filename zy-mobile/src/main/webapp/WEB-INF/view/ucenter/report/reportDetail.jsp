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

<title>检测报告</title>
<%@ include file="/WEB-INF/view/include/head.jsp"%>
<script>

  $(function() {
    
    $('.image-view').click(function() {
      var url = $(this).attr('data-src');
      if (!url) {
        return;
      }
      $.imageview({
        url : url,
        title : '检测结果图片'
      });
    });
    
  });
  
</script>
</head>
<body>
  <header class="header">
    <h1>检测报告</h1>
    <a href="${ctx}/u" class="button-left"><i class="fa fa-angle-left"></i></a>
  </header>
  
  <div class="list-group">
    <div class="list-item">
      <div class="list-text">客户姓名</div>
      <div class="list-unit">${report.realname}</div>
    </div>
    <div class="list-item">
      <div class="list-text">性别</div>
      <div class="list-unit">${report.gender}</div>
    </div>
    <div class="list-item">
      <div class="list-text">年龄</div>
      <div class="list-unit">${report.age}岁</div>
    </div>
    <div class="list-item">
      <div class="list-text">检测时间</div>
      <div class="list-unit">${report.dateLabel}</div>
    </div>
    <div class="list-item">
      <div class="list-text">检测结果</div>
      <div class="list-unit">
        <c:choose>
          <c:when test="${report.reportResult == '阴性'}">
          <span class="font-red">${report.reportResult}</span>
          </c:when>
          <c:when test="${report.reportResult == '弱阳性'}">
          <span class="font-orange">${report.reportResult}</span>
          </c:when>
          <c:when test="${report.reportResult == '阳性'}">
          <span class="font-green">${report.reportResult}</span>
          </c:when>
          <c:when test="${report.reportResult == '干扰色'}">
          <span class="font-purple">${report.reportResult}</span>
          </c:when>
        </c:choose>
      </div>
    </div>
  </div>
  
  <div class="list-title">检测结果图片</div>
  <div class="list-group">
    <div class="list-item">
      <div class="list-text list-image">
        <div class="image-item">
          <img class="image-view" src="${report.image1Thumbnail}" data-src="${report.image1Big}">
        </div>
        <div class="image-item">
          <img class="image-view" src="${report.image2Thumbnail}" data-src="${report.image2Big}">
        </div>
        <div class="image-item">
          <img class="image-view" src="${report.image3Thumbnail}" data-src="${report.image3Big}">
        </div>
        <c:if test="${not empty report.image4Thumbnail}">
        <div class="image-item">
          <img class="image-view" src="${report.image4Thumbnail}" data-src="${report.image4Big}">
        </div>
        </c:if>
        <c:if test="${not empty report.image5Thumbnail}">
        <div class="image-item">
          <img class="image-view" src="${report.image5Thumbnail}" data-src="${report.image5Big}">
        </div>
        </c:if>
        <c:if test="${not empty report.image6Thumbnail}">
        <div class="image-item">
          <img class="image-view" src="${report.image6Thumbnail}" data-src="${report.image6Big}">
        </div>
        </c:if>
      </div>
    </div>
  </div>
  
  <div class="list-title">客户使用心得</div>
  <div class="list-group">
    <div class="list-item">
      <div class="list-text font-777">${report.text}</div>
    </div>
  </div>
</body>
</html>
