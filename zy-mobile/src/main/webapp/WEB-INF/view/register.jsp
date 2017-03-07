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

<title>${sys} - 注册</title>
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
        url : '${ctx}/register/sendSmsCode',
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
    $('.valid-form').validate({
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
        },
        'isAgree' : {
            required : true
        }
      },
	    messages : {},
      submitHandler : function(form) {
		    var parentPhone = $('#parentPhone').val();
		    if(parentPhone != null && parentPhone != '') {
			    $.ajax({
				    url: '${ctx}/checkParentPhone',
				    data: {
					    phone: parentPhone
				    },
				    type: 'POST',
				    dataType: 'JSON',
				    success: function(result){
					    if(result.code == 0) {
						    $('#parentId').val(result.message);
						    form.submit();
					    } else {
						    messageFlash(result.message);
						    return ;
					    }
				    }
			    });
		    } else {
			    form.submit();
		    }
	    }
    });

    //注册协议
  	$('#protocol').click(function(){
  	  showProtocol();
  	});
    
  });
  
  function showProtocol() {
    if ($('#protocolList').length == 0) {
      var bankList = document.getElementById('protocolTpl').innerHTML;
      $(bankList).appendTo('body');
    }
    $('body').addClass('o-hidden');
    $('#protocolList').show().animate({
      'left' : 0
    }, 300, function() {
    });
  }
	
  function hideProtocol() {
    $('#protocolList').animate({
      'left' : '100%'
    }, 300, function() {
      $('body').removeClass('o-hidden');
      $('#protocolList').hide();
    });
  }
</script>
</head>
<body>
  <header class="header">
    <h1>注册</h1>
    <a href="${ctx}/" class="button-left"><i class="fa fa-angle-left"></i></a>
  </header>

  <c:if test="${!isNew}">
  <article>
    <form action="${ctx}/register" class="valid-form" method="post">
      <img class="image-120 block round center mt-30" src="${avatar}">
      <div class="mt-20 font-555 fs-24 lh-30 text-center">${nickname}</div>
      <p class="mt-30 font-999 fs-14 lh-30 text-center">只差一步啦，验证手机号完成注册</p>
      <div class="list-group mt-15">
        <div class="list-item">
          <label class="list-label" for="phone">手机号</label>
          <div class="list-text">
            <input type="text" id="phone" name="phone" class="form-input" placeholder="输入手机号" value="${phone}">
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
        <div class="list-item">
          <label class="list-label" for="phone">上级手机号</label>
          <div class="list-text">
            <input type="text" id="parentPhone" name="parentPhone" class="form-input" placeholder="输入上级手机号" value="">
            <input type="hidden" id="parentId" name="parentId" value="">
          </div>
        </div>
        <div class="list-item form-checkbox">
          <div class="list-icon">
            <input type="checkbox" checked="checked" value="0" name="isAgree" id="isAgree">
            <label for="isAgree" class="i-checked"></label>
          </div>
          <label for="isAgree" class="fs-14">阅读并接受</label>
          <span id="protocol" class="font-blue fs-14">《${sys}注册协议》</span>
        </div>
        
      </div>
      
      <div class="form-btn">
        <input id="btnSubmit" class="btn green btn-block round-2" type="submit" value="提交">
      </div>
    </form>
  </article>
  </c:if>
  
  <c:if test="${isNew}">
    <div class="abs-mm">
      <p class="fs-16 lh-30 text-center">请先完成微信授权</p>
      <a class="btn green round-2 width-200 mt-30" href="${oauthUrl}"><i class="fa fa-weixin font-white"></i> 微信一键授权</a>
    </div>
  </c:if>
  
</body>
</html>

