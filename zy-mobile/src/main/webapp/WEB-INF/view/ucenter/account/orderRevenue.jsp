<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/view/include/taglib.jsp" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="Cache-Control" content="no-store"/>
    <meta http-equiv="Pragma" content="no-cache"/>
    <meta http-equiv="Expires" content="0"/>
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-mobile-web-app-status-bar-style" content="black">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">

    <title></title>
    <%@ include file="/WEB-INF/view/include/head.jsp" %>
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
        #echartFir {
            padding: 10px;
            padding-top: 0;
            width:100%;
            height:280px;
            color: #49494a;
        }
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
        .triangle-up {
            width: 0;
            height: 0;
            border-left: 6px solid transparent;
            border-right: 6px solid transparent;
            border-top: 12px solid #dadada;
            position: absolute;
            top:50%;
            margin-top: -6px;
            left:30%;
        }
        .triangle-down {
            border-left: 6px solid transparent;
            border-right: 6px solid transparent;
            border-bottom: 12px solid #dadada;
            border-top:none;
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
        .echartdetilD {
            background: none;
        }
        .font-triangleD {
            width:50%;
            margin-left: 10%;
            font-size: 16px;
        }
        .font-triangleTD {width:35%;}
        .lookDetil {
            width:100%;
            height:40px;
            text-align: center;
            line-height: 40px;
            color: #74c2db;
            font-size: 20px;
        }
        .changeDetil {
            display: none;
        }
    </style>
    <script>
        <%--var reArrays = "${dateMap.revenues}";--%>
        <%--var reArray= reArrays.split(",");--%>
        <%--var len = reArray.length;--%>
        <%--console.info(${dateMap.len});--%>
        $(function() {
            $('.header .button-popmenu').click(function(){
                $('.header-popmenu').toggle(300);
            });
        });
    </script>
</head>

<body class="account">

<article>
    <div id="echartFir"></div>
    <a href="${ctx}/u/profit/orderRevenueDetail?month=2&type=2" class="look">查看详情</a>
    <c:forEach items="${dateMap.revenues}" var="revenue" varStatus="num">
        <div class="detilAll" onclick="changeTriangle(this)">
            <div class="echartdetil">
                <div class="triangle" >
                    <div class="triangle-up"></div>
                </div>
                <div class="font-triangle">
                    <p>
                            ${dateMap.len - num.index}月
                    </p>
                </div>
                <div class="font-triangleT">
                    ￥${revenue}
                </div>
            </div>
            <div class="changeDetil">
                <div class="echartdetil echartdetilD">
                    <div class="font-triangle font-triangleD">
                        <p>
                            订单号：<span class="firSpan">3541258744</span><br>
                            <span class="lastSpan">05.20 15:30</span>
                        </p>
                    </div>
                    <div class="font-triangleT font-triangleTD">
                        +1200.00
                    </div>
                </div>
                <div class="echartdetil echartdetilD">
                    <div class="font-triangle font-triangleD">
                        <p>
                            订单号：<span class="firSpan">3541258744</span><br>
                            <span class="lastSpan">05.20 15:30</span>
                        </p>
                    </div>
                    <div class="font-triangleT font-triangleTD">
                        +1200.00
                    </div>
                </div>
                <div class="echartdetil echartdetilD">
                    <div class="font-triangle font-triangleD">
                        <p>
                            订单号：<span class="firSpan">3541258744</span><br>
                            <span class="lastSpan">05.20 15:30</span>
                        </p>
                    </div>
                    <div class="font-triangleT font-triangleTD">
                        +1200.00
                    </div>
                </div>
            </div>
        </div>
    </c:forEach>
</article>
<script src="${ctx}/echarts.min.js"></script>
<script type="text/javascript">
    var arrays = "${dateMap.revenue}";
    var array= arrays.split(",");
    <%--console.info(${revenues});--%>
    // 基于准备好的dom，初始化echarts实例
    var myChart = echarts.init(document.getElementById('echartFir'));
    // 指定图表的配置项和数据
    option = {
        color: ['#31d9a5'],
        tooltip : {
            trigger: 'axis',
            axisPointer : {            // 坐标轴指示器，坐标轴触发有效
                type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
            }
        },
        title: {
            text: '${types}月度图'
        },
        grid: {
            left: '3%',
            right: '4%',
            bottom: '3%',
            containLabel: true
        },
        xAxis : [
            {
                type : 'category',
                data: ['一月','二月','三月','四月','五月','六月','七月','八月','九月','十月','十一月','十二月'],
                axisTick: {
                    alignWithLabel: true
                }
            }
        ],
        yAxis : [
            {
                type : 'value'
            }
        ],
        toolbox: {
            show : true,
            feature : {
                mark : {show: false},
                dataView : {show: true, readOnly: false},
                magicType : {
                    show: true,
                    type: ['pie', 'funnel']
                },
                saveAsImage : {show: true}
            }
        },
        series : [
            {
                name:'直接访问',
                type:'bar',
                barWidth: '60%',
                data:array
            }
        ]
    };
    // 使用刚指定的配置项和数据显示图表。
    myChart.setOption(option);
    function changeTriangle(obj) {
        var change=$(obj).find(".triangle-up").attr("class");
        if(change=="triangle-up"){
            $(obj).find(".triangle-up").addClass("triangle-down");
            $(obj).siblings(".detilAll").find(".triangle-up").removeClass("triangle-down");
            $(obj).find(".changeDetil").show();
            $(obj).siblings(".detilAll").find(".changeDetil").hide();
        }else {
            $(obj).find(".triangle-up").removeClass("triangle-down");
            $(obj).find(".changeDetil").hide();
        }

    }
</script>
</body>

</html>
