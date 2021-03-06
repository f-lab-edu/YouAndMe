package com.yam.app.account.infrastructure;

import com.yam.app.common.AuthenticationPrincipal;
import com.yam.app.common.UnauthorizedRequestException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public final class AuthenticationPrincipalArgumentResolver
    implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AuthenticationPrincipal.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
        NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        HttpSession session = ((HttpServletRequest) webRequest.getNativeRequest())
            .getSession(false);

        if (session == null) {
            throw new UnauthorizedRequestException("Unauthorized request");
        }

        var sessionManager = new SessionManager(session);
        return sessionManager.fetchPrincipal()
            .orElseThrow(() -> new UnauthorizedRequestException("Failed fetch principal"));
    }
}
