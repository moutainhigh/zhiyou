<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/view/include/taglib.jsp" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="Cache-Control" content="no-store"/>
<meta http-equiv="Pragma" content="no-cache"/>
<meta http-equiv="Expires" content="0"/>
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="apple-mobile-web-app-status-bar-style" content="black">
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">

<title>积分钱包</title>
<%@ include file="/WEB-INF/view/include/head.jsp" %>
<link rel="stylesheet" href="${stccdn}/css/ucenter/account.css">
  <link rel="stylesheet" href="${ctx}/style.css">
  <style>
    @charset "utf-8";
    html, body, div, p, ul, li, dl, dt, dd, h1, h2, h3, h4, h5, h6, form, input, select, button, textarea, iframe, table, th, td { margin: 0; padding: 0; }
    body { font-family:"Microsoft YaHei";background: #fff; font-size: 26px; color: #151515; margin: 0; padding: 0; }
    img { border: 0 none; vertical-align: top; display:inline-block; -ms-interpolation-mode: bicubic; image-rendering:optimizeQuality; }
    button { cursor: pointer; border:0 none; }
    input{ border:0 none; background:transparent; }
    textarea{ resize: none; border:0 none; }
    ul, li { list-style-type: none; }
    table{ border-collapse:collapse; border-spacing:0; }
    i, em, cite { font-style: normal; }
    p{word-wrap:break-word; }
    body, input, select, button,textarea{ outline: none; }
    a { display:block; }
    a,input,textarea,p,span,h2,em,li,div{text-decoration: none; }
    input,textarea{font-family:"Microsoft YaHei"}
    .clearfloat:before, .clearfloat:after {
      content:"";  display:table;
    }
    .clearfloat:after{
      clear:both;  overflow:hidden;
    }
    .clearfloat{
      zoom:1;
    }
    .bgSum {
      width:100%;
      height:120px;
      background: url("${ctx}/bgSum.png") top center ;
      background-size: cover;
      padding: 30px;
      padding-top: 20px;
    }
    .bgSum p {
      width: 100%;
      font-size: 16px !important;
      margin-bottom:10px;
      color: #909192;
    }
    .bgSum p.percent {
      width: 45%;
      float: left;
      font-size: 16px !important;
      margin-bottom:10px;
      color: #909192;
    }
    .bgSum p.last {
      margin-left:10%;
    }
    .bgSum p span {
      float:right;
      color: #49494a;
      font-size: 16px !important;
    }
    .bgSum p span.percentNum {
      margin-right: 10px;
    }
    #echartFir {
      margin-top:20px;
      padding: 10px;
      width:100%;
      height:300px;
      color: #49494a;
    }
    .all {
      position: relative;
      padding:10px 15px 0 15px;
    }
    .all img {
      width:100%;
      height:135px;
    }
    .percentNum {
      height:135px;
      position: absolute;
      top:20px;
      left:15px;
    }
    .percentNumDiv {
      width:98%;
      margin-left:1%;
      height:80px;
      border-bottom: 1px solid #aaabae;
    }
    .fontImg {
      width:100%;
      height:40px;
      line-height: 35px;
      color: #646565;
      font-size:17px;
    }
    .fontImg span {
      float: left;
      margin-left:10px;
    }
    .fontImg img {
      float: left;
      width:35px;
      height:35px;
      margin-left:10px;
    }
    .fontImg p{
      float: left;
      width:50%;
      font-size: 14px;
    }
    .fontImg p span {
      margin-left: 10px;
    }
    .look {
      width:100%;
      color:#32b1d2;
      font-size: 16px;
      text-align: center;
      line-height: 40px;
    }
  </style>
  <script>
	  $(function() {
      $('.header .button-popmenu').click(function(){
        $('.header-popmenu').toggle(300);
      });
	  });
  </script>
</head>

<body class="account">
<header class="header">
  <a href="${ctx}/u" class="button-left"><i class="fa fa-angle-left"></i></a>
  <a href="javascript:;" class="button-right button-popmenu"><i class="fa fa-ellipsis-h"></i></a>
  <nav class="header-popmenu hide">
    <a id="btnCancel" href="${ctx}/u/bankCard" style="background-color: #3d7878;"><i class="icon icon-account-card"></i> 银行卡</a>
  </nav>
