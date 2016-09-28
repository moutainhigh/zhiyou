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
      var images = $(this).find('img');
      if (images.length == 0) {
        var url = $(this).attr('data-src');
        var title = $(this).attr('data-title');
        $.imageview({
          url : url,
          title : title
        });
      } else {
        var title = $(this).attr('data-title');
        var imageUrls = [];
        $.each(images, function(n, image) {
          imageUrls.push($(image).attr('data-src'));
        })
        $.imageview({
          url : imageUrls,
          title : title
        });
      }
    });
    
  });
  
</script>
</head>
<body>
  <header class="header">
    <h1>检测报告</h1>
    <a href="${ctx}/u/report" class="button-left"><i class="fa fa-angle-left"></i></a>
    <c:if test="${report.confirmStatus != '已通过'}">
    <a href="${ctx}/u/report/edit?id=${report.id}" class="button-right"><i class="fa fa-edit"></i></a>
    </c:if>
  </header>
  
  <article>
    <c:if test="${report.confirmStatus == '待审核'}">
    <div class="note note-warning mb-0">
      <i class="fa fa-clock-o"></i> 审核信息：待审核
    </div>
    </c:if>
    <c:if test="${report.confirmStatus == '未通过'}">
    <div class="note note-danger mb-0">
      <i class="fa fa-close"></i> 审核信息：未通过, ${report.confirmRemark}
    </div>
    </c:if>
    <c:if test="${report.confirmStatus == '已通过'}">
    <div class="note note-success mb-0">
      <i class="fa fa-check"></i> 审核信息：已通过
    </div>
    </c:if>
  
    <%-- 结算信息 --%>
    <c:if test="${report.isSettledUp && not empty profits}">
    
    <div class="list-group mt-10">
      <div class="list-title">我的收益</div>
      <c:forEach items="${profits}" var="profit">
      <div class="list-item">
        <i class="list-icon fa fa-cny"></i>
        <div class="list-text">${profit.profitType}</div>
        <div class="list-unit font-orange">${profit.amount}元</div>
      </div>
      </c:forEach>
      <c:forEach items="${transfers}" var="transfer">
      <div class="list-item">
        <i class="list-icon fa fa-cny"></i>
        <div class="list-text">${transfer.transferType}</div>
        <div class="list-unit font-orange">${transfer.amount}元</div>
      </div>
      </c:forEach>
    </div>
    
    </c:if>
  
    <div class="list-group">
      <div class="list-title">客户资料</div>
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
        <div class="list-text">手机号</div>
        <div class="list-unit">${report.phone}</div>
      </div>
      <div class="list-item">
        <div class="list-text">地区</div>
        <div class="list-unit">${report.province} ${report.city} ${report.district}</div>
      </div>
      <div class="list-item">
        <div class="list-text">职业</div>
        <div class="list-unit">${report.jobName}</div>
      </div>
    </div>
    
    <div class="list-group">
      <div class="list-title">检测信息</div>
      <div class="list-item">
        <div class="list-text">检测次数</div>
        <div class="list-unit">第 ${report.times} 次检测</div>
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
      <div class="list-item">
        <div class="list-text list-image image-view" data-title="检测报告图片">
        <c:forEach items="${report.images}" var="image" varStatus="varStatus">
          <img src="${report.imageThumbnails[varStatus.index]}" data-src="${report.imageBigs[varStatus.index]}">
        </c:forEach>
        </div>
      </div>
    </div>
    
    <div class="list-group">
      <div class="list-title">客户使用心得</div>
      <div class="list-item">
        <div class="list-text font-777">${report.text}</div>
      </div>
    </div>
  </article>
</body>
</html>
