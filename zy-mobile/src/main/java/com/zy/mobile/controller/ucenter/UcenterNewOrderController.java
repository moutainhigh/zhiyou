package com.zy.mobile.controller.ucenter;

import com.zy.Config;
import com.zy.common.model.result.Result;
import com.zy.common.model.result.ResultBuilder;
import com.zy.component.ProductComponent;
import com.zy.entity.mal.Order;
import com.zy.entity.mal.Product;
import com.zy.entity.usr.User;
import com.zy.model.Principal;
import com.zy.service.OrderService;
import com.zy.service.ProductService;
import com.zy.service.UserService;
import com.zy.util.GcUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

import static com.zy.common.util.ValidateUtils.NOT_NULL;
import static com.zy.common.util.ValidateUtils.validate;
import static com.zy.model.Constants.SETTING_NEW_MIN_QUANTITY;
import static com.zy.model.Constants.SETTING_OLD_MIN_QUANTITY;

/**
 * Author: Xuwq
 * Date: 2017/10/31.
 */
@RequestMapping("/u/newOrder")
@Controller
public class UcenterNewOrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @Autowired
    private ProductComponent productComponent;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String detail(@PathVariable Long id, Model model) {
        Product product = productService.findOne(id);
        Principal principal = GcUtils.getPrincipal();
        if(principal != null) {
            User user = userService.findOne(principal.getUserId());
            User.UserRank userRank = user.getUserRank();
            model.addAttribute("userRank", userRank);
            product.setPrice(productService.getPrice(product.getId(), user.getUserRank(), 1L));

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
    @RequestMapping(value = "/order", method = RequestMethod.POST)
    @ResponseBody
    public Result<?> order(Principal principal, @RequestParam Long productId, @RequestParam Long quantity, @RequestParam BigDecimal amount, Model model) {
        Order order = new Order();

        order.setUserId(principal.getUserId());
        order.setProductId(productId);
        order.setQuantity(quantity);
        order.setAmount(amount);


        return ResultBuilder.ok("下单成功");
    }
}
