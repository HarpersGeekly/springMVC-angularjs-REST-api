package com.codstrainingapp.trainingapp;

import com.codstrainingapp.trainingapp.models.Password;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;
import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableWebMvc
@EnableTransactionManagement
@ComponentScan(basePackages= {"com.codstrainingapp.trainingapp"})
@PropertySource("classpath:application.properties")
public class WebConfig extends WebMvcConfigurerAdapter {

    @Autowired
    private Environment environment;

    @Bean
    public ViewResolver viewResolver() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setViewClass(JstlView.class);
        viewResolver.setPrefix("/WEB-INF/views/");
        viewResolver.setSuffix(".jsp");
        return viewResolver;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // <mvc:resources mapping="/styles/**" location="/css/" />
        registry
                .addResourceHandler("/static/**")
                .addResourceLocations("/static/"); // Spring 4.1
    }

    @Bean
    public LocalSessionFactoryBean sessionFactory() {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource());
        sessionFactory.setPackagesToScan("com.codstrainingapp.trainingapp.models");
        sessionFactory.setHibernateProperties(hibernateProperties());
        return sessionFactory;
    }

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(environment.getRequiredProperty("spring.datasource.driver-class-name"));
        dataSource.setUrl(environment.getRequiredProperty("spring.datasource.url"));
        dataSource.setUsername(environment.getRequiredProperty("spring.datasource.username"));
        dataSource.setPassword(environment.getRequiredProperty("spring.datasource.password"));
        return dataSource;
    }

    private Properties hibernateProperties() {
        Properties properties = new Properties();
//        properties.put("hibernate.dialect", environment.getRequiredProperty("hibernate.dialect"));
//        properties.put("hibernate.format_sql", environment.getRequiredProperty("hibernate.format_sql"));
        properties.put("true", environment.getRequiredProperty("spring.jpa.show-sql"));
        properties.put("org.hibernate.dialect.MySQLDialect", environment.getRequiredProperty("spring.jpa.database-platform"));
        properties.put("hibernate.hbm2ddl.auto", environment.getRequiredProperty("spring.jpa.hibernate.ddl-auto"));
        return properties;
    }

    @Bean
    @Autowired
    public HibernateTransactionManager transactionManager(SessionFactory s) {
        HibernateTransactionManager txManager = new HibernateTransactionManager();
        txManager.setSessionFactory(s);
        return txManager;
    }

}

//    @Configuration indicates that this class contains one or more bean methods annotated with
//    @Bean producing bean manageable by spring container. Above Configuration class is equivalent to following XML counterpart:
//    http://websystique.com/springmvc/spring-4-mvc-helloworld-tutorial-annotation-javaconfig-full-example/

//    @EnableWebMvc is equivalent to mvc:annotation-driven in XML. It enables support for @Controller-annotated classes
//    that use @RequestMapping to map incoming requests to specific method.

//    @ComponentScan is equivalent to context:component-scan base-package="..." providing with where to look for spring managed beans/classes.

//in addition:

//@Configuration indicates that this class contains one or more bean methods annotated with @Bean producing beans manageable by spring container. In our case, this class represent hibernate configuration.
//@ComponentScan is equivalent to context:component-scan base-package="..." in xml, providing with where to look for spring managed beans/classes.
//@EnableTransactionManagement is equivalent to Spring’s tx:* XML namespace, enabling Spring’s annotation-driven transaction management capability.
//@PropertySource is used to declare a set of properties(defined in a properties file in application classpath) in Spring run-time Environment, providing flexibility to have different values in different application environments.
//
//        Method sessionFactory() is creating a LocalSessionFactoryBean, which exactly mirrors the XML based configuration : We need a dataSource and hibernate properties (same as hibernate.properties). Thanks to @PropertySource, we can externalize the real values in a .properties file, and use Spring’s Environment to fetch the value corresponding to an item. Once the SessionFactory is created, it will be injected into Bean method transactionManager which may eventually provide transaction support for the sessions created by this sessionFactory.
//
//        Below is the properties file used in this post.
//        /src/main/resources/application.properties
//
//        jdbc.driverClassName = com.mysql.jdbc.Driver
//        jdbc.url = jdbc:mysql://localhost:3306/websystique
//        jdbc.username = myuser
//        jdbc.password = mypassword
//        hibernate.dialect = org.hibernate.dialect.MySQLDialect
//        hibernate.show_sql = true
//        hibernate.format_sql = true