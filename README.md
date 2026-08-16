# Sistema de Control de Préstamos de Biblioteca 📚

Este es un sistema de escritorio diseñado y desarrollado para la gestión y el control de préstamos de libros en una biblioteca. Permite administrar de manera segura e interactiva el registro de usuarios, empleados, catálogo de libros y el historial de transacciones de préstamos, controlando el stock disponible de los textos en tiempo real.

---

## 🛠️ Tecnologías y Lenguajes
El proyecto ha sido desarrollado utilizando herramientas estándares y profesionales del desarrollo de software:
* **Lenguaje de Programación:** Java (JDK 17 o superior).
* **Gestor de Proyectos / Construcción:** Apache Maven.
* **Interfaz Gráfica de Usuario (GUI):** Java Desktop (Swing).
* **Motor de Base de Datos:** MySQL Server (Relacional).
* **Conectividad:** JDBC (Java Database Connectivity) con el controlador oficial `mysql-connector-j`.

---

## 🚀 ¿En qué te ayuda este Sistema?
* **Automatización del Proceso de Biblioteca:** Elimina el uso de registros manuales en papel o archivos Excel desorganizados.
* **Control de Stock en Tiempo Real:** Garantiza que no se puedan realizar préstamos si no hay ejemplares físicos disponibles en la biblioteca.
* **Integridad de Transacciones (ACID):** El registro de préstamos trabaja de forma transaccional; si ocurre un error al registrar el detalle del préstamo o al actualizar el stock del libro, el sistema hace un *Rollback* automático dejando la base de datos intacta.
* **Seguridad de Acceso por Roles:** Restringe las opciones visibles del menú dependiendo de quién inicie sesión (por ejemplo, ocultando la pestaña de control de empleados a asistentes regulares).
* **Validaciones Robustas:** Cuenta con controles de entrada que validan en tiempo real los formatos de ISBN, el año de publicación (4 dígitos), stock numérico y evita el registro de datos duplicados.

---

## 👥 Roles y Credenciales de Acceso
El sistema evalúa el puesto del empleado registrado en la base de datos para asignar los privilegios correspondientes:
1. **Rol Administrador (Acceso Completo):**
   * **Usuario:** `admin` | **Contraseña:** `admin`
   * *Acciones:* Acceso completo a todas las pestañas (Usuarios, Empleados, Libros, Préstamos e Historial). Permite seleccionar dinámicamente qué empleado registra cada préstamo.
2. **Rol Empleado / Asistente (Acceso Restringido):**
   * **Usuario:** `luis` | **Contraseña:** `luis`
   * *Acciones:* La pestaña de "Empleados" se oculta. Al registrar un préstamo, su nombre queda bloqueado como gestor de la transacción para evitar suplantaciones.

---

## 💻 Instrucciones para Conectar y Ejecutar el Proyecto
Para visualizar y ejecutar la aplicación de forma local, sigue estos sencillos pasos:

### 1. Configurar la Base de Datos
1. Abre tu gestor de base de datos (por ejemplo, **MySQL Workbench**).
2. Abre y ejecuta el archivo de script SQL incluido en el proyecto: `db_biblioteca.sql`.
3. Esto creará automáticamente el esquema `db_biblioteca` y las tablas necesarias con datos de prueba iniciales.

### 2. Configurar la Conexión en NetBeans
1. Abre el proyecto `SistemaBiblioteca` en tu IDE **Apache NetBeans**.
2. Abre el archivo de conexión ubicado en la ruta: `src/main/java/conexion/Conexion.java`.
3. Configura tus datos locales de acceso a MySQL:
   ```java
   private static final String USER = "tu_usuario_mysql"; // Usualmente "root"
   private static final String PASSWORD = "tu_contraseña_mysql"; // Digita tu contraseña del servidor MySQL
   ```

### 3. Ejecutar la Aplicación
1. Compila y ejecuta el proyecto en NetBeans presionando **`F6`** (o haciendo clic derecho sobre el proyecto y seleccionando **Run**).
2. El sistema iniciará en la pantalla de Login lista para autenticar a los usuarios.
