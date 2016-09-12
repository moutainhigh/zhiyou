package com.zy.admin.controller.usr;

import java.util.List;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.zy.common.model.result.Result;
import com.zy.common.model.result.ResultBuilder;
import com.zy.entity.usr.Job;
import com.zy.model.Constants;
import com.zy.service.JobService;

@RequestMapping("/job")
@Controller
public class JobController {

	@Autowired
	private JobService jobService;
	
	@RequiresPermissions("job:view")
	@RequestMapping(method = RequestMethod.GET)
	public String list(Model model) {
		List<Job> jobs = jobService.findAll();
		model.addAttribute("jobs", jobs);
		return "usr/jobList";
	}
	
	@RequiresPermissions("job:edit")
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public String create() {
		return "usr/jobCreate";
	}
	
	@RequiresPermissions("job:edit")
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public String create(Job job, RedirectAttributes redirectAttributes) {
		try {
			jobService.create(job);
			redirectAttributes.addFlashAttribute(Constants.MODEL_ATTRIBUTE_RESULT, ResultBuilder.ok("保存成功"));
			return "redirect:/job";
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("job", job);
			redirectAttributes.addFlashAttribute(Constants.MODEL_ATTRIBUTE_RESULT,  ResultBuilder.error(e.getMessage()));
			return "redirect:/job/create";
		}
	}
	
	@RequiresPermissions("job:edit")
	@RequestMapping(value = "update", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> update(Job job, RedirectAttributes redirectAttributes) {
		try {
			jobService.modify(job);
		} catch (Exception e) {
			return ResultBuilder.error(e.getMessage());
		}
		return ResultBuilder.result(job.getJobName());
	}
}
