package com.yam.app.account.infrastructure;

import com.yam.app.common.UnauthorizedRequestException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

public final class SessionAuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request,
        HttpServletResponse response, Object handler) throws Exception {

        var session = request.getSession(false);
        if (session == null) {
            throw new UnauthorizedRequestException("Unauthorized request");
        }

        var sessionManager = new SessionManager(session);

        if (!sessionManager.isExistPrincipal()) {
            throw new UnauthorizedRequestException("Failed fetch principal");
        }

        return true;
    }
}
