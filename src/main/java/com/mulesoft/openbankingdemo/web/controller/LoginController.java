package com.mulesoft.openbankingdemo.web.controller;

import javax.servlet.http.HttpSession;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import com.mulesoft.openbankingdemo.web.bean.Login;
import com.mulesoft.openbankingdemo.web.bean.OpenBankingPrincipal;

@Controller
public class LoginController {
	
	@PostMapping("/authenticate")
	public String processLogin(HttpSession session,Login user) {
		
		System.out.println("************ Username="+user.getUsername());
		
		System.out.println("HTTP SESSION="+session.getId());
		
		UsernamePasswordAuthenticationToken authReq
	      = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
		Authentication auth=new AuthenticationManagerImpl().authenticate(authReq);
		
		// Adding additional details to session context with the access_token
		OpenBankingPrincipal principal=(OpenBankingPrincipal)auth.getPrincipal();
	     
	    SecurityContext sc = SecurityContextHolder.getContext();
	    sc.setAuthentication(auth);
	    session.setAttribute("SPRING_SECURITY_CONTEXT_KEY", sc);
	    session.setAttribute("ACCESS_TOKEN", principal.getAccessToken());
		
//		return "forward:/transfer";
	    // The landing page is not fund-transfer but customer dashboard.
		return "forward:/customerdashboard";
	}

}
