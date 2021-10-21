package com.yam.app;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public final class ActuatorApi {

    @GetMapping("/")
    public void hello() {

    }
}
