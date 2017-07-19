<!DOCTYPE html>
<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/view/include/taglib.jsp"%>
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
  <script src="${stccdn}/plugin/laytpl-1.1/laytpl.js"></script>
  <link rel="stylesheet" href="${ctx}/css/styleCSS.css" />
  <script type="text/javascript">
    $(function() {
      $(".TravelFont p img").parents("p").css("padding","0 !important");
      var area = new areaInit('province', 'city', 'district', '${report.areaId}');
//      var area1 = new areaInit('province1', 'city1', 'district1');
      //选择产品
      showProductList();
      $('.image-multi .image-add').imageupload({
        isMultipart : true
      });

      //验证
      $('.valid-form').validate({
        ignore: ':hidden',
        rules : {
          'realname' : {
            required : true
          },
          'gender' : {
            required : true
          },
          'age' : {
            required : true
          },
          'phone' : {
            required : true,
            phone : true
          },
          'times' : {
            required : true,
            digits : true,
            max: 999
          },
          'reportedDate' : {
            required : true
          },
          'areaId' : {
            required : true
          },
          'jobId' : {
            required : true
          },
          'reportResult' : {
            required : true
          },
          'text' : {
            required : true
          },
          'code' : {
            required : true
          },
          'image1' : {
            required : true
          },
          'idCardNumber' : {
            required : true
          },
          'birthday' : {
            required : true
          }
        },
        submitHandler : function(form) {
          if($('input[name="image"]').length < 1) {
            messageFlash('请至少上传一张图片');
            return;
          }
          var productId = $('#productId').val();
          if(!productId) {
            messageAlert('请选择产品');
            return;
          }
          form.submit();
        }
      });

      //选择产品
      $('.product-info').click(function(){
        showProductList();

//        if($(".list-unit span").html()=="优检一生"){
//          alert($(".list-unit span").html());
//          $(".footer").show();
//        }else {
//          alert($(".list-unit span").html());
//          $(".footer").hide();
//        }
      });

      $('body').on('click', '.product', function() {
        var $this = $(this);
        var product = {};
        product.id = $this.attr('data-id');
        product.title = $this.attr('data-title');
        product.image = $this.attr('data-image');
        setProduct(product);
        if(product.id == 2) {
          //当用户选择的产品是2.0时，显示旅游和保险
          $(".footer").show();
          $(".form-btn").css("padding-bottom","50px");
          $('#policy').show();
        } else {
          //当用户选择的产品是1.0时，不显示旅游和保险
          $(".footer").hide();
          $(".form-btn").css("padding-bottom","0");
          $('#policy').hide();
          $('#hasPolicy').removeAttr('checked');
        }
      });

      //是否选择保单
      $('#hasPolicy').click(function() {
        var checked = $(this).is(':checked');
        //messageFlash(checked);
        if(checked) {
          $('#policyInfo').slideDown(300);
        } else {
          $('#policyInfo').slideUp(300);
        }
      });

      $('.image-single').imageupload({
        width : 120,
        height : 75,
      });

      $('.image-view').click(function() {
        var url = $(this).attr('data-src');
        var title = $(this).attr('data-title');
        $.imageview({
          url : url,
          title : title
        });
      });

    });

    function showProductList() {
      var html = document.getElementById('productListTpl').innerHTML;
      pushDialog(html);
    }

    function hideProductList() {
      pullDialog('#productList', function(){
        $('#productList').remove();
      });
    }

    function setProduct(product) {
      hideProductList();
      $('#productId').val(product.id);
      $('.product-info .list-unit').html('<span>' + product.title + '</span>');
      $('.product-info .list-icon').removeClass('list-icon').html('<img class="image-40" src="' + product.image + '">');
    }

  </script>
  <script id="productListTpl" type="text/html">
    <aside id="productList" class="abs-lt size-100p bg-gray zindex-1000">
      <header class="header">
        <h1>选择商品</h1>
        <a href="javascript:hideProductList();" class="button-left"><i class="fa fa-angle-left"></i></a>
      </header>
      <div class="list-group">
        <c:forEach items="${products}" var="product">
          <div class="list-item product" data-id="${product.id}" data-title="${product.title}" data-image="${product.image1Thumbnail}">
            <img class="image-60 mr-10" src="${product.image1Thumbnail}">
            <div class="list-text">${product.title}</div>
          </div>
        </c:forEach>
      </div>
    </aside>
  </script>
