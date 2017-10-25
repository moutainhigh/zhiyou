<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/view/include/head.jsp" %>

<div class="row">
    <div class="col-md-12">
        <div class="portlet light bordered">
            <c:forEach items="${largeAreaTypes}" var="largeAreaType">
                <a href="javascript:;" data-href="${ctx}/newReport/base?type=${largeAreaType.systemValue}">
                    <div style="width: 20%;float: left;padding-left: 5%;">
                            ${largeAreaType.systemName}部大区
                    </div>
                </a>
            </c:forEach>
        </div>
    </div>
</div>
