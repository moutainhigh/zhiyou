<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/view/include/head.jsp" %>
<style>
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
    .map {
        width: 20%;
        height:200px;
        line-height:200px;
        float: left;
        border: 1px solid #eee;
        -webkit-border-radius:10px;
        -moz-border-radius:10px;
        -ms-border-radius:10px;
        -o-border-radius:10px;
        border-radius:10px;
        margin-left: 4%;
        margin-top: 3%;
        text-align: center;
        font-size: 24px;
        color: #fff;
        text-decoration: none;
    }
    a:hover {
        text-decoration: none;
    }
</style>
<div class="row">
    <div class="col-md-12">
        <div class="portlet light bordered clearfloat">
            <c:forEach items="${largeAreaTypes}" var="largeAreaType">
                <a href="javascript:;" data-href="${ctx}/newReport/base?type=${largeAreaType.systemValue}" class="map">
                    <%--<img src="http://image.zhi-you.net/image/9cbac3a0-96b5-4e6e-9cb1-40da63cd240f@450h_750w_1e_1c.jpg" style="width: 100%;"/>--%>
                    <span>
                            ${largeAreaType.systemName}部大区
                    </span>
                </a>
            </c:forEach>
        </div>
    </div>
</div>
<script>
    $(function(){
        $(".bordered").css("height",$(".page-sidebar-menu").height()-100+"px");
        var colors = ['#07a33e', '#97c618', '#f5804a', '#18d6d7', '#0090e2',"#3765a1",'#9a0432','#326966','#67686a','#fdfd07','#feffc6','#ce9a2d','#696796','#97cbfd','#646a00'];
//        alert($(".map").length);
        for(let i=0;i<$(".map").length;i++){
             $(".map").eq(i).css("background",colors[i]);
        }
    })
</script>