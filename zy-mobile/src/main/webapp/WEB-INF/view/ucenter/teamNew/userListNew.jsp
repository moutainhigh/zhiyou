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
      width: 95%;
      background: #fff !important;
      margin-top: 10px;
      margin-left: 2.5%;
    }
    #echartTeam,#echartTeamTwo,#echartTeamFor {
      width:100%;
      height:200px;
      background: #fff !important;
    }
    #echartTeamFor {
      height:250px;
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
      margin-top: 10px;
      margin-bottom: 10px;
    }
    .ranking {
      float: left;
      width:150px;
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
       background: #22b5d4;
    }
    .province {
       background: #fe543e;
    }
    .city {
      background: #fb8604;
    }
    .allLast {
      margin-bottom: 10px;
    }
  </style>
</head>
<body>
  <header class="header">
    <h1>我的团队</h1>
    <a href="${ctx}/u" class="button-left"><i class="fa fa-angle-left"></i></a>
  </header>

  <article>
      <c:if test="${dataMap.flag != null}">
         <div class="all" >
               <div class="teamAll">
                   <img src="${ctx}/team2.png"/>
                   <span>团队总人数</span>
                  <c:if test="${dataMap.flag == 'T'}"> <a href="#">直属特级详情>></a></c:if>
               </div>
               <div id="echartTeam"></div>
         </div>
      </c:if>
      <div class="all" >
            <div class="teamAll">
              <img src="${ctx}/team3.png"/>
              <span>直属团队</span>
              <a href="#">直属详情>></a>
            </div>
            <div id="echartTeamTwo"></div>
      </div>

      <div class="all" >
            <div class="teamAll">
              <img src="${ctx}/team5.png"/>
              <span>团队新成员</span>
              <a href="#">新成员详情>></a>
            </div>
            <div id="echartTeamFor"></div>
      </div>

    <div class="all allName clearfloat">
          <div class="teamAll allList">
            <img src="${ctx}/team4.png"/>
            <span>新晋直属特级</span>
          </div>
          <div class="TeamName">
               <img src="${ctx}/headPortrait.png" />
               <span>赵春华</span>
               <span>13656174839</span>
          </div>
          <div class="TeamName">
            <img src="${ctx}/headPortrait2.png" />
            <span>赵春华</span>
            <span>13656174839</span>
          </div>
          <div class="TeamName">
            <img src="${ctx}/headPortrait.png" />
            <span>赵春华</span>
            <span>13656174839</span>
          </div>
    </div>

    <div class="all">
          <div class="teamAll allList">
            <img src="${ctx}/team6.png"/>
            <span>新晋排名</span>
            <span class="paim">我的排名：25</span>
          </div>
          <div class="rankingAll">
               <span>1</span>
               <img src="${ctx}/headPortrait.png" />
               <div class="ranking">
                 <span>赵春华</span>
                 <span class="rankingSpan must">特级</span>
               </div>
               <span class="tel">13656174839</span>
          </div>
          <div class="rankingAll">
            <span>2</span>
            <img src="${ctx}/headPortrait.png" />
            <div class="ranking">
              <span>赵春华</span>
              <span class="rankingSpan province">省级</span>
            </div>
            <span class="tel">13656174839</span>
          </div>
          <div class="rankingAll">
            <span>3</span>
            <img src="${ctx}/headPortrait.png" />
            <div class="ranking">
              <span>赵春华</span>
              <span class="rankingSpan city">市级</span>
            </div>
            <span class="tel">13656174839</span>
          </div>
          <a href="#" class="lookDetil">更多详情>></a>
    </div>

    <div class="all allLast">
          <div class="teamAll allList">
            <img src="${ctx}/team7.png"/>
            <span>活跃度：90%</span>
          </div>
          <div class="teamAll allList">
            <img src="${ctx}/team1.png"/>
            <span>沉睡成员</span>
          </div>
      <div class="rankingAll">
        <span>1</span>
        <img src="${ctx}/headPortrait.png" />
        <div class="ranking">
          <span>赵春华</span>
          <span class="rankingSpan must">特级</span>
        </div>
        <span class="tel">13656174839</span>
      </div>
      <div class="rankingAll">
        <span>2</span>
        <img src="${ctx}/headPortrait.png" />
        <div class="ranking">
          <span>赵春华</span>
          <span class="rankingSpan province">省级</span>
        </div>
        <span class="tel">13656174839</span>
      </div>
      <div class="rankingAll">
        <span>3</span>
        <img src="${ctx}/headPortrait.png" />
        <div class="ranking">
          <span>赵春华</span>
          <span class="rankingSpan city">市级</span>
        </div>
        <span class="tel">13656174839</span>
      </div>
      <a href="#" class="lookDetil">更多详情>></a>
    </div>
  </article>
  <script src="${ctx}/echarts.min.js"></script>
  <script type="text/javascript">
    var counts = "${dataMap.TTot}";
    var array=counts.split(",");
    var mbolSize = 20;
    var arrayNum=parseInt(array[0])+parseInt(array[1])+parseInt(array[2])+parseInt(array[3]);
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
//          color:['#EEB422','#BEBEBE','#FFA07A','#483D8B'],
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
    var arrayNum=parseInt(array[0])+parseInt(array[1])+parseInt(array[2])+parseInt(array[3])+parseInt(array[4]);
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
          data: [
            {value: array[0], name: '特级服务商('+Math.round(array[0]/arrayNum*100*100)/100+'%)'},
            {value: array[1], name: '省级服务商('+Math.round(array[1]/arrayNum*100*100)/100+'%)'},
            {value: array[2], name: '市级服务商('+Math.round(array[2]/arrayNum*100*100)/100+'%)'},
            {value: array[3], name: 'VIP服务商('+Math.round(array[3]/arrayNum*100*100)/100+'%)'},
            {value: array[4], name: '普通服务商('+Math.round(array[4]/arrayNum*100*100)/100+'%)'}
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
    var arrayNum=parseInt(array[0])+parseInt(array[1])+parseInt(array[2])+parseInt(array[3])+parseInt(array[4]);
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
          radius : [30, 110],
          center : ['50%', '50%'],
          roseType : 'area',
          data:[
            {value:array[0], name:'特级服务商('+Math.round(array[0]/arrayNum*100*100)/100+'%)'},
            {value:array[1], name:'省级服务商('+Math.round(array[1]/arrayNum*100*100)/100+'%)'},
            {value:array[2], name:'市级服务商('+Math.round(array[2]/arrayNum*100*100)/100+'%)'},
            {value:array[3], name:'VIP服务商('+Math.round(array[3]/arrayNum*100*100)/100+'%)'},
            {value:array[4], name:'普通服务商('+Math.round(array[4]/arrayNum*100*100)/100+'%)'}
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
