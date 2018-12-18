package com.codstrainingapp.trainingapp;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class MyWebAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {
//    implements WebApplicationInitializer

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[] { WebConfig.class };
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return null;
    }

    @Override
    protected String[] getServletMappings() {
        return new String[] { "/" };
    }

//    public void onStartup(ServletContext container) throws ServletException {
//
//        AnnotationConfigWebApplicationContext ctx = new AnnotationConfigWebApplicationContext();
//        ctx.register(WebConfig.class);
//        ctx.setServletContext(container);
//
//        ServletRegistration.Dynamic servlet = container.addServlet("dispatcher", new DispatcherServlet(ctx));
//
//        servlet.setLoadOnStartup(1);
//        servlet.addMapping("/");
//    }
}

//    The content above resembles the content of web.xml from previous tutorial as we are using the front-controller
//        DispatcherServlet, assigning the mapping (url-pattern in xml) and instead of providing the path to spring
//        configuration file(spring-servlet.xml) , here we are registering the Configuration Class.
//        Overall, we are doing the same thing, just the approach is different.

//UPDATE: Note that now you can write the above class even more concisely [and it’s the preferred way],
//        by extending AbstractAnnotationConfigDispatcherServletInitializer base class, as shown below:
