package com.room_911.service;

import com.room_911.entity.Employee;
import com.room_911.repository.EmployeeRepository;
import com.room_911.repository.ProductionDepartmentRepository;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.io.InputStream;

@Service
public class ImportExcelEmployess {

    @Autowired
    ProductionDepartmentRepository productionDepartmentRepository;

    @Autowired
    EmployeeRepository employeeRepository;

    public int importEmployeesFromExcel(MultipartFile excel){
        int insertsNum = 0;
        try (InputStream inputStream = excel.getInputStream()){

            Workbook workbook = WorkbookFactory.create(inputStream);
            Sheet hoja = workbook.getSheetAt(0);

            for (int i = 1; i <= hoja.getLastRowNum(); i++){
                Row fila = hoja.getRow(i);
                if (fila == null) continue;

                Employee employee = new Employee();

                employee.setName(fila.getCell(0).getStringCellValue());
                employee.setSurname(fila.getCell(1).getStringCellValue());
                employee.setProductionDepartment(productionDepartmentRepository.findByName(fila.getCell(2).getStringCellValue()));

                Cell celdaAcceso = fila.getCell(3);
                boolean haveAccess = false;

                if(celdaAcceso != null && celdaAcceso.getCellType() == CellType.STRING){
                    String valor = celdaAcceso.getStringCellValue().trim().toLowerCase();

                    if(valor.equals("si")){
                        haveAccess = true;
                    }
                }

                employee.setHaveAccess(haveAccess);

                employeeRepository.save(employee);
                insertsNum ++;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return insertsNum;
    }
}