</head>
<div class="main">
  <header class="header">
    <h1>上传检测报告</h1>
    <a href="${ctx}/u/report" class="button-left"><i class="fa fa-angle-left"></i></a>
  </header>
  <article>
    <form action="${ctx}/u/report/create" class="valid-form" method="post">
      <div class="list-group mt-10">
        <div class="list-title">请选择产品</div>
        <div class="list-item product-info">
          <label class="list-text lh-36">选择产品</label>
          <div class="list-icon"></div>
          <div class="list-unit"></div>
          <i class="list-arrow"></i>
          <input id="productId" type="hidden" name="productId" value="">
        </div>
      </div>

      <div class="list-group mt-10">
        <div class="list-title">填写客户资料</div>
        <div class="list-item">
          <label class="list-label" for="realname">产品编码</label>
          <div class="list-text">
            <input type="text" class="form-input" name ="productNumber" id="productNumber" value="" placeholder="填写产品编码">
          </div>
        </div>
        <div class="list-item">
          <label class="list-label" for="realname">姓名</label>
          <div class="list-text">
            <input type="text" name="realname" id="realname" class="form-input" value="${report.realname}" placeholder="填写客户姓名">
          </div>
        </div>
        <div class="list-item">
          <label class="list-label">性别</label>
          <div class="list-text form-select">
            <select name="gender">
              <option value="">请选择</option>
              <option value="男"<c:if test="${report.gender eq '男'}"> selected="selected"</c:if>>男</option>
              <option value="女"<c:if test="${report.gender eq '女'}"> selected="selected"</c:if>>女</option>
            </select>
          </div>
        </div>
        <div class="list-item">
          <label class="list-label">地区</label>
          <div class="list-text">
            <div class="flex">
              <div class="form-select flex-1">
                <select name="" id="province">
                  <option value="">省</option>
                </select>
              </div>
              <div class="form-select flex-1">
                <select name="" id="city">
                  <option value="">市</option>
                </select>
              </div>
              <div class="form-select flex-1">
                <select name="areaId" id="district">
                  <option value="">区</option>
                </select>
              </div>
            </div>
          </div>
        </div>
        <div class="list-item">
          <label class="list-label">职业</label>
          <div class="list-text form-select">
            <select name="jobId">
              <option value="">请选择</option>
              <c:forEach items="${jobs}" var="job">
                <option value="${job.id}"${job.id eq report.jobId ? ' selected="selected"' : ''}>${job.jobName}</option>
              </c:forEach>
            </select>
          </div>
        </div>
        <div class="list-item" style="display: none">
          <label class="list-label" for="age">年龄</label>
          <div class="list-text">
            <input type="number" name="age" id="age" class="form-input" value="150" placeholder="填写客户年龄">
          </div>
        </div>
        <div class="list-item">
          <label class="list-label" for="phone">手机号</label>
          <div class="list-text">
            <input type="number" name="phone" id="phone" class="form-input" value="${report.phone}" placeholder="填写客户手机号">
          </div>
        </div>
      </div>

      <div class="list-group">
        <div class="list-title">检测信息，至少需要上传1张检测图片</div>
        <div class="list-item">
          <label class="list-label" for="times">检测次数</label>
          <div class="list-text">
            <input type="number" name="times" id="times" class="form-input" value="${report.times}" placeholder="第几次检测">
          </div>
        </div>
        <div class="list-item">
          <label class="list-label" for="date">检测日期</label>
          <div class="list-text">
            <%--<input type="text" id="date" name="reportedDate" class="form-input" value="" placeholder="填写检测时间 2001-01-01" onfocus="this.type='date'">--%>
            <input type="date" id="date" name="reportedDate" class="form-input" value="" >
          </div>
        </div>
        <div class="list-item">
          <label class="list-label">检测结果</label>
          <div class="list-text form-select">
            <select name="reportResult">
              <option value="">请选择</option>
              <option value="阴性"<c:if test="${report.reportResult eq '阴性'}"> selected="selected"</c:if>>阴性</option>
              <option value="弱阳性"<c:if test="${report.reportResult eq '弱阳性'}"> selected="selected"</c:if>>弱阳性</option>
              <option value="阳性"<c:if test="${report.reportResult eq '阳性'}"> selected="selected"</c:if>>阳性</option>
              <option value="干扰色"<c:if test="${report.reportResult eq '干扰色'}"> selected="selected"</c:if>>干扰色</option>
            </select>
          </div>
        </div>
        <div class="list-item">
          <div class="list-text image-upload image-multi">
            <div class="image-add" data-limit="6" data-name="image">
              <input type="hidden" value="">
              <input type="file">
              <em class="state state-add"></em>
            </div>
          </div>
        </div>
      </div>
      <div class="list-group">
        <div class="list-title">填写产品使用心得</div>
        <div class="list-item">
          <div class="list-text">
            <textarea name="text" class="form-input" rows="3" placeholder="填写产品使用心得">${report.text}</textarea>
          </div>
        </div>
      </div>
      <div class="form-btn" style="padding-bottom: 50px;">
        <input id="btnSubmit"  class="btn orange btn-block round-2 round-btnSubmit" type="button" onclick="fromSumbit()" value="提 交">
      </div>
    </form>
  </article>
  <div class="footer" >
    <div onclick="insurance()">保险申请</div>
    <div onclick="travel()" style="border-right: none;">旅游申请</div>
  </div>
