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

<title>实名认证</title>
<%@ include file="/WEB-INF/view/include/head.jsp"%>
<link href="${stccdn}/css/ucenter/tag.css" rel="stylesheet" />
<%@ include file="/WEB-INF/view/include/validate.jsp"%>
<%@ include file="/WEB-INF/view/include/imageupload.jsp"%>
<script src="${stccdn}/js/area.js"></script>
<script type="text/javascript">
  $(function() {
    
    var area = new areaInit('province', 'city', 'district', '${userInfo.areaId}');

    $('.valid-form').validate({
      rules : {
        'areaId' : {
          required : true
        },
        'gender' : {
          required : true
        },
        'birthday' : {
          required : true
        },
        'jobId' : {
          required : true
        },
        'realname' : {
          required : true
        },
        'idCardNumber' : {
          required : true
        },
        'image1' : {
          required : true
        },
        'image2' : {
          required : true
        }
      },
      messages : {}
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
    
    <%--
    /* tags 标签*/
    $('#tagWrap').click(function() {
      showAside();
      //init
      $('#tagSelect .tag-option').removeClass('checked');
      var tagIds = $('#tagIds').val();
      $.each(tagIds.split(','), function(n, tag){
        $('#tagAside').find('.tag-option[data-id="' + tag + '"]').addClass('checked');
      });
    });

    var MAX_TAG_SIZE = 5;
    $('body').on('click', '.tag-option', function() {
      var $this = $(this);
      if ($this.hasClass('checked')) {
        $this.removeClass('checked');
      } else {
        if ($('#tagSelect .tag-option.checked').length >= MAX_TAG_SIZE) {
          messageFlash('您最多只能选择 ' + MAX_TAG_SIZE + ' 个标签');
          return false;
        }
        $this.addClass('checked');
      }
    }).on('click', '#btnTagsSure', function() {
      var $checkedTags = $('#tagSelect .tag-option.checked');
      if ($checkedTags.length == 0) {
        messageFlash('请至少选择一个标签.');
        return false;
      } else if ($checkedTags.length > MAX_TAG_SIZE) {
        messageFlash('您最多只能选择 ' + MAX_TAG_SIZE + ' 个标签.');
        return false;
      }

      hideAside();
      $('#tagWrap em').remove();
      var checkedIds = [];
      $checkedTags.each(function(n) {
        var labelClass = [ 'blue', 'orange', 'red', 'green', 'yellow' ];
        var id = $(this).attr('data-id');
        checkedIds.push(id);
        $('#tagWrap').append('<em class="label ' + labelClass[n % 5] + '">' + $(this).text() + '</em>')
      });
      $('#tagIds').val(checkedIds.join(','));
    });
    
    --%>

  });
  
  <%--
  function showAside() {
    if ($('#tagAside').length == 0) {
      var tagAside = document.getElementById('asideTpl').innerHTML;
      $(tagAside).appendTo('body');
    }
    $('body').addClass('o-hidden');
    $('#tagAside').show().animate({
      'left' : 0
    }, 300);
  }

  function hideAside() {
    $('body').removeClass('o-hidden');
    $('#tagAside').animate({
      'left' : '100%'
    }, 300, function() {
      $('#tagAside').hide();
    });
  }
  --%>
  
</script>

<%--
<script id="asideTpl" type="text/html">
<aside id="tagAside" class="aside-tag header-fixed fix-lt size-100p zindex-1000" style="left:100%;display:none;overflow-y:auto">
  <header class="header">
    <h1>选择标签</h1>
    <a href="javascript:hideAside();" class="button-left"><i class="fa fa-angle-left"></i></a>
    <a href="javascript:;" id="btnTagsSure" class="button-right"><span>确定</span></a>
  </header>
  <div id="tagSelect" class="tag-select">
    <c:forEach items="${tags}" var="tagMap">
      <div class="group-title">${tagMap.key}</div>
      <div class="tag-group">
        <c:forEach items="${tagMap.value}" var="tag">
          <span class="tag-option" data-id='${tag.id}'>${tag.tagName}</span>
        </c:forEach>
      </div>
    </c:forEach>
  </div>
</aside>
</script>
 --%>
 
</head>
<body>
  <header class="header">
    <h1>实名认证</h1>
    <a href="${ctx}/u/info" class="button-left"><i class="fa fa-angle-left"></i></a>
  </header>
  
  <article>
    <form action="${ctx}/u/userInfo/create" class="valid-form" method="post">
      <div class="list-group mt-10">
        <div class="list-item">
          <label class="list-label">姓名</label>
          <div class="list-text">
            <input type="text" id="realname" name="realname" class="form-input" value="${userInfo.realname}" placeholder="填写真实姓名">
          </div>
        </div>
        <div class="list-item">
          <label class="list-label">性别</label>
          <div class="list-text form-select">
            <select name="gender">
              <option value="">请选择</option>
              <option value="0" <c:if test="${userInfo.gender == '男'}"> selected="selected"</c:if>>男</option>
              <option value="1" <c:if test="${userInfo.gender == '女'}"> selected="selected"</c:if>>女</option>
            </select>
          </div>
        </div>
        <div class="list-item">
          <label class="list-label">生日</label>
          <div class="list-text">
            <input type="date" name="birthday" class="form-input" value="${userInfo.birthdayLabel}" placeholder="填写生日  1900-01-01">
          </div>
        </div>
        <div class="list-item">
          <label class="list-label">所在地</label>
          <div class="list-text">
            <div class="form-select pb-10 bd-b">
              <select name="" id="province">
                <option value="">选择省</option>
              </select>
            </div>
            <div class="form-select mt-10 pb-10 bd-b">
              <select name="" id="city">
                <option value="">选择市</option>
              </select>
            </div>
            <div class="form-select mt-10">
              <select name="areaId" id="district">
                <option value="">选择区</option>
              </select>
            </div>
          </div>
        </div>
        <div class="list-item">
          <label class="list-label">职业</label>
          <div class="list-text form-select">
            <select name="jobId">
              <option value="">请选择</option>
              <c:forEach items="${jobs}" var="job">
                <option value="${job.id}" <c:if test="${userInfo.jobId == job.id}"> selected="selected"</c:if>>${job.jobName}</option>
              </c:forEach>
            </select>
          </div>
        </div>
        <%--
        <div class="list-item">
          <label class="list-label">标签</label>
          <div id="tagWrap" class="list-text tag-wrap pt-5">
            <em class="font-999">请选择</em>
            <input type="hidden" id="tagIds" name="tagIds" value="">
          </div>
          <i class="i-arrow"></i>
        </div>
         --%>
      </div>
    
      <div class="list-group">
        <div class="list-title">请填写身份证信息</div>
        <div class="list-item">
          <label class="list-label">身份证号</label>
          <div class="list-text">
            <input type="text" id="idCardNumber" name="idCardNumber" class="form-input" value="${userInfo.idCardNumber}" placeholder="填写身份证号">
          </div>
        </div>
        <div class="list-item">
            <label class="list-label">身份证正面照</label>
            <div class="list-text image-upload">
              <div class="image-item image-single ">
                <input type="hidden" name="image1" id="image1" value="${userInfo.image1Thumbnail}">
            <c:if test="${userInfo.image1Thumbnail == null}">
                <img src="${stccdn}/image/upload_240_150.png">
            </c:if>
            <c:if test="${userInfo.image1Thumbnail != null}">
              <img  src="${userInfo.image1Thumbnail}" data-src="${userInfo.image1}">
            </c:if>
                <input type="file">
              </div>
            </div>
            <div class="list-unit">
              <a href="javascript:;" class="image-view font-blue fs-14" data-src="${stccdn}/image/example/id_card_1.jpg" data-title="身份证正面"><i class="fa fa-question-circle-o"></i> 示意图</a>
            </div>
        </div>
        <div class="list-item">
            <label class="list-label">反面照</label>
            <div class="list-text image-upload">
              <div class="image-item image-single">
                <input type="hidden" name="image2" id="image2" value="${userInfo.image2Thumbnail}">
                <c:if test="${userInfo.image2Thumbnail == null}">
                  <img src="${stccdn}/image/upload_240_150.png">
                </c:if>
                <c:if test="${userInfo.image2Thumbnail != null}">
                  <img src="${userInfo.image2Thumbnail}" data-src="${userInfo.image2}">
                </c:if>
                <img src="${stccdn}/image/upload_240_150.png">
                <input type="file">
              </div>
            </div>
            <div class="list-unit">
              <a href="javascript:;" class="image-view font-blue fs-14" data-src="${stccdn}/image/example/id_card_2.jpg" data-title="身份证反面"><i class="fa fa-question-circle-o"></i> 示意图</a>
            </div>
        </div>
      </div>
      <div class="form-btn">
        <input id="btnSubmit" class="btn orange btn-block round-2" type="submit" value="提 交">
      </div>
    </form>
    
  </article>
  
</body>
</html>
