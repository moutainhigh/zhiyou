<%@ page language="java" pageEncoding="UTF-8" trimDirectiveWhitespaces="false"%>
<%@ include file="/WEB-INF/view/include/taglib.jsp"%>
<%--
areas['8'] = '湖北省';
areas['8_2'] = '宜昌市';
areas['8_2_3'] = '枝江市';
--%>
String.prototype.startWith = function(str) {
  if (str == null || str == "" || this.length == 0 || str.length > this.length)
    return false;
  if (this.substr(0, str.length) == str)
    return true;
  else
    return false;
  return true;
}
String.prototype.endWith = function(str){
  if(str == null || str == "" || this.length == 0 || str.length > this.length)
    return false;
  if(this.substring(this.length - str.length) == str)
    return true;
  else
    return false;
  return true;
}

var areas = {};
<c:forEach items="${areas}" var="area"><c:if test="${area.areaType=='省'}">areas['${area.id}'] = '${area.name}';
</c:if><c:if test="${area.areaType=='市'}">areas['${area.parentId}_${area.id}'] = '${area.name}';
</c:if><c:if test="${area.areaType=='区'}">areas['${area.topId}_${area.parentId}_${area.id}'] = '${area.name}';
</c:if></c:forEach>
var areaInit = function(_province, _city, _district, defaultAreaId) {
  var eleProvince = document.getElementById(_province);
  var eleCity = document.getElementById(_city);
  var eleDistrict = document.getElementById(_district);
  
  var defaultProvinceId = null, defaultCityId = null, defaultDistrictId = null;
  
  if(defaultAreaId) {
    for ( var areaId in areas) {
      if (areaId.endWith('_' + defaultAreaId)) {
        var ids = areaId.split('_');
        if(ids.length == 3) {
          defaultDistrictId = areaId;
          defaultCityId = areaId.substring(0, areaId.lastIndexOf('_'));
          defaultProvinceId = areaId.substring(0, areaId.indexOf('_'));
        }
        if(ids.length == 2) {
          defaultCityId = areaId;
          defaultProvinceId = areaId.substring(0, areaId.indexOf('_'));
        }
      }
    }
    if(defaultProvinceId == null) {
      defaultProvinceId = defaultAreaId;
    }
    //alert(defaultProvinceId + "/" + defaultCityId +  "/" + defaultDistrictId);
  }

  function selectOption(element, id) {
    for (var i = 0; i < element.options.length; i++) {
      if (element.options[i].getAttribute('data-id') == id) {
        element.selectedIndex = i;
        return;
      }
    }
  }

  function addOption(element, id, name) {
    var option = document.createElement("option");
    element.options.add(option);
    option.innerText = name;
    option.setAttribute('data-id', id);
    option.value = id.lastIndexOf('_') == -1 ? id : id.substring(id.lastIndexOf('_') + 1);
  }

  function changeCity() {
    eleDistrict.options.length = 1;
    if (eleCity.selectedIndex == -1) {
      return;
    }
    //addOption(eleDistrict, '', '--请选择--');
    var selectedOption = eleCity.options[eleCity.selectedIndex];
    var city = selectedOption.value;
    var cityAreaId = selectedOption.getAttribute('data-id');
    for ( var areaId in areas) {
      if (areaId != cityAreaId && areaId.startWith(cityAreaId + '_')) {
        addOption(eleDistrict, areaId, areas[areaId]);
      }
    }
    if (defaultDistrictId) {
      selectOption(eleDistrict, defaultDistrictId);
    }
  }

  function changeProvince() {
    eleCity.options.length = 1;
    eleCity.onchange = null;
        eleDistrict.options.length = 1;
        eleDistrict.onchange = null;
    if (eleProvince.selectedIndex == -1) {
      return;
    }
    //addOption(eleCity, '', '--请选择--');
    var selectedOption = eleProvince.options[eleProvince.selectedIndex];
    var province = selectedOption.value;
    var proAreaId = selectedOption.getAttribute('data-id');
    for ( var areaId in areas) {
      if (areaId != proAreaId && areaId.startWith(proAreaId + '_') && areaId.indexOf('_') == areaId.lastIndexOf('_')) {
        addOption(eleCity, areaId, areas[areaId]);
      }
    }
    if (defaultCityId) {
      selectOption(eleCity, defaultCityId);
      changeCity();
    }
    eleCity.onchange = changeCity;
  }

  for ( var areaId in areas) {
    if (areaId.indexOf('_') == -1) {
      addOption(eleProvince, areaId, areas[areaId]);
    }
  }

  if (defaultProvinceId) {
    selectOption(eleProvince, defaultProvinceId);
    changeProvince();
  }
  eleProvince.onchange = changeProvince;
}
