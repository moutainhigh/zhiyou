/*******************************************************************************
 * @description Utils
 * @author http://www.ehuyao.com
 * @copyright Copyright © 2014 ehuyao.com All Rights Reserved.
 ********************************************************************************
 */

sys = {
  base : '',
  currencySign : '￥',
  currencyUnit : '元',
  currencyScale : '2',
  roundType : "HALFUP"
};

// 解决IE6不缓存背景图片问题
if (!window.XMLHttpRequest) {
  document.execCommand("BackgroundImageCache", false, true);
}

// 移除数组中索引为dx的元素
function arrayRemove(array, dx) {
  var newArr = [];
  if (isNaN(dx) || dx > array.length) {
    return array;
  }
  for ( var i = 0, n = 0; i < array.length; i++) {
    if (i != dx) {
      newArr[n++] = array[i];
    }
  }
  return newArr;
}

// 判断数组是否包含另一个数组中所有元素
function arrayContains(array1, array2) {
  if (!(array1 instanceof Array) || !(array2 instanceof Array)) {
    return false;
  }
  if (array1.length < array2.length) {
    return false;
  }
  for ( var i in array2) {
    if ($.inArray(array2[i], array1) == -1) {
      return false;
    }
  }
  return true;
}

// 判断两个数组中的所有元素是否相同
function arrayEqual(array1, array2) {
  if (!(array1 instanceof Array) || !(array2 instanceof Array)) {
    return false;
  }
  if (array1.length != array2.length) {
    return false;
  }
  for ( var i in array2) {
    if ($.inArray(array2[i], array1) == -1) {
      return false;
    }
  }
  return true;
}

/**
 * 去除数组中重复项
 */
function uniqueArray(arr) {
  var o = {}, newArr = [];
  for ( var i = 0; i < arr.length; i++) {
    if (typeof (o[arr[i]]) == 'undefined') {
      o[tharris[i]] = '';
    }
  }
  for ( var j in o) {
    newArr.push(j);
  }
  return newArr;
};

// 添加收藏夹
function addFavorite(url, title) {
  try {
    window.external.addFavorite(url, title);
  } catch (e) {
    try {
      window.sidebar.addPanel(title, url, "");
    } catch (e) {
      alert('请按 [Ctrl + D] 收藏本页面.');
    }
  }
}

