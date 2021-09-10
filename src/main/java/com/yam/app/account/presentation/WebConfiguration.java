package com.yam.app.account.presentation;

import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {

    private final LoginAccountMethodArgumentResolver loginAccountMethodArgumentResolver;

    public WebConfiguration(
        LoginAccountMethodArgumentResolver loginAccountMethodArgumentResolver) {
        this.loginAccountMethodArgumentResolver = loginAccountMethodArgumentResolver;
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(loginAccountMethodArgumentResolver);

    }
}
