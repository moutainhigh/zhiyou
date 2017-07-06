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

    <title>上传检测报告</title>
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
    <body>
    <header class="header">
        <h1>旅游报名申请表单</h1>
        <a href="#" onclick="hideApply()" class="button-left"><i class="fa fa-angle-left"></i></a>
    </header>
    <form class="list-group mt-10">
        <div class="list-title">旅游报名申请表单</div>
    <div class="list-item">
        <label class="list-label" for="realname">旅游路线</label>
        <div class="list-text">
            <input type="text" name="realname" id="realname1" class="form-input" value="${report.realname}" placeholder="填写旅游路线"></div></div>
    <div class="list-item"><label class="list-label" for="realname">姓名</label><div class="list-text"><input type="text" name="realname" id="realname" class="form-input" value="${report.realname}" placeholder="填写姓名">
    </div>
    </div>
    <div class="list-item">
        <label class="list-label" for="realname">身份证号</label><div class="list-text"><input type="text" name="realname" id="realname" class="form-input" value="${report.realname}" placeholder="填写身份证号"></div></div>
    <div class="list-item">
        <label class="list-label" for="realname">性别</label><div class="list-text"><input type="text" name="realname" id="realname" class="form-input" value="${report.realname}" placeholder="填写性别"></div></div>
    <div class="list-item">
        <label class="list-label" for="realname">年龄</label><div class="list-text"><input type="text" name="realname" id="realname" class="form-input" value="${report.realname}" placeholder="填写年龄"></div></div>
    <div class="list-item">
        <label class="list-label" for="realname">户籍城市</label><div class="list-text"><input type="text" name="realname" id="realname" class="form-input" value="${report.realname}" placeholder="填写户籍城市"></div></div>
    <div class="list-item">
        <label class="list-label" for="realname">手机号码</label><div class="list-text"><input type="text" name="realname" id="realname" class="form-input" value="${report.realname}" placeholder="填写手机号码"></div></div>
    <div class="list-item">
        <label class="list-label" for="realname">推荐人姓名</label><div class="list-text"><input type="text" name="realname" id="realname" class="form-input" value="${report.realname}" placeholder="填写推荐人姓名"></div></div>
    <div class="list-item">
        <label class="list-label" for="realname">推荐人手机号</label><div class="list-text"><input type="text" name="realname" id="realname" class="form-input" value="${report.realname}" placeholder="填写推荐人手机号"></div></div>
    <div class="list-item">
        <label class="list-label" for="realname">出游日期</label><div class="list-text"><input type="text" name="realname" id="realname" class="form-input" value="${report.realname}" placeholder="填写出游日期"></div></div>
    <div class="list-item">
        <label class="list-label" for="realname">房型需求</label><div class="list-text"><input type="text" name="realname" id="realname" class="form-input" value="${report.realname}" placeholder="填写房型需求"></div></div>
    <div class="list-item">
        <label class="list-label" for="realname">特殊需求</label><div class="list-text"><input type="text" name="realname" id="realname" class="form-input" value="${report.realname}" placeholder="填写特殊需求"></div></div>
    </form>
    <div class="MyApply" onclick="applyClick()">申请</div></div>
    </body>
<script>
    //点击报名申请表单中的返回
    function hideApply(){

    }
    //点击申请按钮
    function applyClick(){

    }
</script>
</body>
</html>
