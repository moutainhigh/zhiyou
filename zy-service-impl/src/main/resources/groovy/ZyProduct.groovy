import com.zy.entity.usr.User.UserRank

def result

if (userRank == UserRank.V4) { // 特级代理
	result = 88.00 // 特级代理进货数量必须大于100
} else if (userRank == UserRank.V3) { // 1级代理
	result = 108.00 // 1级代理进货数量必须大于100
} else if (userRank == UserRank.V2) { // 2级代理
	if (quantity >= 300) { // 升级1级代理
		result = 108.00
	} else {
		result = 138.00
	}
} else if (userRank == UserRank.V1) { // 3级代理
	if (quantity >= 300) { // 升级1级代理
		result = 108.00
	} else if (quantity >= 100 && quantity < 300) { // 升级2级代理
		result = 138.00
	} else {
		result = 168.00
	}
} else { // 普通用户
	if (quantity >= 300) {
		result = 108.00
	} else if (quantity >= 100 && quantity < 300) {
		result = 138.00
	} else if (quantity >= 15 && quantity < 100) {
		result = 168.00
	} else if (quantity < 15) {
		result = 198.00 // 成为代理进货数量必须大于15
	}
}

return result