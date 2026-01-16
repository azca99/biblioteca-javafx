\# Biblioteca App – JavaFX + MariaDB



Aplicación de escritorio desarrollada en Java utilizando JavaFX y una base de datos MariaDB.



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

\- DBeaver

\- Un IDE compatible con Java (recomendado IntelliJ IDEA)



\## Base de datos

La aplicación utiliza una base de datos MariaDB llamada `biblioteca\_db`.



Antes de ejecutar la aplicación es necesario:

1\. Abrir DBeaver y conectarse a MariaDB

2\. Botón derecho sobre la conexión -> SQL Editor

3\. Abrir el archivo dump-biblioteca_db-202601151347.sql

4\. Ejecutar el script



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

En la carpeta \dist ejecutar el fichero run.bat