package com.zy.mobile.controller;

import com.zy.common.model.result.Result;
import com.zy.common.model.result.ResultBuilder;
import com.zy.entity.usr.User;
import com.zy.service.UserService;
import com.zy.util.GcUtils;
import me.chanjar.weixin.common.util.StringUtils;
import org.im4java.core.ConvertCmd;
import org.im4java.core.IM4JavaException;
import org.im4java.core.IMOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static com.zy.common.util.ValidateUtils.NOT_NULL;
import static com.zy.common.util.ValidateUtils.validate;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RequestMapping("/code")
@Controller
public class CodeController {

    @Autowired
    private UserService userService;

    @Value("${imageMagick.path}")
    private String imageMagickPath;

    @Value("${imageMagick.tmp}")
    private String tmp;


    @RequestMapping
    public String index(Model model) {
        return "code";
    }

    @RequestMapping(value = "check",method = POST)
    @ResponseBody
    public Result<?> check(@RequestParam String code) {
        User user = userService.findByCode(code);
        if (user != null) {
            return ResultBuilder.result(user.getId());
        } else {
            return ResultBuilder.error("授权码不存在");
        }
    }

    String fontPath;
    String bgPath;

    {
        try {
            fontPath = new ClassPathResource("msyh.ttf").getFile().getAbsolutePath();
            bgPath = new ClassPathResource("authorization.jpg").getFile().getAbsolutePath();
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @RequestMapping(value = "/image", produces = "image/jpeg")
    @ResponseBody
    public BufferedImage image(@RequestParam Long userId, Model model) throws InterruptedException, IOException, IM4JavaException {
        User user = userService.findOne(userId);
        validate(user, NOT_NULL, "user id " + userId + " is not found");

        String result = tmp + "/authorization" + userId + ".jpg";


        if (StringUtils.isBlank(user.getCode())) {
            userService.generateCode(userId);
            user = userService.findOne(userId);
        }
        String nickname = user.getNickname();
        IMOperation authorOp = new IMOperation();
        ConvertCmd cmd = new ConvertCmd(false);
        if (StringUtils.isNotBlank(imageMagickPath)) {
            cmd.setSearchPath(imageMagickPath);
        }
        int x = 0;
        if(nickname.length() == 1){
            x = 285;
        }else if (nickname.length() == 2){
            x = 270;
        }else if(nickname.length() == 3){
            x = 260;
        }else if (nickname.length() == 4){
            x = 245;
        }else if (nickname.length() == 5) {
            x = 235;
        }
        authorOp.font(fontPath).pointsize(28).fill("#000000").draw("text "+x+",335 '" + nickname + "'");
        authorOp.font(fontPath).pointsize(20).fill("#000000").draw("text 218,385 '" + user.getCode() + "'");
        authorOp.font(fontPath).pointsize(45).fill("#2170A8").draw("text 183,490 '" + GcUtils.getUserRankLabel(user.getUserRank()) + "'");
        authorOp.addImage();
        authorOp.addImage();
        cmd.run(authorOp, bgPath, result);
        return ImageIO.read(new File(result));
    }

}
