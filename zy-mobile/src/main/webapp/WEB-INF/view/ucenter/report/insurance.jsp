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

    <title>保险申请</title>
    <%@ include file="/WEB-INF/view/include/head.jsp"%>
    <%@ include file="/WEB-INF/view/include/validate.jsp"%>
    <%@ include file="/WEB-INF/view/include/imageupload.jsp"%>
    <script src="${stc}/js/layer/layer.js"></script>
    <script src="${stccdn}/js/area.js"></script>
    <script type="text/javascript">
        $(function() {
            //验证
            $('.valid-form').validate({
                ignore: ':hidden',
                rules: {
                    'code': {
                        required: true
                    },
                    'image1': {
                        required: true
                    },
                    'idCardNumber': {
                        required: true,
                        number: true
                    },
                    'birthday': {
                        required: true
                    }
                },
                submitHandler: function (form) {
                    if ($('input[name="image"]').length == 0) {
                        messageFlash('请上传一张图片');
                        return;
                    }
                    form.submit();
                }
            });
        }
    </script>
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
        #preview img{
            max-width:100%;
            max-height:100%;
            position: absolute;
            top: 50%;
            left:50%;
            -webkit-transform:translate(-50%,-50%);
            transform:translate(-50%,-50%);
        }
    </style>
<body>
<header class="header">
    <h1>填写保险申请</h1>
    <a href="#" onclick="hideBtn()" class="button-left">
        <i class="fa fa-angle-left"></i>
    </a>
</header>
<form action="${ctx}/u/report/create" class="valid-form" method="post">
    <div id="policy" class="list-group">
        <div id="policyInfo">
            <div class="list-item bd-t-0"><label class="list-label" for="code">产品编码</label>
                <div class="list-text">
                    <input type="text" name="code" id="code" class="form-input" value="" placeholder="填写产品编码">
                </div>
            </div>
            <div class="list-item"><label class="list-label" for="idCardNumber">身份证号</label>
                <div class="list-text">
                    <input type="text" name="idCardNumber" id="idCardNumber" class="form-input" value="" placeholder="填写身份证号">
                </div>
            </div>
            <div class="list-item"><label class="list-label">正面照</label>
                <div class="list-text image-upload">
                    <div class="image-item image-single "><input type="hidden" name="image1" id="image1" value="">
                        <div id="preview">
                            <img src="${stccdn}/image/upload_240_150.png" class="previewImage" style="width:100px;height:100px;">
                        </div>
                        <input type="file" onchange="fileChange(this)" style="z-index:999;"/>
                    </div>
                </div>
                <div class="list-unit">
                    <a href="javascript:;" class="image-view font-blue fs-14" data-src="${stccdn}/image/example/id_card_1.jpg" data-title="身份证正面">
                        <i class="fa fa-question-circle-o">

                        </i> 示意图
                    </a>
                </div>
            </div>
            <label style="margin-left: 20px; color:red;">(非必填)上传身份证图片可方便核验您的身份证号码的准确性</label>
            <div class="list-item"><label class="list-label">生日</label>
                <div class="list-text">
                    <input type="date" name="birthday" class="form-input" value="" placeholder="填写生日  1900-01-01"></div>
            </div>
        </div>
    </div>
    <div class="form-btn" style="padding-bottom: 50px;">
        <input class="btn orange btn-block round-2" type="submit" value="申 请" style="margin-bottom:10px;" onclick="submitBtn()">
        <div style="height:35px;background:#f2f3f5;text-align:center;line-height:35px;border:1px solid #c9c9c9;" onclick="hideBtn()">取 消
        </div>
    </div>
</form>
<script>
    function hideBtn() {
        parent.hideBtn();
    }
    function submitBtn(){
        parent.submitBtn();
    }
</script>
</body>
</html>
