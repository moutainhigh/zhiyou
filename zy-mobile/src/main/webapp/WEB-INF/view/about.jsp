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

<title>关于我们</title>
<%@ include file="/WEB-INF/view/include/head.jsp"%>
<link href="${stccdn}/css/about.css" rel="stylesheet" />
</head>

<body class="header-fixed">
  
  <header class="header">
    <h1>关于我们</h1>
    <a href="javascript:history.back(-1);" class="button-left"><i class="fa fa-angle-left"></i></a>
  </header>
  
  <article class="about">
     <h2><i class="icon icon-logo"></i> 关于智优生物</h2>
     <div class="about-content">
	 	<p>智优生物是xxxxxxxx。</p>
	 	<p>第二段介绍。</p>
	 	<p>第三段介绍。</p>
	 	<p>第四段介绍。</p>
     </div>
     
     <h2><i class="fa fa-phone font-blue"></i> 商务合作</h2>
     <div class="about-content">
         <h3>联系方式：</h3>
         <ul>
           <li>联系邮箱：3497804772@qq.com</li>
           <li>公司地址：普陀区云岭东路599弄609号汇银铭尊一号楼10楼</li>
           <li>在线客服QQ：3497804772</li>
         </ul>
     </div>
  </article>
 
</body>
</html>