package com.zy.mobile.controller.ucenter;

import com.zy.common.model.query.Page;
import com.zy.common.model.query.PageBuilder;
import com.zy.common.model.result.Result;
import com.zy.common.model.result.ResultBuilder;
import com.zy.common.util.DateUtil;
import com.zy.component.OrderComponent;
import com.zy.component.OrderStoreComponent;
import com.zy.component.TourComponent;
import com.zy.component.TourUserComponent;
import com.zy.entity.act.PolicyCode;
import com.zy.entity.act.Report;
import com.zy.entity.cms.Article;
import com.zy.entity.mal.Order;
import com.zy.entity.mal.OrderStore;
import com.zy.entity.tour.Tour;
import com.zy.entity.tour.TourTime;
import com.zy.entity.tour.TourUser;
import com.zy.entity.usr.User;
import com.zy.model.Constants;
import com.zy.model.Principal;
import com.zy.model.query.OrderQueryModel;
import com.zy.model.query.OrderStoreQueryModel;
import com.zy.model.query.TourQueryModel;
import com.zy.model.query.TourUserQueryModel;
import com.zy.service.*;
import com.zy.util.GcUtils;
import com.zy.vo.TourTimeVo;
import com.zy.vo.TourUserInfoVo;
import com.zy.vo.TourUserListVo;
import io.gd.generator.api.query.Direction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;
import java.util.stream.Collectors;

import static com.zy.util.GcUtils.getThumbnail;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * Created by Administrator on 2017/7/5.
 */
@RequestMapping("/u/store")
@Controller
public class UcenterStoreController {

    @Autowired
    private StoreService storeService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderComponent orderComponent;

    @Autowired
    private OrderStoreComponent orderStoreComponent;
    /**
     * 跳转到  我的库存页面
     * @param principal
     * @param model
     * @return
     */
    @RequestMapping
    public String toStore(Principal principal, Model model){
        OrderStoreQueryModel storeQueryModel = new OrderStoreQueryModel();
        storeQueryModel.setUserIdEQ(principal.getUserId());
        storeQueryModel.setIsEndEQ(1);
        Page<OrderStore> page = storeService.findPage(storeQueryModel);
        if (page.getTotal()!=null&&page.getTotal()>0){//取到第一条的数量
            model.addAttribute("num",page.getData().get(0).getAfterNumber());
        }else{
            model.addAttribute("num",0);
        }
       return "ucenter/store/storeNum";
    }


    /**
     * 获取 订单信息
     * @param principal
     * @param model
     * @param orderStatus
     * @return
     */
    @RequestMapping(value = "toOutOrder",method = GET)
    public String toOutOrder(Principal principal, Model model,Order.OrderStatus orderStatus){
        OrderQueryModel orderQueryModel = OrderQueryModel.builder().sellerIdEQ(principal.getUserId())
                .orderBy("createdTime").direction(Direction.DESC).isDeletedEQ(false)
                .pageSize(50).build();
        orderQueryModel.setOrderStatusEQ(orderStatus);
        Page<Order> page = orderService.findPage(orderQueryModel);
        model.addAttribute("page", PageBuilder.copyAndConvert(page, orderComponent::buildListVo));
        model.addAttribute("inOut", "out");
        model.addAttribute("orderStatus", orderStatus);
        return "ucenter/store/storeList";
    }

    /**
     * 获取 库存信息
     * @param principal
     * @param model
     * @return
     */
    @RequestMapping(value = "details",method = GET)
   public String details(Principal principal, Model model){
       OrderStoreQueryModel storeQueryModel = new OrderStoreQueryModel();
       storeQueryModel.setUserIdEQ(principal.getUserId());
       storeQueryModel.setPageNumber(0);
       storeQueryModel.setPageSize(10);
       storeQueryModel.setOrderBy("createDate");
       Page<OrderStore> page = storeService.findPage(storeQueryModel);
       model.addAttribute("page", PageBuilder.copyAndConvert(page, orderStoreComponent::buildListVo));
       return "ucenter/store/storeDetails";
   }

    @RequestMapping(value = "ajaxDetails",method = POST)
    @ResponseBody
    public Result<?>  details(Principal principal, Model model,@RequestParam(required = true)Integer pageNumber){
        OrderStoreQueryModel storeQueryModel = new OrderStoreQueryModel();
        storeQueryModel.setUserIdEQ(principal.getUserId());
        storeQueryModel.setPageNumber(pageNumber);
        storeQueryModel.setPageSize(10);
        storeQueryModel.setOrderBy("createDate");
        Page<OrderStore> page = storeService.findPage(storeQueryModel);
        Map<String, Object> map = new HashMap<>();
        map.put("page", PageBuilder.copyAndConvert(page, orderStoreComponent::buildListVo));
        return ResultBuilder.result(map);
    }


}
