package com.zy.mobile.controller.ucenter;

import static com.zy.common.util.ValidateUtils.NOT_NULL;
import static com.zy.common.util.ValidateUtils.validate;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.zy.component.TourComponent;
import com.zy.util.GcUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.zy.Config;
import com.zy.common.exception.BizException;
import com.zy.common.model.query.Page;
import com.zy.common.model.query.PageBuilder;
import com.zy.common.model.result.Result;
import com.zy.common.model.result.ResultBuilder;
import com.zy.component.ProductComponent;
import com.zy.component.ReportComponent;
import com.zy.component.UserComponent;
import com.zy.entity.act.Policy;
import com.zy.entity.act.PolicyCode;
import com.zy.entity.act.Report;
import com.zy.entity.fnc.Profit;
import com.zy.entity.fnc.Transfer;
import com.zy.entity.mal.Product;
import com.zy.entity.sys.ConfirmStatus;
import com.zy.entity.usr.User;
import com.zy.entity.usr.User.UserRank;
import com.zy.entity.usr.UserInfo;
import com.zy.model.BizCode;
import com.zy.model.Constants;
import com.zy.model.Principal;
import com.zy.model.query.ProductQueryModel;
import com.zy.model.query.ProfitQueryModel;
import com.zy.model.query.ReportQueryModel;
import com.zy.model.query.TransferQueryModel;
import com.zy.service.JobService;
import com.zy.service.PolicyCodeService;
import com.zy.service.PolicyService;
import com.zy.service.ProductService;
import com.zy.service.ProfitService;
import com.zy.service.ReportService;
import com.zy.service.TransferService;
import com.zy.service.UserInfoService;
import com.zy.service.UserService;

@RequestMapping("/u/report")
@Controller
public class UcenterReportController {

	@Autowired
	private UserService userService;

	@Autowired
	private ReportService reportService;

	@Autowired
	private PolicyService policyService;

	@Autowired
	private PolicyCodeService policyCodeService;
	
	@Autowired
	private ProfitService profitService;

	@Autowired
	private TransferService transferService;

	@Autowired
	private JobService jobService;

	@Autowired
	private UserInfoService userInfoService;

	@Autowired
	private UserComponent userComponent;
	
	@Autowired
	private ReportComponent reportComponent;

	@Autowired
	private ProductService productService;

	@Autowired
	private ProductComponent productComponent;

	@Autowired
	private Config config;

	@Autowired
	private TourComponent tourComponent;
	
