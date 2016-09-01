package com.zy.admin.controller.act;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zy.common.model.query.Page;
import com.zy.common.model.query.PageBuilder;
import com.zy.common.model.ui.Grid;
import com.gc.component.ReportComponent;
import com.gc.entity.act.Report;
import com.gc.entity.usr.User;
import com.gc.model.query.ReportQueryModel;
import com.gc.model.query.UserQueryModel;
import com.gc.service.ReportService;
import com.gc.service.UserService;
import com.gc.vo.ReportAdminVo;

@RequestMapping("/report")
@Controller
public class ReportController {

	@Autowired
	private ReportService reportService;

	@Autowired
	private UserService userService;

	@Autowired
	private ReportComponent reportComponent;

	@RequestMapping(method = RequestMethod.GET)
	public String list() {
		return "tsk/reportList";
	}

	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public Grid<ReportAdminVo> list(ReportQueryModel reportQueryModel, String phoneEQ, String nicknameLK) {


		if (StringUtils.isNotBlank(phoneEQ) || StringUtils.isNotBlank(nicknameLK)) {
			UserQueryModel userQueryModel = new UserQueryModel();
			userQueryModel.setPhoneEQ(phoneEQ);
			userQueryModel.setNicknameLK(nicknameLK);
			List<User> users = userService.findAll(userQueryModel);
			if (users.isEmpty()) {
				return new Grid<>(PageBuilder.empty(reportQueryModel.getPageSize(), reportQueryModel.getPageNumber()));
			}
			Long[] userIds = users.stream().map(v -> v.getId()).toArray(Long[]::new);

			reportQueryModel.setUserIdIN(userIds);
		}

		Page<Report> page = reportService.findPage(reportQueryModel);
		return new Grid<ReportAdminVo>(PageBuilder.copyAndConvert(page, reportComponent::buildAdminVo));
	}

}
