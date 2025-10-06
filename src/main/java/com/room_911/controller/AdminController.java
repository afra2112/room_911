package com.room_911.controller;

import com.room_911.entity.Admin;
import com.room_911.repository.AdminRepository;
import com.room_911.specification.AttempSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    AttempSpecification attempSpecification;

    @Autowired
    AdminRepository adminRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @GetMapping("/index")
    public String index(
            @RequestParam(name = "startDate", required = false) LocalDate dateInicio,
            @RequestParam(name = "endDate", required = false) LocalDate dateFinal,
            Model model
    ) {
        model.addAttribute("accesos", attempSpecification.filterAttemps(dateInicio, dateFinal, null));
        return "admin/index";
    }

    @GetMapping("/admins")
    public String admins(@RequestParam(name = "eliminados") boolean eliminados, Model model) {
        if(eliminados){
            model.addAttribute("admins", adminRepository.findByActive(false));
            model.addAttribute("eliminados", true);
        }else {
            model.addAttribute("admins", adminRepository.findByActive(true));
            model.addAttribute("eliminados", false);
        }
        return "admin/admins";
    }

    @PostMapping("/admins/eliminar/{id}")
    public String eliminarAdmin(@PathVariable(name = "id") Long id) {
        Admin admin = adminRepository.findById(id).orElseThrow();
        admin.setActive(false);
        adminRepository.save(admin);
        return "redirect:/admin/admins?eliminados=false";
    }

    @PostMapping("/admins/nuevo")
    public String crearAdmin(@ModelAttribute Admin admin) {
        Admin nuevoAdmin = new Admin();
        admin.setAdminName(admin.getAdminName());
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        admin.setUsername(admin.getUsername());
        admin.setActive(true);
        adminRepository.save(admin);
        return "redirect:/admin/admins?eliminados=false";
    }

    @GetMapping("/historial")
    public String historial() {
        return "admin/historial";
    }
}
