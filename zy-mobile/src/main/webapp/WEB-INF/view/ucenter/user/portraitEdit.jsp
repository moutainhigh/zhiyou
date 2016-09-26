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

<title>完善资料</title>
<%@ include file="/WEB-INF/view/include/head.jsp"%>
<%@ include file="/WEB-INF/view/include/validate.jsp"%>
<link rel="stylesheet" href="${stccdn}/css/ucenter/portrait.css" />
<script src="${stccdn}/js/area.js"></script>
<script>
  $(function() {
    var area = new areaInit('province', 'city', 'district', '${portrait.areaId}');

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
        'tagIds' : {
          required : true
        }
      },
      messages : {
        'areaId' : {
          required : '请选择所在地区'
        },
        'gender' : {
          required : '请选择性别'
        },
        'birthday' : {
          required : '请填写生日'
        },
        'jobId' : {
          required : '请选择职业'
        },
        'tagIds' : {
          required : '请填写标签'
        }
      }
    });

    /* tags 标签*/
    function showTagAside() {
      if ($('#tagAside').length == 0) {
        var tagAside = document.getElementById('tagTpl').innerHTML;
        $(tagAside).appendTo('body');
      }
      $('body').addClass('header-fixed');
      $('.header h1').text('选择标签');
      $('#tagAside').show().animate({
        'left' : 0
      }, 300, function() {
        $('#btnTagsClose').show();
        $('#btnTagsSure').show();
      });
    }

    function hideTagAside() {
      $('#tagAside').animate({
        'left' : '100%'
      }, 300, function() {
        $('body').removeClass('header-fixed');
        $('#tagAside').hide();
      });
      $('.header h1').text('自我画像');
      $('#btnTagsClose').hide();
      $('#btnTagsSure').hide();
    }

    $('#formTags').click(function() {
      showTagAside();
      var tagIds = $('#tagIds').val();
      $.each(tagIds.split(','), function(n, tag){
        $('#tagAside').find('.tag-option[data-id="' + tag + '"]').addClass('checked');
      });
    });

    var MAX_TAG_SIZE = 5;
    $('body').on('click', '#btnTagsClose', function() {
      hideTagAside();
    }).on('click', '#btnTagsSure', function() {
      var $checkedTags = $('#tagSelect .tag-option.checked');
      if ($checkedTags.length == 0) {
        messageFlash('请至少选择一个标签.');
        return false;
      } else if ($checkedTags.length > MAX_TAG_SIZE) {
        messageFlash('您最多只能选择 ' + MAX_TAG_SIZE + ' 个标签.');
        return false;
      }

      hideTagAside();
      $('#tagWrap em').remove();
      var checkedIds = [];
      $checkedTags.each(function(n) {
        var labelClass = [ 'blue', 'orange', 'red', 'green', 'yellow' ];
        var id = $(this).attr('data-id');
        checkedIds.push(id);
        $('#tagWrap').append('<em class="label ' + labelClass[n % 5] + '">' + $(this).text() + '</em>')
      });
      $('#tagWrap input:hidden').val(checkedIds.join(','));
    }).on('click', '.tag-option', function() {
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
    });
  });
</script>
<script id="tagTpl" type="text/html">
<aside id="tagAside" class="aside-tag header-fixed">
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

</head>
<body class="ox-hidden">
  <header class="header">
    <h1>完善资料</h1>
    <a href="${ctx}/u/portrait" class="button-left"><i class="fa fa-angle-left"></i></a>
    <a href="javascript:;" id="btnTagsClose" class="button-left hide"><i class="fa fa-angle-left"></i></a>
    <a href="javascript:;" id="btnTagsSure" class="button-right hide"><span>确定</span></a>
  </header>
  <article>
    <form id="form" class="valid-form" action="${ctx}/u/portrait/edit" method="post">
      <div class="list-group">
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
      </div>

      <div class="list-group">
        <div class="list-item">
          <label class="list-label">性别</label>
          <div class="list-text form-select">
            <select name="gender">
              <option value="">请选择</option>
              <option value="0" <c:if test="${portrait.gender eq '男'}"> selected</c:if>>男</option>
              <option value="1" <c:if test="${portrait.gender eq '女'}"> selected</c:if>>女</option>
            </select>
          </div>
        </div>

        <div class="list-item">
          <label class="list-label">生日</label>
          <div class="list-text">
            <input type="date" name="birthday" class="form-input" value="${portrait.birthdayLabel}" placeholder="填写生日  1900-01-01">
          </div>
        </div>

        <div class="list-item">
          <label class="list-label">职业</label>
          <div class="list-text form-select">
            <select name="jobId">
              <option value="">请选择</option>
              <c:forEach items="${jobs}" var="job">
                <option value="${job.id}" <c:if test="${portrait.jobId eq job.id}"> selected </c:if>>${job.jobName}</option>
              </c:forEach>
            </select>
          </div>
        </div>
      </div>

      <div id="formTags" class="list-group">
        <div class="list-item">
          <label class="list-label">标签</label>
          <div id="tagWrap" class="list-text tag-wrap pt-5">
            <c:forEach items="${portrait.tagNames}" var="tag" varStatus="index">
              <em class="label <c:if test="${index.index % 5 == 0 }"> blue</c:if>
                        <c:if test="${index.index % 5 == 1 }"> orange</c:if>
                        <c:if test="${index.index % 5 == 2 }"> red</c:if>
                        <c:if test="${index.index % 5 == 3 }"> green</c:if>
                        <c:if test="${index.index % 5 == 4 }"> yellow</c:if>">${tag}</em>
            </c:forEach>
            <input type="hidden" id="tagIds" name="tagIds" value="${portrait.tagIds}">
          </div>
          <i class="i-arrow"></i>
        </div>
      </div>

      <div class="form-btn">
        <input id="btnSubmit" class="btn orange btn-block round-2" type="submit" value="提 交">
      </div>

    </form>
  </article>

</body>
</html>
