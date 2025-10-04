package com.room_911.controller;

import com.room_911.specification.EmployeeSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @GetMapping("/index")
    public String index() {
        return "admin/index";
    }

    @GetMapping("/historial")
    public String historial() {
        return "admin/historial";
    }
}
