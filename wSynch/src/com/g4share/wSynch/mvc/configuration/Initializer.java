package com.g4share.wSynch.mvc.configuration;


import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

public class Initializer implements WebApplicationInitializer {
    public void onStartup(ServletContext servletContext)
            throws ServletException {
        AnnotationConfigWebApplicationContext mvcContext = new AnnotationConfigWebApplicationContext();
        mvcContext.register(MvcConfig.class);

        ServletRegistration.Dynamic dispatcher = servletContext.addServlet(
                "mvc-dispatcher", new DispatcherServlet(mvcContext));
        dispatcher.setLoadOnStartup(2);
        dispatcher.addMapping("/");
    }
}
