package com.room_911.controller;

import com.room_911.service.ExcelTemplateService;
import com.room_911.service.ImportExcelEmployess;
import com.room_911.specification.EmployeeSpecification;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
@RequestMapping("/admin/empleados")
public class EmployeeController {

    @Autowired
    EmployeeSpecification employeeSpecification;

    @Autowired
    ExcelTemplateService excelTemplateService;

    @Autowired
    ImportExcelEmployess importExcelEmployess;

    @GetMapping("/")
    public String empleados(@RequestParam(name = "query", required = false) String query, Model model) {
        model.addAttribute("empleados", employeeSpecification.filterEmployees(query));
        return "admin/empleados";
    }

    @GetMapping("/plantilla")
    public void plantilla(HttpServletResponse response) throws IOException {
        excelTemplateService.generateTemplateEmployee(response);
    }

    @PostMapping("/cargar")
    public String cargarEmpleados(@RequestParam(name = "file")MultipartFile excel, RedirectAttributes redirectAttributes){
        try {
            redirectAttributes.addFlashAttribute("cargaExitosa", "Se importaron exitosamente " + importExcelEmployess.importEmployeesFromExcel(excel) + " empleados.");
        } catch (Exception e){
            redirectAttributes.addFlashAttribute("error", "Hubo un error importando los empleados" + e.getMessage());
        }
        return "redirect:/admin/empleados/";
    }
}
