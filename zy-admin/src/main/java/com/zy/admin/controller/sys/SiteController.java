package com.zy.admin.controller.sys;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/site")
@Controller
public class SiteController {

	/*@Autowired
	private SiteService siteService;
	
	@RequiresPermissions("site:view")
	@RequestMapping(method = RequestMethod.GET)
	public String list() {
		return "sys/siteList";
	}
	
	@RequiresPermissions("site:view")
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public Grid<Site> listAjax(Model model) {
		List<Site> list = siteService.findAll();
		Page<Site> page = new Page<>();
		page.setData(list);
		page.setPageNumber(0);
		page.setPageSize(60);
		page.setTotal(Long.valueOf(list.size()));
		return new Grid<Site>(page);
	}
	
	@RequiresPermissions("site:edit")
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public String create() {
		return "sys/siteCreate";
	}
	
	@RequiresPermissions("site:edit")
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public String create(Site site, RedirectAttributes redirectAttributes, String siteId, Model model) {
	
		validate(site, "code");
		
		校验title唯一性
		Site persistentSite = siteService.findByCode(site.getCode());
		if(persistentSite != null){
			redirectAttributes.addFlashAttribute(ResultBuilder.error("站点code已存在"));
			return "redirect:/site/create";
		}
		site.setCreatedTime(new Date());
		validate(site);
		siteService.create(site);
		
		redirectAttributes.addFlashAttribute(ResultBuilder.ok("站点【"+ site.getName() +"】创建成功"));
		return "redirect:/site";
	}
	
	@RequiresPermissions("site:edit")
	@RequestMapping(value = "/update/{id}", method = RequestMethod.GET)
	public String update(@PathVariable Long id, Model model) {
		校验id
		Site site = siteService.findOne(id);
		validate(site, NOT_NULL, "site not found, id = " + id);
		model.addAttribute("site", site);
		return "sys/siteUpdate";
	}
	
	@RequiresPermissions("site:edit")
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(Site site, Model model, RedirectAttributes redirectAttributes, String siteId) {
	
		validate(site, "code");
		Site persistentSite = siteService.findOne(site.getId());
		validate(persistentSite, NOT_NULL, "site not found, id = " + site.getId());
		siteService.merge(site, "name", "domainUrl", "qqLoginAppKey", "qqLoginAppSecret", "weixinLoginAppKey", "weixinLoginAppSecret", 
				"weixinPayMchId", "weixinPaySecret", "weixinMpAppKey", "weixinMpAppSecret", "alipayBargainorId", "alipayBargainorKey", "isLocalPay");
		
		redirectAttributes.addFlashAttribute(ResultBuilder.ok("站点【" + site.getName() + "】配置修改成功"));
		return "redirect:/site";
	}*/
	
}
