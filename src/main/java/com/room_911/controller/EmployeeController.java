package com.room_911.controller;

import com.room_911.entity.Attemp;
import com.room_911.entity.Employee;
import com.room_911.repository.ProductionDepartmentRepository;
import com.room_911.service.EmployeeService;
import com.room_911.service.ExcelTemplateService;
import com.room_911.service.ImportExcelEmployess;
import com.room_911.specification.AttempSpecification;
import com.room_911.specification.EmployeeSpecification;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/admin/empleados")
public class EmployeeController {

    @Autowired
    EmployeeSpecification employeeSpecification;

    @Autowired
    AttempSpecification attempSpecification;

    @Autowired
    EmployeeService employeeService;

    @Autowired
    ProductionDepartmentRepository productionDepartmentRepository;

    @Autowired
    ExcelTemplateService excelTemplateService;

    @Autowired
    ImportExcelEmployess importExcelEmployess;

    @Autowired
    SpringTemplateEngine springTemplateEngine;

    @GetMapping("/")
    public String empleados(@RequestParam(name = "query", required = false) String query, @RequestParam(name = "departmentId", required = false) Long department, Model model) {
        model.addAttribute("empleados", employeeSpecification.filterEmployees(query, department));
        model.addAttribute("departamentos", productionDepartmentRepository.findAll());
        return "admin/empleados";
    }

    @GetMapping("/plantilla")
    public void plantilla(HttpServletResponse response) throws IOException {
        excelTemplateService.generateTemplateEmployee(response);
    }

    @GetMapping("/editar/{id}")
    public String editarEmpleado(@PathVariable(name = "id") String idEmpleado, Model model) {
        model.addAttribute("empleado", employeeService.buscarPorId(idEmpleado));
        model.addAttribute("departamentos", productionDepartmentRepository.findAll());
        return "admin/editarEmpleados";
    }

    @PostMapping("/nuevo")
    public String registrarEmpleado(@ModelAttribute Employee empleado) {
        employeeService.crearEmpleado(empleado);
        return "redirect:/admin/empleados/";
    }

    @GetMapping("/historial/{id}")
    public String historialEmpleado(
            @PathVariable(name = "id") String employeeId,
            @RequestParam(name = "startDate", required = false) LocalDate dateInicio,
            @RequestParam(name = "endDate", required = false) LocalDate dateFinal,
            Model model
    ) {
        model.addAttribute("accesos", attempSpecification.filterAttemps(dateInicio, dateFinal, employeeId));
        model.addAttribute("empleado", employeeService.buscarPorId(employeeId).orElseThrow());
        return "admin/historialEmpleado";
    }

    @GetMapping("/historial/pdf/{id}")
    public void generarPdf(
            HttpServletResponse response,
            @PathVariable(name = "id") String employeeId,
            @RequestParam(name = "startDate", required = false) LocalDate dateInicio,
            @RequestParam(name = "endDate", required = false) LocalDate dateFinal
    ) throws IOException {
        Employee employee = employeeService.buscarPorId(employeeId).orElseThrow();
        List<Attemp> attemps = attempSpecification.filterAttemps(dateInicio, dateFinal, employeeId);

        Context context = new Context();
        context.setVariable("startDate", dateInicio);
        context.setVariable("endDate", dateFinal);
        context.setVariable("accesos", attemps);
        context.setVariable("empleado", employee);
        context.setVariable("titulo", "Historial de accesos - " + employee.getName() + " " + employee.getSurname());

        String html = springTemplateEngine.process("admin/historialEmpleadoPDF", context);

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=historial_accesos_"+employee.getName()+"_"+employee.getSurname()+".pdf");

        ITextRenderer renderer = new ITextRenderer();

        renderer.setDocumentFromString(html);
        renderer.layout();
        renderer.createPDF(response.getOutputStream());
        response.getOutputStream().flush();
    }

    @PostMapping("/cargar")
    public String cargarEmpleados(@RequestParam(name = "file")MultipartFile excel, RedirectAttributes redirectAttributes){
        try {
            redirectAttributes.addFlashAttribute("cargaExitosa", "Se importaron exitosamente " + importExcelEmployess.importEmployeesFromExcel(excel) + " empleados.");
        } catch (Exception e){
            redirectAttributes.addFlashAttribute("error", "Hubo un error importando los empleados. Razon: " + e.getMessage());
        }
        return "redirect:/admin/empleados/";
    }
}
