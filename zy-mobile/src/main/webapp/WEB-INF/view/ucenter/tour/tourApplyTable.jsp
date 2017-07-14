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

    <title>旅游报名申请表单</title>
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
        <form id="forma" method="post">
            <input type="hidden" name="tourId" value="${tour.id}">
            <input type="hidden" name="parentPhone" value="${userp.phone}">
            <input type="hidden" name="parentId" value="${userp.id}">
            <input type="hidden" name="reporId" value="${reporId}">
            <input type="hidden" name ="tourTimeId" value="${tourTime.id}">
            <input type="hidden" name ="userId" value="${userinfoVo.userId}">
         <a href="#" onclick="hideApply()" class="button-left"><i class="fa fa-angle-left"></i></a>
        </form>
    </header>
    <form class="list-group " id="_form"  method="post">
        <input type="hidden" name="tourId" value="${tour.id}">
        <input type="hidden" name="parentPhone" value="${userp.phone}">
        <input type="hidden" name="parentId" value="${userp.id}">
        <input type="hidden" name="reporId" value="${reporId}">
        <input type="hidden" name ="tourTimeId" value="${tourTime.id}">
        <input type="hidden" name ="userId" value="${userinfoVo.userId}">
        <div class="list-title">旅游报名申请表单</div>
    <div class="list-item">
        <label class="list-label" >产品编号</label>
        <div class="list-text">
            <input type="text" class="form-input" name="productNumber" value="${productNumber}" ${productNumber!=null?'readonly':''}  required placeholder="填写产品编号">
        </div>
    </div>
    <div class="list-item">
        <label class="list-label" >旅游路线</label>
        <div class="list-text">
            <input type="text" class="form-input" value="${tour.title}" placeholder="填写旅游路线" readonly>
        </div>
    </div>
    <div class="list-item">
        <label class="list-label" >姓名</label>
        <div class="list-text">
            <input type="text" name="realname"  class="form-input" value="${userinfoVo.realname}" ${userinfoVo.realname==null?'':'readonly'} required placeholder="填写姓名">
     </div>
    </div>
    <div class="list-item">
        <label class="list-label" >身份证号</label>
        <div class="list-text">
            <input type="text" name="idCartNumber"  class="form-input" value="${userinfoVo.idCardNumber}" ${userinfoVo.idCardNumber==null?'':'readonly'} required  placeholder="填写身份证号">
        </div>
    </div>
    <div class="list-item">
        <label class="list-label" >性别</label><div class="list-text">
        <div class="list-text form-select">
            <select name="gender" ${userinfoVo.gender !=null?'disabled':''} >
                <option value="男"<c:if test="${userinfoVo.gender eq '男'}"> selected="selected"</c:if>>男</option>
                <option value="女"<c:if test="${userinfoVo.gender eq '女'}"> selected="selected"</c:if>>女</option>
            </select>
        </div>
    </div>
    </div>
    <div class="list-item">
        <label class="list-label" >年龄</label><div class="list-text">
        <input type="text" name="age" id ="age" class="form-input" value="${userinfoVo.age}" ${userinfoVo.age==null?'':'readonly'} placeholder="填写年龄" required=true>
    </div>
    </div>
    <div class="list-item">
        <label class="list-label" >户籍城市</label>
        <div class="list-text">
           <c:choose>
               <c:when test="${userinfoVo.province!=null}">
                   <input type="text"  class="form-input" value="${userinfoVo.province}-${userinfoVo.city}-${userinfoVo.district}"  readonly  placeholder="填写户籍城市">
               </c:when>
               <c:otherwise>
                   <div class="flex">
                       <div class="form-select flex-1">
                           <select name="province" id="province" required>
                               <option value="">省</option>
                           </select>
                       </div>
                       <div class="form-select flex-1">
                           <select name="city" id="city" required>
                               <option value="">市</option>
                           </select>
                       </div>
                       <div class="form-select flex-1">
                           <select name="areaId" id="district" required>
                               <option value="">区</option>
                           </select>
                       </div>
                   </div>
               </c:otherwise>
           </c:choose>
        </div>
    </div>
    <div class="list-item">
        <label class="list-label" >手机号码</label><div class="list-text">
        <input type="text" name="phone"  class="form-input" value="${user.phone}" ${user.phone==null?'':'readonly'} required placeholder="填写手机号码">
    </div>
    </div>
    <div class="list-item">
        <label class="list-label">推荐人姓名</label><div class="list-text">
        <input type="text"  class="form-input" value="${userp.nickname}" readonly placeholder="填写推荐人姓名">
    </div>
    </div>
    <div class="list-item">
        <label class="list-label">推荐人手机号</label><div class="list-text">
        <input type="text" class="form-input" value="${userp.phone}" readonly  placeholder="填写推荐人手机号">
    </div>
    </div>
    <div class="list-item">
        <label class="list-label" >出游日期</label><div class="list-text">
        <input type="text" readonly class="form-input" value="<fmt:formatDate value="${tourTime.begintime}"  pattern="yyyy-MM-dd"/>" placeholder="填写出游日期">
    </div>
    </div>
     </div>
        <div class="list-item">
        <label class="list-label">房型需求</label>
         <div class="list-text form-select">
            <select name="houseType">
             <option value="1">标准间 </option>
             <option value="2">三人间 </option>
         </select>
         </div>
      </div>
    <div class="list-item">
        <label class="list-label" >特殊需求</label>
        <div class="list-text">
            <input type="text" name="userRemark"class="form-input" value="" placeholder="填写特殊需求" >
        </div>
    </div>
    </form>
    <div class="MyApply" onclick="applyClick()">申请</div></div>
    </body>

<script>
    $(function() {
        var area = new areaInit('province', 'city', 'district');

    });
    //点击报名申请表单中的返回
    function hideApply(){
        $("#forma").attr("action", "${ctx}/tour/findTourDetail");
        $("#forma").submit();

    }
    //点击申请按钮
    function applyClick(){
       var fromflage= $('#_form').validate({
            ignore: ':hidden',

            submitHandler : function() {
                var productId = $('#province');
                if(productId=='undefined') {
                    messageAlert('请选择产品');
                    return;
                }else {
                }

            }
        });
        if(fromflage.form()){
            $.ajax({
                url : '${ctx}/tour/ajaxCheckParam',
                data : $("#_form").serialize(),
                dataType : 'json',
                type : 'POST',
                success : function(result){
                    if(result.code == 0){
                        $.ajax({
                            url : '${ctx}/tour/addTourforUser',
                            data : $("#_form").serialize(),
                            dataType : 'json',
                            type : 'POST',
                            success : function(result){
                                if(result.code == 0){
                                    layer.msg('申请成功', {
                                        icon: 1,
                                        time: 1000 //2秒关闭（如果不配置，默认是3秒）
                                    }, function(){
                                        parent.parent.layer.closeAll('iframe');
                                        parent.layer.closeAll('iframe');
                                    });
                                } else {
                                    messageAlert(result.message);
                                }
                            }
                        });

                    } else {
                        messageAlert(result.message);
                    }
                }
            });

        }
    }
</script>
</html>