package com.zy.admin.controller.sys;

import static com.zy.common.util.ValidateUtils.NOT_NULL;
import static com.zy.common.util.ValidateUtils.validate;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.zy.common.exception.ValidationException;
import com.zy.common.model.result.ResultBuilder;
import com.gc.entity.sys.Area;
import com.gc.entity.sys.Area.AreaType;
import com.gc.model.query.AreaQueryModel;
import com.gc.service.AreaService;

@RequestMapping("/area")
@Controller
public class AreaController {

	Logger logger = LoggerFactory.getLogger(AreaController.class);
	
	@Autowired
	private AreaService areaService;
	
	@RequestMapping()
	public String list(Model model, AreaQueryModel areaQueryModel) {
		AreaType areaType = areaQueryModel.getAreaTypeEQ();
		if(areaType == null && areaQueryModel.getParentIdEQ() == null) {
			areaQueryModel.setAreaTypeEQ(AreaType.省);
		}
		List<Area> areas = areaService.findAll(areaQueryModel);
		model.addAttribute("areas", areas);
		return "sys/areaList";
	}
	
	@RequestMapping( value = "/create", method = RequestMethod.GET)
	public String create(@RequestParam Long parentId, Model model) {
		Area area = areaService.findOne(parentId);
		model.addAttribute("area", area);
		return "sys/areaCreate";
	}
	
	@RequestMapping( value = "/create", method = RequestMethod.POST)
	public String create(Area area, RedirectAttributes redirectAttributes) {
		
		Area parentArea = areaService.findOne(area.getParentId());
		validate(parentArea, NOT_NULL, "parent area is null");

		if(StringUtils.isBlank(area.getName())) {
			throw new ValidationException("name is null");
		}
		
		AreaType parentAreaType = parentArea.getAreaType();
		if(parentAreaType == AreaType.省) {
			area.setAreaType(AreaType.市);
		}else if(parentAreaType == AreaType.市) {
			area.setAreaType(AreaType.区);
		}
		
		if(StringUtils.isBlank(area.getName())) {
			throw new ValidationException("name is null");
		}
		if(area.getOrderNumber() == null) {
			throw new ValidationException("orderNumber is null");
		}
		
		areaService.create(area);
		redirectAttributes.addFlashAttribute(ResultBuilder.error("区域[" + area.getName() + "]添加成功"));
		return "redirect:/area";
	}
	
	@RequestMapping( value = "/update/{id}", method = RequestMethod.GET)
	public String update(@PathVariable Long id, Model model) {
		Area area = areaService.findOne(id);
		model.addAttribute("area", area);
		return "sys/areaUpdate";
	}
	
	@RequestMapping( value = "/update", method = RequestMethod.POST)
	public String update(Area area, RedirectAttributes redirectAttributes) {
		
		validate(area.getId(), NOT_NULL, "id is null");
		
		if(StringUtils.isBlank(area.getName())) {
			throw new ValidationException("name is null");
		}
		if(area.getOrderNumber() == null) {
			throw new ValidationException("orderNumber is null");
		}
		
		areaService.merge(area, "name","orderNumber");
		redirectAttributes.addFlashAttribute(ResultBuilder.error("区域[" + area.getName() + "]添加成功"));
		return "redirect:/area";
	}
}
