package org.smart4j.framework.helper;

import java.lang.reflect.Field;
import java.util.Map;

import org.smart4j.framework.annotation.Inject;
import org.smart4j.framework.util.ArrayUtil;
import org.smart4j.framework.util.CollectionUtil;
import org.smart4j.framework.util.ReflectionUtil;


/**
 * 依赖注入助手类
 * @author Administrator
 * @since 1.0.0
 */
public final class IocHelper {
	static {
		//获取所有Bean类与Bean实例之间的映射关系（简称Bean Map）
		Map<Class<?>, Object> beanMap = BeanHelper.getBeanMap();
		if (CollectionUtil.isNotEmpty(beanMap)) {
			//遍历Bean Map
			for(Map.Entry<Class<?>, Object> beanEntry : beanMap.entrySet()){
				//从BeanMap中获取Bean类与Bean实例
				Class<?> beanClass = beanEntry.getKey();
				Object beanInstance = beanEntry.getValue();
				//获取bean类定义的所有成员变量（简称为bean field）
				Field[] beanFields = beanClass.getDeclaredFields();
				if (ArrayUtil.isNotEmpty(beanFields)) {
					//遍历Bean Field
					for (Field beanField : beanFields) {
						//判断当前bean field 是否带有 inject 注解
						if (beanField.isAnnotationPresent(Inject.class)) {
							//在Bean Map中获取Bean Field 对应的实例
							Class<?> beanFieldClass = beanField.getType();
							Object beanFieldInstance = beanMap.get(beanFieldClass);
							if (beanFieldInstance != null) {
								//通过反射初始化BeanField的值
								ReflectionUtil.setField(beanInstance, beanField, beanFieldInstance);
							}
						}
					}
				}
			}
		}
	}
}