	@RequestMapping()
	public String list(Principal principal, Model model) {
		Page<Report> page = reportService.findPage(ReportQueryModel.builder().userIdEQ(principal.getUserId()).pageNumber(0).pageSize(6).build());
		model.addAttribute("page", PageBuilder.copyAndConvert(page, reportComponent::buildListVo));
		model.addAttribute("timeLT", DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
		
		User user = userService.findOne(principal.getUserId());
		model.addAttribute("userRank", user.getUserRank());
		return "ucenter/report/reportList";
	}

	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public Result<?> product(Model model, Date timeLT, @RequestParam(required = true) Integer pageNumber, Principal principal) {
		if (timeLT == null) {
			timeLT = new Date();
		}

		Map<String, Object> map = new HashMap<>();
		Page<Report> page = reportService.findPage(ReportQueryModel.builder().userIdEQ(principal.getUserId()).pageNumber(pageNumber).pageSize(6).build());
		map.put("page", PageBuilder.copyAndConvert(page, reportComponent::buildListVo));
		map.put("timeLT", DateFormatUtils.format(timeLT, "yyyy-MM-dd HH:mm:ss"));

		return ResultBuilder.result(map);
	}

	@RequestMapping(value = "create", method = GET)
	public String create(Principal principal, Model model) {
		/*if(!isCompletedUserInfo(principal.getUserId())) {
			redirectAttributes.addFlashAttribute(Constants.MODEL_ATTRIBUTE_RESULT, ResultBuilder.error("请先完成用户信息认证!"));
			return "redirect:/u/report";
		}*/
		
		User user = userService.findOne(principal.getUserId());
		
//		if (user.getUserRank() == User.UserRank.V0) {
//			Long parentId = user.getParentId();
//			Long inviterId = user.getInviterId();
//			if (parentId != null) {
//				User parent = userService.findOne(parentId);
//				if (parent != null) {
//					model.addAttribute("parent", userComponent.buildListVo(parent));
//				}
//			}
//			if (inviterId != null) {
//				User inviter = userService.findOne(inviterId);
//				if (inviter != null) {
//					model.addAttribute("inviter", userComponent.buildListVo(inviter));
//				}
//			}
//		}
		ProductQueryModel productQueryModel = new ProductQueryModel();
		productQueryModel.setIdIN(new Long[] {1L, 2L}); // TODO 暂时写死
		List<Product> products = productService.findAll(productQueryModel);
		model.addAttribute("products", products.stream().map(productComponent::buildListVo).collect(Collectors.toList()));
		
		model.addAttribute("userRank", user.getUserRank());
		model.addAttribute("jobs", this.jobService.findAll());
		//model.addAttribute("tags", getTags());
		return "ucenter/report/reportCreate";
	}

	@RequestMapping(value = "/create", method = POST)
	public String create(boolean hasPolicy, Report report, Policy policy, Long parentId, Principal principal, Model model, RedirectAttributes redirectAttributes) {
		Product product = productService.findOne(report.getProductId());
		validate(product, NOT_NULL, "product id " + report.getProductId() + " not found");
		
		User user = userService.findOne(principal.getUserId());
		try {
//			//设置上级
//			if (user.getUserRank() == UserRank.V0 && user.getParentId() == null) {
//				if (parentId == null) {
//					throw new BizException(BizCode.ERROR, "首次下单必须填写邀请人");
//				}
//				userService.setParentId(principal.getUserId(), parentId);
//			}
			
			if(config.isOld(report.getProductId()) && hasPolicy){
				throw new BizException(BizCode.ERROR, "一代产品不能提交保单.");
			}
			
			//保单校验
			if(hasPolicy && policy != null) {
				if(policy.getCode() == null) {
					throw new BizException(BizCode.ERROR, "请填写保险单号");
				}
				PolicyCode policyCode = policyCodeService.findByCode(policy.getCode());
				if(policyCode == null) {
					throw new BizException(BizCode.ERROR, "保险单号不存在[" + policy.getCode() + "]");
				}
				if(policyCode.getIsUsed()) {
					throw new BizException(BizCode.ERROR, "保险单号已被使用[" + policy.getCode() + "]");
				}
			}
			report.setUserId(principal.getUserId());
			Report persistentReport = reportService.create(report);
			if(hasPolicy && policy != null) {
				policy.setUserId(principal.getUserId());
				policy.setReportId(persistentReport.getId());
				policyService.create(policy);
			}
		} catch (Exception e) {
			model.addAttribute("report", report);
			model.addAttribute("policy", policy);
			model.addAttribute(Constants.MODEL_ATTRIBUTE_RESULT, ResultBuilder.error(e.getMessage()));
			return create(principal, model);
		}
		redirectAttributes.addFlashAttribute(ResultBuilder.ok("上传检测报告成功"));
		return "redirect:/u/report";
	}



	@RequestMapping(value = "/ajaxCreate", method = POST)
	@ResponseBody
	public  Result<?> ajaxCreate(boolean hasPolicy, Report report, Long parentId, Principal principal, Model model, RedirectAttributes redirectAttributes) {
		Product product = productService.findOne(report.getProductId());
		validate(product, NOT_NULL, "product id " + report.getProductId() + " not found");
		try {
			if (report.getProductNumber()!=null) {
				PolicyCode policyCode = policyCodeService.findByCode(report.getProductNumber());
				if (policyCode == null) {
					return ResultBuilder.error("产品编号不存在");
				}
				if(policyCode.getIsUsed()) {
					return ResultBuilder.error("产品编号已被使用");
				}
				if (policyCode.getTourUsed()!=null) {
					if (policyCode.getTourUsed()) {
						return ResultBuilder.error("产品编号已被使用");
					}
				}
			}
			report.setUserId(principal.getUserId());
			Report persistentReport = reportService.create(report);
			return ResultBuilder.ok(persistentReport.getId()+"");
		} catch (Exception e) {
			e.printStackTrace();
			return ResultBuilder.error(e.getMessage());
		}
	}
	@RequestMapping(value = "/{id}", method = GET)
	public String detail(@PathVariable Long id, Principal principal, Model model) {
		Report report = findAndValidate(id, principal.getUserId());
		model.addAttribute("report", reportComponent.buildDetailVo(report));
		if(report.getIsSettledUp()){
			List<Profit> profits = profitService.findAll(
					ProfitQueryModel.builder()
					.profitTypeEQ(Profit.ProfitType.数据奖)
					.refIdEQ(id)
					.userIdEQ(principal.getUserId())
					.build());
			model.addAttribute("profits", profits);
			
			List<Transfer> transfers = transferService.findAll(
					TransferQueryModel.builder()
					.transferTypeEQ(Transfer.TransferType.数据奖)
					.refIdEQ(id)
					.toUserIdEQ(principal.getUserId())
					.build());
			model.addAttribute("transfers", transfers);
		}
		return "ucenter/report/reportDetail";
	}

	@RequestMapping(value = "/edit", method = GET)
	public String edit(@RequestParam Long id, Principal principal, Model model, RedirectAttributes redirectAttributes) {
		/*if(!isCompletedUserInfo(principal.getUserId())) {
			redirectAttributes.addFlashAttribute(Constants.MODEL_ATTRIBUTE_RESULT, ResultBuilder.error("请先完成用户信息认证!"));
			return "redirect:/u/report";
		}*/
		Report report = findAndValidate(id, principal.getUserId());
		validate(report, NOT_NULL, "report id" + id + " not found");
		if(report.getConfirmStatus() != ConfirmStatus.未通过 && report.getPreConfirmStatus() != ConfirmStatus.待审核) {
			redirectAttributes.addFlashAttribute(Constants.MODEL_ATTRIBUTE_RESULT, ResultBuilder.error("检测报告状态不匹配,不能修改"));
			return "redirect:/u/report";
		}
		model.addAttribute("jobs", this.jobService.findAll());
		//model.addAttribute("tags", getTags());
		model.addAttribute("report", reportComponent.buildDetailVo(report));
		return "ucenter/report/reportEdit";
	}

	@RequestMapping(value = "/edit", method = POST)
	public String edit(Report report, Principal principal, Model model, RedirectAttributes redirectAttributes) {
		/*if(!isCompletedUserInfo(principal.getUserId())) {
			redirectAttributes.addFlashAttribute(Constants.MODEL_ATTRIBUTE_RESULT, ResultBuilder.error("请先完成用户信息认证!"));
			return "redirect:/u/report";
		}*/
		Long id = report.getId();
		validate(id, NOT_NULL, "id is null");
		Report persistence = reportService.findOne(id);
		validate(persistence, NOT_NULL, "report id" + id + " not found");
		if(persistence.getConfirmStatus() != ConfirmStatus.未通过 && persistence.getPreConfirmStatus() != ConfirmStatus.待审核) {
			redirectAttributes.addFlashAttribute(Constants.MODEL_ATTRIBUTE_RESULT, ResultBuilder.error("检测报告状态不匹配,不能修改"));
			return "redirect:/u/report";
		}
		try {
			reportService.modify(report);
		} catch (Exception e) {
			model.addAttribute(ResultBuilder.error(e.getMessage()));
			return edit(report.getId(), principal, model, redirectAttributes);
		}
		redirectAttributes.addFlashAttribute(ResultBuilder.ok("更新检测报告成功"));
		return "redirect:/u/report";
	}

	private boolean isCompletedUserInfo(Long userId) {
		UserInfo userInfo = userInfoService.findByUserId(userId);
		if(userInfo == null) {
			return false;
		}
		if(userInfo.getConfirmStatus() == ConfirmStatus.已通过) {
			return true;
		}
		return false;
	}
	
	private Report findAndValidate(Long id, Long userId) {
		Report report = reportService.findOne(id);
		validate(report, NOT_NULL, "report id" + id + " not found");
		validate(report.getUserId(), v -> userId.equals(v), "权限不足");
		return report;
	}


	@RequestMapping(value = "/insuranceInfo")
	public String insuranceInfo(Long reportId, Model model, Principal principal) {
		Long userId = principal.getUserId(); //userInfoService
		String productNumber = tourComponent.findproductNumber(reportId);
		model.addAttribute("userinfoVo",tourComponent.findUserInfoVo(userId));
		model.addAttribute("reportId", reportId);
		model.addAttribute("productNumber", productNumber);
		return "ucenter/report/insurance";
	}

	@RequestMapping(value = "/addInsuranceInfo", method = POST)
	@ResponseBody
	public Result<?> addInsuranceInfo( Policy policy, Long reportId, Principal principal) {
		try {
			//保单校验
			if(policy != null) {
				PolicyCode policyCode = policyCodeService.findByCode(policy.getCode());
				if(policyCode == null) {
					return ResultBuilder.error("产品编号不存在");
				}
				if(policyCode.getIsUsed()) {
					return ResultBuilder.error("产品编号已被使用");
				}
			}
				policy.setUserId(principal.getUserId());
				policy.setReportId(reportId);
				policy.setImage1(GcUtils.getThumbnail(policy.getImage1(), 750, 450));
				policyService.create(policy);

			return ResultBuilder.ok(null);
		} catch (Exception e) {
			return ResultBuilder.error(e.getMessage());
		}
	}

}
