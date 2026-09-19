package it.aulab.final_project_giuseppe.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import it.aulab.final_project_giuseppe.services.UserService;

@Controller 
public class UserController {

    @Autowired 
    private UserService userService;

    // Rotta di home
    @GetMapping
    public String home() {
        return "home";
    } 

}