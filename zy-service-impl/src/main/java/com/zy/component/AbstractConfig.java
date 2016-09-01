package com.gc.component;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import groovy.lang.Script;

import java.math.BigDecimal;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.control.customizers.ImportCustomizer;

import com.gc.Config;
import com.zy.common.exception.BizException;
import com.gc.entity.fnc.CurrencyType;
import com.gc.entity.sys.Setting;
import com.gc.entity.usr.User.UserType;

public abstract class AbstractConfig implements Config {

	protected abstract Setting getSetting();

	private String getFullName(Class<?> clazz) {
		return StringUtils.replace(clazz.getName(), "$", ".");
	}
	
	@Override
	public Long getSysUserId() {
		return getSetting().getSysUserId();
	}

	@Override
	public Long getFeeUserId() {
		return getSetting().getFeeUserId();
	}

	@Override
	public Long getGrantUserId() {
		return getSetting().getGrantUserId();
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
//		return getSetting().getIsDev();
		// TODO
		return true;
	}




}
