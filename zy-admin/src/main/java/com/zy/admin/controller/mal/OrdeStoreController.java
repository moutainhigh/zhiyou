package com.zy.admin.controller.mal;

import com.zy.common.model.query.Page;
import com.zy.common.model.query.PageBuilder;
import com.zy.common.model.ui.Grid;
import com.zy.component.OrderStoreComponent;
import com.zy.entity.mal.OrderStore;
import com.zy.model.query.OrderStoreQueryModel;
import com.zy.service.StoreService;
import com.zy.vo.OrderStoreAdminVo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Author: Xuwq
 * Date: 2017/8/3.
 */

@RequestMapping("/orderStore")
@Controller
public class OrdeStoreController {

    @Autowired
    private OrderStoreComponent orderStoreComponent;

    @Autowired
    private StoreService storeService;

    @RequiresPermissions("orderStore:view")
    @RequestMapping(method = RequestMethod.GET)
    public String list() {
        return "mal/orderStoreList";
    }

    @RequiresPermissions("orderStore:view")
    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public Grid<OrderStoreAdminVo> list(OrderStoreQueryModel orderStoreQueryModel) {
        orderStoreQueryModel.setIsEndEQ(1);
        Page<OrderStore> page = storeService.findPage(orderStoreQueryModel);
        List<OrderStoreAdminVo> list = page.getData().stream().map(v -> {
            return orderStoreComponent.buildOrderStoreAdminVo(v);
        }).collect(Collectors.toList());
        return new Grid<OrderStoreAdminVo>(PageBuilder.copyAndConvert(page, list));
    }

}
