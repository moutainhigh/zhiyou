<%@ page language="java" pageEncoding="UTF-8"%>
<c:if test="${not empty nav}">
<nav class="footer footer-nav flex">
  <a class="flex-1<c:if test="${nav == '0'}"> current</c:if>" href="${ctx}/"><i class="icon-home"></i><span>首页</span></a>
  <a class="flex-1<c:if test="${nav == '1'}"> current</c:if>" href="${ctx}/product/1"><i class="icon-cate"></i><span>服务</span></a>
  <a class="flex-1<c:if test="${nav == '2'}"> current</c:if>" href="${ctx}/activity"><i class="icon-activity"></i><span>活动</span></a>
  <a class="flex-1<c:if test="${nav == '3'}"> current</c:if>" href="${ctx}/u"><i class="icon-user"></i><span>我的</span></a>
</nav>
</c:if>

<aside class="elevator">
  <a href="javascript:;" class="go-top"><i class="fa fa-angle-up"></i></a>
</aside>
