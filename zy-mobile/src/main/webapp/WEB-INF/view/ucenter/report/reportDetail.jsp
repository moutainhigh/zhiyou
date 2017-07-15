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

<title>检测报告</title>
<%@ include file="/WEB-INF/view/include/head.jsp"%>
<%@ include file="/WEB-INF/view/include/validate.jsp"%>
<script src="${stc}/js/layer/layer.js"></script>
  <script src="${stccdn}/plugin/laytpl-1.1/laytpl.js"></script>
  <link rel="stylesheet" href="${ctx}/css/styleCSS.css" />
<script>
  $(function() {
    function change() {
      if ($(".list-unit").html() == "优检一生") {
        $(".footer").hide();
        $(".footer").css("padding-bottom", "50px");
      }
    }
    change();
    $('.image-view').click(function() {
      var images = $(this).find('img');
      if (images.length == 0) {
        var url = $(this).attr('data-src');
        var title = $(this).attr('data-title');
        $.imageview({
          url : url,
          title : title
        });
      } else {
        var title = $(this).attr('data-title');
        var imageUrls = [];
        $.each(images, function(n, image) {
          imageUrls.push($(image).attr('data-src'));
        })
        $.imageview({
          url : imageUrls,
          title : title
        });
      }
    });
    
  });
  
</script>
</head>
<body>
<div class="reportDetail">
  <header class="header">
    <h1>检测报告</h1>
    <a href="${ctx}/u/report" class="button-left"><i class="fa fa-angle-left"></i></a>
    ${report.preConfirmStatus}
    <c:if test="${report.preConfirmStatus == '待审核'}">
    <a href="${ctx}/u/report/edit?id=${report.id}" class="button-right"><i class="fa fa-edit"></i></a>
    </c:if>
  </header>
  
  <article>
    <c:if test="${report.confirmStatus == '待审核'}">
    <div class="note note-warning mb-0">
      <i class="fa fa-clock-o"></i> 审核信息：待审核
    </div>
    </c:if>
    <c:if test="${report.confirmStatus == '未通过'}">
    <div class="note note-danger mb-0">
      <i class="fa fa-close"></i> 审核信息：未通过, ${report.confirmRemark}
    </div>
    </c:if>
    <c:if test="${report.confirmStatus == '已通过'}">
    <div class="note note-success mb-0">
      <i class="fa fa-check"></i> 审核信息：已通过  | 审核时间: ${report.confirmedTimeLabel}
    </div>
    </c:if>
  
    <%-- 结算信息 --%>
    <c:if test="${report.isSettledUp && not empty profits}">
    
    <div class="list-group mt-10">
      <div class="list-title">我的收益</div>
      <c:forEach items="${profits}" var="profit">
      <div class="list-item">
        <i class="list-icon fa fa-cny"></i>
        <div class="list-text">${profit.profitType}</div>
        <div class="list-unit font-orange">${profit.amount}元</div>
      </div>
      </c:forEach>
      <c:forEach items="${transfers}" var="transfer">
      <div class="list-item">
        <i class="list-icon fa fa-cny"></i>
        <div class="list-text">${transfer.transferType}</div>
        <div class="list-unit font-orange">${transfer.amount}元</div>
      </div>
      </c:forEach>
    </div>
    
    </c:if>
    
    <div class="list-group mt-10">
      <div class="list-title">产品信息</div>
      <div class="list-item">
        <label class="list-text lh-36">产品</label>
        <img class="image-40 mr-10" src="${report.product.image1Thumbnail}">
        <div class="list-unit">${report.product.title}</div>
      </div>
    </div>
  
    <div class="list-group">
      <div class="list-title">客户资料</div>
      <div class="list-item">
        <div class="list-text">客户姓名</div>
        <div class="list-unit">${report.realname}</div>
      </div>
      <div class="list-item">
        <div class="list-text">性别</div>
        <div class="list-unit">${report.gender}</div>
      </div>
     <%-- <div class="list-item">
        <div class="list-text">年龄</div>
        <div class="list-unit">${report.age}岁</div>
      </div>--%>
      <div class="list-item">
        <div class="list-text">手机号</div>
        <div class="list-unit">${report.phone}</div>
      </div>
      <div class="list-item">
        <div class="list-text">检测时间</div>
        <div class="list-unit">${report.reportedDateLabel}</div>
      </div>
      <div class="list-item">
        <div class="list-text">地区</div>
        <div class="list-unit">${report.province} ${report.city} ${report.district}</div>
      </div>
      <div class="list-item">
        <div class="list-text">职业</div>
        <div class="list-unit">${report.jobName}</div>
      </div>
    </div>
    
    <div class="list-group">
      <div class="list-title">检测信息</div>
      <div class="list-item">
        <div class="list-text">检测次数</div>
        <div class="list-unit">第 ${report.times} 次检测</div>
      </div>
      <div class="list-item">
        <div class="list-text">检测结果</div>
        <div class="list-unit">
          <c:choose>
            <c:when test="${report.reportResult == '阴性'}">
            <span class="font-red">${report.reportResult}</span>
            </c:when>
            <c:when test="${report.reportResult == '弱阳性'}">
            <span class="font-orange">${report.reportResult}</span>
            </c:when>
            <c:when test="${report.reportResult == '阳性'}">
            <span class="font-green">${report.reportResult}</span>
            </c:when>
            <c:when test="${report.reportResult == '干扰色'}">
            <span class="font-purple">${report.reportResult}</span>
            </c:when>
          </c:choose>
        </div>
      </div>
      <div class="list-item">
        <div class="list-text list-image image-view" data-title="检测报告图片">
        <c:forEach items="${report.images}" var="image" varStatus="varStatus">
          <img src="${report.imageThumbnails[varStatus.index]}" data-src="${report.imageBigs[varStatus.index]}">
        </c:forEach>
        </div>
      </div>
    </div>
    
    <div class="list-group">
      <div class="list-title">客户使用心得</div>
      <div class="list-item">
        <div class="list-text font-777">${report.text}</div>
      </div>
      <div class="footer" >
        <div onclick="insurance('${report.id}')" style="border-right: none;border-right: 1px solid #fff;box-sizing: border-box;">保险申请</div>
        <div onclick="travel('${report.id}')" style="border-right: none;">旅游申请</div>
      </div>
    </div>
  </article>
