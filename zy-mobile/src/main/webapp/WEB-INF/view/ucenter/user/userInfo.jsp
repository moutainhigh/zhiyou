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

<title>实名认证</title>
<%@ include file="/WEB-INF/view/include/head.jsp"%>
<script type="text/javascript">
  $(function() {
    $('.image-view').click(function() {
      var url = $(this).attr('data-src');
      $.imageview({
        url : url
      });
    });
  });
</script>
</head>
<body>
  <header class="header">
    <h1>实名认证</h1>
    <a href="${ctx}/u/info" class="button-left"><i class="fa fa-angle-left"></i></a>
    <c:if test="${userInfo.confirmStatus != '已通过'}">
      <a href="${ctx}/u/userInfo/edit" class="button-right"><i class="fa fa-edit"></i></a>
    </c:if>
  </header>

  <article>

    <c:if test="${userInfo.confirmStatus == '待审核'}">
      <div class="note note-warning mb-0">
        <i class="fa fa-clock-o"></i> 审核信息：${userInfo.confirmStatus}
      </div>
    </c:if>
    <c:if test="${userInfo.confirmStatus == '未通过'}">
      <div class="note note-danger mb-0">
        <i class="fa fa-close"></i> 审核信息：${userInfo.confirmStatus}, ${userInfo.confirmRemark}
      </div>
    </c:if>
    <c:if test="${userInfo.confirmStatus == '已通过'}">
      <div class="note note-success mb-0">
        <i class="fa fa-check"></i> 审核信息：${userInfo.confirmStatus}
      </div>
    </c:if>

    <div class="list-group">
      <div class="list-item">
        <div class="list-text">姓名</div>
        <div class="list-unit">${userInfo.realname}</div>
      </div>
      <div class="list-item">
        <div class="list-text">性别</div>
        <div class="list-unit">${userInfo.gender}</div>
      </div>
      <div class="list-item">
        <div class="list-text">生日</div>
        <div class="list-unit">${userInfo.birthdayLabel}</div>
      </div>
      <div class="list-item">
        <div class="list-text">所在地</div>
        <div class="list-unit">${userInfo.province} ${userInfo.city} ${userInfo.district}</div>
      </div>
      <div class="list-item">
        <div class="list-text">职业</div>
        <div class="list-unit">${userInfo.jobName}</div>
      </div>
      <%--
      <div class="list-item">
        <div class="list-text">标签</div>
        <div class="list-unit">
          <c:forEach items="${userInfo.tagNames}" var="tag" varStatus="index">
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
       --%>
    </div>
    
    <div class="list-group">
      <div class="list-title">身份证信息</div>
      <div class="list-item">
        <div class="list-text">身份证号</div>
        <div class="list-unit">${userInfo.idCardNumber}</div>
      </div>
      <div class="list-item">
        <div class="list-text">身份证正面照</div>
        <div class="list-unit">
          <img class="image-view image-120-75" src="${userInfo.image1Thumbnail}" data-src="${userInfo.image1}">
        </div>
      </div>
      <div class="list-item">
        <div class="list-text">身份证反面照</div>
        <div class="list-unit">
          <img class="image-view image-120-75" src="${userInfo.image2Thumbnail}" data-src="${userInfo.image2}">
        </div>
      </div>
    </div>

  </article>

</body>
</html>
