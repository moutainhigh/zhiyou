import com.zy.common.exception.BizException
import com.zy.entity.fnc.CurrencyType
import com.zy.entity.usr.User
import com.zy.model.BizCode

def result
if (userType == User.UserType.代理 && currencyType == CurrencyType.现金) {
	result = 0.01
} else {
	throw new BizException(BizCode.ERROR, "不存在的情况")
}

return result