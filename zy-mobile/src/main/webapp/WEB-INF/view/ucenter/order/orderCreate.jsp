<%@ page language="java" pageEncoding="UTF-8"%>
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

<script src="${stccdn}/plugin/laytpl-1.1/laytpl.js"></script>

<title>确认订单信息</title>
<%@ include file="/WEB-INF/view/include/head.jsp"%>
<script src="${stccdn}/js/area.js"></script>

<script>
  $(function() {
    $('#btnSubmit').click(function(){
      if(!$('#addressId').val()) {
        messageFlash('请先选择收货地址.');
        return false;  
      }
      if($('input[name=isPayToPlatform]:checked').length == 0) {
        messageFlash('请先选择收款对象.');
        return false;  
      }

      // 检测是否  实名认证realFlage
      <c:if test="${!realFlage}">
         $.dialog({
            content : '实名认证后才能提交订单,去认证？',
          callback : function(index) {
            if (index == 1) {
              $(".miui-dialog").remove();
              $('#orderForm').attr("action",'${ctx}/u/userInfo');
              $('#orderForm').submit();
             return false;
          }
        }
      });
       return false;
      </c:if>
      /*if(!${realFlage}){
        $.dialog({
          content : '实名认证后才能提交订单,去认证？',
          callback : function(index) {
            if (index == 1) {
              $(".miui-dialog").remove();
              $('#orderForm').attr("action",'${ctx}/u/userInfo');
              $('#orderForm').submit();
              return false;
            }
          }
        });
        return false;
      }*/
        <c:if test="${userRank == 'V0' && empty parent}">
        var parentPhone = $('#parentPhone').val();
        if(!parentPhone) {
          messageFlash('请填写推荐人手机号');
          return false;
        }
        $.ajax({
          url: '${ctx}/checkParentPhone',
          data: {
            phone: parentPhone
          },
          type: 'POST',
          dataType: 'JSON',
          success: function(result){
            if(result.code == 0) {
              $('[name="parentId"]').val(result.message);
              $('#orderForm').submit();
            } else {
              messageAlert(result.message);
            }
          }
        });
        return false;
        </c:if>
    });
    
  	//选择收货地址
  	$('.receiver-info').click(function(){
  		if($(this).find('.btn-add-address').length){
  			return;
  		}
  		showAddressList();
  	});
  
    $('body').on('click', '.btn-add-address', function() {
      showAddressCreate();
    });

    $('body').on('click', '.address', function() {
      var $this = $(this);
      var address = {};
      address.id = $this.attr('data-id');
      address.realname = $this.find('.address-name').text();
      address.phone = $this.find('.address-phone').text();
      address.addressText = $this.find('.address-text').text();
      
      $.dialog({
        content : '您确定要设置收货地址吗？<br>' + '收件人：' + address.realname + '<br>' + '手机号：' + address.phone + '<br>' + '地址：' + address.addressText,
        callback : function(index){
          if(index == 1) {
            hideAddressList();
            setAddress(address);
            return true;
          }
        }
      });
    });

    $('body').on('click', '.btn-create', function() {
      var realname = $('#realname').val();
      var phone = $('#phone').val();
      var address = $('#address').val();
      var areaId = $('#district option:selected').val();
      if (!realname) {
        messageFlash('请填写收件人名');
        return;
      }
      if (!phone) {
        messageFlash('请填写收件人电话');
        return;
      }
      if (!areaId) {
        messageFlash('请选择收件人省市区');
        return;
      }
      if (!address) {
        messageFlash('请填写收件人详细地址');
        return;
      }

      $.ajax({
        url : '${ctx}/u/address/createAjax',
        data : {
          realname : $('#realname').val(),
          phone : $('#phone').val(),
          address : $('#address').val(),
          areaId : $('#district option:selected').val()
        },
        type : 'POST',
        dataType : 'json',
        success : function(result) {
          if (result.code == 0) {
            var address = result.data;
            setAddress(address);
            hideAddressCreate();
          }
        }
      });
    });

	  $('.form-switch label').click(function() {
		  var checked = $(this).prev().is(':checked');
		  $('#isOrderFill').val(!checked);
	  });

  });
  
  function showAddressCreate() {
    var addressCreateHtml = document.getElementById('addressCreateTpl').innerHTML;
    $('article > .list-item').hide();
    $('body').addClass('o-hidden').append(addressCreateHtml);
    $('#addressCreate').show().animate({
      'left' : 0
    }, 300, function() {
      $('#addressList').remove();
    });
    var area = new areaInit('province', 'city', 'district');
  }
  
  function hideAddressCreate(){
    $('#addressCreate').animate({
      'left' : '100%'
    }, 300, function() {
      $('#addressCreate').remove();
      $('body').removeClass('o-hidden');
    });
  }

  function showAddressList() {
    $.ajax({
      url : '${ctx}/u/address/listAjax',
      type : 'POST',
      dataType : 'json',
      success : function(result) {
        var addressTpl = document.getElementById('addressListTpl').innerHTML;
        laytpl(addressTpl).render(result.data, function(html) {
          $('article > .list-item').hide();
          $('body').addClass('o-hidden').append(html);
          $('#addressList').show().animate({
            'left' : 0
          }, 300, function() {
          });
          
        });
      }
    });
  }
  
  function hideAddressList(){
    $('#addressList').animate({
      'left' : '100%'
    }, 300, function() {
      $('#addressList').remove();
      $('body').removeClass('o-hidden');
    });
  }
  
  function setAddress(address) {
    $('#addressId').val(address.id);
    var addressText = address.addressText ? address.addressText 
        : address.province + ' ' + address.city + ' ' + address.district + ' ' + address.address
    $('.receiver-info').html('<div>' + address.realname + '<span class="right">' + address.phone + '</span></div>' 
        + '<div class="fs-14 font-777">' + addressText+ '</div>')
  }
  
