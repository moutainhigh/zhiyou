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

  <title>我的团队</title>
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
    #echartTeam,#echartTeamTwo,#echartTeamFor {
      width:100%;
      height:200px;
      background: #fff !important;
    }
    .teamAll {height: 50px;}
    .teamAll img {float: left;width: 40px;height: 40px;margin-left: 8px;margin-top: 5px;}
    .teamAll span {
      float: left;line-height: 50px;margin-left: 0px;font-size: 18px;
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
      border-radius: 50%;
      -webkit-border-radius: 50%;
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
      height:40px;
      border-radius: 50%;
      -webkit-border-radius: 50%;
      margin-top: 10px;
      margin-bottom: 10px;
    }
    .ranking {
      float: left;
      width:130px;
      height:100%;
      text-align: center;
      line-height: 60px;
    }
    .rankingSpan {
      padding:0 8px 0 8px;
      color: #fff;
      font-size: 12px;
    }
    /*color:['#5d77e5','#ef7b54','#ffb558','#51c187'],*/
    /*特级*/
    .must {
      background: #ffcd48;
    }
    .province {
      background: #7ed1df;
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
    .tel {
      float: left;
      margin-right: 10px;
      color: #303134;
      line-height: 60px;
    }
    .teamAll span#teamNewCount{
      margin-left: 0;
      font-size: 12px;
      color: #6cb92d;
      line-height: 50px;
    }
    .tel img {
      float: left;
      margin-top: 22px;
    }
    @media (device-height:568px) and (-webkit-min-device-pixel-ratio:2){/* 兼容iphone5 */
      .rankingAll>span{
        width:55px;
      }
      .ranking {
        width:120px;
      }
    }
  </style>
</head>
<body>
<header class="header">
  <h1>我的${title}团队</h1>
  <a href="${ctx}/u" class="button-left"><i class="fa fa-angle-left"></i></a>
</header>

<article>
  <c:if test="${dataMap.flag != null}">
    <div class="all" >
      <div class="teamAll">
        <img src="${ctx}/images/teamNew.png"/>
        <span>团队总人数</span><span id="teamNewCount"></span>
        <c:if test="${dataMap.flag eq 'T'}"> <a href="${ctx}/u/team/findDirectlySup?productType=${productType}">直属特级详情>></a></c:if>
      </div>
      <div id="echartTeam"></div>
    </div>
  </c:if>
  <div class="all" >
    <div class="teamAll">
      <img src="${ctx}/images/team3.png"/>
      <span>直属团队</span>
      <a href="${ctx}/u/team/teamDetail?productType=${productType}">直属详情>></a>
    </div>
    <div id="echartTeamTwo"></div>
  </div>

  <div class="all" >
    <div class="teamAll">
      <img src="${ctx}/images/team5.png"/>
      <span>团队新成员</span>
      <a href="${ctx}/u/team/teamNew?productType=${productType}">新成员详情>></a>
    </div>
    <div id="echartTeamFor"></div>
  </div>

<c:if test="${dataMap.flag != null}">
  <c:if test="${dataMap.flag eq 'T'}">
    <div class="all allName clearfloat">
      <div class="teamAll allList">
        <img src="${ctx}/images/team4.png"/>
        <span>新晋直属特级</span>
      </div>
      <c:forEach items="${dataMap.mynT}" var="tlist">
        <div class="TeamName">
          <img src="${tlist.avatar}" />
          <span>${tlist.nickname}</span>
          <a href="tel:${tlist.phone}">
              <span>${tlist.phone}</span>
          </a>
        </div>
      </c:forEach>
    </div>
  </c:if>
