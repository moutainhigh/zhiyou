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
        line-height: 30px;
        font-size: 16px;
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
                <div class="col-md-12" style="padding-left: 4%;margin-top: 10px;">
                    <div class="col-md-1 volume">销量</div>
                    <div class="col-md-10 dataGoal">
                        <div class="colMl">
                            <span class="num">185次</span>
                            <span class="dateVol">当月销量目标</span>
                        </div>
                        <div class="colMl">
                            <span class="num">18500次</span>
                            <span class="dateVol">当月销量完成</span>
                        </div>
                        <div class="colMl">
                            <span class="num">88%</span>
                            <span class="dateVol">当月销量达成率</span>
                        </div>
                    </div>
                </div>
                <div class="col-md-12" style="padding-left: 4%;margin-top: 20px;">
                    <div class="col-md-1 team">团队</div>
                        <div class="col-md-10 dataTeam">
                            <div class="colMl">
                                <span class="num">185次</span>
                                <span class="dateVol">当月销量目标</span>
                            </div>
                            <div class="colMl">
                                <span class="num">18500次</span>
                                <span class="dateVol">当月销量完成</span>
                            </div>
                            <div class="colMl">
                                <span class="num">88%</span>
                                <span class="dateVol">当月销量达成率</span>
                            </div>
                        </div>
                    </div>
                <div class="col-md-12" style="padding-left: 4%;margin-top: 20px;">
                    <div class="col-md-1 expenses">收支</div>
                    <div class="col-md-10 dataExpenses">
                        <div class="colMl">
                            <span class="num">185次</span>
                            <span class="dateVol">当月销量目标</span>
                        </div>
                        <div class="colMl">
                            <span class="num">18500次</span>
                            <span class="dateVol">当月销量完成</span>
                        </div>
                        <div class="colMl">
                            <span class="num">88%</span>
                            <span class="dateVol">当月销量达成率</span>
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
                <div class="portlet-body">

                </div>
            </div>
        </div>
    </div>
    <%--<script>--%>
        <%--$(function(){--%>
            <%--var myChart = echarts.init(document.getElementById('orderChart'));--%>
            <%--// 指定图表的配置项和数据--%>
            <%--myChart.setOption({--%>
                <%--tooltip: {},--%>
                <%--legend: {--%>
                    <%--data:['订单支数']--%>
                <%--},--%>
                <%--xAxis: {--%>
                    <%--data: []--%>
                <%--},--%>
                <%--yAxis: {},--%>
                <%--series: [--%>
                    <%--{--%>
                        <%--name: '订单支数',--%>
                        <%--type: 'line',--%>
                        <%--data: []--%>
                    <%--}]--%>
            <%--});--%>
            <%--// 异步加载数据--%>
            <%--$.post('${ctx}/main/ajaxChart/order',{},function(result) {--%>
                <%--myChart.setOption({--%>
                    <%--xAxis: {--%>
                        <%--data: result.chartLabel--%>
                    <%--},--%>
                    <%--yAxis: {--%>
                        <%--splitNumber : 5--%>
                    <%--},--%>
                    <%--series: [{--%>
                        <%--// 根据名字对应到相应的系列--%>
                        <%--name: '订单支数',--%>
                        <%--data: result.orderCount--%>
                    <%--},]--%>
                <%--});--%>
            <%--});--%>
        <%--})--%>
    <%--</script>--%>
    <%--<div class="row">--%>
        <%--<div class="col-md-12">--%>
            <%--<div class="portlet light bordered">--%>
                <%--<div class="portlet-title">--%>
                    <%--<div class="caption">--%>
                        <%--<i class="icon-graph"></i>--%>
                        <%--<span class="caption-subject bold uppercase"> 销量月度报表(${date})</span>--%>
                    <%--</div>--%>
                <%--</div>--%>
                <%--<div class="portlet-body">--%>
                    <%--<div id="orderChartXiao" style="height:350px;">--%>

                    <%--</div>--%>
                <%--</div>--%>
            <%--</div>--%>
        <%--</div>--%>
    <%--</div>--%>
    <%--<script>--%>
        <%--$(function(){--%>
            <%--var myChartxiao = echarts.init(document.getElementById('orderChartXiao'));--%>
            <%--// 异步加载数据--%>
            <%--$.post('${ctx}/main/ajaxChart/salesvolume',{},function(result) {--%>
                <%--console.log(result.achievementList);--%>
                <%--myChartxiao.setOption({--%>
                    <%--tooltip: {--%>
                        <%--trigger: 'axis',--%>
                        <%--axisPointer: {--%>
                            <%--type: 'cross',--%>
                            <%--crossStyle: {--%>
                                <%--color: '#999'--%>
                            <%--}--%>
                        <%--}--%>
                    <%--},--%>
                    <%--toolbox: {--%>
                        <%--feature: {--%>
                            <%--dataView: {show: true, readOnly: false},--%>
                            <%--magicType: {show: true, type: ['line', 'bar']},--%>
                            <%--restore: {show: true},--%>
                            <%--saveAsImage: {show: true}--%>
                        <%--}--%>
                    <%--},--%>
                    <%--legend: {--%>
                        <%--data:['用户名','达成量','目标量','达成率']--%>
                    <%--},--%>
                    <%--xAxis: [--%>
                        <%--{--%>
                            <%--type: 'category',--%>
                            <%--data: result.userNameList,--%>
                            <%--axisPointer: {--%>
                                <%--type: 'shadow'--%>
                            <%--}--%>
                        <%--}--%>
                    <%--],--%>
                    <%--yAxis: [--%>
                        <%--{--%>
                            <%--type: 'value',--%>
                            <%--name: '销量',--%>
                            <%--axisLabel: {--%>
                                <%--formatter: '{value} '--%>
                            <%--}--%>
                        <%--},--%>
                        <%--{--%>
                            <%--type: 'value',--%>
                            <%--name: '比率',--%>
                            <%--axisLabel: {--%>
                                <%--formatter: '{value}%'--%>
                            <%--}--%>
                        <%--}--%>
                    <%--],--%>
                    <%--color:['#cf3834','#31c7b2',"#589198"],--%>
                    <%--series: [--%>
                        <%--{--%>
                            <%--name:'目标量',--%>
                            <%--type:'bar',--%>
                            <%--data:result.amountTargetList--%>
                        <%--},--%>
                        <%--{--%>
                            <%--name:'达成量',--%>
                            <%--type:'bar',--%>
                            <%--data:result.amountReachedList--%>
                        <%--},--%>
                        <%--{--%>
                            <%--name:'达成率',--%>
                            <%--type:'line',--%>
                            <%--yAxisIndex: 1,--%>
                            <%--data:result.achievementList--%>
                        <%--}--%>
                    <%--]--%>
                <%--});--%>
            <%--});--%>
        <%--})--%>
    <%--</script>--%>

    <%--<div class="row">--%>
        <%--<div class="col-md-12">--%>
            <%--<div class="portlet light bordered">--%>
                <%--<div class="portlet-title">--%>
                    <%--<div class="caption">--%>
                        <%--<i class="icon-graph"></i>--%>
                        <%--<span class="caption-subject bold uppercase"> 大区总裁团队月度报表(${date})</span>--%>
                    <%--</div>--%>
                <%--</div>--%>
                <%--<div class="portlet-body">--%>
                    <%--<div id="orderChartTeam" style="height:350px;">--%>

                    <%--</div>--%>
                <%--</div>--%>
            <%--</div>--%>
        <%--</div>--%>
    <%--</div>--%>

    <%--<script>--%>
        <%--$(function(){--%>
            <%--var myChartTeam = echarts.init(document.getElementById('orderChartTeam'));--%>
            <%--// 指定图表的配置项和数据--%>
            <%--myChartTeam.setOption({--%>
                <%--tooltip : {--%>
                    <%--trigger: 'item',--%>
                <%--},--%>
                <%--legend: {--%>
                    <%--data:['新晋特级','特级']--%>
                <%--},--%>
                <%--toolbox: {--%>
                    <%--show : true,--%>
                    <%--feature : {--%>
                        <%--dataView : {show: true, readOnly: false},--%>
                        <%--magicType : {show: true, type: ['line', 'bar']},--%>
                        <%--restore : {show: true},--%>
                        <%--saveAsImage : {show: true}--%>
                    <%--}--%>
                <%--},--%>
                <%--xAxis: [--%>
                <%--],--%>
                <%--yAxis: [--%>
                <%--],--%>
                <%--series: [--%>

                <%--]--%>
            <%--});--%>
<%--// 异步加载数据--%>
            <%--$.post('${ctx}/report/teamReportNew/ajaxTeamReportNew',{},function(result) {--%>
                <%--myChartTeam.setOption({--%>
                    <%--xAxis: [--%>
                        <%--{--%>
                            <%--type: 'category',--%>
                            <%--data: result.data.namearry,--%>
                            <%--axisPointer: {--%>
                                <%--type: 'shadow'--%>
                            <%--}--%>
                        <%--}--%>
                    <%--],--%>
                    <%--yAxis: [--%>
                        <%--{--%>
                            <%--type: 'value',--%>
                            <%--name: '人数',--%>
                            <%--axisLabel: {--%>
                                <%--formatter: '{value} '--%>
                            <%--}--%>
                        <%--}--%>
                    <%--],--%>
                    <%--series: [--%>
                        <%--{--%>
                            <%--name:'新晋特级',--%>
                            <%--type:'bar',--%>
                            <%--data: result.data.newextraNumberarry,--%>
                        <%--},--%>
                        <%--{--%>
                            <%--name:'特级',--%>
                            <%--type:'bar',--%>
                            <%--data: result.data.extraNumberarry,--%>
                        <%--}--%>
                    <%--]--%>
                <%--});--%>
            <%--});--%>
        <%--})--%>
    <%--</script>--%>
    <%--<div class="row">--%>
        <%--<div class="col-md-12">--%>
            <%--<div class="portlet light bordered">--%>
                <%--<div class="portlet-title">--%>
                    <%--<div class="caption">--%>
                        <%--<i class="icon-graph"></i>--%>
                        <%--<span class="caption-subject bold uppercase"> 省份服务商活跃报表(${date})</span>--%>
                    <%--</div>--%>
                <%--</div>--%>
                <%--<div class="portlet-body">--%>
                    <%--<div id="orderChartMap" style="height:800px;">--%>

                    <%--</div>--%>
                <%--</div>--%>
            <%--</div>--%>
        <%--</div>--%>
    <%--</div>--%>
    <%--<script>--%>
        <%--var v4AvticeRate='${v4AvticeRate}';--%>
        <%--var V3='${v3}';--%>
        <%--var V4='${v4}';--%>
        <%--var newV3='${newv3}';--%>
        <%--var newV4='${newv4}';--%>
        <%--var name='${name}';--%>
        <%--var arrayv4AvticeRate=v4AvticeRate.split(",");--%>
        <%--var arraynewV3=newV3.split(",");--%>
        <%--var arraynewV4=newV4.split(",");--%>
        <%--var arrayV3=V3.split(",");--%>
        <%--var arrayV4=V4.split(",");--%>
        <%--var arrayname=name.split(",");--%>
        <%--var str="";--%>
        <%--function randomData() {--%>
            <%--return Math.round(Math.random()*1000);--%>
        <%--}--%>
        <%--function makeMapData() {--%>
            <%--var mapData = [];--%>

            <%--for(var j=0;j<arrayname.length;j++){--%>

                <%--if(arrayV3[j]!=0||arrayV4[j]!=0){--%>
                    <%--if(arrayname[j].length<4){--%>
                        <%--var arrayname2=arrayname[j].substring(0,2);--%>
                    <%--}else {--%>
                        <%--var arrayname2=arrayname[j].substring(0,2);--%>
                        <%--if(arrayname[j]=="内蒙古自治区"||arrayname[j]=="黑龙江省"){--%>
                            <%--var arrayname2=arrayname[j].substring(0,3);--%>
                        <%--}--%>
                    <%--}--%>
                    <%--mapData.push({--%>
                        <%--name: arrayname2,--%>
<%--//						selected:true,--%>
                        <%--value: randomData()--%>
                    <%--});--%>
                <%--}--%>
            <%--}--%>
            <%--return mapData;--%>
        <%--}--%>
        <%--$.get('${ctx}/map/json/china.json', function (chinaJson) {--%>
            <%--echarts.registerMap('china', chinaJson);--%>
            <%--var chart = echarts.init(document.getElementById('orderChartMap'));--%>
            <%--chart.setOption({--%>
                <%--tooltip : {--%>
                    <%--trigger: 'item',--%>
                    <%--formatter: function(param){--%>
                        <%--if(arrayname.length==1){--%>
                            <%--str = param.name + "：<br>特级人数：" + 0 + "<br>" + "新增特级人数：" + 0 +"<br>" + "省级人数：" + 0 +"<br>" + "新增省级人数：" + 0 +  "<br>" + "特级活跃度：" + 0;--%>
                        <%--}else {--%>
                            <%--for (var i = 0; i < arrayname.length; i++) {--%>
                                <%--var name = arrayname[i].substring(0, param.name.length);--%>
                                <%--if (param.name == name) {--%>
                                    <%--str = param.name + "：<br>特级人数：" + arrayV4[i] + "<br>" + "新增特级人数：" + arraynewV4[i] + "<br>" + "省级人数：" + arrayV3[i] + "<br>" + "新增省级人数：" + arraynewV3[i] +  "<br>" + "特级活跃度：" + arrayv4AvticeRate[i];--%>
                                <%--}--%>
                            <%--}--%>
                        <%--}--%>
                        <%--return str;--%>
                    <%--}--%>
                <%--},--%>
                <%--visualMap: {--%>
                    <%--min: 0,--%>
                    <%--max: 2500,--%>
                    <%--left: 'left',--%>
                    <%--top: 'bottom',--%>
                    <%--text: ['颜色值',''],           // 文本，默认为数值文本--%>
                    <%--calculable: false--%>
                <%--},--%>
                <%--series: [--%>
                    <%--{--%>
                        <%--name: '中国',--%>
                        <%--type: 'map',--%>
                        <%--mapType: 'china',--%>
                        <%--selectedMode : 'multiple',--%>
                        <%--label: {--%>
                            <%--normal: {--%>
                                <%--show: true--%>
                            <%--},--%>
                            <%--emphasis: {--%>
                                <%--show: true--%>
                            <%--}--%>
                        <%--},--%>
                        <%--itemStyle: {--%>
                            <%--normal: {--%>
                                <%--borderColor: '#389BB7',--%>
                                <%--areaColor: '#fff',--%>
                            <%--},--%>
                            <%--emphasis: {--%>
                                <%--areaColor: '#389BB7',--%>
                                <%--borderWidth: 0--%>
                            <%--}--%>
                        <%--},--%>
                        <%--data:makeMapData()--%>
                    <%--}--%>
                <%--]--%>
            <%--});--%>
            <%--chart.on('click',  function (param) {--%>
<%--//				console.log(param);--%>
<%--//				console.log(param.tooltip);--%>
            <%--});--%>
    <%--});--%>
    <%--</script>--%>