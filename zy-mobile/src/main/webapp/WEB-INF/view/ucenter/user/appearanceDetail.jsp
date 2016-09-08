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
    $('.user-image img').click(function() {
      var url = $(this).attr('data-src');
      var title = '我的美照';
      $.imageview({
        url : url,
        title : title
      });
    });
  });
</script>
</head>
<body class="header-fixed">
  <header class="header">
    <h1>实名认证</h1>
    <a href="${ctx}/u/userInfo" class="button-left"><i class="fa fa-angle-left"></i></a>
    <c:if test="${appearance.confirmStatus != '已通过'}">
      <a href="${ctx}/u/appearance/edit" class="button-right"><i class="fa fa-edit"></i></a>
    </c:if>
  </header>

  <article>

    <c:if test="${appearance.confirmStatus == '待审核'}">
      <div class="note note-warning mb-0">
        <p>
          <i class="fa fa-clock-o"></i> 审核信息：${appearance.confirmStatus}
        </p>
      </div>
    </c:if>
    <c:if test="${appearance.confirmStatus == '未通过'}">
      <div class="note note-danger mb-0">
        <p>
          <i class="fa fa-close"></i> 审核信息：${appearance.confirmStatus}, ${appearance.confirmRemark}
        </p>
      </div>
    </c:if>
    <c:if test="${appearance.confirmStatus == '已通过'}">
      <div class="note note-success mb-0">
        <p>
          <i class="fa fa-check"></i> 审核信息：${appearance.confirmStatus}
        </p>
      </div>
    </c:if>

    <div class="list-group">

      <div class="list-item">
        <div class="list-text">姓名</div>
        <div class="list-unit">${appearance.realname}</div>
      </div>
      <div class="list-item">
        <div class="list-text">身份证号</div>
        <div class="list-unit">${appearance.idCardNumber}</div>
      </div>
      <div class="list-item">
        <div class="list-text">身份证正面照</div>
        <div class="list-unit">
          <img class="image-120-75" src="${appearance.image1Thumbnail}" data-src="${appearance.image1}">
        </div>
      </div>
      <div class="list-item">
        <div class="list-text">身份证反面照</div>
        <div class="list-unit">
          <img class="image-120-75" src="${appearance.image2Thumbnail}" data-src="${appearance.image2}">
        </div>
      </div>
    </div>

  </article>

</body>
</html>
