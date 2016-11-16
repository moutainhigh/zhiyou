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

<title>支出 - ${type}</title>
<%@ include file="/WEB-INF/view/include/head.jsp"%>
<%@ include file="/WEB-INF/view/include/pageload.jsp"%>
<script type="text/javascript">
  function getUrl() {
    return '${ctx}/u/account/transferOut?type=${type.ordinal()}';
  }
  
  function buildRow(row) {
    var html = '<div class="list-item" data-id="' + row.id + '">' 
             +   '<img class="image-40 round mr-10" src="' + row.toUser.avatarThumbnail + '">'
             +   '<div class="list-text pl-5">'
             +     '<div class="transfer-nickname fs-14">' + row.toUser.nickname + '</div>'
             +     '<div class="fs-12 font-999">' + row.createdTimeLabel + '</div>' 
             +   '</div>' 
             +   '<div class="list-unit width-100 text-right">' 
             +     '<div class="transfer-amount currency-out">' + row.amountLabel + '</div>' 
             +     '<div class="fs-12 font-999">状态: ' + row.transferStatus + '</div>';
         <%--
         if(row.transferStatus == '待转账'){
           html += '<a class="btn-transfer btn btn-sm red" href="javascript:;">余额转账</a>';
         }
         --%>
         html += '</div>'
             + '</div>';
    return html;
  }
  
  $(function(){
    $('body').on('.btn-transfer', 'click', function(){
      var $transfer =  $(this).parents('.list-item');
      var id = $transfer.attr('data-id');
      var nickname = $transfer.find('.transfer-nickname').text();
      var amount = $transfer.find('.transfer-amount').text();
      $.dialog({
        content : '您确定要将' + amount + '元${type}转账给' + nickname + '吗?',
        callback : function(index){
          if(index == 1) {
            $.ajax({
              url : '${ctx}/u/account/transfer',
              data : {
                id : id
              },
              dataType : 'json',
              type : 'POST',
              success : function(result){
                if(result.code == 0){
                  messageFlash('${result.message}');
                } else {
                  messageAlert('${result.message}');
                }
              }
            });
            return true;
          }
        }
      });
    });
  });
</script>
</head>
 
<body class="account-list">
  <article class="page-wrap">
    <header class="header">
      <h1>支出 - ${type}</h1>
      <a href="${ctx}/u/account" class="button-left"><i class="fa fa-angle-left"></i></a>
    </header>
  
    <div class="page-inner">
      <div class="page-list list-group mb-0">
      </div>
    </div>
  </article>
  <%@ include file="/WEB-INF/view/include/footer.jsp"%>
</body>
</html>
