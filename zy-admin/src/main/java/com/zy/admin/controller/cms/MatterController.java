package com.zy.admin.controller.cms;

import com.zy.admin.model.AdminPrincipal;
import com.zy.common.model.query.Page;
import com.zy.common.model.query.PageBuilder;
import com.zy.common.model.result.ResultBuilder;
import com.zy.common.model.ui.Grid;
import com.zy.component.MatterComponent;
import com.zy.entity.cms.Matter;
import com.zy.model.query.MatterQueryModel;
import com.zy.service.MatterService;
import com.zy.vo.MatterVo;
import io.gd.generator.api.query.Direction;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
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

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 资源管理
 * @Author :Xuwq
 */
@Controller
@RequestMapping("/matter")
public class MatterController {

	final Logger logger = LoggerFactory.getLogger(MatterController.class);

	@Autowired
	private MatterComponent matterComponent;

	@Autowired
	private MatterService matterService;

	@RequestMapping(method = RequestMethod.GET)
	public String list(Model model) {

		return "cms/matterList";
	}

	/**
	 * 资源列表查询
	 * @param matterQueryModel
	 * @return
     */
	@RequiresPermissions("matter:view")
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public Grid<MatterVo> list(MatterQueryModel matterQueryModel) {
		matterQueryModel.setOrderBy("uploadTime");
		matterQueryModel.setDirection(Direction.DESC);
		Page<Matter> page = matterService.findPage(matterQueryModel);
		List<MatterVo> list = page.getData().stream().map(v -> {
			return matterComponent.buildMatterVo(v,false);
		}).collect(Collectors.toList());
		return new Grid<MatterVo>(PageBuilder.copyAndConvert(page, list));
	}

	@RequiresPermissions("matter:edit")
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public String create(Model model, RedirectAttributes redirectAttributes) {

		return "cms/matterCreate";
	}

	/**
	 * 新增资源
	 * @param redirectAttributes
	 * @param matter
     * @return
     */
	@RequiresPermissions("matter:edit")
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public String create(RedirectAttributes redirectAttributes, Matter matter) {
		try {
			matter.setUploadUserId(getPrincipalUserId());
			Date now = new Date();
			matter.setUploadTime(now);
			matterService.create(matter);
			redirectAttributes.addFlashAttribute(ResultBuilder.ok("资源保存成功"));
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute(ResultBuilder.error("资源保存失败, 原因" + e.getMessage()));
		}
		return "redirect:/matter";
	}

	@RequiresPermissions("matter:edit")
	@RequestMapping(value = "/update", method = RequestMethod.GET)
	public String update(Long id, Model model) {
		MatterQueryModel matterQueryModel = new MatterQueryModel();
		matterQueryModel.setId(id);
		Matter matter = matterService.findOne(matterQueryModel);
		model.addAttribute("matter", matterComponent.buildMatterVo(matter,false));
		return "cms/matterUpdate";
	}

	/**
	 * 编辑保存
	 * @param matter
	 * @param redirectAttributes
     * @return
     */
	@RequiresPermissions("matter:edit")
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(Matter matter, RedirectAttributes redirectAttributes) {
		try {
			matter.setUrl(StringUtils.substringBefore(matter.getUrl(),"@"));
			matterService.updateMatter(matter);
			redirectAttributes.addFlashAttribute(ResultBuilder.ok("资源保存成功"));
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute(ResultBuilder.error("资源保存失败, 原因" + e.getMessage()));
		}
		return "redirect:/matter";
	}

	/**
	 * 资源状态变更
	 * @param id
	 * @param status
     * @return
     */
	@RequiresPermissions("article:edit")
	@RequestMapping(value = "/updateStatus")
	public String updateStatus(Long id, RedirectAttributes redirectAttributes, @RequestParam Integer status) {
		String updateStatus = null ;
		if (status == 0){
			updateStatus = "下架";
		}else if (status == 1){
			updateStatus = "上架";
		}
		try {
			matterService.updateStatus(id, status);
			redirectAttributes.addFlashAttribute(ResultBuilder.ok("资源" + updateStatus + "成功"));
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute(ResultBuilder.error("资源" + updateStatus + "失败, 原因" + e.getMessage()));
		}
		return "redirect:/matter";
	}

	/**
	 * 获取登录人的id
	 * @return
     */
	private Long getPrincipalUserId() {
		AdminPrincipal principal = (AdminPrincipal) SecurityUtils.getSubject().getPrincipal();
		return principal.getUserId();
	}
}
