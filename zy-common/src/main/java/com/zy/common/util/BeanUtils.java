/**
 * E-Commerce team
 * Copyright (c) 2014-2014 All Rights Reserved.
 */
package com.zy.common.util;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeansException;
import org.springframework.beans.FatalBeanException;
import org.springframework.util.Assert;

/**
 * expand spring beanUtils
 * @author bill
 * @version $Id: BeanUtils.java, v 0.1 2014年6月16日 下午1:40:58 bill Exp $
 */
public class BeanUtils extends org.springframework.beans.BeanUtils {

    /**
    * Copy the property values of the given source bean into the given target bean,
     * ignoring the given "ignoreProperties".
     * <p>Note: The source and target classes do not have to match or even be derived
     * from each other, as long as the properties match. Any bean properties that the
     * source bean exposes but the target bean does not will silently be ignored.
     * <p>This is just a convenience method. For more complex transfer needs,
     * consider using a full BeanWrapper.
     * @param source the source bean
     * @param target the target bean
     * @param ignoreProperties array of property names to ignore
     * @throws BeansException if the copying failed
     * @see BeanWrapper
     */
    public static void copyProperties(Object source, Object target, String ignoreProperties) {
        if (StringUtils.isBlank(ignoreProperties))
            copyProperties(source, target);
        else
            copyProperties(source, target, ignoreProperties.split(","));
    }

    /**
     * Copy the property values of the given source bean into the given target bean.
     * @param source
     * @param target
     * @param editable
     * @param containProperties
     * @throws BeansException
     */
    public static void copyPropertiesIn(Object source, Object target, String containProperties)
                                                                                                     throws BeansException {
        if (StringUtils.isBlank(containProperties)) {
            throw new IllegalArgumentException("Copy Properties is Blank");
        }
        copyPropertiesIn(source, target, null, containProperties.split(","));
    }

    /**
     * Copy the property values of the given source bean into the given target bean.
     * @param source
     * @param target
     * @param editable
     * @param containProperties
     * @throws BeansException
     */
    public static void copyPropertiesIn(Object source, Object target, Class<?> editable,
                                              String containProperties) throws BeansException {
        if (StringUtils.isBlank(containProperties)) {
            throw new IllegalArgumentException("Copy Properties is Blank");
        }
        copyPropertiesIn(source, target, editable, containProperties.split(","));
    }


	public static void copyPropertiesIn(Object source, Object target, String... containProperties)
			throws BeansException {
		copyPropertiesIn(source, target, null, containProperties);
	}
    
    /**
     * Copy the property values of the given source bean into the given target bean.
     * <p>Note: The source and target classes do not have to match or even be derived
     * from each other, as long as the properties match. Any bean properties that the
     * source bean exposes but the target bean only contain containProperties
     * @param source the source bean
     * @param target the target bean
     * @param editable the class (or interface) to restrict property setting to
     * @param containProperties array of property names to contain
     * @throws BeansException if the copying failed
     * @see BeanWrapper
     */
    public static void copyPropertiesIn(Object source, Object target, Class<?> editable,
                                              String[] containProperties) throws BeansException {

        Assert.notNull(source, "Source must not be null");
        Assert.notNull(target, "Target must not be null");

        Class<?> actualEditable = target.getClass();
        if (editable != null) {
            if (!editable.isInstance(target)) {
                throw new IllegalArgumentException("Target class [" + target.getClass().getName()
                                                   + "] not assignable to Editable class ["
                                                   + editable.getName() + "]");
            }
            actualEditable = editable;
        }
        PropertyDescriptor[] targetPds = getPropertyDescriptors(actualEditable);
        List<String> containList = (containProperties != null) ? Arrays.asList(containProperties)
            : null;

        for (PropertyDescriptor targetPd : targetPds) {
            if (targetPd.getWriteMethod() != null && (containList.contains(targetPd.getName()))) {
                PropertyDescriptor sourcePd = getPropertyDescriptor(source.getClass(),
                    targetPd.getName());
                if (sourcePd != null && sourcePd.getReadMethod() != null) {
                    try {
                        Method readMethod = sourcePd.getReadMethod();
                        if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
                            readMethod.setAccessible(true);
                        }
                        Object value = readMethod.invoke(source);
                        Method writeMethod = targetPd.getWriteMethod();
                        if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
                            writeMethod.setAccessible(true);
                        }
                        writeMethod.invoke(target, value);
                    } catch (Throwable ex) {
                        throw new FatalBeanException(
                            "Could not copy properties from source to target", ex);
                    }
                }
            }
        }
    }

	public static Map<String, String> toMap(Object javaBean) {
		Map<String, String> result = new HashMap<String, String>();
		Method[] methods = javaBean.getClass().getDeclaredMethods();

		for (Method method : methods) {
			try {
				if (method.getName().startsWith("get")) {
					String field = method.getName();
					field = field.substring(field.indexOf("get") + 3);
					field = field.toLowerCase().charAt(0) + field.substring(1);

					Object value = method.invoke(javaBean, (Object[]) null);
					result.put(field, null == value ? "" : value.toString());
				}
			} catch (Exception e) {
			}
		}
		return result;
	}

    
}
