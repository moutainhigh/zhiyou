package com.zy.mobile.controller.ucenter;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.zy.common.exception.UnauthorizedException;
import com.zy.common.model.result.Result;
import com.zy.common.model.result.ResultBuilder;
import com.zy.component.CacheComponent;
import com.zy.entity.usr.Address;
import com.zy.model.Principal;
import com.zy.service.AddressService;

@RequestMapping("/u/address")
@Controller
public class UcenterAddressController {

	Logger logger = LoggerFactory.getLogger(UcenterAddressController.class);
	
	@Autowired
	private AddressService addressService;
	
	@Autowired
	private CacheComponent cacheComponent;
	
	@RequestMapping
	public String list(Principal principal, Model model, Integer pageNumber) {
		List<Address> list = addressService.findByUserId(principal.getUserId());
		model.addAttribute("list", list);
		return "ucenter/user/addressList";
	}
	
	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public String create(Principal principal) {
		
		return "ucenter/user/addressCreate";
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public String create(Address address, Model model, Principal principal, RedirectAttributes redirectAttributes) {
		
		try {
			address.setUserId(principal.getUserId());
			addressService.create(address);
			redirectAttributes.addFlashAttribute(ResultBuilder.ok("收货地址保存成功"));
			
			cacheComponent.deleteAddress(principal.getUserId());
			
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute(ResultBuilder.error(e.getMessage()));
			return "redirect:/u/address/create";
		}
		
		return "redirect:/u/address";
	}
	
	@RequestMapping("/{id}")
	public String detail(Principal principal, @PathVariable Long id, Model model) {
		Address address = addressService.findOne(id);
		if(address != null) {
			if(!address.getUserId().equals(principal.getUserId())) {
				throw new UnauthorizedException();
			}
		}
		model.addAttribute("address", address);
		return "ucenter/user/addressEdit";
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public String edit(Address address, Model model, Principal principal, RedirectAttributes redirectAttributes) {
		
		Long addressId = address.getId();
		Address persistence = addressService.findOne(addressId);
		if (!persistence.getUserId().equals(principal.getUserId())) {
			throw new UnauthorizedException("权限不足只能编辑自己的地址");
		}
		
		try {
			addressService.update(address);
			cacheComponent.deleteAddress(principal.getUserId());
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute(ResultBuilder.error(e.getMessage()));
			return "redirect:/u/address/edit";
		}
		redirectAttributes.addFlashAttribute(ResultBuilder.ok("收货地址修改成功"));
		return "redirect:/u/address";
	}
	
	@RequestMapping(value = "/delete/{id}")
	public String delete(@PathVariable Long id, Principal principal, Model model, RedirectAttributes redirectAttributes) {
		Address address = addressService.findOne(id);
		if(address != null) {
			if(!address.getUserId().equals(principal.getUserId())) {
				throw new UnauthorizedException();
			}
		}
		addressService.delete(id);
		cacheComponent.deleteAddress(principal.getUserId());
		redirectAttributes.addFlashAttribute(ResultBuilder.ok("收货地址删除成功"));
		return "redirect:/u/address";
	}
	
	@RequestMapping(value = "/createAjax")
	@ResponseBody
	public Result<Address> createAjax(Address address, Principal principal) {
		address.setUserId(principal.getUserId());
		address.setIsDefault(false);
		address = addressService.create(address);
		cacheComponent.deleteAddress(principal.getUserId());
		return ResultBuilder.result(address);
	}
	
	@RequestMapping(value = "/listAjax")
	@ResponseBody
	public Result<List<Address>> listAjax(Principal principal) {
		List<Address> addressList = addressService.findByUserId(principal.getUserId());
		return ResultBuilder.result(addressList);
	}
}