</script>

<script id="addressCreateTpl" type="text/html">
<aside id="addressCreate" class="abs-lt size-100p bg-gray zindex-1000" style="left: 100%; display: none;">
  <header class="header">
    <h1>新增收货地址</h1>
    <a href="javascript:hideAddressCreate();" class="button-left"><i class="fa fa-angle-left"></i></a>
  </header>
  <article class="address-create">
      <div class="form-message note note-warning mb-0 hide">
        <p>输入信息有误，请先更正。</p>
      </div>
      <div class="list-group">
        <div class="list-item">
          <label class="list-label" for="realname">收件人</label>
          <div class="list-text">
            <input type="text" id="realname" name="realname" class="form-input" value="" placeholder="填写收件人姓名">
          </div>
        </div>
        <div class="list-item">
          <label class="list-label" for="phone">手机号码</label>
          <div class="list-text">
            <input type="tel" id="phone" name="phone" class="form-input" value="" placeholder="填写收件人手机号码">
          </div>
        </div>
        <div class="list-item">
          <label class="list-label">省份</label>
          <div class="list-text form-select">
            <select name="province" id="province">
              <option value="">请选择</option>
            </select>
          </div>
        </div>
        <div class="list-item">
          <label class="list-label">城市</label>
          <div class="list-text form-select">
            <select name="city" id="city">
              <option value="">请选择</option>
            </select>
          </div>
        </div>
        <div class="list-item">
          <label class="list-label">地区</label>
          <div class="list-text form-select">
            <select name="areaId" id="district">
              <option value="">请选择</option>
            </select>
          </div>
        </div>
        <div class="list-item">
          <div class="list-text">
            <textarea id="address" name="address" class="form-input" rows="3" placeholder="填写详细地址，例如街道名称，楼层和门牌号等信息"></textarea>
          </div>
        </div>
        <div class="list-item">
          <div class="list-text">设为默认地址</div>
          <div class="list-unit form-switch">
            <input type="hidden" name="_isDefault" value="false">
            <input type="checkbox" id="isDefault" name="isDefault">
            <label class="i-switch" for="isDefault"></label>
          </div>
        </div>
      </div>

      <div class="form-btn">
        <input class="btn-create btn green btn-block" type="button" value="保 存">
      </div>
  </article>
</aside>
</script>

