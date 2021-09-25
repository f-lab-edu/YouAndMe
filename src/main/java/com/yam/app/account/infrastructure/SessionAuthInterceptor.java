package com.yam.app.account.infrastructure;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

public final class SessionAuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request,
        HttpServletResponse response, Object handler) throws Exception {

        var session = request.getSession(false);

        if (session == null) {
            request.setAttribute("message", "Unauthorized request");
            request.getRequestDispatcher(AccountApiUri.UNAUTHORIZED_REQUEST)
                .forward(request, response);
            return false;
        }

        if (session.getAttribute(SessionManager.LOGIN_ACCOUNT) == null) {
            request.setAttribute("message", "Failed fetch principal");
            request.getRequestDispatcher(AccountApiUri.UNAUTHORIZED_REQUEST)
                .forward(request, response);
            return false;
        }

        return true;
    }
}
