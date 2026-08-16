CREATE DATABASE db_biblioteca;
USE db_biblioteca;

CREATE TABLE USUARIO (
    ID_Usuario INT AUTO_INCREMENT PRIMARY KEY,
    Nombre VARCHAR(100) NOT NULL,
    Direccion VARCHAR(150) NULL,
    Telefono VARCHAR(20) NULL,
    Correo VARCHAR(100) NULL
);

CREATE TABLE EMPLEADO (
    ID_Empleado INT AUTO_INCREMENT PRIMARY KEY,
    Nombre VARCHAR(100) NOT NULL,
    Puesto VARCHAR(50) NULL,
    Usuario VARCHAR(50) UNIQUE NULL,
    Contrasena VARCHAR(50) NULL
);

CREATE TABLE LIBRO (
    ID_Libro INT AUTO_INCREMENT PRIMARY KEY,
    Titulo VARCHAR(150) NOT NULL,
    Autor VARCHAR(100) NOT NULL,
    Editorial VARCHAR(100) NULL,
    ISBN VARCHAR(20) NULL,
    Anio INT NULL,
    Stock INT NOT NULL DEFAULT 0
);

CREATE TABLE PRESTAMO (
    ID_Prestamo INT AUTO_INCREMENT PRIMARY KEY,
    ID_Usuario INT NOT NULL,
    ID_Empleado INT NOT NULL,
    Fecha_Salida DATE NOT NULL,
    Fecha_Devolucion_Maxima DATE NOT NULL,
    Estado VARCHAR(20) NOT NULL,
    CONSTRAINT FK_Prestamo_Usuario FOREIGN KEY (ID_Usuario) REFERENCES USUARIO(ID_Usuario) ON DELETE CASCADE,
    CONSTRAINT FK_Prestamo_Empleado FOREIGN KEY (ID_Empleado) REFERENCES EMPLEADO(ID_Empleado) ON DELETE CASCADE
);

CREATE TABLE PRESTAMO_DETALLE (
    ID_Prestamo INT NOT NULL,
    ID_Libro INT NOT NULL,
    CONSTRAINT PK_Prestamo_Detalle PRIMARY KEY (ID_Prestamo, ID_Libro),
    CONSTRAINT FK_Detalle_Prestamo FOREIGN KEY (ID_Prestamo) REFERENCES PRESTAMO(ID_Prestamo) ON DELETE CASCADE,
    CONSTRAINT FK_Detalle_Libro FOREIGN KEY (ID_Libro) REFERENCES LIBRO(ID_Libro) ON DELETE CASCADE
);

INSERT INTO USUARIO (Nombre, Direccion, Telefono, Correo) VALUES 
('Juan Perez Gomez', 'Av. Larco 456, Miraflores', '987654321', 'juan.perez@gmail.com'),
('Maria Rodriguez Diaz', 'Calle Los Pinos 123, San Isidro', '912345678', 'maria.rodriguez@outlook.com'),
('Carlos Mendoza Ruiz', 'Jr. Junin 789, Cercado de Lima', '945612378', 'carlos.mendoza@gmail.com');

INSERT INTO EMPLEADO (Nombre, Puesto, Usuario, Contrasena) VALUES 
('Ana Torres Vega', 'Bibliotecaria Principal', 'admin', 'admin'),
('Luis Rojas Soto', 'Asistente de Biblioteca', 'luis', 'luis');

INSERT INTO LIBRO (Titulo, Autor, Editorial, ISBN, Anio, Stock) VALUES 
('Cien Anios de Soledad', 'Gabriel Garcia Marquez', 'Editorial Sudamericana', '978-0307474728', 1967, 5),
('El Psicoanalista', 'John Katzenbach', 'Ediciones B', '978-8466618587', 2002, 3),
('Introduccion a Java', 'Deitel & Deitel', 'Pearson', '978-6073238168', 2016, 8),
('Clean Code', 'Robert C. Martin', 'Prentice Hall', '978-0132350884', 2008, 4);

INSERT INTO PRESTAMO (ID_Usuario, ID_Empleado, Fecha_Salida, Fecha_Devolucion_Maxima, Estado) VALUES 
(1, 1, '2026-06-15', '2026-06-22', 'Devuelto');

INSERT INTO PRESTAMO_DETALLE (ID_Prestamo, ID_Libro) VALUES 
(1, 1);
