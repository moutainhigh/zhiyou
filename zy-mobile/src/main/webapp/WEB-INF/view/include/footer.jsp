<%@ page language="java" pageEncoding="UTF-8"%>
<c:if test="${not empty nav}">
<nav class="footer footer-nav flex">
  <a class="flex-1<c:if test="${nav == '0'}"> current</c:if>" href="${ctx}/"><i class="fa fa-home"></i><span>首页</span></a>
  <a class="flex-1<c:if test="${nav == '1'}"> current</c:if>" href="${ctx}/product/1"><i class="fa fa-th-large"></i><span>商品</span></a>
  <a class="flex-1<c:if test="${nav == '2'}"> current</c:if>" href="${ctx}/activity"><i class="fa fa-life-ring"></i><span>活动</span></a>
  <c:if test="${empty principal}">
  <a class="flex-1<c:if test="${nav == '3'}"> current</c:if>" href="${ctx}/superLogin/11"><i class="fa fa-user"></i><span>我的</span></a>
  </c:if>
  <c:if test="${not empty principal}">
  <a class="flex-1<c:if test="${nav == '3'}"> current</c:if>" href="${ctx}/u"><i class="fa fa-user"></i><span>我的</span></a>
  </c:if>
</nav>
</c:if>

<aside class="elevator">
  <a href="javascript:;" class="go-top"><i class="fa fa-angle-up"></i></a>
</aside>
