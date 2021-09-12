package com.yam.app.account.presentation;

import com.yam.app.account.application.AccountFacade;
import com.yam.app.account.common.SessionConst;
import com.yam.app.account.domain.AccountPrincipal;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public final class LoginAccountMethodArgumentResolver implements HandlerMethodArgumentResolver {

    private final AccountFacade accountFacade;

    public LoginAccountMethodArgumentResolver(
        AccountFacade accountFacade) {
        this.accountFacade = accountFacade;
    }

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
            throw new IllegalStateException();
        }

        return accountFacade.getLoginAccount(
            (AccountPrincipal) session.getAttribute(SessionConst.LOGIN_ACCOUNT_PRINCIPAL));

    }
}
