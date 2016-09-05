package com.zy.admin.controller.act;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.zy.common.model.query.Page;
import com.zy.common.model.query.PageBuilder;
import com.zy.common.model.result.ResultBuilder;
import com.zy.common.model.ui.Grid;
import com.zy.component.ActivityComponent;
import com.zy.entity.act.Activity;
import com.zy.model.Constants;
import com.zy.model.query.ActivityQueryModel;
import com.zy.service.ActivityService;
import com.zy.vo.ActivityAdminVo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import static com.zy.common.util.ValidateUtils.NOT_NULL;
import static com.zy.common.util.ValidateUtils.validate;

@RequestMapping("/activity")
@Controller
public class ActivityController {

	Logger logger = LoggerFactory.getLogger(ActivityController.class);

	@Autowired
	private ActivityService activityService;

	@Autowired
	private ActivityComponent activityComponent;

	@RequiresPermissions("activity:view")
	@RequestMapping(method = RequestMethod.GET)
	public String list(Model model) {
		return "act/activityList";
	}

	@RequiresPermissions("activity:view")
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public Grid<ActivityAdminVo> list(ActivityQueryModel activityQueryModel) {
		Page<Activity> page = activityService.findPage(activityQueryModel);
		Page<ActivityAdminVo> voPage = PageBuilder.copyAndConvert(page, v -> activityComponent.buildAdminVo(v, false));
		return new Grid<>(voPage);
	}

	@RequiresPermissions("activity:view")
	@RequestMapping(value = "detail", method = RequestMethod.GET)
	public String detail(Model model, Long id) {
		model.addAttribute("activity", activityComponent.buildAdminFullVo(activityService.findOne(id)));
		return "act/activityDetail";
	}

	@RequiresPermissions("activity:edit")
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public String create(Model model) {
		return "act/activityCreate";
	}

	@RequiresPermissions("activity:edit")
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public String create(RedirectAttributes redirectAttributes, Activity activity) {
		try {
			activityService.create(activity);
			redirectAttributes.addFlashAttribute(ResultBuilder.ok("活动保存成功"));
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute(ResultBuilder.error("活动保存失败, 原因" + e.getMessage()));
		}
		return "redirect:/activity";
	}

	@RequiresPermissions("activity:edit")
	@RequestMapping(value = "/update", method = RequestMethod.GET)
	public String update(Long id, Model model) {
		Activity activity = activityService.findOne(id);
		model.addAttribute("activity", activityComponent.buildAdminVo(activity, true));
		return "act/activityUpdate";
	}

	@RequiresPermissions("activity:edit")
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(Activity activity, RedirectAttributes redirectAttributes) {
		try {
			activityService.modify(activity);
			redirectAttributes.addFlashAttribute(ResultBuilder.ok("活动保存成功"));
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute(ResultBuilder.error("活动保存失败, 原因" + e.getMessage()));
		}
		return "redirect:/activity";
	}

	@RequiresPermissions("activity:edit")
	@RequestMapping(value = "/release")
	public String release(Long id, RedirectAttributes redirectAttributes, boolean isReleased) {
		String released = isReleased ? "上架" : "下架";
		try {
			activityService.release(id, isReleased);
			redirectAttributes.addFlashAttribute(ResultBuilder.ok("活动" + released + "成功"));
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute(ResultBuilder.error("活动" + released + "失败, 原因" + e.getMessage()));
		}
		return "redirect:/activity";
	}

	@RequestMapping(value = "/signInQrCode", produces = "image/jpeg")
	@ResponseBody
	public BufferedImage signInQrCode(@RequestParam Long id) throws WriterException {
		Activity activity = activityService.findOne(id);

		validate(activity, NOT_NULL, "activity id " + id + " is not found");
		String qrCodeUrl = Constants.URL_MOBILE + "/u/activity/signIn?id=" + id;
		MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
		Map<EncodeHintType, String> hints = new HashMap<>();
		hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
		BitMatrix bitMatrix = multiFormatWriter.encode(qrCodeUrl, BarcodeFormat.QR_CODE, 480, 480, hints);
		return MatrixToImageWriter.toBufferedImage(bitMatrix);
	}

	@RequestMapping(value = "/detailQrCode", produces = "image/jpeg")
	@ResponseBody
	public BufferedImage detailQrCode(@RequestParam Long id) throws WriterException {
		Activity activity = activityService.findOne(id);

		validate(activity, NOT_NULL, "activity id " + id + " is not found");
		String qrCodeUrl = Constants.URL_MOBILE + "/activity/" + id;
		MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
		Map<EncodeHintType, String> hints = new HashMap<>();
		hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
		BitMatrix bitMatrix = multiFormatWriter.encode(qrCodeUrl, BarcodeFormat.QR_CODE, 480, 480, hints);
		return MatrixToImageWriter.toBufferedImage(bitMatrix);
	}


}