</div>

  <!--填写旅游申请代码如下!-->
  <div class="policyInfoMain" style="display: none;">
    <header class="header"><h1>填写旅游申请</h1><a href="#" onclick="hideTravel()" class="button-left"><i class="fa fa-angle-left"></i></a></header>
    <article>
      <form class="valid-form" method="post" action="${ctx}/tour/findTourApple">
        <div id="policy" class="list-group">
          <div id="policyInfo">
            <div class="list-item">
              <label class="list-label" for="phone1">推荐人电话：</label>
              <div class="list-text">
                <input type="number" name="phone1" id="phone1" class="form-input" value="${parentPhone}" placeholder="填写推荐人手机号" ${parentPhone!=null?'readonly':''}>
              </div>
            </div>
            <div class="form-btn" style="padding-bottom: 50px;">
              <input class="btn orange btn-block round-2" type="button" value="确 认" style="margin-bottom:10px;" onclick="submitTravel()">
              <div style="height:35px;background:#f2f3f5;text-align:center;line-height:35px;border:1px solid #c9c9c9;" onclick="hideTravel()">取 消</div>
            </div>
          </div>
        </div>
      </form>
    </article>
  </div>


  <!--选择旅游路线代码如下!-->
  <from class="tourApply" style="display: none;">
    <header class="header"><h1>选择旅游路线</h1><a href="#" onclick="tourApply()"  class="button-left"><i class="fa fa-angle-left"></i></a></header>
    <div id="tourchange">

    </div>
  </from>


