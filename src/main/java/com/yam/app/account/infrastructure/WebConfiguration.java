package com.yam.app.account.infrastructure;

import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {

    private static final String[] EXCLUDE_PATHS = {
        "/api/accounts/login",
        "/api/accounts/authorize",
        "/api/accounts",
        "/api/articles/all",
        "/api/articles/{articleId:[0-9]+}",
        "/api/comments/{articleId:[0-9]+}",
        "/actuator/**"
    };

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new AuthenticationPrincipalArgumentResolver());
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SessionAuthInterceptor())
            .addPathPatterns("/api/**")
            .excludePathPatterns(EXCLUDE_PATHS);
    }
}
