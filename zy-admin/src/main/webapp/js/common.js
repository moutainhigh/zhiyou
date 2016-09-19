function formatUser(user) {
	if (user) {
		return '<p><img src="' + full.user.avatarThumbnail + '" width="30" height="30" style="border-radius: 40px !important; margin-right:5px"/>' + full.user.nickname + '</p>' +
			'<p>手机: ' + full.user.phone + '</p>';
	} else {
		return '-';
	}
}
