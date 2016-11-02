import com.zy.entity.usr.User.UserRank

def result

def quantityToV1 = 5;
def quantityToV2 = 80;
def quantityToV3 = 240;

def priceV0 = 238.00;
def priceV1 = 198.00;
def priceV2 = 168.00;
def priceV3 = 148.00;
def priceV4 = 128.00;

if (userRank == UserRank.V4) { // 总代理
	result = priceV4
} else if (userRank == UserRank.V3) { // 1级代理
	result = priceV3
} else if (userRank == UserRank.V2) { // 2级代理
	if (quantity >= quantityToV3) { // 升级1级代理
		result = priceV3
	} else {
		result = priceV2
	}
} else if (userRank == UserRank.V1) { // 3级代理
	if (quantity >= quantityToV3) { // 升级1级代理
		result = priceV3
	} else if (quantity >= quantityToV2 && quantity < quantityToV3) { // 升级2级代理
		result = priceV2
	} else {
		result = priceV1
	}
} else { // 普通用户
	if (quantity >= quantityToV3) {
		result = priceV3
	} else if (quantity >= quantityToV2 && quantity < quantityToV3) {
		result = priceV2
	} else if (quantity >= quantityToV1 && quantity < quantityToV2) {
		result = priceV1
	} else {
		result = priceV0
	}
}

return result