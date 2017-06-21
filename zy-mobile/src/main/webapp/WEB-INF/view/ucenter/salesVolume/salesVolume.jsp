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

  <title>销量</title>
  <%@ include file="/WEB-INF/view/include/head.jsp"%>
  <style>
    body {background: #eeeeee}
    /*清除浮动代码*/
    .clearfloat:before, .clearfloat:after {
      content:"";
      display:table;
    }
    .clearfloat:after{
      clear:both;
      overflow:hidden;
    }
    .clearfloat{
      zoom:1;
    }
    .all {
      background: #fff !important;
      margin-top: 10px;
    }
   #echartTeamTwo,#echartTeamFor {
      width:100%;
      height:200px;
      background: #fff !important;
    }
    #echartTeamFor {
      height:250px;
    }
    #echartTeamAll {
      width:100%;
      height:200px;
      background: #f9f9f9 !important;
    }
    .teamAll {height: 50px;}
    .teamAll img {float: left;width: 50px;height: 50px;margin-left: 8px;}
    .teamAll span {
      float: left;line-height: 50px;margin-left: 20px;font-size: 18px;
      color: #838385;
    }
    .teamAll a,.teamAll span.paim {
      float: right;
      line-height: 50px;
      color: #6cb92d;
      margin-right: 15px;
    }
    .allList {
      border-bottom: 1px solid #e5e5e5;
    }
    .TeamName {
      float: left;
      width:33%;
      text-align: center;
    }
    .allName {padding-bottom: 20px;}
    .TeamName img {
      width:55%;
      margin-top:20px;
    }
    .TeamName span {
      display: block;
      margin-top:10px;
    }
    .lookDetil {
      display: block;
      width:100%;
      height:40px;
      text-align: center;
      line-height: 40px;
      color: #6cb92d;
      font-size: 15px;
    }
    .rankingAll {
      border-bottom: 1px solid #e5e5e5;
      width:100%;
      height:60px;
    }
    .rankingAll>span {
      float: left;
      display: block;;
      width:65px;
      height:100%;
      text-align: center;
      line-height:60px;
    }
    .rankingAll>img {
      float: left;
      width: 40px;
      height: 40px;
      margin-top: 10px;
      margin-bottom: 10px;
      margin-left: 15px;
      -webkit-border-radius:50%;
      -moz-border-radius:50%;
      border-radius:50%;
    }
    .ranking {
      float: left;
      width:120px;
      height:100%;
      text-align: center;
      line-height: 60px;
    }
    .rankingSpan {
      padding:0 8px 0 8px;
      color: #fff;
      font-size: 12px;
    }
    /*特级*/
    .must {
      background: #5d77e5;
    }
    .province {
      background: #6cb92d;
    }
    .city {
      background: #ffb558;
    }
    .VIP {
      background: #51c187;
    }
    .com {
      background: #91c7ae;
    }
    .allLast {
      margin-bottom: 10px;
    }
    .rankingBtn {
      float: right;
      width:80px;
      height:30px;
      margin-top: 15px;
      text-align: center;
      line-height: 30px;
      margin-right: 10px;
      border:1px solid #00d747;
      -webkit-border-radius:5px;
      -moz-border-radius:5px;
      border-radius:5px;
      color: #00d747;
    }
  </style>
</head>
<body>
<header class="header">
  <h1>销量</h1>
  <a href="${ctx}/u" class="button-left"><i class="fa fa-angle-left"></i></a>
</header>

<article>

  <div class="all" style="padding-bottom: 20px;">
    <div class="teamAll">
      <img src="${ctx}/peopleVolume.png"/>
      <span>个人销量</span>
    </div>
    <div id="echartTeamTwo"></div>
    <div id="echartTeamFor" style="margin-top: 20px;"></div>
  </div>

  <div class="all allLast">
    <div class="teamAll allList">
      <img src="${ctx}/findVolume.png"/>
      <span>直属团队进销单</span>
    </div>
    <c:forEach items="${dateMap.userList}" var="user" end="2">
      <div class="rankingAll">
        <img src="${user.avatar}" />
        <div class="ranking">
          <span>${user.nickname}</span>
          <c:if test="${user.userRank == 'V4'}"><span class="rankingSpan must">特级</span></c:if>
          <c:if test="${user.userRank == 'V3'}"><span class="rankingSpan province">省级</span></c:if>
          <c:if test="${user.userRank == 'V2'}"><span class="rankingSpan city">市级</span></c:if>
          <c:if test="${user.userRank == 'V1'}"><span class="rankingSpan VIP">VIP级</span></c:if>
          <c:if test="${user.userRank == 'V0'}"><span class="rankingSpan com">普通</span></c:if>
        </div>
        <span class="tel">${user.phone}</span>
        <a href="${ctx}/u/salesVolume/salesVolumeDetail?userId=${user.id}&userName=${user.nickname}"  class="rankingBtn">查看销量</a>
      </div>
    </c:forEach>
    <c:if test="${fn:length(dateMap.userList) > 3}">
      <a href="${ctx}/u/salesVolume/subordinateSubordinateDetail" class="lookDetil">更多详情>></a>
    </c:if>
  </div>

  <c:if test="${dateMap.userRank == 'V4'}">
    <div class="all allLast">
      <div class="teamAll allList">
        <img src="${ctx}/mustVolume.png"/>
        <span>直属特级进销单</span>
      </div>
      <c:forEach items="${dateMap.v4List}" var="user" end="2">
        <div class="rankingAll">
          <img src="${user.avatar}" />
          <div class="ranking">
            <span>${user.nickname}</span>
          </div>
          <span class="tel">${user.phone}</span>
          <a href="${ctx}/u/salesVolume/salesVolumeDetail?userId=${user.id}&userName=${user.nickname}"  class="rankingBtn">查看销量</a>
        </div>
      </c:forEach>
      <c:if test="${fn:length(dateMap.v4List) > 3}">
        <a href="${ctx}/u/salesVolume/directlyUnderGradeDetail" class="lookDetil">更多详情>></a>
      </c:if>
    </div>
  </c:if>

  <c:if test="${dateMap.userRank == 'V4'}">
    <div class="all" style="background: #f9f9f9 !important;padding-bottom: 20px;">
      <div class="teamAll">
        <img src="${ctx}/teamVolume.png"/>
        <span>团队销量</span>
      </div>
      <div id="echartTeamAll"></div>
    </div>
  </c:if>
