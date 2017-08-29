<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/view/include/head.jsp" %>
<div class="row">
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
<script>
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
    console.log(V1+"V1"+V2+"V2"+V3+"V3"+V4+"V4");
    $.get('${ctx}/map/json/china.json', function (chinaJson) {
        echarts.registerMap('china', chinaJson);
        var chart = echarts.init(document.getElementById('orderChartMap'));
        chart.setOption({
            tooltip : {
                trigger: 'item',
                formatter: function(param){
                    var str = param.name+"：<br>特级："+arrayV4[0]+"<br>"+"省级："+arrayV4[0]+"<br>市级："+arrayV1[0]+"<br>"+"VIP："+arrayV1[0];
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
                    data:[
                        {name:"安徽",value: "222"},
                    ]
                }
            ]
        });
        chart.on('click',  function (param) {
//				console.log(param);
//				console.log(param.tooltip);
        });
    });

</script>