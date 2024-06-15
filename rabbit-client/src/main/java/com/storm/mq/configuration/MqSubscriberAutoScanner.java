package com.storm.mq.configuration;

import com.storm.mq.anno.MqSubscriber;
import com.storm.mq.utils.ClassUtil;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 *
 */
public class MqSubscriberAutoScanner implements ImportBeanDefinitionRegistrar, EnvironmentAware {

    private Environment environment;

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry, BeanNameGenerator importBeanNameGenerator) {
        Set<AnnotationAttributes> componentScans = attributesForRepeatable(importingClassMetadata, ComponentScans.class.getName(), ComponentScan.class.getName());
        Set<String> basePackage = new LinkedHashSet<>();
        Iterator<AnnotationAttributes> var6 = componentScans.iterator();
        while (var6.hasNext()){
            AnnotationAttributes componentScan = var6.next();
            String[] basePackages = componentScan.getStringArray("basePackages");
            int var10 = basePackages.length;

            int var11;
            for (var11 = 0; var11 < var10; var11++) {
                String pkg = basePackages[var11];
                String[] tokenized = StringUtils.tokenizeToStringArray(this.environment.resolvePlaceholders(pkg),",; \t\n");
                Collections.addAll(basePackage,tokenized);
            }
            Class<?>[] basePackageClasses = componentScan.getClassArray("basePackageClasses");
            var10 = basePackageClasses.length;
            for (var11 = 0; var11 < var10; var11++) {
                Class<?> clazz = basePackageClasses[var11];
                basePackage.add(ClassUtils.getPackageName(clazz));
            }

            if(basePackage.isEmpty()){
                basePackage.add(ClassUtils.getPackageName(importingClassMetadata.getClassName()));
            }
        }

        ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner(registry,false,this.environment);
        scanner.addIncludeFilter(new AnnotationTypeFilter(MqSubscriber.class));
        scanner.scan(basePackage.toArray(new String[0]));
    }

    private Set<AnnotationAttributes> attributesForRepeatable(AnnotationMetadata metadata, String containerClassName, String annotationClassName) {
        Set<AnnotationAttributes> result = new LinkedHashSet<>();
        attributesIfNotNull(result,metadata.getAnnotationAttributes(annotationClassName,false));
        Map<String, Object> container = metadata.getAnnotationAttributes(containerClassName, false);
        if(container != null && container.containsKey("value")){
            Map<String,Object>[] var5 = (Map<String,Object>[]) container.get("value");
            int var6 = var5.length;
            for (int var7 = 0; var7 < var6; var7++) {
                Map<String,Object> map = var5[var7];
                attributesIfNotNull(result,map);
            }
        }
        return Collections.unmodifiableSet(result);
    }

    private void attributesIfNotNull(Set<AnnotationAttributes> annotationAttributes, Map<String, Object> attributes) {
        if(attributes != null){
            annotationAttributes.add(AnnotationAttributes.fromMap(attributes));
        }
    }
}
