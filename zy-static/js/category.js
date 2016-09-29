
//计算字符串长度
String.prototype.strLen = function() {
  var len = 0;
  for (var i = 0; i < this.length; i++) {
    if (this.charCodeAt(i) > 255 || this.charCodeAt(i) < 0) {
      len += 2;
    } else {
      len++;
    }
  }
  return len;
}

//判断某个字符是否是汉字
String.prototype.isCH = function(i) {
  if (this.charCodeAt(i) > 255 || this.charCodeAt(i) < 0) {
    return true;
  } else {
    return false;
  }
}

// 将字符串拆成字符，并存到数组中
String.prototype.strToChars = function() {
  var chars = new Array();
  for (var i = 0; i < this.length; i++) {
    chars[i] = [ this.substr(i, 1), this.isCH(i) ];
  }
  String.prototype.charsArray = chars;
  return chars;
}

// 截取字符串（从start字节到end字节）
String.prototype.subCHString = function(start, end) {
  var len = 0;
  var str = "";
  this.strToChars();
  for (var i = 0; i < this.length; i++) {
    if (this.charsArray[i][1]){
      len += 2;
    } else {
      len++;
    }
    if (end < len){
      return str;
    } else if (start < len) {
      str += this.charsArray[i][0];
    }
  }
  return str;
}

String.prototype.startWith = function(str) {
  var reg = new RegExp("^" + str);
  return reg.test(this);
}

String.prototype.endWith = function(str) {
  var reg = new RegExp(str + "$");
  return reg.test(this);
}


/**
 * @example Remove the second item from the array
 *   array.remove(1); 
 * @example Remove the second-to-last item from the array 
 *   array.remove(-2);
 * @example Remove the second and third items from the array
 *   array.remove(1,2);
 * @example Remove the last and second-to-last items from the array
 *   array.remove(-2,-1);
 */
Array.prototype.remove = function(from, to) {  
  var rest = this.slice((to || from) + 1 || this.length);  
  this.length = from < 0 ? this.length + from : from;  
  return this.push.apply(this, rest);  
};

/**
 * 去除数组中重复项
 */
Array.prototype.uniqueArray = function() {
  var o = {}, newArr = [];
  for (var i = 0; i < this.length; i++) {
    if (typeof (o[this[i]]) == 'undefined') {
      o[this[i]] = '';
    }
  }
  for ( var j in o ) {
    newArr.push(j);
  }
  return newArr;
};
