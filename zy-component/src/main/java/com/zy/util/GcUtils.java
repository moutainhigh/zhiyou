package com.zy.util;

import com.zy.common.exception.ValidationException;
import com.zy.common.util.Identities;
import com.zy.entity.act.Policy;
import com.zy.entity.fnc.*;
import com.zy.entity.mal.Order;
import com.zy.entity.mal.ProductReplacement;
import com.zy.entity.mergeusr.MergeUser;
import com.zy.entity.sys.ConfirmStatus;
import com.zy.entity.usr.User;
import com.zy.model.Constants;
import com.zy.model.Principal;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;

import static com.zy.model.Constants.ALIYUN_URL_IMAGE;

public class GcUtils {

	public static String getHost() {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		return request.getRemoteAddr();
	}

	public static Principal getPrincipal() {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		HttpSession session = request.getSession();
		Principal principal = session != null ? (Principal) session.getAttribute(Constants.SESSION_ATTRIBUTE_PRINCIPAL1) : null;
		return principal;
	}

	public static String resolveRedirectUrl(HttpServletRequest request) {
		String method = request.getMethod();
		String queryString = request.getQueryString();
		String redirectUrl = null;
		if (method.equalsIgnoreCase("GET")) {
			redirectUrl = request.getRequestURL() + (queryString == null ? "" : "?" + queryString);
		} else {
			String referer = request.getHeader("Referer");
			if (StringUtils.isNotBlank(referer)) {
				redirectUrl = referer;
			}
		}
		return redirectUrl;
	}

	public static String getTimeLabel(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
		String result = null;
		if (hourOfDay >= 0 && hourOfDay < 6) {
			result = "凌晨";
		} else if (hourOfDay >= 6 && hourOfDay < 12) {
			result = "早上";
		} else if (hourOfDay >= 12 && hourOfDay < 18) {
			result = "下午";
		} else if (hourOfDay >= 18 && hourOfDay < 24) {
			result = "晚上";
		}
		return result;
	}

	public static String getDateLabel(Date date, String formatterPattern) {
		String fmtDateString = "";
		if (date == null) {
			return fmtDateString;
		}
		if (formatterPattern == null) {
			formatterPattern = "yyyy-MM-dd";
		}
		Long subTime = new Date().getTime() - date.getTime();
		// long days = subTime / (1000 * 60 * 60 * 24);
		long hours = subTime / (1000 * 60 * 60);
		long minutes = subTime / (1000 * 60);
		if (minutes == 0) {
			fmtDateString = "刚刚";
		} else if (minutes < 60 && minutes > 0) {
			fmtDateString = minutes + "分钟前";
		} else if (minutes < 24 * 60 && minutes > 60) {
			fmtDateString = hours + "小时前";
		} else if (minutes <= 1 * 60 * 24 && minutes > 60 * 24) {
			fmtDateString = "昨天" + DateFormatUtils.format(date, "HH:mm");
		} else {
			fmtDateString = DateFormatUtils.format(date, formatterPattern);
		}
		return fmtDateString;
	}

	public static String getDateLabel(Date date) {
		return getDateLabel(date, null);
	}

	public static String getThumbnail(String url) {
		return getThumbnail(url, 160, 160);
	}

	public static String getThumbnail(String url, int width, int height) {
		return getThumbnail(url, width, height, true, true);
	}

	/*
	 * isCut: 是否裁剪 shortFirst: 短边优先 /
	 */
	private static String getThumbnail(String url, int width, int height, boolean isCut, boolean shortFirst) {
		if (StringUtils.isBlank(url)) {
			return null;
		}
		if (url.startsWith(ALIYUN_URL_IMAGE)) {
			return url + "@" + height + "h_" + width + "w" + (shortFirst ? "_1e" : "") + (isCut ? "_1c" : "_0c") + ".jpg";
		} else {
			return url;
		}
	}

	public static String formatDate(Date date, String pattern) {
		if (date == null) {
			return null;
		}
		return DateFormatUtils.format(date, pattern);
	}


	public static String formatCurreny(BigDecimal bigDecimal) {
		if (bigDecimal == null) {
			return "";
		}
		DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
		return decimalFormat.format(bigDecimal);
	}

	public static String overlay(String str, int prefixLen, int suffixLen) {
		int totalLen = str.length();
		if (prefixLen + suffixLen >= totalLen) {
			return str;
		} else {
			int padLen = totalLen - prefixLen - suffixLen;
			return StringUtils.overlay(str, StringUtils.leftPad("", padLen, "*"), prefixLen, totalLen - suffixLen);
		}

	}

	public static String generateTgt() {
		return "tgt-" + Identities.uuid();
	}

