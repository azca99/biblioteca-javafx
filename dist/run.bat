@echo off
set JAVA_HOME=E:\JDK.25
set PATH=%JAVA_HOME%\bin;%PATH%

java ^
--module-path lib ^
--add-modules javafx.controls,javafx.fxml ^
-cp "biblioteca-app-1.0-SNAPSHOT.jar;lib/*" ^
com.daniel.app.MainApp

pause
