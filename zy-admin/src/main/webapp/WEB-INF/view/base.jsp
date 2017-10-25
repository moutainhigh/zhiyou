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
    #orderChartTeam4 {
        width:100%;
    }
    table tr {
        text-align: center;
    }
    table tr td {
        border-left: 1px solid #ccc;
    }

    .dateNewVol{
        border:1px solid #ccc;
        -webkit-border-radius: 5px !important;
        -moz-border-radius: 5px !important;
        -o-border-radius: 5px !important;
        border-radius:5px !important;
        -moz-box-shadow:0px 1px 10px #aaa;
        -o-box-shadow:0px 1px 10px #aaa;
        -webkit-box-shadow:0px 1px 10px #aaa;
        box-shadow:0px 1px 10px #aaa;
    }
    .number {
        width:7%;
        height:90px;
        border:1px solid #ccc;
        float: left;
        margin-left: 3%;
        margin-top: 10px;
        text-align: center;
        line-height: 90px;
        font-size: 60px;
        color: #269952;
    }
</style>
<script>
    $(document).ready(function() {
      //销量
        orderFunction();
       //特級
        userV4Function();
        //奖金
        ubFunction();


        //先调用一下在执行定时器
        ajaxOrderNumber();
        window.setInterval(ajaxOrderNumber,300000);
    });


    //处理  日销量
    function ajaxOrderNumber(){
        var  type = ${type};
        $.ajax({
            url : '${ctx}/newReport/ajaxOrderNumber',
            data : {
                type:type
            },
            dataType : 'json',
            type : 'POST',
            success : function(result) {
                if(result.code != 0) {
                    layer.msg("销量信息加载失败："+result.message);
                    return;
                }else{
                 var data = result.message;
                    for(i=0;i<data.length;i++){
                        $("#number"+i).html(data.charAt(i));
                    }


                }
            }
        });
    }

    function orderFunction () {
       var  type = ${type};
        $.ajax({
            url : '${ctx}/newReport/ajaxNewReportTeamTage',
            data : {
                type:type
            },
            dataType : 'json',
            type : 'POST',
            success : function(result) {
                if(result.code != 0) {
                    layer.msg("销量信息加载失败："+result.message);
                    return;
                }else{
                    var data =result.data;
                    $("#tageNumber").html(data.tageNumber+"次");
                    $("#finishNumber").html(data.finishNumber+"次")
                    $("#rate").html(data.rate+"%")

                }
            }
        });
    }
    function userV4Function() {
        var  type = ${type};
        $.ajax({
            url : '${ctx}/newReport/ajaxNewReportTeamV4',
            data : {
                type:type
            },
            dataType : 'json',
            type : 'POST',
            success : function(result) {
                if(result.code != 0) {
                    layer.msg("团队信息加载失败："+result.message);
                    return;
                }else{
                    var data =result.data;
                    $("#V4Number").html(data.V4Number+"人");
                    $("#V4NewNumber").html(data.V4NewNumber+"人")
                    $("#V4SleepNumber").html(data.V4SleepNumber+"人")
                }
            }
        });
    }

    function ubFunction() {
        var  type = ${type};
        $.ajax({
            url : '${ctx}/newReport/ajaxNewReportTeamUb',
            data : {
                type:type
            },
            dataType : 'json',
            type : 'POST',
            success : function(result) {
                if(result.code != 0) {
                    layer.msg("奖励信息加载失败："+result.message);
                    return;
                }else{
                    var data =result.data;
                    $("#Ub").html(data.Ub+"元");
                    $("#mMunber").html(data.mMunber+"元")
                    $("#yMunber").html(data.yMunber+"元")
                }
            }
        });
    }
</script>
<div class="page-bar">
    <ul class="page-breadcrumb">
        <li><img src="${ctx}/image/dataXiao.png" /> <span class="ajaxify">今日销量</span>
    </ul>

