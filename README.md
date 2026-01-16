\# Biblioteca App – JavaFX + MariaDB



Aplicación de escritorio desarrollada en Java utilizando JavaFX y una base de datos MariaDB.

El proyecto forma parte de la asignatura Programación de un Grado Superior.



La aplicación permite gestionar una biblioteca mediante tres entidades principales:

\- Socios

\- Libros

\- Préstamos



\## Tecnologías utilizadas

\- Java

\- JavaFX

\- Maven

\- MariaDB

\- JDBC



\## Requisitos del sistema

Para ejecutar la aplicación es necesario disponer de:

\- JDK 17 o superior

\- Maven

\- MariaDB

\- Un IDE compatible con Java (recomendado IntelliJ IDEA)



\## Base de datos

La aplicación utiliza una base de datos MariaDB llamada `biblioteca\_db`.



Antes de ejecutar la aplicación es necesario:

1\. Crear la base de datos.

2\. Ejecutar el script SQL proporcionado (`biblioteca.sql`) para crear las tablas.



\## Configuración de la conexión

Los datos de conexión se encuentran en la clase:



`src/main/java/com/daniel/db/DB.java`



En esta clase se configuran:

\- URL de conexión

\- Usuario

\- Contraseña



Estos valores deben ajustarse según el entorno local del usuario.



\## Ejecución de la aplicación

\### Desde IntelliJ IDEA

1\. Abrir ventana de maven.

2\. Entrar en plugings.

3\. Ejecutar el plugin javafx:run.



\### Desde ejecutable run.bat

En la carpeta dist ejecutar el fichero run.bat