<!--旅游路线详情代码如下!-->
<div class="tourDetil" style="display: none;">
  <header class="header">
    <h1>旅游路线详情</h1>
    <a href="#" onclick="fromSumbitTour()" class="button-left"><i class="fa fa-angle-left"></i></a>
  </header>
  <div class="TravelFont">
    ${tour.content}
  </div>
  <div class="clearfloat" style="padding:10px 15px;background:#fff;">
    <label class="list-label" style="height:30px;line-height:25px;font-size:16px;width:50%;float:left;">请选择出游时间：</label>
    <div class="list-text form-select" style="width:50%;float:left;">
      <select name="jobId" onchange="selectValue(this)" id="selectTourTime">
      </select>
    </div>
  </div>
  <div class="TravelDis clearfloat">
  </div>
  <div class="MyApply" onclick="MyApplyFun()">我要报名</div>
</div>
<div class="tourApplyTableNew" style="display: none">

</div>
<script>
  var reportId="";
  //保险申请
  function insurance(id){
    reportId=id;
    $.ajax({
      url : '${ctx}/tour/ajaxCheckReport',
      data :{
        'reportId':reportId
      },
      dataType : 'json',
      type : 'POST',
      success : function(result){
        if(result.code == 0){
          travelT= layer.open({
            type: 2,
            area:['100%', '100%'],
            title: false,
            scrollbar: false,
            closeBtn: 0,
            content: '${ctx}/u/report/insuranceInfo?reportId='+ reportId + '',
            skin: 'layer-class'
          });
        } else {
          messageAlert(result.message);
        }
      }
    });
  }
  //旅游申请
  function travel(id) {
    reportId=id;
    $.ajax({
      url : '${ctx}/tour/ajaxCheckTour',
      data :{
        'reportId':reportId
      },
      dataType : 'json',
      type : 'POST',
      success : function(result){
        if(result.code == 0){
          $(".reportDetail").hide();
          $(".policyInfoMain").show();
          $.ajax({
            url : '${ctx}/tour/findparentInfo',
            dataType : 'json',
            type : 'POST',
            success : function(result1){
              if(result1.code == 0){
                $("#phone1").val(result1.data);
                if(result1.data!=null){
                  $("#phone1").attr("readonly",true);
                }
              } else{
                messageAlert(result1.message);
              }
            }
          });
        } else {
          messageAlert(result.message);
        }
      }
    });
  }
  // 取消/返回旅游申请
  function hideTravel(){
      $(".reportDetail").show();
      $(".policyInfoMain").hide();
  }
  //旅游申请确定
  function submitTravel(){
     $.ajax({
      url : '${ctx}/tour/findparentInfobyPhone',
      data : {
        phone:$('#phone1').val()
      },
      dataType : 'json',
      type : 'POST',
      success : function(result) {
        if(result.code != 0) {
          layer.msg(result.message);
          return;
        }else{
          $.dialog({
            content : '请确认信息是否正确 <br/>姓名：'+result.data.nickname+'<br/>电话：'+result.data.phone,
            callback : function(index) {
              if (index == 1) {
                $(".miui-dialog").remove();
                $(".policyInfoMain").hide();
                $(".tourApply").show();
                $.ajax({
                  url : '${ctx}/tour/findTourApple',
                  dataType : 'json',
                  type : 'POST',
                  success : function(result1) {
                    if(result1.code != 0) {
                      return;
                    }
                    var pageData= result1.data;
                    if (pageData.length) {
                      for ( var i in pageData) {
                        var row = pageData[i];
                        if (row.userRank!="V0"){
                          buildRow(row);
                        }
                      }
                    }
                  }
                });
              }
            }
          });
        }
      }
    });
  }
  //选择旅游路返回
  function tourApply(){
    $(".policyInfoMain").show();
    $(".tourApply").hide();
  }
  //选择旅游路
  function TravelDetil(num){
    $(".tourDetil").show();
    $(".tourApply").hide();
    $.ajax({
      url : '${ctx}/tour/findTourDetail',
      data :{
        'tourId':num,
        'reporId':reportId
      },
      dataType : 'json',
      type : 'POST',
      success : function(result1) {
        if(result1.code != 0) {
          return;
        }
        var pageData= result1.data.str;
        if (pageData.length) {
          $('#selectTourTime').html("");
          $("#selectTourTime").append('<option value="0">请选择</option>');
          for ( var i in pageData) {
            var row = pageData[i];
            $("#selectTourTime").append("<option value='"+row+"'>"+row+"</option>");
          }
        }
        var content =  result1.data.tour.content;
        $(".TravelFont").html(content);
        tourId= result1.data.tour.id;
      }
    });
  }
  //选择出游时间
  function selectValue(obj) {
    $('.TravelDis').html("");
    $(".TravelDis").show();
    var num=$(obj).val();
    $.ajax({
      url : '${ctx}/tour/ajaxTourTime',
      data : {
        reporId:reportId,
        tourId:tourId,
        tourTime:num
      },
      dataType : 'json',
      type : 'POST',
      success : function(result) {
        if(result.code != 0) {
          return;
        }
        var pageData= result.data;
        if (pageData.length) {
          for ( var i in pageData) {
            var row = pageData[i];
            buildRow2(row);
          }
        }
      }
    });
  }
  function  fromSumbitTour() {
    $(".TravelDis").html("");
    $(".tourDetil").hide();
    $(".tourApply").show();
  }
  var tourTimeid="";
  //点击出游时间
  $(function(){
    $(document).on("click",".TravelDisTime",function(){
      $(this).css("background","#f15b00");
      $(this).find("p").css("color","#fff");
      $(this).find("p.TravelDisTimecolor").css("color","#fff");
      tourTimeid= $(this).find("input[name='tourTimeid']").val();
      $(this).siblings(".TravelDisTime").css("background","#fff");
      $(this).siblings(".TravelDisTime").find("p").css("color","#333");
      $(this).siblings(".TravelDisTime").find("p.TravelDisTimecolor").css("color","#f15b00");
    });
  })
  //旅游详情页面点击报名
  function MyApplyFun(){
    //装载数据
    if(tourTimeid==""){
      messageAlert("请选择出游时间");
      return ;
    }
    $.ajax({
      url : '${ctx}/tour/ajaxCheckPraentNumber',
      data : {
        phone:$("#phone1").val(),
        tourTimeId:tourTimeid
      },
      dataType : 'json',
      type : 'POST',
      success : function(result) {
        if(result.code == 0) {
          $(".TravelDis").html("");
          $(".tourDetil").hide();
          $(".tourApplyTableNew").show();
          $.ajax({
            url : '${ctx}/tour/findTourUserVo',
            data : {
              phone:$("#phone1").val(),
              reporId:reportId,
              tourTimeid:tourTimeid,
              tourId:tourId
            },
            dataType : 'json',
            type : 'POST',
            success : function(result) {
              if(result.code != 0) {
                return;
              }
              var pageData= result.data;
              var rowTpl3 = document.getElementById('rowTpl3').innerHTML;
              laytpl(rowTpl3).render(pageData,function(html) {
                $('.tourApplyTableNew').append(html);
              });
            }
          });
        }else {
          messageAlert(result.message);
        }
      }
    });
  }
    //点击报名申请表单中的返回
    function hideApply(){
      $(".tourDetil").show();
      $(".tourApplyTableNew").html("");
      $(".tourApplyTableNew").hide();
    }

  /***
   * 提交旅游申请
   */
  function applyClick(){
    var flage= $('#tourApplyTable').validate({
      ignore: ':hidden'
    });
    var IDCode=$("#IDCode").val();
    var ID=IDCode.substring(0,2);
    if(flage.form()){
      if(ID==41){
        $.dialog({
          content : '户籍要求：川渝藉不享受四川境内游活动；云南藉不享受云南境内游活动；以身份证登记的年龄和户藉地为准，工作地、居住地、暂住地信息无效。',
          callback : function(index) {
            if (index == 1) {
              $(".miui-dialog").remove();
              $.ajax({
                url : '${ctx}/tour/ajaxCheckParam',
                data : $("#tourApplyTable").serialize(),
                dataType : 'json',
                type : 'POST',
                success : function(result){
                  if(result.code == 0) {
                    if (result.message != null) {
                      $.dialog({
                        content : result.message,
                        callback : function(index) {
                          if (index == 1) {
                            $(".miui-animation-scale,.miui-dialog-shade").hide();
                            $.ajax({
                              url : '${ctx}/tour/addTourforUser',
                              data : $("#tourApplyTable").serialize(),
                              dataType : 'json',
                              type : 'POST',
                              success : function(result){
                                if(result.code == 0){
                                  $.dialog({
                                    content : '您的旅游申请已成功，我们工作人员近期会与您联系，在此之前请勿购买参游机/车票，由此造成的财产损失，公司概不负责。',
                                    callback : function(index) {
                                      $(".miui-dialog").remove();
                                      $(".tourApplyTableNew").hide();
                                      $(".reportDetail").show();
                                    }
                                  });
                                } else {
                                  messageAlert(result.message);
                                }
                              }
                            });
                          }
                        }
                      });

                    }else{
                      $.ajax({
                        url : '${ctx}/tour/addTourforUser',
                        data : $("#tourApplyTable").serialize(),
                        dataType : 'json',
                        type : 'POST',
                        success : function(result){
                          if(result.code == 0){
                            $.dialog({
                              content : '您的旅游申请已成功，我们工作人员近期会与您联系，在此之前请勿购买参游机/车票，由此造成的财产损失，公司概不负责。',
                              callback : function(index) {
                                $(".miui-dialog").remove();
                                $(".tourApplyTableNew").hide();
                                $(".reportDetail").show();
                              }
                            });
                          } else {
                            messageAlert(result.message);
                          }
                        }
                      });
                    }
                  }else {
                    messageAlert(result.message);
                  }
                }
              });
            }
          }
        });
      }
    }
  }
  function buildRow(row){
    var rowTpl = document.getElementById('rowTpl').innerHTML;
    laytpl(rowTpl).render(row,function(html) {
      $('#tourchange').append(html);
    });
  }
  function buildRow2(row){
    var rowTpl2 = document.getElementById('rowTpl2').innerHTML;
    laytpl(rowTpl2).render(row,function(html) {
      $('.TravelDis').append(html);
    });
  }
