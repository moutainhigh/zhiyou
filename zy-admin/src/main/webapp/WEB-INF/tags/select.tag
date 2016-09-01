<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ tag import="java.util.Collection"%>
<%@ tag import="java.util.Map"%>
<%@ include file="/WEB-INF/view/include/taglib.jsp"%>
<%@ attribute name="items" type="java.lang.Object" required="true" rtexprvalue="true" description="data"%>
<%@ attribute name="id" type="java.lang.String" required="false" rtexprvalue="true" description="id"%>
<%@ attribute name="name" type="java.lang.String" required="false" rtexprvalue="true" description="select name"%>
<%@ attribute name="value" type="java.lang.String" required="false" rtexprvalue="true" description="selected value"%>
<%@ attribute name="cssClass" type="java.lang.String" required="false" rtexprvalue="true" description="css class"%>
<%@ attribute name="cssStyle" type="java.lang.String" required="false" rtexprvalue="true" description="css style"%>
<%@ attribute name="valueField" type="java.lang.String" required="false" rtexprvalue="true" description="valueField"%>
<%@ attribute name="labelField" type="java.lang.String" required="false" rtexprvalue="true" description="labelField"%>
<%@ attribute name="lvlField" type="java.lang.String" required="false" rtexprvalue="true" description="lvlField"%>
<%@ attribute name="allOption" type="java.lang.Boolean" required="false" rtexprvalue="true" description="allOption"%>
<c:if test="${empty allOption}">
	<c:set var="allOption" value="true" />
</c:if>
<%
	if (Map.class.isAssignableFrom(items.getClass())) {
%>
	<c:if test="${empty labelField}">
		<c:set var="labelField" value="label" />
	</c:if>
	<c:if test="${empty valueField}">
		<c:set var="valueField" value="value" />
	</c:if>
	<select class="${cssClass}" id="${id}" name="${name}" style="${cssStyle}">
		<c:if test="${allOption}">
			<option value="">-- 请选择 --</option>
		</c:if>
		<c:forEach items="${items}" var="item">
			<option value="${item.value[valueField]}" <c:if test="${value == item.value[valueField]}">selected="selected"</c:if>>${item.value[labelField]}</option>
		</c:forEach>
	</select>
<%
	} else if (Collection.class.isAssignableFrom(items.getClass())) {
%>
	<c:if test="${empty labelField}">
		<c:set var="labelField" value="name" />
	</c:if>
	<c:if test="${empty valueField}">
		<c:set var="valueField" value="id" />
	</c:if>
	<select class="${cssClass}" id="${id}" name="${name}" style="${cssStyle}">
		<c:if test="${allOption}">
			<option value="">-- 请选择 --</option>
		</c:if>
		<c:forEach items="${items}" var="item">
			<option value="${item[valueField]}" <c:if test="${value == item[valueField]}">selected="selected"</c:if>>
				${item[labelField]}
			</option>
		</c:forEach>
	</select>
<%
	}
%>