</article>
<script src="${ctx}/echarts.min.js"></script>
<script type="text/javascript">
  var svArray = "${dateMap.salesVolumeData}";
  var array = svArray.split(",");

  var sArray = "${dateMap.shipmentData}";
  var arrayT = sArray.split(",");

  var mbolSize = 20;
  // 基于准备好的dom，初始化echarts实例
  var myChart = echarts.init(document.getElementById('echartTeamTwo'));
  // 指定图表的配置项和数据
  optionTwo = {
    tooltip : {
      trigger: 'axis',
      axisPointer : {            // 坐标轴指示器，坐标轴触发有效
        type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
      }
    },
    legend: {
      data:['个人进货量','个人销货量']
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      containLabel: true
    },
    xAxis : [
      {
        type : 'category',
        data : ['一月','二月','三月','四月','五月','六月','七月','八月','九月','十月','十一月','十二月']
      }
    ],
    yAxis : [
      {
        type : 'value'
      }
    ],
    series : [
      {
        name:'个人进货量',
        type:'bar',
        stack: '广告',
        itemStyle: {
          normal: {
            color:'#6cb92d'
          }
        },
        data:array
      },

      {
        name:'个人销货量',
        type:'bar',
        itemStyle: {
          normal: {
            color:'#add889'
          }
        },
        data:arrayT
      }
    ]
  };
  // 使用刚指定的配置项和数据显示图表。
  myChart.setOption(optionTwo);
</script>

<script type="text/javascript">
  var svArray = "${dateMap.svData}";
  var array = svArray.split(",");

  var sArray = "${dateMap.sData}";
  var arrayT = sArray.split(",");

  var mbolSize =40;
  // 基于准备好的dom，初始化echarts实例
  var myChart = echarts.init(document.getElementById('echartTeamFor'));
  // 指定图表的配置项和数据
  optionFor = {
    tooltip: {
      trigger: 'axis'
    },
    legend: {
      data:['进货量环比','销货量环比']
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      containLabel: true
    },

    xAxis: {
      type: 'category',
      boundaryGap: false,
      data : ['一月','二月','三月','四月','五月','六月','七月','八月','九月','十月','十一月','十二月']
    },
    yAxis: {
      type: 'value'
    },
    series: [
      {
        name:'进货量环比',
        type:'line',
        stack: '总量',
        itemStyle: {
          normal: {
            color:'#6cb92d'
          }
        },
        data:array
      },
      {
        name:'销货量环比',
        type:'line',
        stack: '总量',
//        label: {
//          normal: {
//            show: true,
//            position: 'top'
//          }
//        },
        itemStyle: {
          normal: {
            color:'#fc4e33'
          }
        },
        data:arrayT
      }
    ]
  };
  // 使用刚指定的配置项和数据显示图表。
  myChart.setOption(optionFor);
</script>
<script type="text/javascript">
  var svArray = "${dateMap.salesVolumeTeamData}";
  var array = svArray.split(",");

  var sArray = "${dateMap.shipmentTeamData}";
  var arrayT = sArray.split(",");

  var mbolSize = 20;
  // 基于准备好的dom，初始化echarts实例
  var myChart = echarts.init(document.getElementById('echartTeamAll'));
  // 指定图表的配置项和数据
  option = {
    tooltip : {
      trigger: 'axis',
      axisPointer : {            // 坐标轴指示器，坐标轴触发有效
        type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
      }
    },
    legend: {
      data:['团队进货量','团队销货量']
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      containLabel: true
    },
    xAxis : [
      {
        type : 'category',
        data : ['一月','二月','三月','四月','五月','六月','七月','八月','九月','十月','十一月','十二月']
      }
    ],
    yAxis : [
      {
        type : 'value'
      }
    ],
    series : [
      {
        name:'团队进货量',
        type:'bar',
        stack: '广告',
        itemStyle: {
          normal: {
            color:'#6cb92d'
          }
        },
        data:array
      },

      {
        name:'团队销货量',
        type:'bar',
        itemStyle: {
          normal: {
            color:'#add889'
          }
        },
        data:arrayT
      }
    ]
  };
  // 使用刚指定的配置项和数据显示图表。
  myChart.setOption(option);
</script>
<%@ include file="/WEB-INF/view/include/footer.jsp"%>
</body>
</html>