	public static void checkImage(MultipartFile file) {
		if (file == null || file.isEmpty()) {
			throw new ValidationException("[上传图片]图片不能为空");
		}

		String[] typeAllowed = new String[] { "image/jpeg", "image/png", "image/bmp", "image/gif" };
		String contentType = file.getContentType();
		List<String> types = new ArrayList<String>();
		for (String type : typeAllowed) {
			types.add(type);
		}
		if (!types.contains(contentType)) {
			throw new ValidationException("[上传图片]文件类型必须为" + StringUtils.join(typeAllowed, ","));
		}

		int maxSize = 4;
		long fileSize = file.getSize();
		if (fileSize > maxSize * 1024 * 1024) {
			throw new ValidationException("[上传图片]文件大小不能超过" + maxSize + "MB");
		}
	}
	public static void checkVideo(MultipartFile file) {
		if (file == null || file.isEmpty()) {
			throw new ValidationException("[上传视频]视频不能为空");
		}

		//String[] typeAllowed = new String[] { "video/Ogg", "video/MPEG4", "video/WebM", "video/flv" };
		String[] typeAllowed = new String[] {  "video/mp4" };
		String contentType = file.getContentType();
		List<String> types = new ArrayList<String>();
		for (String type : typeAllowed) {
			types.add(type);
		}
		if (!types.contains(contentType)) {
			throw new ValidationException("[上传视频]文件类型必须为" + StringUtils.join(typeAllowed, ","));
		}

		int maxSize = 3;
		long fileSize = file.getSize()/1048576;
		if (fileSize > maxSize * 1024 ) {
			throw new ValidationException("[上传视频]文件大小不能超过" + maxSize + "GB");
		}
	}
	public static void checkAudio(MultipartFile file) {
		if (file == null || file.isEmpty()) {
			throw new ValidationException("[上传音频]音频不能为空");
		}

		//String[] typeAllowed = new String[] { "video/Ogg", "video/MPEG4", "video/WebM", "video/flv" };
		String[] typeAllowed = new String[] {  "audio/mp3" };
		String contentType = file.getContentType();
		List<String> types = new ArrayList<String>();
		for (String type : typeAllowed) {
			types.add(type);
		}
		if (!types.contains(contentType)) {
			throw new ValidationException("[上传音频]文件类型必须为" + StringUtils.join(typeAllowed, ","));
		}

		int maxSize = 10;
		long fileSize = file.getSize();
		if (fileSize > maxSize * 1024 * 1024) {
			throw new ValidationException("[上传音频]文件大小不能超过" + maxSize + "MB");
		}
	}
	public static void checkDocument(MultipartFile file) {
		if (file == null || file.isEmpty()) {
			throw new ValidationException("[上传文档]文档不能为空");
		}

		//String[] typeAllowed = new String[] { "application/pdf", "application/doc", "application/xls","application/txt" };
		String[] typeAllowed = new String[] {  "application/pdf" , "application/pdfx"};
		String contentType = file.getContentType();
		List<String> types = new ArrayList<String>();
		for (String type : typeAllowed) {
			types.add(type);
		}
		if (!types.contains(contentType)) {
			throw new ValidationException("[上传文档]文件类型必须为" + StringUtils.join(typeAllowed, ","));
		}

		int maxSize = 10;
		long fileSize = file.getSize();
		if (fileSize > maxSize * 1024 * 1024) {
			throw new ValidationException("[上传文档]文件大小不能超过" + maxSize + "MB");
		}
	}

	public static String getUserRankLabel(User.UserRank userRank) {
		if (userRank == User.UserRank.V0) {
			return "普通用户";
		} else if (userRank == User.UserRank.V1) {
			return "VIP";
		} else if (userRank == User.UserRank.V2) {
			return "市级服务商";
		} else if (userRank == User.UserRank.V3) {
			return "省级服务商";
		} else if (userRank == User.UserRank.V4) {
			return "特级服务商";
		} else {
			return "-";
		}
	}

	public static String getMergeUserRankLabel(User.UserRank userRank) {
		if (userRank == User.UserRank.V0) {
			return "普通用户";
		} else if (userRank == User.UserRank.V1) {
			return "VIP";
		} else if (userRank == User.UserRank.V2) {
			return "品牌经理";
		} else if (userRank == User.UserRank.V3) {
			return "品牌合伙人";
		} else if (userRank == User.UserRank.V4) {
			return "联合创始人";
		} else {
			return "-";
		}
	}

	public static String getConfirmStatusStyle(ConfirmStatus confirmStatus) {
		String labelClass = "default";
		switch (confirmStatus) {
			case 待审核:
				labelClass = "danger";
				break;
			case 已通过:
				labelClass = "success";
				break;
			case 未通过:
				labelClass = "default";
				break;
			default:
				break;
		}
		return labelClass;
	}

