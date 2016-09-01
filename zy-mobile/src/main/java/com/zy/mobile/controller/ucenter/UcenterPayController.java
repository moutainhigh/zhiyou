package com.zy.mobile.controller.ucenter;

import com.zy.common.exception.BizException;
import com.zy.common.extend.BigDecimalBinder;
import com.zy.common.util.Identities;
import com.zy.entity.fnc.CurrencyType;
import com.zy.entity.fnc.Deposit;
import com.zy.entity.fnc.Deposit.DepositStatus;
import com.zy.entity.fnc.PayType;
import com.zy.entity.usr.WeixinUser;
import com.zy.model.BizCode;
import com.zy.model.Constants;
import com.zy.model.Principal;
import com.zy.model.query.DepositQueryModel;
import com.zy.service.DepositService;
import com.zy.service.WeixinUserService;
import com.zy.util.GcUtils;
import io.gd.generator.api.query.Direction;
import me.chanjar.weixin.common.bean.WxJsapiSignature;
import me.chanjar.weixin.common.util.crypto.WxCryptUtil;
import me.chanjar.weixin.mp.api.WxMpConfigStorage;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpPrepayIdResult;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;

import static com.zy.common.util.ValidateUtils.validate;
import static com.zy.model.Constants.WEIXIN_PAY_EXPIRE_IN_MINUTES;

@Controller
@RequestMapping("/u/pay")
public class UcenterPayController {

	final Logger logger = LoggerFactory.getLogger(UcenterPayController.class);

	@Autowired
	private DepositService depositService;

	@Autowired
	private WeixinUserService weixinUserService;

	@Autowired
	private WxMpConfigStorage wxMpConfigStorage;

	@Autowired
	private WxMpService wxMpService;

	@RequestMapping
	public String create(@BigDecimalBinder BigDecimal coin, Model model, Principal principal, HttpServletRequest request) {

		PayType payType = PayType.微信公众号;

		final BigDecimal zero = new BigDecimal("0.00");
		String title = CurrencyType.金币.getAlias() + "充值";
		Long userId = principal.getUserId();
		validate(coin, v -> v.compareTo(zero) > 0, "coin must be more than 0.00");

		DepositQueryModel depositQueryModel = new DepositQueryModel();
		depositQueryModel.setUserIdEQ(principal.getUserId());
		depositQueryModel.setDepositStatusEQ(DepositStatus.待充值);
		depositQueryModel.setOrderBy("createdTime");
		depositQueryModel.setDirection(Direction.DESC);
		List<Deposit> deposits = depositService.findAll(depositQueryModel);
		Deposit deposit = deposits.stream().filter(v -> v.getPayType() == payType)
				.filter(v -> v.getExpiredTime() == null || v.getExpiredTime().after(new Date())).filter(v -> {
					if (v.getAmount1().equals(coin) && v.getCurrencyType1() == CurrencyType.金币 && v.getCurrencyType2() == null) {
						return true;
					} else {
						return false;
					}
				}).findFirst().orElse(null);

		if (deposit == null) {
			deposit = depositService.create(title, CurrencyType.金币, coin, null, null, PayType.微信公众号, userId, null);
		}

		WeixinUser weixinUser = weixinUserService.findByUserId(deposit.getUserId());
		String openId = weixinUser.getOpenId();

		if (!deposit.getIsOuterCreated()) {
			String sn = deposit.getSn();
			Date expiredTime = DateUtils.addMinutes(new Date(), WEIXIN_PAY_EXPIRE_IN_MINUTES);
			int weixinAmount = (deposit.getTotalAmount().multiply(new BigDecimal("100"))).intValue();

			String tradeType = "JSAPI"; // tradeType 交易类型 JSAPI，NATIVE，APP，WAP
			WxMpPrepayIdResult wxMpPrepayIdResult = wxMpService.getPrepayId(getPrepayIdModel(openId, sn, weixinAmount, deposit.getTitle(), tradeType,
					GcUtils.getHost(), Constants.WEIXIN_MP_PAY_NOTIFY));
			String prepayId = wxMpPrepayIdResult.getPrepay_id();

			if (StringUtils.isBlank(prepayId)) {
				throw new BizException(BizCode.ERROR, "获取微信预支付交易会话标识码失败");
			}

			deposit.setExpiredTime(expiredTime);
			deposit.setOuterSn(prepayId);
			deposit.setIsOuterCreated(true);
			depositService.update(deposit);
		}
		model.addAttribute("deposit", deposit);
		String url = request.getRequestURL().toString();
		String queryStr = request.getQueryString();
		if (queryStr != null)
			url = url + '?' + queryStr;
		model.addAttribute("deposit", deposit);
		model.addAttribute("weixinJsModel", getWeixinJsModel(url));
		model.addAttribute("weixinPayModel", getWeixinPayModel(deposit.getOuterSn()));
		return "ucenter/currency/weixinPay";
	}

	private Map<String, String> getPrepayIdModel(String openId, String outTradeNo, int amount, String body, String tradeType, String ip, String notifyUrl) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("appid", wxMpConfigStorage.getAppId());
		params.put("mch_id", wxMpConfigStorage.getPartnerId());
		params.put("body", body);
		params.put("out_trade_no", outTradeNo);
		params.put("total_fee", amount + "");
		params.put("spbill_create_ip", ip);
		params.put("notify_url", notifyUrl);
		params.put("trade_type", tradeType);
		params.put("openid", openId);
		return params;
	}

	private Map<String, String> getWeixinJsModel(String url) {
		WxJsapiSignature wxJsapiSignature;
		Map<String, String> params = new HashMap<>();
		try {
			wxJsapiSignature = wxMpService.createJsapiSignature(url);
			params.put("appId", wxJsapiSignature.getAppid());
			params.put("timestamp", wxJsapiSignature.getTimestamp() + "");
			params.put("nonceStr", wxJsapiSignature.getNoncestr());
			params.put("signature", wxJsapiSignature.getSignature());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return params;
	}

	public Map<String, String> getWeixinPayModel(String prepayId) {
		Map<String, String> result = new HashMap<>();
		Map<String, String> mapForSign = new TreeMap<>();
		Long timeStamp = System.currentTimeMillis() / 1000;
		String nonceStr = Identities.uuid2();
		String pkg = "prepay_id=" + prepayId;
		String signType = "MD5";
		String paySign = null;
		mapForSign.put("appId", wxMpConfigStorage.getAppId());
		mapForSign.put("timeStamp", timeStamp.toString());
		mapForSign.put("nonceStr", nonceStr);
		mapForSign.put("package", pkg);
		mapForSign.put("signType", signType);
		paySign = WxCryptUtil.createSign(mapForSign, wxMpConfigStorage.getPartnerKey());

		result.put("timeStamp", timeStamp.toString());
		result.put("nonceStr", nonceStr);
		result.put("pkg", pkg);
		result.put("signType", signType);
		result.put("paySign", paySign);
		return result;
	}

}