// html字符串转义
function htmlEscape(htmlString) {
  htmlString = htmlString.replace(/&/g, '&amp;');
  htmlString = htmlString.replace(/</g, '&lt;');
  htmlString = htmlString.replace(/>/g, '&gt;');
  htmlString = htmlString.replace(/'/g, '&acute;');
  htmlString = htmlString.replace(/"/g, '&quot;');
  htmlString = htmlString.replace(/\|/g, '&brvbar;');
  return htmlString;
}

/**
 * [Cookie] Sets value in a cookie
 */
function setCookie(cookieName, cookieValue, path, expires, domain, secure) {
  if (typeof (expires) == 'undefined') {
    expires = new Date();
    expires.setTime(expires.getTime() + 1000 * 60 * 60 * 24 * 7);
  }
  document.cookie = cookieName + '=' + encodeURIComponent(cookieValue)
      + (expires ? '; expires=' + expires.toGMTString() : '') + ('; path=' + (path ? path : '/'))
      + ('; domain=' + (domain ? domain : document.domain)) + (secure ? '; secure' : '');
}

/**
 * [Cookie] Gets a value from a cookie
 */
function getCookie(cookieName) {
  var cookieValue = null;
  var posName = document.cookie.indexOf(cookieName + '=');
  if (posName != -1) {
    var posValue = posName + (cookieName + '=').length;
    var endPos = document.cookie.indexOf(';', posValue);
    if (endPos != -1)
      cookieValue = decodeURIComponent(document.cookie.substring(posValue, endPos));
    else
      cookieValue = decodeURIComponent(document.cookie.substring(posValue));
  }
  return cookieValue;
}

/**
 * [Cookie] Clears cookies
 */
function clearCookie(cookieName) {
  var now = new Date();
  var yesterday = new Date(now.getTime() - 1000 * 60 * 60 * 24);
  this.setCookie(cookieName, 'null', yesterday);
}

// 获取参数
function getParameter(name) {
  var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
  var r = window.location.search.substr(1).match(reg);
  if (r != null) {
    return decodeURIComponent(r[2]);
  } else {
    return null;
  }
}

// 浮点数加法运算
function floatAdd(arg1, arg2) {
  var r1, r2, m;
  try {
    r1 = arg1.toString().split(".")[1].length;
  } catch (e) {
    r1 = 0;
  }
  try {
    r2 = arg2.toString().split(".")[1].length;
  } catch (e) {
    r2 = 0;
  }
  m = Math.pow(10, Math.max(r1, r2));
  return (arg1 * m + arg2 * m) / m;
}

// 浮点数减法运算
function floatSub(arg1, arg2) {
  var r1, r2, m, n;
  try {
    r1 = arg1.toString().split(".")[1].length;
  } catch (e) {
    r1 = 0;
  }
  try {
    r2 = arg2.toString().split(".")[1].length;
  } catch (e) {
    r2 = 0;
  }
  m = Math.pow(10, Math.max(r1, r2));
  n = (r1 >= r2) ? r1 : r2;
  return ((arg1 * m - arg2 * m) / m).toFixed(n);
}

// 浮点数乘法运算
function floatMul(arg1, arg2) {
  var m = 0, s1 = arg1.toString(), s2 = arg2.toString();
  try {
    m += s1.split(".")[1].length;
  } catch (e) {
  }
  try {
    m += s2.split(".")[1].length;
  } catch (e) {
  }
  return Number(s1.replace(".", "")) * Number(s2.replace(".", "")) / Math.pow(10, m);
}

// 浮点数除法运算
function floatDiv(arg1, arg2) {
  var t1 = 0, t2 = 0, r1, r2;
  try {
    t1 = arg1.toString().split(".")[1].length;
  } catch (e) {
  }
  try {
    t2 = arg2.toString().split(".")[1].length;
  } catch (e) {
  }
  with (Math) {
    r1 = Number(arg1.toString().replace(".", ""));
    r2 = Number(arg2.toString().replace(".", ""));
    return (r1 / r2) * pow(10, t2 - t1);
  }
}

// 设置数值精度
function setScale(value, scale, roundType) {
  if (!roundType) {
    roundType = "HALFUP";
  }
  if (roundType.toLowerCase() == "HALFUP") {
    return (Math.round(value * Math.pow(10, scale)) / Math.pow(10, scale)).toFixed(scale);
  } else if (roundType.toLowerCase() == "UP") {
    return (Math.ceil(value * Math.pow(10, scale)) / Math.pow(10, scale)).toFixed(scale);
  } else { // DOWN
    return (Math.floor(value * Math.pow(10, scale)) / Math.pow(10, scale)).toFixed(scale);
  }
}

// 货币格式化
function formatCurrency(price, withSign, withUnit) {
  price = setScale(price, sys.currencyScale, sys.roundType);
  return (withSign ? sys.currencySign : '') + price + (withUnit ? sys.currencyUnit : '');
}

/**
 * 增加formatString功能 使用方法：formatString('字符串{0}字符串{1}字符串','第一个变量','第二个变量');
 * 
 * @returns 格式化后的字符串
 */
function formatString(str) {
  for ( var i = 0; i < arguments.length - 1; i++) {
    str = str.replace("{" + i + "}", arguments[i + 1]);
  }
  return str;
}

/**
 * 根据长度截取字符串，超长部分追加suffix
 * 
 * @param str
 *          对象字符串
 * @param len
 *          目标字节长度
 * @param suffix
 *          追加后缀
 * @return 处理结果字符串
 */
function subString(str, len, suffix) {
  // length属性读出来的汉字长度为1
  if (!suffix) {
    suffix = '';
  }
  if (str == null) {
    return '';
  }
  if (str.length * 2 <= len) {
    return str;
  }
  var strlen = 0;
  var s = "";
  for ( var i = 0; i < str.length; i++) {
    strlen = strlen + (str.charCodeAt(i) > 128 ? 2 : 1);
    if (strlen <= len - suffix.length) {
      s = s + str.charAt(i);
    } else {
      s = s + suffix;
      break;
    }
  }
  return s;
}
/**
 * 接收一个以逗号分割的字符串，返回List，list里每一项都是一个字符串
 * 
 * @returns list
 */
function stringToList(value) {
  if (value != undefined && value != '') {
    var values = [];
    var t = value.split(',');
    for ( var i = 0; i < t.length; i++) {
      values.push('' + t[i]);/* 避免他将ID当成数字 */
    }
    return values;
  } else {
    return [];
  }
};

/**
 * Debug print
 * 
 * @param map
 */
function printMap(map) {
  var s = '{';
  for ( var k in map) {
    s += '\n\t' + k + ': \'' + map[k] + '\'';
  }
  s += '\n}';
  alert(s);
}