</c:if>

  <%--<div class="all">--%>
    <%--<div class="teamAll allList">--%>
      <%--<img src="${ctx}/team6.png"/>--%>
      <%--<span>新晋排名</span>--%>
      <%--<span class="paim">我的排名：${dataMap.myRank}</span>--%>
    <%--</div>--%>
    <%--<c:forEach items="${dataMap.rankList}" var="udto" begin="0" end="2">--%>
      <%--<div class="rankingAll">--%>
        <%--<span>${udto.rank}</span>--%>
        <%--<img src="${udto.avatar}" />--%>
        <%--<div class="ranking">--%>
          <%--<span>${udto.nickname}</span>--%>
          <%--<span class="rank">(${udto.num}人)</span>--%>
        <%--</div>--%>
        <%--<a href="tel:${v0user.phone}" class="tel"><img src="${ctx}/tel.png" style="width: 15px;height: 15px;padding-right: 5px">${v0user.phone}</a>--%>
        <%--<span class="tel">${udto.phone}</span>--%>
      <%--</div>--%>
    <%--</c:forEach>--%>
    <%--<c:if test="${fn:length(dataMap.rankList)>3}">--%>
      <%--<a href="${ctx}/u/team/teamRank" class="lookDetil">更多详情>></a>--%>
    <%--</c:if>--%>
  <%--</div>--%>

  <div class="all allLast">
    <div class="teamAll allList">
      <img src="${ctx}/images/team7.png"/>
      <span>活跃度：${dataMap.actPer}%</span>
    </div>
    <div class="teamAll allList">
      <img src="${ctx}/images/team1.png"/>
      <span>沉睡成员</span>
    </div>
    <c:forEach items="${dataMap.act}" var="user" begin="0" end="2" varStatus="index">
      <c:if test="${user.userRank.level!=0}">
      <div class="rankingAll">
        <span>${index.index+1}</span>
        <img src="${user.avatar}" />
        <div class="ranking">
          <span>${user.nickname}</span>
          <span class="rankingSpan ${user.userRank.level==4?"must":user.userRank.level==3?"province":user.userRank.level==2?"city":user.userRank.level==1?"VIP":user.userRank.level==0?"com":""}">${user.userRank.level==4?"特级":user.userRank.level==3?"省级":user.userRank.level==2?"市级":user.userRank.level==1?"VIP":user.userRank.level==0?"普通":""}</span>
        </div>
        <a href="tel:${user.phone}" class="tel"><img src="${ctx}/images/tel.png" style="width: 15px;height: 15px;padding-right: 5px"><span class="tel">${user.phone}</span></a>

      </div>
      </c:if>
    </c:forEach>
    <a href="${ctx}/u/team/teamSleep?productType=${productType}" class="lookDetil">查看详情>></a>
  </div>
</article>
<script src="${ctx}/js/echarts.min.js"></script>
<script type="text/javascript">

  var counts = "${dataMap.TTot}";
  var array=counts.split(",");
  var mbolSize = 20;
  var arrayNum=parseInt(array[0])+parseInt(array[1])+parseInt(array[2])+parseInt(array[3]);
  $("#teamNewCount").text("("+arrayNum+"人)");
  if(arrayNum==0){
    arrayNum=1;
  }
  // 基于准备好的dom，初始化echarts实例
  var myChart = echarts.init(document.getElementById('echartTeam'));
  // 指定图表的配置项和数据
  option = {
    tooltip: {
      trigger: 'item',
      formatter: "{a} <br/>{b}: {c}人",
    },
//      toolbox: {
//        show : true,
//        feature : {
//          mark : {show: false},
//          dataView : {show: true, readOnly: false},
//          magicType : {
//            show: true,
//            type: ['pie', 'funnel']
//          },
//
//          saveAsImage : {show: true}
//        }
//      },
    series: [
      {
        name:'团队总人数',
        type:'pie',
        radius: ['40%', '55%'],
        color:['#ffcd48','#7ed1df','#ffb558','#51c187'],
        data:[
          {value:array[0], name:'特级服务商('+Math.round(array[0]/arrayNum*100*100)/100+'%)'},
          {value:array[1], name:'省级服务商('+Math.round(array[1]/arrayNum*100*100)/100+'%)'},
          {value:array[2], name:'市级服务商('+Math.round(array[2]/arrayNum*100*100)/100+'%)'},
          {value:array[3], name:'VIP服务商('+Math.round(array[3]/arrayNum*100*100)/100+'%)'}
        ]
      },
//        {
//          name:'团队总人数',
//          type:'pie',
//          radius: ['40%', '55%'],
//          label: {
//            normal: {
//              position: 'inside',
//              formatter: '{d}%',
//              textStyle: {
//                color: '#fff'
//              }
//            }
//          },
//          data:[
//            {value:array[0], name:'特级服务商'},
//            {value:array[1], name:'省级服务商'},
//            {value:array[2], name:'市级服务商'},
//            {value:array[3], name:'VIP服务商'}
//          ]
//        }
    ]
  };
  // 使用刚指定的配置项和数据显示图表。
  myChart.setOption(option);
</script>
<script type="text/javascript">
  var counts = "${dataMap.DTot}";
  var array=counts.split(",");
  var arrayNum=parseInt(array[0])+parseInt(array[1])+parseInt(array[2])+parseInt(array[3]);
