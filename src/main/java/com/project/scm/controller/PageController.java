package com.project.scm.controller;

import com.project.scm.entities.User;
import com.project.scm.forms.UserForm;
import com.project.scm.helper.Message;
import com.project.scm.helper.MessageType;
import com.project.scm.services.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PageController {

    private UserService userService;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public PageController(UserService userService, PasswordEncoder passwordEncoder){
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @RequestMapping("/")
    public String index(){
        return "home";
    }

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

    @RequestMapping("/contact")
    public String contactPage(){
        return "contact";
    }

    @RequestMapping("/login")
    public String loginPage(){
        return "login";
    }

    @RequestMapping("/register")
    public String registerPage(Model model){
        UserForm userForm = new UserForm();
        model.addAttribute("userForm",userForm);
        return "register";
    }

    @PostMapping("/do-register")
    public String processRegister(@Valid @ModelAttribute UserForm userForm, HttpSession session, BindingResult bindingResult){
        //fetch form data
        //validate data
        if(bindingResult.hasErrors()){
            return "register";
        }
        //save to DB
        User user = new User();
        user.setName(userForm.getName());
        user.setEmail(userForm.getEmail());
        user.setPassword(userForm.getPassword());
        user.setAbout(userForm.getAbout());
        user.setPhoneNumber(userForm.getPhoneNumber());

        User savedUser = userService.saveUser(user);
        System.out.println("User Saved");
        //Message "Registration successful"
        Message message = Message.builder().content("Registration Successful").type(MessageType.green).build();
        session.setAttribute("message", message);
        //redirect to login page
        return "redirect:/register";
    }

}
