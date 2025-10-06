package com.room_911.controller;

import com.room_911.entity.Admin;
import com.room_911.entity.Attemp;
import com.room_911.entity.Employee;
import com.room_911.repository.AdminRepository;
import com.room_911.specification.AttempSpecification;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    AttempSpecification attempSpecification;

    @Autowired
    AdminRepository adminRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    SpringTemplateEngine springTemplateEngine;

    @GetMapping("/index")
    public String index(
            @RequestParam(name = "startDate", required = false) LocalDate dateInicio,
            @RequestParam(name = "endDate", required = false) LocalDate dateFinal,
            Model model
    ) {
        model.addAttribute("accesos", attempSpecification.filterAttemps(dateInicio, dateFinal, null));
        return "admin/index";
    }

    @GetMapping("/historial/pdf")
    public void generarPdf(
            HttpServletResponse response,
            @RequestParam(name = "startDate", required = false) LocalDate dateInicio,
            @RequestParam(name = "endDate", required = false) LocalDate dateFinal
    ) throws IOException {
        List<Attemp> attemps = attempSpecification.filterAttemps(dateInicio, dateFinal, null);

        Context context = new Context();
        context.setVariable("accesos", attemps);
        context.setVariable("startDate", dateInicio);
        context.setVariable("endDate", dateFinal);
        context.setVariable("titulo", "Historial de accesos - TODOS");

        String html = springTemplateEngine.process("admin/historialTodoPDF", context);

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=historial_accesos_todo.pdf");

        ITextRenderer renderer = new ITextRenderer();

        renderer.setDocumentFromString(html);
        renderer.layout();
        renderer.createPDF(response.getOutputStream());
        response.getOutputStream().flush();
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
