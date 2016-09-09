package com.zy.component;

import com.zy.Config;
import com.zy.common.exception.BizException;
import com.zy.entity.mal.Product;
import com.zy.entity.usr.User;
import com.zy.mapper.ProductMapper;
import com.zy.mapper.UserMapper;
import com.zy.model.BizCode;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import groovy.lang.Script;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.control.customizers.ImportCustomizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

import static com.zy.common.util.ValidateUtils.NOT_NULL;
import static com.zy.common.util.ValidateUtils.validate;

@Component
@Validated
public class MalComponent {


	@Autowired
	private Config config;

	@Autowired
	private UserMapper userMaper;

	@Autowired
	private ProductMapper productMapper;

	public BigDecimal getPrice(@NotNull Long productId, @NotNull User.UserRank userRank, long quantity) {

		Product product = productMapper.findOne(productId);
		validate(product, NOT_NULL, "product id " + productId + " is not found");
		Product.ProductPriceType productPriceType = product.getProductPriceType();
		if (productPriceType == Product.ProductPriceType.一般价格) {
			return product.getPrice();
		} else if (productPriceType == Product.ProductPriceType.脚本价格) {
			GroovyShell withdrawFeeRateShell;
			ImportCustomizer importCustomizer = new ImportCustomizer();
			CompilerConfiguration compilerConfiguration = new CompilerConfiguration();
			compilerConfiguration.addCompilationCustomizers(importCustomizer);
			withdrawFeeRateShell = new GroovyShell(compilerConfiguration);
			String script = product.getPriceScript();
			Binding binding = new Binding();
			Script parse = withdrawFeeRateShell.parse(script);
			binding.setVariable("userRank", userRank);
			binding.setVariable("quantity", quantity);
			parse.setBinding(binding);
			BigDecimal result = (BigDecimal) parse.run();
			return result;
		} else {
			throw new BizException(BizCode.ERROR, "暂不支持矩阵价格");
		}
	}

	public Long calculateSellerId(User.UserRank userRank, long quantity, Long parentId) {

		return parentId;
	}

}