</header>
<article>
  <nav class="flex account-nav pb-30">
    <a class="flex-1 text-center" href="${ctx}/u/money?currencyType=0">
      <div class="mb-10"><i class="icon icon-account-umoney"></i></div>
      <div class="fs-16 lh-24">U币</div>
      <div class="fs-12 lh-20 font-ccc">${dataMap.amount1}</div>
    </a>
    <a class="flex-1 text-center" href="${ctx}/u/money?currencyType=1">
      <div class="mb-10"><i class="icon icon-account-money"></i></div>
      <div class="fs-16 lh-24">积分</div>
      <div class="fs-12 lh-20 font-ccc">${dataMap.amount2}</div>
    </a>
    <a class="flex-1 text-center" href="${ctx}/u/money?currencyType=2">
      <div class="mb-10"><i class="icon icon-account-option"></i></div>
      <div class="fs-16 lh-24">期权</div>
      <div class="fs-12 lh-20 font-ccc">${dataMap.amount3}</div>
    </a>
  </nav>
  <div class="bgSum clearfloat">
      <p>上月收入：<span>¥${dataMap.BM}</span></p>
      <p>当前累计：<span>¥${dataMap.TOT}</span></p>
      <p class="percent">上月环比：<span>${dataMap.QoQ}%</span></p>
      <p class="percent last">上月同比：<span>${dataMap.YoY}%</span></p>
  </div>
  <div id="echartFir"></div>
  <div id="last" style="width: 100%;height: 400px;margin-top: 20px;padding: 10px;"></div>

    <div class="all">
      <img src="${ctx}/percent.png" />
      <div class="percentNum">
           <div class="percentNumDiv">
                 <div class="fontImg clearfloat">
                   <img src="${ctx}/01.png" />
                   <span>订单收益</span>
                 </div>
               <div class="fontImg clearfloat">
                    <p><span>上月收益：</span>￥${dataMap.ord[0]}</p>
                    <p><span>累计收益：</span>￥${dataMap.ord[1]}</p>
               </div>
           </div>
           <a href="#" class="look">查看详情</a>
      </div>
    </div>

  <div class="all">
    <img src="${ctx}/percent.png" />
    <div class="percentNum">
      <div class="percentNumDiv">
        <div class="fontImg clearfloat">
          <img src="${ctx}/02.png" />
          <span>返利奖</span>
        </div>
        <div class="fontImg clearfloat">
          <p><span>上月收益：</span>￥${dataMap.red[0]}</p>
          <p><span>累计收益：</span>￥${dataMap.red[1]}</p>
        </div>
      </div>
      <a href="#" class="look">查看详情</a>
    </div>
  </div>

  <div class="all">
    <img src="${ctx}/percent.png" />
    <div class="percentNum">
      <div class="percentNumDiv">
        <div class="fontImg clearfloat">
          <img src="${ctx}/03.png" />
          <span>数据奖</span>
        </div>
        <div class="fontImg clearfloat">
          <p><span>上月收益：</span>￥${dataMap.dat[0]}</p>
          <p><span>累计收益：</span>￥${dataMap.dat[1]}</p>
        </div>
      </div>
      <a href="#" class="look">查看详情</a>
    </div>
  </div>
  <div class="all">
    <img src="${ctx}/percent.png" />
    <div class="percentNum">
      <div class="percentNumDiv">
        <div class="fontImg clearfloat">
          <img src="${ctx}/04.png" />
          <span>销量奖</span>
        </div>
        <div class="fontImg clearfloat">
          <p><span>上月收益：</span>￥${dataMap.sal[0]}</p>
          <p><span>累计收益：</span>￥${dataMap.sal[1]}</p>
        </div>
      </div>
      <a href="#" class="look">查看详情</a>
    </div>
  </div>
  <div class="all">
    <img src="${ctx}/percent.png" />
    <div class="percentNum">
      <div class="percentNumDiv">
        <div class="fontImg clearfloat">
          <img src="${ctx}/05.png" />
          <span>特级平级奖</span>
        </div>
        <div class="fontImg clearfloat">
          <p><span>上月收益：</span>￥${dataMap.ftl[0]}</p>
          <p><span>累计收益：</span>￥${dataMap.ftl[1]}</p>
        </div>
      </div>
      <a href="#" class="look">查看详情</a>
    </div>
  </div>
  <div class="all">
    <img src="${ctx}/percent.png" />
    <div class="percentNum">
      <div class="percentNumDiv">
        <div class="fontImg clearfloat">
          <img src="${ctx}/06.png" />
          <span>董事贡献奖</span>
        </div>
        <div class="fontImg clearfloat">
          <p><span>上月收益：</span>￥${dataMap.sen[0]}</p>
          <p><span>累计收益：</span>￥${dataMap.sen[1]}</p>
        </div>
      </div>
      <a href="#" class="look">查看详情</a>
    </div>
  </div>
  <div class="all">
    <img src="${ctx}/percent.png" />
    <div class="percentNum">
      <div class="percentNumDiv">
        <div class="fontImg clearfloat">
          <img src="${ctx}/07.png" />
          <span>平级推荐奖</span>
        </div>
        <div class="fontImg clearfloat">
          <p><span>上月收益：</span>￥${dataMap.ltl[0]}</p>
          <p><span>累计收益：</span>￥${dataMap.ltl[1]}</p>
        </div>
      </div>
      <a href="#" class="look">查看详情</a>
    </div>
  </div>
  <div class="all">
    <img src="${ctx}/percent.png" />
    <div class="percentNum">
      <div class="percentNumDiv">
        <div class="fontImg clearfloat">
          <img src="${ctx}/08.png" />
          <span>特级推荐奖</span>
        </div>
        <div class="fontImg clearfloat">
          <p><span>上月收益：</span>￥${dataMap.rec[0]}</p>
          <p><span>累计收益：</span>￥${dataMap.rec[1]}</p>
        </div>
      </div>
      <a href="${ctx}/u/profit/orderRevenue?type=2" class="look">查看详情</a>
    </div>
  </div>
