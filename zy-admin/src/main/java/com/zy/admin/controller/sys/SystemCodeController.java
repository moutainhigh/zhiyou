package com.zy.admin.controller.sys;

import com.zy.common.model.query.Page;
import com.zy.common.model.query.PageBuilder;
import com.zy.common.model.result.ResultBuilder;
import com.zy.common.model.ui.Grid;
import com.zy.component.SystemCodeComponent;
import com.zy.entity.sys.SystemCode;
import com.zy.entity.usr.User;
import com.zy.model.Principal;
import com.zy.model.query.SystemCodeQueryModel;
import com.zy.model.query.UserQueryModel;
import com.zy.service.SystemCodeService;
import com.zy.service.UserService;
import com.zy.vo.SystemCodeAdminVo;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Date;
import java.util.List;

import static com.zy.common.util.ValidateUtils.NOT_NULL;
import static com.zy.common.util.ValidateUtils.validate;

/**
 * Created by liang on 2017/7/5.
 */
@RequestMapping("/systemCode")
@Controller
public class SystemCodeController {

    @Autowired
    private UserService userService;

    @Autowired
    private SystemCodeService systemCodeService;

    @Autowired
    private SystemCodeComponent systemCodeComponent;

    @RequiresPermissions("systemCode:view")
    @RequestMapping(method = RequestMethod.GET)
    public String list(Model model) {
        return "sys/systemCodeList";
    }

    @RequiresPermissions("systemCode:edit")
    @RequestMapping(value = "/createSystemCode", method = RequestMethod.GET)
    public String create(Model model) {
        return "sys/systemCodeCreate";
    }

    /**
     * 新增系统默认值
     * @param systemCode
     * @param principal
     * @param redirectAttributes
     * @return
     */
    @RequiresPermissions("systemCode:edit")
    @RequestMapping(value = "/createSystemCode", method = RequestMethod.POST)
    public String create(SystemCode systemCode, Principal principal , RedirectAttributes redirectAttributes) {
        try{
            SystemCode code = systemCodeService.findByTypeAndName(systemCode.getSystemType(), systemCode.getSystemName());
            if(null == code){
                Long userId = principal.getUserId();
                Date date = new Date();
                systemCode.setCreateBy(userId);
                systemCode.setCreateDate(date);
                systemCodeService.create(systemCode);
                redirectAttributes.addFlashAttribute(ResultBuilder.ok("新增成功"));
            }else{
                redirectAttributes.addFlashAttribute(ResultBuilder.error("新增失败，该系统默认值已经存在" ));
                return "redirect:/systemCode/create";
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute(ResultBuilder.error("新增失败, 原因" + e.getMessage()));
            return "redirect:/systemCode/create";
        }
        return "redirect:/systemCode";
    }

    /**
     * 编辑系统默认值
     * @param id
     * @param model
     * @return
     */
    @RequiresPermissions("systemCode:edit")
    @RequestMapping(value = "/editSystemCode/{id}", method = RequestMethod.GET)
    public String editSystemCode(@PathVariable Long id, Model model) {
        SystemCode systemCode = systemCodeService.findOne(id);
        SystemCodeAdminVo systemCodeAdminVo = systemCodeComponent.buildAdminVo(systemCode);
        model.addAttribute("systemCodeAdminVo",systemCodeAdminVo);
        return "sys/systemCodeEdit";
    }

    @RequiresPermissions("systemCode:edit")
    @RequestMapping(value = "/editSystemCode", method = RequestMethod.POST)
    public String editSystemCode(SystemCode systemCode ,RedirectAttributes redirectAttributes, Principal principal) {
        Long systemCodeId = systemCode.getId();
        validate(systemCode, NOT_NULL, "systemCode id is null");
        try {
            SystemCode code = systemCodeService.findByTypeAndName(systemCode.getSystemType(), systemCode.getSystemName());
            if(code != null){
                Long userId = principal.getUserId();
                Date date = new Date();
                systemCode.setUpdateBy(userId);
                systemCode.setUpdateDate(date);
                systemCodeService.update(systemCode);
                redirectAttributes.addFlashAttribute(ResultBuilder.ok("编辑成功"));
            }else{
                redirectAttributes.addFlashAttribute(ResultBuilder.error("编辑失败, 系统类型码和名字已经存在"));
                return "redirect:/systemCode/edit/" + systemCodeId;
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute(ResultBuilder.error(e.getMessage()));
            return "redirect:/systemCode/edit/" + systemCodeId;
        }
        return "redirect:/systemCode";
    }

    /**
     * 系统默认值列表查询
     * @param systemCodeQueryModel
     * @param createNicknameLK
     * @param createPhoneEQ
     * @param updateNicknameLK
     * @param updatePhoneEQ
     * @return
     */
    @RequiresPermissions("systemCode:view")
    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public Grid<SystemCodeAdminVo> list(SystemCodeQueryModel systemCodeQueryModel
            , String createNicknameLK, String createPhoneEQ , String updateNicknameLK, String updatePhoneEQ) {
        if (StringUtils.isNotBlank(createNicknameLK) || StringUtils.isNotBlank(createPhoneEQ)) {
            List<User> all = userService.findAll(UserQueryModel.builder().nicknameLK(createNicknameLK).phoneEQ(createPhoneEQ).build());
            if (all.isEmpty()) {
                return new Grid<SystemCodeAdminVo>(PageBuilder.empty(systemCodeQueryModel.getPageSize(), systemCodeQueryModel.getPageNumber()));
            }
            systemCodeQueryModel.setCreateByIN(all.stream().map(v -> v.getId()).toArray(Long[]::new));
        }
        if (StringUtils.isNotBlank(updateNicknameLK) || StringUtils.isNotBlank(updatePhoneEQ)) {
            List<User> all = userService.findAll(UserQueryModel.builder().nicknameLK(updateNicknameLK).phoneEQ(updatePhoneEQ).build());
            if (all.isEmpty()) {
                return new Grid<SystemCodeAdminVo>(PageBuilder.empty(systemCodeQueryModel.getPageSize(), systemCodeQueryModel.getPageNumber()));
            }
            systemCodeQueryModel.setUpdateByIN(all.stream().map(v -> v.getId()).toArray(Long[]::new));
        }
        Page<SystemCode> page = systemCodeService.findPage(systemCodeQueryModel);
        Page<SystemCodeAdminVo> voPage = PageBuilder.copyAndConvert(page, v -> systemCodeComponent.buildAdminVo(v));
        return new Grid<>(voPage);
    }

    /**
     * 删除系统默认值
     * @param id
     * @param redirectAttributes
     * @return
     */
    @RequiresPermissions("systemCode:edit")
    @RequestMapping(value = "/deleteSystemCode/{id}", method = RequestMethod.GET)
    public String deleteBlackWhite(@PathVariable Long id ,RedirectAttributes redirectAttributes) {
        try {
            systemCodeService.delete(id);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute(ResultBuilder.error(e.getMessage()));
        }
        return "redirect:/systemCode";
    }

}
