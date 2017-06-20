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

    <title>沉睡成员详情</title>
    <%@ include file="/WEB-INF/view/include/head.jsp"%>
    <style>
        body {background: #f7f7f9}
        /*清除浮动代码*/
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
        body,html {font-family:  "Microsoft YaHei";text-overflow: ellipsis;
            white-space: nowrap;}
        a {display: block;}
        .all {
            width: 100%;
            background: #fff !important;
            border-bottom: 1px solid #e5e5e5;
        }
        .teamAll img {float: left;width: 50px;height: 50px;margin-left: 8px;}
        .teamAll span {
            float: left;line-height: 50px;margin-left: 20px;font-size: 18px;
            color: #838385;
        }
        .teamAll a {
            float: right;
            line-height: 50px;
            color: #6cb92d;
            margin-right: 15px;
        }
        .TeamName img {
            width:55%;
            margin-top:20px;
        }
        .TeamName span {
            display: block;
            margin-top:10px;
        }
        .rankingAllList {border-bottom: 1px solid #e5e5e5;}
        .rankingAll {
            width:100%;
            height:60px;
        }
        .rankingAll>span {
            float: left;
            display: block;;
            width:65px;
            height:100%;
            text-align: center;
            line-height:60px;
        }
        .rankingAll>img {
            float: left;
            width: 40px;
            margin-top: 10px;
            margin-bottom: 10px;
            height:40px;
            -webkit-border-radius:50%;
            -moz-border-radius:50%;
            border-radius:50%;
        }
        .ranking {
            float: left;
            width:100px;
            height:100%;
            text-align: center;
            line-height: 60px;
        }
        .rankingSpan {
            padding:0 8px 0 8px;
            color: #fff;
            font-size: 12px;
        }
        .teamTop {
            width:90%;
            height:60px;
            margin-left: 5%;
            position: relative;
        }
        .teamTop img {
            position: absolute;
            width:70%;
            height:30px;
            top: 15px;
            z-index: 1;
        }
        .teamTop input {
            position: absolute;
            background: none;
            width:70%;
            height:30px;
            border: none;
            top: 15px;
            z-index: 2;
        }
        .teamTop img.seatchImg,.searchBtn {
            width:20%;
            height:30px;
            color: #6cb92d;
            text-align: center;
            line-height: 30px;
            position: absolute;
            left:80%;
            top:15px;
            z-index: 1;
        }
        .searchBtn {
            background: none;
            border:none;
            z-index: 2;
        }
        .rankingTop {font-size: 18px}
        .rankingAll>a.tel,.rankingAll>span.tel {
            width:110px;
            float: right;
            margin-right: 20px;
            color: #303134;
            line-height: 60px;
        }
        .rankingAll>span.telDetil {width:50px;}
        .sanjiao {
            float: left;
            height:60px;
            width:50px;
            position: relative;
        }
        .jiaoOne {
            width: 0;
            height: 0;
            border-top:8px solid transparent;
            border-bottom: 8px solid transparent;
            border-left: 8px solid #b4b4b4;
            position: absolute;
            left: 50%;
            top:50%;
            margin-left: -4px;
            margin-top: -8px;
        }
        .jiaoTwo {
            width: 0;
            height: 0;
            border-left:8px solid transparent;
            border-right:8px solid transparent;
            border-top:8px solid #b4b4b4;
            border-bottom: 0px solid transparent;
            position: absolute;
            left: 50%;
            top:50%;
            margin-left: -8px;
            margin-top: -4px;
        }
        /*特级*/
        .must {
            background: #22b5d4;
        }
        .province {
            background: #fe543e;
        }
        .city {
            background: #fb8604;
        }
        .telAll {
            float: right;
            margin-right: 20px;
            height:60px;
        }
        .telAll img {
            width:20px;
            margin-top: 20px;
        }
        .jian {margin-right: 0;padding-right: 20px;}
        .jian img {
            margin-top: 24px;
        }
        .allLast {display: none;}
        .searchList {
            width:100%;
            height: 80px;
            background: #fff;
            text-align: center;
            line-height:80px;
            font-size: 20px;
            display: none;
        }
        .searchListShow {
            background: #fff;
            display: none;
        }
        .rankingAllList:last-child {
            border-bottom: none;
        }
        .ranknumber p {
            width:100%;
            text-align: center;
            line-height: 25px;
            color: #a6a6a6;
        }
    </style>
</head>
<body>
<header class="header">
    <h1>沉睡成员详情</h1>
    <a href="${ctx}/u" class="button-left"><i class="fa fa-angle-left"></i></a>
</header>

<article>
    <div class="teamTop">
        <img src="${ctx}/seatch.png" />
        <input type="search" class="searchInput" placeholder="请输入姓名或手机号" />
        <img src="${ctx}/searchBtn.png" class="seatchImg" onclick="seatch()" />
        <div class="searchBtn" onclick="seatch()">搜索</div>
    </div>
    <div class="numberList">
        <div class="all" change="false" onclick="showList(this)">
            <div class="rankingAll">
                <div class="ranking">
                    <div class="sanjiao">
                        <div class="jiaoOne"></div>
                    </div>
                    <span class="rankingTop">特级服务商</span>
                    <span class="rankingSpan must">特级</span>
                </div>
                <span class="tel telDetil">136人</span>
            </div>
        </div>
        <div class="all allLast">
            <div class="rankingAllList">
                <div class="rankingAll">
                    <span>1</span>
                    <img src="${ctx}/headPortrait.png" />
                    <div class="ranking">
                        <span>赵春华</span>
                    </div>
                    <a href="tel:13656174839" class="tel"><img src="${ctx}/tel.png" style="width: 15px;height: 15px;padding-right: 5px">13656174839</a>
                </div>
            </div>
            <div class="rankingAllList">
                <div class="rankingAll">
                    <span>2</span>
                    <img src="${ctx}/headPortrait.png" />
                    <div class="ranking">
                        <span>赵春华</span>
                    </div>
                    <a href="tel:13656174839" class="tel"><img src="${ctx}/tel.png" style="width: 15px;height: 15px;padding-right: 5px">13656174839</a>
                </div>

            </div>
            <div class="rankingAllList">
                <div class="rankingAll">
                    <span>3</span>
                    <img src="${ctx}/headPortrait.png" />
                    <div class="ranking">
                        <span>赵春华</span>
                    </div>
                    <a href="tel:13656174839" class="tel"><img src="${ctx}/tel.png" style="width: 15px;height: 15px;padding-right: 5px">13656174839</a>
                </div>
            </div>
        </div>

        <div class="all" change="false" onclick="showList(this)">
            <div class="rankingAll">
                <div class="ranking">
                    <div class="sanjiao">
                        <div class="jiaoOne"></div>
                    </div>
                    <span class="rankingTop">省级服务商</span>
                    <span class="rankingSpan province">省级</span>
                </div>
                <span class="tel telDetil">136人</span>
            </div>
        </div>
        <div class="all allLast">
            <div class="rankingAllList">

            </div>
            <div class="rankingAllList">

            </div>
            <div class="rankingAllList">

            </div>

        </div>
        <div class="all" change="false" onclick="showList(this)">
            <div class="rankingAll">
                <div class="ranking">
                    <div class="sanjiao">
                        <div class="jiaoOne"></div>
                    </div>
                    <span class="rankingTop">市级服务商</span>
                    <span class="rankingSpan city">市级</span>
                </div>
                <span class="tel telDetil">136人</span>
            </div>
        </div>

        <div class="all allLast">
            <div class="rankingAllList">

            </div>
            <div class="rankingAllList">

            </div>
            <div class="rankingAllList">

            </div>

        </div>
        <div class="all" change="false" onclick="showList(this)">
            <div class="rankingAll">
                <div class="ranking">
                    <div class="sanjiao">
                        <div class="jiaoOne"></div>
                    </div>
                    <span class="rankingTop">VIP服务商</span>
                    <span class="rankingSpan must">VIP</span>
                </div>
                <span class="tel telDetil">136人</span>
            </div>
        </div>
        <div class="all allLast">


        </div>
        <div class="rankingAllList">


        </div>
        <div class="rankingAllList">


        </div>
    </div>
    </div>
    <div class="searchList">查无此人!</div>
    <div class="searchListShow">
        <div class="rankingAllList">
            <div class="rankingAll">
                <span>1</span>
                <img src="${ctx}/headPortrait.png" />
                <div class="ranking">
                    <span>赵春华</span>
                    <span class="rankingSpan city">市级</span>
                </div>
                <a href="tel:13656174839" class="tel"><img src="${ctx}/tel.png" style="width: 15px;height: 15px;padding-right: 5px">13656174839</a>
            </div>
        </div>
        <div class="rankingAllList">
            <div class="rankingAll">
                <span>2</span>
                <img src="${ctx}/headPortrait.png" />
                <div class="ranking">
                    <span>赵春华</span>
                    <span class="rankingSpan city">市级</span>
                </div>
                <a href="tel:13656174839" class="tel"><img src="${ctx}/tel.png" style="width: 15px;height: 15px;padding-right: 5px">13656174839</a>
            </div>
        </div>
        <div class="rankingAllList">
            <div class="rankingAll">
                <span>3</span>
                <img src="${ctx}/headPortrait.png" />
                <div class="ranking">
                    <span>赵春华</span>
                    <span class="rankingSpan city">市级</span>
                </div>
                <a href="tel:13656174839" class="tel"><img src="${ctx}/tel.png" style="width: 15px;height: 15px;padding-right: 5px">13656174839</a>
            </div>
        </div>
    </div>

    </div>
</article>
<script>
    //点击特级省级列表
    function showList(obj){
        if($(obj).attr("change")=="false"){
            $(obj).next(".allLast").show();
            $(obj).attr("change","true");
            $(obj).find(".jiaoOne").addClass("jiaoTwo");
        }else {
            $(obj).find(".jiaoOne").removeClass("jiaoTwo");
            $(obj).attr("change","false");
            $(obj).next(".allLast").hide();
        }
    }

    //点击搜索
    function seatch() {
        if($(".searchInput").val()==""){
            $(".numberList").show();
            $(".searchListShow").hide();
        }else {
            $(".numberList").hide();
            $(".searchListShow").show();
        }

    }
</script>
<%@ include file="/WEB-INF/view/include/footer.jsp"%>
</body>
</html>