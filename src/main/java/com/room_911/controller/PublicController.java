package com.room_911.controller;

import com.room_911.config.enums.ResultEnum;
import com.room_911.entity.Attemp;
import com.room_911.entity.Employee;
import com.room_911.repository.AttempRepository;
import com.room_911.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.Optional;

@Controller
public class PublicController {

    @Autowired
    EmployeeService employeeService;

    @Autowired
    AttempRepository attempRepository;

    @GetMapping("/")
    public String acceso() {
        return "acceso";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/validar-acceso")
    public String validarAcceso(@RequestParam(name = "codigoEmpleado") String codigo, RedirectAttributes redirectAttributes){

        Optional<Employee> employee = employeeService.buscarPorId(codigo);
        Attemp attemp = new Attemp();

        attemp.setEntryNumber(codigo);
        attemp.setDate(LocalDateTime.now());

        if(employee.isEmpty()){
            attemp.setResult(ResultEnum.USER_NOT_REGISTERED);
            attemp.setDetails("El empleado con codigo: " + codigo + ". No esta registrado en el sistema");
            redirectAttributes.addFlashAttribute("tieneAcceso", false);
        } else if (employee.get().isHaveAccess()) {
            attemp.setResult(ResultEnum.SUCCESS);
            attemp.setDetails("El empleado accedio exitosamente a ROOM_911");
            attemp.setEmployee(employee.orElse(null));
            redirectAttributes.addFlashAttribute("tieneAcceso", true);
        } else {
            attemp.setResult(ResultEnum.DENIED);
            attemp.setDetails("El empleado no accedio a ROOM_911 porque no esta autorizado");
            attemp.setEmployee(employee.orElse(null));
            redirectAttributes.addFlashAttribute("tieneAcceso", false);
        }

        attempRepository.save(attemp);

        return "redirect:/";
    }
}
