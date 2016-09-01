<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ tag import="java.util.Collection"%>
<%@ tag import="java.util.Map"%>
<%@ include file="/WEB-INF/view/include/taglib.jsp"%>
<%@ attribute name="items" type="java.lang.Object" required="true" rtexprvalue="true" description="data"%>
<%@ attribute name="id" type="java.lang.String" required="false" rtexprvalue="true" description="id"%>
<%@ attribute name="name" type="java.lang.String" required="false" rtexprvalue="true" description="checkbox name"%>
<%@ attribute name="value" type="java.lang.String" required="false" rtexprvalue="true" description="selected value"%>
<%@ attribute name="cssClass" type="java.lang.String" required="false" rtexprvalue="true" description="css class"%>
<%@ attribute name="cssStyle" type="java.lang.String" required="false" rtexprvalue="true" description="css style"%>
<%@ attribute name="valueField" type="java.lang.String" required="false" rtexprvalue="true" description="valueField"%>
<%@ attribute name="labelField" type="java.lang.String" required="false" rtexprvalue="true" description="labelField"%>
<%
	if (Map.class.isAssignableFrom(items.getClass())) {
%>
<c:if test="${empty labelField}">
	<c:set var="labelField" value="label" />
</c:if>
<c:if test="${empty valueField}">
	<c:set var="valueField" value="value" />
</c:if>
<c:forEach items="${items}" var="item">
	<input type="checkbox" value="${item.value[valueField]}" name="${name}" style="${cssStyle}" class="${cssClass}"<c:if test="${value == item.value[valueField]}"> checked="checked"</c:if> />
	<label>${item.value[labelField]}</label>
</c:forEach>
<%
	} else if (Collection.class.isAssignableFrom(items.getClass())) {
%>
<c:if test="${empty labelField}">
	<c:set var="labelField" value="name" />
</c:if>
<c:if test="${empty valueField}">
	<c:set var="valueField" value="id" />
</c:if>
<c:forEach items="${items}" var="item">
	<input type="checkbox" value="${item.value[valueField]}" name="${name}" style="${cssStyle}" class="${cssClass}"<c:if test="${value == item.value[valueField]}"> checked="checked"</c:if> />
	<label>${item.value[labelField]}</label>
</c:forEach>
<%
	}
%>
