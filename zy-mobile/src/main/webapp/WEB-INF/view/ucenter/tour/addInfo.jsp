<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/view/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="Cache-Control" content="no-store" />
    <meta http-equiv="Pragma" content="no-cache" />
    <meta http-equiv="Expires" content="0" />
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-mobile-web-app-status-bar-style" content="black">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">

    <title>待补充旅游路线</title>
    <%@ include file="/WEB-INF/view/include/head.jsp"%>
    <%@ include file="/WEB-INF/view/include/validate.jsp"%>
    <%@ include file="/WEB-INF/view/include/imageupload.jsp"%>
    <script src="${stccdn}/js/area.js"></script>
    <script type="text/javascript">
        $(function() {
            //验证
            $('.valid-form').validate({
                rules : {
                    'planTime' : {
                        required : true
                    },
                    'carNumber' : {
                        required : true
                    },
                    'carImages' : {
                        required : true
                    }
                },
                submitHandler : function(form) {
                    $(form).find(':submit').prop('disabled', true);
                    form.submit();
                }
            });
        });


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
        .layer-anim {
            width:100% !important;
            left:0 !important;
            top:0 !important;
        }
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
        }
        .list-item .list-label {
            width:150px;
        }
        .list-item .list-label2 {
            width:200px;
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

</head>
<div class="main">
    <header class="header">
        <h1>待补充旅游路线</h1>
        <a href="${ctx}/tour" class="button-left"><i class="fa fa-angle-left"></i></a>
    </header>

    <article>
        <form action="${ctx}/tour/create?tourUserId=${tourUserId}" class="valid-form" method="post">
            <div class="list-title">客户资料</div>
            <div class="list-item">
                <label class="list-label">预计到达日期</label>
                <div class="list-text">
                    <input type="date" name ="planTime" class="form-input" value="" placeholder="填写预计到达日期">
                </div>
            </div>
            <div class="list-item">
                <label class="list-label">航班/班次号(去)</label>
                <div class="list-text">
                    <input type="text" name ="carNumber" class="form-input" value="${report.realname}" placeholder="填写航班班次">
                </div>
            </div>
            <div class="list-item">
                <label class="list-label list-label2">添加票务照片（机/车票照片）</label>
            </div>
            <div class="list-item">
                <div class="list-text image-upload image-multi">
                    <div class="image-add" data-limit="6" data-name="image">
                        <input type="file"  name ="carImages" class="file" accept="image/*" capture="camera" onchange="selectFileImage(this)">
                        <em class="state state-add" id="preview">
                        </em>

                    </div>
                </div>
            </div>

            <div class="form-btn" style="padding-bottom: 50px;">
                <input id="btnSubmit" class="btn orange btn-block round-2" type="submit" value="提 交" style="margin-top: 30px">
            </div>
        </form>
    </article>
</div>
<script>

    $(function() {
        $('[type=file]').change(function(e) {
            var file = e.target.files[0]
            preview(file);
        })
    });
    function preview(file) {
        var img = new Image(), url = img.src = URL.createObjectURL(file)
        var $img = $(img)
        img.onload = function() {
            URL.revokeObjectURL(url)
            $('#preview').empty().append($img);
            $('#preview').removeClass("state-add");
        }
    }
</script>
</body>
</html>
