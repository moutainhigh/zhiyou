<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/include/taglib.jsp"%>
<%@ attribute name="id" type="java.lang.String" required="false" rtexprvalue="true" %>
<%@ attribute name="page" type="com.zy.common.model.query.Page" required="true" rtexprvalue="true" %>
<%@ attribute name="queryString" type="java.lang.String" required="false" rtexprvalue="true" %>
<c:if test="${page.total > 0 && not empty page.pageSize}">
<div class="pagination" id="${id}">
  <%
	long total = page.getTotal(); //?
  	int pageSize = page.getPageSize(); //?
  	int pageNumber = page.getPageNumber();
  	int pageCount = page.getPageCount();
    
  	int minPageNumber = 0;
  	int maxPageNumber = pageCount - 1;
    
	boolean first = pageNumber == minPageNumber;
	boolean last = pageNumber == maxPageNumber;
  
	jspContext.setAttribute("pageSize", pageSize);
	jspContext.setAttribute("total", total);
	jspContext.setAttribute("pageNumber", pageNumber);
	jspContext.setAttribute("pageCount", pageCount);
	jspContext.setAttribute("maxPageNumber", maxPageNumber);
	jspContext.setAttribute("minPageNumber", minPageNumber);
	jspContext.setAttribute("first", first);
	jspContext.setAttribute("last", last);
  %>
  <c:if test="${pageCount > 1}">
  <a class="${first ? 'disabled' : ''}"<c:if test="${!first}"> href="?pageNumber=${minPageNumber}${empty queryString ? '' : '&'.concat(queryString)}"</c:if>>首页</a>
  <a class="${first ? 'disabled' : ''}"<c:if test="${!first}"> href="?pageNumber=${pageNumber - 1}${empty queryString ? '' : '&'.concat(queryString)}"</c:if>>上一页</a>
  <a class="${last ? 'disabled' : ''}"<c:if test="${!last}"> href="?pageNumber=${pageNumber + 1}${empty queryString ? '' : '&'.concat(queryString)}"</c:if>>下一页</a>
  <a class="${last ? 'disabled' : ''}"<c:if test="${!last}"> href="?pageNumber=${maxPageNumber}${empty queryString ? '' : '&'.concat(queryString)}"</c:if>>尾页</a>
  </c:if>
</div>
</c:if>