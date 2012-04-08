package com.g4share.wSynch.mvc.controller.user;


import com.g4share.jSynch.share.ConfigHash;
import com.g4share.wSynch.mvc.service.W3StatusInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.security.Principal;

@Service
@Controller
public class Home {
    private W3StatusInfo loader;

    @Autowired
    public Home(@Qualifier("W3StatusInfoLoaderService") W3StatusInfo loader) {
        this.loader = loader;
    }

    @RequestMapping(value="/user/home", method = RequestMethod.GET)
    public String getConfig(ModelMap model, Principal principal ) {
        String userName = "gm";//principal.getName();
        model.addAttribute("userName", userName);

        ConfigHash cHash = loader.getConfigHash();
        model.addAttribute("config", cHash);

        return "home";
    }
}