//  var arrayNum=parseInt(array[0])+parseInt(array[1])+parseInt(array[2])+parseInt(array[3])+parseInt(array[4]);
  if(arrayNum==0){
    arrayNum=1;
  }
  var mbolSize = 20;
  // 基于准备好的dom，初始化echarts实例
  var myChart = echarts.init(document.getElementById('echartTeamTwo'));
  // 指定图表的配置项和数据
  optionTwo = {
//      toolbox: {
//        show : true,
//        feature : {
//          mark : {show: false},
//          dataView : {show: true, readOnly: false},
//          magicType : {
//            show: true,
//            type: ['pie', 'funnel']
//          },
//          saveAsImage : {show: true}
//        }
//      },
    tooltip : {
      trigger: 'item',
      formatter: "{a} <br/>{b} : {c}人"
    },
    series : [
      {
        name: '直属团队',
        type: 'pie',
        radius: '55%',
        center: ['50%', '50%'],
        color:['#ffcd48','#7ed1df','#ffb558','#51c187'],
//        color:['#ffcd48','#7ed1df','#ffb558','#51c187','#91c7ae'],
        data: [
          {value: array[0], name: '特级服务商('+Math.round(array[0]/arrayNum*100*100)/100+'%)'},
          {value: array[1], name: '省级服务商('+Math.round(array[1]/arrayNum*100*100)/100+'%)'},
          {value: array[2], name: '市级服务商('+Math.round(array[2]/arrayNum*100*100)/100+'%)'},
          {value: array[3], name: 'VIP服务商('+Math.round(array[3]/arrayNum*100*100)/100+'%)'}
//          {value: array[4], name: '普通服务商('+Math.round(array[4]/arrayNum*100*100)/100+'%)'}
        ],
        itemStyle: {
          emphasis: {
            shadowBlur: 10,
            shadowOffsetX: 0,
            shadowColor: 'rgba(0, 0, 0, 0.5)'
          }
        },
      },
//        {
//          name: '直属团队',
//          type: 'pie',
//          radius : '55%',
//          center: ['50%', '50%'],
//          label: {
//            normal: {
//              position: 'inside',
//              formatter: '{d}%',
//              textStyle: {
//                color: '#fff'
//              }
//            }
//          },
//          data:[
//            {value:array[0], name:'特级服务商'},
//            {value:array[1], name:'省级服务商'},
//            {value:array[2], name:'市级服务商'},
//            {value:array[3], name:'VIP服务商'},
//            {value:array[4], name:'普通服务商'}
//          ],
//          itemStyle: {
//            emphasis: {
//              shadowBlur: 10,
//              shadowOffsetX: 0,
//              shadowColor: 'rgba(0, 0, 0, 0.5)'
//            }
//          },
//
//        }
    ]
  };
  // 使用刚指定的配置项和数据显示图表。
  myChart.setOption(optionTwo);
</script>
<script type="text/javascript">
  var counts = "${dataMap.MTot}";
  var array=counts.split(",");
  var arrayNum=parseInt(array[0])+parseInt(array[1])+parseInt(array[2])+parseInt(array[3]);
//  var arrayNum=parseInt(array[0])+parseInt(array[1])+parseInt(array[2])+parseInt(array[3])+parseInt(array[4]);
  if(arrayNum==0){
    arrayNum=1;
  }
  var mbolSize = 20;
  // 基于准备好的dom，初始化echarts实例
  var myChart = echarts.init(document.getElementById('echartTeamFor'));
  // 指定图表的配置项和数据
  optionFor = {
    tooltip : {
      trigger: 'item',
      formatter: "{a} <br/>{b} : {c}人"
    },
//      toolbox: {
//        show : true,
//        feature : {
//          mark : {show: false},
//          dataView : {show: true, readOnly: false},
//          magicType : {
//            show: true,
//            type: ['pie', 'funnel']
//          },
//          saveAsImage : {show: true}
//        }
//      },
//      legend: {
//        x : 'center',
//        y : 'bottom',
//        data:['特级服务商','省级服务商','市级服务商','VIP服务商','普通服务商']
//      },
    series : [
      {
        name:'团队新成员',
        type:'pie',
        radius: ['40%', '55%'],
        color:['#ffcd48','#7ed1df','#ffb558','#51c187'],
//        color:['#ffcd48','#7ed1df','#ffb558','#51c187','#91c7ae'],
        data:[
          {value:array[0], name:'特级服务商('+Math.round(array[0]/arrayNum*100*100)/100+'%)'},
          {value:array[1], name:'省级服务商('+Math.round(array[1]/arrayNum*100*100)/100+'%)'},
          {value:array[2], name:'市级服务商('+Math.round(array[2]/arrayNum*100*100)/100+'%)'},
          {value:array[3], name:'VIP服务商('+Math.round(array[3]/arrayNum*100*100)/100+'%)'},
//          {value:array[4], name:'普通服务商('+Math.round(array[4]/arrayNum*100*100)/100+'%)'}
        ]
      },
//        {
//          name:'团队新成员',
//          type:'pie',
//          radius : [30, 110],
//          center : ['50%', '50%'],
//          roseType : 'area',
//          label: {
//            normal: {
//              position: 'inside',
//              formatter: '{d}%',
//              textStyle: {
//                color: '#fff'
//              }
//            }
//          },
//          data:[
//            {value:array[0], name:'特级服务商'},
//            {value:array[1], name:'省级服务商'},
//            {value:array[2], name:'市级服务商'},
//            {value:array[3], name:'VIP服务商'},
//            {value:array[4], name:'普通服务商'}
//          ]
//        }
    ]
  };
  // 使用刚指定的配置项和数据显示图表。
  myChart.setOption(optionFor);
</script>
<%@ include file="/WEB-INF/view/include/footer.jsp"%>
</body>
</html>
