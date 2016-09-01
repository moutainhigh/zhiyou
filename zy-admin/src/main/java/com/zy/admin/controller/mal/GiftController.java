package com.zy.admin.controller.mal;

import com.zy.common.model.query.Page;
import com.zy.common.model.ui.Grid;
import com.zy.entity.mal.Gift;
import com.zy.model.query.GiftQueryModel;
import com.zy.service.GiftService;
import io.gd.generator.api.query.Direction;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static com.zy.common.util.ValidateUtils.NOT_NULL;
import static com.zy.common.util.ValidateUtils.validate;

@RequestMapping("/gift")
@Controller
public class GiftController {

	@Autowired
	private GiftService giftService;
	
	@RequestMapping(method = RequestMethod.GET)
	public String list() {
		return "mal/giftList";
	}
	
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public Grid<Gift> listAjax(GiftQueryModel giftQueryModel) {
		if(giftQueryModel.getOrderBy() == null) {
			giftQueryModel.setOrderBy("orderNumber");
			giftQueryModel.setDirection(Direction.DESC);
		}
		Page<Gift> page = giftService.findPage(giftQueryModel);
		return new Grid<Gift>(page);
	}
	
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public String create() {
		return "mal/giftCreate";
	}
	
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public String create(Gift gift, Model mode, RedirectAttributes redirectAttributes) {
		giftService.create(gift);
		redirectAttributes.addFlashAttribute("保存成功");
		return "redirect:/gift";
	}
	
	@RequestMapping(value = "/update/{id}", method = RequestMethod.GET)
	public String update(@PathVariable Long id, Model model) {
		Gift gift = giftService.findOne(id);
		validate(gift, NOT_NULL, "gift not fund, id = " + id);
		model.addAttribute("gift", gift);
		
		String detail = StringEscapeUtils.unescapeHtml4(gift.getDetail());
		if (StringUtils.isNotBlank(detail)) {
			detail = detail.replace("\r\n", "").replace("'", "\\'");
		}
		model.addAttribute("detail", detail);
		return "mal/giftUpdate";
	}
	
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(Gift gift, String mainImage, RedirectAttributes redirectAttributes, Model model) {
		Long id = gift.getId();
		validate(id, NOT_NULL, "id为空");
		Gift persistence = giftService.findOne(id);
		validate(persistence, NOT_NULL, "persistence not found, id = " + id);
		
		giftService.modify(gift);
		
		return "redirect:/gift";
	}
	
	@RequestMapping("/on")
	@ResponseBody
	public boolean isOn(Long id, Boolean isOn) {
		validate(id, NOT_NULL, "id is null");
		validate(isOn, NOT_NULL, "isOn is null");
		
		Gift gift = giftService.findOne(id);
		validate(gift, NOT_NULL, "gift is null");
		
		giftService.on(id, isOn);
		return true;
	}

	
}
