<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/view/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text; charset=utf-8">
<meta http-equiv="Cache-Control" content="no-store" />
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Expires" content="0" />
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="apple-mobile-web-app-status-bar-style" content="black">
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
<meta name="keywords" content="微信分销" />
<meta name="description" content="团队成员信息" />

<title>团队成员信息</title>
<%@ include file="/WEB-INF/view/include/head.jsp"%>
<script>
  $(function() {

  });
</script>
</head>
<body>
  <header class="header">
    <h1>团队成员信息</h1>
    <a href="javascript:history.back();" class="button-left"><i class="fa fa-angle-left"></i></a>
  </header>

  <article>
    <div class="list-title">成员资料</div>
    <div class="list-group">
      <div class="list-item">
        <h3>姓名</h3>
        <div>
          <span>${inviteUser.realname}</span>
        </div>
      </div>
      <div class="list-item">
        <h3>手机号</h3>
        <div>
          <span>${inviteUser.phone}</span>
        </div>
      </div>
      <div class="list-item">
        <h3>代理等级</h3>
        <div>
          <span>${inviteUser.userRank}</span>
        </div>
      </div>
    </div>

    <div class="list-title">销售信息</div>
    <div class="list-group">
      <div class="list-item">
        <h3>订单数</h3>
        <div>
          <span>${orderCount}</span>
        </div>
      </div>
      <div class="list-item">
        <h3>销售额</h3>
        <div>
          <span>${empty amount ? 0 : amount}</span>
        </div>
      </div>
    </div>
  </article>

</body>
</html>
