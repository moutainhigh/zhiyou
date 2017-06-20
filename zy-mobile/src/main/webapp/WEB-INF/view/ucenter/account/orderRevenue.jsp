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
    <style>
        html, body, div, p, ul, li, dl, dt, dd, h1, h2, h3, h4, h5, h6, form, input, select, button, textarea, iframe, table, th, td { margin: 0; padding: 0; }
        body { font-family:"Microsoft YaHei";background: #eee; font-size: 26px; color: #151515; margin: 0; padding: 0; }
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
        .account {background-color: #eee;}
        #echartFir {
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
            -webkit-transform:translate(0,-50%);
            transform:translate(0,-50%);
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
            margin-right: 5%;
            text-align: right;
            font-size: 16px;
            line-height: 80px;
            position: absolute;
            right:10px;
        }
        .echartdetilD {
            background: none;
        }
        .font-triangleD {
            width:95%;
            margin-left: 5%;
            font-size: 16px;
        }
        .lookDetil {
            width:100%;
            height:40px;
            text-align: center;
            line-height: 40px;
            color: #6cb92d;
            font-size: 20px;
        }
        .changeDetil {
            display: none;
            background: #eee;
        }
        .allAccount {background: #fff;margin-bottom: 15px;}
        .teamAllAccount {height: 50px;background: #fff;padding-top: 10px;}
        .teamAllAccount img {float: left;width: 50px;height: 50px;margin-left: 8px;}
        .teamAllAccount span {
            float: left;line-height: 50px;margin-left: 20px;font-size: 18px;
            color: #838385;
        }
        .teamAllAccount a,.teamAllAccount span.paim {
            float: right;
            line-height: 50px;
            color: #6cb92d;
            margin-right: 15px;
        }
        .color {
            font-size:14px;
            color: #c7c7c7;
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
    <div class="allAccount">
        <div class="teamAllAccount">
            <img src="${ctx}/peopleVolume.png"/>
            <span>${types}月度表</span>
        </div>
        <div id="echartFir"></div>
    </div>

    <c:forEach items="${dateMap.revenues}" var="revenue" varStatus="num">
        <div class="detilAll" onclick="changeTriangle(this)">
            <div class="echartdetil">
                <div class="triangle" >
                    <div class="triangle-up"></div>
                </div>
                <div class="font-triangle">
                    <p>
                            <span class="spanTriange">${dateMap.len - num.index}</span>月<br/>
                            <span class="color">${dateMap.len - num.index}月01-${dateMap.len - num.index}月30</span>
                    </p>
                </div>
                <div class="font-triangleT">
                    ￥${revenue}
                </div>
            </div>
        </div>
        <div class="changeDetil">
        </div>
    </c:forEach>
</article>
<input type="hidden" value="-" />
<script src="${ctx}/echarts.min.js"></script>
<script type="text/javascript">
    function changeTriangle(obj) {
        var type = ${type};
        var moth = $(obj).find(".spanTriange").text();
        var change = $(obj).find(".triangle-up").attr("class");
        if(change=="triangle-up"){
            $(obj).find(".triangle-up").addClass("triangle-down");
            $(obj).siblings(".detilAll").find(".triangle-up").removeClass("triangle-down");
            $(obj).next(".changeDetil").show();
            $.ajax({
                url : '${ctx}/u/profit/revenueDetail',
                data : {
                    type : type,
                    month : moth
                },
                dataType : 'json',
                type : 'POST',
                success : function(result) {
                    var detailArray = result.data;
                    for(var i = 0; i < detailArray.length; i++){
                        if( i < 3){
                            $(obj).next(".changeDetil").append('<div class="echartdetil echartdetilD"><div class="font-triangle font-triangleD"><p>订单号：<span class="firSpan">'+ detailArray[i].sn +'</span><br><span class="lastSpan">'+ detailArray[i].createdTimeLabel +'</span></p></div><div class="font-triangleT font-triangleTD">+ '+ detailArray[i].amountLabel +'</div></div>');
                        }else if( i == 3){
                            $(obj).next(".changeDetil").append('<a href="${ctx}/u/profit/orderRevenueDetail?month='+ moth +'&type=${type}" class="lookDetil">查看明细</a>');
                        }
                    }
                    $(obj).siblings(".detilAll").next(".changeDetil").html("");

                    $(obj).siblings(".detilAll").next(".changeDetil").hide();
                }

            });
        }else {
            $(obj).find(".triangle-up").removeClass("triangle-down");
            $(obj).next(".changeDetil").html("");
            $(obj).next(".changeDetil").hide();
        }
    }
    var arrays = "${dateMap.revenue}";
    var array= arrays.split(",");
    // 基于准备好的dom，初始化echarts实例
    var myChart = echarts.init(document.getElementById('echartFir'));
    var colorText='#68dcc1';
    // 指定图表的配置项和数据
    option = {
        color: [colorText],
        tooltip : {
            trigger: 'axis',
            axisPointer : {            // 坐标轴指示器，坐标轴触发有效
                type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
            }
        },
        grid: {
            left: '3%',
            right: '4%',
            top: '15%',
            containLabel: true
        },
        xAxis : [
            {
                type : 'category',
                data: ['一月','二月','三月','四月','五月','六月','七月','八月','九月','十月','十一月','十二月'],
                axisTick: {
                    alignWithLabel: true
                },
                axisLine: {
                    onZero: false,
                    lineStyle: {
                        color: colorText
                    }
                }
            }
        ],
        yAxis : [
            {
                type : 'value',
                axisLine: {
                    onZero: false,
                    lineStyle: {
                        color: colorText
                    }
                }
            },

        ],
        series : [
           {
            name:'金额',
            type:'bar',
            stack: '金额',
               barCategoryGap:'40%',
            label: {
            normal: {
                show: true,
                        position: 'top'
            }
        },
        data:array
    }
        ]
    };
    // 使用刚指定的配置项和数据显示图表。
    myChart.setOption(option);

</script>
</body>

</html>
