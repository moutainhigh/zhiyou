package com.zy.component;

import com.zy.Config;
import com.zy.common.exception.BizException;
import com.zy.entity.fnc.CurrencyType;
import com.zy.entity.sys.Setting;
import com.zy.entity.usr.User.UserType;
import com.zy.model.BizCode;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import groovy.lang.Script;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.control.customizers.ImportCustomizer;

import java.math.BigDecimal;

public abstract class AbstractConfig implements Config {

	protected abstract Setting getSetting();

	private String getFullName(Class<?> clazz) {
		return StringUtils.replace(clazz.getName(), "$", ".");
	}
	
	@Override
	public Long getSysUserId() {
		return getSetting().getSysUserId();
	}

	final GroovyShell withdrawFeeRateShell;

	{
		ImportCustomizer importCustomizer = new ImportCustomizer();
		importCustomizer.addImports(getFullName(UserType.class), getFullName(CurrencyType.class), getFullName(BizException.class));
		CompilerConfiguration compilerConfiguration = new CompilerConfiguration();
		compilerConfiguration.addCompilationCustomizers(importCustomizer);
		withdrawFeeRateShell = new GroovyShell(compilerConfiguration);
	}

	@Override
	public BigDecimal getWithdrawFeeRate(UserType userType, CurrencyType currencyType) {
		String script = getSetting().getWithdrawFeeRateScript();
		Binding binding = new Binding();
		Script parse = withdrawFeeRateShell.parse(script);
		binding.setVariable("userType", userType);
		binding.setVariable("currencyType", currencyType);
		parse.setBinding(binding);
		BigDecimal result = (BigDecimal) parse.run();
		return result;
	}

	@Override
	public boolean isDev() {
		return getSetting().getIsDev();
	}

	@Override
	public boolean isOld(Long productId) {
		if (productId != null && productId.equals(1L)) {
			return true;
		} else if (productId != null && productId.equals(2L)) {
			return false;
		} else {
			throw new BizException(BizCode.ERROR, "productId错误");
		}
	}

	@Override
	public Long getOld() {
		return 1L;
	}

	@Override
	public Long getNew() {
		return 2L;
	}


}
