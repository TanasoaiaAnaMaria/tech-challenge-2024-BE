package com.usv.Team.Finder.App.controller.othersTest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @GetMapping("/advice")
    public String getAdminAdvice(){
        return "You are an admin :)";
    }
}