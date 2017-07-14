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

    <title>填写旅游申请</title>
    <%@ include file="/WEB-INF/view/include/head.jsp"%>
    <%@ include file="/WEB-INF/view/include/validate.jsp"%>
    <script src="${stc}/js/layer/layer.js"></script>
    <script type="text/javascript">
        function hideTravel(){
            parent.hideTravel();
            /*parent.layer.closeAll();
            parent.parent.layer.closeAll();*/
        }
        function submitTravel(){
            var formvalidate = $('.valid-form').validate({
                rules : {
                    'phone' : {
                        required : true,
                        phone : true
                    }
                }
            });
            if(formvalidate.form()){
                $.ajax({
                    url : '${ctx}/tour/findparentInfobyPhone',
                    data : {
                        phone:$('#phone').val()
                    },
                    dataType : 'json',
                    type : 'POST',
                    success : function(result) {
                        if(result.code != 0) {
                            layer.msg(result.message);
                            return;
                        }else{
                            layer.confirm('请确认信息是否正确 <br/>姓名：'+result.data.nickname+'<br/>电话：'+result.data.phone, {
                                btn: ['正确','错误'] //按钮
                            }, function(){
                                $('.valid-form').submit();
                            }, function(){
                            });
                        }
                    }
                });
                return;
            }
        }
    </script>
</head>
<body>
<header class="header"><h1>填写旅游申请</h1><a href="#" onclick="hideTravel()" class="button-left"><i class="fa fa-angle-left"></i></a></header>
<article>
    <form class="valid-form" method="post" action="${ctx}/tour/findTourApple">
    <div id="policy" class="list-group">
        <div id="policyInfo">
            <div class="list-item">
                <label class="list-label" for="phone">推荐人电话：</label>
                <div class="list-text">
                    <input type="number" name="phone" id="phone" class="form-input" value="${parentPhone}" placeholder="填写推荐人手机号" ${parentPhone!=null?'readonly':''}>
                </div>
            </div>
         <%--<div class="list-text" style="margin-left: 5%;margin-bottom: 20px;margin-top: 20px;">
             <span>填写推荐人手机号：</span><input type="number" style="height: 30px;width:50%;" class="form-input" value="${parentPhone}" ${parentPhone!=null?'readonly':''} id="phone" name="phone" placeholder="填写客户手机号"/>
         </div>--%>
        <div class="form-btn" style="padding-bottom: 50px;">
            <input class="btn orange btn-block round-2" type="button" value="确 认" style="margin-bottom:10px;" onclick="submitTravel()">
            <div style="height:35px;background:#f2f3f5;text-align:center;line-height:35px;border:1px solid #c9c9c9;" onclick="hideTravel()">取 消</div>
        </div>
     </div>
    </div>
    </form>
    </article>
</body>
</html>
