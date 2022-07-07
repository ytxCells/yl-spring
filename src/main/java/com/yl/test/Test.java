package com.yl.test;

import com.yl.spring.YlApplicationContent;
import com.yl.test.service.UserService;

/**
 * @author ytx
 * @date 2022/7/7 14:21
 */
public class Test {
    public static void main(String[] args) {
        YlApplicationContent applicationContent = new YlApplicationContent(AppConfig.class);
        UserService userService = (UserService) applicationContent.getBean("userService");
        userService.test();
    }
}
