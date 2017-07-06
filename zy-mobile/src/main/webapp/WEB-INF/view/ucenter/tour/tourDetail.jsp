<!DOCTYPE html>
<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/view/include/taglib.jsp"%>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="Cache-Control" content="no-store" />
    <meta http-equiv="Pragma" content="no-cache" />
    <meta http-equiv="Expires" content="0" />
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-mobile-web-app-status-bar-style" content="black">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">

    <title>旅游路线详情</title>
    <%@ include file="/WEB-INF/view/include/head.jsp"%>
    <%@ include file="/WEB-INF/view/include/validate.jsp"%>
    <%@ include file="/WEB-INF/view/include/imageupload.jsp"%>
    <script src="${stc}/js/layer/layer.js"></script>
    <script src="${stccdn}/js/area.js"></script>
    <style>
        .footer {
            position: fixed;
            bottom:0;
            left:0;
            width:100%;
            height:50px;
            background: #6cb92d;
            z-index: 99;
        }
        .footer div {
            float: left;
            width:50%;
            height:50px;
            text-align: center;
            line-height: 50px;
            color: #fff;
            font-size: 18px;
            border-right: 1px solid #fff;
            box-sizing: border-box;
        }
        /*.layer-anim {
          width:100% !important;
          left:0 !important;
          top:0 !important;
        }*/
        .sumbitBtn {
            width:90%;
            margin-left: 5%;
            margin-right: 5%;
        }
        .list-group {
            margin-bottom: 0;
        }
        .font {
            font-size: 20px;
            color: #fff;
            width: 100%;
            float: left;
            position: relative;
            z-index: 99;
            left: 50%;
            margin-left: -20px;
        }
        .MyApply {
            width:90%;
            margin-left: 5%;
            margin-right: 5%;
            height:40px;
            background:#f18200;
            color: #fff;
            margin-top: 30px;
            margin-bottom: 10px;
            text-align: center;
            line-height: 40px;
            font-size: 18px;
            -webkit-border-radius:10px;
            -moz-border-radius:10px;
            border-radius:10px;
            box-sizing: border-box;
        }
        .list-item .list-label {
            width:150px;
        }
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
        .TravelFont {
            width:100%;
            background: #f0f0f0;
            -webkit-border-radius:20px 20px 0 0;
            -moz-border-radius:20px 20px 0 0;
            border-radius:20px 20px 0 0;
            margin-top: -60px;
            position: relative;
            z-index:9;
            padding:20px 0 20px 0;
        }
        .TravelFOne {
            width:100%;
            height:30px;
            font-size: 18px;
            color: #f13e00;
            text-align: center;
            line-height: 30px;
        }
        .TravelFOneD {
            padding-left:30px;
            padding-right: 30px;
            color: #f13e00;
            line-height: 30px;
        }
        .TravelDis {
            width:100%;
            padding:0px 20px 10px 20px;
            background: #fff;
            display: none;
        }
        .TravelDisTime {
            float: left;
            border: 1px solid #eee;
            -webkit-border-radius:5px;
            -moz-border-radius:5px;
            border-radius:5px;
            margin-left: 15px;
            padding:5px;
            margin-top: 10px;
        }
        .changeAll {
            width:100%;height:50px;background:#fff;border-bottom: 1px solid #eee;
        }
        .changeSelect,.changeFont {
            float: left;
            width:50%;
            height:50px;
            border:none;
            text-align: center;
            line-height: 50px;
        }
        .changeSelect {
            height:48px;
        }
        .TravelDisTimecolor {
            color: #f15b00;
        }
    </style>
</head>
<header class="header">
    <h1>旅游路线详情</h1>
    <a href="#" onclick="hideDetil()" class="button-left"><i class="fa fa-angle-left"></i></a>
</header>
<img src="${ctx}/images/TravelTop.png" style="width:100%;" />
<div class="TravelFont">
    <div class="TravelFOne">预定须知</div>
    <div class="TravelFOneD">旅游信息有几个限制条件：1、户籍所在地的限制，目前对四川、重庆不参与四川境内游活动，云南籍不参与云南境内游活动；2、年龄限制，年龄在28-58岁旅游费用全免，27岁以下与59岁至65岁者另加960，儿童另加960元且门票住宿自理。66岁以上老人及残疾人也不享受本活动。</div>
    <div class="TravelFOne" style="margin-top:10px;">产品特色</div>
    <div class="TravelFOneD">旅游信息有几个限制条件：1、户籍所在地的限制，目前对四川、重庆不参与四川境内游活动，云南籍不参与云南境内游活动；2、年龄限制，年龄在28-58岁旅游费用全免。</div>
</div>
<img src="${ctx}/images/TravelDetilT.png" style="width:100%;"/>'
<div class="clearfloat" style="padding:10px 15px;background:#fff;">
    <label class="list-label" style="height:30px;line-height:25px;font-size:16px;width:50%;float:left;">请选择出游时间：</label>
    <div class="list-text form-select" style="width:50%;float:left;">
        <select name="jobId" onchange="selectValue(this)"><option value="0">请选择</option><option value="1">一月</option><option value="2">二月</option></select>
    </div>
</div>
<div class="TravelDis clearfloat">
    <div class="TravelDisTime">
        <p>07-06 周四</p>
        <p class="TravelDisTimecolor">原价:<del>￥1000</del></p>
    </div><div class="TravelDisTime"><p>07-06 周四</p>
    <p class="TravelDisTimecolor">原价:<del>￥1000</del></p>
</div>
    <div class="TravelDisTime"><p>07-06 周四</p><p class="TravelDisTimecolor">原价:<del>￥1000</del></p></div>
    <div class="TravelDisTime"><p>07-06 周四</p><p class="TravelDisTimecolor">原价:<del>￥1000</del></p></div>
    <div class="TravelDisTime"><p>07-06 周四</p><p class="TravelDisTimecolor">原价:<del>￥1000</del></p></div>
</div>
<div class="MyApply" onclick="MyApplyFun()">我要报名</div>
<script>
    //返回上一页
    function hideDetil(){

    }
    //我要报名
    function MyApplyFun(){

    }
    $(function(){
        $(document).on("click",".TravelDisTime",function(){
            $(this).css("background","#f15b00");
            $(this).find("p").css("color","#fff");
            $(this).find("p.TravelDisTimecolor").css("color","#fff");

            $(this).siblings(".TravelDisTime").css("background","#fff");
            $(this).siblings(".TravelDisTime").find("p").css("color","#333");
            $(this).siblings(".TravelDisTime").find("p.TravelDisTimecolor").css("color","#f15b00");
        });
    })
    //选择出游时间
    function selectValue(obj) {
        var num=$(obj).val();
        if(num==0){
            $(".TravelDis").hide();
        }else {
            $(".TravelDis").show();
            if(num==1){

            }else if(num==2){

            }

        }
    }
</script>
</body>
</html>
