package com.g4share.wSynch.mvc.configuration;

import com.g4share.wSynch.mvc.service.W3StatusInfoLoader;
import org.springframework.context.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@Configuration
@EnableWebMvc
@ImportResource(value = {"/WEB-INF/config.xml"})

@ComponentScan(basePackages =   {
                                    "com.g4share.wSynch.mvc.handler",           /*handlers*/
                                    "com.g4share.wSynch.mvc.controller",
                                    "com.g4share.wSynch.mvc.controller.user",   /*controllers*/

                                    "com.g4share.wSynch.mvc.service",           /*beans*/
                                    "com.g4share.jSynch.log",
                                    "com.g4share.jSynch.config",
                                    "com.g4share.jSynch.share"
                                })
@Import(W3StatusInfoLoader.class)
public class MvcConfig {
    @Bean
    public InternalResourceViewResolver configureInternalResourceViewResolver() {
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setPrefix("/WEB-INF/view/");
        resolver.setSuffix(".jsp");
        return resolver;
    }

}