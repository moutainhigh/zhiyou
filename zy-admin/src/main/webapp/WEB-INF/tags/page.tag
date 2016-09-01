<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/view/include/taglib.jsp"%>
<%@ attribute name="id" type="java.lang.String" required="false" rtexprvalue="true" description="id"%>
<%@ attribute name="page" type="com.zy.common.model.query.Page" required="false" rtexprvalue="true" description="page data"%>
<%@ attribute name="cssClass" type="java.lang.String" required="false" rtexprvalue="true" description="css class"%>
<%@ attribute name="length" type="java.lang.Integer" required="false" rtexprvalue="true" description="page bar length"%>
<%@ attribute name="callback" type="java.lang.String" required="false" rtexprvalue="true" description="page request function name"%>
<%
%>
<c:if test="${empty page}">
  <c:set var="page" value="${filterContext.page}" />
</c:if>
<c:if test="${empty length}">
  <c:set var="length" value="1" />
</c:if>
<c:if test="${empty cssClass}">
  <c:set var="cssClass" value="pager" />
</c:if>
<c:if test="${empty callback}">
  <c:set var="callback" value="onSearch" />
</c:if>

<div class="${cssClass}" id="${id}">
  <span class="total">共<strong>${page.rowCount}</strong>条记录</span>
  <span class="text"><i>${page.pageNumber}</i> / ${page.pageCount} 页</span><span class="separator"></span>
  <span class="${page.first?'disabled':'button'}"><a class="prev" href="javascript:<c:if test="${page.first}">void(0)</c:if><c:if test="${!page.first}">${callback}(${page.pageNumber-1})</c:if>;">上一页</a></span>
  <c:if test="${page.pageCount>0 && length>0}">
    <span class="button"><a<c:if test="${page.pageNumber==1}"> class="current"</c:if> href="javascript:${callback}(1);">1</a></span>
    <c:if test="${page.pageNumber>length+3}"><span class="text">...</span></c:if>
    <c:forEach begin="${page.pageNumber>length+3?page.pageNumber-length:2}" end="${page.pageNumber<page.pageCount-2-length?page.pageNumber+length:page.pageCount-1}" var="pageIndex">
    <span class="button"><a<c:if test="${page.pageNumber==pageIndex}"> class="current"</c:if> href="javascript:${callback}(${pageIndex})">${pageIndex}</a></span>
    </c:forEach>
    <c:if test="${page.pageNumber<page.pageCount-2-length}"><span class="text">...</span></c:if>
    <c:if test="${page.pageCount>1}"><span class="button"><a<c:if test="${page.pageNumber==page.pageCount}"> class="current"</c:if> href="javascript:${callback}(${page.pageCount});">${page.pageCount}</a></span></c:if>
  </c:if>
  <span class="${page.last?'disabled':'button'}"><a class="next" href="javascript:<c:if test="${page.last}">void(0)</c:if><c:if test="${!page.last}">${callback}(${page.pageNumber+1})</c:if>;">下一页</a></span>
  <c:if test="${page.pageCount>0 && length>0}">
  <span class="separator"></span>
  <span class="text">到第<input type="text" id="_page_number" value="" />页</span><span class="button"><a href="javascript:void(0);" onclick="javascript:var _pageno=document.getElementById('_page_number').value;if(_pageno>${page.pageCount})_pageno=${page.pageCount};${callback}(_pageno);">确定</a></span>
  </c:if>
</div>