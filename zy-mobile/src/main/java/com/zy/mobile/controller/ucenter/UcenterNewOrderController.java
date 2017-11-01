package com.zy.mobile.controller.ucenter;

import com.zy.Config;
import com.zy.component.ProductComponent;
import com.zy.component.UserComponent;
import com.zy.entity.mal.OrderFillUser;
import com.zy.entity.mal.Product;
import com.zy.entity.sys.ConfirmStatus;
import com.zy.entity.usr.Address;
import com.zy.entity.usr.User;
import com.zy.entity.usr.UserInfo;
import com.zy.model.Principal;
import com.zy.model.query.OrderFillUserQueryModel;
import com.zy.service.*;
import com.zy.util.GcUtils;
import com.zy.vo.ProductListVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.List;

import static com.zy.common.util.ValidateUtils.NOT_NULL;
import static com.zy.common.util.ValidateUtils.validate;

/**
 * Author: Xuwq
 * Date: 2017/10/31.
 */
@RequestMapping("/u/newOrder")
@Controller
public class UcenterNewOrderController {

    Logger logger = LoggerFactory.getLogger(UcenterOrderCreateController.class);

    @Autowired
    private AddressService addressService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderFillUserService orderFillUserService;

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @Autowired
    private Config config;

    @Autowired
    private ProductComponent productComponent;

    @Autowired
    private UserComponent userComponent;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String detail(@PathVariable Long id, Model model) {
        Product product = productService.findOne(id);
        Principal principal = GcUtils.getPrincipal();
        if(principal != null) {
            User user = userService.findOne(principal.getUserId());
            User.UserRank userRank = user.getUserRank();
            model.addAttribute("userRank", userRank);
        }
        validate(product, NOT_NULL, "product id" + id + " not found");
        validate(product.getIsOn(), v -> true, "product is not on");

        model.addAttribute("product", productComponent.buildDetailVo(product));
        return "product/productDetailNew";
    }

    /**
     * 下单
     * @return
     */
    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String create(@RequestParam Long productId,@RequestParam  Long quantity,@RequestParam BigDecimal price, Model model, Principal principal) {
        Long userId = principal.getUserId();
        User user = userService.findOne(userId);
        User.UserRank userRank = user.getUserRank();

        Address address = addressService.findDefaultByUserId(userId);
        Product product = productService.findOne(productId);
        product.setPrice(price);
        validate(product, NOT_NULL, "product id:" + productId + " is not found !");
        ProductListVo productVo = productComponent.buildListVo(product);
        model.addAttribute("product", productVo);
        model.addAttribute("quantity", quantity);
        model.addAttribute("address", address);
        model.addAttribute("userRank", userRank);
        model.addAttribute("useOfflinePay", quantity <= 160L );


        if (userRank == User.UserRank.V0) {
            Long parentId = user.getParentId();
            Long inviterId = user.getInviterId();
            if (parentId != null) {
                User parent = userService.findOne(parentId);
                if (parent != null) {
                    model.addAttribute("parent", userComponent.buildListVo(parent));
                }
            }
            if (inviterId != null) {
                User inviter = userService.findOne(inviterId);
                if (inviter != null) {
                    model.addAttribute("inviter", userComponent.buildListVo(inviter));
                }
            }
        }

        boolean orderFill = config.isOpenOrderFill();
        if(orderFill) {
            List<OrderFillUser> all = orderFillUserService.findAll(OrderFillUserQueryModel.builder().userIdEQ(userId).build());
            if(all.isEmpty()) {
                orderFill = false;
            } else {
                orderFill = true;
            }
        }
        UserInfo userInfo = userInfoService.findByUserId(userId);
        if (userInfo!=null&&userInfo.getConfirmStatus() == ConfirmStatus.已通过){
            model.addAttribute("realFlage", true);
        }else{
            model.addAttribute("realFlage", false);
        }
        model.addAttribute("orderFill", orderFill);
        return "ucenter/order/orderCreate";
    }
}
