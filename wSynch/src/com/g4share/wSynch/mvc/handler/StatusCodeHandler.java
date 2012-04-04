package com.g4share.wSynch.mvc.handler;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class StatusCodeHandler {
    @RequestMapping(value = "/accessDenied")
    public String accessDenied() {

        return "statusCode/403";
    }

    @RequestMapping(value = "/missing")
    public String missing() {

        return "statusCode/404";
    }
}
