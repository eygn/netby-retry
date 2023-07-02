package com.netby.biz.retry.log;

import com.netby.biz.retry.annotation.LogPrinter;
import com.netby.common.util.StringUtil;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.MethodMatcher;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.annotation.AnnotationClassFilter;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;

/**
 * @author: byg
 */
@Slf4j
public class LogMatchingPointcut implements Pointcut, ClassFilter, MethodMatcher {

    private final Set<String> classnameSet = new HashSet<>();

    private final ClassFilter classFilter = new AnnotationClassFilter(LogPrinter.class);

    public LogMatchingPointcut(String packagePath, ResourceLoader resourceLoader) {
        if (StringUtil.isNotBlank(packagePath)) {
            ResourcePatternResolver resolver = ResourcePatternUtils.getResourcePatternResolver(resourceLoader);
            String[] pathArray = packagePath.split(",", -1);
            try {
                for (String path : pathArray) {
                    path = StringUtil.replace(path, ".", "/");
                    MetadataReaderFactory readerFactory = new CachingMetadataReaderFactory(resourceLoader);
                    String locationPattern = "classpath*:" + path + "/*.class";
                    Resource[] resources = resolver.getResources(locationPattern);
                    for (Resource resource : resources) {
                        MetadataReader reader = readerFactory.getMetadataReader(resource);
                        ClassMetadata classMetadata = reader.getClassMetadata();
                        String className = classMetadata.getClassName();
                        Class<?> clazz = LogMatchingPointcut.class.getClassLoader().loadClass(className);
                        if (!clazz.isInterface()) {
                            classnameSet.add(clazz.getName());
                        }
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e.getLocalizedMessage(), e);
            }
        }
    }

    @NonNull
    @Override
    public ClassFilter getClassFilter() {
        return this;
    }

    @NonNull
    @Override
    public MethodMatcher getMethodMatcher() {
        return this;
    }

    @Override
    public boolean matches(Class<?> clazz) {
        if (clazz.isInterface()) {
            return false;
        }
        // 注解声明的类可以
        if (classFilter.matches(clazz)) {
            return clazz.getAnnotation(LogPrinter.class).enable();
        }
        // 扫描包下的所有类都可以
        if (classnameSet.contains(clazz.getName())) {
            return true;
        }
        // 类中注解声明的方法也可以
        Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            boolean flag = false;
            LogPrinter logPrinter = method.getAnnotation(LogPrinter.class);
            if (logPrinter != null) {
                flag = true;
            }
            return flag;
        }
        return false;
    }

    @Override
    public boolean matches(@NonNull Method method, @NonNull Class<?> targetClass) {
        if (targetClass.isInterface()) {
            return false;
        }
        boolean flag;
        // 能到这里，有三种情况
        // 1. 类上有注解，且 enable=true
        LogPrinter logPrinter = targetClass.getAnnotation(LogPrinter.class);
        if (logPrinter != null) {
            flag = methodMatch(method, true);
        } else if (classnameSet.contains(targetClass.getName())) {
            // 2. 扫描包包含类
            flag = methodMatch(method, true);
        } else {
            // 3. 某个方法上有注解
            flag = methodMatch(method, false);
        }
        // 只支持 public 方法,非 static 方法
        return flag && Modifier.isPublic(method.getModifiers())
                && !Modifier.isStatic(method.getModifiers());
    }

    private static boolean methodMatch(@NonNull Method method, boolean defaultValue) {
        boolean flag;
        LogPrinter logPrinter = method.getAnnotation(LogPrinter.class);
        if (logPrinter != null) {
            flag = logPrinter.enable();
        } else {
            flag = defaultValue;
        }
        return flag;
    }

    @Override
    public boolean isRuntime() {
        return false;
    }

    @Override
    public boolean matches(@NonNull Method method, @NonNull Class<?> targetClass, Object... args) {
        throw new UnsupportedOperationException("Illegal MethodMatcher usage");
    }
}
