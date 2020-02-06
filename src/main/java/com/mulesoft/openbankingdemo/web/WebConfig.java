package com.mulesoft.openbankingdemo.web;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

// @EnableWebMvc
// @Configuration
// @ComponentScan(basePackages = { "com.mulesoft.openbankingdemo.web.controller" })
public class WebConfig implements WebMvcConfigurer {
/*	
	   @Override
	   public void addViewControllers(ViewControllerRegistry registry) {
	      registry.addViewController("/").setViewName("index");
	      registry.addRedirectViewController("/home", "/customer");
	      registry.addStatusController("/account", HttpStatus.BAD_REQUEST);  
	      
	   }
	 
	   @Bean
	   public ViewResolver viewResolver() {
	      InternalResourceViewResolver bean = new InternalResourceViewResolver();
	 
	      bean.setViewClass(JstlView.class);
	      bean.setPrefix("/WEB-INF/");
	      bean.setSuffix(".jsp");
	 
	      return bean;
	   }
*/
}
