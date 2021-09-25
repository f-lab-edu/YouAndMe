package com.yam.app.account.presentation;

import com.yam.app.account.infrastructure.AccountApiUri;
import com.yam.app.common.UnauthorizedRequestException;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(
    produces = MediaType.APPLICATION_JSON_VALUE,
    consumes = MediaType.APPLICATION_JSON_VALUE
)
public final class AccountErrorApi {

    @GetMapping(AccountApiUri.UNAUTHORIZED_REQUEST)
    public void apiError(HttpServletRequest request) throws UnauthorizedRequestException {
        throw new UnauthorizedRequestException(
            Optional.ofNullable((String) request.getAttribute("message"))
                .orElse("Unauthorized request"));
    }
}