<script id="protocolTpl" type="text/html">
  <aside id="protocolList" class="bank-list header-fixed abs-lt size-100p bg-white zindex-1000" style="left: 100%; display: none; overflow-y: scroll;">
    <header class="header">
      <h1>注册协议</h1>
      <a href="javascript:hideProtocol();" class="button-left"><i class="fa fa-angle-left"></i></a>
    </header>
    <div class="p-10">
      <p style="margin-top:5px;margin-bottom:5px;text-align:center">
        <strong><span style="font-family: 微软雅黑;color: rgb(51, 51, 51);font-size: 16px"><span style="font-family:微软雅黑">服务商注册协议</span></span></strong>
      </p>
      <p style="margin-top: 5px;margin-bottom: 5px">
        <span style=";font-family:微软雅黑;color:rgb(51,51,51);font-size:16px">&nbsp;&nbsp;&nbsp;&nbsp;<span style="font-family:微软雅黑">欢</span></span><span style=";font-family:微软雅黑;font-size:16px"><span style="font-family:微软雅黑">迎使用智优软件网络服务（以下简称</span>&quot;服务&quot;）。以下所述条款和条件即构成您与智优就合作和使用服务所达成的协议。一旦您使</span><span style=";font-family:微软雅黑;color:rgb(51,51,51);font-size:16px"><span style="font-family:微软雅黑">用了该服务，即表示您已接受了以下所述的条款和条件。如果您不同意接受全部的条款和条件，请您点击</span>“不同意”键以退出使用。当您完成注册并点击本协议下方&quot;同意&quot;键时，即表示您已同意受智优服务商注册协议约束，包括但不限于本协议、网站不时更新的使用规则等。</span>
      </p>
      <p style="margin-top: 5px;margin-bottom: 5px">
        <span style=";font-family:微软雅黑;color:rgb(51,51,51);font-size:16px">&nbsp;&nbsp;</span><strong><span style="font-family: 微软雅黑;color: rgb(51, 51, 51);font-size: 16px">&nbsp;&nbsp;<span style="font-family:微软雅黑">一、总则</span></span></strong>
      </p>
      <p style="margin-top: 5px;margin-bottom: 5px">
        <span style=";font-family:微软雅黑;font-size:16px">&nbsp;&nbsp;&nbsp;&nbsp;1.本软件的所有权和运营权归智优生物科技（上海）有限公司（以下简称“智优“）所有。</span>
      </p>
      <p style="margin-top: 5px;margin-bottom: 5px;text-indent: 28px">
        <span style=";font-family:微软雅黑;font-size:16px">2.智优有权随时修改本协议条款，您应当及时关注并同意，智优不承担通知义务。在智优修改服务条款后，您继续使用服务被视作您已接受了修改后的条款。除非得到智优的书面授权，任何人不得修改本协议。智优的通知、公告、声明或其它类似内容是本协议的一部分。</span>
      </p>
      <p style="margin-top: 5px;margin-bottom: 5px;text-indent: 32px">
        <strong><span style="font-family: 微软雅黑;font-size: 16px"><span style="font-family:微软雅黑">二、注册</span></span></strong>
      </p>
      <p style="margin-top: 5px;margin-bottom: 5px;text-indent: 32px">
        <span style=";font-family:微软雅黑;font-size:16px">1. 注册义务： 根据智优服务商资料的要求，提供关于您的真实、准确、完整的资料，并及时更新。倘若因您提供任何不真实、不准确、不完整或不能反映当前情况的资料造成的任何损失或损害，由您承担全部责任。</span>
      </p>
      <p style="margin-top: 5px;margin-bottom: 5px;text-indent: 32px">
        <span style=";font-family:微软雅黑;font-size:16px">2. 经本软件系统完成注册程序并通过身份认证后，您即成为智优服务商，可以享有服务商的一切权限。智优有权对服务商的权限设计进行变更。服务商有义务保证密码和帐号的安全，您利用该密码和帐号所进行的一切活动由您承担全部责任。如您发现帐号遭到未授权的使用或发生其他任何安全问题，应立即修改帐号密码并妥善保管，如有必要，即时通知智优。因黑客行为或您的保管疏忽导致帐号非法使用，智优不承担任何责任。智优有义务在接到服务商通知的情况下，采取相应措施减少服务商可能产生的损失。</span>
      </p>
      <p style="margin-top: 5px;margin-bottom: 5px;text-indent: 32px">
        <strong><span style="font-family: 微软雅黑;font-size: 16px"><span style="font-family:微软雅黑">三、合作与范围</span></span></strong>
      </p>
      <p style="margin-top: 5px;margin-bottom: 5px;text-indent: 32px">
        <span style=";font-family:微软雅黑;font-size:16px"><span style="font-family:微软雅黑">服务商是智优的兼职人员，智优授权服务商通过微信等社交工具负责智优产品的销售及客户服务工作。服务商不得在淘宝、天猫、京东等电子商务平台销售智优公司产品。</span></span>
      </p>
      <p style="margin-top: 5px;margin-bottom: 5px;text-indent: 32px">
        <strong><span style="font-family: 微软雅黑;font-size: 16px"><span style="font-family:微软雅黑">四、权利与义务</span></span></strong>
      </p>
      <p style="margin-top: 5px;margin-bottom: 5px;text-indent: 32px">
        <span style=";font-family:微软雅黑;font-size:16px">1.智优的权利与义务</span>
      </p>
      <p style="margin-top: 5px;margin-bottom: 5px;text-indent: 32px">
        <span style=";font-family:微软雅黑;font-size:16px"><span style="font-family:微软雅黑">（</span>1）智优作为产品和服务提供方，是在中国合法登记注册的企业，具有相关正规资质，并保证向您提供的产品为质量合格产品。</span>
      </p>
      <p style="margin-top: 5px;margin-bottom: 5px;text-indent: 32px">
        <span style=";font-family:微软雅黑;font-size:16px"><span style="font-family:微软雅黑">（</span>2）智优应向服务商提供服务商授权码，并提供合适的渠道查询。</span>
      </p>
      <p style="margin-top: 5px;margin-bottom: 5px;text-indent: 32px">
        <span style=";font-family:微软雅黑;font-size:16px"><span style="font-family:微软雅黑">（</span>3）智优应及时处理服务商的订单，同时向服务商提供详尽的业务相关文件与资料，提供有关业务及操作流程培训，帮助服务商提高销售服务能力。</span>
      </p>
      <p style="margin-top: 5px;margin-bottom: 5px;text-indent: 32px">
        <span style=";font-family:微软雅黑;font-size:16px"><span style="font-family:微软雅黑">（</span>4）如因市场变化需要调整价格体系、市场政策的，智优应提前告知服务商。</span>
      </p>
      <p style="margin-top: 5px;margin-bottom: 5px;text-indent: 32px">
        <span style=";font-family:微软雅黑;font-size:16px"><span style="font-family:微软雅黑">（</span>5）为规范市场，智优有权对服务商进行监督和管理，对违反公司制度的，智优有权采取警告、停货或取消服务商资格等措施，造成损失的服务商按所造成的经济损失赔偿并承担相应的法律责任。</span>
      </p>
      <p style="margin-top: 5px;margin-bottom: 5px;text-indent: 32px">
        <span style=";font-family:微软雅黑;font-size:16px">2. 服务商的权利与义务</span>
      </p>
      <p style="margin-top: 5px;margin-bottom: 5px;text-indent: 32px">
        <span style=";font-family:微软雅黑;font-size:16px"><span style="font-family:微软雅黑">（</span>1）服务商有权在本协议约定的渠道范围内开拓、发展客户，推广智优产品，维护智优的品牌形象和服务品质。业务中服务商应如实向客户介绍智优产品，并确保为客户提供良好的服务，不得以欺诈、胁迫等不正当手段损害客户及智优声誉。</span>
      </p>
      <p style="margin-top: 5px;margin-bottom: 5px;text-indent: 32px">
        <span style=";font-family:微软雅黑;font-size:16px"><span style="font-family:微软雅黑">（</span>2）服务商需要智优协助培训或者从事其他相关工作的，智优应根据协议内容给予支持与帮助。</span>
      </p>
      <p style="margin-top: 5px;margin-bottom: 5px;text-indent: 32px">
        <span style=";font-family:微软雅黑;font-size:16px"><span style="font-family:微软雅黑">（</span>3）作为智优的服务商，您保证销售的所有智优产品都是从智优正规渠道订货，不得销售任何伪劣产品。一经发现，取消您的服务商资格。产生损失或索赔的，由您承担全部责任和连带责任。</span>
      </p>
      <p style="margin-top: 5px;margin-bottom: 5px;text-indent: 32px">
        <span style=";font-family:微软雅黑;font-size:16px"><span style="font-family:微软雅黑">（</span>4）严格遵守公司制度，严格遵守公司产品价格体系，终端零售价按智优产品统一价格执行。若违反相关规定，智优有权采取警告、停货或取消服务商资格等措施。</span>
      </p>
      <p style="margin-top: 5px;margin-bottom: 5px;text-indent: 32px">
        <span style=";font-family:微软雅黑;font-size:16px"><span style="font-family:微软雅黑">（</span>5）服务商出单后，应及时通知上级服务商，并按公司要求及时下单。</span>
      </p>
      <p style="margin-top: 5px;margin-bottom: 5px;text-indent: 32px">
        <span style=";font-family:微软雅黑;font-size:16px"><span style="font-family:微软雅黑">（</span>6）您购买智优产品为自用或销售终端用户自用。您承诺批发智优品时只批发给具备国家相应要求正规资质的单位或个人。</span>
      </p>
      <p style="margin-top: 5px;margin-bottom: 5px;text-indent: 32px">
        <span style=";font-family:微软雅黑;font-size:16px"><span style="font-family:微软雅黑">（</span>7）您有权利和义务管理所管辖的服务商团队，不得有任何违反公司制度和扰乱市场的行为。一经发现，应及时妥善处理并向公司汇报。</span>
      </p>
      <p style="margin-top: 5px;margin-bottom: 5px;text-indent: 32px">
        <span style=";font-family:微软雅黑;font-size:16px"><span style="font-family:微软雅黑">（</span>8）为保障服务商的利益，更好贯彻价格统一原则、规范市场，您承诺在签订本协议前，未接触过智优产品其他服务商，未涉及过询价、拿货等可能在本协议签署后，损害智优其他服务商利益的行为。本协议签署后，您承诺不会通过越级、窜货等行为损害智优及智优其他服务商的利益。</span>
      </p>
      <p style="margin-top: 5px;margin-bottom: 5px;text-indent: 32px">
        <span style=";font-family:微软雅黑;font-size:16px"><span style="font-family:微软雅黑">（</span>9）未经智优正式书面授权，您承诺不以智优办事处等垄断性、排斥性名义进行广告宣传、商业活动及其他非本协议目的之活动，不做任何超出协议内容及经营范围之外的本业务不相关的事。</span>
      </p>
      <p style="margin-top: 5px;margin-bottom: 5px;text-indent: 32px">
        <span style=";font-family:微软雅黑;font-size:16px"><span style="font-family:微软雅黑">（</span>10）您承诺不直接或间接从事同智优具有竞争性的业务，不接受智优竞争对手的任何形式的聘用，不为智优竞争对手提供任何形式的咨询、顾问及培训等服务。您同意在担任智优服务商期间不以微商、直销等模式经营其他产品。您同意，合作终止一年内不经营其他同类产品。</span>
      </p>
      <p style="margin-top: 5px;margin-bottom: 5px;text-indent: 32px">
        <span style=";font-family:微软雅黑;font-size:16px"><span style="font-family:微软雅黑">（</span>11）不论是本协议履行期间还是终止后,您同意对根据本协议所了解到的智优、智优服务商以及智优产品信息、价格等相关商业秘密负有保密义务。如因您的原因泄密造成智优及其他服务商损失的，您同意按所造成的经济损失赔偿并承担相应的法律责任。</span>
      </p>
      <p style="margin-top: 5px;margin-bottom: 5px;text-indent: 32px">
        <span style=";font-family:微软雅黑;font-size:16px"><span style="font-family:微软雅黑">商业秘密包括但不限于：所有智优业务、智优提供服务商的具有商业价值、目前尚未公知的且智优认为需要保密的行销计划与方案、管理模式、经营模式、经营状况、发展规划、投资决策、行动计划、分立计划、广告策略、财务资料、人事资料、薪酬资料、程序文件、档案等方面的内容。</span></span>
      </p>
      <p style="margin-top: 5px;margin-bottom: 5px;text-indent: 32px">
        <span style=";font-family:微软雅黑;font-size:16px"><span style="font-family:微软雅黑">若您违反上述保密义务，向与智优存在竞争关系的公司或个人提供上述任何保密信息的，智优除追究本协议项下的违约责任外，另行向您追究违约责任壹佰万元，违约金不足以弥补智优损失的，您还应该赔偿因此给智优造成的损失（包括但不限于诉讼成本、律师费、差旅费等）。情节严重涉嫌犯罪的，智优除追究您的民事责任外，还将运用司法救济途径追究您的刑事责任。</span></span>
      </p>
      <p style="margin-top: 5px;margin-bottom: 5px;text-indent: 32px">
        <span style=";font-family:微软雅黑;font-size:16px"><span style="font-family:微软雅黑">（</span>12）遵守智优APP用户注册协议。 </span>
      </p>
      <p style="margin-top: 5px;margin-bottom: 5px;text-indent: 32px">
        <span style=";font-family:微软雅黑;font-size:16px"><span style="font-family:微软雅黑">（</span>13）若您违反本协议条款，您同意赔偿智优由此产生的全部经济损失,同时智优保留对您追究相关法律责任的权利。如因您原因导致智优被索赔，您同意承担责任并赔偿给智优造成的全部损失。</span>
      </p>
      <p style="margin-top: 5px;margin-bottom: 5px">
        <span style=";font-family:微软雅黑;font-size:16px">&nbsp;&nbsp;&nbsp;</span><strong><span style="font-family: 微软雅黑;font-size: 16px">&nbsp;&nbsp;<span style="font-family:微软雅黑">五、软件服务更新、中断和终止</span></span></strong>
      </p>
      <p style="margin-top: 5px;margin-bottom: 5px;text-indent: 32px">
        <span style=";font-family:微软雅黑;font-size:16px">1.您同意，本软件保留在任何时候自行决定对服务及其相关功能、应用软件变更、升级、修改、转移的权利。您同意，对于上述行为，智优均不需通知，并且对您和任何第三人不承担任何责任。</span>
      </p>
      <p style="margin-top: 5px;margin-bottom: 5px">
        <span style=";font-family:微软雅黑;font-size:16px">&nbsp;&nbsp;&nbsp;&nbsp;2.</span><span style=";font-family:宋体;font-size:16px">&nbsp;</span><span style=";font-family:微软雅黑;font-size:16px"><span style="font-family:微软雅黑">您同意，存在下列情形之一，智优有权对您停止或中断软件服务，并无需向您或任何第三人承担任何责任：</span></span>
      </p>
      <p style="margin-top: 5px;margin-bottom: 5px">
        <span style=";font-family:微软雅黑;font-size:16px">&nbsp;&nbsp;&nbsp;&nbsp;<span style="font-family:微软雅黑">（</span>1）智优无法确认您提供的注册登记资料或身份信息；</span>
      </p>
      <p style="margin-top: 5px;margin-bottom: 5px;text-indent: 28px">
        <span style=";font-family:微软雅黑;font-size:16px"><span style="font-family:微软雅黑">（</span>2）智优</span><span style=";font-family:微软雅黑;font-size:16px"><span style="font-family:微软雅黑">判断您违反了本协议条款。</span></span>
      </p>
      <p style="margin-top: 5px;margin-bottom: 5px;text-indent: 28px">
        <span style=";font-family:微软雅黑;font-size:16px">3. 对于因不可抗力或智优不能控制的原因造成的网络服务中断或其它缺陷，智优不承担任何责任，但将尽力减少因此而造成的损失和影响。</span>
      </p>
      <p style="margin-top: 5px;margin-bottom: 5px">
        <span style=";font-family:微软雅黑;font-size:16px">&nbsp;&nbsp;&nbsp;&nbsp;3.</span><span style=";font-family:宋体;font-size:16px">&nbsp;</span><span style=";font-family:微软雅黑;font-size:16px"><span style="font-family:微软雅黑">您同意，智优有权于任何时间因任何理由暂时或永久修改或终止本软件服务</span>(或其任何部分)，智优对您和任何第三人均无需承担任何责任。</span>
      </p>
      <p style="margin-top: 5px;margin-bottom: 5px;text-indent: 32px">
        <strong><span style="font-family: 微软雅黑;font-size: 16px"><span style="font-family:微软雅黑">六、附则</span></span></strong>
      </p>
      <p style="margin-top: 5px;margin-bottom: 5px;text-indent: 32px">
        <span style=";font-family:微软雅黑;font-size:16px">1. 智优关于服务商及业务操作等相关制度文件作为本协议附件，具有同等法律效力。</span>
      </p>
      <p style="margin-top: 5px;margin-bottom: 5px;text-indent: 32px">
        <span style=";font-family:微软雅黑;font-size:16px">2. 协议期壹年，自注册通过日期开始计。协议期满双方无异议，服务商继续使用注册账号的视为自动续约。</span>
      </p>
      <p style="margin-top: 5px;margin-bottom: 5px;text-indent: 32px">
        <span style=";font-family:微软雅黑;font-size:16px">3.如本协议中的任何条款无论因何种原因完全或部分无效或不具有执行力，本协议的其余条款仍应有效并且有约束力。</span>
      </p>
      <p style="margin-top: 5px;margin-bottom: 5px;text-indent: 32px">
        <span style=";font-family:微软雅黑;font-size:16px">4. 本协议解释权及修订权归智优生物科技（上海）有限公司所有，本协议的执行及争议的解决均提交智优生物科技（上海）有限公司所在地人民法院裁决，应适用中华人民共和国法律。</span>
      </p>
  </div>  
  </aside>
</script>