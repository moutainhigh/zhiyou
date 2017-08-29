package com.zy.admin.controller.rpt;

import com.zy.common.model.query.Page;
import com.zy.common.model.query.PageBuilder;
import com.zy.common.model.ui.Grid;
import com.zy.common.util.ExcelUtils;
import com.zy.common.util.WebUtils;
import com.zy.component.SalesVolumeComponent;
import com.zy.entity.report.SalesVolume;
import com.zy.entity.sys.SystemCode;
import com.zy.model.query.SalesVolumeQueryModel;
import com.zy.service.SalesVolumeService;
import com.zy.service.SystemCodeService;
import com.zy.util.GcUtils;
import com.zy.vo.SalesVolumeExportVo;
import com.zy.vo.SalesVolumeListVo;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Author: Xuwq
 * Date: 2017/8/24.
 */
@Controller
@RequestMapping("/report/salesVolumeReport")
public class SalesVolumeReportController {

	@Autowired
	private SalesVolumeService salesVolumeService;

	@Autowired
	private SalesVolumeComponent salesVolumeComponent;

	@Autowired
	private SystemCodeService systemCodeService;

	@RequiresPermissions("salesVolumeReport:view")
	@RequestMapping(method = RequestMethod.GET)
	public String list(Model model, String queryDate) throws ParseException {
		Date date = new Date();
		Calendar ca = Calendar.getInstance();// 得到一个Calendar的实例
		ca.setTime(date); // 设置时间为当前时间
		ca.add(Calendar.MONTH, -1);// 月份减1
		Date resultDate = ca.getTime(); // 结果
		if(StringUtils.isNotBlank(queryDate)) {
			resultDate = new SimpleDateFormat("yyyy-MM").parse(queryDate);
		}
		SalesVolumeQueryModel salesVolumeQueryModel = new SalesVolumeQueryModel();
		salesVolumeQueryModel.setCreateTime(resultDate);
		long countNumber = salesVolumeService.countNumber(salesVolumeQueryModel);
		long sumQuantity = salesVolumeService.sumQuantity(salesVolumeQueryModel);
		long sumAmount = salesVolumeService.sumAmount(salesVolumeQueryModel);

		model.addAttribute("queryDate", GcUtils.formatDate(resultDate, "yyyy-MM"));
		model.addAttribute("queryTimeLabels", getQueryTimeLabels());
		model.addAttribute("countNumber", countNumber);
		model.addAttribute("sumQuantity", sumQuantity);
		model.addAttribute("sumAmount", sumAmount);

		List<SystemCode> largeAreaTypes = systemCodeService.findByType("LargeAreaType");
		model.addAttribute("largeAreas",largeAreaTypes);

		return "rpt/salesVolumeReport";
	}

	private List<String> getQueryTimeLabels() {
		LocalDate begin = LocalDate.of(2016, 2, 1);
		LocalDate today = LocalDate.now();
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM");
		List<String> timeLabels = new ArrayList<>();
		for (LocalDate itDate = begin; itDate.isEqual(today) || itDate.isBefore(today); itDate = itDate.plusMonths(1)) {
			timeLabels.add(dateTimeFormatter.format(itDate));
		}
		Collections.reverse(timeLabels);
		return timeLabels;
	}

	@RequiresPermissions("salesVolumeReport:view")
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public Grid<SalesVolumeListVo> list(SalesVolumeQueryModel salesVolumeQueryModel, @RequestParam String queryDate) throws ParseException {
		DateFormat format = new SimpleDateFormat("yyyy-MM");
		Date date = null;
		date = format.parse(queryDate);
		salesVolumeQueryModel.setCreateTime(date);
		Page<SalesVolume> page = salesVolumeService.findPage(salesVolumeQueryModel);
		Page<SalesVolumeListVo> voPage = PageBuilder.copyAndConvert(page, v -> salesVolumeComponent.buildSalesVolumeListVo(v));
		return new Grid<>(voPage);
	}

	@RequiresPermissions("salesVolumeReport:export")
	@RequestMapping("/export")
	public String export(SalesVolumeQueryModel salesVolumeQueryModel, HttpServletResponse response, @RequestParam String queryDate) throws IOException, ParseException{
		salesVolumeQueryModel.setPageSize(null);
		salesVolumeQueryModel.setPageNumber(null);
		DateFormat format = new SimpleDateFormat("yyyy-MM");
		Date date = null;
		date = format.parse(queryDate);
		salesVolumeQueryModel.setCreateTime(date);
		List<SalesVolume> salesVolume =  salesVolumeService.findExReport(salesVolumeQueryModel);
		String fileName = "销量报表.xlsx";
		WebUtils.setFileDownloadHeader(response, fileName);

		List<SalesVolumeExportVo> salesVolumeExportVo = salesVolume.stream().map(salesVolumeComponent::buildSalesVolumeExportVo).collect(Collectors.toList());
		OutputStream os = response.getOutputStream();
		ExcelUtils.exportExcel(salesVolumeExportVo, SalesVolumeExportVo.class, os);

		return null;
	}

}
