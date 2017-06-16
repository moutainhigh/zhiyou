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
    #echartTeam,#echartTeamTwo,#echartTeamFor {
      width:100%;
      height:360px;
      background: url("${ctx}/bg.png") top center !important;
      background-size: cover;
      padding-top:10px;
    }
    #echartTeamFor {
      height:400px;
    }
  </style>
<script type="text/javascript">
  $(function() {

  });
</script>
</head>
<body>
  <header class="header">
    <h1>我的团队</h1>
    <a href="${ctx}/u" class="button-left"><i class="fa fa-angle-left"></i></a>
  </header>

  <article>
       <div id="echartTeam" ></div>
       <div id="echartTeamTwo"></div>
       <div id="echartTeamFor"></div>
  </article>
  <script src="${ctx}/echarts.min.js"></script>
  <script type="text/javascript">
    var counts = "${dataMap.TTot}";
    var array=counts.split(",");
    var mbolSize = 20;
    // 基于准备好的dom，初始化echarts实例
    var myChart = echarts.init(document.getElementById('echartTeam'));
    // 指定图表的配置项和数据
    option = {
      tooltip: {
        trigger: 'item',
        formatter: "{a} <br/>{b}: {c} ({d}%)",

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
      title : {
        text: '一、团队总人数'
      },
      series: [
        {
          name:'团队总人数',
          type:'pie',
          radius: ['40%', '55%'],
          data:[
            {value:array[0], name:'特级服务商'},
            {value:array[1], name:'省级服务商'},
            {value:array[2], name:'市级服务商'},
            {value:array[3], name:'VIP服务商'}
          ]
        },
        {
          name:'团队总人数',
          type:'pie',
          radius: ['40%', '55%'],
          label: {
            normal: {
              position: 'inside',
              formatter: '{d}%',
              textStyle: {
                color: '#fff'
              }
            }
          },
          data:[
            {value:array[0], name:'特级服务商'},
            {value:array[1], name:'省级服务商'},
            {value:array[2], name:'市级服务商'},
            {value:array[3], name:'VIP服务商'}
          ]
        }
      ]
    };
    // 使用刚指定的配置项和数据显示图表。
    myChart.setOption(option);
  </script>
  <script type="text/javascript">
    var array=[100,310,234,135,125];
    var mbolSize = 20;
    // 基于准备好的dom，初始化echarts实例
    var myChart = echarts.init(document.getElementById('echartTeamTwo'));
    // 指定图表的配置项和数据
    optionTwo = {
      title : {
        text: '二、直属团队',
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
      tooltip : {
        trigger: 'item',
        formatter: "{a} <br/>{b} : {c} ({d}%)"
      },
      series : [
        {
          name: '直属团队',
          type: 'pie',
          radius: '55%',
          center: ['50%', '50%'],

          data: [
            {value: array[0], name: '特级服务商'},
            {value: array[1], name: '省级服务商'},
            {value: array[2], name: '市级服务商'},
            {value: array[3], name: 'VIP服务商'},
            {value: array[4], name: '普通服务商'}
          ],
          itemStyle: {
            emphasis: {
              shadowBlur: 10,
              shadowOffsetX: 0,
              shadowColor: 'rgba(0, 0, 0, 0.5)'
            }
          },
        },
        {
          name: '直属团队',
          type: 'pie',
          radius : '55%',
          center: ['50%', '50%'],
          label: {
            normal: {
              position: 'inside',
              formatter: '{d}%',
              textStyle: {
                color: '#fff'
              }
            }
          },
          data:[
            {value:array[0], name:'特级服务商'},
            {value:array[1], name:'省级服务商'},
            {value:array[2], name:'市级服务商'},
            {value:array[3], name:'VIP服务商'},
            {value:array[4], name:'普通服务商'}
          ],
          itemStyle: {
            emphasis: {
              shadowBlur: 10,
              shadowOffsetX: 0,
              shadowColor: 'rgba(0, 0, 0, 0.5)'
            }
          },

        }
      ]
    };
    // 使用刚指定的配置项和数据显示图表。
    myChart.setOption(optionTwo);
  </script>
  <script type="text/javascript">
    var array=[335,310,234,135,125];
    var mbolSize = 20;
    // 基于准备好的dom，初始化echarts实例
    var myChart = echarts.init(document.getElementById('echartTeamFor'));
    // 指定图表的配置项和数据
    optionFor = {
      title : {
        text: '三、团队新增成员'
      },
      tooltip : {
        trigger: 'item',
        formatter: "{a} <br/>{b} : {c} ({d}%)"
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
      legend: {
        x : 'center',
        y : 'bottom',
        data:['特级服务商','省级服务商','市级服务商','VIP服务商','普通服务商']
      },
      series : [
        {
          name:'团队新成员',
          type:'pie',
          radius : [30, 110],
          center : ['50%', '50%'],
          roseType : 'area',
          data:[
            {value:array[0], name:'特级服务商'},
            {value:array[1], name:'省级服务商'},
            {value:array[2], name:'市级服务商'},
            {value:array[3], name:'VIP服务商'},
            {value:array[4], name:'普通服务商'}
          ]
        },
        {
          name:'团队新成员',
          type:'pie',
          radius : [30, 110],
          center : ['50%', '50%'],
          roseType : 'area',
          label: {
            normal: {
              position: 'inside',
              formatter: '{d}%',
              textStyle: {
                color: '#fff'
              }
            }
          },
          data:[
            {value:array[0], name:'特级服务商'},
            {value:array[1], name:'省级服务商'},
            {value:array[2], name:'市级服务商'},
            {value:array[3], name:'VIP服务商'},
            {value:array[4], name:'普通服务商'}
          ]
        }
      ]
    };
    // 使用刚指定的配置项和数据显示图表。
    myChart.setOption(optionFor);
  </script>
  <%@ include file="/WEB-INF/view/include/footer.jsp"%>
</body>
</html>