</div>

<!--填写旅游申请代码如下!-->
<div class="policyInfoMain" style="display: none;">
  <header class="header"><h1>填写旅游申请</h1><a href="#" onclick="hideTravel()" class="button-left"><i class="fa fa-angle-left"></i></a></header>
  <article>
    <form class="valid-form" method="post" action="${ctx}/tour/findTourApple">
      <div id="policy" class="list-group">
        <div id="policyInfo">
          <div class="list-item">
            <label class="list-label" for="phone">推荐人电话：</label>
            <div class="list-text">
              <input type="number" name="phone1" id="phone1" class="form-input" value="${parentPhone}" placeholder="填写推荐人手机号" required >
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
  var  submitFalge = false;
  var tourId = "";
  var reportId="";//  提交 检查记录后再置值
  //保险申请
  var insuranceT,travelT,line,MyApply;
  function insurance(){
      if(submitFalge){
        $.ajax({
          url : '${ctx}/tour/ajaxCheckReport',
          data :{
            'reportId':reportId
          },
          dataType : 'json',
          type : 'POST',
          success : function(result){
            if(result.code == 0){
              insuranceT= layer.open({
                type: 2,
                area:['100%', '100%'],
                title: false,
                scrollbar: true,
                closeBtn: 0,
                content: '${ctx}/u/report/insuranceInfo?reportId='+ reportId + ''
              });
            } else {
              messageAlert(result.message);
            }
          }
        });
      }else{
        messageAlert("请先提交检测报告");
      }
  }
  function fileChange(obj){
    var file = obj.files[0];
    preview(file);
  }
  function preview(file) {
    var img = new Image(), url = img.src = URL.createObjectURL(file)
    var $img = $(img)
    img.onload = function() {
      URL.revokeObjectURL(url);
      $('#preview .previewImage').remove();
      $('#preview').empty().append($img);
    }
  }
  <%--//提交保险申请--%>
  <%--function submitBtn(){--%>
    <%--layer.close(insuranceT);--%>
    <%--line=layer.open({--%>
      <%--type: 2,--%>
      <%--area:['100%', '100%'],--%>
      <%--title: false,--%>
      <%--scrollbar: false,--%>
      <%--closeBtn: 0,--%>
      <%--content: '${ctx}/u/report/create'--%>
    <%--});--%>
  <%--}--%>
  //取消保险申请
  function hideBtn(){
    layer.close(insuranceT);
    $(".main").show();
  }
  //旅游申请
  function travel(){
    if(submitFalge){
      $.ajax({
        url : '${ctx}/tour/ajaxCheckTour',
        data :{
          'reportId':reportId
        },
        dataType : 'json',
        type : 'POST',
        success : function(result){
          if(result.code == 0){
            $(".main").hide();
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
            $(".policyInfoMain").show();
          } else {
            messageAlert(result.message);
          }
        }
      });
    }else{
      messageAlert("请先提交检测报告");
    }
  }
  //确认旅游申请
  function submitTravel(){
    if($('#phone1').val()!=null){
      layer.msg("请填入推荐人手机号！");
    }else{
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
  }
  //取消旅游申请
  function hideTravel(){
//    layer.close(travelT);
    $(".policyInfoMain").hide();
    $(".main").show();
  }

 //选择旅游路线
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
  function hideLine(){
    layer.close(line);
    travel();
  }

  //旅游信息返回上级
  function tourApply(){
    $(".policyInfoMain").show();
    $(".tourApply").hide();
  }
  /**
   * 获取rteportId
   * @returns {*}
   */
  function getReportId(){
    return reportId;
  }

  /**
   * ajax 提交数
   */
  function fromSumbit() {
    //验证
   var flage= $('.valid-form').validate({
      ignore: ':hidden',
      rules : {
        'realname' : {
          required : true
        },
        'gender' : {
          required : true
        },
        'age' : {
          required : true
        },
        'phone' : {
          required : true,
          phone : true
        },
        'times' : {
          required : true,
          digits : true,
          max: 999
        },
        'reportedDate' : {
          required : true
        },
        'areaId' : {
          required : true
        },
        'jobId' : {
          required : true
        },
        'reportResult' : {
          required : true
        },
        'text' : {
          required : true
        },
        'code' : {
          required : true
        },
        'image1' : {
          required : true
        },
        'idCardNumber' : {
          required : true
        },
        'birthday' : {
          required : true
        }
      }
    });
    if(flage.form()){
      if($('input[name="image"]').length < 1) {
        messageFlash('请至少上传一张图片');
        return;
      }
      var productId = $('#productId').val();
      if(!productId) {
        messageAlert('请选择产品');
        return;
      }
      var phone=parseInt(${myPhone});
      var phoneHtml=$("#phone").val();
      if(phone!=phoneHtml){
        $.dialog({
          content : '您的手机号码不匹配，无法申请旅游以及保险。',
          callback : function(index) {
            if (index == 1) {
              $(".footer").hide();
              $(".miui-dialog").remove();
              $.ajax({
                url : '${ctx}/u/report/ajaxCreate',
                data : $(".valid-form").serialize(),
                dataType : 'json',
                type : 'POST',
                success : function(result){
                  if(result.code == 0){
                    reportId=result.message;
                    submitFalge = true;
                    messageAlert("上传检测报告成功");
                    $("#btnSubmit").attr("disabled",true);
                    $(".orange.round-btnSubmit").css("background","#ccc");
                  } else{
                    messageAlert(result.message);
                  }
                }
              });
            }
          }
        });
      }else {
        $(".footer").show();
        $.ajax({
          url : '${ctx}/u/report/ajaxCreate',
          data : $(".valid-form").serialize(),
          dataType : 'json',
          type : 'POST',
          success : function(result){
            if(result.code == 0){
              reportId=result.message;
              submitFalge = true;
              messageAlert("上传检测报告成功");
              $("#btnSubmit").attr("disabled",true);
              $(".orange.round-btnSubmit").css("background","#ccc");
            } else{
              messageAlert(result.message);
            }
          }
        });
      }
    }
  }
  //选择出游时间
  function selectValue(obj) {
    tourTimeid="";
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
      if(ID==51||ID==53){
        $.dialog({
          content : '户籍要求：川渝藉不享受四川境内游活动；云南藉不享受云南境内游活动；以身份证登记的年龄和户藉地为准，工作地、居住地、暂住地信息无效。',
          callback : function(index) {
            if (index == 1) {
              $(".miui-dialog").remove();
              ajaxChange();
            }
          }
        });
      }else {
        ajaxChange();
      }
    }
  }
  function ajaxChange(){
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
                  $(".miui-dialog").remove();
                  $.ajax({
                    url : '${ctx}/tour/addTourforUser',
                    data : $("#tourApplyTable").serialize(),
                    dataType : 'json',
                    type : 'POST',
                    success : function(result){
                      if(result.code == 0){
                        $.dialog({
                          content : '您的旅游申请已提交，我们工作人员近期会与您联系，在此之前请勿购买参游机/车票，由此造成的财产损失，公司概不负责。',
                          callback : function(index) {
                            $(".miui-dialog").remove();
                            $(".tourApplyTableNew").hide();
                            $(".main").show();
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
                    content : '您的旅游申请已提交，我们工作人员近期会与您联系，在此之前请勿购买参游机/车票，由此造成的财产损失，公司概不负责。',
                    callback : function(index) {
                      $(".miui-dialog").remove();
                      $(".tourApplyTableNew").hide();
                      $(".main").show();
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
  //点击报名申请表单中的返回
  function hideApply(){
    $(".tourDetil").show();
    $("#selectTourTime option").eq(0).attr("selected",true);
    tourTimeid="";
    $(".tourApplyTableNew").html("");
    $(".tourApplyTableNew").hide();
  }

  function buildRow2(row){
    var rowTpl2 = document.getElementById('rowTpl2').innerHTML;
    laytpl(rowTpl2).render(row,function(html) {
      $('.TravelDis').append(html);
    });
  }


function buildRow(row){
var rowTpl = document.getElementById('rowTpl').innerHTML;
laytpl(rowTpl).render(row,function(html) {
$('#tourchange').append(html);
});
}
</script>
<script id="rowTpl" type="text/html">
  <a href="#" onclick="TravelDetil('{{d.id}}')" class="opacityAll">
    <p class="font">【{{d.title}}】</p>
    <img class="opacityFirst" src="{{d.image}}" style="display:block;width:100%;" />
    <%--<img src="${ctx}/images/opacityTwo.png" class="opacity" style="display:block;width:100%;z-index:9;" />--%>
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
     <%-- <div class="list-item">
        <label class="list-label" >年龄</label><div class="list-text">
        <input type="number" name="ages"  class="form-input" value="{{d.userinfoVo.agestr}}" {{#if(d.userinfoVo.agestr !='') { }} readonly {{# } }}  placeholder="填写年龄" required>
      </div>--%>
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
