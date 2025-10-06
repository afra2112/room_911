package com.room_911.controller;

import com.room_911.specification.AttempSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    AttempSpecification attempSpecification;

    @GetMapping("/index")
    public String index(
            @RequestParam(name = "startDate", required = false) LocalDate dateInicio,
            @RequestParam(name = "endDate", required = false) LocalDate dateFinal,
            Model model
    ) {
        model.addAttribute("accesos", attempSpecification.filterAttemps(dateInicio, dateFinal, null));
        return "admin/index";
    }

    @GetMapping("/historial")
    public String historial() {
        return "admin/historial";
    }
}