</div>
<div class="portlet-body">
    <div class="dateNewVol" style="margin-left:5%;width: 90%;height: 110px;border: 1px solid #ccc; margin-bottom: 30px;">
        <div class="clearfloat" style="margin-left: 2%;">
             <div class="number" id="number0">0</div>
             <div class="number" id="number1">0</div>
             <div class="number" id="number2">0</div>
             <div class="number" id="number3">0</div>
             <div class="number" id="number4">0</div>
             <div class="number" id="number5">0</div>
             <div class="number" id="number6">0</div>
             <div class="number" id="number7">0</div>
             <div class="number" id="number8">0</div>
            <span style="float:left;font-size: 45px;color: #269952;margin-left: 3%;margin-top: 25px;">次</span>
        </div>
    </div>
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
                            <span class="num" id="tageNumber">0次</span>
                            <span class="dateVol">当月销量目标</span>
                        </div>
                        <div class="colMl" change="2" onmouseover="scaleCol(this)" onmouseout="scaleColTo(this)">
                            <span class="num" id="finishNumber">0次</span>
                            <span class="dateVol">当月销量完成</span>
                        </div>
                        <div class="colMl" change="3" onmouseover="scaleCol(this)" onmouseout="scaleColTo(this)">
                            <span class="num" id="rate">0%</span>
                            <span class="dateVol">当月销量达成率</span>
                        </div>
                    </div>
                </div>
                <div class="col-md-12" style="margin-top: 20px;">
                    <div class="col-md-2 team">团队</div>
                        <div class="col-md-10 dataTeam">
                            <div class="colMl" change="4" onmouseover="scaleCol(this)" onmouseout="scaleColTo(this)">
                                <span class="num" id="V4Number">0人</span>
                                <span class="dateVol">当月总特级数</span>
                            </div>
                            <div class="colMl" change="5" onmouseover="scaleCol(this)" onmouseout="scaleColTo(this)">
                                <span class="num" id="V4NewNumber">0人</span>
                                <span class="dateVol">当月新增特级数</span>
                            </div>
                            <div class="colMl" change="6" onmouseover="scaleCol(this)" onmouseout="scaleColTo(this)">
                                <span class="num" id="V4SleepNumber">0人</span>
                                <span class="dateVol">沉睡特级数</span>
                            </div>
                        </div>
                    </div>
                <div class="col-md-12" style="margin-top: 20px;">
                    <div class="col-md-2 expenses">收支</div>
                    <div class="col-md-10 dataExpenses">
                        <div class="colMl" change="7" onmouseover="scaleCol(this)" onmouseout="scaleColTo(this)">
                            <span class="num" id="Ub">0元</span>
                            <span class="dateVol">当月U币充值金额</span>
                        </div>
                        <div class="colMl" change="8" onmouseover="scaleCol(this)" onmouseout="scaleColTo(this)">
                            <span class="num" id="mMunber">0元</span>
                            <span class="dateVol">上月奖励发放金额</span>
                        </div>
                        <div class="colMl" change="9" onmouseover="scaleCol(this)" onmouseout="scaleColTo(this)">
                            <span class="num"id="yMunber">0元</span>
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
                        <span class="caption-subject bold uppercase uppercaseNew">销量统计</span>
                    </div>
                </div>
                <div class="portlet-body clearfloat">
                      <div class="col-md-4 firstExpenses">
                          <div class="allExpenses on1" change="1" onclick="ajaxTeam(this)">销量</div>
                          <div class="allExpenses" style="border-left: none;" change="2" onclick="ajaxTeam(this)">团队</div>
                          <div class="allExpenses" style="border-left: none;" change="3"  onclick="ajaxTeam(this)">收支</div>
                      </div>
                </div>
                <div class="AllThrVolume clearfloat">
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
                            <li><span class="ajaxify">每月历史完成销量</span>
                        </ul>
                    </div>
                    <div class="portlet-body">
                        <div id="orderChart2" style="height:350px;">

                        </div>
                    </div>
                    <div style="float:left;width:48%;">
                        <div class="page-bar" style="margin-top: 30px;">
                            <ul class="page-breadcrumb">
                                <li><span class="ajaxify">当月完成量及目标量</span>
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
                                <li><span class="ajaxify">当月达成的销量占比</span>
                            </ul>
                        </div>
                        <div class="portlet-body">
                            <div id="orderChart4" style="height:350px;">

                            </div>
                        </div>
                    </div>
                    <div style="float:left;width:48%;">
                        <div class="page-bar" style="margin-top: 30px;">
                            <ul class="page-breadcrumb">
                                <li><span class="ajaxify">销量环比</span>
                            </ul>
                        </div>
                        <div class="portlet-body">
                            <div id="orderChart5" style="height:350px;">

                            </div>
                        </div>
                    </div>
                    <div style="float:left;width:48%;margin-left: 4%;">
                        <div class="page-bar" style="margin-top: 30px;">
                            <ul class="page-breadcrumb">
                                <li><span class="ajaxify">销量同比</span>
                            </ul>
                        </div>
                        <div class="portlet-body">
                            <div id="orderChart6" style="height:350px;">

                            </div>
                        </div>
                    </div>
                </div>
                <%--团队HTML--%>
                <div class="AllThrTeam clearfloat" style="display: none">
                    <div style="float:left;width:30%;">
                        <div class="page-bar" style="margin-top: 30px;">
                            <ul class="page-breadcrumb">
                                <li><span class="ajaxify">总人数</span>
                            </ul>
                        </div>
                        <div class="portlet-body">
                            <div id="orderChartTeam1" style="height:350px;">

                            </div>
                        </div>
                    </div>
                    <div style="float:left;width:30%;margin-left: 5%">
                        <div class="page-bar" style="margin-top: 30px;">
                            <ul class="page-breadcrumb">
                                <li><span class="ajaxify">本月新增人员占比</span>
                            </ul>
                        </div>
                        <div class="portlet-body">
                            <div id="orderChartTeam2" style="height:350px;">

                            </div>
                        </div>
                    </div>
                    <div style="float:left;width:30%;margin-left: 5%">
                        <div class="page-bar" style="margin-top: 30px;">
                            <ul class="page-breadcrumb">
                                <li><span class="ajaxify">沉睡人员占比</span>
                            </ul>
                        </div>
                        <div class="portlet-body">
                            <div id="orderChartTeam3" style="height:350px;">

                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-12">
                            <div class="portlet light bordered clearfloat">
                                <div class="portlet-title">
                                    <div class="caption">
                                        <i class="icon-graph"></i>
                                        <span class="caption-subject bold uppercase"> 人员省份分布图</span>
                                    </div>
                                </div>
                                <div class="portlet-body" style="width: 70%;float: left;">
                                    <div id="orderChartTeam4" style="height:800px;">

                                    </div>
                                </div>
                                <table class="table table-hover" style="float: left;width: 25%;height:150px;border: 1px solid #ccc;margin-top: 200px;margin-left: 4%;" >
                                    <tr id="areatV4user">
                                        <td>排名</td>
                                        <td>省份</td>
                                        <td>人数</td>
                                        <td>排名变化</td>
                                    </tr>

                                  <%--  <tr>
                                        <td><img src="${ctx}/image/sec.png" style="width: 25px;"/></td>
                                        <td>江苏省</td>
                                        <td>598</td>
                                        <td>2<img src="${ctx}/image/up.png" style="width:10px;"/><img src="${ctx}/image/down.png" style="width:10px;"/></td>
                                    </tr>
                                    <tr>
                                        <td><img src="${ctx}/image/thr.png" style="width: 25px;"/></td>
                                        <td>安徽省</td>
                                        <td>550</td>
                                        <td>3<img src="${ctx}/image/up.png" style="width:10px;"/><img src="${ctx}/image/down.png" style="width:10px;"/></td>
                                    </tr>
                                    <tr>
                                        <td>4</td>
                                        <td>上海市</td>
                                        <td>520</td>
                                        <td>2<img src="${ctx}/image/up.png" style="width:10px;"/><img src="${ctx}/image/down.png" style="width:10px;"/></td>
                                    </tr>
                                    <tr>
                                        <td>5</td>
                                        <td>北京市</td>
                                        <td>501</td>
                                        <td>1<img src="${ctx}/image/up.png" style="width:10px;"/><img src="${ctx}/image/down.png" style="width:10px;"/></td>
                                    </tr>--%>
                                    <tr><td colspan="4" style="background: rgba(49,199,178,0.5);color: #fff;cursor: pointer" onclick="alertMy(this)">点击查看更多</td></tr>
                                </table>

                            </div>
                        </div>
                    </div>
                </div>
                <div class="AllThrExpenses" style="display: none">
                    <div class="page-bar" style="margin-top: 30px;">
                        <ul class="page-breadcrumb">
                            <li><span class="ajaxify">历史收益情况</span>
                        </ul>
                    </div>
                    <div class="portlet-body">
                        <div id="orderChartEX1" style="height:350px;">

                        </div>
                    </div>
                    <div style="float:left;width:48%;">
                        <div class="page-bar" style="margin-top: 30px;">
                            <ul class="page-breadcrumb">
                                <li><span class="ajaxify">收益环比</span>
                            </ul>
                        </div>
                        <div class="portlet-body">
                            <div id="orderChartEX2" style="height:350px;">

                            </div>
                        </div>
                    </div>
                    <div style="float:left;width:48%;margin-left: 4%;">
                        <div class="page-bar" style="margin-top: 30px;">
                            <ul class="page-breadcrumb">
                                <li><span class="ajaxify">收益同比</span>
                            </ul>
                        </div>
                        <div class="portlet-body">
                            <div id="orderChartEX3" style="height:350px;">

                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