<script id="addressListTpl" type="text/html">
<aside id="addressList" class="address-list abs-lt size-100p bg-gray z-100" style="left: 100%; display: none;">
  <header class="header">
    <h1>设置收货地址</h1>
    <a href="javascript:hideAddressList();" class="button-left"><i class="fa fa-angle-left"></i></a>
    <a href="javascript:;" class="button-right btn-add-address">新增</a>
  </header>
  <div class="list-group">
  	{{# for(var i = 0, len = d.length; i < len; i++){ }}
      <a href="javascript:;" class="list-item address" data-id="{{ d[i].id }}">
        <div class="list-text pl-10 pr-10">
          <div><span class="address-name">{{ d[i].realname }}</span><span class="address-phone fs-14 font-777 right">{{ d[i].phone }}</span></div>
          <div class="fs-14 font-777 address-text">{{ d[i].province }} {{ d[i].city }} {{ d[i].district }} {{ d[i].address }}</div>
        </div>
      </a>
      {{# } }}
  </div>
</aside>
</script>
</head>
<body class="order-detail">
  <header class="header">
    <h1>确认订单信息</h1>
    <a href="javascript:history.back();" class="button-left"><i class="fa fa-angle-left"></i></a>
  </header>
  
  <form id="orderForm" class="valid-form" action="${ctx}/u/order/create" method="post">
    <input type="hidden" name="orderType" value="${orderType}" />
  <%--
  <c:if test="${not empty parent}">
  <div class="parent-alert alert alert-warning mb-10">
    <img class="image-40 round" src="${parent.avatarThumbnail}">
    <span class="ml-10">${parent.nickname} 将成为您的上级服务商.</span>
  </div>
  </c:if>
  --%>
  
  <article>
    <div class="list-group">
      <div class="list-title">收件人信息</div>
      <div class="list-item">
        <div class="list-icon"><i class="fa fa-map-marker fs-24"></i></div>
        <div class="list-text fs-14 font-333 receiver-info">
          <c:if test="${empty address}">
            <span class="lh-30">您还未设置收货地址</span>
            <a class="btn-add-address btn orange btn-sm round-2 right width-180" href="javascript:;">设置收货地址</a>
          </c:if>
          <c:if test="${not empty address}">
            <div>${address.realname}<span class="right">${address.phone}</span></div>
            <div class="fs-14 font-777">${address.province} ${address.city} ${address.district} ${address.address}</div>
          </c:if>
        </div>
        <input type="hidden" id="addressId" name="addressId" value="${address.id}">
      </div>
    </div>
    
    <div class="list-group">
      <div class="list-title">商品信息</div>
      <div class="list-item">
        <img class="image-80 block mr-10" alt="" src="${product.image1Thumbnail}">
        <div class="list-text relative font-333">
          <div class="fs-14 lh-24">¥ ${product.title}</div>
          <div class="lh-24 fs-14 text-right font-orange">¥ ${product.price}</div>
          <div class="lh-24 fs-12 text-right font-gray">x ${quantity}</div>
          <input type="hidden" name="productId" value="${product.id}">
          <input type="hidden" name="quantity" value="${quantity}">
        </div>
      </div>
      <div class="list-item">
        <div class="list-text text-right">
          <%-- <div class="fs-12">合计：<span class="font-red">¥ ${product.price * quantity}</span></div> --%>
          <div class="fs-14">应付款： <span class="font-orange fs-16 bold">¥ ${product.price * quantity}</span></div>
        </div>
      </div>
    </div>
    
    <div class="list-group">
      <div class="list-title">请选择付款方式</div>
      <c:if test="${useOfflinePay}">
      <c:if test="${userRank == 'V0' || userRank == 'V1' || userRank == 'V2' || userRank == 'V3'}">
        <div class="list-item form-radio">
          <label class="list-text" for="isPayToPlatform0">线下转账</label>
          <div class="list-unit">
            <input id="isPayToPlatform0" type="radio" name="isPayToPlatform" value="false"${userRank == 'V2' || userRank == 'V1' || userRank == 'V0' ? ' checked="checked"' : ''}>
            <label class="i-checked" for="isPayToPlatform0"></label>
          </div>
        </div>
      </c:if>
      </c:if>
      <c:if test="${userRank == 'V0' || userRank == 'V1' || userRank == 'V2' || userRank == 'V3' || userRank == 'V4'}">
        <div class="list-item form-radio">
          <label class="list-text" for="isPayToPlatform1">余额支付</label>
          <div class="list-unit">
            <input id="isPayToPlatform1" type="radio" name="isPayToPlatform" value="true"${userRank == 'V4' ? ' checked="checked"' : ''}>
            <label class="i-checked" for="isPayToPlatform1"></label>
          </div>
        </div>
      </c:if>
    </div>

    <c:if test="${orderFill}">
    <div class="list-group">
      <div class="list-item">
        <div class="list-text">是否补订单</div>
        <div class="list-unit form-switch">
          <input type="hidden" name="_isOrderFill" value="false">
          <input type="checkbox" id="isOrderFill" name="isOrderFill">
          <label class="i-switch" for="isOrderFill"></label>
        </div>
      </div>
    </div>
    </c:if>
    
    <div class="list-group">
      <div class="list-title">请写下您的订单留言</div>
      <div class="list-item">
        <div class="list-text">
          <textarea name="buyerMemo" class="form-input" rows="2" placeholder="填写您的订单留言"></textarea>
        </div>
      </div>
    </div>
    
    <c:if test="${userRank == 'V0' && empty parent}">
      <div class="list-group">
        <div class="list-item">
          <label class="list-label">推荐人手机号</label>
          <div class="list-text">
            <input id="parentPhone" name="parentPhone" class="form-input" type="tel" value="${inviter.phone}" placeholder="输入推荐人服务商手机号">
            <input type="hidden" name="parentId" value="${inviter.id}">
          </div>
        </div>
        <div style="color: #8a6d3b; padding: 15px 30px 15px 15px;">
          <p>此手机号码为直属推荐人的手机号码，填写错误，则推荐关系不予以更改</p>
        </div>
      </div>
    </c:if>
    
    <div class="form-btn">
      <input id="btnSubmit" type="submit" value="确认订单" class="btn btn-block orange round-2">
    </div>
    
  </article>
  </form>
  
</body>
</html>
