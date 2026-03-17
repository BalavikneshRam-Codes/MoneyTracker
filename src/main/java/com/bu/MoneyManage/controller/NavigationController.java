package com.bu.MoneyManage.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class NavigationController {
    @GetMapping("/createNotes")
    public String createNotes() {
        return "createNotes";
    }
    @GetMapping("/")
    public String login() {
        return "login";
    }
    @GetMapping("/notes")
    public String notes() {
        return "notes";
    }
}
