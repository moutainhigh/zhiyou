<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/include/taglib.jsp"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<c:set var="stc" value="${pageContext.request.contextPath}"/>
<c:if test='${not empty result}'>
<script>
$(function() {
  switch ('${result.code}') {
    case '0':
      toastr.success('${result.message}', '提示信息');
      break;
    case '500':
      toastr.error('${result.message}', '错误信息');
      break;
    default:
      break;
  }
});
</script>
</c:if>