	public static String getOrderStatusStyle(Order.OrderStatus orderStatus) {
		String labelClass = "default";
		switch (orderStatus) {
			case 待支付:
				labelClass = "danger";
				break;
			case 已支付:
				labelClass = "warning";
				break;
			case 已发货:
				labelClass = "info";
				break;
			case 已完成:
				labelClass = "success";
				break;
			case 已退款:
				labelClass = "default";
				break;
			case 已取消:
				labelClass = "default";
				break;
			case 待确认:
				labelClass = "warning";
				break;
			default:
				break;
		}
		return labelClass;
	}

	public static String getTransferStatusStyle(Transfer.TransferStatus transferStatus) {
		String labelClass = "default";
		switch (transferStatus) {
			case 待转账:
				labelClass = "danger";
				break;
			case 已转账:
				labelClass = "success";
				break;
			case 已取消:
				labelClass = "default";
				break;
			default:
				break;
		}
		return labelClass;
	}

	public static String getProfitStatusStyle(Profit.ProfitStatus profitStatus) {
		String labelClass = "default";
		switch (profitStatus) {
			case 待发放:
				labelClass = "danger";
				break;
			case 已发放:
				labelClass = "success";
				break;
			case 已取消:
				labelClass = "default";
				break;
			default:
				break;
		}
		return labelClass;
	}

	public static String getDepositStatusStyle(Deposit.DepositStatus depositStatus) {
		String labelClass = "default";
		switch (depositStatus) {
			case 待充值:
				labelClass = "danger";
				break;
			case 充值成功:
				labelClass = "success";
				break;
			case 已取消:
				labelClass = "default";
				break;
			case 待确认:
				labelClass = "warning";
			default:
				break;
		}
		return labelClass;
	}

	public static String getWithdrawStatusStyle(Withdraw.WithdrawStatus withdrawStatus) {
		String labelClass = "default";
		switch (withdrawStatus) {
			case 已申请:
				labelClass = "danger";
				break;
			case 提现成功:
				labelClass = "success";
				break;
			case 已取消:
				labelClass = "default";
				break;
			default:
				break;
		}
		return labelClass;
	}

	public static String getPaymentStatusStyle(Payment.PaymentStatus paymentStatus) {
		String labelClass = "default";
		switch (paymentStatus) {
			case 待支付:
				labelClass = "danger";
				break;
			case 已支付:
				labelClass = "success";
				break;
			case 已退款:
				labelClass = "default";
				break;
			case 已取消:
				labelClass = "default";
				break;
			case 待确认:
				labelClass = "warning";
			default:
				break;
		}
		return labelClass;
	}

	public static String getProductReplacementStatusStyle(ProductReplacement.ProductReplacementStatus productReplacementStatus) {
		String labelClass = "default";
		switch (productReplacementStatus) {
			case 已申请:
				labelClass = "warning";
				break;
			case 已发货:
				labelClass = "info";
				break;
			case 已完成:
				labelClass = "success";
				break;
			case 已驳回:
				labelClass = "default";
				break;
			default:
				break;
		}
		return labelClass;
	}

	public static String getPolicyStatusStyle(Policy.PolicyStatus policyStatus) {
		String labelClass = "default";
		switch (policyStatus) {
			case 审核中:
				labelClass = "info";
				break;
			case 已生效:
				labelClass = "success";
				break;
			case 未通过:
				labelClass = "default";
				break;
			case 已到期:
				labelClass = "default";
				break;
			default:
				break;
		}
		return labelClass;
	}

	public static List<String> getRootNames() {
		return Arrays.asList("金生系统", "创优系统", "优墨系统", "传奇系统", "聚城系统", "汇营系统");
	}

	public static Map<String, Object> getVisitedMap() {
		Map<String, Object> map = new HashMap<>();
		map.put("guanxi", Constants.relationshipList);
		map.put("zuoxi", Constants.restTimeLabelList);
		map.put("shuimian", Constants.sleepQualityList);
		map.put("yinjiu", Constants.drinkList);
		map.put("chouyan", Constants.smokeList);
		map.put("duanlian", Constants.exerciseList);
		map.put("xingqu", Constants.hobbyList);
		map.put("yuanyin", Constants.causeList);
		map.put("jiankang", Constants.healthList);
		map.put("bingzhuang", Constants.sicknessList);
		map.put("bingshi", Constants.familyHistoryList);
		map.put("baojianpin", Constants.healthProductList);
		map.put("yuexiaofei", Constants.monthlyCostList);
		map.put("fenxiang", Constants.productSharingList);
		map.put("zuodaili", Constants.toAgentList);
		map.put("fangshi", Constants.contactWayList);
		return map;
	}

}