</script>
<script id="rowTpl" type="text/html">
  <a href="#" onclick="TravelDetil('{{d.id}}')" class="opacityAll" style="width:100%;position:relative;">
    <p class="font">【{{d.title}}】</p>
    <img class="opacityFirst" src="{{d.image}}" style="display:block;width:100%;" />
  </a>
</script>
<script id="rowTpl2" type="text/html">
  <div class="TravelDisTime"><input type="hidden" name="tourTimeid" value="{{d.id}}"> <p>{{d.beginTimeStr}} {{d.weekStr}}</p><p class="TravelDisTimecolor">原价:<del>￥{{d.fee}}</del></p></div>
</script>
<script id="rowTpl3" type="text/html">
  <header class="header">
    <h1>旅游报名申请表单</h1>
    <a href="#" onclick="hideApply()" class="button-left"><i class="fa fa-angle-left"></i></a>
  </header>
  <form class="tourApplyTable" id="tourApplyTable" action="${ctx}/tour/addTourforUser" method="post">
    <input type="hidden" name="tourId" value="{{d.tour.id}}">
    <input type="hidden" name="parentPhone" value="{{d.userp.phone}}">
    <input type="hidden" name="parentId" value="{{d.userp.id}}">
    <input type="hidden" name="reporId" value="{{d.reporId}}">
    <input type="hidden" name ="tourTimeId" value="{{d.tourTime.id}}">
    <input type="hidden" name ="id" value="{{d.userinfoVo.id}}">
    <div class="list-title">旅游报名申请表单</div>
    <div class="list-item">
      <label class="list-label" >产品编号</label>
      <div class="list-text">
        <input type="text" class="form-input" name="productNumber" value="{{d.productNumber}}" {{#if(d.productNumber!='') { }} readonly="readonly" {{# } }}  required placeholder="填写产品编号">
      </div>
    </div>
    <div class="list-item">
      <label class="list-label" >旅游路线</label>
      <div class="list-text">
        <input type="text" class="form-input" value="{{d.tour.title}}" placeholder="填写旅游路线" readonly>
      </div>
    </div>
    <div class="list-item">
      <label class="list-label" >姓名</label>
      <div class="list-text">
        <input type="text" name="realname"  class="form-input" value="{{d.userinfoVo.realname}}" readonly placeholder="填写姓名">
      </div>
    </div>
    <div class="list-item">
      <label class="list-label" >身份证号</label>
      <div class="list-text">
        <input type="text" name="idCartNumber" id="IDCode"  class="form-input" value="{{d.userinfoVo.idCardNumber}}" {{#if(d.userinfoVo.idCardNumber!='') { }} readonly="readonly" {{# } }}  required  placeholder="填写身份证号">
      </div>
    </div>
    <div class="list-item">
      <label class="list-label" >性别</label><div class="list-text">
      <div class="list-text form-select">
        {{#if(d.userinfoVo.gender !=null) { }}
        <input type="text" name="genders"  class="form-input" value="{{d.userinfoVo.gender}}" readonly="readonly">
        {{# }else{ }}
        <select name="genders"  >
          <option value="男"  {{#if(d.userinfoVo.gender=='男') { }} selected="selected" {{# } }}>男</option>
          <option value="女" {{#if(d.userinfoVo.gender=='女') { }} selected="selected" {{# } }}>女</option>
        </select>

        {{# } }}
      </div>
    </div>
    </div>
    <div class="list-item">
      <label class="list-label" >年龄</label><div class="list-text">
      <input type="text" name="ages"  class="form-input" value="{{d.userinfoVo.agestr}}" {{#if(d.userinfoVo.agestr !='') { }} readonly {{# } }}  placeholder="填写年龄" required>
    </div>
    </div>
    <div class="list-item">
      <label class="list-label" >户籍城市</label>
      <div class="list-text">
        {{#if(d.userinfoVo.province!=null) { }}
        <input type="text"  class="form-input" value="{{d.userinfoVo.province}}-{{d.userinfoVo.city}}-{{d.userinfoVo.district}}"  readonly  placeholder="填写户籍城市">
        <input type="hidden"  class="form-input" value="{{d.userinfoVo.areaId}}" name="areaId">
        {{# } }}
      </div>
    </div>
    <div class="list-item">
      <label class="list-label" >手机号码</label><div class="list-text">
      <input type="text" name="phone"  class="form-input" value="{{d.user.phone}}" readonly required placeholder="填写手机号码">
    </div>
    </div>
    <div class="list-item">
      <label class="list-label">推荐人姓名</label><div class="list-text">
      <input type="text"  class="form-input" value="{{d.userp.nickname}}" readonly placeholder="填写推荐人姓名">
    </div>
    </div>
    <div class="list-item">
      <label class="list-label">推荐人手机号</label><div class="list-text">
      <input type="text" class="form-input" value="{{d.userp.phone}}" readonly  placeholder="填写推荐人手机号">
    </div>
    </div>
    <div class="list-item">
      <label class="list-label" >出游日期</label><div class="list-text">
      <input type="text" readonly class="form-input time-input"  value="{{d.timedate}}"  placeholder="填写出游日期">
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
    <input type="button" class="MyApply" onclick="applyClick()" value="申请">
  </form>
</script>
</body>
</html>
