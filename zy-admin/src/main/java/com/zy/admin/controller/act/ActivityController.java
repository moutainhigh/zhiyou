package com.zy.admin.controller.act;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.zy.common.model.query.Page;
import com.zy.common.model.query.PageBuilder;
import com.zy.common.model.result.Result;
import com.zy.common.model.result.ResultBuilder;
import com.zy.common.model.ui.Grid;
import com.zy.common.util.ExcelUtils;
import com.zy.common.util.WebUtils;
import com.zy.component.ActivityComponent;
import com.zy.component.ActivityReportComponent;
import com.zy.entity.act.Activity;
import com.zy.entity.act.ActivityApply;
import com.zy.entity.star.Lesson;
import com.zy.entity.usr.User;
import com.zy.model.Constants;
import com.zy.model.query.ActivityQueryModel;
import com.zy.model.query.ActivityReportQueryModel;
import com.zy.model.query.LessonQueryModel;
import com.zy.service.ActivityApplyService;
import com.zy.service.ActivityService;
import com.zy.service.LessonService;
import com.zy.util.GcUtils;
import com.zy.vo.ActionReportExportVo;
import com.zy.vo.ActivityAdminVo;
import com.zy.vo.ActivityReportVo;
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

import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;
import java.util.stream.Collectors;

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

	@Autowired
	private ActivityApplyService activityApplyService;

	@Autowired
	private ActivityReportComponent activityReportComponent;

	@Autowired
	private LessonService lessonService;

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
		LessonQueryModel lessonQueryModel = new LessonQueryModel();
		lessonQueryModel.setViewFlageEQ(1);
		Page<Lesson>page = lessonService.findPage(lessonQueryModel);
		model.addAttribute("lessonId",activity.getLessonId());
		model.addAttribute("lessonList",page.getData());
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
		/*String qrCodeUrl = Constants.URL_MOBILE + "/u/activity/signIn?id=" + id;*/
		String qrCodeUrl = "http://192.168.1.222:8088/" + "/u/activity/signIn?id=" + id;
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

	/**
	 * 跳转到 活动报表导出页面
	 * @return
     */
	@RequiresPermissions("activityReport:view")
	@RequestMapping(value = "/activityReport")
	public String toActivityReport(Model model){
		model.addAttribute("userRankMap", Arrays.asList(User.UserRank.values()).stream().collect(Collectors.toMap(v->v, v-> GcUtils.getUserRankLabel(v),(u, v) -> { throw new IllegalStateException(String.format("Duplicate key %s", u)); }, LinkedHashMap::new)) );
		return "act/activityReport";
	}

	/**
	 * 查询报表数据数据
	 * @return
     */
	@RequiresPermissions("activity:view")
	@RequestMapping(value = "activityReportList" ,method = RequestMethod.POST)
	@ResponseBody
	public Grid<ActivityReportVo>  activityReportList(ActivityReportQueryModel activityReportQueryModel){
          if("0".equals(activityReportQueryModel.getInitFalg())){//不需要初始化页面
			  Page<ActivityReportVo> page = new Page<ActivityReportVo>();
				  page.setPageNumber(0);
				  page.setPageSize(20);
				  page.setTotal(0L);
			    return new Grid<>(page);
		  }
		//处理逻辑
		Page<ActivityReportVo> voPage=null;
		try {
			Map<String,Object> dataMap = activityApplyService.findPageByReport(activityReportQueryModel);
			Page<ActivityApply> page =(Page<ActivityApply>)dataMap.get("page");
			voPage = PageBuilder.copyAndConvert(page, v-> activityReportComponent.buildReportVo(v, false,dataMap));
		}catch (Exception e){
			e.printStackTrace();
		}
		return new Grid<>(voPage);
	}

	/**
	 * 统计活动数据（分类统计）
	 * @param activityReportQueryModel
	 * @return
     */
	@RequestMapping(value = "/sum", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> sum(ActivityReportQueryModel activityReportQueryModel) {
		Map<String,Object> dataMap = activityApplyService.findPageByReport(activityReportQueryModel);
		return ResultBuilder.result(dataMap);
	}


	@RequiresPermissions("activityReport:export")
	@RequestMapping("/export")
	public String export(ActivityReportQueryModel activityReportQueryModel,HttpServletResponse response) throws IOException {

		  activityReportQueryModel.setPageSize(null);
		  activityReportQueryModel.setPageNumber(null);
		  Map<String,Object> dataMap = activityApplyService.findPageByReport(activityReportQueryModel);
		  Page<ActivityApply> page =(Page<ActivityApply>)dataMap.get("page");
		  List<ActivityApply> activityApplyList = page.getData();
		  String fileName = "活动报表_签到.xlsx";
		  if(0==activityReportQueryModel.getActivityApplyStatus()){
			  fileName = "活动报表_未支付.xlsx";
		  }else if(1==activityReportQueryModel.getActivityApplyStatus()){
			  fileName = "活动报表_已支付.xlsx";
		  }
		  WebUtils.setFileDownloadHeader(response, fileName);

		List<ActionReportExportVo> actionReportExportVoList= activityApplyList.stream().map(activityReportComponent::buildExportVo).collect(Collectors.toList());
		OutputStream os = response.getOutputStream();
		ExcelUtils.exportExcel(actionReportExportVoList, ActionReportExportVo.class, os);
		return null;
	}

	/**
	 * 查询  所有的课程
	 * @return
     */
	@RequestMapping(value = "selectLesson" ,method = RequestMethod.POST)
	@ResponseBody
	public Result<?> selectLesson(){
		LessonQueryModel lessonQueryModel = new LessonQueryModel();
		lessonQueryModel.setViewFlageEQ(1);
		Page<Lesson>page = lessonService.findPage(lessonQueryModel);
	   /* Map<String,Long>map=page.getData().stream().collect(Collectors.toMap(Lesson::getTitle, lesson -> lesson.getId()));*/
		return ResultBuilder.result(page.getData());
	}
}
