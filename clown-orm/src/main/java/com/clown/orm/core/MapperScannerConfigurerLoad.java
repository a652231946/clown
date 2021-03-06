package com.clown.orm.core;

import com.clown.framework.configurations.ClownContextPropertiesConstant;
import com.clown.framework.configurations.PropertiesConfiguration;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.GenericBeanDefinition;

/**
 * Created by len.li on 25/3/2016.
 */
public class MapperScannerConfigurerLoad implements BeanDefinitionRegistryPostProcessor {

    private static final String defaultMybaitsPackage = "com.clown.*.dao";

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        StringBuilder stringBuilder = new StringBuilder(defaultMybaitsPackage);
        String basepackage = PropertiesConfiguration.findPropertieValue(ClownContextPropertiesConstant.CLOWN_MYBATIS_BASEPACKAGE,null);
        if(basepackage!=null && basepackage.trim().length()>0){
            stringBuilder.append(",");
            stringBuilder.append(basepackage);
        }
        MutablePropertyValues mutablePropertyValues = new MutablePropertyValues();

        mutablePropertyValues.add("basePackage",stringBuilder.toString());
        mutablePropertyValues.add("sqlSessionFactoryBeanName","sqlSessionFactory");
        GenericBeanDefinition genericBeanDefinition = new GenericBeanDefinition();
        genericBeanDefinition.setBeanClass(MapperScannerConfigurer.class);
        genericBeanDefinition.setAbstract(false);
        genericBeanDefinition.setPropertyValues(mutablePropertyValues);
        registry.registerBeanDefinition("mapperScannerConfigurer",genericBeanDefinition);

    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

    }
}
