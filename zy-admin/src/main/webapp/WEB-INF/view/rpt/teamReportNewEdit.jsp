<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/view/include/head.jsp" %>
<div class="page-bar">
    <ul class="page-breadcrumb">
        <li><i class="fa fa-home"></i> <a href="javascript:;" data-href="${ctx}/main">首页</a> <i class="fa fa-angle-right"></i></li>
        <li><a href="javascript:;" data-href="${ctx}/report/teamReportNew" id="team">团队报表</a>  <i class="fa fa-angle-right"></i></li>
        <li><a href="javascript:;">成员分布</a></li>
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
<%--<div class="row orderChartMapTwo">--%>
    <%--<div class="col-md-12">--%>
        <%--<div class="portlet light bordered">--%>
            <%--<div class="portlet-title">--%>
                <%--<div class="caption">--%>
                    <%--<i class="icon-graph"></i>--%>
                    <%--<span class="caption-subject bold uppercase"> 子地图月度报表</span>--%>
                <%--</div>--%>
            <%--</div>--%>
            <%--<div class="portlet-body">--%>
                <%--<div id="orderChartMapTwo" style="width:100%;height:800px;">--%>

                <%--</div>--%>
            <%--</div>--%>
        <%--</div>--%>
    <%--</div>--%>
<%--</div>--%>
<script>
    $(function(){
        chinaMap();
    });
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
    var str="";
    function makeMapData() {
        var mapData = [];
        for(var j=0;j<arrayname.length;j++){
            if(arrayV1[j]!=0||arrayV2[j]!=0||arrayV3[j]!=0||arrayV4[j]!=0){
                if(arrayname[j].length<4){
                    var arrayname2=arrayname[j].substring(0,2);
                }else {
                    var arrayname2=arrayname[j].substring(0,2);
                    if(arrayname[j]=="内蒙古自治区"||arrayname[j]=="黑龙江省"){
                        var arrayname2=arrayname[j].substring(0,3);
                    }
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

    function chinaMap(){
        $.get('${ctx}/map/json/china.json', function (chinaJson) {
//            $(".orderChartMap").show();
//            $(".orderChartMapTwo").hide();
            echarts.registerMap('china', chinaJson);
            chart.setOption({
                tooltip : {
                    trigger: 'item',
                    formatter: function(param){
                        if(arrayname.length==1){
                            str = param.name + "：<br>特级人数：" + 0 + "<br>" + "省级人数：" + 0 + "<br>特级活跃人数：" + 0 + "<br>" + "特级活跃度：" + 0;
                        }else {
                            for (var i = 0; i < arrayname.length; i++) {
                                var name = arrayname[i].substring(0, param.name.length);
                                if (param.name == name) {
                                    str = param.name + "：<br>特级：" + arrayV4[i] + "<br>" + "省级：" + arrayV3[i] + "<br>市级：" + arrayV2[i] + "<br>" + "VIP：" + arrayV1[i];
                                }
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
                        data:makeMapData()
                    }
                ]
            });
            chart.on('click',  function (param) {
//                    console.log(param.name);
//                    var paramCity="";
//                    if(param.name=="上海"){
//                        paramCity="shanghai.json";
//                    }else if(param.name=="安徽"){
//                        paramCity="anhui.json";
//                    }else if(param.name=="河北"){
//                        paramCity="hebei.json";
//                    }else if(param.name=="山西"){
//                        paramCity="shanxi.json";
//                    }else if(param.name=="陕西"){
//                        paramCity="shanxi1.json";
//                    }else if(param.name=="内蒙古"){
//                        paramCity="neimenggu.json";
//                    }else if(param.name=="辽宁"){
//                        paramCity="liaoning.json";
//                    }else if(param.name=="吉林"){
//                        paramCity="jilin.json";
//                    }else if(param.name=="黑龙江"){
//                        paramCity="heilongjiang.json";
//                    }else if(param.name=="江苏"){
//                        paramCity="jiangsu.json";
//                    }else if(param.name=="浙江"){
//                        paramCity="zhejiang.json";
//                    }else if(param.name=="福建"){
//                        paramCity="fujian.json";
//                    }else if(param.name=="江西"){
//                        paramCity="jiangxi.json";
//                    }else if(param.name=="山东"){
//                        paramCity="shandong.json";
//                    }else if(param.name=="河南"){
//                        paramCity="hunan.json";
//                    }else if(param.name=="湖北"){
//                        paramCity="hubei.json";
//                    }else if(param.name=="湖南"){
//                        paramCity="hunan.json";
//                    }else if(param.name=="广东"){
//                        paramCity="guangdong.json";
//                    }else if(param.name=="广西"){
//                        paramCity="guangxi.json";
//                    }else if(param.name=="海南"){
//                        paramCity="hainan.json";
//                    }else if(param.name=="四川"){
//                        paramCity="sichuan.json";
//                    }else if(param.name=="贵州"){
//                        paramCity="guizhou.json";
//                    }else if(param.name=="云南"){
//                        paramCity="yunnan.json";
//                    }else if(param.name=="西藏"){
//                        paramCity="xizang.json";
//                    }else if(param.name=="甘肃"){
//                        paramCity="gansu.json";
//                    }else if(param.name=="青海"){
//                        paramCity="qinghai.json";
//                    }else if(param.name=="宁夏"){
//                        paramCity="ningxia.json";
//                    }else if(param.name=="新疆"){
//                        paramCity="xinjiang.json";
//                    }else if(param.name=="北京"){
//                        paramCity="beijing.json";
//                    }else if(param.name=="天津"){
//                        paramCity="tianjin.json";
//                    }else if(param.name=="重庆"){
//                        paramCity="chongqing.json";
//                    }else if(param.name=="香港"){
//                        paramCity="xianggang.json";
//                    }else if(param.name=="澳门"){
//                        paramCity="aomen.json";
//                    }else if(param.name=="台湾"){
//                        paramCity="taiwan.json";
//                    }
//                    showProvince(paramCity,param.name);
            });
        });
    }
    <%--function showProvince(provinces,nameCity) {--%>
        <%--$.get('${ctx}/map/json/' + provinces , function (geoJson) {--%>
            <%--$(".orderChartMap").hide();--%>
            <%--$(".orderChartMapTwo").show();--%>
            <%--echarts.registerMap(nameCity, geoJson);--%>
            <%--chartTwo.setOption({--%>
                <%--title: {--%>
                    <%--text: nameCity,--%>
                    <%--left: 'center',--%>
                <%--},--%>
                <%--tooltip : {--%>
                    <%--trigger: 'item',--%>
                    <%--formatter: function(param){--%>
                        <%--for(var i=0;i<arrayname.length;i++){--%>
                            <%--var name = arrayname[i].substring(0,param.name.length);--%>
                            <%--if(param.name==name){--%>
                                <%--str = param.name+"：<br>特级："+0+"<br>"+"省级："+0+"<br>市级："+0+"<br>"+"VIP："+0;--%>
                            <%--}--%>
                        <%--}--%>
                        <%--return str;--%>
                    <%--}--%>
                <%--},--%>
                <%--series: [--%>
                    <%--{--%>
                        <%--type: 'map',--%>
                        <%--mapType: nameCity,--%>
                        <%--itemStyle: {--%>
                            <%--normal: {--%>
                                <%--borderColor: '#389BB7',--%>
                                <%--areaColor: '#fff',--%>
                            <%--},--%>
                            <%--emphasis: {--%>
                                <%--areaColor: '#389BB7',--%>
                                <%--borderWidth: 0--%>
                            <%--}--%>
                        <%--},--%>
                        <%--label: {--%>
                            <%--normal: {--%>
                                <%--show: true--%>
                            <%--},--%>
                            <%--emphasis: {--%>
                                <%--show: true--%>
                            <%--}--%>
                        <%--},--%>
                    <%--}--%>
                <%--]--%>
            <%--});--%>
        <%--});--%>
    <%--}--%>


</script>