<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/view/include/head.jsp" %>
<div class="page-bar">
    <ul class="page-breadcrumb">
        <li><i class="fa fa-home"></i> <a href="javascript:;" data-href="${ctx}/main">首页</a> <i class="fa fa-angle-right"></i></li>
        <li><a href="javascript:;" data-href="${ctx}/report/teamReportNew" id="team">团队报表</a>  <i class="fa fa-angle-right"></i></li>
        <li>成员分布</li>
    </ul>
</div>
<div class="row orderChartMap">
    <div class="col-md-12">
        <div class="portlet light bordered">
            <div class="portlet-title">
                <div class="caption">
                    <i class="icon-graph"></i>
                    <span class="caption-subject bold uppercase"> 地图月度报表</span>
                </div>
            </div>
            <div class="portlet-body">
                <div id="orderChartMap" style="height:800px;">

                </div>
            </div>
        </div>
    </div>
</div>
<div class="row orderChartMapTwo">
    <div class="col-md-12">
        <div class="portlet light bordered">
            <div class="portlet-title">
                <div class="caption">
                    <i class="icon-graph"></i>
                    <span class="caption-subject bold uppercase"> 地图月度报表</span>
                </div>
            </div>
            <div class="portlet-body">
                <div id="orderChartMapTwo" style="height:800px;">

                </div>
            </div>
        </div>
    </div>
</div>
<script>
    $(function(){
        chinaMap();
    })
    var V1='${v1}';
    var V2='${v2}';
    var V3='${v3}';
    var V4='${v4}';
    var name='${name}';
   var arrayV1=V1.split(",");
   var arrayV2=V2.split(",");
   var arrayV3=V3.split(",");
   var arrayV4=V4.split(",");
   var arrayname=name.split(",");
    var dataArray=[];
    console.log(V1+"V1"+arrayname+V2+"V2"+arrayname+V3+"V3"+arrayname+V4+"V4"+arrayname);
    var str="";
    function makeMapData(rawData) {
        var mapData = [];
        for(var j=0;j<arrayname.length;j++){
            if(arrayV1[j]!=0||arrayV2[j]!=0||arrayV3[j]!=0||arrayV4[j]!=0){
                if(arrayname[j].length<4){
                    var arrayname2=arrayname[j].substring(0,2);
                }
                if(arrayname[j]=="内蒙古自治区"||arrayname[j]=="黑龙江省"){
                    var arrayname2=arrayname[j].substring(0,3);
                }
                mapData.push({
                    name: arrayname2,
                    selected:true
                });
            }
        }
        return mapData;
    }
    var chart = echarts.init(document.getElementById('orderChartMap'));
    var chartTwo = echarts.init(document.getElementById('orderChartMapTwo'));

    function chinaMap(){
        $.get('${ctx}/map/json/china.json', function (chinaJson) {
            $(".orderChartMap").show();
            $(".orderChartMapTwo").hide();
            echarts.registerMap('china', chinaJson);
            chart.setOption({
                tooltip : {
                    trigger: 'item',
                    formatter: function(param){
                        for(var i=0;i<arrayname.length;i++){
                            var name = arrayname[i].substring(0,param.name.length);
                              if(param.name==name){
                                  str = param.name+"：<br>特级："+arrayV4[i]+"<br>"+"省级："+arrayV3[i]+"<br>市级："+arrayV2[i]+"<br>"+"VIP："+arrayV1[i];
                              }
                        }
                        return str;
                    }
                },
                series: [
                    {
                        name: '中国',
                        type: 'map',
                        mapType: 'china',
                        selectedMode : 'multiple',
                        label: {
                            normal: {
                                show: true
                            },
                            emphasis: {
                                show: true
                            }
                        },
                        itemStyle: {
                            normal: {
                                borderColor: '#389BB7',
                                areaColor: '#fff',
                            },
                            emphasis: {
                                areaColor: '#389BB7',
                                borderWidth: 0
                            }
                        },
                        data:makeMapData(dataArray)
                    }
                ]
            });
            chart.on('click',  function (param) {
                    console.log(param.name);
                    var paramCity="";
                    if(param.name=="上海"){
                        paramCity="shanghai";
                    }else if(param.name=="安徽"){
                        paramCity="anhui";
                    }else{

                    }
                    showProvince(paramCity,param.name);
            });
        });
    }
    function showProvince(provinces,nameCity) {
        $.get('${ctx}/map/json/' + provinces + '.json', function (geoJson) {
            $(".orderChartMap").hide();
            $(".orderChartMapTwo").show();
            echarts.registerMap(name, geoJson);
            chartTwo.setOption({
                title: {
                    text: nameCity,
                    left: 'center',
                },
                tooltip : {
                    trigger: 'item',
                    formatter: function(param){
                        for(var i=0;i<arrayname.length;i++){
                            var name = arrayname[i].substring(0,param.name.length);
                            if(param.name==name){
                                str = param.name+"：<br>特级："+0+"<br>"+"省级："+0+"<br>市级："+0+"<br>"+"VIP："+0;
                            }
                        }
                        return str;
                    }
                },
                series: [
                    {
                        type: 'map',
                        mapType: name,
                        itemStyle: {
                            normal: {
                                borderColor: '#389BB7',
                                areaColor: '#fff',
                            },
                            emphasis: {
                                areaColor: '#389BB7',
                                borderWidth: 0
                            }
                        },
                        label: {
                            normal: {
                                show: true
                            },
                            emphasis: {
                                show: true
                            }
                        },
                    }
                ]
            });
        });
    }


</script>