</article>
<script src="${ctx}/echarts.min.js"></script>
<script type="text/javascript">
  var array=${map.PL};
  $(function(){
    $(".percentNum").css("width",$(".all img").css("width"));
  })
  var symbolSize = 20;
  // 基于准备好的dom，初始化echarts实例
  var myChart = echarts.init(document.getElementById('echartFir'));
  // 指定图表的配置项和数据
  option = {
    title: {
      text: '年度收益趋势图'
    },
    tooltip: {
      trigger: 'axis'
    },
    toolbox: {
      show : true,
      feature : {
        mark : {show: false},
        dataView : {show: true, readOnly: false},
        magicType : {
          show: true,
          type: ['pie', 'funnel']
        },

        saveAsImage : {show: true}
      }
    },
    xAxis:  {
      type: 'category',
      splitLine: {
        show: false
      },
      boundaryGap: false,
      data: ['一月','二月','三月','四月','五月','六月','七月','八月','九月','十月','十一月','十二月']
    },
    yAxis: {
      type: 'value',
      axisLabel: {
        formatter: '{value}'
      }
    },
    series: [
      {
        name:'月收益',
        type:'line',
        data:array,
        markPoint: {
          data: [
            {type: 'max', name: '1'},
            {type: 'min', name: '2'},
          ]
        }
      }
    ]
  };
  // 使用刚指定的配置项和数据显示图表。
  myChart.setOption(option);
</script>

<script>
  var array=${map.pie};
  var myChartSec = echarts.init(document.getElementById('last'));
  optionSec = {
    title : {
      text: '累计收益占比'
    },
    tooltip : {
      trigger: 'item',
      formatter: "{a} <br/>{b} : {c} ({d}%)"
    },
    legend: {
      x : 'center',
      y : 'bottom',
      data:['特级平级奖','订单收益','返利奖','数据奖','董事贡献奖','特级推荐奖','平级推荐奖','销量奖']
    },
    toolbox: {
      show : true,
      feature : {
        mark : {show: false},
        dataView : {show: true, readOnly: false},
        magicType : {
          show: true,
          type: ['pie', 'funnel']
        },
        saveAsImage : {show: true}
      }
    },
    calculable : true,
    series : [
      {
        name:'',
        type:'pie',
        radius : [20, 110],
        center : ['25%', '50%'],
        roseType : 'radius',
        label: {
          normal: {
            show: false
          },
          emphasis: {
            show: true
          }
        },
        lableLine: {
          normal: {
            show: false
          },
          emphasis: {
            show: true
          }
        }
      },
      {
        name:'累计收益',
        type:'pie',
        radius : [30, 110],
        center : ['50%', '50%'],
        roseType : 'area',
        data:[
          {value:array[0], name:'特级平级奖'},
          {value:array[1], name:'订单收益'},
          {value:array[2], name:'返利奖'},
          {value:array[3], name:'数据奖'},
          {value:array[4], name:'董事贡献奖'},
          {value:array[5], name:'特级推荐奖'},
          {value:array[6], name:'平级推荐奖'},
          {value:array[7], name:'销量奖'}
        ]
      }
    ]
  };
  myChartSec.setOption(optionSec);
  $("#echartFir").css("background","url('${ctx}/bg.png') top center");
  $("#last").css("background","url('${ctx}/bg.png') top center");

</script>

</body>

</html>
