package com.room_911.service;

import com.room_911.entity.ProductionDepartment;
import com.room_911.repository.EmployeeRepository;
import com.room_911.repository.ProductionDepartmentRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.List;

@Service
public class ExcelTemplateService {

    @Autowired
    ProductionDepartmentRepository productionDepartmentRepository;

    public void generateTemplateEmployee(HttpServletResponse response) throws IOException {
        List<ProductionDepartment> departments = productionDepartmentRepository.findAll();


        try (Workbook workbook = new XSSFWorkbook()){
            //primero creo una hoja en donde se insertaran la cabecera
            Sheet hoja = workbook.createSheet("Empleados");

            Row cabecera = hoja.createRow(0);
            cabecera.createCell(0).setCellValue("Nombre");
            cabecera.createCell(1).setCellValue("Apellido");
            cabecera.createCell(2).setCellValue("Departamento");
            cabecera.createCell(3).setCellValue("Tiene Acceso");

            String[] nombresDepartamentos = departments.stream().map(ProductionDepartment::getName).toArray(String[]::new);

            //creo una hoja oculta auxiliar en la que coloco una fila por cada nombre de departamento
            Sheet oculta = workbook.createSheet("departamentos");
            for (int i = 0; i < nombresDepartamentos.length; i++){
                oculta.createRow(i).createCell(0).setCellValue(nombresDepartamentos[i]);
            }

            //creo un rango en excel con nombre y le asigno el valor de las celdas que van a ocupar los departamentos en la hoja auxiliar
            Name rango = workbook.createName();
            rango.setNameName("lista_departamentos");
            rango.setRefersToFormula("'departamentos'!$A$1:$A$" + nombresDepartamentos.length);

            //creo un helper que me ayudara a crear una lista desplegable con .createFormulaListConstraint y le asigna los valores de mi lista en la hoja oculta
            DataValidationHelper helper = hoja.getDataValidationHelper();
            DataValidationConstraint constraint = helper.createFormulaListConstraint("lista_departamentos");
            //empieza desde la fila 1 porq 0 es la header, hasta la 100, y empieza en columna 2 (o sea la c) y termina ahi mismo
            CellRangeAddressList addressList = new CellRangeAddressList(1, 100, 2, 2);
            DataValidation validation = helper.createValidation(constraint, addressList);
            validation.setShowErrorBox(true);
            hoja.addValidationData(validation);

            DataValidationHelper helperAcceso = hoja.getDataValidationHelper();
            CellRangeAddressList rangoAcceso = new CellRangeAddressList(1, 100, 3, 3);
            DataValidationConstraint constraintAcceso = helperAcceso.createExplicitListConstraint(new String[]{"Si", "No"});
            DataValidation validationAcceso = helperAcceso.createValidation(constraintAcceso, rangoAcceso);
            validation.setShowErrorBox(true);
            hoja.addValidationData(validationAcceso);


            for(int i = 0; i < 4; i++){
                hoja.autoSizeColumn(i);
            }

            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=employee_template.xlsx");

            workbook.setSheetHidden(workbook.getSheetIndex(oculta), true);

            workbook.write(response.getOutputStream());
        }
    }
}
