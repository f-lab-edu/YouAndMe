package com.yam.app.account.infrastructure;

import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {

    private static final String[] EXCLUDE_PATHS = {
        AccountApiUri.LOGIN,
        AccountApiUri.EMAIL_CONFIRM,
        AccountApiUri.REGISTER,
        "/api/accounts/error/**"
    };

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginAccountMethodArgumentResolver());
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SessionAuthInterceptor())
            .addPathPatterns("/api/**")
            .excludePathPatterns(EXCLUDE_PATHS);
    }
}
