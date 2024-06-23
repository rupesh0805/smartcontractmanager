package com.project.scm.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PageController {

    @RequestMapping("/home")
    public String home(Model model){
        //Sending data to View
        model.addAttribute("name","Rupesh Ahirwar");
        model.addAttribute("role","Software Engineer");
        return "home";
    }

    //about route
    @RequestMapping("/about")
    public String aboutPage(){
        return "about";
    }

    //services
    @RequestMapping("/services")
    public String servicesPage(){
        return "services";
    }
}
