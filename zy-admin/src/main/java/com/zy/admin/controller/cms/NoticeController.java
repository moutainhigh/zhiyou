package com.zy.admin.controller.cms;

import com.zy.common.model.query.Page;
import com.zy.common.model.result.ResultBuilder;
import com.zy.common.model.ui.Grid;
import com.zy.entity.cms.Notice;
import com.zy.entity.cms.Notice.NoticeType;
import com.zy.model.query.NoticeQueryModel;
import com.zy.service.NoticeService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@RequestMapping("/notice")
@Controller
public class NoticeController {

	Logger logger = LoggerFactory.getLogger(NoticeController.class);

	@Autowired
	private NoticeService noticeService;

	@RequiresPermissions("notice:view")
	@RequestMapping(method = RequestMethod.GET)
	public String list(Model model) {
		model.addAttribute("noticeTypes", NoticeType.values());
		return "cms/noticeList";
	}

	@RequiresPermissions("notice:view")
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public Grid<Notice> list(NoticeQueryModel noticeQueryModel) {
		Page<Notice> page = noticeService.findPage(noticeQueryModel);
		return new Grid<Notice>(page);
	}

	@RequiresPermissions("notice:edit")
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public String create(Model model) {
		model.addAttribute("noticeTypes", NoticeType.values());
		return "cms/noticeCreate";
	}

	@RequiresPermissions("notice:edit")
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public String create(RedirectAttributes redirectAttributes, Notice notice, Model model) {
		try {
			noticeService.create(notice);
			redirectAttributes.addFlashAttribute(ResultBuilder.ok("Notice[" + notice.getTitle() + "]保存成功"));
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute(ResultBuilder.error("Notice保存失败，原因：" + e.getMessage()));
		}
		return "redirect:/notice";
	}

	@RequiresPermissions("notice:edit")
	@RequestMapping(value = "/update/{id}", method = RequestMethod.GET)
	public String update(@PathVariable Long id, Model model) {
		Notice notice = noticeService.findOne(id);
		model.addAttribute("notice", notice);
		model.addAttribute("noticeTypes", NoticeType.values());
		return "cms/noticeUpdate";
	}

	@RequiresPermissions("notice:edit")
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(Notice notice, RedirectAttributes redirectAttributes) {
		try {
			noticeService.update(notice);
			redirectAttributes.addFlashAttribute(ResultBuilder.ok("Notice[" + notice.getTitle() + "]保存成功"));
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute(ResultBuilder.error("Notice保存失败，原因：" + e.getMessage()));
		}
		return "redirect:/notice";
	}

	@RequiresPermissions("notice:edit")
	@RequestMapping("/delete/{id}")
	public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes, Model model) {
		if (id == null) {
			model.addAttribute(ResultBuilder.error("id为空"));
			return "cms/noticeList";
		}
		Notice notice = noticeService.findOne(id);
		if (notice == null) {
			model.addAttribute(ResultBuilder.error("没有查询到数据，id = " + id));
			return "cms/noticeList";
		}
		try {
			noticeService.delete(id);
			redirectAttributes.addFlashAttribute(ResultBuilder.ok("Notice[" + notice.getTitle() + "]删除成功"));
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute(ResultBuilder.error("Notice[[" + notice.getTitle() + "]删除失败,原因：" + e.getMessage()));
		}
		return "redirect:/notice";
	}

}
