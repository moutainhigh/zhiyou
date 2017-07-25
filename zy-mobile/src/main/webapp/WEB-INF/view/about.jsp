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
      <p>智优生物科技是一家专业从事健康预防科学产品的研发、生产、销售及服务为一体的高科技企业，以生物技术和生命科学为先导，涵盖医疗检查、预防治疗、健康卫生、营养保健、健身休闲等领域。致力于降低人类重大疾病发生率，改善民生、提高全民健康水平，为企业家、白领及不同领域民众定制专属健康产品，打造最符合个人需求的健康生活方式。</p>
      <p>智优生物科技（上海）有限公司研发、技术力量雄厚，拥有一批专业高素质的核心技术人才。在“健康中国”与“中国梦•健康梦”等国家政策带动下，智优生物将整合互联网时代的网络与技术优势，催生崭新的商业模式和不可估量的万亿市场。十年科研路，今朝启航时。智优生物团队在科技健康产业累计了多年经验和资源，在市场蓬勃之时，开始发力布局生态健康领域，推出划时代的产品——优检一生，希望为全人类防癌和最终消灭癌症作出巨大贡献。</p>
      <p>未来智优将倾力打造大健康产业生态链，以生物技术和生命科学为先导，致力于降低人类重大疾病的发生率，我们将推出千城万店计划，打造超过10000家一站式智优健康中心，全面导入尖端生物科技产品，成为中国最大的体外检测大数据库。为“中国梦•健康梦”的实现添砖加瓦！此外，智优商学院计划在兄弟单位——纵横商学院的协作下，一年投入2000万，培养10万智友，发展1000万优粉，同时培养30个千万富翁，300个百万富翁。</p>
    </div>

    <h2><i class="fa fa-phone font-blue"></i> 商务合作</h2>
    <div class="about-content">
      <h3>联系方式：</h3>
      <ul>
        <li>咨询热线：400-7282-999</li>
        <li>公司地址：上海市普陀区云岭东路609号汇银铭尊一号楼10楼</li>
      </ul>
    </div>
  </article>

</body>
</html>