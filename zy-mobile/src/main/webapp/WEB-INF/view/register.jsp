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

<title>完成注册</title>
<%@ include file="/WEB-INF/view/include/head.jsp"%>
<%@ include file="/WEB-INF/view/include/validate.jsp"%>
<script type="text/javascript">
  $(function() {
    // 验证码刷新
    if ($('#captchaImage').length > 0) {
      var refreshCaptcha = function() {
        $("#captchaImage:visible").attr("src", Config.ctx + '/captcha?r=' + new Date().getTime());
      }
      //$(".captcha-refresh").click(refreshCaptcha);
      $("#captchaImage").click(refreshCaptcha);
    }

    // 发送短信校验码
    $('#btnSend').click(function() {
      var $this = $(this);

      var phone = $('[name="phone"]:visible').val();
      var phoneRegex = /^1\d{10}$/;
      if (!phone || !phone.match(phoneRegex)) {
        messageFlash('请填写正确的手机号', 'error');
        return;
      }
      var captcha = $('[name="captcha"]:visible').val();
      if (!captcha) {
        messageFlash('请填写图形验证码');
        return;
      }

      var leftTime = 120;

      $.ajax({
        url : '${ctx}/u/sendBindPhoneSmsCode',
        type : 'POST',
        data : {
          phone : phone,
          captcha : captcha
        },
        success : function(result) {
          if (result.code != 0) {
            $.message(result.message, 'error');
          } else {
            $.message(result.message, 'success');
            leftTime = 120;
            setTimeout(refreshSendButton, 1000);
          }
        }
      });

      function refreshSendButton() {
        if (leftTime > 0) {
          $this.attr('disabled', 'disabled').addClass('disabled').text('' + leftTime + ' 秒后重新发送');
          setTimeout(refreshSendButton, 1000);
          leftTime--;
        } else {
          $this.removeAttr('disabled').removeClass('disabled').text('发送验证码');
        }
      }

    });

    //注册验证
    $(".valid-form").validate({
      rules : {
        'phone' : {
          required : true,
          mobile : true
        },
        'captcha' : {
          required : true
        },
        'smsCode' : {
          required : true
        }
      }
    });
  });
</script>
</head>
<body class="header-fixed">
  <header class="header">
    <h1>完成注册</h1>
  </header>

  <article>
    <form action="${ctx}/u/bindPhone" class="valid-form" method="post">
      <img class="image-120 block round center mt-30" src="${avatar}">
      <div class="mt-20 font-555 fs-24 lh-30 text-center">${nickname}</div>
      <p class="mt-30 font-999 fs-14 lh-30 text-center">只差一步啦，验证手机号完成注册</p>
      <div class="list-group mt-15">
        <div class="list-item">
          <label class="list-label" for="realname">手机号</label>
          <div class="list-text">
            <input type="text" id="phone" name="phone" class="form-input" placeholder="输入手机号" value="">
          </div>
        </div>
        <div class="list-item img-captcha">
          <label class="list-label" for="captcha">图形码</label>
          <div class="list-text">
            <input type="text" id="captcha" name="captcha" class="form-input" placeholder="图形验证码">
          </div>
          <div class="list-unit">
            <img id="captchaImage" src="${ctx}/captcha">
          </div>
        </div>
        <div class="list-item phone-captcha">
          <label class="list-label" for="smsCode">手机验证码</label>
          <div class="list-text">
            <input type="text" id="smsCode" name="smsCode" class="form-input" placeholder="手机验证码" value="">
          </div>
          <div class="list-unit">
            <a id="btnSend" class="btn blue btn-sm">发送验证码</a>
          </div>
        </div>
      </div>
      <div class="form-btn">
        <input id="btnSubmit" class="btn-submit btn green btn-block" type="submit" value="提交">
      </div>
    </form>
  </article>

</body>
</html>
