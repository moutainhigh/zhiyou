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
    <title>通知公告列表</title>
    <%@ include file="/WEB-INF/view/include/head.jsp"%>
    <script src="${stccdn}/plugin/laytpl-1.1/laytpl.js"></script>

    <script type="text/javascript">
        $(function() {
            if (!$('.page-more').hasClass('disabled')) {
                $('.page-more').click(loadMore);
            }
        });

        var timeLT = '${timeLT}';
        var pageNumber = 0;

        function loadMore() {
            $.ajax({
                url : '${ctx}/u/notice',
                data : {
                    pageNumber : pageNumber + 1,
                    timeLT : timeLT
                },
                dataType : 'json',
                type : 'POST',
                success : function(result) {
                    if(result.code != 0) {
                        return;
                    }
                    var page = result.data.page;
                    if (page.data.length) {
                        timeLT = result.data.timeLT;
                        pageNumber = page.pageNumber;
                        var pageData = page.data;
                        for ( var i in pageData) {
                            var row = pageData[i];
                            buildRow(row);
                        }
                    }
                    if (!page.data.length || page.data.length < page.pageSize) {
                        $('.page-more').addClass('disabled').html('<span>没有更多数据了</span>').unbind('click', loadMore);
                    }
                }
            });
        }

        function buildRow(row){
            var rowTpl = document.getElementById('rowTpl').innerHTML;
            laytpl(rowTpl).render(row, function(html) {
                $('.list-group').append(html);
            });
        }
    </script>
    <script id="rowTpl" type="text/html">
        <a class="list-item article" href="${ctx}/u/notice/{{ d.id }}">
            <div class="list-text">
                <h2 class="fs-15 lh-24 o-hidden">{{ d.title }}</h2>
                <div class="font-777 fs-12">{{ d.createdTimeLabel }}</div>
                <i class="list-arrow" style="float: right;margin-right: 0;margin-top: -25px;"></i>
            </div>
        </a>
    </script>

</head>
<body>
<header class="header">
    <a href="${ctx}/u" class="button-left"><i class="fa fa-angle-left"></i></a>
    <h1>通知公告列表</h1>
</header>
<article>
        <div class="list-group mb-0">
            <c:forEach items="${page.data}" var="notice">
                <a class="list-item article" href="${ctx}/u/notice/${notice.id}">
                    <div class="list-text">
                        <h2 class="fs-15 lh-24 o-hidden">${notice.title}</h2>
                        <div class="font-777 fs-12">${notice.createdTimeLabel}</div>
                        <i class="list-arrow" style="float: right;margin-right: 0;margin-top: -25px;"></i>
                    </div>
                </a>
            </c:forEach>
        </div>

    <c:if test="${page.total > page.pageSize}">
        <div class="page-more"><span>点击加载更多</span></div>
    </c:if>
    <c:if test="${page.total <= page.pageSize}">
        <div class="page-more disabled"><span>没有更多数据了</span></div>
    </c:if>
</article>
</body>
</html>
