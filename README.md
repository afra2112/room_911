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

### 🧑‍💼 Administrador ROOM_911
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

### 👷‍♂️ Empleado
- Simular intento de acceso al ROOM_911 con número de identificación interno.  
- Recibir respuesta de autorización o denegación.

---

## Diagramas

### Diagrama de Casos de Uso

```mermaid
%% Caso de uso ROOM_911
usecaseDiagram
actor "Administrador ROOM_911" as Admin
actor "Empleado" as Empleado

rectangle ROOM_911 {
  (Iniciar sesión) as UC1
  (Registrar empleado) as UC2
  (Cargar empleados por CSV) as UC3
  (Editar información del empleado) as UC4
  (Conceder o denegar acceso) as UC5
  (Buscar empleados) as UC6
  (Filtrar empleados por departamento) as UC7
  (Consultar histórico de accesos) as UC8
  (Filtrar accesos por rango de fechas) as UC9
  (Generar PDF del historial de accesos) as UC10
  (Registrar nuevos administradores) as UC11
  (Cerrar sesión) as UC12
  (Intentar acceso al ROOM_911) as UC13
  (Registrar intento de acceso) as UC14

  Admin --> UC1
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

  Empleado --> UC13
  UC13 --> UC14 : <<include>>

  UC4 --> UC5 : <<extend>>
  UC8 --> UC9 : <<include>>
  UC8 --> UC10 : <<extend>>
}

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

Versión: 1.0
Autor: Andres Ramirez
Fecha: 10/05/2025
