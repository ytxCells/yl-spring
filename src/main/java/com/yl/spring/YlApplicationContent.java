package com.yl.spring;
import com.yl.spring.annotation.Component;
import com.yl.spring.annotation.ComponentScan;
import com.yl.test.AppConfig;

import java.beans.Introspector;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;

/**
 * @author ytx
 * @date 2022/7/7 14:03
 */
public class YlApplicationContent {
    private Class configClass;
    public YlApplicationContent(Class<AppConfig> configClass) {
        this.configClass = configClass;
        //扫描
        scan(configClass);
        //遍历Bean
    }

    public Object getBean(String userService) {
        return null;
    }

    private void scan(Class<AppConfig> configClass) {
        if (configClass.isAnnotationPresent(ComponentScan.class)){
            ComponentScan componentScanAnnotation = configClass.getAnnotation(ComponentScan.class);
            String path = componentScanAnnotation.value();
            System.out.println(path);
            //相对路径
            path = path.replace(".","/");
            //类加载器
            ClassLoader classLoader = YlApplicationContent.class.getClassLoader();
            URL resource = classLoader.getResource(path);
            File file = new File(resource.getFile());
            try {
                file = new File(URLDecoder.decode(resource.getFile(),"utf-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            //是否是目录
            if (file.isDirectory()){
                for (File f : file.listFiles()){
                    String absolutePath = f.getAbsolutePath();
                    absolutePath = absolutePath.substring(absolutePath.indexOf("com"),absolutePath.indexOf(".class"));
                    absolutePath = absolutePath.replace("/",".");
                    try {
                        Class<?> clazz = classLoader.loadClass(absolutePath);
                        if (clazz.isAnnotationPresent(Component.class)){
                           Component componentAnnotation = clazz.getAnnotation(Component.class);
                           String beanName = componentAnnotation.value();
                           if ("".equals(beanName)){
                               System.out.println(clazz.getSimpleName());
                               beanName = Introspector.decapitalize(clazz.getSimpleName());
                           }
                            System.out.println(beanName);
                        }
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }else {
                System.out.println("没有文件");
            }
        }
    }
}
