import com.zy.common.exception.BizException
import com.zy.entity.usr.User.UserRank
import com.zy.model.BizCode

def result

if (userRank == UserRank.V4) { // 特级代理
	result = 88.00
} else if (userRank == UserRank.V3) { // 1级代理
	if (quantity < 100) {
		throw new BizException(BizCode.ERROR, "1级代理拿货必须大于100单");
	}
	result = 108.00
} else if (userRank == UserRank.V2) { // 2级代理
	result = 128.00
} else if (userRank == UserRank.V1) { // 3级代理
	result = 158.00
} else { // 普通用户
	if (quantity >= 300) {
		result = 198.00
	} else if (quantity >= 100 && quantity < 300) {
		result = 198.00
	} else if (quantity >= 15 && quantity < 100) {
		result = 198.00
	} else if (quantity < 15) {
		result = 198.00
	}
}

return result