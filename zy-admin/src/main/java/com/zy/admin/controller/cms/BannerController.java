package com.zy.admin.controller.cms;

import com.zy.common.model.query.Page;
import com.zy.common.model.query.PageBuilder;
import com.zy.common.model.result.ResultBuilder;
import com.zy.common.model.ui.Grid;
import com.zy.component.BannerComponent;
import com.zy.entity.cms.Banner;
import com.zy.entity.cms.Banner.BannerPosition;
import com.zy.model.query.BannerQueryModel;
import com.zy.service.BannerService;
import com.zy.vo.BannerAdminVo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RequestMapping("/banner")
@Controller
public class BannerController {

	Logger logger = LoggerFactory.getLogger(BannerController.class);

	@Autowired
	private BannerService bannerService;

	@Autowired
	private BannerComponent bannerComponent;

	@RequiresPermissions("banner:view")
	@RequestMapping(method = RequestMethod.GET)
	public String list(Model model) {
		model.addAttribute("bannerPositions", BannerPosition.values());
		return "cms/bannerList";
	}

	@RequiresPermissions("banner:view")
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public Grid<BannerAdminVo> list(BannerQueryModel bannerQueryModel) {
		Page<Banner> page = bannerService.findPage(bannerQueryModel);
		Page<BannerAdminVo> voPage = PageBuilder.copyAndConvert(page, bannerComponent::buildAdminVo);
		return new Grid<>(voPage);
	}

	@RequiresPermissions("banner:edit")
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public String create(Model model) {
		model.addAttribute("bannerPositions", BannerPosition.values());
		return "cms/bannerCreate";
	}

	@RequiresPermissions("banner:edit")
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public String create(RedirectAttributes redirectAttributes, Banner banner) {
		try {
			bannerService.create(banner);
			redirectAttributes.addFlashAttribute(ResultBuilder.ok("Banner保存成功"));
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute(ResultBuilder.error("Banner保存失败, 原因" + e.getMessage()));
		}
		return "redirect:/banner";
	}

	@RequiresPermissions("banner:edit")
	@RequestMapping(value = "/update", method = RequestMethod.GET)
	public String update(Long id, Model model) {
		Banner banner = bannerService.findOne(id);
		model.addAttribute("banner", bannerComponent.buildAdminVo(banner));
		return "cms/bannerUpdate";
	}

	@RequiresPermissions("banner:edit")
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(Banner banner, RedirectAttributes redirectAttributes) {
		try {
			bannerService.modify(banner);
			redirectAttributes.addFlashAttribute(ResultBuilder.ok("Banner保存成功"));
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute(ResultBuilder.error("Banner保存失败, 原因" + e.getMessage()));
		}
		return "redirect:/banner";
	}

	@RequiresPermissions("banner:edit")
	@RequestMapping("/delete")
	public String delete(Long id, RedirectAttributes redirectAttributes) {
		try {
			bannerService.delete(id);
			redirectAttributes.addFlashAttribute(ResultBuilder.ok("Banner删除成功"));
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute(ResultBuilder.error("Banner删除失败, 原因" + e.getMessage()));
		}
		return "redirect:/banner";
	}

	@RequiresPermissions("banner:edit")
	@RequestMapping(value = "/release")
	public String release(Long id, RedirectAttributes redirectAttributes, boolean isReleased) {
		String released = isReleased ? "上架" : "下架";
		try {
			bannerService.release(id, isReleased);
			redirectAttributes.addFlashAttribute(ResultBuilder.ok("Banner" + released + "成功"));
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute(ResultBuilder.error("Banner" + released + "失败, 原因" + e.getMessage()));
		}
		return "redirect:/banner";
	}
}
