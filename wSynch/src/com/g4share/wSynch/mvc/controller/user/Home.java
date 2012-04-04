package com.g4share.wSynch.mvc.controller.user;


import com.g4share.wSynch.mvc.service.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.security.Principal;

@Controller
public class Home {
    private ConfigService loader;

    @Autowired
    public Home(@Qualifier("ConfigService") ConfigService loader) {
        this.loader = loader;
    }

    @RequestMapping(value="/user/home", method = RequestMethod.GET)
    public String getConfig(ModelMap model, Principal principal ) {
        String userName = "gm";//principal.getName();
        model.addAttribute("userName", userName);
        model.addAttribute("config", loader.getConfigInfo());
        return "home";
    }
}