package com.mulesoft.openbankingdemo.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mulesoft.openbankingdemo.web.bean.FundTransfer;
import com.mulesoft.openbankingdemo.web.bean.Login;

import java.security.Principal;

import javax.servlet.http.HttpSession;

@Controller
public class WebController {

    @RequestMapping("/")
    public String index(Model model, Principal principal) {
    	model.addAttribute(new Login());
        return "login";
    }
    
    @RequestMapping("/login")
    public String login(Model model, Principal principal) {
    	model.addAttribute(new Login());
        return "login";
    }
    
    @RequestMapping("/logout")
    public String logout(Model model, Principal principal) {
    	model.addAttribute(new Login());
        return "login";
    }
    
}