package com.mulesoft.openbankingdemo.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
	
	@Override
    public void configure(HttpSecurity http) throws Exception {
        http
        	.csrf().disable()
        	.authorizeRequests()
            .antMatchers("/", "/login**").permitAll()
            .antMatchers("/authenticate").permitAll()
            .anyRequest().authenticated()
            .and().formLogin().loginPage("/login")
            .defaultSuccessUrl("/securedPage")
            .and()
            .logout()
            .logoutUrl("/logout")
            .deleteCookies("JSESSIONID")
            .logoutSuccessUrl("redirect:/login")
            .logoutSuccessHandler(new LogoutSuccessHandler() {
				
				@Override
				public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
						throws IOException, ServletException {
					HttpSession session=request.getSession();
					session.removeAttribute("SPRING_SECURITY_CONTEXT_KEY");
					session.removeAttribute("ACCESS_TOKEN");
				}
			});
    }
    
}
