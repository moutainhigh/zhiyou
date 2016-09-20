function formatUser(user) {
	if (user) {
		return '<p><img src="' + user.avatarThumbnail + '" width="30" height="30" style="border-radius:40px !important;margin-right:5px"/>' + user.nickname + '</p>'
			+ '<p>手机: ' + user.phone + '</p>'
			+ '<p>等级: ' + user.userRankLabel + '</p>';
	} else {
		return '-';
	}
}

/**
 * Debug print
 * @param object
 */
function log(object) {
  var s = '{';
  for ( var k in object) {
    s += '\n\t' + k + ': \'' + object[k] + '\'';
  }
  s += '\n}';
  console.log(s);
}