<script>
    var colors = ['#07a33e', '#97c618', '#f5804a', '#18d6d7', '#0090e2',"#3765a1",'#9a0432','#326966','#67686a','#fdfd07','#feffc6','#ce9a2d','#696796','#97cbfd','#646a00'];
    var tablehtml= "";
    function alertMy(obj){

        layer.open({
            type: 1,
            area:['50%', '50%'],
         /*   skin: 'layui-layer-rim', //加上边框*/
            content: tablehtml
        });
    }
    $(function(){

        var myChartxiao1 = echarts.init(document.getElementById('orderChart1'));
        // 异步加载数据
        $.post('${ctx}/newReport/largeAreaDaySales?type='+${type},function(result) {
            var dataMap = result.data.largeAreaMonthDaySales ;
            var days = result.data.days ;
            var categories = [];
            var daySales = [];
            for (let i in dataMap) {
                categories.unshift(i);
                var sArray = dataMap[i];
                var arrayT = sArray.split(",");
                daySales.unshift(arrayT);
            }
            myChartxiao1.setOption({
                color: colors,
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
                    data : days
                },
                yAxis: {
                    type: 'value'
                },
                series : (function (){
                    var series = [];
                    for (let i = 0; i <categories.length; i++) {
                        series.push({
                            name:categories[i],
                            type:'line',
                            data:daySales[i]
                        });
                    }
                    return series;
                })()
            });
        });

        var myChartxiao3 = echarts.init(document.getElementById('orderChart3'));
        // 异步加载数据salesAndTargets
        $.post('${ctx}/newReport/monthSalesAndTarget?type='+${type},function(result) {
            var categories = new Array();
            var targets = [];
            var finish = [];
            var rate = [];
            var siteMap = eval(result.data.salesAndTargets);
            for(var j=0;j<siteMap.length;j++) {
                var newsiteMap = siteMap[j];
                for ( var key in newsiteMap ) {
                    categories[j]=key;
                    targets[j]=newsiteMap[key][0];
                    finish[j]=newsiteMap[key][1];
                    rate[j]=newsiteMap[key][2];
                }
            }
            myChartxiao3.setOption({
                color: colors,
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
                        data:targets
                    },
                    {
                        name:'完成量',
                        type:'bar',
                        data:finish
                    },
                    {
                        name:'完成率',
                        type:'line',
                        yAxisIndex: 1,
                        data:rate
                    }
                ]
            });
        });
        var myChartxiao4 = echarts.init(document.getElementById('orderChart4'));
        // 异步加载数据
        $.post('${ctx}/newReport/ajaxLargeAreaThisMonthSalesHaveRate?type='+${type},function(result) {
            var categories = new Array();
            var data = [];
            var siteMap = eval(result.data.thisMonthSales);
            for(var j=0;j<siteMap.length;j++) {
                var newsiteMap = siteMap[j];
                for ( var key in newsiteMap ) {
                    categories[j]=key;
                    data.push({
                        value:newsiteMap[key],
                        name:key
                    });
                }

            }
            myChartxiao4.setOption({
                color: colors,
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
        var myChartxiao5 = echarts.init(document.getElementById('orderChart5'));
        // 异步加载数据
        $.post('${ctx}/newReport/largeAreaMonthSalesRelativeRate?type='+${type},function(result) {
            //获取大区年份销量数据
            var relativeRate = result.data.largeAreaYearRelativeRate;
            var area = [];   //五大区数组
            var rRate = [];  //数组里面方数组，每个数组里面就是每个大区的量
            for (let i in relativeRate) {
                area.unshift(i);
                var sArray = relativeRate[i];
                var arrayT = sArray.split(",");
                rRate.unshift(arrayT);
            }
            {
            //环比
            myChartxiao5.setOption({
                color: colors,
                tooltip: {
                    trigger: 'axis',
                },
                legend: [{
                    data: area.map(function (a) {
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
                    data: ['1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月', '9月', '10月', '11月', '12月']
                },
                yAxis: {
                    type: 'value',
                    axisLabel: {
                        formatter: '{value} %'
                    }
                },
                series: (function () {
                    var series = [];
                    for (let i = 0; i < area.length; i++) {
                        series.push({
                            name: area[i],
                            type: 'line',
                            data: rRate[i]
                        });
                    }
                    return series;
                })()
            });
        }
            {
                //同比
                var sameiveRate = result.data.largeAreaYearSameRate;
                var sRate = [];
                for (let i in sameiveRate) {
                    var sArray = sameiveRate[i];
                    var arrayT = sArray.split(",");
                    sRate.unshift(arrayT);
                }
                var myChartxiao6 = echarts.init(document.getElementById('orderChart6'));
                myChartxiao6.setOption({
                    color: colors,
                    tooltip: {
                        trigger: 'axis'
                    },
                    legend: [{
                        data: area.map(function (a) {
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
                        data: ['1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月', '9月', '10月', '11月', '12月']
                    },
                    yAxis: {
                        type: 'value',
                        axisLabel: {
                            formatter: '{value} %'
                        }
                    },
                    series: (function () {
                        var series = [];
                        for (let i = 0; i < area.length; i++) {
                            series.push({
                                name: area[i],
                                type: 'line',
                                data: sRate[i]
                            });
                        }
                        return series;
                    })()
                });
            }
            {
                //大区年份销量
                var yearSales = result.data.largeAreaYearSales;
                var sales = [];
                for (let i in yearSales) {
                    var sArray = yearSales[i];
                    var arrayT = sArray.split(",");
                    sales.unshift(arrayT);
                }
                var myChartxiao2 = echarts.init(document.getElementById('orderChart2'));
                myChartxiao2.setOption({
                    color: colors,
                    tooltip: {
                        trigger: 'axis'
                    },
                    legend: [{
                        data: area.map(function (a) {
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
                    calculable: true,
                    xAxis: [
                        {
                            type: 'category',
                            data: ['1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月', '9月', '10月', '11月', '12月']
                        }
                    ],
                    yAxis: [
                        {
                            type: 'value'
                        }
                    ],
                    series: (function () {
                        var series = [];
                        for (let i = 0; i < area.length; i++) {
                            series.push({
                                name: area[i],
                                type: 'line',
                                data: sales[i]
                            });
                        }
                        return series;
                    })()
                });
            }
        });
    })
</script>
<script>

     function scaleColTeam(){
         var myCharTeam1 = echarts.init(document.getElementById('orderChartTeam1'));
         // 异步加载数据
         $.post('${ctx}/newReport/ajaxNewReportTeamNumber?type='+${type},{},function(result) {
             var categories = new Array();
             var categoriesData = new Array();
             var siteMap = eval(result.data.number);
             for(var j=0;j<siteMap.length;j++) {
                 var newsiteMap = siteMap[j];
                 for ( var key in newsiteMap ) {
                     categories[j]=key;
                     categoriesData[j]=newsiteMap[key];

                 }

             }
             myCharTeam1.setOption({
                 color: colors,
                 tooltip: {
                     trigger: 'axis',
                     axisPointer: {
                         type: 'shadow'
                     }
                 },
                 legend: {
                     data: categories.map(function (a) {
                         return a;
                     })
                 },
                 grid: {
                     left: '3%',
                     right: '4%',
                     bottom: '3%',
                     containLabel: true
                 },
                 xAxis: {
                     type: 'value',
                     boundaryGap: [0, 0.01]
                 },
                 yAxis: {
                     type: 'category',
                     data: categories.map(function (a) {
                         return a;
                     })
                 },
                 series: [
                     {
                         name:"总人数",
                         type: 'bar',
                         data: categoriesData
                     }
                 ]
             });
         });
         var myCharTeam2 = echarts.init(document.getElementById('orderChartTeam2'));
         // 异步加载数据
         $.post('${ctx}/newReport/ajaxNewReportTeamNewNumber?type='+${type},{},function(result) {
             var categories = new Array();
             var data = [];
             var siteMap = eval(result.data.newV4);
             for(var j=0;j<siteMap.length;j++) {
                 var newsiteMap = siteMap[j];
                 for ( var key in newsiteMap ) {
                     categories[j]=key;
                     data.push({
                         value:newsiteMap[key],
                         name:key
                     });
                 }

             }
             myCharTeam2.setOption({
                 color: colors,
                 tooltip : {
                     trigger: 'item',
                     formatter: "{a} <br/>{b} : {c} ({d}%)"
                 },
//                 legend: {
//                     orient: 'vertical',
//                     left: 'left',
//                     data: categories.map(function (a) {
//                         return a;
//                     })
//                 },
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
         var myCharTeam3 = echarts.init(document.getElementById('orderChartTeam3'));
         // 异步加载数据
         $.post('${ctx}/newReport/ajaxNewReportTeamSleepNumber?type='+${type},{},function(result) {
             var categories = new Array();
             var data = [];
             var siteMap = eval(result.data.sleep);
             for(var j=0;j<siteMap.length;j++) {
                 var newsiteMap = siteMap[j];
                 for ( var key in newsiteMap ) {
                     categories[j]=key;
                     data.push({
                         value:newsiteMap[key],
                         name:key
                     });
                 }
             }

             myCharTeam3.setOption({
                 color: colors,
                 tooltip : {
                     trigger: 'item',
                     formatter: "{a} <br/>{b} : {c} ({d}%)"
                 },
//                 legend: {
//                     orient: 'vertical',
//                     left: 'left',
//                     data: categories.map(function (a) {
//                         return a;
//                     })
//                 },
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
         $.post('${ctx}/newReport/ajaxNewReportTeamAreat?type='+${type},{},function(result) {
             tablehtml="";
             $("#areatV4user").siblings(".tr").remove();
             tablehtml=  "<table class='table table-hover' style='float: left;width: 100%;border: 1px solid #ccc;' > <tr  style='text-align: center;'> " +
             "<td>排名</td> <td>省份</td> <td>人数</td> <td>排名变化</td> </tr>";
             var data1 = [];
             var siteMap = result.data.areat;
             var i=0;
             var trhtml="";
             var imges;
             for(var j=0;j<siteMap.length;j++) {
                 var newsite = siteMap[j];
                 var key = newsite.provinceName;
                     if (key =="内蒙古自治区"||key=="黑龙江省"){
                         key = key.substring(0,3);
                     }else{
                         key = key.substring(0,2);
                     }
                     data1.push({
                         value:newsite.number,
                         name:key
                     });
                if(i<5){
                 if (newsite.rank==1){
                     trhtml=trhtml+ "<tr class='tr'> <td><img src='${ctx}/image/first.png' style='width:20px;' /></td><td>"+key +"</td><td>"+newsite.number+"</td>";
                     if(newsite.rankChange>0){
                         trhtml=trhtml+"<td>"+newsite.rankChange+"<img src='${ctx}/image/up.png' style='width:10px;' /></td> </tr>";
                     }else if(newsite.rankChange<0){
                         trhtml=trhtml+"<td>"+newsite.rankChange+"<img src='${ctx}/image/down.png' style='width:10px;' /></td> </tr>";
                     }else{
                         trhtml=trhtml+"<td>"+newsite.rankChange+"</td> </tr>";
                     }

                 } else if(newsite.rank==2){
                     trhtml=trhtml+ "<tr class='tr'> <td><img src='${ctx}/image/sec.png' style='width:20px;' /></td> <td>"+key +"</td><td>"+newsite.number+"</td>";
                     if(newsite.rankChange>0){
                         trhtml= trhtml+"<td>"+newsite.rankChange+"<img src='${ctx}/image/up.png' style='width:10px;' /></td> </tr>";
                     }else if(newsite.rankChange<0){
                         trhtml= trhtml+"<td>"+newsite.rankChange+"<img src='${ctx}/image/down.png' style='width:10px;' /></td> </tr>";
                     }else{
                         trhtml= trhtml+"<td>"+newsite.rankChange+"</td> </tr>";
                     }

                 }else if(newsite.rank==3){
                     trhtml=trhtml+ "<tr class='tr'> <td><img src='${ctx}/image/thr.png' style='width:20px;' /></td>  <td>"+key +"</td><td>"+newsite.number+"</td>";
                     if(newsite.rankChange>0){
                         trhtml= trhtml+"<td>"+newsite.rankChange+"<img src='${ctx}/image/up.png' style='width:10px;' /></td> </tr>";
                     }else if(newsite.rankChange<0){
                         trhtml=  trhtml+"<td>"+newsite.rankChange+"<img src='${ctx}/image/down.png' style='width:10px;' /></td> </tr>";
                     }else{
                         trhtml=  trhtml+"<td>"+newsite.rankChange+"</td> </tr>";
                     }
                 }else{
                     trhtml=trhtml+ "<tr class='tr'> <td>"+newsite.rank+"</td> <td>"+key +"</td><td>"+newsite.number+"</td>";
                     if(newsite.rankChange>0){
                         trhtml= trhtml+"<td>"+newsite.rankChange+"<img src='${ctx}/image/up.png' style='width:10px;' /></td> </tr>";
                     }else if(newsite.rankChange<0){
                         trhtml=  trhtml+"<td>"+newsite.rankChange+"<img src='${ctx}/image/down.png' style='width:10px;' /></td> </tr>";
                     }else{
                         trhtml= trhtml+"<td>"+newsite.rankChange+"</td> </tr>";
                     }
                 }
               }
              i++;
               //拼接   更多页面
                 if (newsite.rank==1){
                     tablehtml=tablehtml+ "<tr class='tr'> <td><img src='${ctx}/image/first.png' style='width:20px;' /></td><td>"+key +"</td><td>"+newsite.number+"</td>";
                     if(newsite.rankChange>0){
                         tablehtml=tablehtml+"<td>"+newsite.rankChange+"<img src='${ctx}/image/up.png' style='width:10px;' /></td> </tr>";
                     }else if(newsite.rankChange<0){
                         tablehtml=tablehtml+"<td>"+newsite.rankChange+"<img src='${ctx}/image/down.png' style='width:10px;' /></td> </tr>";
                     }else{
                         tablehtml=tablehtml+"<td>"+newsite.rankChange+"</td> </tr>";
                     }

                 } else if(newsite.rank==2){
                     tablehtml=tablehtml+ "<tr class='tr'> <td><img src='${ctx}/image/sec.png' style='width:20px;' /></td> <td>"+key +"</td><td>"+newsite.number+"</td>";
                     if(newsite.rankChange>0){
                         tablehtml= tablehtml+"<td>"+newsite.rankChange+"<img src='${ctx}/image/up.png' style='width:10px;' /></td> </tr>";
                     }else if(newsite.rankChange<0){
                         tablehtml= tablehtml+"<td>"+newsite.rankChange+"<img src='${ctx}/image/down.png' style='width:10px;' /></td> </tr>";
                     }else{
                         tablehtml= tablehtml+"<td>"+newsite.rankChange+"</td> </tr>";
                     }

                 }else if(newsite.rank==3){
                     tablehtml=tablehtml+ "<tr class='tr'> <td><img src='${ctx}/image/thr.png' style='width:20px;' /></td>  <td>"+key +"</td><td>"+newsite.number+"</td>";
                     if(newsite.rankChange>0){
                         tablehtml= tablehtml+"<td>"+newsite.rankChange+"<img src='${ctx}/image/up.png' style='width:10px;' /></td> </tr>";
                     }else if(newsite.rankChange<0){
                         tablehtml=  tablehtml+"<td>"+newsite.rankChange+"<img src='${ctx}/image/down.png' style='width:10px;' /></td> </tr>";
                     }else{
                         tablehtml=  tablehtml+"<td>"+newsite.rankChange+"</td> </tr>";
                     }
                 }else{
                     tablehtml=tablehtml+ "<tr class='tr'> <td>"+newsite.rank+"</td> <td>"+key +"</td><td>"+newsite.number+"</td>";
                     if(newsite.rankChange>0){
                         tablehtml= tablehtml+"<td>"+newsite.rankChange+"<img src='${ctx}/image/up.png' style='width:10px;' /></td> </tr>";
                     }else if(newsite.rankChange<0){
                         tablehtml=  tablehtml+"<td>"+newsite.rankChange+"<img src='${ctx}/image/down.png' style='width:10px;' /></td> </tr>";
                     }else{
                         tablehtml= tablehtml+"<td>"+newsite.rankChange+"</td> </tr>";
                     }
                 }
             }
             tablehtml=tablehtml+"</table>"
            $("#areatV4user").after(trhtml);
          $.get('${ctx}/map/json/china.json', function (chinaJson) {
             echarts.registerMap('china', chinaJson);
             var orderChartTeam4 = echarts.init(document.getElementById('orderChartTeam4'));
             orderChartTeam4.setOption({
                 color: colors,
                 tooltip: {
                     trigger: 'item'
                 },
                 legend: {
                     orient: 'vertical',
                     left: 'left',
                 },
                 visualMap: {
                     min: 0,
                     max: 100,
                     left: 'left',
                     top: 'bottom',
                     text: ['人数高','人数低'],           // 文本，默认为数值文本
                     calculable: true
                 },
                 toolbox: {
                     show: true,
                     orient: 'vertical',
                     left: 'right',
                     top: 'center',
                     feature: {
                         dataView: {readOnly: false},
                         restore: {},
                         saveAsImage: {}
                     }
                 },
                 series: [
                     {
                         name: '特级人数',
                         type: 'map',
                         mapType: 'china',
                         roam: false,
                         label: {
                             normal: {
                                 show: true
                             },
                             emphasis: {
                                 show: true
                             }
                         },
                        data:data1
                       /*  data:[
                             {name: '北京',value: 10 },
                             {name: '天津',value: randomData() },
                             {name: '上海',value: randomData() },
                             {name: '重庆',value: randomData() },
                             {name: '河北',value: randomData() },
                             {name: '河南',value: randomData() },
                             {name: '云南',value: randomData() },
                             {name: '辽宁',value: randomData() },
                             {name: '黑龙江',value: randomData() },
                             {name: '湖南',value: randomData() },
                             {name: '安徽',value: randomData() },
                             {name: '山东',value: randomData() },
                             {name: '新疆',value: randomData() },
                             {name: '江苏',value: randomData() },
                             {name: '浙江',value: randomData() },
                             {name: '江西',value: randomData() },
                             {name: '湖北',value: randomData() },
                             {name: '广西',value: randomData() },
                             {name: '甘肃',value: randomData() },
                             {name: '山西',value: randomData() },
                             {name: '内蒙古',value: randomData() },
                             {name: '陕西',value: randomData() },
                             {name: '吉林',value: randomData() },
                             {name: '福建',value: randomData() },
                             {name: '贵州',value: randomData() },
                             {name: '广东',value: randomData() },
                             {name: '青海',value: randomData() },
                             {name: '西藏',value: randomData() },
                             {name: '四川',value: randomData() },
                             {name: '宁夏',value: randomData() },
                             {name: '海南',value: randomData() },
                             {name: '台湾',value: randomData() },
                             {name: '香港',value: randomData() },
                             {name: '澳门',value: randomData() }
                         ]*/
                     }
                 ]
             });
         });
    });
     }
     //收益方法
    function scaleColExpenses(){
        var orderChartEX1 = echarts.init(document.getElementById('orderChartEX1'));
        // 异步加载数据
        $.post('${ctx}/report/profit/profit?type='+${type},{},function(result) {
            //获取收益数据
            var data = result.data.profit;
            var area = [];
            var profit = [];
            for (let i in data) {
                area.unshift(i);
                var sArray = data[i];
                var arrayT = sArray.split(",");
                profit.unshift(arrayT);
            }

            {
            orderChartEX1.setOption({
                color: colors,
                tooltip: {
                    trigger: 'axis'
                },
                legend: [{
                    data: area.map(function (a) {
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
                calculable: true,
                xAxis: [
                    {
                        type: 'category',
                        data: ['1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月', '9月', '10月', '11月', '12月']
                    }
                ],
                yAxis: [
                    {
                        type: 'value'
                    }
                ],
                series: (function () {
                    var series = [];
                    for (var i = 0; i < area.length; i++) {
                        series.push({
                            name: area[i],
                            type: 'bar',
                            data: profit[i]
                        });
                    }
                    return series;
                })()
            });
        }
            {
                //环比
                //获取环比数据
                var relativeRate = result.data.relativeRate;
                var rRate = [];
                for (let i in relativeRate) {
                    var sArray = relativeRate[i];
                    var arrayT = sArray.split(",");
                    rRate.unshift(arrayT);
                }
                var orderChartEX2 = echarts.init(document.getElementById('orderChartEX2'));
                orderChartEX2.setOption({
                    color: colors,
                    tooltip: {
                        trigger: 'axis'
                    },
                    legend: [{
                        data: area.map(function (a) {
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
                        data: ['1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月', '9月', '10月', '11月', '12月']
                    },
                    yAxis: {
                        type: 'value',
                        axisLabel: {
                            formatter: '{value} %'
                        }
                    },
                    series: (function () {
                        var series = [];
                        for (let i = 0; i < area.length; i++) {
                            series.push({
                                name: area[i],
                                type: 'line',
                                data: rRate[i]
                            });
                        }
                        return series;
                    })()

                });
            }
            {
                //同比
                //获取环比数据
                var sameiveRate = result.data.sameiveRate;
                var sRate = [];
                for (let i in sameiveRate) {
                    var sArray = sameiveRate[i];
                    var arrayT = sArray.split(",");
                    sRate.unshift(arrayT);
                }
                var orderChartEX3 = echarts.init(document.getElementById('orderChartEX3'));
                orderChartEX3.setOption({
                    color: colors,
                    tooltip: {
                        trigger: 'axis'
                    },
                    legend: [{
                        data: area.map(function (a) {
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
                        data: ['1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月', '9月', '10月', '11月', '12月']
                    },
                    yAxis: {
                        type: 'value',
                        axisLabel: {
                            formatter: '{value} %'
                        }
                    },
                    series: (function () {
                        var series = [];
                        for (let i = 0; i < area.length; i++) {
                            series.push({
                                name: area[i],
                                type: 'line',
                                data: sRate[i]
                            });
                        }
                        return series;
                    })()
                });
            }
        });
    }
     function randomData() {
         return Math.round(Math.random()*1000);
     }
</script>
<script>
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
                $(".uppercaseNew").html("销量统计");
                break;
            case 2:
                $(obj).addClass("on2");
                $(obj).siblings(".allExpenses").eq(0).removeClass("on1");
                $(obj).siblings(".allExpenses").eq(1).removeClass("on3");
                $(".AllThrTeam").show();
                $(".AllThrVolume,.AllThrExpenses").hide();
                $(".uppercaseNew").html("团队统计");
                scaleColTeam();
                break;
            case 3:
                $(obj).addClass("on3");
                $(obj).siblings(".allExpenses").eq(0).removeClass("on1");
                $(obj).siblings(".allExpenses").eq(1).removeClass("on2");
                $(".AllThrExpenses").show();
                $(".AllThrVolume,.AllThrTeam").hide();
                $(".uppercaseNew").html("收支统计");
                scaleColExpenses();
                break;
            default:
        }
    }
</script>
