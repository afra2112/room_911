package com.room_911.service;

import com.room_911.entity.Employee;
import com.room_911.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EmployeeService {

    @Autowired
    EmployeeRepository employeeRepository;

    public Employee crearEmpleado(Employee employee){
        if (employee.getEmployeeId() == null || employee.getEmployeeId().isEmpty()){
            employee.setEmployeeId(generarIdEmpleados());
        }
        return employeeRepository.save(employee);
    }

    public String generarIdEmpleados(){
        Optional<Employee> ultimoEmpleado = employeeRepository.findTopByOrderByEmployeeIdDesc();
        int numero = 1;

        if (ultimoEmpleado.isPresent()){
            String parteNumericaDelUltimoId = ultimoEmpleado.get().getEmployeeId().replaceAll("\\D+","");
            numero = Integer.parseInt(parteNumericaDelUltimoId) + 1;
        }

        return String.format("EMPLEADO%03d", numero);
    }

    public Optional<Employee> buscarPorId(String id){
        return employeeRepository.findById(id);
    }
}
