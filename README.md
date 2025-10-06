# ROOM_911

Módulo de control de acceso ROOM_911 – Historias de Usuario

**Objetivo general:**  
Diseñar y desarrollar un módulo web que permita gestionar el acceso del personal autorizado al cuarto ROOM_911, mediante autenticación, control de permisos, registro de accesos y generación de reportes.

---

## Actores

- **Administrador ROOM_911**
- **Empleado**

---

## Historias de Usuario

### Administrador ROOM_911
- Iniciar sesión con usuario y contraseña.  
- Registrar nuevos empleados con datos básicos y asignar departamento.  
- Cargar un archivo CSV con varios empleados.  
- Editar información de un empleado y conceder/denegar acceso.  
- Consultar empleados por ID, nombre o apellido.  
- Filtrar empleados por departamento.  
- Consultar histórico de intentos de acceso de un empleado.  
- Filtrar intentos de acceso por rango de fechas.  
- Descargar en PDF el historial de intentos de acceso de un empleado.  
- Registrar y gestionar otros usuarios administradores.

### Empleado
- Simular intento de acceso al ROOM_911 con número de identificación interno.  

---

## Diagramas

## Casos de Uso - ROOM_911
```mermaid
graph TD
    subgraph ROOM_911
        UC1[Iniciar sesión]
        UC2[Registrar empleado]
        UC3[Cargar empleados por CSV]
        UC4[Editar información del empleado]
        UC5[Conceder o denegar acceso]
        UC6[Buscar empleados]
        UC7[Filtrar empleados por departamento]
        UC8[Consultar histórico de accesos]
        UC9[Filtrar accesos por rango de fechas]
        UC10[Generar PDF del historial de accesos]
        UC11[Registrar nuevos administradores]
        UC12[Cerrar sesión]
        UC13[Intentar acceso al ROOM_911]
        UC14[Registrar intento de acceso]
    end

    Admin[Administrador ROOM_911] --> UC1
    Admin --> UC2
    Admin --> UC3
    Admin --> UC4
    Admin --> UC5
    Admin --> UC6
    Admin --> UC7
    Admin --> UC8
    Admin --> UC9
    Admin --> UC10
    Admin --> UC11
    Admin --> UC12

    Empleado[Empleado] --> UC13
    UC13 --> UC14

    UC4 -.-> UC5
    UC8 --> UC9
    UC8 -.-> UC10
```

## Modelo Relacional - ROOM_911
```mermaid
erDiagram
    ADMIN {
        int adminId PK
        string adminName
        string username
        string password
        boolean active
        boolean deleted
    }

    PRODUCTION_DEPARTMENT {
        int departmentId PK
        string name
    }

    EMPLOYEE {
        string employeeId PK
        string name
        string surname
        bigint document
        boolean haveAccess
        boolean active
        int departmentId FK
    }

    ATTEMP {
        int attempId PK
        datetime date
        string result
        string entryNumber
        string details
        string employeeId FK
    }

    EMPLOYEE }|--|| PRODUCTION_DEPARTMENT : "pertenece a"
    ATTEMP }|--|| EMPLOYEE : "realizado por"
```

Versión: 1.0
Autor: Andres Ramirez
Fecha: 10/05/2025
