<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/view/include/taglib.jsp"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<style>
    .clearfloat:before, .clearfloat:after {
        content:"";
        display:table;
    }
    .clearfloat:after{
        clear:both;
        overflow:hidden;
    }
    .clearfloat{
        zoom:1;
    }
    .page-breadcrumb li img,.caption img  {
        float: left;
        width:17px;
    }
    .page-breadcrumb li span,.caption span {
        float: left;
        margin-left: 10px;
    }
    .volume,.team,.expenses {
        height:70px;
        text-align: center;
        line-height:70px;
        background: #d24940;
        -webkit-border-radius:35px 0 0 35px !important;
        -moz-border-radius:35px 0 0 35px !important;
        border-radius:35px 0 0 35px !important;
        color: #fff;
        font-size: 20px;
    }
    .team {
        background: #269952;
    }
    .expenses {
        background: #2582c1;
    }
    .dataGoal,.dataTeam,.dataExpenses {
        height:70px;
        text-align: center;
        color: #fff;
        font-size: 20px;
        background: -webkit-linear-gradient(left, #e74c41 , #ed845b); /* Safari 5.1 - 6.0 */
        background: -o-linear-gradient(right, #e74c41 , #ed845b); /* Opera 11.1 - 12.0 */
        background: -moz-linear-gradient(right, #e74c41 , #ed845b); /* Firefox 3.6 - 15 */
        background: linear-gradient(to right, #e74c41 , #ed845b); /* 标准的语法 */
    }
    .dataTeam {
        background: -webkit-linear-gradient(left, #35ac4c , #34c69e); /* Safari 5.1 - 6.0 */
        background: -o-linear-gradient(right, #35ac4c , #34c69e); /* Opera 11.1 - 12.0 */
        background: -moz-linear-gradient(right, #35ac4c , #34c69e); /* Firefox 3.6 - 15 */
        background: linear-gradient(to right, #35ac4c , #34c69e); /* 标准的语法 */
    }
    .dataExpenses {
        background: -webkit-linear-gradient(left, #3794d3 , #52b9dd); /* Safari 5.1 - 6.0 */
        background: -o-linear-gradient(right, #3794d3 , #52b9dd); /* Opera 11.1 - 12.0 */
        background: -moz-linear-gradient(right, #3794d3 , #52b9dd); /* Firefox 3.6 - 15 */
        background: linear-gradient(to right, #3794d3 , #52b9dd); /* 标准的语法 */
    }
    .colMl {
        float: left;
        width:33%;
        height:70px;
    }
    .dataGoal span.num,.dataGoal span.dateVol,.dataTeam span.dateVol,.dataTeam span.num,.dataExpenses span.dateVol,.dataExpenses span.num{
        display: block;
        width:100%;
        height:40px;
        text-align: center;
        line-height: 40px;
        color: #fff;
        font-size: 28px;
    }
    .dataGoal span.dateVol,.dataTeam span.dateVol,.dataExpenses span.dateVol{
        height:30px;
        line-height: 20px;
        font-size: 16px;
    }
    .bouFirst {
        background: -webkit-linear-gradient(left, #fc6d5f , #fe8764); /* Safari 5.1 - 6.0 */
        background: -o-linear-gradient(right,  #fc6d5f , #fe8764); /* Opera 11.1 - 12.0 */
        background: -moz-linear-gradient(right,  #fc6d5f , #fe8764); /* Firefox 3.6 - 15 */
        background: linear-gradient(to right,  #fc6d5f , #fe8764); /* 标准的语法 */
    }
    .bouSec {
        background: -webkit-linear-gradient(left, #fe8764 , #fe9a5a); /* Safari 5.1 - 6.0 */
        background: -o-linear-gradient(right,  #fe8764 , #fe9a5a); /* Opera 11.1 - 12.0 */
        background: -moz-linear-gradient(right,  #fe8764 , #fe9a5a); /* Firefox 3.6 - 15 */
        background: linear-gradient(to right,  #fe8764 , #fe9a5a); /* 标准的语法 */
    }
    .bouFou {
        background: -webkit-linear-gradient(left, #fe9a5a , #ffb463); /* Safari 5.1 - 6.0 */
        background: -o-linear-gradient(right,  #fe9a5a , #ffb463); /* Opera 11.1 - 12.0 */
        background: -moz-linear-gradient(right,  #fe9a5a , #ffb463); /* Firefox 3.6 - 15 */
        background: linear-gradient(to right,  #fe9a5a , #ffb463); /* 标准的语法 */
    }
    .bouF4 {
        background: -webkit-linear-gradient(left, #39cc39 , #42d17a); /* Safari 5.1 - 6.0 */
        background: -o-linear-gradient(right,  #39cc39 , #42d17a); /* Opera 11.1 - 12.0 */
        background: -moz-linear-gradient(right,  #39cc39 , #42d17a); /* Firefox 3.6 - 15 */
        background: linear-gradient(to right,  #39cc39 , #42d17a); /* 标准的语法 */
    }
    .bouF5 {
        background: -webkit-linear-gradient(left, #42d17a , #48eca0); /* Safari 5.1 - 6.0 */
        background: -o-linear-gradient(right,  #42d17a , #48eca0); /* Opera 11.1 - 12.0 */
        background: -moz-linear-gradient(right,  #42d17a , #48eca0); /* Firefox 3.6 - 15 */
        background: linear-gradient(to right,  #42d17a , #48eca0); /* 标准的语法 */
    }
    .bouF6 {
        background: -webkit-linear-gradient(left, #48eca0 , #48ecce); /* Safari 5.1 - 6.0 */
        background: -o-linear-gradient(right,  #48eca0 , #48ecce); /* Opera 11.1 - 12.0 */
        background: -moz-linear-gradient(right,  #48eca0 , #48ecce); /* Firefox 3.6 - 15 */
        background: linear-gradient(to right,  #48eca0 , #48ecce); /* 标准的语法 */
    }
    .bouF7 {
        background: -webkit-linear-gradient(left, #34a9e5 , #43bffa); /* Safari 5.1 - 6.0 */
        background: -o-linear-gradient(right,  #34a9e5 , #43bffa); /* Opera 11.1 - 12.0 */
        background: -moz-linear-gradient(right,  #34a9e5 , #43bffa); /* Firefox 3.6 - 15 */
        background: linear-gradient(to right,  #34a9e5 , #43bffa); /* 标准的语法 */
    }
    .bouF8 {
        background: -webkit-linear-gradient(left, #43bffa , #79d4fe); /* Safari 5.1 - 6.0 */
        background: -o-linear-gradient(right,  #43bffa , #79d4fe); /* Opera 11.1 - 12.0 */
        background: -moz-linear-gradient(right,  #43bffa , #79d4fe); /* Firefox 3.6 - 15 */
        background: linear-gradient(to right,  #43bffa , #79d4fe); /* 标准的语法 */
    }
    .bouF9 {
        background: -webkit-linear-gradient(left, #79d4fe , #7de4fd); /* Safari 5.1 - 6.0 */
        background: -o-linear-gradient(right,  #79d4fe , #7de4fd); /* Opera 11.1 - 12.0 */
        background: -moz-linear-gradient(right,  #79d4fe , #7de4fd); /* Firefox 3.6 - 15 */
        background: linear-gradient(to right,  #79d4fe , #7de4fd); /* 标准的语法 */
    }
    .bounce {
        -webkit-animation:zang 0.5s;
        -o-animation:zang 0.5s;
        -o-moz:zang 0.5s;
        animation:zang 0.5s;

        -o-transform:scale3d(1.2,1.2,1.2);
        -moz-transform:scale3d(1.2,1.2,1.2);
        -webkit-transform:scale3d(1.2,1.2,1.2);
        transform:scale3d(1.2,1.2,1.2);

        transition-delay:0.5s;
        -webkit-transition-delay:0.5s;
        -o-transition-delay:0.5s;
        -moz-transition-delay:0.5s;
    }
    @-webkit-keyframes zang {
        from {
            -webkit-transform: scale3d(1, 1, 1);
            transform: scale3d(1, 1, 1);
        }
        to {
            -webkit-transform: scale3d(1.2, 1.2, 1.2);
            transform: scale3d(1.2, 1.2, 1.2);
        }
    }
    @-o-keyframes zang {
        from {
            -o-transform: scale3d(1, 1, 1);
            transform: scale3d(1, 1, 1);
        }
        to {
            -o-transform: scale3d(1.2, 1.2, 1.2);
            transform: scale3d(1.2, 1.2, 1.2);
        }
    }
    @-moz-keyframes zang {
        from {
            -moz-transform: scale3d(1, 1, 1);
            transform: scale3d(1, 1, 1);
        }
        to {
            -moz-transform: scale3d(1.2, 1.2, 1.2);
            transform: scale3d(1.2, 1.2, 1.2);
        }
    }
    @keyframes zang {
        from {
            -moz-transform: scale3d(1, 1, 1);
            -o-transform: scale3d(1, 1, 1);
            -webkit-transform: scale3d(1, 1, 1);
            transform: scale3d(1, 1, 1);
        }
        to {
            -webkit-transform: scale3d(1.2, 1.2, 1.2);
            -o-transform: scale3d(1.2, 1.2, 1.2);
            -moz-transform: scale3d(1.2, 1.2, 1.2);
            transform: scale3d(1.2, 1.2, 1.2);
        }
    }
    .firstExpenses {
       margin-left: 30%;
    }
    .allExpenses {
        float: left;
        width:33%;
        height:50px;
        text-align: center;
        line-height:50px;
        color: #bbb;
        font-size: 24px;
        border: 1px solid #e1e1e1;
    }
    .on1 {
        background: #d24940;
        color: #fff;
    }
    .on2 {
        background: #269952;
        color: #fff;
    }
    .on3 {
        background: #2582c1;
        color: #fff;
    }
</style>
<div class="page-bar">
    <ul class="page-breadcrumb">
        <li><img src="${ctx}/image/dataXiao.png" /> <span class="ajaxify">今日销量</span>
    </ul>
</div>
<!-- END PAGE HEADER-->
    <div class="row">
        <div class="col-md-12">
            <div class="portlet light bordered clearfloat">
                <div class="portlet-title">
                    <div class="caption">
                        <img src="${ctx}/image/dataTong.png" />
                        <span class="caption-subject bold uppercase"> 数据统计</span>
                    </div>
                </div>
                <div class="col-md-12" style="margin-top: 10px;">
                    <div class="col-md-2 volume">销量</div>
                    <div class="col-md-10 dataGoal">
                        <div class="colMl" change="1" onmouseover="scaleCol(this)" onmouseout="scaleColTo(this)">
                            <span class="num">185次</span>
                            <span class="dateVol">当月销量目标</span>
                        </div>
                        <div class="colMl" change="2" onmouseover="scaleCol(this)" onmouseout="scaleColTo(this)">
                            <span class="num">18500次</span>
                            <span class="dateVol">当月销量完成</span>
                        </div>
                        <div class="colMl" change="3" onmouseover="scaleCol(this)" onmouseout="scaleColTo(this)">
                            <span class="num">88%</span>
                            <span class="dateVol">当月销量达成率</span>
                        </div>
                    </div>
                </div>
                <div class="col-md-12" style="margin-top: 20px;">
                    <div class="col-md-2 team">团队</div>
                        <div class="col-md-10 dataTeam">
                            <div class="colMl" change="4" onmouseover="scaleCol(this)" onmouseout="scaleColTo(this)">
                                <span class="num">185人</span>
                                <span class="dateVol">当月总特级数</span>
                            </div>
                            <div class="colMl" change="5" onmouseover="scaleCol(this)" onmouseout="scaleColTo(this)">
                                <span class="num">18500人</span>
                                <span class="dateVol">当月新增特级数</span>
                            </div>
                            <div class="colMl" change="6" onmouseover="scaleCol(this)" onmouseout="scaleColTo(this)">
                                <span class="num">88人</span>
                                <span class="dateVol">沉睡特级数</span>
                            </div>
                        </div>
                    </div>
                <div class="col-md-12" style="margin-top: 20px;">
                    <div class="col-md-2 expenses">收支</div>
                    <div class="col-md-10 dataExpenses">
                        <div class="colMl" change="7" onmouseover="scaleCol(this)" onmouseout="scaleColTo(this)">
                            <span class="num">185元</span>
                            <span class="dateVol">当月U币充值金额</span>
                        </div>
                        <div class="colMl" change="8" onmouseover="scaleCol(this)" onmouseout="scaleColTo(this)">
                            <span class="num">18500元</span>
                            <span class="dateVol">上月奖励发放金额</span>
                        </div>
                        <div class="colMl" change="9" onmouseover="scaleCol(this)" onmouseout="scaleColTo(this)">
                            <span class="num">88元</span>
                            <span class="dateVol">本年度累计发放金额</span>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="row">
        <div class="col-md-12">
            <div class="portlet light bordered">
                <div class="portlet-title">
                    <div class="caption">
                        <img src="${ctx}/image/dataTong.png" />
                        <span class="caption-subject bold uppercase">销量统计</span>
                    </div>
                </div>
                <div class="portlet-body clearfloat">
                      <div class="col-md-4 firstExpenses">
                          <div class="allExpenses on1" change="1" onclick="ajaxTeam(this)">销量</div>
                          <div class="allExpenses" style="border-left: none;" change="2" onclick="ajaxTeam(this)">团队</div>
                          <div class="allExpenses" style="border-left: none;" change="3"  onclick="ajaxTeam(this)">收支</div>
                      </div>
                </div>
                <div class="AllThrVolume">
                    <div class="page-bar" style="margin-top: 30px;">
                        <ul class="page-breadcrumb">
                            <li><span class="ajaxify">当月日销量情况</span>
                        </ul>
                    </div>
                    <div class="portlet-body">
                        <div id="orderChart1" style="height:350px;">

                        </div>
                    </div>
                    <div class="page-bar" style="margin-top: 30px;">
                        <ul class="page-breadcrumb">
                            <li><span class="ajaxify">五大区各区每月历史完成销量</span>
                        </ul>
                    </div>
                    <div class="portlet-body">
                        <div id="orderChart2" style="height:350px;">

                        </div>
                    </div>
                    <div style="float:left;width:48%;">
                        <div class="page-bar" style="margin-top: 30px;">
                            <ul class="page-breadcrumb">
                                <li><span class="ajaxify">五大区当月完成量及目标量</span>
                            </ul>
                        </div>
                        <div class="portlet-body">
                            <div id="orderChart3" style="height:350px;">

                            </div>
                        </div>
                    </div>
                    <div style="float:left;width:48%;margin-left: 4%;">
                        <div class="page-bar" style="margin-top: 30px;">
                            <ul class="page-breadcrumb">
                                <li><span class="ajaxify">五大区当月达成的销量占比</span>
                            </ul>
                        </div>
                        <div class="portlet-body">
                            <div id="orderChart4" style="height:350px;">

                            </div>
                        </div>
                    </div>
                </div>
                <div class="AllThrTeam">
                </div>
                <div class="AllThrExpenses">
                </div>
            </div>
        </div>
    </div>

<script>
    $(function(){
        var myChartxiao1 = echarts.init(document.getElementById('orderChart1'));
        // 异步加载数据
        $.post('${ctx}/main/ajaxChart/salesvolume',{},function(result) {
            console.log(result);
            var categories = ['东部','南部','西部','北部','中部'];
            myChartxiao1.setOption({
                tooltip: {
                    trigger: 'axis'
                },
                legend: [{
                    data: categories.map(function (a) {
                        return a;
                    })
                }],
                grid: {
                    left: '3%',
                    right: '4%',
                    bottom: '3%',
                    containLabel: true
                },
                toolbox: {
                    feature: {
                        saveAsImage: {}
                    }
                },
                xAxis: {
                    type: 'category',
                    boundaryGap: false,
                    data : ['1月','2月','3月','4月','5月','6月','7月','8月','9月','10月','11月','12月']
                },
                yAxis: {
                    type: 'value'
                },
                series : (function (){
                    var series = [];
                    for (var i = 0; i <categories.length; i++) {
                        series.push({
                            name:categories[i],
                            type:'line',
                            stack: '总量',
                            data:[120, 132, 101, 134, 90, 230, 210,120, 132, 101, 134, 90]
                        });
                    }
                    return series;
                })()
            });
        });
    })
</script>
<script>
    var myChartxiao2 = echarts.init(document.getElementById('orderChart2'));
    // 异步加载数据
    $.post('${ctx}/main/ajaxChart/salesvolume',{},function(result) {
        console.log(result);
        var categories = ['东部','南部','西部','北部','中部'];
        myChartxiao2.setOption({
            tooltip : {
                trigger: 'axis'
            },
            legend: [{
                data: categories.map(function (a) {
                    return a;
                })
            }],
            grid: {
                left: '3%',
                right: '4%',
                bottom: '3%',
                containLabel: true
            },
            toolbox: {
                feature: {
                    saveAsImage: {}
                }
            },
            calculable : true,
            xAxis : [
                {
                    type : 'category',
                    data : ['1月','2月','3月','4月','5月','6月','7月','8月','9月','10月','11月','12月']
                }
            ],
            yAxis : [
                {
                    type : 'value'
                }
            ],
            series : (function (){
                var series = [];
                for (var i = 0; i <categories.length; i++) {
                    series.push({
                        name:categories[i],
                        type:'bar',
                        data:[2.0, 4.9, 7.0, 23.2, 25.6, 76.7, 135.6, 162.2, 32.6, 20.0, 6.4, 3.3]
                    });
                }
                return series;
            })()
        });
    });
</script>
<script>
    var myChartxiao3 = echarts.init(document.getElementById('orderChart3'));
    // 异步加载数据
    $.post('${ctx}/main/ajaxChart/salesvolume',{},function(result) {
        console.log(result);
        var categories = ['东部','南部','西部','北部','中部'];
        myChartxiao3.setOption({
            tooltip: {
                trigger: 'axis',
                axisPointer: {
                    type: 'cross',
                    crossStyle: {
                        color: '#999'
                    }
                }
            },
            grid: {
                left: '3%',
                right: '4%',
                bottom: '3%',
                containLabel: true
            },
            toolbox: {
                feature: {
                    dataView: {show: true, readOnly: false},
                    magicType: {show: true, type: ['line', 'bar']},
                    restore: {show: true},
                    saveAsImage: {show: true}
                }
            },
            legend: {
                data:['目标量','完成量','完成率']
            },
            xAxis: [
                {
                    type: 'category',
                    data: categories.map(function (a) {
                        return a;
                    }),
                    axisPointer: {
                        type: 'shadow'
                    }
                }
            ],
            yAxis: [
                {
                    type: 'value',
                    name: '当月量',
                    axisLabel: {
                        formatter: '{value} '
                    }
                },
                {
                    type: 'value',
                    name: '完成率',
                    axisLabel: {
                        formatter: '{value} %'
                    }
                }
            ],
            series: [
                {
                    name:'目标量',
                    type:'bar',
                    data:[2.0, 4.9, 7.0, 23.2, 25.6]
                },
                {
                    name:'完成量',
                    type:'bar',
                    data:[2.6, 5.9, 9.0, 26.4, 28.7]
                },
                {
                    name:'完成率',
                    type:'line',
                    yAxisIndex: 1,
                    data:[2.0, 4.2, 3.3, 4.5, 6.3]
                }
            ]
        });
    });
</script>
<script>
    var myChartxiao4 = echarts.init(document.getElementById('orderChart4'));
    // 异步加载数据
    $.post('${ctx}/main/ajaxChart/salesvolume',{},function(result) {
        console.log(result);
        var categories = ['东部','南部','西部','北部','中部'];
        var data=[
            {value:335, name:'东部'},
            {value:310, name:'南部'},
            {value:234, name:'西部'},
            {value:135, name:'北部'},
            {value:1548, name:'中部'}
        ];
        myChartxiao4.setOption({
            tooltip : {
                trigger: 'item',
                formatter: "{a} <br/>{b} : {c} ({d}%)"
            },
            legend: {
                orient: 'vertical',
                left: 'left',
                data: categories.map(function (a) {
                    return a;
                })
            },
            series : [
                {
                    type: 'pie',
                    radius : '55%',
                    itemStyle: {
                        emphasis: {
                            shadowBlur: 10,
                            shadowOffsetX: 0,
                            shadowColor: 'rgba(0, 0, 0, 0.5)'
                        }
                    },
                    data:data
                }
            ]
        });
    });
</script>
<script>
    function orderChartTwo() {

    }
    function scaleCol(obj){
        var num=parseInt($(obj).attr("change"));
        switch(num)
        {
            case 1:
                $(obj).addClass("bouFirst");
                break;
            case 2:
                $(obj).addClass("bouSec");
                break;
            case 3:
                $(obj).addClass("bouFou");
                break;
            case 4:
                $(obj).addClass("bouF4");
                break;
            case 5:
                $(obj).addClass("bouF5");
                break;
            case 6:
                $(obj).addClass("bouF6");
                break;
            case 7:
                $(obj).addClass("bouF7");
                break;
            case 8:
                $(obj).addClass("bouF8");
                break;
            case 9:
                $(obj).addClass("bouF9");
                break;
            default:
        }
        $(obj).addClass("bounce");
    }
    function scaleColTo(obj){
        var num=parseInt($(obj).attr("change"));
        switch(num)
        {
            case 1:
                $(obj).removeClass("bouFirst");
                break;
            case 2:
                $(obj).removeClass("bouSec");
                break;
            case 3:
                $(obj).removeClass("bouFou");
                break;
            case 4:
                $(obj).removeClass("bouF4");
                break;
            case 5:
                $(obj).removeClass("bouF5");
                break;
            case 6:
                $(obj).removeClass("bouF6");
                break;
            case 7:
                $(obj).removeClass("bouF7");
                break;
            case 8:
                $(obj).removeClass("bouF8");
                break;
            case 9:
                $(obj).removeClass("bouF9");
                break;
            default:
        }
        $(obj).removeClass("bounce");
    }
    function ajaxTeam(obj){
        var num=parseInt($(obj).attr("change"));
        switch(num)
        {
            case 1:
                $(obj).addClass("on1");
                $(obj).siblings(".allExpenses").eq(0).removeClass("on2");
                $(obj).siblings(".allExpenses").eq(1).removeClass("on3");
                $(".AllThrVolume").show();
                $(".AllThrTeam,.AllThrExpenses").hide();
                break;
            case 2:
                $(obj).addClass("on2");
                $(obj).siblings(".allExpenses").eq(0).removeClass("on1");
                $(obj).siblings(".allExpenses").eq(1).removeClass("on3");
                $(".AllThrTeam").show();
                $(".AllThrVolume,.AllThrExpenses").hide();
                break;
            case 3:
                $(obj).addClass("on3");
                $(obj).siblings(".allExpenses").eq(0).removeClass("on1");
                $(obj).siblings(".allExpenses").eq(1).removeClass("on2");
                $(".AllThrExpenses").show();
                $(".AllThrVolume,.AllThrTeam").hide();
                break;
            default:
        }
    }
</script>