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



\## Ejecución del proyecto

\### Desde IntelliJ IDEA

1\. Importar el proyecto como proyecto Maven.

2\. Esperar a que se descarguen las dependencias.

3\. Ejecutar la clase `MainApp`.



\### Desde línea de comandos

Situarse en la carpeta raíz del proyecto y ejecutar:

```bash

mvn clean javafx:run

