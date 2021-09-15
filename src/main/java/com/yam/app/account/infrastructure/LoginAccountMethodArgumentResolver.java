package com.yam.app.account.infrastructure;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public final class LoginAccountMethodArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(LoginAccount.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
        NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        HttpSession session = ((HttpServletRequest) webRequest.getNativeRequest())
            .getSession(false);

        if (session == null) {
            return null;
        }

        var sessionManager = new SessionManager(session);
        return sessionManager.fetchPrincipal()
            .orElseThrow(IllegalArgumentException::new);
    }
}
