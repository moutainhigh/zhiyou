<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/view/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text; charset=utf-8">
    <meta http-equiv="Cache-Control" content="no-store" />
    <meta http-equiv="Pragma" content="no-cache" />
    <meta http-equiv="Expires" content="0" />
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-mobile-web-app-status-bar-style" content="black">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
    <link rel="stylesheet" href="${stccdn}/css/ucenter/account.css">
    <style>
        html, body, div, p, ul, li, dl, dt, dd, h1, h2, h3, h4, h5, h6, form, input, select, button, textarea, iframe, table, th, td { margin: 0; padding: 0; }
        body { font-family:"Microsoft YaHei";background: #fff; font-size: 26px; color: #151515; margin: 0; padding: 0; }
        img { border: 0 none; vertical-align: top; display:inline-block; -ms-interpolation-mode: bicubic; image-rendering:optimizeQuality; }
        button { cursor: pointer; border:0 none; }
        input{ border:0 none; background:transparent; }
        textarea{ resize: none; border:0 none; }
        ul, li { list-style-type: none; }
        table{ border-collapse:collapse; border-spacing:0; }
        i, em, cite { font-style: normal; }
        p{word-wrap:break-word; }
        body, input, select, button,textarea{ outline: none; }
        a { display:block; }
        a,input,textarea,p,span,h2,em,li,div{text-decoration: none; }
        input,textarea{font-family:"Microsoft YaHei"}
        .echartdetil {
            width:100%;
            height:80px;
            background: #fff;
            border-bottom: 1px solid #ccc;
        }
        .triangle {
            float: left;
            width:10%;
            height:100%;
            position:relative;
        }
        .font-triangle {
            float: left;
            width:40%;
            height:100%;
            text-align: left;
            font-size: 20px;
            position: relative;
        }
        .font-triangle p {
            position: absolute;
            top:50%;
            margin-top: -24px;
            line-height: 25px;
        }
        .font-triangle .firSpan,.font-triangle .lastSpan {
            font-size: 16px;
        }

        .font-triangleT {
            float: right;
            width:45%;
            margin-right: 5%;
            text-align: right;
            font-size: 16px;
            line-height: 80px;
        }
        .font-triangleD {
            width:80%;
            margin-left: 5%;
            font-size: 16px;
        }
        .font-triangleTD {
            position: absolute;
            right:0;
        }
        @media (device-height:568px) and (-webkit-min-device-pixel-ratio:2){/* 兼容iphone5 */
            .font-triangleTD {width:10%;position: absolute;
                right:0;}
            .font-triangleD,.font-triangleTD {font-size: 15px;}
            .font-triangleT {
                line-height:100px;
            }
        }
    </style>
    <title>出入库明细</title>
    <%@ include file="/WEB-INF/view/include/head.jsp"%>
    <script src="${stccdn}/plugin/laytpl-1.1/laytpl.js"></script>
    <script type="text/javascript">
        $(function() {

        });
    </script>
</head>
<body>
<header class="header">
    <h1>出入库明细</h1>
    <a href="${ctx}/u/store" class="button-left"><i class="fa fa-angle-left"></i></a>


    <script type="text/javascript">
        $(function() {
            if (!$('.page-more').hasClass('disabled')) {
                $('.page-more').click(loadMore);
            }
        });

        var pageNumber = 0;

        function loadMore() {
            $.ajax({
                url : '${ctx}/u/store/ajaxDetails',
                data : {
                    pageNumber : pageNumber + 1
                },
                dataType : 'json',
                type : 'POST',
                success : function(result) {
                    if(result.code != 0) {
                        return;
                    }
                    var page = result.data.page;
                    if (page.data.length) {
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
                $('.detilAll').append(html);
            });
        }
    </script>
    <script id="rowTpl" type="text/html">
        <div class="changeDetil">
            <div class="echartdetil echartdetilD">
                <div class="font-triangle font-triangleD">
                    <p>
                        订单号：<span class="firSpan">{{d.sn}}</span><br>
                        <span class="lastSpan">{{d.createDate}}</span>
                    </p>
                </div>
                <div class="font-triangleT font-triangleTD">
                  {{d.type==1?'-':'+'}}
                  {{d.number}}
                </div>
            </div>
        </div>
    </script>
</header>

<article>
    <div class="detilAll">
        <c:forEach items="${page.data}" var="orderStore" >
            <div class="changeDetil">
                <div class="echartdetil echartdetilD">
                    <div class="font-triangle font-triangleD">
                        <p>
                            订单号：<span class="firSpan">${orderStore.sn}</span><br>
                            <span class="lastSpan">${orderStore.createDate}</span>
                        </p>
                    </div>
                    <div class="font-triangleT font-triangleTD">
                        <c:choose>
                            <c:when test="${orderStore.type==1}">
                                -${orderStore.number}
                            </c:when>
                            <c:otherwise>
                                +${orderStore.number}
                            </c:otherwise>
                        </c:choose>

                    </div>
                </div>
            </div>
        </c:forEach>
    </div>

    <c:if test="${page.total > page.pageSize}">
        <div class="page-more"><span>点击加载更多</span></div>
    </c:if>
    <c:if test="${page.total <= page.pageSize}">
        <div class="page-more disabled"><span>没有更多数据了</span></div>
    </c:if>
</article>
<%@ include file="/WEB-INF/view/include/footer.jsp"%>
</body>
</html>
