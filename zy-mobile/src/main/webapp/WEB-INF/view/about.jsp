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
    <h2><i class="icon icon-logo"></i> 关于${sys}</h2>
    <div class="about-content">
      <p>竹印工坊生态科技是一家专注生态健康科学产品的研发、生产、销售及服务为一体的生态环保企业，以生态环境和安全健康为先导，未来将涵盖、健康卫生、营养保健、健身休闲等领域。致力于减少人类重大疾病发生率，改善民生、提高全民健康水平，为家庭领域民众定制专属健康产品，打造最符合个人需求的健康生活方式。</p>
      <p>合肥竹印工坊生态科技有限公司研发、技术力量雄厚，拥有一批专业高素质的核心技术人才。在“健康中国”与“中国梦•健康梦”等国家政策带动下，竹印工坊将整合互联网时代的网络与技术优势，催生崭新的商业模式和不可估量的万亿市场。竹妈妈团队在科技健康产业累计了多年经验和资源，在市场蓬勃之时，开始发力布局生态健康领域，推出造福人类和地球的产品——竹妈妈纯竹纸，希望为全中国家庭带来更健康环保的生活用纸，为地球环保作出巨大贡献。</p>
      <p>未来竹印工坊将倾力打造大健康产业生态链，以生态科技与生物技术和生命科学为先导，致力于提高人类健康生活，改善地球生活环境，我们将推出千城万店计划，以个人为中心即为移动店铺辐射全国市场，推动国家号召互联网的人人创业时代，带动1000万人加入环保事业，影响10亿人的健康生活理念，公司将全面导入尖端生态产品与生物科技产品，成为中国最大健康大数据库。为“中国梦•健康梦”的实现添砖加瓦！</p>
    </div>

    <h2><i class="fa fa-phone font-blue"></i> 商务合作</h2>
    <div class="about-content">
      <h3>联系方式：</h3>
      <ul>
        <li>联系人：杨鹏 18949515151(微信)</li>
        <li>公司地址：上海市普陀区云岭东路609号汇银铭尊一号楼10楼</li>
      </ul>
    </div>
  </article>

</body>
</html>