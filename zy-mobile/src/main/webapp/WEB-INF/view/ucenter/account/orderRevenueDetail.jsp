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
    <link rel="stylesheet" href="${ctx}/style.css">
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
            height:100%;
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
            .font-triangleTD {width:20%;position: absolute;
                right:0;}
            .font-triangleT {
                line-height:100px;
            }
        }
    </style>
    <title>我的团队</title>
    <%@ include file="/WEB-INF/view/include/head.jsp"%>
    <script type="text/javascript">
        $(function() {

        });
    </script>
</head>
<body>
<header class="header">
    <h1>订单收益明细</h1>
    <a href="${ctx}/u/profit/orderRevenue?type=${type}" class="button-left"><i class="fa fa-angle-left"></i></a>
</header>

<article>
    <div class="detilAll" onclick="changeTriangle(this)">
        <c:forEach items="${orderRevenueDetails}" var="orderRevenueDetail" >
            <div class="changeDetil">
                <div class="echartdetil echartdetilD">
                    <div class="font-triangle font-triangleD">
                        <p>
                            订单号：<span class="firSpan">${orderRevenueDetail.sn}</span><br>
                            <span class="lastSpan">${orderRevenueDetail.createdTimeLabel}</span>
                        </p>
                    </div>
                    <div class="font-triangleT font-triangleTD">
                        +${orderRevenueDetail.amountLabel}
                    </div>
                </div>
            </div>
        </c:forEach>
    </div>
</article>
<%@ include file="/WEB-INF/view/include/footer.jsp"%>
</body>
</html>
